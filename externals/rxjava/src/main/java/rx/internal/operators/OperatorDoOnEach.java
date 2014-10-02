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

import rx.Observable.Operator;
import rx.Observer;
import rx.Subscriber;
import rx.exceptions.OnErrorThrowable;

/**
 * Converts the elements of an observable sequence to the specified type.
 */
public class OperatorDoOnEach<T> implements Operator<T, T> {
    private final Observer<? super T> doOnEachObserver;

    public OperatorDoOnEach(Observer<? super T> doOnEachObserver) {
        this.doOnEachObserver = doOnEachObserver;
    }

    @Override
    public Subscriber<? super T> call(final Subscriber<? super T> observer) {
        return new Subscriber<T>(observer) {

            private boolean done = false;

            @Override
            public void onCompleted() {
                if (done) {
                    return;
                }
                try {
                    doOnEachObserver.onCompleted();
                } catch (Throwable e) {
                    onError(e);
                    return;
                }
                // Set `done` here so that the error in `doOnEachObserver.onCompleted()` can be noticed by observer
                done = true;
                observer.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                if (done) {
                    return;
                }
                done = true;
                try {
                    doOnEachObserver.onError(e);
                } catch (Throwable e2) {
                    observer.onError(e2);
                    return;
                }
                observer.onError(e);
            }

            @Override
            public void onNext(T value) {
                if (done) {
                    return;
                }
                try {
                    doOnEachObserver.onNext(value);
                } catch (Throwable e) {
                    onError(OnErrorThrowable.addValueAsLastCause(e, value));
                    return;
                }
                observer.onNext(value);
            }
        };
    }
}