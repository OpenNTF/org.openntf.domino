/**
 * Copyright 2014 Netflix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rx.subscriptions;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import rx.Observable;
import rx.Subscription;

/**
 * Subscription that can be checked for status such as in a loop inside an {@link Observable} to exit the loop
 * if unsubscribed.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.disposables.multipleassignmentdisposable">Rx.Net equivalent MultipleAssignmentDisposable</a>
 */
public final class MultipleAssignmentSubscription implements Subscription {
    /** The shared empty state. */
    static final State EMPTY_STATE = new State(false, Subscriptions.empty());
    volatile State state = EMPTY_STATE;
    static final AtomicReferenceFieldUpdater<MultipleAssignmentSubscription, State> STATE_UPDATER
            = AtomicReferenceFieldUpdater.newUpdater(MultipleAssignmentSubscription.class, State.class, "state");
    
    private static final class State {
        final boolean isUnsubscribed;
        final Subscription subscription;

        State(boolean u, Subscription s) {
            this.isUnsubscribed = u;
            this.subscription = s;
        }

        State unsubscribe() {
            return new State(true, subscription);
        }

        State set(Subscription s) {
            return new State(isUnsubscribed, s);
        }

    }
    @Override
    public boolean isUnsubscribed() {
        return state.isUnsubscribed;
    }

    @Override
    public void unsubscribe() {
        State oldState;
        State newState;
        do {
            oldState = state;
            if (oldState.isUnsubscribed) {
                return;
            } else {
                newState = oldState.unsubscribe();
            }
        } while (!STATE_UPDATER.compareAndSet(this, oldState, newState));
        oldState.subscription.unsubscribe();
    }

    /**
     * Sets the underlying subscription. If the {@code MultipleAssignmentSubscription} is already unsubscribed,
     * setting a new subscription causes the new subscription to also be immediately unsubscribed.
     *
     * @param s the {@link Subscription} to set
     * @throws IllegalArgumentException if {@code s} is {@code null}
     */
    public void set(Subscription s) {
        if (s == null) {
            throw new IllegalArgumentException("Subscription can not be null");
        }
        State oldState;
        State newState;
        do {
            oldState = state;
            if (oldState.isUnsubscribed) {
                s.unsubscribe();
                return;
            } else {
                newState = oldState.set(s);
            }
        } while (!STATE_UPDATER.compareAndSet(this, oldState, newState));
    }

    /**
     * Gets the underlying subscription.
     *
     * @return the {@link Subscription} that underlies the {@code MultipleAssignmentSubscription}
     */
    public Subscription get() {
        return state.subscription;
    }

}
