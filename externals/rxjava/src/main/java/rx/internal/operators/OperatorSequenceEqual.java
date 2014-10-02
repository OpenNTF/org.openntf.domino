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

import static rx.Observable.concat;
import static rx.Observable.from;
import static rx.Observable.zip;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Functions;

/**
 * Returns an {@link Observable} that emits a single {@code Boolean} value that indicates whether two source
 * {@code Observable}s emit sequences of items that are equivalent to each other.
 */
public final class OperatorSequenceEqual {
    private OperatorSequenceEqual() {
        throw new IllegalStateException("No instances!");
    }

    /** NotificationLite doesn't work as zip uses it. */
    private static final Object LOCAL_ONCOMPLETED = new Object();
    static <T> Observable<Object> materializeLite(Observable<T> source) {
        return concat(
                source.map(new Func1<T, Object>() {

                    @Override
                    public Object call(T t1) {
                        return t1;
                    }

                }), from(LOCAL_ONCOMPLETED));
    }

    /**
     * Tests whether two {@code Observable} sequences are identical, emitting {@code true} if both sequences
     * complete without differing, and {@code false} if the two sequences diverge at any point.
     *
     * @param first
     *      the first of the two {@code Observable}s to compare
     * @param second
     *      the second of the two {@code Observable}s to compare
     * @param equality
     *      a function that tests emissions from each {@code Observable} for equality
     * @return an {@code Observable} that emits {@code true} if {@code first} and {@code second} complete
     *         after emitting equal sequences of items, {@code false} if at any point in their sequences the
     *         two {@code Observable}s emit a non-equal item.
     */
    public static <T> Observable<Boolean> sequenceEqual(
            Observable<? extends T> first, Observable<? extends T> second,
            final Func2<? super T, ? super T, Boolean> equality) {
        Observable<Object> firstObservable = materializeLite(first);
        Observable<Object> secondObservable = materializeLite(second);

        return zip(firstObservable, secondObservable,
                new Func2<Object, Object, Boolean>() {

                    @Override
                    @SuppressWarnings("unchecked")
                    public Boolean call(Object t1, Object t2) {
                        boolean c1 = t1 == LOCAL_ONCOMPLETED;
                        boolean c2 = t2 == LOCAL_ONCOMPLETED;
                        if (c1 && c2) {
                            return true;
                        }
                        if (c1 || c2) {
                            return false;
                        }
                        // Now t1 and t2 must be 'onNext'.
                        return equality.call((T)t1, (T)t2);
                    }

                }).all(Functions.<Boolean> identity());
    }
}
