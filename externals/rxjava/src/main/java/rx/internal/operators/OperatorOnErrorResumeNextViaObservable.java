/**
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rx.internal.operators;

import rx.Observable;
import rx.Observable.Operator;
import rx.Subscriber;
import rx.plugins.RxJavaPlugins;

/**
 * Instruct an Observable to pass control to another Observable rather than invoking
 * <code>onError</code> if it encounters an error.
 * <p>
 * <img width="640" src="https://github.com/Netflix/RxJava/wiki/images/rx-operators/onErrorResumeNext.png" alt="">
 * <p>
 * By default, when an Observable encounters an error that prevents it from emitting the expected item to its
 * Observer, the Observable invokes its Observer's {@code onError} method, and then quits without invoking any
 * more of its Observer's methods. The {@code onErrorResumeNext} operation changes this behavior. If you pass
 * an Observable ({@code resumeSequence}) to {@code onErrorResumeNext}, if the source Observable encounters an
 * error, instead of invoking its Observer's {@code onError} method, it will instead relinquish control to this
 * new Observable, which will invoke the Observer's {@code onNext} method if it is able to do so. In such a
 * case, because no Observable necessarily invokes {@code onError}, the Observer may never know that an error
 * happened.
 * <p>
 * You can use this to prevent errors from propagating or to supply fallback data should errors be
 * encountered.
 * 
 * @param <T> the value type
 */
public final class OperatorOnErrorResumeNextViaObservable<T> implements Operator<T, T> {
    final Observable<? extends T> resumeSequence;

    public OperatorOnErrorResumeNextViaObservable(Observable<? extends T> resumeSequence) {
        this.resumeSequence = resumeSequence;
    }

    @Override
    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        // shared subscription won't work here
        Subscriber<T> s = new Subscriber<T>() {
            @Override
            public void onNext(T t) {
                child.onNext(t);
            }

            @Override
            public void onError(Throwable e) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                unsubscribe();
                resumeSequence.unsafeSubscribe(child);
            }

            @Override
            public void onCompleted() {
                child.onCompleted();
            }
            
        };
        child.add(s);
        
        return s;
    }
    
}
