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
import rx.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Returns an {@code Observable} that emits a single item, a list composed of all the items emitted by the
 * source {@code Observable}.
 * <p>
 * <img width="640" height="305" src="https://raw.githubusercontent.com/wiki/Netflix/RxJava/images/rx-operators/toList.png" alt="">
 * <p>
 * Normally, an {@code Observable} that returns multiple items will do so by invoking its subscriber's
 * {@link Subscriber#onNext onNext} method for each such item. You can change this behavior, instructing the
 * {@code Observable} to compose a list of all of these multiple items and then to invoke the subscriber's
 * {@code onNext} method once, passing it the entire list, by using this operator.
 * <p>
 * Be careful not to use this operator on {@code Observable}s that emit infinite or very large numbers of items,
 * as you do not have the option to unsubscribe.
 */
public final class OperatorToObservableList<T> implements Operator<List<T>, T> {

    @Override
    public Subscriber<? super T> call(final Subscriber<? super List<T>> o) {
        return new Subscriber<T>(o) {

            private boolean completed = false;
            final List<T> list = new LinkedList<T>();

            @Override
            public void onStart() {
                request(Long.MAX_VALUE);
            }

            @Override
            public void onCompleted() {
                try {
                    completed = true;
                    /*
                     * Ideally this should just return Collections.unmodifiableList(list) and not copy it, 
                     * but, it ends up being a breaking change if we make that modification. 
                     * 
                     * Here is an example of is being done with these lists that breaks if we make it immutable:
                     * 
                     * Caused by: java.lang.UnsupportedOperationException
                     *     at java.util.Collections$UnmodifiableList$1.set(Collections.java:1244)
                     *     at java.util.Collections.sort(Collections.java:221)
                     *     ...
                     * Caused by: rx.exceptions.OnErrorThrowable$OnNextValue: OnError while emitting onNext value: UnmodifiableList.class
                     *     at rx.exceptions.OnErrorThrowable.addValueAsLastCause(OnErrorThrowable.java:98)
                     *     at rx.internal.operators.OperatorMap$1.onNext(OperatorMap.java:56)
                     *     ... 419 more
                     */
                    o.onNext(new ArrayList<T>(list));
                    o.onCompleted();
                } catch (Throwable e) {
                    onError(e);
                }
            }

            @Override
            public void onError(Throwable e) {
                o.onError(e);
            }

            @Override
            public void onNext(T value) {
                if (!completed) {
                    list.add(value);
                }
            }

        };
    }

}
