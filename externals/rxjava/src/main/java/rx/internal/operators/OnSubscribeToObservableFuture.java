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
package rx.internal.operators;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Converts a {@code Future} into an {@code Observable}.
 * <p>
 * <img width="640" src="https://github.com/Netflix/RxJava/wiki/images/rx-operators/from.Future.png" alt="">
 * <p>
 * You can convert any object that supports the {@code Future} interface into an {@code Observable} that emits
 * the return value of the {@code get} method of that object, by using this operator.
 * <p>
 * This is blocking so the {@code Subscription} returned when calling
 * {@code Observable.unsafeSubscribe(Observer)} does nothing.
 */
public class OnSubscribeToObservableFuture {
    /* package accessible for unit tests */static class ToObservableFuture<T> implements OnSubscribe<T> {
        private final Future<? extends T> that;
        private final long time;
        private final TimeUnit unit;

        public ToObservableFuture(Future<? extends T> that) {
            this.that = that;
            this.time = 0;
            this.unit = null;
        }

        public ToObservableFuture(Future<? extends T> that, long time, TimeUnit unit) {
            this.that = that;
            this.time = time;
            this.unit = unit;
        }

        @Override
        public void call(Subscriber<? super T> subscriber) {
            subscriber.add(Subscriptions.create(new Action0() {
                @Override
                public void call() {
                    // If the Future is already completed, "cancel" does nothing.
                    that.cancel(true);
                }
            }));
            try {
                //don't block or propagate CancellationException if already unsubscribed
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                T value = (unit == null) ? (T) that.get() : (T) that.get(time, unit);
                subscriber.onNext(value);
                subscriber.onCompleted();
            } catch (Throwable e) {
                // If this Observable is unsubscribed, we will receive an CancellationException.
                // However, CancellationException will not be passed to the final Subscriber
                // since it's already subscribed.
                // If the Future is canceled in other place, CancellationException will be still
                // passed to the final Subscriber.
                if (subscriber.isUnsubscribed()) {
                    //refuse to emit onError if already unsubscribed
                    return;
                }
                subscriber.onError(e);
            }
        }
    }

    public static <T> OnSubscribe<T> toObservableFuture(final Future<? extends T> that) {
        return new ToObservableFuture<T>(that);
    }

    public static <T> OnSubscribe<T> toObservableFuture(final Future<? extends T> that, long time, TimeUnit unit) {
        return new ToObservableFuture<T>(that, time, unit);
    }
}
