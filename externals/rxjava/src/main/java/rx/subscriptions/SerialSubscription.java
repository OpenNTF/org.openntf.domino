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

import rx.Subscription;

/**
 * Represents a subscription whose underlying subscription can be swapped for another subscription which causes
 * the previous underlying subscription to be unsubscribed.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.disposables.serialdisposable(v=vs.103).aspx">Rx.Net equivalent SerialDisposable</a>
 */
public final class SerialSubscription implements Subscription {
    static final State EMPTY_STATE = new State(false, Subscriptions.empty());
    volatile State state = EMPTY_STATE;
    static final AtomicReferenceFieldUpdater<SerialSubscription, State> STATE_UPDATER
            = AtomicReferenceFieldUpdater.newUpdater(SerialSubscription.class, State.class, "state");

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
     * Swaps out the old {@link Subscription} for the specified {@code Subscription}.
     *
     * @param s
     *          the new {@code Subscription} to swap in
     * @throws IllegalArgumentException
     *          if {@code s} is {@code null}
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
        oldState.subscription.unsubscribe();
    }

    /**
     * Retrieves the current {@link Subscription} that is being represented by this {@code SerialSubscription}.
     * 
     * @return the current {@link Subscription} that is being represented by this {@code SerialSubscription}
     */
    public Subscription get() {
        return state.subscription;
    }

}
