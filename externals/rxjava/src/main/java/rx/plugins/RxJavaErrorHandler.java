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
package rx.plugins;

import rx.Observable;
import rx.Subscriber;

/**
 * Abstract class for defining error handling logic in addition to the normal
 * {@link Subscriber#onError(Throwable)} behavior.
 * <p>
 * For example, all {@code Exception}s can be logged using this handler even if
 * {@link Subscriber#onError(Throwable)} is ignored or not provided when an {@link Observable} is subscribed to.
 * <p>
 * See {@link RxJavaPlugins} or the RxJava GitHub Wiki for information on configuring plugins: <a
 * href="https://github.com/Netflix/RxJava/wiki/Plugins">https://github.com/Netflix/RxJava/wiki/Plugins</a>.
 */
public abstract class RxJavaErrorHandler {

    /**
     * Receives all {@code Exception}s from an {@link Observable} passed to
     * {@link Subscriber#onError(Throwable)}.
     * <p>
     * This should <em>never</em> throw an {@code Exception}. Make sure to try/catch({@code Throwable}) all code
     * inside this method implementation.
     * 
     * @param e
     *            the {@code Exception}
     */
    public void handleError(Throwable e) {
        // do nothing by default
    }

}
