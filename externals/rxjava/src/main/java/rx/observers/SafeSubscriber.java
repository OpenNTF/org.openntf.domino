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
package rx.observers;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorFailedException;
import rx.exceptions.OnErrorNotImplementedException;
import rx.plugins.RxJavaPlugins;

/**
 * Wrapper around {@code Observer} that ensures compliance with the Rx contract.
 * <p>
 * The following is taken from <a href="http://go.microsoft.com/fwlink/?LinkID=205219">the Rx Design Guidelines
 * document</a>:
 * <blockquote><p>
 * Messages sent to instances of the {@code IObserver} interface follow the following grammar:
 * </p><blockquote><p> {@code OnNext* (OnCompleted | OnError)?} </p></blockquote><p>
 * This grammar allows observable sequences to send any amount (0 or more) of {@code OnNext} messages to the
 * subscribed observer instance, optionally followed by a single success ({@code OnCompleted}) or failure
 * ({@code OnError}) message.
 * </p><p>
 * The single message indicating that an observable sequence has finished ensures that consumers of the
 * observable sequence can deterministically establish that it is safe to perform cleanup operations.
 * </p><p>
 * A single failure further ensures that abort semantics can be maintained for operators that work on
 * multiple observable sequences (see paragraph 6.6).
 * </p></blockquote>
 * <p>
 * This wrapper does the following:
 * <ul>
 * <li>Allows only single execution of either {@code onError} or {@code onCompleted}.</li>
 * <li>Ensures that once an {@code onCompleted} or {@code onError} is performed, no further calls can be executed</li>
 * <li>If {@code unsubscribe} is called, calls {@code completed} and forbids any further {@code onNext} calls.</li>
 * <li>When {@code onError} or {@code onComplete} occur, unsubscribes from the {@code Observable} (if executing asynchronously).</li>
 * </ul>
 * <p> {@code SafeSubscriber} will not synchronize {@code onNext} execution. Use {@link SerializedSubscriber} to do
 * that.
 * 
 * @param <T>
 *            the type of item expected by the {@link Subscriber}
 */
public class SafeSubscriber<T> extends Subscriber<T> {

    private final Subscriber<? super T> actual;

    boolean done = false;

    public SafeSubscriber(Subscriber<? super T> actual) {
        super(actual);
        this.actual = actual;
    }

    @Override
    public void onCompleted() {
        if (!done) {
            done = true;
            try {
                actual.onCompleted();
            } catch (Throwable e) {
                // we handle here instead of another method so we don't add stacks to the frame
                // which can prevent it from being able to handle StackOverflow
                Exceptions.throwIfFatal(e);
                // handle errors if the onCompleted implementation fails, not just if the Observable fails
                _onError(e);
            } finally {
                // auto-unsubscribe
                unsubscribe();
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        // we handle here instead of another method so we don't add stacks to the frame
        // which can prevent it from being able to handle StackOverflow
        Exceptions.throwIfFatal(e);
        if (!done) {
            done = true;
            _onError(e);
        }
    }

    @Override
    public void onNext(T args) {
        try {
            if (!done) {
                actual.onNext(args);
            }
        } catch (Throwable e) {
            // we handle here instead of another method so we don't add stacks to the frame
            // which can prevent it from being able to handle StackOverflow
            Exceptions.throwIfFatal(e);
            // handle errors if the onNext implementation fails, not just if the Observable fails
            onError(e);
        }
    }

    /**
     * The logic for {@code onError} without the {@code isFinished} check so it can be called from within {@code onCompleted}.
     * 
     * @see <a href="https://github.com/Netflix/RxJava/issues/630">the report of this bug</a>
     */
    protected void _onError(Throwable e) {
        try {
            RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
        } catch (Throwable pluginException) {
            handlePluginException(pluginException);
        }
        try {
            actual.onError(e);
        } catch (Throwable e2) {
            if (e2 instanceof OnErrorNotImplementedException) {
                /*
                 * onError isn't implemented so throw
                 * 
                 * https://github.com/Netflix/RxJava/issues/198
                 * 
                 * Rx Design Guidelines 5.2
                 * 
                 * "when calling the Subscribe method that only has an onNext argument, the OnError behavior
                 * will be to rethrow the exception on the thread that the message comes out from the observable
                 * sequence. The OnCompleted behavior in this case is to do nothing."
                 */
                try {
                    unsubscribe();
                } catch (Throwable unsubscribeException) {
                    try {
                        RxJavaPlugins.getInstance().getErrorHandler().handleError(unsubscribeException);
                    } catch (Throwable pluginException) {
                        handlePluginException(pluginException);
                    }
                    throw new RuntimeException("Observer.onError not implemented and error while unsubscribing.", new CompositeException(Arrays.asList(e, unsubscribeException)));
                }
                throw (OnErrorNotImplementedException) e2;
            } else {
                /*
                 * throw since the Rx contract is broken if onError failed
                 * 
                 * https://github.com/Netflix/RxJava/issues/198
                 */
                try {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(e2);
                } catch (Throwable pluginException) {
                    handlePluginException(pluginException);
                }
                try {
                    unsubscribe();
                } catch (Throwable unsubscribeException) {
                    try {
                        RxJavaPlugins.getInstance().getErrorHandler().handleError(unsubscribeException);
                    } catch (Throwable pluginException) {
                        handlePluginException(pluginException);
                    }
                    throw new OnErrorFailedException("Error occurred when trying to propagate error to Observer.onError and during unsubscription.", new CompositeException(Arrays.asList(e, e2, unsubscribeException)));
                }

                throw new OnErrorFailedException("Error occurred when trying to propagate error to Observer.onError", new CompositeException(Arrays.asList(e, e2)));
            }
        }
        // if we did not throw above we will unsubscribe here, if onError failed then unsubscribe happens in the catch
        try {
            unsubscribe();
        } catch (RuntimeException unsubscribeException) {
            try {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(unsubscribeException);
            } catch (Throwable pluginException) {
                handlePluginException(pluginException);
            }
            throw new OnErrorFailedException(unsubscribeException);
        }
    }

    private void handlePluginException(Throwable pluginException) {
        /*
         * We don't want errors from the plugin to affect normal flow.
         * Since the plugin should never throw this is a safety net
         * and will complain loudly to System.err so it gets fixed.
         */
        System.err.println("RxJavaErrorHandler threw an Exception. It shouldn't. => " + pluginException.getMessage());
        pluginException.printStackTrace();
    }

    /**
     * Returns the {@link Subscriber} underlying this {@code SafeSubscriber}.
     *
     * @return the {@link Subscriber} that was used to create this {@code SafeSubscriber}
     */
    public Subscriber<? super T> getActual() {
        return actual;
    }
}
