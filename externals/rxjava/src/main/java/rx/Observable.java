/**
 * Copyright 2014 Netflix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package rx;

import static rx.functions.Functions.alwaysFalse;

import java.util.*;
import java.util.concurrent.*;

import rx.exceptions.*;
import rx.functions.*;
import rx.internal.operators.*;
import rx.internal.util.ScalarSynchronousObservable;
import rx.observables.*;
import rx.observers.SafeSubscriber;
import rx.plugins.*;
import rx.schedulers.*;
import rx.subjects.*;
import rx.subscriptions.Subscriptions;

/**
 * The Observable class that implements the Reactive Pattern.
 * <p>
 * This class provides methods for subscribing to the Observable as well as delegate methods to the various
 * Observers.
 * <p>
 * The documentation for this class makes use of marble diagrams. The following legend explains these diagrams:
 * <p>
 * <img width="640" height="301" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/legend.png" alt="">
 * <p>
 * For more information see the <a href="https://github.com/Netflix/RxJava/wiki/Observable">RxJava wiki</a>
 * 
 * @param <T>
 *            the type of the items emitted by the Observable
 */
public class Observable<T> {

    final OnSubscribe<T> onSubscribe;

    /**
     * Creates an Observable with a Function to execute when it is subscribed to.
     * <p>
     * <em>Note:</em> Use {@link #create(OnSubscribe)} to create an Observable, instead of this constructor,
     * unless you specifically have a need for inheritance.
     * 
     * @param f
     *            {@link OnSubscribe} to be executed when {@link #subscribe(Subscriber)} is called
     */
    protected Observable(OnSubscribe<T> f) {
        this.onSubscribe = f;
    }

    private static final RxJavaObservableExecutionHook hook = RxJavaPlugins.getInstance().getObservableExecutionHook();

    /**
     * Returns an Observable that will execute the specified function when a {@link Subscriber} subscribes to
     * it.
     * <p>
     * <img width="640" height="200" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/create.png" alt="">
     * <p>
     * Write the function you pass to {@code create} so that it behaves as an Observable: It should invoke the
     * Subscriber's {@link Subscriber#onNext onNext}, {@link Subscriber#onError onError}, and
     * {@link Subscriber#onCompleted onCompleted} methods appropriately.
     * <p>
     * A well-formed Observable must invoke either the Subscriber's {@code onCompleted} method exactly once or
     * its {@code onError} method exactly once.
     * <p>
     * See <a href="http://go.microsoft.com/fwlink/?LinkID=205219">Rx Design Guidelines (PDF)</a> for detailed
     * information.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <T>
     *            the type of the items that this Observable emits
     * @param f
     *            a function that accepts an {@code Subscriber<T>}, and invokes its {@code onNext},
     *            {@code onError}, and {@code onCompleted} methods as appropriate
     * @return an Observable that, when a {@link Subscriber} subscribes to it, will execute the specified
     *         function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#create">RxJava wiki: create</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.create.aspx">MSDN: Observable.Create</a>
     */
    public final static <T> Observable<T> create(OnSubscribe<T> f) {
        return new Observable<T>(hook.onCreate(f));
    }

    /**
     * Invoked when Obserable.subscribe is called.
     */
    public static interface OnSubscribe<T> extends Action1<Subscriber<? super T>> {
        // cover for generics insanity
    }

    /**
     * Operator function for lifting into an Observable.
     */
    public interface Operator<R, T> extends Func1<Subscriber<? super R>, Subscriber<? super T>> {
        // cover for generics insanity
    }

    /**
     * @deprecated use {@link #create(OnSubscribe)}
     */
    @Deprecated
    public final static <T> Observable<T> create(final OnSubscribeFunc<T> f) {
        return new Observable<T>(new OnSubscribe<T>() {

            @Override
            public void call(Subscriber<? super T> observer) {
                Subscription s = f.onSubscribe(observer);
                if (s != null && s != observer) {
                    observer.add(s);
                }
            }

        });
    }

    /**
     * @deprecated use {@link OnSubscribe}
     */
    @Deprecated
    public static interface OnSubscribeFunc<T> extends Function {

        public Subscription onSubscribe(Observer<? super T> op);

    }

    /**
     * Lifts a function to the current Observable and returns a new Observable that when subscribed to will pass
     * the values of the current Observable through the Operator function.
     * <p>
     * In other words, this allows chaining Observers together on an Observable for acting on the values within
     * the Observable.
     * <p> {@code
     * observable.map(...).filter(...).take(5).lift(new OperatorA()).lift(new OperatorB(...)).subscribe()
     * }
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code lift} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param lift the Operator that implements the Observable-operating function to be applied to the source
     *             Observable
     * @return an Observable that is the result of applying the lifted Operator to the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Implementing-Your-Own-Operators">RxJava wiki: Implementing Your Own Operators</a>
     * @since 0.17
     */
    public final <R> Observable<R> lift(final Operator<? extends R, ? super T> lift) {
        return new Observable<R>(new OnSubscribe<R>() {
            @Override
            public void call(Subscriber<? super R> o) {
                try {
                    Subscriber<? super T> st = hook.onLift(lift).call(o);
                    // new Subscriber created and being subscribed with so 'onStart' it
                    st.onStart();
                    onSubscribe.call(st);
                } catch (Throwable e) {
                    // localized capture of errors rather than it skipping all operators 
                    // and ending up in the try/catch of the subscribe method which then
                    // prevents onErrorResumeNext and other similar approaches to error handling
                    if (e instanceof OnErrorNotImplementedException) {
                        throw (OnErrorNotImplementedException) e;
                    }
                    o.onError(e);
                }
            }
        });
    }

    /* *********************************************************************************************************
     * Observers Below Here
     * *********************************************************************************************************
     */

    /**
     * Mirrors the one Observable in an Iterable of several Observables that first emits an item.
     * <p>
     * <img width="640" height="385" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/amb.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code amb} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param sources
     *            an Iterable of Observable sources competing to react first
     * @return an Observable that emits the same sequence of items as whichever of the source Observables first
     *         emitted an item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#amb">RxJava wiki: amb</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229115.aspx">MSDN: Observable.Amb</a>
     */
    public final static <T> Observable<T> amb(Iterable<? extends Observable<? extends T>> sources) {
        return create(OnSubscribeAmb.amb(sources));
    }

    /**
     * Given two Observables, mirrors the one that first emits an item.
     * <p>
     * <img width="640" height="385" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/amb.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code amb} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            an Observable competing to react first
     * @param o2
     *            an Observable competing to react first
     * @return an Observable that emits the same sequence of items as whichever of the source Observables first
     *         emitted an item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#amb">RxJava wiki: amb</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229733.aspx">MSDN: Observable.Amb</a>
     */
    public final static <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2) {
        return create(OnSubscribeAmb.amb(o1, o2));
    }

    /**
     * Given three Observables, mirrors the one that first emits an item.
     * <p>
     * <img width="640" height="385" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/amb.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code amb} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            an Observable competing to react first
     * @param o2
     *            an Observable competing to react first
     * @param o3
     *            an Observable competing to react first
     * @return an Observable that emits the same sequence of items as whichever of the source Observables first
     *         emitted an item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#amb">RxJava wiki: amb</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229733.aspx">MSDN: Observable.Amb</a>
     */
    public final static <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3) {
        return create(OnSubscribeAmb.amb(o1, o2, o3));
    }

    /**
     * Given four Observables, mirrors the one that first emits an item.
     * <p>
     * <img width="640" height="385" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/amb.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code amb} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            an Observable competing to react first
     * @param o2
     *            an Observable competing to react first
     * @param o3
     *            an Observable competing to react first
     * @param o4
     *            an Observable competing to react first
     * @return an Observable that emits the same sequence of items as whichever of the source Observables first
     *         emitted an item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#amb">RxJava wiki: amb</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229733.aspx">MSDN: Observable.Amb</a>
     */
    public final static <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4));
    }

    /**
     * Given five Observables, mirrors the one that first emits an item.
     * <p>
     * <img width="640" height="385" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/amb.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code amb} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param o1
     *            an Observable competing to react first
     * @param o2
     *            an Observable competing to react first
     * @param o3
     *            an Observable competing to react first
     * @param o4
     *            an Observable competing to react first
     * @param o5
     *            an Observable competing to react first
     * @return an Observable that emits the same sequence of items as whichever of the source Observables first
     *         emitted an item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#amb">RxJava wiki: amb</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229733.aspx">MSDN: Observable.Amb</a>
     */
    public final static <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5));
    }

    /**
     * Given six Observables, mirrors the one that first emits an item.
     * <p>
     * <img width="640" height="385" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/amb.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code amb} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            an Observable competing to react first
     * @param o2
     *            an Observable competing to react first
     * @param o3
     *            an Observable competing to react first
     * @param o4
     *            an Observable competing to react first
     * @param o5
     *            an Observable competing to react first
     * @param o6
     *            an Observable competing to react first
     * @return an Observable that emits the same sequence of items as whichever of the source Observables first
     *         emitted an item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#amb">RxJava wiki: amb</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229733.aspx">MSDN: Observable.Amb</a>
     */
    public final static <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5, o6));
    }

    /**
     * Given seven Observables, mirrors the one that first emits an item.
     * <p>
     * <img width="640" height="385" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/amb.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code amb} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param o1
     *            an Observable competing to react first
     * @param o2
     *            an Observable competing to react first
     * @param o3
     *            an Observable competing to react first
     * @param o4
     *            an Observable competing to react first
     * @param o5
     *            an Observable competing to react first
     * @param o6
     *            an Observable competing to react first
     * @param o7
     *            an Observable competing to react first
     * @return an Observable that emits the same sequence of items as whichever of the source Observables first
     *         emitted an item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#amb">RxJava wiki: amb</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229733.aspx">MSDN: Observable.Amb</a>
     */
    public final static <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5, o6, o7));
    }

    /**
     * Given eight Observables, mirrors the one that first emits an item.
     * <p>
     * <img width="640" height="385" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/amb.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code amb} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param o1
     *            an Observable competing to react first
     * @param o2
     *            an Observable competing to react first
     * @param o3
     *            an Observable competing to react first
     * @param o4
     *            an Observable competing to react first
     * @param o5
     *            an Observable competing to react first
     * @param o6
     *            an Observable competing to react first
     * @param o7
     *            an Observable competing to react first
     * @param o8
     *            an observable competing to react first
     * @return an Observable that emits the same sequence of items as whichever of the source Observables first
     *         emitted an item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#amb">RxJava wiki: amb</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229733.aspx">MSDN: Observable.Amb</a>
     */
    public final static <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5, o6, o7, o8));
    }

    /**
     * Given nine Observables, mirrors the one that first emits an item.
     * <p>
     * <img width="640" height="385" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/amb.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code amb} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            an Observable competing to react first
     * @param o2
     *            an Observable competing to react first
     * @param o3
     *            an Observable competing to react first
     * @param o4
     *            an Observable competing to react first
     * @param o5
     *            an Observable competing to react first
     * @param o6
     *            an Observable competing to react first
     * @param o7
     *            an Observable competing to react first
     * @param o8
     *            an Observable competing to react first
     * @param o9
     *            an Observable competing to react first
     * @return an Observable that emits the same sequence of items as whichever of the source Observables first
     *         emitted an item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#amb">RxJava wiki: amb</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229733.aspx">MSDN: Observable.Amb</a>
     */
    public final static <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8, Observable<? extends T> o9) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5, o6, o7, o8, o9));
    }

    /**
     * Combines two source Observables by emitting an item that aggregates the latest values of each of the
     * source Observables each time an item is received from either of the source Observables, where this
     * aggregation is defined by a specified function.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/combineLatest.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code combineLatest} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param o1
     *            the first source Observable
     * @param o2
     *            the second source Observable
     * @param combineFunction
     *            the aggregation function used to combine the items emitted by the source Observables
     * @return an Observable that emits items that are the result of combining the items emitted by the source
     *         Observables by means of the given aggregation function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#combinelatest">RxJava wiki: combineLatest</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211991.aspx">MSDN: Observable.CombineLatest</a>
     */
    @SuppressWarnings("unchecked")
    public static final <T1, T2, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Func2<? super T1, ? super T2, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2), Functions.fromFunc(combineFunction));
    }

    /**
     * Combines three source Observables by emitting an item that aggregates the latest values of each of the
     * source Observables each time an item is received from any of the source Observables, where this
     * aggregation is defined by a specified function.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/combineLatest.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code combineLatest} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            the first source Observable
     * @param o2
     *            the second source Observable
     * @param o3
     *            the third source Observable
     * @param combineFunction
     *            the aggregation function used to combine the items emitted by the source Observables
     * @return an Observable that emits items that are the result of combining the items emitted by the source
     *         Observables by means of the given aggregation function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#combinelatest">RxJava wiki: combineLatest</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211991.aspx">MSDN: Observable.CombineLatest</a>
     */
    @SuppressWarnings("unchecked")
    public static final <T1, T2, T3, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Func3<? super T1, ? super T2, ? super T3, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3), Functions.fromFunc(combineFunction));
    }

    /**
     * Combines four source Observables by emitting an item that aggregates the latest values of each of the
     * source Observables each time an item is received from any of the source Observables, where this
     * aggregation is defined by a specified function.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/combineLatest.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code combineLatest} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            the first source Observable
     * @param o2
     *            the second source Observable
     * @param o3
     *            the third source Observable
     * @param o4
     *            the fourth source Observable
     * @param combineFunction
     *            the aggregation function used to combine the items emitted by the source Observables
     * @return an Observable that emits items that are the result of combining the items emitted by the source
     *         Observables by means of the given aggregation function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#combinelatest">RxJava wiki: combineLatest</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211991.aspx">MSDN: Observable.CombineLatest</a>
     */
    @SuppressWarnings("unchecked")
    public static final <T1, T2, T3, T4, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4,
            Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4), Functions.fromFunc(combineFunction));
    }

    /**
     * Combines five source Observables by emitting an item that aggregates the latest values of each of the
     * source Observables each time an item is received from any of the source Observables, where this
     * aggregation is defined by a specified function.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/combineLatest.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code combineLatest} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            the first source Observable
     * @param o2
     *            the second source Observable
     * @param o3
     *            the third source Observable
     * @param o4
     *            the fourth source Observable
     * @param o5
     *            the fifth source Observable
     * @param combineFunction
     *            the aggregation function used to combine the items emitted by the source Observables
     * @return an Observable that emits items that are the result of combining the items emitted by the source
     *         Observables by means of the given aggregation function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#combinelatest">RxJava wiki: combineLatest</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211991.aspx">MSDN: Observable.CombineLatest</a>
     */
    @SuppressWarnings("unchecked")
    public static final <T1, T2, T3, T4, T5, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5,
            Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4, o5), Functions.fromFunc(combineFunction));
    }

    /**
     * Combines six source Observables by emitting an item that aggregates the latest values of each of the
     * source Observables each time an item is received from any of the source Observables, where this
     * aggregation is defined by a specified function.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/combineLatest.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code combineLatest} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            the first source Observable
     * @param o2
     *            the second source Observable
     * @param o3
     *            the third source Observable
     * @param o4
     *            the fourth source Observable
     * @param o5
     *            the fifth source Observable
     * @param o6
     *            the sixth source Observable
     * @param combineFunction
     *            the aggregation function used to combine the items emitted by the source Observables
     * @return an Observable that emits items that are the result of combining the items emitted by the source
     *         Observables by means of the given aggregation function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#combinelatest">RxJava wiki: combineLatest</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211991.aspx">MSDN: Observable.CombineLatest</a>
     */
    @SuppressWarnings("unchecked")
    public static final <T1, T2, T3, T4, T5, T6, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6,
            Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4, o5, o6), Functions.fromFunc(combineFunction));
    }

    /**
     * Combines seven source Observables by emitting an item that aggregates the latest values of each of the
     * source Observables each time an item is received from any of the source Observables, where this
     * aggregation is defined by a specified function.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/combineLatest.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code combineLatest} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            the first source Observable
     * @param o2
     *            the second source Observable
     * @param o3
     *            the third source Observable
     * @param o4
     *            the fourth source Observable
     * @param o5
     *            the fifth source Observable
     * @param o6
     *            the sixth source Observable
     * @param o7
     *            the seventh source Observable
     * @param combineFunction
     *            the aggregation function used to combine the items emitted by the source Observables
     * @return an Observable that emits items that are the result of combining the items emitted by the source
     *         Observables by means of the given aggregation function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#combinelatest">RxJava wiki: combineLatest</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211991.aspx">MSDN: Observable.CombineLatest</a>
     */
    @SuppressWarnings("unchecked")
    public static final <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7,
            Func7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4, o5, o6, o7), Functions.fromFunc(combineFunction));
    }

    /**
     * Combines eight source Observables by emitting an item that aggregates the latest values of each of the
     * source Observables each time an item is received from any of the source Observables, where this
     * aggregation is defined by a specified function.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/combineLatest.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code combineLatest} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            the first source Observable
     * @param o2
     *            the second source Observable
     * @param o3
     *            the third source Observable
     * @param o4
     *            the fourth source Observable
     * @param o5
     *            the fifth source Observable
     * @param o6
     *            the sixth source Observable
     * @param o7
     *            the seventh source Observable
     * @param o8
     *            the eighth source Observable
     * @param combineFunction
     *            the aggregation function used to combine the items emitted by the source Observables
     * @return an Observable that emits items that are the result of combining the items emitted by the source
     *         Observables by means of the given aggregation function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#combinelatest">RxJava wiki: combineLatest</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211991.aspx">MSDN: Observable.CombineLatest</a>
     */
    @SuppressWarnings("unchecked")
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Observable<? extends T8> o8,
            Func8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8), Functions.fromFunc(combineFunction));
    }

    /**
     * Combines nine source Observables by emitting an item that aggregates the latest values of each of the
     * source Observables each time an item is received from any of the source Observables, where this
     * aggregation is defined by a specified function.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/combineLatest.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code combineLatest} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            the first source Observable
     * @param o2
     *            the second source Observable
     * @param o3
     *            the third source Observable
     * @param o4
     *            the fourth source Observable
     * @param o5
     *            the fifth source Observable
     * @param o6
     *            the sixth source Observable
     * @param o7
     *            the seventh source Observable
     * @param o8
     *            the eighth source Observable
     * @param o9
     *            the ninth source Observable
     * @param combineFunction
     *            the aggregation function used to combine the items emitted by the source Observables
     * @return an Observable that emits items that are the result of combining the items emitted by the source
     *         Observables by means of the given aggregation function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#combinelatest">RxJava wiki: combineLatest</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211991.aspx">MSDN: Observable.CombineLatest</a>
     */
    @SuppressWarnings("unchecked")
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Observable<? extends T8> o8,
            Observable<? extends T9> o9,
            Func9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8, o9), Functions.fromFunc(combineFunction));
    }
    /**
     * Combines a list of source Observables by emitting an item that aggregates the latest values of each of
     * the source Observables each time an item is received from any of the source Observables, where this
     * aggregation is defined by a specified function.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code combineLatest} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T>
     *            the common base type of source values
     * @param <R>
     *            the result type
     * @param sources
     *            the list of source Observables
     * @param combineFunction
     *            the aggregation function used to combine the items emitted by the source Observables
     * @return an Observable that emits items that are the result of combining the items emitted by the source
     *         Observables by means of the given aggregation function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#combinelatest">RxJava wiki: combineLatest</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211991.aspx">MSDN: Observable.CombineLatest</a>
     */
    public static final <T, R> Observable<R> combineLatest(List<? extends Observable<? extends T>> sources, FuncN<? extends R> combineFunction) {
        return create(new OnSubscribeCombineLatest<T, R>(sources, combineFunction));
    }

    /**
     * Returns an Observable that emits the items emitted by each of the Observables emitted by the source
     * Observable, one after the other, without interleaving them.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/concat.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param observables
     *            an Observable that emits Observables
     * @return an Observable that emits items all of the items emitted by the Observables emitted by
     *         {@code observables}, one after the other, without interleaving them
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#concat">RxJava wiki: concat</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.concat.aspx">MSDN: Observable.Concat</a>
     */
    public final static <T> Observable<T> concat(Observable<? extends Observable<? extends T>> observables) {
        return observables.lift(new OperatorConcat<T>());
    }

    /**
     * Returns an Observable that emits the items emitted by two Observables, one after the other, without
     * interleaving them.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/concat.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param t1
     *            an Observable to be concatenated
     * @param t2
     *            an Observable to be concatenated
     * @return an Observable that emits items emitted by the two source Observables, one after the other,
     *         without interleaving them
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#concat">RxJava wiki: concat</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.concat.aspx">MSDN: Observable.Concat</a>
     */
    public final static <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2) {
        return concat(from(t1, t2));
    }

    /**
     * Returns an Observable that emits the items emitted by three Observables, one after the other, without
     * interleaving them.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/concat.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param t1
     *            an Observable to be concatenated
     * @param t2
     *            an Observable to be concatenated
     * @param t3
     *            an Observable to be concatenated
     * @return an Observable that emits items emitted by the three source Observables, one after the other,
     *         without interleaving them
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#concat">RxJava wiki: concat</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.concat.aspx">MSDN: Observable.Concat</a>
     */
    public final static <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3) {
        return concat(from(t1, t2, t3));
    }

    /**
     * Returns an Observable that emits the items emitted by four Observables, one after the other, without
     * interleaving them.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/concat.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be concatenated
     * @param t2
     *            an Observable to be concatenated
     * @param t3
     *            an Observable to be concatenated
     * @param t4
     *            an Observable to be concatenated
     * @return an Observable that emits items emitted by the four source Observables, one after the other,
     *         without interleaving them
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#concat">RxJava wiki: concat</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.concat.aspx">MSDN: Observable.Concat</a>
     */
    public final static <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4) {
        return concat(from(t1, t2, t3, t4));
    }

    /**
     * Returns an Observable that emits the items emitted by five Observables, one after the other, without
     * interleaving them.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/concat.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param t1
     *            an Observable to be concatenated
     * @param t2
     *            an Observable to be concatenated
     * @param t3
     *            an Observable to be concatenated
     * @param t4
     *            an Observable to be concatenated
     * @param t5
     *            an Observable to be concatenated
     * @return an Observable that emits items emitted by the five source Observables, one after the other,
     *         without interleaving them
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#concat">RxJava wiki: concat</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.concat.aspx">MSDN: Observable.Concat</a>
     */
    public final static <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5) {
        return concat(from(t1, t2, t3, t4, t5));
    }

    /**
     * Returns an Observable that emits the items emitted by six Observables, one after the other, without
     * interleaving them.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/concat.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param t1
     *            an Observable to be concatenated
     * @param t2
     *            an Observable to be concatenated
     * @param t3
     *            an Observable to be concatenated
     * @param t4
     *            an Observable to be concatenated
     * @param t5
     *            an Observable to be concatenated
     * @param t6
     *            an Observable to be concatenated
     * @return an Observable that emits items emitted by the six source Observables, one after the other,
     *         without interleaving them
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#concat">RxJava wiki: concat</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.concat.aspx">MSDN: Observable.Concat</a>
     */
    public final static <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6) {
        return concat(from(t1, t2, t3, t4, t5, t6));
    }

    /**
     * Returns an Observable that emits the items emitted by seven Observables, one after the other, without
     * interleaving them.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/concat.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param t1
     *            an Observable to be concatenated
     * @param t2
     *            an Observable to be concatenated
     * @param t3
     *            an Observable to be concatenated
     * @param t4
     *            an Observable to be concatenated
     * @param t5
     *            an Observable to be concatenated
     * @param t6
     *            an Observable to be concatenated
     * @param t7
     *            an Observable to be concatenated
     * @return an Observable that emits items emitted by the seven source Observables, one after the other,
     *         without interleaving them
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#concat">RxJava wiki: concat</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.concat.aspx">MSDN: Observable.Concat</a>
     */
    public final static <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7) {
        return concat(from(t1, t2, t3, t4, t5, t6, t7));
    }

    /**
     * Returns an Observable that emits the items emitted by eight Observables, one after the other, without
     * interleaving them.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/concat.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be concatenated
     * @param t2
     *            an Observable to be concatenated
     * @param t3
     *            an Observable to be concatenated
     * @param t4
     *            an Observable to be concatenated
     * @param t5
     *            an Observable to be concatenated
     * @param t6
     *            an Observable to be concatenated
     * @param t7
     *            an Observable to be concatenated
     * @param t8
     *            an Observable to be concatenated
     * @return an Observable that emits items emitted by the eight source Observables, one after the other,
     *         without interleaving them
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#concat">RxJava wiki: concat</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.concat.aspx">MSDN: Observable.Concat</a>
     */
    public final static <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8) {
        return concat(from(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    /**
     * Returns an Observable that emits the items emitted by nine Observables, one after the other, without
     * interleaving them.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/concat.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be concatenated
     * @param t2
     *            an Observable to be concatenated
     * @param t3
     *            an Observable to be concatenated
     * @param t4
     *            an Observable to be concatenated
     * @param t5
     *            an Observable to be concatenated
     * @param t6
     *            an Observable to be concatenated
     * @param t7
     *            an Observable to be concatenated
     * @param t8
     *            an Observable to be concatenated
     * @param t9
     *            an Observable to be concatenated
     * @return an Observable that emits items emitted by the nine source Observables, one after the other,
     *         without interleaving them
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#concat">RxJava wiki: concat</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.concat.aspx">MSDN: Observable.Concat</a>
     */
    public final static <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8, Observable<? extends T> t9) {
        return concat(from(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    /**
     * Returns an Observable that calls an Observable factory to create an Observable for each new Observer
     * that subscribes. That is, for each subscriber, the actual Observable that subscriber observes is
     * determined by the factory function.
     * <p>
     * <img width="640" height="340" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/defer.png" alt="">
     * <p>
     * The defer Observer allows you to defer or delay emitting items from an Observable until such time as an
     * Observer subscribes to the Observable. This allows an {@link Observer} to easily obtain updates or a
     * refreshed version of the sequence.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code defer} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param observableFactory
     *            the Observable factory function to invoke for each {@link Observer} that subscribes to the
     *            resulting Observable
     * @param <T>
     *            the type of the items emitted by the Observable
     * @return an Observable whose {@link Observer}s' subscriptions trigger an invocation of the given
     *         Observable factory function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#defer">RxJava wiki: defer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229160.aspx">MSDN: Observable.Defer</a>
     */
    public final static <T> Observable<T> defer(Func0<? extends Observable<? extends T>> observableFactory) {
        return create(new OnSubscribeDefer<T>(observableFactory));
    }

    /**
     * Returns an Observable that emits no items to the {@link Observer} and immediately invokes its
     * {@link Observer#onCompleted onCompleted} method.
     * <p>
     * <img width="640" height="190" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/empty.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code empty} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T>
     *            the type of the items (ostensibly) emitted by the Observable
     * @return an Observable that emits no items to the {@link Observer} but immediately invokes the
     *         {@link Observer}'s {@link Observer#onCompleted() onCompleted} method
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#empty-error-and-never">RxJava wiki: empty</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229670.aspx">MSDN: Observable.Empty</a>
     */
    public final static <T> Observable<T> empty() {
        return from(new ArrayList<T>());
    }

    /**
     * Returns an Observable that emits no items to the {@link Observer} and immediately invokes its
     * {@link Observer#onCompleted onCompleted} method on the specified Scheduler.
     * <p>
     * <img width="640" height="190" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/empty.s.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param scheduler
     *            the Scheduler to use to call the {@link Observer#onCompleted onCompleted} method
     * @param <T>
     *            the type of the items (ostensibly) emitted by the Observable
     * @return an Observable that emits no items to the {@link Observer} but immediately invokes the
     *         {@link Observer}'s {@link Observer#onCompleted() onCompleted} method with the specified
     *         {@code scheduler}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#empty-error-and-never">RxJava wiki: empty</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229066.aspx">MSDN: Observable.Empty(IScheduler)</a>
     */
    public final static <T> Observable<T> empty(Scheduler scheduler) {
        return Observable.<T> empty().subscribeOn(scheduler);
    }

    /**
     * Returns an Observable that invokes an {@link Observer}'s {@link Observer#onError onError} method when the
     * Observer subscribes to it.
     * <p>
     * <img width="640" height="190" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/error.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code error} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param exception
     *            the particular Throwable to pass to {@link Observer#onError onError}
     * @param <T>
     *            the type of the items (ostensibly) emitted by the Observable
     * @return an Observable that invokes the {@link Observer}'s {@link Observer#onError onError} method when
     *         the Observer subscribes to it
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#empty-error-and-never">RxJava wiki: error</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh244299.aspx">MSDN: Observable.Throw</a>
     */
    public final static <T> Observable<T> error(Throwable exception) {
        return new ThrowObservable<T>(exception);
    }

    /**
     * Returns an Observable that invokes an {@link Observer}'s {@link Observer#onError onError} method on the
     * specified Scheduler.
     * <p>
     * <img width="640" height="190" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/error.s.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param exception
     *            the particular Throwable to pass to {@link Observer#onError onError}
     * @param scheduler
     *            the Scheduler on which to call {@link Observer#onError onError}
     * @param <T>
     *            the type of the items (ostensibly) emitted by the Observable
     * @return an Observable that invokes the {@link Observer}'s {@link Observer#onError onError} method, on
     *         the specified Scheduler
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#empty-error-and-never">RxJava wiki: error</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211711.aspx">MSDN: Observable.Throw</a>
     */
    public final static <T> Observable<T> error(Throwable exception, Scheduler scheduler) {
        return Observable.<T> error(exception).subscribeOn(scheduler);
    }

    /**
     * Converts a {@link Future} into an Observable.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.Future.png" alt="">
     * <p>
     * You can convert any object that supports the {@link Future} interface into an Observable that emits the
     * return value of the {@link Future#get} method of that object, by passing the object into the {@code from}
     * method.
     * <p>
     * <em>Important note:</em> This Observable is blocking; you cannot unsubscribe from it.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code from} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param future
     *            the source {@link Future}
     * @param <T>
     *            the type of object that the {@link Future} returns, and also the type of item to be emitted by
     *            the resulting Observable
     * @return an Observable that emits the item from the source {@link Future}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     */
    public final static <T> Observable<T> from(Future<? extends T> future) {
        return create(OnSubscribeToObservableFuture.toObservableFuture(future));
    }

    /**
     * Converts a {@link Future} into an Observable, with a timeout on the Future.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.Future.png" alt="">
     * <p>
     * You can convert any object that supports the {@link Future} interface into an Observable that emits the
     * return value of the {@link Future#get} method of that object, by passing the object into the {@code from}
     * method.
     * <p>
     * <em>Important note:</em> This Observable is blocking; you cannot unsubscribe from it.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code from} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param future
     *            the source {@link Future}
     * @param timeout
     *            the maximum time to wait before calling {@code get}
     * @param unit
     *            the {@link TimeUnit} of the {@code timeout} argument
     * @param <T>
     *            the type of object that the {@link Future} returns, and also the type of item to be emitted by
     *            the resulting Observable
     * @return an Observable that emits the item from the source {@link Future}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     */
    public final static <T> Observable<T> from(Future<? extends T> future, long timeout, TimeUnit unit) {
        return create(OnSubscribeToObservableFuture.toObservableFuture(future, timeout, unit));
    }

    /**
     * Converts a {@link Future}, operating on a specified {@link Scheduler}, into an Observable.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.Future.s.png" alt="">
     * <p>
     * You can convert any object that supports the {@link Future} interface into an Observable that emits the
     * return value of the {@link Future#get} method of that object, by passing the object into the {@code from}
     * method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param future
     *            the source {@link Future}
     * @param scheduler
     *            the {@link Scheduler} to wait for the Future on. Use a Scheduler such as
     *            {@link Schedulers#io()} that can block and wait on the Future
     * @param <T>
     *            the type of object that the {@link Future} returns, and also the type of item to be emitted by
     *            the resulting Observable
     * @return an Observable that emits the item from the source {@link Future}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     */
    public final static <T> Observable<T> from(Future<? extends T> future, Scheduler scheduler) {
        return create(OnSubscribeToObservableFuture.toObservableFuture(future)).subscribeOn(scheduler);
    }

    /**
     * Converts an {@link Iterable} sequence into an Observable that emits the items in the sequence.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code from} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param iterable
     *            the source {@link Iterable} sequence
     * @param <T>
     *            the type of items in the {@link Iterable} sequence and the type of items to be emitted by the
     *            resulting Observable
     * @return an Observable that emits each item in the source {@link Iterable} sequence
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212140.aspx">MSDN: Observable.ToObservable</a>
     */
    public final static <T> Observable<T> from(Iterable<? extends T> iterable) {
        return create(new OnSubscribeFromIterable<T>(iterable));
    }

    /**
     * Converts an {@link Iterable} sequence into an Observable that operates on the specified Scheduler,
     * emitting each item from the sequence.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.s.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param iterable
     *            the source {@link Iterable} sequence
     * @param scheduler
     *            the Scheduler on which the Observable is to emit the items of the Iterable
     * @param <T>
     *            the type of items in the {@link Iterable} sequence and the type of items to be emitted by the
     *            resulting Observable
     * @return an Observable that emits each item in the source {@link Iterable} sequence, on the specified
     *         Scheduler
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212140.aspx">MSDN: Observable.ToObservable</a>
     */
    public final static <T> Observable<T> from(Iterable<? extends T> iterable, Scheduler scheduler) {
        return from(iterable).subscribeOn(scheduler);
    }

    /**
     * Converts an item into an Observable that emits that item.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code from} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            the item
     * @param <T>
     *            the type of the item
     * @return an Observable that emits the item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     */
    public final static <T> Observable<T> from(T t1) {
        return just(t1);
    }

    /**
     * Converts two items into an Observable that emits those items.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code from} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            first item
     * @param t2
     *            second item
     * @param <T>
     *            the type of these items
     * @return an Observable that emits each item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     */
    // suppress unchecked because we are using varargs inside the method
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> from(T t1, T t2) {
        return from(Arrays.asList(t1, t2));
    }

    /**
     * Converts three items into an Observable that emits those items.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code from} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            first item
     * @param t2
     *            second item
     * @param t3
     *            third item
     * @param <T>
     *            the type of these items
     * @return an Observable that emits each item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     */
    // suppress unchecked because we are using varargs inside the method
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> from(T t1, T t2, T t3) {
        return from(Arrays.asList(t1, t2, t3));
    }

    /**
     * Converts four items into an Observable that emits those items.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code from} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            first item
     * @param t2
     *            second item
     * @param t3
     *            third item
     * @param t4
     *            fourth item
     * @param <T>
     *            the type of these items
     * @return an Observable that emits each item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     */
    // suppress unchecked because we are using varargs inside the method
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> from(T t1, T t2, T t3, T t4) {
        return from(Arrays.asList(t1, t2, t3, t4));
    }

    /**
     * Converts five items into an Observable that emits those items.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code from} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param t1
     *            first item
     * @param t2
     *            second item
     * @param t3
     *            third item
     * @param t4
     *            fourth item
     * @param t5
     *            fifth item
     * @param <T>
     *            the type of these items
     * @return an Observable that emits each item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     */
    // suppress unchecked because we are using varargs inside the method
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> from(T t1, T t2, T t3, T t4, T t5) {
        return from(Arrays.asList(t1, t2, t3, t4, t5));
    }

    /**
     * Converts six items into an Observable that emits those items.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code from} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            first item
     * @param t2
     *            second item
     * @param t3
     *            third item
     * @param t4
     *            fourth item
     * @param t5
     *            fifth item
     * @param t6
     *            sixth item
     * @param <T>
     *            the type of these items
     * @return an Observable that emits each item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     */
    // suppress unchecked because we are using varargs inside the method
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> from(T t1, T t2, T t3, T t4, T t5, T t6) {
        return from(Arrays.asList(t1, t2, t3, t4, t5, t6));
    }

    /**
     * Converts seven items into an Observable that emits those items.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code from} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            first item
     * @param t2
     *            second item
     * @param t3
     *            third item
     * @param t4
     *            fourth item
     * @param t5
     *            fifth item
     * @param t6
     *            sixth item
     * @param t7
     *            seventh item
     * @param <T>
     *            the type of these items
     * @return an Observable that emits each item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     */
    // suppress unchecked because we are using varargs inside the method
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> from(T t1, T t2, T t3, T t4, T t5, T t6, T t7) {
        return from(Arrays.asList(t1, t2, t3, t4, t5, t6, t7));
    }

    /**
     * Converts eight items into an Observable that emits those items.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code from} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            first item
     * @param t2
     *            second item
     * @param t3
     *            third item
     * @param t4
     *            fourth item
     * @param t5
     *            fifth item
     * @param t6
     *            sixth item
     * @param t7
     *            seventh item
     * @param t8
     *            eighth item
     * @param <T>
     *            the type of these items
     * @return an Observable that emits each item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     */
    // suppress unchecked because we are using varargs inside the method
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> from(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8) {
        return from(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    /**
     * Converts nine items into an Observable that emits those items.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code from} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            first item
     * @param t2
     *            second item
     * @param t3
     *            third item
     * @param t4
     *            fourth item
     * @param t5
     *            fifth item
     * @param t6
     *            sixth item
     * @param t7
     *            seventh item
     * @param t8
     *            eighth item
     * @param t9
     *            ninth item
     * @param <T>
     *            the type of these items
     * @return an Observable that emits each item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     */
    // suppress unchecked because we are using varargs inside the method
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> from(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9) {
        return from(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    /**
     * Converts ten items into an Observable that emits those items.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code from} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            first item
     * @param t2
     *            second item
     * @param t3
     *            third item
     * @param t4
     *            fourth item
     * @param t5
     *            fifth item
     * @param t6
     *            sixth item
     * @param t7
     *            seventh item
     * @param t8
     *            eighth item
     * @param t9
     *            ninth item
     * @param t10
     *            tenth item
     * @param <T>
     *            the type of these items
     * @return an Observable that emits each item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     */
    // suppress unchecked because we are using varargs inside the method
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> from(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9, T t10) {
        return from(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    /**
     * Converts an Array into an Observable that emits the items in the Array.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code from} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            the source Array
     * @param <T>
     *            the type of items in the Array and the type of items to be emitted by the resulting Observable
     * @return an Observable that emits each item in the source Array
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212140.aspx">MSDN: Observable.ToObservable</a>
     */
    public final static <T> Observable<T> from(T... t1) {
        return from(Arrays.asList(t1));
    }

    /**
     * Converts an Array into an Observable that emits the items in the Array on a specified Scheduler.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param items
     *            the source Array
     * @param scheduler
     *            the Scheduler on which the Observable emits the items of the Array
     * @param <T>
     *            the type of items in the Array and the type of items to be emitted by the resulting Observable
     * @return an Observable that emits each item in the source Array
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212140.aspx">MSDN: Observable.ToObservable</a>
     */
    public final static <T> Observable<T> from(T[] items, Scheduler scheduler) {
        return from(Arrays.asList(items), scheduler);
    }

    /**
     * Returns an Observable that emits a sequential number every specified interval of time.
     * <p>
     * <img width="640" height="195" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/interval.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code interval} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param interval
     *            interval size in time units (see below)
     * @param unit
     *            time units to use for the interval size
     * @return an Observable that emits a sequential number each time interval
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#interval">RxJava wiki: interval</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229027.aspx">MSDN: Observable.Interval</a>
     */
    public final static Observable<Long> interval(long interval, TimeUnit unit) {
        return create(new OnSubscribeTimerPeriodically(interval, interval, unit, Schedulers.computation()));
    }

    /**
     * Returns an Observable that emits a sequential number every specified interval of time, on a
     * specified Scheduler.
     * <p>
     * <img width="640" height="200" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/interval.s.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param interval
     *            interval size in time units (see below)
     * @param unit
     *            time units to use for the interval size
     * @param scheduler
     *            the Scheduler to use for scheduling the items
     * @return an Observable that emits a sequential number each time interval
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#interval">RxJava wiki: interval</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh228911.aspx">MSDN: Observable.Interval</a>
     */
    public final static Observable<Long> interval(long interval, TimeUnit unit, Scheduler scheduler) {
        return create(new OnSubscribeTimerPeriodically(interval, interval, unit, scheduler));
    }

    /**
     * Returns an Observable that emits a single item and then completes.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/just.png" alt="">
     * <p>
     * To convert any object into an Observable that emits that object, pass that object into the {@code just}
     * method.
     * <p>
     * This is similar to the {@link #from(java.lang.Object[])} method, except that {@code from} will convert
     * an {@link Iterable} object into an Observable that emits each of the items in the Iterable, one at a
     * time, while the {@code just} method converts an Iterable into an Observable that emits the entire
     * Iterable as a single item.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code just} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param value
     *            the item to emit
     * @param <T>
     *            the type of that item
     * @return an Observable that emits {@code value} as a single item and then completes
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#just">RxJava wiki: just</a>
     */
    public final static <T> Observable<T> just(final T value) {
        return ScalarSynchronousObservable.create(value);
    }

    /**
     * Returns an Observable that emits a single item and then completes, on a specified Scheduler.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/just.s.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param value
     *            the item to emit
     * @param <T>
     *            the type of that item
     * @param scheduler
     *            the Scheduler to emit the single item on
     * @return an Observable that emits {@code value} as a single item and then completes, on a specified
     *         Scheduler
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#just">RxJava wiki: just</a>
     */
    public final static <T> Observable<T> just(T value, Scheduler scheduler) {
        return just(value).subscribeOn(scheduler);
    }

    /**
     * Flattens an Iterable of Observables into one Observable, without any transformation.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine the items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param sequences
     *            the Iterable of Observables
     * @return an Observable that emits items that are the result of flattening the items emitted by the
     *         Observables in the Iterable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229590.aspx">MSDN: Observable.Merge</a>
     */
    public final static <T> Observable<T> merge(Iterable<? extends Observable<? extends T>> sequences) {
        return merge(from(sequences));
    }

    /**
     * Flattens an Iterable of Observables into one Observable, without any transformation, while limiting the
     * number of concurrent subscriptions to these Observables.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine the items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param sequences
     *            the Iterable of Observables
     * @param maxConcurrent
     *            the maximum number of Observables that may be subscribed to concurrently
     * @return an Observable that emits items that are the result of flattening the items emitted by the
     *         Observables in the Iterable
     * @throws IllegalArgumentException
     *             if {@code maxConcurrent} is less than or equal to 0
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229923.aspx">MSDN: Observable.Merge</a>
     */
    public final static <T> Observable<T> merge(Iterable<? extends Observable<? extends T>> sequences, int maxConcurrent) {
        return merge(from(sequences), maxConcurrent);
    }

    /**
     * Flattens an Iterable of Observables into one Observable, without any transformation, while limiting the
     * number of concurrent subscriptions to these Observables, and subscribing to these Observables on a
     * specified Scheduler.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine the items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param sequences
     *            the Iterable of Observables
     * @param maxConcurrent
     *            the maximum number of Observables that may be subscribed to concurrently
     * @param scheduler
     *            the Scheduler on which to traverse the Iterable of Observables
     * @return an Observable that emits items that are the result of flattening the items emitted by the
     *         Observables in the Iterable
     * @throws IllegalArgumentException
     *             if {@code maxConcurrent} is less than or equal to 0
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh244329.aspx">MSDN: Observable.Merge</a>
     */
    public final static <T> Observable<T> merge(Iterable<? extends Observable<? extends T>> sequences, int maxConcurrent, Scheduler scheduler) {
        return merge(from(sequences, scheduler), maxConcurrent);
    }

    /**
     * Flattens an Iterable of Observables into one Observable, without any transformation, subscribing to these
     * Observables on a specified Scheduler.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine the items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param sequences
     *            the Iterable of Observables
     * @param scheduler
     *            the Scheduler on which to traverse the Iterable of Observables
     * @return an Observable that emits items that are the result of flattening the items emitted by the
     *         Observables in the Iterable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh244336.aspx">MSDN: Observable.Merge</a>
     */
    public final static <T> Observable<T> merge(Iterable<? extends Observable<? extends T>> sequences, Scheduler scheduler) {
        return merge(from(sequences, scheduler));
    }

    /**
     * Flattens an Observable that emits Observables into a single Observable that emits the items emitted by
     * those Observables, without any transformation.
     * <p>
     * <img width="640" height="370" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.oo.png" alt="">
     * <p>
     * You can combine the items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param source
     *            an Observable that emits Observables
     * @return an Observable that emits items that are the result of flattening the Observables emitted by the
     *         {@code source} Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    public final static <T> Observable<T> merge(Observable<? extends Observable<? extends T>> source) {
        return source.lift(new OperatorMerge<T>());
    }

    /**
     * Flattens an Observable that emits Observables into a single Observable that emits the items emitted by
     * those Observables, without any transformation, while limiting the maximum number of concurrent
     * subscriptions to these Observables.
     * <p>
     * <img width="640" height="370" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.oo.png" alt="">
     * <p>
     * You can combine the items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param source
     *            an Observable that emits Observables
     * @param maxConcurrent
     *            the maximum number of Observables that may be subscribed to concurrently
     * @return an Observable that emits items that are the result of flattening the Observables emitted by the
     *         {@code source} Observable
     * @throws IllegalArgumentException
     *             if {@code maxConcurrent} is less than or equal to 0
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211914.aspx">MSDN: Observable.Merge</a>
     */
    public final static <T> Observable<T> merge(Observable<? extends Observable<? extends T>> source, int maxConcurrent) {
        return source.lift(new OperatorMergeMaxConcurrent<T>(maxConcurrent));
    }

    /**
     * Flattens two Observables into a single Observable, without any transformation.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @return an Observable that emits all of the items emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2) {
        return merge(from(Arrays.asList(t1, t2)));
    }

    /**
     * Flattens three Observables into a single Observable, without any transformation.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @param t3
     *            an Observable to be merged
     * @return an Observable that emits all of the items emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3) {
        return merge(from(Arrays.asList(t1, t2, t3)));
    }

    /**
     * Flattens four Observables into a single Observable, without any transformation.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @param t3
     *            an Observable to be merged
     * @param t4
     *            an Observable to be merged
     * @return an Observable that emits all of the items emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4) {
        return merge(from(Arrays.asList(t1, t2, t3, t4)));
    }

    /**
     * Flattens five Observables into a single Observable, without any transformation.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @param t3
     *            an Observable to be merged
     * @param t4
     *            an Observable to be merged
     * @param t5
     *            an Observable to be merged
     * @return an Observable that emits all of the items emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5) {
        return merge(from(Arrays.asList(t1, t2, t3, t4, t5)));
    }

    /**
     * Flattens six Observables into a single Observable, without any transformation.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @param t3
     *            an Observable to be merged
     * @param t4
     *            an Observable to be merged
     * @param t5
     *            an Observable to be merged
     * @param t6
     *            an Observable to be merged
     * @return an Observable that emits all of the items emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6) {
        return merge(from(Arrays.asList(t1, t2, t3, t4, t5, t6)));
    }

    /**
     * Flattens seven Observables into a single Observable, without any transformation.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @param t3
     *            an Observable to be merged
     * @param t4
     *            an Observable to be merged
     * @param t5
     *            an Observable to be merged
     * @param t6
     *            an Observable to be merged
     * @param t7
     *            an Observable to be merged
     * @return an Observable that emits all of the items emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7) {
        return merge(from(Arrays.asList(t1, t2, t3, t4, t5, t6, t7)));
    }

    /**
     * Flattens eight Observables into a single Observable, without any transformation.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @param t3
     *            an Observable to be merged
     * @param t4
     *            an Observable to be merged
     * @param t5
     *            an Observable to be merged
     * @param t6
     *            an Observable to be merged
     * @param t7
     *            an Observable to be merged
     * @param t8
     *            an Observable to be merged
     * @return an Observable that emits all of the items emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8) {
        return merge(from(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8)));
    }

    /**
     * Flattens nine Observables into a single Observable, without any transformation.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @param t3
     *            an Observable to be merged
     * @param t4
     *            an Observable to be merged
     * @param t5
     *            an Observable to be merged
     * @param t6
     *            an Observable to be merged
     * @param t7
     *            an Observable to be merged
     * @param t8
     *            an Observable to be merged
     * @param t9
     *            an Observable to be merged
     * @return an Observable that emits all of the items emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    @SuppressWarnings("unchecked")
    public final static <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8, Observable<? extends T> t9) {
        return merge(from(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9)));
    }

    /**
     * Flattens an Array of Observables into one Observable, without any transformation.
     * <p>
     * <img width="640" height="370" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.io.png" alt="">
     * <p>
     * You can combine items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param sequences
     *            the Array of Observables
     * @return an Observable that emits all of the items emitted by the Observables in the Array
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    public final static <T> Observable<T> merge(Observable<? extends T>[] sequences) {
        return merge(from(sequences));
    }

    /**
     * Flattens an Array of Observables into one Observable, without any transformation, traversing the array on
     * a specified Scheduler.
     * <p>
     * <img width="640" height="370" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.ios.png" alt="">
     * <p>
     * You can combine items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param sequences
     *            the Array of Observables
     * @param scheduler
     *            the Scheduler on which to traverse the Array
     * @return an Observable that emits all of the items emitted by the Observables in the Array
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229061.aspx">MSDN: Observable.Merge</a>
     */
    public final static <T> Observable<T> merge(Observable<? extends T>[] sequences, Scheduler scheduler) {
        return merge(from(sequences, scheduler));
    }

    /**
     * Flattens an Observable that emits Observables into one Observable, in a way that allows an Observer to
     * receive all successfully emitted items from all of the source Observables without being interrupted by
     * an error notification from one of them.
     * <p>
     * This behaves like {@link #merge(Observable)} except that if any of the merged Observables notify of an
     * error via {@link Observer#onError onError}, {@code mergeDelayError} will refrain from propagating that
     * error notification until all of the merged Observables have finished emitting items.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/mergeDelayError.png" alt="">
     * <p>
     * Even if multiple merged Observables send {@code onError} notifications, {@code mergeDelayError} will only
     * invoke the {@code onError} method of its Observers once.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code mergeDelayError} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param source
     *            an Observable that emits Observables
     * @return an Observable that emits all of the items emitted by the Observables emitted by the
     *         {@code source} Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#mergedelayerror">RxJava wiki: mergeDelayError</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    public final static <T> Observable<T> mergeDelayError(Observable<? extends Observable<? extends T>> source) {
        return source.lift(new OperatorMergeDelayError<T>());
    }

    /**
     * Flattens two Observables into one Observable, in a way that allows an Observer to receive all
     * successfully emitted items from each of the source Observables without being interrupted by an error
     * notification from one of them.
     * <p>
     * This behaves like {@link #merge(Observable, Observable)} except that if any of the merged Observables
     * notify of an error via {@link Observer#onError onError}, {@code mergeDelayError} will refrain from
     * propagating that error notification until all of the merged Observables have finished emitting items.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/mergeDelayError.png" alt="">
     * <p>
     * Even if both merged Observables send {@code onError} notifications, {@code mergeDelayError} will only
     * invoke the {@code onError} method of its Observers once.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code mergeDelayError} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @return an Observable that emits all of the items that are emitted by the two source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#mergedelayerror">RxJava wiki: mergeDelayError</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    public final static <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2) {
        return mergeDelayError(from(t1, t2));
    }

    /**
     * Flattens three Observables into one Observable, in a way that allows an Observer to receive all
     * successfully emitted items from all of the source Observables without being interrupted by an error
     * notification from one of them.
     * <p>
     * This behaves like {@link #merge(Observable, Observable, Observable)} except that if any of the merged
     * Observables notify of an error via {@link Observer#onError onError}, {@code mergeDelayError} will refrain
     * from propagating that error notification until all of the merged Observables have finished emitting
     * items.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/mergeDelayError.png" alt="">
     * <p>
     * Even if multiple merged Observables send {@code onError} notifications, {@code mergeDelayError} will only
     * invoke the {@code onError} method of its Observers once.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code mergeDelayError} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @param t3
     *            an Observable to be merged
     * @return an Observable that emits all of the items that are emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#mergedelayerror">RxJava wiki: mergeDelayError</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    public final static <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3) {
        return mergeDelayError(from(t1, t2, t3));
    }

    /**
     * Flattens four Observables into one Observable, in a way that allows an Observer to receive all
     * successfully emitted items from all of the source Observables without being interrupted by an error
     * notification from one of them.
     * <p>
     * This behaves like {@link #merge(Observable, Observable, Observable, Observable)} except that if any of
     * the merged Observables notify of an error via {@link Observer#onError onError}, {@code mergeDelayError}
     * will refrain from propagating that error notification until all of the merged Observables have finished
     * emitting items.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/mergeDelayError.png" alt="">
     * <p>
     * Even if multiple merged Observables send {@code onError} notifications, {@code mergeDelayError} will only
     * invoke the {@code onError} method of its Observers once.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code mergeDelayError} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @param t3
     *            an Observable to be merged
     * @param t4
     *            an Observable to be merged
     * @return an Observable that emits all of the items that are emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#mergedelayerror">RxJava wiki: mergeDelayError</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    public final static <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4) {
        return mergeDelayError(from(t1, t2, t3, t4));
    }

    /**
     * Flattens five Observables into one Observable, in a way that allows an Observer to receive all
     * successfully emitted items from all of the source Observables without being interrupted by an error
     * notification from one of them.
     * <p>
     * This behaves like {@link #merge(Observable, Observable, Observable, Observable, Observable)} except that
     * if any of the merged Observables notify of an error via {@link Observer#onError onError},
     * {@code mergeDelayError} will refrain from propagating that error notification until all of the merged
     * Observables have finished emitting items.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/mergeDelayError.png" alt="">
     * <p>
     * Even if multiple merged Observables send {@code onError} notifications, {@code mergeDelayError} will only
     * invoke the {@code onError} method of its Observers once.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code mergeDelayError} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @param t3
     *            an Observable to be merged
     * @param t4
     *            an Observable to be merged
     * @param t5
     *            an Observable to be merged
     * @return an Observable that emits all of the items that are emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#mergedelayerror">RxJava wiki: mergeDelayError</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    public final static <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5) {
        return mergeDelayError(from(t1, t2, t3, t4, t5));
    }

    /**
     * Flattens six Observables into one Observable, in a way that allows an Observer to receive all
     * successfully emitted items from all of the source Observables without being interrupted by an error
     * notification from one of them.
     * <p>
     * This behaves like {@link #merge(Observable, Observable, Observable, Observable, Observable, Observable)}
     * except that if any of the merged Observables notify of an error via {@link Observer#onError onError},
     * {@code mergeDelayError} will refrain from propagating that error notification until all of the merged
     * Observables have finished emitting items.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/mergeDelayError.png" alt="">
     * <p>
     * Even if multiple merged Observables send {@code onError} notifications, {@code mergeDelayError} will only
     * invoke the {@code onError} method of its Observers once.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code mergeDelayError} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @param t3
     *            an Observable to be merged
     * @param t4
     *            an Observable to be merged
     * @param t5
     *            an Observable to be merged
     * @param t6
     *            an Observable to be merged
     * @return an Observable that emits all of the items that are emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#mergedelayerror">RxJava wiki: mergeDelayError</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    public final static <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6) {
        return mergeDelayError(from(t1, t2, t3, t4, t5, t6));
    }

    /**
     * Flattens seven Observables into one Observable, in a way that allows an Observer to receive all
     * successfully emitted items from all of the source Observables without being interrupted by an error
     * notification from one of them.
     * <p>
     * This behaves like
     * {@link #merge(Observable, Observable, Observable, Observable, Observable, Observable, Observable)}
     * except that if any of the merged Observables notify of an error via {@link Observer#onError onError},
     * {@code mergeDelayError} will refrain from propagating that error notification until all of the merged
     * Observables have finished emitting items.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/mergeDelayError.png" alt="">
     * <p>
     * Even if multiple merged Observables send {@code onError} notifications, {@code mergeDelayError} will only
     * invoke the {@code onError} method of its Observers once.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code mergeDelayError} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @param t3
     *            an Observable to be merged
     * @param t4
     *            an Observable to be merged
     * @param t5
     *            an Observable to be merged
     * @param t6
     *            an Observable to be merged
     * @param t7
     *            an Observable to be merged
     * @return an Observable that emits all of the items that are emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#mergedelayerror">RxJava wiki: mergeDelayError</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    public final static <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7) {
        return mergeDelayError(from(t1, t2, t3, t4, t5, t6, t7));
    }

    /**
     * Flattens eight Observables into one Observable, in a way that allows an Observer to receive all
     * successfully emitted items from all of the source Observables without being interrupted by an error
     * notification from one of them.
     * <p>
     * This behaves like {@link #merge(Observable, Observable, Observable, Observable, Observable, Observable, Observable, Observable)}
     * except that if any of the merged Observables notify of an error via {@link Observer#onError onError},
     * {@code mergeDelayError} will refrain from propagating that error notification until all of the merged
     * Observables have finished emitting items.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/mergeDelayError.png" alt="">
     * <p>
     * Even if multiple merged Observables send {@code onError} notifications, {@code mergeDelayError} will only
     * invoke the {@code onError} method of its Observers once.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code mergeDelayError} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @param t3
     *            an Observable to be merged
     * @param t4
     *            an Observable to be merged
     * @param t5
     *            an Observable to be merged
     * @param t6
     *            an Observable to be merged
     * @param t7
     *            an Observable to be merged
     * @param t8
     *            an Observable to be merged
     * @return an Observable that emits all of the items that are emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#mergedelayerror">RxJava wiki: mergeDelayError</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    // suppress because the types are checked by the method signature before using a vararg
    public final static <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8) {
        return mergeDelayError(from(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    /**
     * Flattens nine Observables into one Observable, in a way that allows an Observer to receive all
     * successfully emitted items from all of the source Observables without being interrupted by an error
     * notification from one of them.
     * <p>
     * This behaves like {@link #merge(Observable, Observable, Observable, Observable, Observable, Observable, Observable, Observable, Observable)}
     * except that if any of the merged Observables notify of an error via {@link Observer#onError onError},
     * {@code mergeDelayError} will refrain from propagating that error notification until all of the merged
     * Observables have finished emitting items.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/mergeDelayError.png" alt="">
     * <p>
     * Even if multiple merged Observables send {@code onError} notifications, {@code mergeDelayError} will only
     * invoke the {@code onError} method of its Observers once.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code mergeDelayError} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @param t3
     *            an Observable to be merged
     * @param t4
     *            an Observable to be merged
     * @param t5
     *            an Observable to be merged
     * @param t6
     *            an Observable to be merged
     * @param t7
     *            an Observable to be merged
     * @param t8
     *            an Observable to be merged
     * @param t9
     *            an Observable to be merged
     * @return an Observable that emits all of the items that are emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#mergedelayerror">RxJava wiki: mergeDelayError</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    public final static <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8, Observable<? extends T> t9) {
        return mergeDelayError(from(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    /**
     * Converts the source {@code Observable<T>} into an {@code Observable<Observable<T>>} that emits the
     * source Observable as its single emission.
     * <p>
     * <img width="640" height="350" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/nest.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code nest} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that emits a single item: the source Observable
     * @since 0.17
     */
    public final Observable<Observable<T>> nest() {
        return just(this);
    }

    /**
     * Returns an Observable that never sends any items or notifications to an {@link Observer}.
     * <p>
     * <img width="640" height="185" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/never.png" alt="">
     * <p>
     * This Observable is useful primarily for testing purposes.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code never} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <T>
     *            the type of items (not) emitted by the Observable
     * @return an Observable that never emits any items or sends any notifications to an {@link Observer}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#empty-error-and-never">RxJava wiki: never</a>
     */
    public final static <T> Observable<T> never() {
        return new NeverObservable<T>();
    }

    /**
     * Converts an {@code Observable<Observable<T>>} into another {@code Observable<Observable<T>>} whose
     * emitted Observables emit the same items as those emitted by the source Observable, but where the number
     * of such Observables is restricted by {@code parallelObservables}.
     * <p>
     * For example, if the original {@code Observable<Observable<T>>} emits 100 Observables and
     * {@code parallelObservables} is 8, the items emitted by the 100 original Observables will be distributed
     * among 8 Observables emitted by the resulting Observable.
     * <p>
     * <img width="640" height="535" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/parallelMerge.png" alt="">
     * <p>
     * This is a mechanism for efficiently processing <i>n</i> number of Observables on a smaller <i>m</i>
     * number of resources (typically CPU cores).
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code parallelMerge} by default operates on the {@code immediate} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param parallelObservables
     *            the number of Observables to merge into
     * @return an Observable of Observables constrained in number by {@code parallelObservables}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#parallelmerge">RxJava wiki: parallelMerge</a>
     */
    public final static <T> Observable<Observable<T>> parallelMerge(Observable<Observable<T>> source, int parallelObservables) {
        return OperatorParallelMerge.parallelMerge(source, parallelObservables);
    }

    /**
     * Converts an {@code Observable<Observable<T>>} into another {@code Observable<Observable<T>>} whose
     * emitted Observables emit the same items as those emitted by the source Observable, but where the number
     * of such Observables is restricted by {@code parallelObservables}, and each runs on a defined Scheduler.
     * <p>
     * For example, if the original {@code Observable<Observable<T>>} emits 100 Observables and
     * {@code parallelObservables} is 8, the items emitted by the 100 original Observables will be distributed
     * among 8 Observables emitted by the resulting Observable.
     * <p>
     * <img width="640" height="535" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/parallelMerge.png" alt="">
     * <p>
     * This is a mechanism for efficiently processing <i>n</i> number of Observables on a smaller <i>m</i>
     * number of resources (typically CPU cores).
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param parallelObservables
     *            the number of Observables to merge into
     * @param scheduler
     *            the {@link Scheduler} to run each Observable on
     * @return an Observable of Observables constrained in number by {@code parallelObservables}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#parallelmerge">RxJava wiki: parallelMerge</a>
     */
    public final static <T> Observable<Observable<T>> parallelMerge(Observable<Observable<T>> source, int parallelObservables, Scheduler scheduler) {
        return OperatorParallelMerge.parallelMerge(source, parallelObservables, scheduler);
    }

    /**
     * Returns an Observable that emits a sequence of Integers within a specified range.
     * <p>
     * <img width="640" height="195" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/range.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code range} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param start
     *            the value of the first Integer in the sequence
     * @param count
     *            the number of sequential Integers to generate
     * @return an Observable that emits a range of sequential Integers
     * @throws IllegalArgumentException
     *             if {@code count} is less than zero, or if {@code start} + {@code count} &minus; 1 exceeds
     *             {@code Integer.MAX_VALUE}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#range">RxJava wiki: range</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229460.aspx">MSDN: Observable.Range</a>
     */
    public final static Observable<Integer> range(int start, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count can not be negative");
        }
        if (count == 0) {
            return Observable.empty();
        }
        if (start > Integer.MAX_VALUE - count + 1) {
            throw new IllegalArgumentException("start + count can not exceed Integer.MAX_VALUE");
        }
        if(count == 1) {
            return Observable.just(start);
        }
        return Observable.create(new OnSubscribeRange(start, start + (count - 1)));
    }

    /**
     * Returns an Observable that emits a sequence of Integers within a specified range, on a specified
     * Scheduler.
     * <p>
     * <img width="640" height="195" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/range.s.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param start
     *            the value of the first Integer in the sequence
     * @param count
     *            the number of sequential Integers to generate
     * @param scheduler
     *            the Scheduler to run the generator loop on
     * @return an Observable that emits a range of sequential Integers
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#range">RxJava wiki: range</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211896.aspx">MSDN: Observable.Range</a>
     */
    public final static Observable<Integer> range(int start, int count, Scheduler scheduler) {
        return range(start, count).subscribeOn(scheduler);
    }

    /**
     * Returns an Observable that emits a Boolean value that indicates whether two Observable sequences are the
     * same by comparing the items emitted by each Observable pairwise.
     * <p>
     * <img width="640" height="385" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/sequenceEqual.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code sequenceEqual} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param first
     *            the first Observable to compare
     * @param second
     *            the second Observable to compare
     * @param <T>
     *            the type of items emitted by each Observable
     * @return an Observable that emits a Boolean value that indicates whether the two sequences are the same
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#sequenceequal">RxJava wiki: sequenceEqual</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.sequenceequal.aspx">MSDN: Observable.SequenceEqual</a>
     */
    public final static <T> Observable<Boolean> sequenceEqual(Observable<? extends T> first, Observable<? extends T> second) {
        return sequenceEqual(first, second, new Func2<T, T, Boolean>() {
            @Override
            public final Boolean call(T first, T second) {
                if (first == null) {
                    return second == null;
                }
                return first.equals(second);
            }
        });
    }

    /**
     * Returns an Observable that emits a Boolean value that indicates whether two Observable sequences are the
     * same by comparing the items emitted by each Observable pairwise based on the results of a specified
     * equality function.
     * <p>
     * <img width="640" height="385" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/sequenceEqual.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code sequenceEqual} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param first
     *            the first Observable to compare
     * @param second
     *            the second Observable to compare
     * @param equality
     *            a function used to compare items emitted by each Observable
     * @param <T>
     *            the type of items emitted by each Observable
     * @return an Observable that emits a Boolean value that indicates whether the two Observable two sequences
     *         are the same according to the specified function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#sequenceequal">RxJava wiki: sequenceEqual</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.sequenceequal.aspx">MSDN: Observable.SequenceEqual</a>
     */
    public final static <T> Observable<Boolean> sequenceEqual(Observable<? extends T> first, Observable<? extends T> second, Func2<? super T, ? super T, Boolean> equality) {
        return OperatorSequenceEqual.sequenceEqual(first, second, equality);
    }

    /**
     * Converts an Observable that emits Observables into an Observable that emits the items emitted by the
     * most recently emitted of those Observables.
     * <p>
     * <img width="640" height="370" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/switchDo.png" alt="">
     * <p>
     * {@code switchOnNext} subscribes to an Observable that emits Observables. Each time it observes one of
     * these emitted Observables, the Observable returned by {@code switchOnNext} begins emitting the items
     * emitted by that Observable. When a new Observable is emitted, {@code switchOnNext} stops emitting items
     * from the earlier-emitted Observable and begins emitting items from the new one.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code switchOnNext} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <T> the item type
     * @param sequenceOfSequences
     *            the source Observable that emits Observables
     * @return an Observable that emits the items emitted by the Observable most recently emitted by the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#switchonnext">RxJava wiki: switchOnNext</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229197.aspx">MSDN: Observable.Switch</a>
     */
    public final static <T> Observable<T> switchOnNext(Observable<? extends Observable<? extends T>> sequenceOfSequences) {
        return sequenceOfSequences.lift(new OperatorSwitch<T>());
    }

    /**
     * Returns an Observable that emits a {@code 0L} after the {@code initialDelay} and ever increasing numbers
     * after each {@code period} of time thereafter.
     * <p>
     * <img width="640" height="200" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/timer.p.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time. If the downstream needs a slower rate
     *      it should slow the timer or use something like {@link #onBackpressureDrop}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code timer} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param initialDelay
     *            the initial delay time to wait before emitting the first value of 0L
     * @param period
     *            the period of time between emissions of the subsequent numbers
     * @param unit
     *            the time unit for both {@code initialDelay} and {@code period}
     * @return an Observable that emits a 0L after the {@code initialDelay} and ever increasing numbers after
     *         each {@code period} of time thereafter
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#timer">RxJava wiki: timer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229435.aspx">MSDN: Observable.Timer</a>
     */
    public final static Observable<Long> timer(long initialDelay, long period, TimeUnit unit) {
        return timer(initialDelay, period, unit, Schedulers.computation());
    }

    /**
     * Returns an Observable that emits a {@code 0L} after the {@code initialDelay} and ever increasing numbers
     * after each {@code period} of time thereafter, on a specified {@link Scheduler}.
     * <p>
     * <img width="640" height="200" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/timer.ps.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time. If the downstream needs a slower rate
     *      it should slow the timer or use something like {@link #onBackpressureDrop}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param initialDelay
     *            the initial delay time to wait before emitting the first value of 0L
     * @param period
     *            the period of time between emissions of the subsequent numbers
     * @param unit
     *            the time unit for both {@code initialDelay} and {@code period}
     * @param scheduler
     *            the Scheduler on which the waiting happens and items are emitted
     * @return an Observable that emits a 0L after the {@code initialDelay} and ever increasing numbers after
     *         each {@code period} of time thereafter, while running on the given Scheduler
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#timer">RxJava wiki: timer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229652.aspx">MSDN: Observable.Timer</a>
     */
    public final static Observable<Long> timer(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        return create(new OnSubscribeTimerPeriodically(initialDelay, period, unit, scheduler));
    }

    /**
     * Returns an Observable that emits one item after a specified delay, and then completes.
     * <p>
     * <img width="640" height="200" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/timer.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time. If the downstream needs a slower rate
     *      it should slow the timer or use something like {@link #onBackpressureDrop}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code timer} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param delay
     *            the initial delay before emitting a single {@code 0L}
     * @param unit
     *            time units to use for {@code delay}
     * @return an Observable that emits one item after a specified delay, and then completes
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#timer">RxJava wiki: timer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212050.aspx">MSDN: Observable.Timer</a>
     */
    public final static Observable<Long> timer(long delay, TimeUnit unit) {
        return timer(delay, unit, Schedulers.computation());
    }

    /**
     * Returns an Observable that emits one item after a specified delay, on a specified Scheduler, and then
     * completes.
     * <p>
     * <img width="640" height="200" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/timer.s.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time. If the downstream needs a slower rate
     *      it should slow the timer or use something like {@link #onBackpressureDrop}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param delay
     *            the initial delay before emitting a single 0L
     * @param unit
     *            time units to use for {@code delay}
     * @param scheduler
     *            the {@link Scheduler} to use for scheduling the item
     * @return an Observable that emits one item after a specified delay, on a specified Scheduler, and then
     *         completes
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#timer">RxJava wiki: timer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229503.aspx">MSDN: Observable.Timer</a>
     */
    public final static Observable<Long> timer(long delay, TimeUnit unit, Scheduler scheduler) {
        return create(new OnSubscribeTimerOnce(delay, unit, scheduler));
    }

    /**
     * Constructs an Observable that creates a dependent resource object.
     * <p>
     * <img width="640" height="400" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/using.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code using} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param resourceFactory
     *            the factory function to create a resource object that depends on the Observable
     * @param observableFactory
     *            the factory function to create an Observable
     * @return the Observable whose lifetime controls the lifetime of the dependent resource object
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#using">RxJava wiki: using</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229585.aspx">MSDN: Observable.Using</a>
     */
    public final static <T, Resource extends Subscription> Observable<T> using(Func0<Resource> resourceFactory, Func1<Resource, ? extends Observable<? extends T>> observableFactory) {
        return create(new OnSubscribeUsing<T, Resource>(resourceFactory, observableFactory));
    }

    /**
     * Returns an Observable that emits the results of a function of your choosing applied to combinations of
     * items emitted, in sequence, by an Iterable of other Observables.
     * <p>
     * {@code zip} applies this function in strict sequence, so the first item emitted by the new Observable
     * will be the result of the function applied to the first item emitted by each of the source Observables;
     * the second item emitted by the new Observable will be the result of the function applied to the second
     * item emitted by each of those Observables; and so forth.
     * <p>
     * The resulting {@code Observable<R>} returned from {@code zip} will invoke {@code onNext} as many times as
     * the number of {@code onNext} invokations of the source Observable that emits the fewest items.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/zip.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param ws
     *            an Iterable of source Observables
     * @param zipFunction
     *            a function that, when applied to an item emitted by each of the source Observables, results in
     *            an item that will be emitted by the resulting Observable
     * @return an Observable that emits the zipped results
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#zip">RxJava wiki: zip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.zip.aspx">MSDN: Observable.Zip</a>
     */
    public final static <R> Observable<R> zip(Iterable<? extends Observable<?>> ws, FuncN<? extends R> zipFunction) {
        List<Observable<?>> os = new ArrayList<Observable<?>>();
        for (Observable<?> o : ws) {
            os.add(o);
        }
        return Observable.just(os.toArray(new Observable<?>[os.size()])).lift(new OperatorZip<R>(zipFunction));
    }

    /**
     * Returns an Observable that emits the results of a function of your choosing applied to combinations of
     * <i>n</i> items emitted, in sequence, by the <i>n</i> Observables emitted by a specified Observable.
     * <p>
     * {@code zip} applies this function in strict sequence, so the first item emitted by the new Observable
     * will be the result of the function applied to the first item emitted by each of the Observables emitted
     * by the source Observable; the second item emitted by the new Observable will be the result of the
     * function applied to the second item emitted by each of those Observables; and so forth.
     * <p>
     * The resulting {@code Observable<R>} returned from {@code zip} will invoke {@code onNext} as many times as
     * the number of {@code onNext} invokations of the source Observable that emits the fewest items.
     * <p>
     * <img width="640" height="370" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/zip.o.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param ws
     *            an Observable of source Observables
     * @param zipFunction
     *            a function that, when applied to an item emitted by each of the Observables emitted by
     *            {@code ws}, results in an item that will be emitted by the resulting Observable
     * @return an Observable that emits the zipped results
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#zip">RxJava wiki: zip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.zip.aspx">MSDN: Observable.Zip</a>
     */
    public final static <R> Observable<R> zip(Observable<? extends Observable<?>> ws, final FuncN<? extends R> zipFunction) {
        return ws.toList().map(new Func1<List<? extends Observable<?>>, Observable<?>[]>() {

            @Override
            public Observable<?>[] call(List<? extends Observable<?>> o) {
                return o.toArray(new Observable<?>[o.size()]);
            }

        }).lift(new OperatorZip<R>(zipFunction));
    }

    /**
     * Returns an Observable that emits the results of a function of your choosing applied to combinations of
     * two items emitted, in sequence, by two other Observables.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/zip.png" alt="">
     * <p>
     * {@code zip} applies this function in strict sequence, so the first item emitted by the new Observable
     * will be the result of the function applied to the first item emitted by {@code o1} and the first item
     * emitted by {@code o2}; the second item emitted by the new Observable will be the result of the function
     * applied to the second item emitted by {@code o1} and the second item emitted by {@code o2}; and so forth.
     * <p>
     * The resulting {@code Observable<R>} returned from {@code zip} will invoke {@link Observer#onNext onNext}
     * as many times as the number of {@code onNext} invocations of the source Observable that emits the fewest
     * items.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            the first source Observable
     * @param o2
     *            a second source Observable
     * @param zipFunction
     *            a function that, when applied to an item emitted by each of the source Observables, results
     *            in an item that will be emitted by the resulting Observable
     * @return an Observable that emits the zipped results
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#zip">RxJava wiki: zip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.zip.aspx">MSDN: Observable.Zip</a>
     */
    public final static <T1, T2, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, final Func2<? super T1, ? super T2, ? extends R> zipFunction) {
        return just(new Observable<?>[] { o1, o2 }).lift(new OperatorZip<R>(zipFunction));
    }

    /**
     * Returns an Observable that emits the results of a function of your choosing applied to combinations of
     * three items emitted, in sequence, by three other Observables.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/zip.png" alt="">
     * <p>
     * {@code zip} applies this function in strict sequence, so the first item emitted by the new Observable
     * will be the result of the function applied to the first item emitted by {@code o1}, the first item
     * emitted by {@code o2}, and the first item emitted by {@code o3}; the second item emitted by the new
     * Observable will be the result of the function applied to the second item emitted by {@code o1}, the
     * second item emitted by {@code o2}, and the second item emitted by {@code o3}; and so forth.
     * <p>
     * The resulting {@code Observable<R>} returned from {@code zip} will invoke {@link Observer#onNext onNext}
     * as many times as the number of {@code onNext} invocations of the source Observable that emits the fewest
     * items.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            the first source Observable
     * @param o2
     *            a second source Observable
     * @param o3
     *            a third source Observable
     * @param zipFunction
     *            a function that, when applied to an item emitted by each of the source Observables, results in
     *            an item that will be emitted by the resulting Observable
     * @return an Observable that emits the zipped results
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#zip">RxJava wiki: zip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.zip.aspx">MSDN: Observable.Zip</a>
     */
    public final static <T1, T2, T3, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Func3<? super T1, ? super T2, ? super T3, ? extends R> zipFunction) {
        return just(new Observable<?>[] { o1, o2, o3 }).lift(new OperatorZip<R>(zipFunction));
    }

    /**
     * Returns an Observable that emits the results of a function of your choosing applied to combinations of
     * four items emitted, in sequence, by four other Observables.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/zip.png" alt="">
     * <p>
     * {@code zip} applies this function in strict sequence, so the first item emitted by the new Observable
     * will be the result of the function applied to the first item emitted by {@code o1}, the first item
     * emitted by {@code o2}, the first item emitted by {@code o3}, and the first item emitted by {@code 04};
     * the second item emitted by the new Observable will be the result of the function applied to the second
     * item emitted by each of those Observables; and so forth.
     * <p>
     * The resulting {@code Observable<R>} returned from {@code zip} will invoke {@link Observer#onNext onNext}
     * as many times as the number of {@code onNext} invocations of the source Observable that emits the fewest
     * items.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            the first source Observable
     * @param o2
     *            a second source Observable
     * @param o3
     *            a third source Observable
     * @param o4
     *            a fourth source Observable
     * @param zipFunction
     *            a function that, when applied to an item emitted by each of the source Observables, results in
     *            an item that will be emitted by the resulting Observable
     * @return an Observable that emits the zipped results
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#zip">RxJava wiki: zip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.zip.aspx">MSDN: Observable.Zip</a>
     */
    public final static <T1, T2, T3, T4, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipFunction) {
        return just(new Observable<?>[] { o1, o2, o3, o4 }).lift(new OperatorZip<R>(zipFunction));
    }

    /**
     * Returns an Observable that emits the results of a function of your choosing applied to combinations of
     * five items emitted, in sequence, by five other Observables.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/zip.png" alt="">
     * <p>
     * {@code zip} applies this function in strict sequence, so the first item emitted by the new Observable
     * will be the result of the function applied to the first item emitted by {@code o1}, the first item
     * emitted by {@code o2}, the first item emitted by {@code o3}, the first item emitted by {@code o4}, and
     * the first item emitted by {@code o5}; the second item emitted by the new Observable will be the result of
     * the function applied to the second item emitted by each of those Observables; and so forth.
     * <p>
     * The resulting {@code Observable<R>} returned from {@code zip} will invoke {@link Observer#onNext onNext}
     * as many times as the number of {@code onNext} invocations of the source Observable that emits the fewest
     * items.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            the first source Observable
     * @param o2
     *            a second source Observable
     * @param o3
     *            a third source Observable
     * @param o4
     *            a fourth source Observable
     * @param o5
     *            a fifth source Observable
     * @param zipFunction
     *            a function that, when applied to an item emitted by each of the source Observables, results in
     *            an item that will be emitted by the resulting Observable
     * @return an Observable that emits the zipped results
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#zip">RxJava wiki: zip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.zip.aspx">MSDN: Observable.Zip</a>
     */
    public final static <T1, T2, T3, T4, T5, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipFunction) {
        return just(new Observable<?>[] { o1, o2, o3, o4, o5 }).lift(new OperatorZip<R>(zipFunction));
    }

    /**
     * Returns an Observable that emits the results of a function of your choosing applied to combinations of
     * six items emitted, in sequence, by six other Observables.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/zip.png" alt="">
     * <p>
     * {@code zip} applies this function in strict sequence, so the first item emitted by the new Observable
     * will be the result of the function applied to the first item emitted by each source Observable, the
     * second item emitted by the new Observable will be the result of the function applied to the second item
     * emitted by each of those Observables, and so forth.
     * <p>
     * The resulting {@code Observable<R>} returned from {@code zip} will invoke {@link Observer#onNext onNext}
     * as many times as the number of {@code onNext} invocations of the source Observable that emits the fewest
     * items.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            the first source Observable
     * @param o2
     *            a second source Observable
     * @param o3
     *            a third source Observable
     * @param o4
     *            a fourth source Observable
     * @param o5
     *            a fifth source Observable
     * @param o6
     *            a sixth source Observable
     * @param zipFunction
     *            a function that, when applied to an item emitted by each of the source Observables, results in
     *            an item that will be emitted by the resulting Observable
     * @return an Observable that emits the zipped results
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#zip">RxJava wiki: zip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.zip.aspx">MSDN: Observable.Zip</a>
     */
    public final static <T1, T2, T3, T4, T5, T6, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6,
            Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipFunction) {
        return just(new Observable<?>[] { o1, o2, o3, o4, o5, o6 }).lift(new OperatorZip<R>(zipFunction));
    }

    /**
     * Returns an Observable that emits the results of a function of your choosing applied to combinations of
     * seven items emitted, in sequence, by seven other Observables.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/zip.png" alt="">
     * <p>
     * {@code zip} applies this function in strict sequence, so the first item emitted by the new Observable
     * will be the result of the function applied to the first item emitted by each source Observable, the
     * second item emitted by the new Observable will be the result of the function applied to the second item
     * emitted by each of those Observables, and so forth.
     * <p>
     * The resulting {@code Observable<R>} returned from {@code zip} will invoke {@link Observer#onNext onNext}
     * as many times as the number of {@code onNext} invocations of the source Observable that emits the fewest
     * items.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            the first source Observable
     * @param o2
     *            a second source Observable
     * @param o3
     *            a third source Observable
     * @param o4
     *            a fourth source Observable
     * @param o5
     *            a fifth source Observable
     * @param o6
     *            a sixth source Observable
     * @param o7
     *            a seventh source Observable
     * @param zipFunction
     *            a function that, when applied to an item emitted by each of the source Observables, results in
     *            an item that will be emitted by the resulting Observable
     * @return an Observable that emits the zipped results
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#zip">RxJava wiki: zip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.zip.aspx">MSDN: Observable.Zip</a>
     */
    public final static <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7,
            Func7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipFunction) {
        return just(new Observable<?>[] { o1, o2, o3, o4, o5, o6, o7 }).lift(new OperatorZip<R>(zipFunction));
    }

    /**
     * Returns an Observable that emits the results of a function of your choosing applied to combinations of
     * eight items emitted, in sequence, by eight other Observables.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/zip.png" alt="">
     * <p>
     * {@code zip} applies this function in strict sequence, so the first item emitted by the new Observable
     * will be the result of the function applied to the first item emitted by each source Observable, the
     * second item emitted by the new Observable will be the result of the function applied to the second item
     * emitted by each of those Observables, and so forth.
     * <p>
     * The resulting {@code Observable<R>} returned from {@code zip} will invoke {@link Observer#onNext onNext}
     * as many times as the number of {@code onNext} invocations of the source Observable that emits the fewest
     * items.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            the first source Observable
     * @param o2
     *            a second source Observable
     * @param o3
     *            a third source Observable
     * @param o4
     *            a fourth source Observable
     * @param o5
     *            a fifth source Observable
     * @param o6
     *            a sixth source Observable
     * @param o7
     *            a seventh source Observable
     * @param o8
     *            an eighth source Observable
     * @param zipFunction
     *            a function that, when applied to an item emitted by each of the source Observables, results in
     *            an item that will be emitted by the resulting Observable
     * @return an Observable that emits the zipped results
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#zip">RxJava wiki: zip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.zip.aspx">MSDN: Observable.Zip</a>
     */
    public final static <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Observable<? extends T8> o8,
            Func8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipFunction) {
        return just(new Observable<?>[] { o1, o2, o3, o4, o5, o6, o7, o8 }).lift(new OperatorZip<R>(zipFunction));
    }

    /**
     * Returns an Observable that emits the results of a function of your choosing applied to combinations of
     * nine items emitted, in sequence, by nine other Observables.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/zip.png" alt="">
     * <p>
     * {@code zip} applies this function in strict sequence, so the first item emitted by the new Observable
     * will be the result of the function applied to the first item emitted by each source Observable, the
     * second item emitted by the new Observable will be the result of the function applied to the second item
     * emitted by each of those Observables, and so forth.
     * <p>
     * The resulting {@code Observable<R>} returned from {@code zip} will invoke {@link Observer#onNext onNext}
     * as many times as the number of {@code onNext} invocations of the source Observable that emits the fewest
     * items.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param o1
     *            the first source Observable
     * @param o2
     *            a second source Observable
     * @param o3
     *            a third source Observable
     * @param o4
     *            a fourth source Observable
     * @param o5
     *            a fifth source Observable
     * @param o6
     *            a sixth source Observable
     * @param o7
     *            a seventh source Observable
     * @param o8
     *            an eighth source Observable
     * @param o9
     *            a ninth source Observable
     * @param zipFunction
     *            a function that, when applied to an item emitted by each of the source Observables, results in
     *            an item that will be emitted by the resulting Observable
     * @return an Observable that emits the zipped results
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#zip">RxJava wiki: zip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.zip.aspx">MSDN: Observable.Zip</a>
     */
    public final static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Observable<? extends T8> o8,
            Observable<? extends T9> o9, Func9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipFunction) {
        return just(new Observable<?>[] { o1, o2, o3, o4, o5, o6, o7, o8, o9 }).lift(new OperatorZip<R>(zipFunction));
    }

    /**
     * Returns an Observable that emits a Boolean that indicates whether all of the items emitted by the source
     * Observable satisfy a condition.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/all.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code all} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param predicate
     *            a function that evaluates an item and returns a Boolean
     * @return an Observable that emits {@code true} if all items emitted by the source Observable satisfy the
     *         predicate; otherwise, {@code false}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#all">RxJava wiki: all</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229537.aspx">MSDN: Observable.All</a>
     */
    public final Observable<Boolean> all(Func1<? super T, Boolean> predicate) {
        return lift(new OperatorAll<T>(predicate));
    }
    
    /**
     * Mirrors the first Observable (current or provided) that emits an item.
     * <p>
     * <img width="640" height="385" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/amb.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code amb} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable competing to react first
     * @return an Observable that emits the same sequence of items as whichever of the source Observables first
     *         emitted an item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#amb">RxJava wiki: amb</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229733.aspx">MSDN: Observable.Amb</a>
     * @since 0.20
     */
    public final Observable<T> ambWith(Observable<? extends T> t1) {
        return amb(this, t1);
    }

    /**
     * Disguises a object of an Observable subclass as a simple Observable object. Useful for instance when you
     * have an implementation of a subclass of Observable but you want to hide the properties and methods of
     * this subclass from whomever you are passing the Observable to.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code asObservable} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that hides the identity of this Observable
     */
    public final Observable<T> asObservable() {
        return lift(new OperatorAsObservable<T>());
    }

    /**
     * Returns an Observable that emits buffers of items it collects from the source Observable. The resulting
     * Observable emits connected, non-overlapping buffers. It emits the current buffer and replaces it with a
     * new buffer whenever the Observable produced by the specified {@code bufferClosingSelector} emits an item.
     * <p>
     * <img width="640" height="395" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/buffer1.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it is instead controlled by the given Observables and
     *      buffers data. It requests {@code Long.MAX_VALUE} upstream and does not obey downstream requests.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code buffer} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param bufferClosingSelector
     *            a {@link Func0} that produces an Observable that governs the boundary between buffers.
     *            Whenever this {@code Observable} emits an item, {@code buffer} emits the current buffer and
     *            begins to fill a new one
     * @return an Observable that emits a connected, non-overlapping buffer of items from the source Observable
     *         each time the Observable created with the {@code bufferClosingSelector} argument emits an item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#buffer">RxJava wiki: buffer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.buffer.aspx">MSDN: Observable.Buffer</a>
     */
    public final <TClosing> Observable<List<T>> buffer(Func0<? extends Observable<? extends TClosing>> bufferClosingSelector) {
        return lift(new OperatorBufferWithSingleObservable<T, TClosing>(bufferClosingSelector, 16));
    }

    /**
     * Returns an Observable that emits buffers of items it collects from the source Observable. The resulting
     * Observable emits connected, non-overlapping buffers, each containing {@code count} items. When the source
     * Observable completes or encounters an error, the resulting Observable emits the current buffer and
     * propagates the notification from the source Observable.
     * <p>
     * <img width="640" height="320" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/buffer3.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code buffer} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param count
     *            the maximum number of items in each buffer before it should be emitted
     * @return an Observable that emits connected, non-overlapping buffers, each containing at most
     *         {@code count} items from the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#buffer">RxJava wiki: buffer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.buffer.aspx">MSDN: Observable.Buffer</a>
     */
    public final Observable<List<T>> buffer(int count) {
        return lift(new OperatorBufferWithSize<T>(count, count));
    }

    /**
     * Returns an Observable that emits buffers of items it collects from the source Observable. The resulting
     * Observable emits buffers every {@code skip} items, each containing {@code count} items. When the source
     * Observable completes or encounters an error, the resulting Observable emits the current buffer and
     * propagates the notification from the source Observable.
     * <p>
     * <img width="640" height="320" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/buffer4.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code buffer} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param count
     *            the maximum size of each buffer before it should be emitted
     * @param skip
     *            how many items emitted by the source Observable should be skipped before starting a new
     *            buffer. Note that when {@code skip} and {@code count} are equal, this is the same operation as
     *            {@link #buffer(int)}.
     * @return an Observable that emits buffers for every {@code skip} item from the source Observable and
     *         containing at most {@code count} items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#buffer">RxJava wiki: buffer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.buffer.aspx">MSDN: Observable.Buffer</a>
     */
    public final Observable<List<T>> buffer(int count, int skip) {
        return lift(new OperatorBufferWithSize<T>(count, skip));
    }

    /**
     * Returns an Observable that emits buffers of items it collects from the source Observable. The resulting
     * Observable starts a new buffer periodically, as determined by the {@code timeshift} argument. It emits
     * each buffer after a fixed timespan, specified by the {@code timespan} argument. When the source
     * Observable completes or encounters an error, the resulting Observable emits the current buffer and
     * propagates the notification from the source Observable.
     * <p>
     * <img width="640" height="320" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/buffer7.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time. It requests {@code Long.MAX_VALUE}
     *      upstream and does not obey downstream requests.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code buffer} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param timespan
     *            the period of time each buffer collects items before it is emitted
     * @param timeshift
     *            the period of time after which a new buffer will be created
     * @param unit
     *            the unit of time that applies to the {@code timespan} and {@code timeshift} arguments
     * @return an Observable that emits new buffers of items emitted by the source Observable periodically after
     *         a fixed timespan has elapsed
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#buffer">RxJava wiki: buffer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.buffer.aspx">MSDN: Observable.Buffer</a>
     */
    public final Observable<List<T>> buffer(long timespan, long timeshift, TimeUnit unit) {
        return lift(new OperatorBufferWithTime<T>(timespan, timeshift, unit, Integer.MAX_VALUE, Schedulers.computation()));
    }

    /**
     * Returns an Observable that emits buffers of items it collects from the source Observable. The resulting
     * Observable starts a new buffer periodically, as determined by the {@code timeshift} argument, and on the
     * specified {@code scheduler}. It emits each buffer after a fixed timespan, specified by the
     * {@code timespan} argument. When the source Observable completes or encounters an error, the resulting
     * Observable emits the current buffer and propagates the notification from the source Observable.
     * <p>
     * <img width="640" height="320" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/buffer7.s.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time. It requests {@code Long.MAX_VALUE}
     *      upstream and does not obey downstream requests.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param timespan
     *            the period of time each buffer collects items before it is emitted
     * @param timeshift
     *            the period of time after which a new buffer will be created
     * @param unit
     *            the unit of time that applies to the {@code timespan} and {@code timeshift} arguments
     * @param scheduler
     *            the {@link Scheduler} to use when determining the end and start of a buffer
     * @return an Observable that emits new buffers of items emitted by the source Observable periodically after
     *         a fixed timespan has elapsed
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#buffer">RxJava wiki: buffer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.buffer.aspx">MSDN: Observable.Buffer</a>
     */
    public final Observable<List<T>> buffer(long timespan, long timeshift, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorBufferWithTime<T>(timespan, timeshift, unit, Integer.MAX_VALUE, scheduler));
    }

    /**
     * Returns an Observable that emits buffers of items it collects from the source Observable. The resulting
     * Observable emits connected, non-overlapping buffers, each of a fixed duration specified by the
     * {@code timespan} argument. When the source Observable completes or encounters an error, the resulting
     * Observable emits the current buffer and propagates the notification from the source Observable.
     * <p>
     * <img width="640" height="320" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/buffer5.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time. It requests {@code Long.MAX_VALUE}
     *      upstream and does not obey downstream requests.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code buffer} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param timespan
     *            the period of time each buffer collects items before it is emitted and replaced with a new
     *            buffer
     * @param unit
     *            the unit of time that applies to the {@code timespan} argument
     * @return an Observable that emits connected, non-overlapping buffers of items emitted by the source
     *         Observable within a fixed duration
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#buffer">RxJava wiki: buffer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.buffer.aspx">MSDN: Observable.Buffer</a>
     */
    public final Observable<List<T>> buffer(long timespan, TimeUnit unit) {
        return lift(new OperatorBufferWithTime<T>(timespan, timespan, unit, Integer.MAX_VALUE, Schedulers.computation()));
    }

    /**
     * Returns an Observable that emits buffers of items it collects from the source Observable. The resulting
     * Observable emits connected, non-overlapping buffers, each of a fixed duration specified by the
     * {@code timespan} argument or a maximum size specified by the {@code count} argument (whichever is reached
     * first). When the source Observable completes or encounters an error, the resulting Observable emits the
     * current buffer and propagates the notification from the source Observable.
     * <p>
     * <img width="640" height="320" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/buffer6.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time. It requests {@code Long.MAX_VALUE}
     *      upstream and does not obey downstream requests.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code buffer} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param timespan
     *            the period of time each buffer collects items before it is emitted and replaced with a new
     *            buffer
     * @param unit
     *            the unit of time which applies to the {@code timespan} argument
     * @param count
     *            the maximum size of each buffer before it is emitted
     * @return an Observable that emits connected, non-overlapping buffers of items emitted by the source
     *         Observable, after a fixed duration or when the buffer reaches maximum capacity (whichever occurs
     *         first)
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#buffer">RxJava wiki: buffer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.buffer.aspx">MSDN: Observable.Buffer</a>
     */
    public final Observable<List<T>> buffer(long timespan, TimeUnit unit, int count) {
        return lift(new OperatorBufferWithTime<T>(timespan, timespan, unit, count, Schedulers.computation()));
    }

    /**
     * Returns an Observable that emits buffers of items it collects from the source Observable. The resulting
     * Observable emits connected, non-overlapping buffers, each of a fixed duration specified by the
     * {@code timespan} argument as measured on the specified {@code scheduler}, or a maximum size specified by
     * the {@code count} argument (whichever is reached first). When the source Observable completes or
     * encounters an error, the resulting Observable emits the current buffer and propagates the notification
     * from the source Observable.
     * <p>
     * <img width="640" height="320" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/buffer6.s.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time. It requests {@code Long.MAX_VALUE}
     *      upstream and does not obey downstream requests.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param timespan
     *            the period of time each buffer collects items before it is emitted and replaced with a new
     *            buffer
     * @param unit
     *            the unit of time which applies to the {@code timespan} argument
     * @param count
     *            the maximum size of each buffer before it is emitted
     * @param scheduler
     *            the {@link Scheduler} to use when determining the end and start of a buffer
     * @return an Observable that emits connected, non-overlapping buffers of items emitted by the source
     *         Observable after a fixed duration or when the buffer reaches maximum capacity (whichever occurs
     *         first)
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#buffer">RxJava wiki: buffer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.buffer.aspx">MSDN: Observable.Buffer</a>
     */
    public final Observable<List<T>> buffer(long timespan, TimeUnit unit, int count, Scheduler scheduler) {
        return lift(new OperatorBufferWithTime<T>(timespan, timespan, unit, count, scheduler));
    }

    /**
     * Returns an Observable that emits buffers of items it collects from the source Observable. The resulting
     * Observable emits connected, non-overlapping buffers, each of a fixed duration specified by the
     * {@code timespan} argument and on the specified {@code scheduler}. When the source Observable completes or
     * encounters an error, the resulting Observable emits the current buffer and propagates the notification
     * from the source Observable.
     * <p>
     * <img width="640" height="320" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/buffer5.s.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time. It requests {@code Long.MAX_VALUE}
     *      upstream and does not obey downstream requests.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param timespan
     *            the period of time each buffer collects items before it is emitted and replaced with a new
     *            buffer
     * @param unit
     *            the unit of time which applies to the {@code timespan} argument
     * @param scheduler
     *            the {@link Scheduler} to use when determining the end and start of a buffer
     * @return an Observable that emits connected, non-overlapping buffers of items emitted by the source
     *         Observable within a fixed duration
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#buffer">RxJava wiki: buffer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.buffer.aspx">MSDN: Observable.Buffer</a>
     */
    public final Observable<List<T>> buffer(long timespan, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorBufferWithTime<T>(timespan, timespan, unit, Integer.MAX_VALUE, scheduler));
    }

    /**
     * Returns an Observable that emits buffers of items it collects from the source Observable. The resulting
     * Observable emits buffers that it creates when the specified {@code bufferOpenings} Observable emits an
     * item, and closes when the Observable returned from {@code bufferClosingSelector} emits an item.
     * <p>
     * <img width="640" height="470" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/buffer2.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it is instead controlled by the given Observables and
     *      buffers data. It requests {@code Long.MAX_VALUE} upstream and does not obey downstream requests.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code buffer} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param bufferOpenings
     *            the Observable that, when it emits an item, causes a new buffer to be created
     * @param bufferClosingSelector
     *            the {@link Func1} that is used to produce an Observable for every buffer created. When this
     *            Observable emits an item, the associated buffer is emitted.
     * @return an Observable that emits buffers, containing items from the source Observable, that are created
     *         and closed when the specified Observables emit items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#buffer">RxJava wiki: buffer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.buffer.aspx">MSDN: Observable.Buffer</a>
     */
    public final <TOpening, TClosing> Observable<List<T>> buffer(Observable<? extends TOpening> bufferOpenings, Func1<? super TOpening, ? extends Observable<? extends TClosing>> bufferClosingSelector) {
        return lift(new OperatorBufferWithStartEndObservable<T, TOpening, TClosing>(bufferOpenings, bufferClosingSelector));
    }

    /**
     * Returns an Observable that emits non-overlapping buffered items from the source Observable each time the
     * specified boundary Observable emits an item.
     * <p>
     * <img width="640" height="395" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/buffer8.png" alt="">
     * <p>
     * Completion of either the source or the boundary Observable causes the returned Observable to emit the
     * latest buffer and complete.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it is instead controlled by the {@code Observable}
     *      {@code boundary} and buffers data. It requests {@code Long.MAX_VALUE} upstream and does not obey
     *      downstream requests.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code buffer} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <B>
     *            the boundary value type (ignored)
     * @param boundary
     *            the boundary Observable
     * @return an Observable that emits buffered items from the source Observable when the boundary Observable
     *         emits an item
     * @see #buffer(rx.Observable, int)
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#buffer">RxJava wiki: buffer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.buffer.aspx">MSDN: Observable.Buffer</a>
     */
    public final <B> Observable<List<T>> buffer(Observable<B> boundary) {
        return lift(new OperatorBufferWithSingleObservable<T, B>(boundary, 16));
    }

    /**
     * Returns an Observable that emits non-overlapping buffered items from the source Observable each time the
     * specified boundary Observable emits an item.
     * <p>
     * <img width="640" height="395" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/buffer8.png" alt="">
     * <p>
     * Completion of either the source or the boundary Observable causes the returned Observable to emit the
     * latest buffer and complete.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it is instead controlled by the {@code Observable}
     *      {@code boundary} and buffers data. It requests {@code Long.MAX_VALUE} upstream and does not obey
     *      downstream requests.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code buffer} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <B>
     *            the boundary value type (ignored)
     * @param boundary
     *            the boundary Observable
     * @param initialCapacity
     *            the initial capacity of each buffer chunk
     * @return an Observable that emits buffered items from the source Observable when the boundary Observable
     *         emits an item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#buffer">RxJava wiki: buffer</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.buffer.aspx">MSDN: Observable.Buffer</a>
     * @see #buffer(rx.Observable, int)
     */
    public final <B> Observable<List<T>> buffer(Observable<B> boundary, int initialCapacity) {
        return lift(new OperatorBufferWithSingleObservable<T, B>(boundary, initialCapacity));
    }

    /**
     * Caches the emissions from the source Observable and replays them in order to any subsequent Subscribers.
     * This method has similar behavior to {@link #replay} except that this auto-subscribes to the source
     * Observable rather than returning a {@link ConnectableObservable} for which you must call
     * {@code connect} to activate the subscription.
     * <p>
     * <img width="640" height="410" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/cache.png" alt="">
     * <p>
     * This is useful when you want an Observable to cache responses and you can't control the
     * subscribe/unsubscribe behavior of all the {@link Subscriber}s.
     * <p>
     * When you call {@code cache}, it does not yet subscribe to the source Observable and so does not yet
     * begin cacheing items. This only happens when the first Subscriber calls the resulting Observable's
     * {@code subscribe} method.
     * <p>
     * <em>Note:</em> You sacrifice the ability to unsubscribe from the origin when you use the {@code cache}
     * Observer so be careful not to use this Observer on Observables that emit an infinite or very large number
     * of items that will use up memory.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support upstream backpressure as it is purposefully requesting and caching
     *      everything emitted.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code cache} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that, when first subscribed to, caches all of its items and notifications for the
     *         benefit of subsequent subscribers
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#cache">RxJava wiki: cache</a>
     */
    public final Observable<T> cache() {
        return create(new OnSubscribeCache<T>(this));
    }

    /**
     * Caches emissions from the source Observable and replays them in order to any subsequent Subscribers.
     * This method has similar behavior to {@link #replay} except that this auto-subscribes to the source
     * Observable rather than returning a {@link ConnectableObservable} for which you must call
     * {@code connect} to activate the subscription.
     * <p>
     * <img width="640" height="410" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/cache.png" alt="">
     * <p>
     * This is useful when you want an Observable to cache responses and you can't control the
     * subscribe/unsubscribe behavior of all the {@link Subscriber}s.
     * <p>
     * When you call {@code cache}, it does not yet subscribe to the source Observable and so does not yet
     * begin cacheing items. This only happens when the first Subscriber calls the resulting Observable's
     * {@code subscribe} method.
     * <p>
     * <em>Note:</em> You sacrifice the ability to unsubscribe from the origin when you use the {@code cache}
     * Observer so be careful not to use this Observer on Observables that emit an infinite or very large number
     * of items that will use up memory.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support upstream backpressure as it is purposefully requesting and caching
     *      everything emitted.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code cache} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param capacity hint for number of items to cache (for optimizing underlying data structure)
     * @return an Observable that, when first subscribed to, caches all of its items and notifications for the
     *         benefit of subsequent subscribers
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#cache">RxJava wiki: cache</a>
     * @since 0.20
     */
    public final Observable<T> cache(int capacity) {
        return create(new OnSubscribeCache<T>(this, capacity));
    }

    /**
     * Returns an Observable that emits the items emitted by the source Observable, converted to the specified
     * type.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/cast.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code cast} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param klass
     *            the target class type that {@code cast} will cast the items emitted by the source Observable
     *            into before emitting them from the resulting Observable
     * @return an Observable that emits each item from the source Observable after converting it to the
     *         specified type
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#cast">RxJava wiki: cast</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211842.aspx">MSDN: Observable.Cast</a>
     */
    public final <R> Observable<R> cast(final Class<R> klass) {
        return lift(new OperatorCast<T, R>(klass));
    }

    /**
     * Collects items emitted by the source Observable into a single mutable data structure and returns an
     * Observable that emits this structure.
     * <p>
     * <img width="640" height="330" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/collect.png" alt="">
     * <p>
     * This is a simplified version of {@code reduce} that does not need to return the state on each pass.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because by intent it will receive all values and reduce
     *      them to a single {@code onNext}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code collect} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param state
     *           the mutable data structure that will collect the items
     * @param collector
     *           a function that accepts the {@code state} and an emitted item, and modifies {@code state}
     *           accordingly
     * @return an Observable that emits the result of collecting the values emitted by the source Observable
     *         into a single mutable data structure
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#collect">RxJava wiki: collect</a>
     */
    public final <R> Observable<R> collect(R state, final Action2<R, ? super T> collector) {
        Func2<R, T, R> accumulator = new Func2<R, T, R>() {

            @Override
            public final R call(R state, T value) {
                collector.call(state, value);
                return state;
            }

        };
        return reduce(state, accumulator);
    }

    /**
     * Returns a new Observable that emits items resulting from applying a function that you supply to each item
     * emitted by the source Observable, where that function returns an Observable, and then emitting the items
     * that result from concatinating those resulting Observables.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/concatMap.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code concatMap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param func
     *            a function that, when applied to an item emitted by the source Observable, returns an
     *            Observable
     * @return an Observable that emits the result of applying the transformation function to each item emitted
     *         by the source Observable and concatinating the Observables obtained from this transformation
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#flatmap-and-concatmap">RxJava wiki: concatMap</a>
     */
    public final <R> Observable<R> concatMap(Func1<? super T, ? extends Observable<? extends R>> func) {
        return concat(map(func));
    }
    
    /**
     * Returns an Observable that emits the items emitted from the current Observable, then the next, one after
     * the other, without interleaving them.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/concat.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be concatenated after the current
     * @return an Observable that emits items emitted by the two source Observables, one after the other,
     *         without interleaving them
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#concat">RxJava wiki: concat</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.concat.aspx">MSDN: Observable.Concat</a>
     * @since 0.20
     */
    public final Observable<T> concatWith(Observable<? extends T> t1) {
        return concat(this, t1);
    }

    /**
     * Returns an Observable that emits a Boolean that indicates whether the source Observable emitted a
     * specified item.
     * <p>
     * <img width="640" height="320" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/contains.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code contains} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param element
     *            the item to search for in the emissions from the source Observable
     * @return an Observable that emits {@code true} if the specified item is emitted by the source Observable,
     *         or {@code false} if the source Observable completes without emitting that item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#contains">RxJava wiki: contains</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh228965.aspx">MSDN: Observable.Contains</a>
     */
    public final Observable<Boolean> contains(final Object element) {
        return exists(new Func1<T, Boolean>() {
            public final Boolean call(T t1) {
                return element == null ? t1 == null : element.equals(t1);
            }
        });
    }

    /**
     * Returns an Observable that emits the count of the total number of items emitted by the source Observable.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/count.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because by intent it will receive all values and reduce
     *      them to a single {@code onNext}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code count} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that emits a single item: the number of elements emitted by the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#count-and-longcount">RxJava wiki: count</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229470.aspx">MSDN: Observable.Count</a>
     * @see #longCount()
     */
    public final Observable<Integer> count() {
        return reduce(0, new Func2<Integer, T, Integer>() {
            @Override
            public final Integer call(Integer t1, T t2) {
                return t1 + 1;
            }
        });
    }

    /**
     * Returns an Observable that mirrors the source Observable, except that it drops items emitted by the
     * source Observable that are followed by another item within a computed debounce duration.
     * <p>
     * <img width="640" height="425" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/debounce.f.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses the {@code debounceSelector} to mark
     *      boundaries.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code debounce} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <U>
     *            the debounce value type (ignored)
     * @param debounceSelector
     *            function to retrieve a sequence that indicates the throttle duration for each item
     * @return an Observable that omits items emitted by the source Observable that are followed by another item
     *         within a computed debounce duration
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#throttlewithtimeout-or-debounce">RxJava wiki: debounce</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.throttle.aspx">MSDN: Observable.Throttle</a>
     */
    public final <U> Observable<T> debounce(Func1<? super T, ? extends Observable<U>> debounceSelector) {
        return lift(new OperatorDebounceWithSelector<T, U>(debounceSelector));
    }

    /**
     * Returns an Observable that mirrors the source Observable, except that it drops items emitted by the
     * source Observable that are followed by newer items before a timeout value expires. The timer resets on
     * each emission.
     * <p>
     * <em>Note:</em> If items keep being emitted by the source Observable faster than the timeout then no items
     * will be emitted by the resulting Observable.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/debounce.png" alt="">
     * <p>
     * Information on debounce vs throttle:
     * <p>
     * <ul>
     * <li><a href="http://drupalmotion.com/article/debounce-and-throttle-visual-explanation">Debounce and Throttle: visual explanation</a></li>
     * <li><a href="http://unscriptable.com/2009/03/20/debouncing-javascript-methods/">Debouncing: javascript methods</a></li>
     * <li><a href="http://www.illyriad.co.uk/blog/index.php/2011/09/javascript-dont-spam-your-server-debounce-and-throttle/">Javascript - don't spam your server: debounce and throttle</a></li>
     * </ul>
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code debounce} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param timeout
     *            the time each item has to be "the most recent" of those emitted by the source Observable to
     *            ensure that it's not dropped
     * @param unit
     *            the {@link TimeUnit} for the timeout
     * @return an Observable that filters out items from the source Observable that are too quickly followed by
     *         newer items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#throttlewithtimeout-or-debounce">RxJava wiki: debounce</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.throttle.aspx">MSDN: Observable.Throttle</a>
     * @see #throttleWithTimeout(long, TimeUnit)
     */
    public final Observable<T> debounce(long timeout, TimeUnit unit) {
        return lift(new OperatorDebounceWithTime<T>(timeout, unit, Schedulers.computation()));
    }

    /**
     * Returns an Observable that mirrors the source Observable, except that it drops items emitted by the
     * source Observable that are followed by newer items before a timeout value expires on a specified
     * Scheduler. The timer resets on each emission.
     * <p>
     * <em>Note:</em> If items keep being emitted by the source Observable faster than the timeout then no items
     * will be emitted by the resulting Observable.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/debounce.s.png" alt="">
     * <p>
     * Information on debounce vs throttle:
     * <p>
     * <ul>
     * <li><a href="http://drupalmotion.com/article/debounce-and-throttle-visual-explanation">Debounce and Throttle: visual explanation</a></li>
     * <li><a href="http://unscriptable.com/2009/03/20/debouncing-javascript-methods/">Debouncing: javascript methods</a></li>
     * <li><a href="http://www.illyriad.co.uk/blog/index.php/2011/09/javascript-dont-spam-your-server-debounce-and-throttle/">Javascript - don't spam your server: debounce and throttle</a></li>
     * </ul>
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param timeout
     *            the time each item has to be "the most recent" of those emitted by the source Observable to
     *            ensure that it's not dropped
     * @param unit
     *            the unit of time for the specified timeout
     * @param scheduler
     *            the {@link Scheduler} to use internally to manage the timers that handle the timeout for each
     *            item
     * @return an Observable that filters out items from the source Observable that are too quickly followed by
     *         newer items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#throttlewithtimeout-or-debounce">RxJava wiki: debounce</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.throttle.aspx">MSDN: Observable.Throttle</a>
     * @see #throttleWithTimeout(long, TimeUnit, Scheduler)
     */
    public final Observable<T> debounce(long timeout, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorDebounceWithTime<T>(timeout, unit, scheduler));
    }

    /**
     * Returns an Observable that emits the items emitted by the source Observable or a specified default item
     * if the source Observable is empty.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/defaultIfEmpty.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code defaultIfEmpty} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param defaultValue
     *            the item to emit if the source Observable emits no items
     * @return an Observable that emits either the specified default item if the source Observable emits no
     *         items, or the items emitted by the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#defaultifempty">RxJava wiki: defaultIfEmpty</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229624.aspx">MSDN: Observable.DefaultIfEmpty</a>
     */
    public final Observable<T> defaultIfEmpty(T defaultValue) {
        return lift(new OperatorDefaultIfEmpty<T>(defaultValue));
    }

    /**
     * Returns an Observable that delays the subscription to and emissions from the souce Observable via another
     * Observable on a per-item basis.
     * <p>
     * <img width="640" height="450" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/delay.oo.png" alt="">
     * <p>
     * <em>Note:</em> the resulting Observable will immediately propagate any {@code onError} notification
     * from the source Observable.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code delay} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <U>
     *            the subscription delay value type (ignored)
     * @param <V>
     *            the item delay value type (ignored)
     * @param subscriptionDelay
     *            a function that returns an Observable that triggers the subscription to the source Observable
     *            once it emits any item
     * @param itemDelay
     *            a function that returns an Observable for each item emitted by the source Observable, which is
     *            then used to delay the emission of that item by the resulting Observable until the Observable
     *            returned from {@code itemDelay} emits an item
     * @return an Observable that delays the subscription and emissions of the source Observable via another
     *         Observable on a per-item basis
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#delay">RxJava wiki: delay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.delay.aspx">MSDN: Observable.Delay</a>
     */
    public final <U, V> Observable<T> delay(
            Func0<? extends Observable<U>> subscriptionDelay,
            Func1<? super T, ? extends Observable<V>> itemDelay) {
        return create(new OnSubscribeDelayWithSelector<T, U, V>(this, subscriptionDelay, itemDelay));
    }

    /**
     * Returns an Observable that delays the emissions of the source Observable via another Observable on a
     * per-item basis.
     * <p>
     * <img width="640" height="450" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/delay.o.png" alt="">
     * <p>
     * <em>Note:</em> the resulting Observable will immediately propagate any {@code onError} notification
     * from the source Observable.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code delay} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <U>
     *            the item delay value type (ignored)
     * @param itemDelay
     *            a function that returns an Observable for each item emitted by the source Observable, which is
     *            then used to delay the emission of that item by the resulting Observable until the Observable
     *            returned from {@code itemDelay} emits an item
     * @return an Observable that delays the emissions of the source Observable via another Observable on a
     *         per-item basis
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#delay">RxJava wiki: delay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.delay.aspx">MSDN: Observable.Delay</a>
     */
    public final <U> Observable<T> delay(Func1<? super T, ? extends Observable<U>> itemDelay) {
        return create(new OnSubscribeDelayWithSelector<T, U, U>(this, itemDelay));
    }

    /**
     * Returns an Observable that emits the items emitted by the source Observable shifted forward in time by a
     * specified delay. Error notifications from the source Observable are not delayed.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/delay.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code delay} operates by default on the {@code compuation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param delay
     *            the delay to shift the source by
     * @param unit
     *            the {@link TimeUnit} in which {@code period} is defined
     * @return the source Observable shifted in time by the specified delay
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#delay">RxJava wiki: delay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229810.aspx">MSDN: Observable.Delay</a>
     */
    public final Observable<T> delay(long delay, TimeUnit unit) {
        return create(new OnSubscribeDelay<T>(this, delay, unit, Schedulers.computation()));
    }

    /**
     * Returns an Observable that emits the items emitted by the source Observable shifted forward in time by a
     * specified delay. Error notifications from the source Observable are not delayed.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/delay.s.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param delay
     *            the delay to shift the source by
     * @param unit
     *            the time unit of {@code delay}
     * @param scheduler
     *            the {@link Scheduler} to use for delaying
     * @return the source Observable shifted in time by the specified delay
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#delay">RxJava wiki: delay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229280.aspx">MSDN: Observable.Delay</a>
     */
    public final Observable<T> delay(long delay, TimeUnit unit, Scheduler scheduler) {
        return create(new OnSubscribeDelay<T>(this, delay, unit, scheduler));
    }

    /**
     * Returns an Observable that delays the subscription to the source Observable by a given amount of time.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/delaySubscription.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code delay} operates by default on the {@code compuation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param delay
     *            the time to delay the subscription
     * @param unit
     *            the time unit of {@code delay}
     * @return an Observable that delays the subscription to the source Observable by the given amount
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#delaysubscription">RxJava wiki: delaySubscription</a>
     */
    public final Observable<T> delaySubscription(long delay, TimeUnit unit) {
        return delaySubscription(delay, unit, Schedulers.computation());
    }

    /**
     * Returns an Observable that delays the subscription to the source Observable by a given amount of time,
     * both waiting and subscribing on a given Scheduler.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/delaySubscription.s.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param delay
     *            the time to delay the subscription
     * @param unit
     *            the time unit of {@code delay}
     * @param scheduler
     *            the Scheduler on which the waiting and subscription will happen
     * @return an Observable that delays the subscription to the source Observable by a given
     *         amount, waiting and subscribing on the given Scheduler
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#delaysubscription">RxJava wiki: delaySubscription</a>
     */
    public final Observable<T> delaySubscription(long delay, TimeUnit unit, Scheduler scheduler) {
        return create(new OnSubscribeDelaySubscription<T>(this, delay, unit, scheduler));
    }

    /**
     * Returns an Observable that reverses the effect of {@link #materialize materialize} by transforming the
     * {@link Notification} objects emitted by the source Observable into the items or notifications they
     * represent.
     * <p>
     * <img width="640" height="335" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/dematerialize.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code dematerialize} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that emits the items and notifications embedded in the {@link Notification} objects
     *         emitted by the source Observable
     * @throws OnErrorNotImplementedException
     *             if the source Observable is not of type {@code Observable<Notification<T>>}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#dematerialize">RxJava wiki: dematerialize</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229047.aspx">MSDN: Observable.dematerialize</a>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public final <T2> Observable<T2> dematerialize() {
        return lift(new OperatorDematerialize());
    }

    /**
     * Returns an Observable that emits all items emitted by the source Observable that are distinct.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/distinct.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code distinct} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that emits only those items emitted by the source Observable that are distinct from
     *         each other
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#distinct">RxJava wiki: distinct</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229764.aspx">MSDN: Observable.distinct</a>
     */
    public final Observable<T> distinct() {
        return lift(new OperatorDistinct<T, T>(Functions.<T>identity()));
    }

    /**
     * Returns an Observable that emits all items emitted by the source Observable that are distinct according
     * to a key selector function.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/distinct.key.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code distinct} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param keySelector
     *            a function that projects an emitted item to a key value that is used to decide whether an item
     *            is distinct from another one or not
     * @return an Observable that emits those items emitted by the source Observable that have distinct keys
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#distinct">RxJava wiki: distinct</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh244310.aspx">MSDN: Observable.distinct</a>
     */
    public final <U> Observable<T> distinct(Func1<? super T, ? extends U> keySelector) {
        return lift(new OperatorDistinct<T, U>(keySelector));
    }

    /**
     * Returns an Observable that emits all items emitted by the source Observable that are distinct from their
     * immediate predecessors.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/distinctUntilChanged.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code distinctUntilChanged} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that emits those items from the source Observable that are distinct from their
     *         immediate predecessors
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#distinctuntilchanged">RxJava wiki: distinctUntilChanged</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229494.aspx">MSDN: Observable.distinctUntilChanged</a>
     */
    public final Observable<T> distinctUntilChanged() {
        return lift(new OperatorDistinctUntilChanged<T, T>(Functions.<T>identity()));
    }

    /**
     * Returns an Observable that emits all items emitted by the source Observable that are distinct from their
     * immediate predecessors, according to a key selector function.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/distinctUntilChanged.key.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code distinctUntilChanged} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param keySelector
     *            a function that projects an emitted item to a key value that is used to decide whether an item
     *            is distinct from another one or not
     * @return an Observable that emits those items from the source Observable whose keys are distinct from
     *         those of their immediate predecessors
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#distinctuntilchanged">RxJava wiki: distinctUntilChanged</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229508.aspx">MSDN: Observable.distinctUntilChanged</a>
     */
    public final <U> Observable<T> distinctUntilChanged(Func1<? super T, ? extends U> keySelector) {
        return lift(new OperatorDistinctUntilChanged<T, U>(keySelector));
    }

    /**
     * Modifies the source Observable so that it invokes an action when it calls {@code onCompleted}.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/doOnCompleted.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code doOnCompleted} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param onCompleted
     *            the action to invoke when the source Observable calls {@code onCompleted}
     * @return the source Observable with the side-effecting behavior applied
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#dooncompleted">RxJava wiki: doOnCompleted</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229804.aspx">MSDN: Observable.Do</a>
     */
    public final Observable<T> doOnCompleted(final Action0 onCompleted) {
        Observer<T> observer = new Observer<T>() {
            @Override
            public final void onCompleted() {
                onCompleted.call();
            }

            @Override
            public final void onError(Throwable e) {
            }

            @Override
            public final void onNext(T args) {
            }

        };

        return lift(new OperatorDoOnEach<T>(observer));
    }

    /**
     * Modifies the source Observable so that it invokes an action for each item it emits.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/doOnEach.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code doOnEach} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param onNotification
     *            the action to invoke for each item emitted by the source Observable
     * @return the source Observable with the side-effecting behavior applied
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#dooneach">RxJava wiki: doOnEach</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229307.aspx">MSDN: Observable.Do</a>
     */
    public final Observable<T> doOnEach(final Action1<Notification<? super T>> onNotification) {
        Observer<T> observer = new Observer<T>() {
            @Override
            public final void onCompleted() {
                onNotification.call(Notification.createOnCompleted());
            }

            @Override
            public final void onError(Throwable e) {
                onNotification.call(Notification.createOnError(e));
            }

            @Override
            public final void onNext(T v) {
                onNotification.call(Notification.createOnNext(v));
            }

        };

        return lift(new OperatorDoOnEach<T>(observer));
    }

    /**
     * Modifies the source Observable so that it notifies an Observer for each item it emits.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/doOnEach.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code doOnEach} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param observer
     *            the action to invoke for each item emitted by the source Observable
     * @return the source Observable with the side-effecting behavior applied
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#dooneach">RxJava wiki: doOnEach</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229307.aspx">MSDN: Observable.Do</a>
     */
    public final Observable<T> doOnEach(Observer<? super T> observer) {
        return lift(new OperatorDoOnEach<T>(observer));
    }

    /**
     * Modifies the source Observable so that it invokes an action if it calls {@code onError}.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/doOnError.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code doOnError} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param onError
     *            the action to invoke if the source Observable calls {@code onError}
     * @return the source Observable with the side-effecting behavior applied
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#doonerror">RxJava wiki: doOnError</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229804.aspx">MSDN: Observable.Do</a>
     */
    public final Observable<T> doOnError(final Action1<Throwable> onError) {
        Observer<T> observer = new Observer<T>() {
            @Override
            public final void onCompleted() {
            }

            @Override
            public final void onError(Throwable e) {
                onError.call(e);
            }

            @Override
            public final void onNext(T args) {
            }

        };

        return lift(new OperatorDoOnEach<T>(observer));
    }

    /**
     * Modifies the source Observable so that it invokes an action when it calls {@code onNext}.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/doOnNext.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code doOnNext} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param onNext
     *            the action to invoke when the source Observable calls {@code onNext}
     * @return the source Observable with the side-effecting behavior applied
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#dooneach">RxJava wiki: doOnEach</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229804.aspx">MSDN: Observable.Do</a>
     */
    public final Observable<T> doOnNext(final Action1<? super T> onNext) {
        Observer<T> observer = new Observer<T>() {
            @Override
            public final void onCompleted() {
            }

            @Override
            public final void onError(Throwable e) {
            }

            @Override
            public final void onNext(T args) {
                onNext.call(args);
            }

        };

        return lift(new OperatorDoOnEach<T>(observer));
    }

    /**
     * Modifies the source Observable so that it invokes an action when it calls {@code onCompleted} or
     * {@code onError}.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/doOnTerminate.png" alt="">
     * <p>
     * This differs from {@code finallyDo} in that this happens <em>before</em> the {@code onCompleted} or
     * {@code onError} notification.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code doOnTerminate} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param onTerminate
     *            the action to invoke when the source Observable calls {@code onCompleted} or {@code onError}
     * @return the source Observable with the side-effecting behavior applied
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#doonterminate">RxJava wiki: doOnTerminate</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229804.aspx">MSDN: Observable.Do</a>
     * @see #finallyDo(Action0)
     * @since 0.17
     */
    public final Observable<T> doOnTerminate(final Action0 onTerminate) {
        Observer<T> observer = new Observer<T>() {
            @Override
            public final void onCompleted() {
                onTerminate.call();
            }

            @Override
            public final void onError(Throwable e) {
                onTerminate.call();
            }

            @Override
            public final void onNext(T args) {
            }

        };

        return lift(new OperatorDoOnEach<T>(observer));
    }

    /**
     * Returns an Observable that emits the single item at a specified index in a sequence of emissions from a
     * source Observbable.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/elementAt.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code elementAt} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param index
     *            the zero-based index of the item to retrieve
     * @return an Observable that emits a single item: the item at the specified position in the sequence of
     *         those emitted by the source Observable
     * @throws IndexOutOfBoundsException
     *             if {@code index} is greater than or equal to the number of items emitted by the source
     *             Observable, or
     *             if {@code index} is less than 0
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#elementat">RxJava wiki: elementAt</a>
     */
    public final Observable<T> elementAt(int index) {
        return lift(new OperatorElementAt<T>(index));
    }

    /**
     * Returns an Observable that emits the item found at a specified index in a sequence of emissions from a
     * source Observable, or a default item if that index is out of range.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/elementAtOrDefault.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code elementAtOrDefault} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param index
     *            the zero-based index of the item to retrieve
     * @param defaultValue
     *            the default item
     * @return an Observable that emits the item at the specified position in the sequence emitted by the source
     *         Observable, or the default item if that index is outside the bounds of the source sequence
     * @throws IndexOutOfBoundsException
     *             if {@code index} is less than 0
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#elementatordefault">RxJava wiki: elementAtOrDefault</a>
     */
    public final Observable<T> elementAtOrDefault(int index, T defaultValue) {
        return lift(new OperatorElementAt<T>(index, defaultValue));
    }

    /**
     * Returns an Observable that emits {@code true} if any item emitted by the source Observable satisfies a
     * specified condition, otherwise {@code false}. <em>Note:</em> this always emits {@code false} if the
     * source Observable is empty.
     * <p>
     * <img width="640" height="320" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/exists.png" alt="">
     * <p>
     * In Rx.Net this is the {@code any} Observer but we renamed it in RxJava to better match Java naming
     * idioms.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code exists} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param predicate
     *            the condition to test items emitted by the source Observable
     * @return an Observable that emits a Boolean that indicates whether any item emitted by the source
     *         Observable satisfies the {@code predicate}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#exists-and-isempty">RxJava wiki: exists</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211993.aspx">MSDN: Observable.Any (Note: the description in this page was wrong at the time of this writing)</a>
     */
    public final Observable<Boolean> exists(Func1<? super T, Boolean> predicate) {
        return lift(new OperatorAny<T>(predicate, false));
    }

    /**
     * Filters items emitted by an Observable by only emitting those that satisfy a specified predicate.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/filter.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code filter} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param predicate
     *            a function that evaluates each item emitted by the source Observable, returning {@code true}
     *            if it passes the filter
     * @return an Observable that emits only those items emitted by the source Observable that the filter
     *         evaluates as {@code true}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#filter-or-where">RxJava wiki: filter</a>
     */
    public final Observable<T> filter(Func1<? super T, Boolean> predicate) {
        return lift(new OperatorFilter<T>(predicate));
    }

    /**
     * Registers an {@link Action0} to be called when this Observable invokes either
     * {@link Observer#onCompleted onCompleted} or {@link Observer#onError onError}.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/finallyDo.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code finallyDo} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param action
     *            an {@link Action0} to be invoked when the source Observable finishes
     * @return an Observable that emits the same items as the source Observable, then invokes the
     *         {@link Action0}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#finallydo">RxJava wiki: finallyDo</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212133.aspx">MSDN: Observable.Finally</a>
     * @see #doOnTerminate(Action0)
     */
    public final Observable<T> finallyDo(Action0 action) {
        return lift(new OperatorFinally<T>(action));
    }

    /**
     * Returns an Observable that emits only the very first item emitted by the source Observable, or notifies
     * of an {@code NoSuchElementException} if the source Observable is empty.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/first.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code first} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that emits only the very first item emitted by the source Observable, or raises an
     *         {@code NoSuchElementException} if the source Observable is empty
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#first-and-takefirst">RxJava wiki: first</a>
     * @see "MSDN: Observable.firstAsync"
     */
    public final Observable<T> first() {
        return take(1).single();
    }

    /**
     * Returns an Observable that emits only the very first item emitted by the source Observable that satisfies
     * a specified condition, or notifies of an {@code NoSuchElementException} if no such items are emitted.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/firstN.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code first} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param predicate
     *            the condition that an item emitted by the source Observable has to satisfy
     * @return an Observable that emits only the very first item emitted by the source Observable that satisfies
     *         the {@code predicate}, or raises an {@code NoSuchElementException} if no such items are emitted
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#first-and-takefirst">RxJava wiki: takeFirst</a>
     * @see "MSDN: Observable.firstAsync"
     */
    public final Observable<T> first(Func1<? super T, Boolean> predicate) {
        return takeFirst(predicate).single();
    }

    /**
     * Returns an Observable that emits only the very first item emitted by the source Observable, or a default
     * item if the source Observable completes without emitting anything.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/firstOrDefault.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code firstOrDefault} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param defaultValue
     *            the default item to emit if the source Observable doesn't emit anything
     * @return an Observable that emits only the very first item from the source, or a default item if the
     *         source Observable completes without emitting any items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#firstordefault">RxJava wiki: firstOrDefault</a>
     * @see "MSDN: Observable.firstOrDefaultAsync"
     */
    public final Observable<T> firstOrDefault(T defaultValue) {
        return take(1).singleOrDefault(defaultValue);
    }

    /**
     * Returns an Observable that emits only the very first item emitted by the source Observable that satisfies
     * a specified condition, or a default item if the source Observable emits no such items.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/firstOrDefaultN.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code firstOrDefault} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param predicate
     *            the condition any item emitted by the source Observable has to satisfy
     * @param defaultValue
     *            the default item to emit if the source Observable doesn't emit anything that satisfies the
     *            {@code predicate}
     * @return an Observable that emits only the very first item emitted by the source Observable that satisfies
     *         the {@code predicate}, or a default item if the source Observable emits no such items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#firstordefault">RxJava wiki: firstOrDefault</a>
     * @see "MSDN: Observable.firstOrDefaultAsync"
     */
    public final Observable<T> firstOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return takeFirst(predicate).singleOrDefault(defaultValue);
    }

    /**
     * Returns an Observable that emits items based on applying a function that you supply to each item emitted
     * by the source Observable, where that function returns an Observable, and then merging those resulting
     * Observables and emitting the results of this merger.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/flatMap.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code flatMap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param func
     *            a function that, when applied to an item emitted by the source Observable, returns an
     *            Observable
     * @return an Observable that emits the result of applying the transformation function to each item emitted
     *         by the source Observable and merging the results of the Observables obtained from this
     *         transformation
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#flatmap-and-concatmap">RxJava wiki: flatMap</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.selectmany.aspx">MSDN: Observable.SelectMany</a>
     */
    public final <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends R>> func) {
        return mergeMap(func);
    }

    /**
     * Subscribes to the {@link Observable} and receives notifications for each element.
     * <p>
     * Alias to {@link #subscribe(Action1)}
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code forEach} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param onNext
     *            {@link Action1} to execute for each item.
     * @throws IllegalArgumentException
     *             if {@code onNext} is null, or
     *             if {@code onError} is null, or
     *             if {@code onComplete} is null
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable#onnext-oncompleted-and-onerror">RxJava wiki: onNext, onCompleted, and onError</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211815.aspx">MSDN: Observable.ForEach</a>
     * @since 0.19
     */
    public final void forEach(final Action1<? super T> onNext) {
        subscribe(onNext);
    }
    
    /**
     * Subscribes to the {@link Observable} and receives notifications for each element and error events.
     * <p>
     * Alias to {@link #subscribe(Action1, Action1)}
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code forEach} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param onNext
     *            {@link Action1} to execute for each item.
     * @param onError
     *            {@link Action1} to execute when an error is emitted.
     * @throws IllegalArgumentException
     *             if {@code onNext} is null, or
     *             if {@code onError} is null, or
     *             if {@code onComplete} is null
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable#onnext-oncompleted-and-onerror">RxJava wiki: onNext, onCompleted, and onError</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211815.aspx">MSDN: Observable.ForEach</a>
     * @since 0.19
     */
    public final void forEach(final Action1<? super T> onNext, final Action1<Throwable> onError) {
        subscribe(onNext, onError);
    }
    
    /**
     * Subscribes to the {@link Observable} and receives notifications for each element and the terminal events.
     * <p>
     * Alias to {@link #subscribe(Action1, Action1, Action0)}
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code forEach} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param onNext
     *            {@link Action1} to execute for each item.
     * @param onError
     *            {@link Action1} to execute when an error is emitted.
     * @param onComplete
     *            {@link Action0} to execute when completion is signalled.
     * @throws IllegalArgumentException
     *             if {@code onNext} is null, or
     *             if {@code onError} is null, or
     *             if {@code onComplete} is null
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable#onnext-oncompleted-and-onerror">RxJava wiki: onNext, onCompleted, and onError</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211815.aspx">MSDN: Observable.ForEach</a>
     * @since 0.19
     */
    public final void forEach(final Action1<? super T> onNext, final Action1<Throwable> onError, final Action0 onComplete) {
        subscribe(onNext, onError, onComplete);
    }
    
    /**
     * Groups the items emitted by an {@code Observable} according to a specified criterion, and emits these
     * grouped items as {@link GroupedObservable}s, one {@code GroupedObservable} per group.
     * <p>
     * <img width="640" height="360" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/groupBy.png" alt="">
     * <p>
     * <em>Note:</em> A {@link GroupedObservable} will cache the items it is to emit until such time as it
     * is subscribed to. For this reason, in order to avoid memory leaks, you should not simply ignore those
     * {@code GroupedObservable}s that do not concern you. Instead, you can signal to them that they may
     * discard their buffers by applying an operator like {@link #take}{@code (0)} to them.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as splitting a stream effectively turns it into a "hot
     *      observable" and blocking any one group would block the entire parent stream. If you need
     *      backpressure on individual groups then you should use operators such as {@link #onBackpressureDrop}
     *      or {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code groupBy} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param keySelector
     *            a function that extracts the key for each item
     * @param <K>
     *            the key type
     * @return an {@code Observable} that emits {@link GroupedObservable}s, each of which corresponds to a
     *         unique key value and each of which emits those items from the source Observable that share that
     *         key value
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#groupby-and-groupbyuntil">RxJava wiki: groupBy</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.groupby.aspx">MSDN: Observable.GroupBy</a>
     */
    public final <K> Observable<GroupedObservable<K, T>> groupBy(final Func1<? super T, ? extends K> keySelector) {
        return lift(new OperatorGroupBy<K, T>(keySelector));
    }

    /**
     * Groups the items emitted by an {@code Observable} according to a specified key selector function until
     * the duration {@code Observable} expires for the key.
     * <p>
     * <img width="640" height="375" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/groupByUntil.png" alt="">
     * <p>
     * <em>Note:</em> A {@link GroupedObservable} will cache the items it is to emit until such time as it
     * is subscribed to. For this reason, in order to avoid memory leaks, you should not simply ignore those
     * {@code GroupedObservable}s that do not concern you. Instead, you can signal to them that they may
     * discard their buffers by applying an operator like {@link #take}{@code (0)} to them.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as splitting a stream effectively turns it into a "hot
     *      observable" and blocking any one group would block the entire parent stream. If you need
     *      backpressure on individual groups then you should use operators such as {@link #onBackpressureDrop}
     *      or {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code groupByUntil} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param keySelector
     *            a function to extract the key for each item
     * @param durationSelector
     *            a function to signal the expiration of a group
     * @return an {@code Observable} that emits {@link GroupedObservable}s, each of which corresponds to a key
     *         value and each of which emits all items emitted by the source {@code Observable} during that
     *         key's duration that share that same key value
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#groupby-and-groupbyuntil">RxJava wiki: groupByUntil</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211932.aspx">MSDN: Observable.GroupByUntil</a>
     */
    public final <TKey, TDuration> Observable<GroupedObservable<TKey, T>> groupByUntil(Func1<? super T, ? extends TKey> keySelector, Func1<? super GroupedObservable<TKey, T>, ? extends Observable<? extends TDuration>> durationSelector) {
        return groupByUntil(keySelector, Functions.<T> identity(), durationSelector);
    }

    /**
     * Groups the items emitted by an {@code Observable} (transformed by a selector) according to a specified
     * key selector function until the duration Observable expires for the key.
     * <p>
     * <img width="640" height="375" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/groupByUntil.png" alt="">
     * <p>
     * <em>Note:</em> A {@link GroupedObservable} will cache the items it is to emit until such time as it
     * is subscribed to. For this reason, in order to avoid memory leaks, you should not simply ignore those
     * {@code GroupedObservable}s that do not concern you. Instead, you can signal to them that they may
     * discard their buffers by applying an operator like {@link #take}{@code (0)} to them.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as splitting a stream effectively turns it into a "hot
     *      observable" and blocking any one group would block the entire parent stream. If you need
     *      backpressure on individual groups then you should use operators such as {@link #onBackpressureDrop}
     *      or {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code groupByUntil} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param keySelector
     *            a function to extract the key for each item
     * @param valueSelector
     *            a function to map each item emitted by the source {@code Observable} to an item emitted by one
     *            of the resulting {@link GroupedObservable}s
     * @param durationSelector
     *            a function to signal the expiration of a group
     * @return an {@code Observable} that emits {@link GroupedObservable}s, each of which corresponds to a key
     *         value and each of which emits all items emitted by the source {@code Observable} during that
     *         key's duration that share that same key value, transformed by the value selector
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#groupby-and-groupbyuntil">RxJava wiki: groupByUntil</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229433.aspx">MSDN: Observable.GroupByUntil</a>
     */
    public final <TKey, TValue, TDuration> Observable<GroupedObservable<TKey, TValue>> groupByUntil(Func1<? super T, ? extends TKey> keySelector, Func1<? super T, ? extends TValue> valueSelector, Func1<? super GroupedObservable<TKey, TValue>, ? extends Observable<? extends TDuration>> durationSelector) {
        return lift(new OperatorGroupByUntil<T, TKey, TValue, TDuration>(keySelector, valueSelector, durationSelector));
    }

    /**
     * Returns an Observable that correlates two Observables when they overlap in time and groups the results.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/groupJoin.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code groupJoin} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param right
     *            the other Observable to correlate items from the source Observable with
     * @param leftDuration
     *            a function that returns an Observable whose emissions indicate the duration of the values of
     *            the source Observable
     * @param rightDuration
     *            a function that returns an Observable whose emissions indicate the duration of the values of
     *            the {@code right} Observable
     * @param resultSelector
     *            a function that takes an item emitted by each Observable and returns the value to be emitted
     *            by the resulting Observable
     * @return an Observable that emits items based on combining those items emitted by the source Observables
     *         whose durations overlap
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#join-and-groupjoin">RxJava Wiiki: groupJoin</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh244235.aspx">MSDN: Observable.GroupJoin</a>
     */
    public final <T2, D1, D2, R> Observable<R> groupJoin(Observable<T2> right, Func1<? super T, ? extends Observable<D1>> leftDuration,
            Func1<? super T2, ? extends Observable<D2>> rightDuration,
            Func2<? super T, ? super Observable<T2>, ? extends R> resultSelector) {
        return create(new OnSubscribeGroupJoin<T, T2, D1, D2, R>(this, right, leftDuration, rightDuration, resultSelector));
    }

    /**
     * Ignores all items emitted by the source Observable and only calls {@code onCompleted} or {@code onError}.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/ignoreElements.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code ignoreElements} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an empty Observable that only calls {@code onCompleted} or {@code onError}, based on which one is
     *         called by the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#ignoreelements">RxJava wiki: ignoreElements</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229242.aspx">MSDN: Observable.IgnoreElements</a>
     */
    public final Observable<T> ignoreElements() {
        return filter(alwaysFalse());
    }

    /**
     * Returns an Observable that emits {@code true} if the source Observable is empty, otherwise {@code false}.
     * <p>
     * In Rx.Net this is negated as the {@code any} Observer but we renamed this in RxJava to better match Java
     * naming idioms.
     * <p>
     * <img width="640" height="320" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/isEmpty.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code isEmpty} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that emits a Boolean
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#exists-and-isempty">RxJava wiki: isEmpty</a>
     * @see <a href= "http://msdn.microsoft.com/en-us/library/hh229905.aspx">MSDN: Observable.Any</a>
     */
    public final Observable<Boolean> isEmpty() {
        return lift(new OperatorAny<T>(Functions.alwaysTrue(), true));
    }

    /**
     * Correlates the items emitted by two Observables based on overlapping durations.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/join_.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code join} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param right
     *            the second Observable to join items from
     * @param leftDurationSelector
     *            a function to select a duration for each item emitted by the source Observable, used to
     *            determine overlap
     * @param rightDurationSelector
     *            a function to select a duration for each item emitted by the {@code right} Observable, used to
     *            determine overlap
     * @param resultSelector
     *            a function that computes an item to be emitted by the resulting Observable for any two
     *            overlapping items emitted by the two Observables
     * @return an Observable that emits items correlating to items emitted by the source Observables that have
     *         overlapping durations
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#join-and-groupjoin">RxJava wiki: join</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229750.aspx">MSDN: Observable.Join</a>
     */
    public final <TRight, TLeftDuration, TRightDuration, R> Observable<R> join(Observable<TRight> right, Func1<T, Observable<TLeftDuration>> leftDurationSelector,
            Func1<TRight, Observable<TRightDuration>> rightDurationSelector,
            Func2<T, TRight, R> resultSelector) {
        return create(new OnSubscribeJoin<T, TRight, TLeftDuration, TRightDuration, R>(this, right, leftDurationSelector, rightDurationSelector, resultSelector));
    }

    /**
     * Returns an Observable that emits the last item emitted by the source Observable or notifies observers of
     * a {@code NoSuchElementException} if the source Observable is empty.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/last.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code last} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that emits the last item from the source Observable or notifies observers of an
     *         error
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#last">RxJava wiki: last</a>
     * @see "MSDN: Observable.lastAsync"
     */
    public final Observable<T> last() {
        return takeLast(1).single();
    }

    /**
     * Returns an Observable that emits only the last item emitted by the source Observable that satisfies a
     * given condition, or notifies of a {@code NoSuchElementException} if no such items are emitted.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/last.p.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code last} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param predicate
     *            the condition any source emitted item has to satisfy
     * @return an Observable that emits only the last item satisfying the given condition from the source, or an
     *         {@code NoSuchElementException} if no such items are emitted
     * @throws IllegalArgumentException
     *             if no items that match the predicate are emitted by the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#last">RxJava wiki: last</a>
     * @see "MSDN: Observable.lastAsync"
     */
    public final Observable<T> last(Func1<? super T, Boolean> predicate) {
        return filter(predicate).takeLast(1).single();
    }

    /**
     * Returns an Observable that emits only the last item emitted by the source Observable, or a default item
     * if the source Observable completes without emitting any items.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/lastOrDefault.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code lastOrDefault} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param defaultValue
     *            the default item to emit if the source Observable is empty
     * @return an Observable that emits only the last item emitted by the source Observable, or a default item
     *         if the source Observable is empty
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#lastordefault">RxJava wiki: lastOrDefault</a>
     * @see "MSDN: Observable.lastOrDefaultAsync"
     */
    public final Observable<T> lastOrDefault(T defaultValue) {
        return takeLast(1).singleOrDefault(defaultValue);
    }

    /**
     * Returns an Observable that emits only the last item emitted by the source Observable that satisfies a
     * specified condition, or a default item if no such item is emitted by the source Observable.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/lastOrDefault.p.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code lastOrDefault} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param defaultValue
     *            the default item to emit if the source Observable doesn't emit anything that satisfies the
     *            specified {@code predicate}
     * @param predicate
     *            the condition any item emitted by the source Observable has to satisfy
     * @return an Observable that emits only the last item emitted by the source Observable that satisfies the
     *         given condition, or a default item if no such item is emitted by the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#lastordefault">RxJava wiki: lastOrDefault</a>
     * @see "MSDN: Observable.lastOrDefaultAsync"
     */
    public final Observable<T> lastOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return filter(predicate).takeLast(1).singleOrDefault(defaultValue);
    }

    /**
     * Returns an Observable that emits only the first {@code num} items emitted by the source Observable.
     * <p>
     * Alias of {@link #take(int)} to match Java 8 Stream API naming convention.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/take.png" alt="">
     * <p>
     * This method returns an Observable that will invoke a subscribing {@link Observer}'s
     * {@link Observer#onNext onNext} function a maximum of {@code num} times before invoking
     * {@link Observer#onCompleted onCompleted}.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code limit} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param num
     *            the maximum number of items to emit
     * @return an Observable that emits only the first {@code num} items emitted by the source Observable, or
     *         all of the items from the source Observable if that Observable emits fewer than {@code num} items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#take">RxJava wiki: take</a>
     * @since 0.19
     */
    public final Observable<T> limit(int num) {
        return take(num);
    }
    
    /**
     * Returns an Observable that counts the total number of items emitted by the source Observable and emits
     * this count as a 64-bit Long.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/longCount.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because by intent it will receive all values and reduce
     *      them to a single {@code onNext}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code longCount} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that emits a single item: the number of items emitted by the source Observable as a
     *         64-bit Long item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#count-and-longcount">RxJava wiki: count</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229120.aspx">MSDN: Observable.LongCount</a>
     * @see #count()
     */
    public final Observable<Long> longCount() {
        return reduce(0L, new Func2<Long, T, Long>() {
            @Override
            public final Long call(Long t1, T t2) {
                return t1 + 1;
            }
        });
    }

    /**
     * Returns an Observable that applies a specified function to each item emitted by the source Observable and
     * emits the results of these function applications.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/map.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code map} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param func
     *            a function to apply to each item emitted by the Observable
     * @return an Observable that emits the items from the source Observable, transformed by the specified
     *         function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#map">RxJava wiki: map</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh244306.aspx">MSDN: Observable.Select</a>
     */
    public final <R> Observable<R> map(Func1<? super T, ? extends R> func) {
        return lift(new OperatorMap<T, R>(func));
    }

    /**
     * Returns an Observable that represents all of the emissions <em>and</em> notifications from the source
     * Observable into emissions marked with their original types within {@link Notification} objects.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/materialize.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code materialize} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that emits items that are the result of materializing the items and notifications
     *         of the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#materialize">RxJava wiki: materialize</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229453.aspx">MSDN: Observable.Materialize</a>
     */
    public final Observable<Notification<T>> materialize() {
        return lift(new OperatorMaterialize<T>());
    }

    /**
     * Returns an Observable that emits the results of applying a specified function to each item emitted by the
     * source Observable, where that function returns an Observable, and then merging those resulting
     * Observables and emitting the results of this merger.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/mergeMap.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code mergeMap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param func
     *            a function that, when applied to an item emitted by the source Observable, returns an
     *            Observable
     * @return an Observable that emits the result of applying the transformation function to each item emitted
     *         by the source Observable and merging the results of the Observables obtained from these
     *         transformations
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#mergemap-and-mergemapiterable">RxJava wiki: mergeMap</a>
     * @see #flatMap(Func1)
     */
    public final <R> Observable<R> mergeMap(Func1<? super T, ? extends Observable<? extends R>> func) {
        return merge(map(func));
    }

    /**
     * Returns an Observable that applies a function to each item emitted or notification raised by the source
     * Observable and then flattens the Observables returned from these functions and emits the resulting items.
     * <p>
     * <img width="640" height="410" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/mergeMap.nce.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code mergeMap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <R>
     *            the result type
     * @param onNext
     *            a function that returns an Observable to merge for each item emitted by the source Observable
     * @param onError
     *            a function that returns an Observable to merge for an onError notification from the source
     *            Observable
     * @param onCompleted
     *            a function that returns an Observable to merge for an onCompleted notification from the source
     *            Observable
     * @return an Observable that emits the results of merging the Observables returned from applying the
     *         specified functions to the emissions and notifications of the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#mergemap-and-mergemapiterable">RxJava wiki: mergeMap</a>
     */
    public final <R> Observable<R> mergeMap(
            Func1<? super T, ? extends Observable<? extends R>> onNext,
            Func1<? super Throwable, ? extends Observable<? extends R>> onError,
            Func0<? extends Observable<? extends R>> onCompleted) {
        return lift(new OperatorMergeMapTransform<T, R>(onNext, onError, onCompleted));
    }

    /**
     * Returns an Observable that emits the results of a specified function to the pair of values emitted by the
     * source Observable and a specified collection Observable.
     * <p>
     * <img width="640" height="390" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/mergeMap.r.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code mergeMap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <U>
     *            the type of items emitted by the collection Observable
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param collectionSelector
     *            a function that returns an Observable for each item emitted by the source Observable
     * @param resultSelector
     *            a function that combines one item emitted by each of the source and collection Observables and
     *            returns an item to be emitted by the resulting Observable
     * @return an Observable that emits the results of applying a function to a pair of values emitted by the
     *         source Observable and the collection Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#mergemap-and-mergemapiterable">RxJava wiki: mergeMap</a>
     */
    public final <U, R> Observable<R> mergeMap(Func1<? super T, ? extends Observable<? extends U>> collectionSelector,
            Func2<? super T, ? super U, ? extends R> resultSelector) {
        return lift(new OperatorMergeMapPair<T, U, R>(collectionSelector, resultSelector));
    }

    /**
     * Returns an Observable that merges each item emitted by the source Observable with the values in an
     * Iterable corresponding to that item that is generated by a selector.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/mergeMapIterable.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code mergeMapIterable} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <R>
     *            the type of item emitted by the resulting Observable
     * @param collectionSelector
     *            a function that returns an Iterable sequence of values for when given an item emitted by the
     *            source Observable
     * @return an Observable that emits the results of merging the items emitted by the source Observable with
     *         the values in the Iterables corresponding to those items, as generated by {@code collectionSelector}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#mergemap-and-mergemapiterable">RxJava wiki: mergeMapIterable</a>
     */
    public final <R> Observable<R> mergeMapIterable(Func1<? super T, ? extends Iterable<? extends R>> collectionSelector) {
        return merge(map(OperatorMergeMapPair.convertSelector(collectionSelector)));
    }

    /**
     * Returns an Observable that emits the results of applying a function to the pair of values from the source
     * Observable and an Iterable corresponding to that item that is generated by a selector.
     * <p>
     * <img width="640" height="390" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/mergeMapIterable.r.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code mergeMapIterable} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <U>
     *            the collection element type
     * @param <R>
     *            the type of item emited by the resulting Observable
     * @param collectionSelector
     *            a function that returns an Iterable sequence of values for each item emitted by the source
     *            Observable
     * @param resultSelector
     *            a function that returns an item based on the item emitted by the source Observable and the
     *            Iterable returned for that item by the {@code collectionSelector}
     * @return an Observable that emits the items returned by {@code resultSelector} for each item in the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#mergemap-and-mergemapiterable">RxJava wiki: mergeMapIterable</a>
     */
    public final <U, R> Observable<R> mergeMapIterable(Func1<? super T, ? extends Iterable<? extends U>> collectionSelector,
            Func2<? super T, ? super U, ? extends R> resultSelector) {
        return mergeMap(OperatorMergeMapPair.convertSelector(collectionSelector), resultSelector);
    }

    /**
     * Flattens this and another Observable into a single Observable, without any transformation.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code mergeWith} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code mergeWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            an Observable to be merged
     * @return an Observable that emits all of the items emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     * @since 0.20
     */
    public final Observable<T> mergeWith(Observable<? extends T> t1) {
        return merge(this, t1);
    }
    
    /**
     * Returns an Observable that emits items produced by multicasting the source Observable within a selector
     * function.
     * <p>
     * This is largely a helper function used by RxJava for other forms of multicasting, such as
     * {@link #publish} and {@link #publishLast}.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code multicast} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @warn javadocs incomplete: description needs improvement
     * @param subjectFactory
     *            the {@link Subject} factory
     * @warn javadocs incomplete: "subjectFactory" parameter described poorly
     * @param selector
     *            the selector function, which can use the multicasted source Observable subject to the policies
     *            enforced by the created {@code Subject}
     * @warn javadocs incomplete: "selector" parameter described poorly
     * @return an Observable that emits the items produced by multicasting the source Observable within a
     *         selector function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablepublish-and-observablemulticast">RxJava: Observable.publish() and Observable.multicast</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229708.aspx">MSDN: Observable.Multicast</a>
     */
    public final <TIntermediate, TResult> Observable<TResult> multicast(
            final Func0<? extends Subject<? super T, ? extends TIntermediate>> subjectFactory,
            final Func1<? super Observable<TIntermediate>, ? extends Observable<TResult>> selector) {
        return create(new OnSubscribeMulticastSelector<T, TIntermediate, TResult>(this, subjectFactory, selector));
    }

    /**
     * Returns a {@link ConnectableObservable} that upon connection causes the source Observable to push results
     * into the specified subject. A Connectable Observable resembles an ordinary Observable, except that it
     * does not begin emitting items when it is subscribed to, but only when its {@code connect} method
     * is called.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code multicast} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param subject
     *            the {@link Subject} for the {@link ConnectableObservable} to push source items into
     * @param <R>
     *            the type of items emitted by the resulting {@code ConnectableObservable}
     * @return a {@link ConnectableObservable} that upon connection causes the source Observable to push results
     *         into the specified {@link Subject}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablepublish-and-observablemulticast">RxJava wiki: Observable.publish and Observable.multicast</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229708.aspx">MSDN: Observable.Multicast</a>
     */
    public final <R> ConnectableObservable<R> multicast(final Subject<? super T, ? extends R> subject) {
        return new OperatorMulticast<T, R>(this, new Func0<Subject<? super T, ? extends R>>() {

            @Override
            public Subject<? super T, ? extends R> call() {
                // same one every time, no factory behavior
                return subject;
            }
            
        });
    }
    
    /**
     * Returns a {@link ConnectableObservable} that upon connection causes the source Observable to push results
     * into the specified subject. A Connectable Observable resembles an ordinary Observable, except that it
     * does not begin emitting items when it is subscribed to, but only when its {@code connect} method
     * is called.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code multicast} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param subjectFactory
     *            a function that creates a new {@link Subject} for the {@link ConnectableObservable} to push
     *            source items into
     * @param <R>
     *            the type of items emitted by the resulting {@code ConnectableObservable}
     * @return a {@link ConnectableObservable} that upon connection causes the source Observable to push results
     *         into the specified {@link Subject}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablepublish-and-observablemulticast">RxJava wiki: Observable.publish and Observable.multicast</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229708.aspx">MSDN: Observable.Multicast</a>
     * @since 0.20
     */
    public final <R> ConnectableObservable<R> multicast(Func0<? extends Subject<? super T, ? extends R>> subjectFactory) {
        return new OperatorMulticast<T, R>(this, subjectFactory);
    }

    /**
     * Modifies an Observable to perform its emissions and notifications on a specified {@link Scheduler},
     * asynchronously with an unbounded buffer.
     * <p>
     * <img width="640" height="308" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/observeOn.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param scheduler
     *            the {@link Scheduler} to notify {@link Observer}s on
     * @return the source Observable modified so that its {@link Observer}s are notified on the specified
     *         {@link Scheduler}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#observeon">RxJava wiki: observeOn</a>
     * @see <a href="http://www.grahamlea.com/2014/07/rxjava-threading-examples/">RxJava Threading Examples</a>
     * @see #subscribeOn
     */
    public final Observable<T> observeOn(Scheduler scheduler) {
        return lift(new OperatorObserveOn<T>(scheduler));
    }

    /**
     * Filters the items emitted by an Observable, only emitting those of the specified type.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/ofClass.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code ofType} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param klass
     *            the class type to filter the items emitted by the source Observable
     * @return an Observable that emits items from the source Observable of type {@code klass}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#oftype">RxJava wiki: ofType</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229380.aspx">MSDN: Observable.OfType</a>
     */
    public final <R> Observable<R> ofType(final Class<R> klass) {
        return filter(new Func1<T, Boolean>() {
            public final Boolean call(T t) {
                return klass.isInstance(t);
            }
        }).cast(klass);
    }

    /**
     * Instructs an Observable that is emitting items faster than its observer can consume them to buffer these
     * items indefinitely until they can be emitted.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code onBackpressureBuffer} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @return the source Observable modified to buffer items to the extent system resources allow
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Backpressure">RxJava wiki: Backpressure</a>
     * @since 0.20
     */
    public final Observable<T> onBackpressureBuffer() {
        return lift(new OperatorOnBackpressureBuffer<T>());
    }

    /**
     * Use this operator when the upstream does not natively support backpressure and you wish to drop
     * {@code onNext} when unable to handle further events.
     * <p>
     * If the downstream request count hits 0 then {@code onNext} will be dropped until {@code request(long n)}
     * is invoked again to increase the request count.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code onBackpressureDrop} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return the source Observable modified to drop {@code onNext} notifications on overflow
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Backpressure">RxJava wiki: Backpressure</a>
     * @since 0.20
     */
    public final Observable<T> onBackpressureDrop() {
        return lift(new OperatorOnBackpressureDrop<T>());
    }
    
    /**
     * Instructs an Observable to pass control to another Observable rather than invoking
     * {@link Observer#onError onError} if it encounters an error.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/onErrorResumeNext.png" alt="">
     * <p>
     * By default, when an Observable encounters an error that prevents it from emitting the expected item to
     * its {@link Observer}, the Observable invokes its Observer's {@code onError} method, and then quits
     * without invoking any more of its Observer's methods. The {@code onErrorResumeNext} method changes this
     * behavior. If you pass a function that returns an Observable ({@code resumeFunction}) to
     * {@code onErrorResumeNext}, if the original Observable encounters an error, instead of invoking its
     * Observer's {@code onError} method, it will instead relinquish control to the Observable returned from
     * {@code resumeFunction}, which will invoke the Observer's {@link Observer#onNext onNext} method if it is
     * able to do so. In such a case, because no Observable necessarily invokes {@code onError}, the Observer
     * may never know that an error happened.
     * <p>
     * You can use this to prevent errors from propagating or to supply fallback data should errors be
     * encountered.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code onErrorResumeNext} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param resumeFunction
     *            a function that returns an Observable that will take over if the source Observable encounters
     *            an error
     * @return the original Observable, with appropriately modified behavior
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Error-Handling-Operators#onerrorresumenext">RxJava wiki: onErrorResumeNext</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.onerrorresumenext.aspx">MSDN: Observable.OnErrorResumeNext</a>
     */
    public final Observable<T> onErrorResumeNext(final Func1<Throwable, ? extends Observable<? extends T>> resumeFunction) {
        return lift(new OperatorOnErrorResumeNextViaFunction<T>(resumeFunction));
    }

    /**
     * Instructs an Observable to pass control to another Observable rather than invoking
     * {@link Observer#onError onError} if it encounters an error.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/onErrorResumeNext.png" alt="">
     * <p>
     * By default, when an Observable encounters an error that prevents it from emitting the expected item to
     * its {@link Observer}, the Observable invokes its Observer's {@code onError} method, and then quits
     * without invoking any more of its Observer's methods. The {@code onErrorResumeNext} method changes this
     * behavior. If you pass another Observable ({@code resumeSequence}) to an Observable's
     * {@code onErrorResumeNext} method, if the original Observable encounters an error, instead of invoking its
     * Observer's {@code onError} method, it will instead relinquish control to {@code resumeSequence} which
     * will invoke the Observer's {@link Observer#onNext onNext} method if it is able to do so. In such a case,
     * because no Observable necessarily invokes {@code onError}, the Observer may never know that an error
     * happened.
     * <p>
     * You can use this to prevent errors from propagating or to supply fallback data should errors be
     * encountered.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code onErrorResumeNext} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param resumeSequence
     *            a function that returns an Observable that will take over if the source Observable encounters
     *            an error
     * @return the original Observable, with appropriately modified behavior
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Error-Handling-Operators#onerrorresumenext">RxJava wiki: onErrorResumeNext</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.onerrorresumenext.aspx">MSDN: Observable.OnErrorResumeNext</a>
     */
    public final Observable<T> onErrorResumeNext(final Observable<? extends T> resumeSequence) {
        return lift(new OperatorOnErrorResumeNextViaObservable<T>(resumeSequence));
    }

    /**
     * Instructs an Observable to emit an item (returned by a specified function) rather than invoking
     * {@link Observer#onError onError} if it encounters an error.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/onErrorReturn.png" alt="">
     * <p>
     * By default, when an Observable encounters an error that prevents it from emitting the expected item to
     * its {@link Observer}, the Observable invokes its Observer's {@code onError} method, and then quits
     * without invoking any more of its Observer's methods. The {@code onErrorReturn} method changes this
     * behavior. If you pass a function ({@code resumeFunction}) to an Observable's {@code onErrorReturn}
     * method, if the original Observable encounters an error, instead of invoking its Observer's
     * {@code onError} method, it will instead emit the return value of {@code resumeFunction}.
     * <p>
     * You can use this to prevent errors from propagating or to supply fallback data should errors be
     * encountered.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code onErrorReturn} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param resumeFunction
     *            a function that returns an item that the new Observable will emit if the source Observable
     *            encounters an error
     * @return the original Observable with appropriately modified behavior
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Error-Handling-Operators#onerrorreturn">RxJava wiki: onErrorReturn</a>
     */
    public final Observable<T> onErrorReturn(Func1<Throwable, ? extends T> resumeFunction) {
        return lift(new OperatorOnErrorReturn<T>(resumeFunction));
    }

    /**
     * Intercepts {@code onError} notifications from the source Observable and replaces them with the
     * {@code onNext} emissions of an Observable returned by a specified function. This allows the source
     * sequence to continue even if it issues multiple {@code onError} notifications.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/onErrorFlatMap.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code onErrorFlatMap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param resumeFunction
     *            a function that accepts an {@link OnErrorThrowable} representing the Throwable issued by the
     *            source Observable, and returns an Observable that emits items that will be emitted in place
     *            of the error
     * @return the original Observable, with appropriately modified behavior
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Phantom-Operators#onerrorflatmap">RxJava wiki: onErrorFlatMap</a>
     * @since 0.17
     * @deprecated See https://github.com/Netflix/RxJava/issues/1465
     */
    @Deprecated
    public final Observable<T> onErrorFlatMap(final Func1<OnErrorThrowable, ? extends Observable<? extends T>> resumeFunction) {
        return lift(new OperatorOnErrorFlatMap<T>(resumeFunction));
    }

    /**
     * Instructs an Observable to pass control to another Observable rather than invoking
     * {@link Observer#onError onError} if it encounters an {@link java.lang.Exception}.
     * <p>
     * This differs from {@link #onErrorResumeNext} in that this one does not handle {@link java.lang.Throwable}
     * or {@link java.lang.Error} but lets those continue through.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/onExceptionResumeNextViaObservable.png" alt="">
     * <p>
     * By default, when an Observable encounters an exception that prevents it from emitting the expected item
     * to its {@link Observer}, the Observable invokes its Observer's {@code onError} method, and then quits
     * without invoking any more of its Observer's methods. The {@code onExceptionResumeNext} method changes
     * this behavior. If you pass another Observable ({@code resumeSequence}) to an Observable's
     * {@code onExceptionResumeNext} method, if the original Observable encounters an exception, instead of
     * invoking its Observer's {@code onError} method, it will instead relinquish control to
     * {@code resumeSequence} which will invoke the Observer's {@link Observer#onNext onNext} method if it is
     * able to do so. In such a case, because no Observable necessarily invokes {@code onError}, the Observer
     * may never know that an exception happened.
     * <p>
     * You can use this to prevent exceptions from propagating or to supply fallback data should exceptions be
     * encountered.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code onErrorResumeNext} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param resumeSequence
     *            a function that returns an Observable that will take over if the source Observable encounters
     *            an exception
     * @return the original Observable, with appropriately modified behavior
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Error-Handling-Operators#onexceptionresumenext">RxJava wiki: onExceptionResumeNext</a>
     */
    public final Observable<T> onExceptionResumeNext(final Observable<? extends T> resumeSequence) {
        return lift(new OperatorOnExceptionResumeNextViaObservable<T>(resumeSequence));
    }

    /**
     * Performs work on the source Observable in parallel by sharding it on a {@link Schedulers#computation()}
     * {@link Scheduler}, and returns the resulting Observable.
     * <p>
     * <img width="640" height="475" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/parallel.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code parallel} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param f
     *            a {@link Func1} that applies Observable Operators to {@code Observable<T>} in parallel and
     *            returns an {@code Observable<R>}
     * @return an Observable that emits the results of applying {@code f} to the items emitted by the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#parallel">RxJava wiki: parallel</a>
     * @see <a href="http://www.grahamlea.com/2014/07/rxjava-threading-examples/">RxJava Threading Examples</a>
     */
    public final <R> Observable<R> parallel(Func1<Observable<T>, Observable<R>> f) {
        return lift(new OperatorParallel<T, R>(f, Schedulers.computation()));
    }

    /**
     * Performs work on the source Observable<T> in parallel by sharding it on a {@link Scheduler}, and returns
     * the resulting Observable.
     * <p>
     * <img width="640" height="475" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/parallel.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param f
     *            a {@link Func1} that applies Observable Operators to {@code Observable<T>} in parallel and
     *            returns an {@code Observable<R>}
     * @param s
     *            a {@link Scheduler} to perform the work on
     * @return an Observable that emits the results of applying {@code f} to the items emitted by the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#parallel">RxJava wiki: parallel</a>
     * @see <a href="http://www.grahamlea.com/2014/07/rxjava-threading-examples/">RxJava Threading Examples</a>
     */
    public final <R> Observable<R> parallel(final Func1<Observable<T>, Observable<R>> f, final Scheduler s) {
        return lift(new OperatorParallel<T, R>(f, s));
    }

    /**
     * Returns a {@link ConnectableObservable}, which waits until its
     * {@link ConnectableObservable#connect connect} method is called before it begins emitting items to those
     * {@link Observer}s that have subscribed to it.
     * <p>
     * <img width="640" height="510" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/publishConnect.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code publish} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return a {@link ConnectableObservable} that upon connection causes the source Observable to emit items
     *         to its {@link Observer}s
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablepublish-and-observablemulticast">RxJava wiki: publish</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.publish.aspx">MSDN: Observable.Publish</a>
     */
    public final ConnectableObservable<T> publish() {
        return new OperatorMulticast<T, T>(this, new Func0<Subject<? super T, ? extends T>>() {

            @Override
            public Subject<? super T, ? extends T> call() {
                return PublishSubject.<T> create();
            }
            
        });
    }

    /**
     * Returns an Observable that emits the results of invoking a specified selector on items emitted by a
     * {@link ConnectableObservable} that shares a single subscription to the underlying sequence.
     * <p>
     * <img width="640" height="510" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/publishConnect.f.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code publish} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            a function that can use the multicasted source sequence as many times as needed, without
     *            causing multiple subscriptions to the source sequence. Subscribers to the given source will
     *            receive all notifications of the source from the time of the subscription forward.
     * @return an Observable that emits the results of invoking the selector on the items emitted by a {@link ConnectableObservable} that shares a single subscription to the underlying sequence
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablepublish-and-observablemulticast">RxJava wiki: publish</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.publish.aspx">MSDN: Observable.Publish</a>
     */
    public final <R> Observable<R> publish(Func1<? super Observable<T>, ? extends Observable<R>> selector) {
        return multicast(new Func0<Subject<T, T>>() {
            @Override
            public final Subject<T, T> call() {
                return PublishSubject.create();
            }
        }, selector);
    }

    /**
     * Returns an Observable that emits {@code initialValue} followed by the results of invoking a specified
     * selector on items emitted by a {@link ConnectableObservable} that shares a single subscription to the
     * source Observable.
     * <p>
     * <img width="640" height="510" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/publishConnect.if.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code publish} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            a function that can use the multicasted source sequence as many times as needed, without
     *            causing multiple subscriptions to the source Observable. Subscribers to the source will
     *            receive all notifications of the source from the time of the subscription forward
     * @param initialValue
     *            the initial value of the underlying {@link BehaviorSubject}
     * @return an Observable that emits {@code initialValue} followed by the results of invoking the selector
     *         on a {@link ConnectableObservable} that shares a single subscription to the underlying Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablepublish-and-observablemulticast">RxJava wiki: publish</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.publish.aspx">MSDN: Observable.Publish</a>
     */
    public final <R> Observable<R> publish(Func1<? super Observable<T>, ? extends Observable<R>> selector, final T initialValue) {
        return multicast(new Func0<Subject<T, T>>() {
            @Override
            public final Subject<T, T> call() {
                return BehaviorSubject.create(initialValue);
            }
        }, selector);
    }

    /**
     * Returns a {@link ConnectableObservable} that emits {@code initialValue} followed by the items emitted by
     * the source Observable. A Connectable Observable resembles an ordinary Observable, except that it does not
     * begin emitting items when it is subscribed to, but only when its {@code connect} method is called.
     * <p>
     * <img width="640" height="510" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/publishConnect.i.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code publish} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param initialValue
     *            the initial value to be emitted by the resulting Observable
     * @return a {@link ConnectableObservable} that shares a single subscription to the underlying Observable
     *         and starts with {@code initialValue}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablepublish-and-observablemulticast">RxJava wiki: publish</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.publish.aspx">MSDN: Observable.Publish</a>
     */
    public final ConnectableObservable<T> publish(final T initialValue) {
        return new OperatorMulticast<T, T>(this, new Func0<Subject<? super T, ? extends T>>() {

            @Override
            public Subject<? super T, ? extends T> call() {
                return BehaviorSubject.<T> create(initialValue);
            }
            
        });
    }

    /**
     * Returns a {@link ConnectableObservable} that emits only the last item emitted by the source Observable.
     * A Connectable Observable resembles an ordinary Observable, except that it does not begin emitting items
     * when it is subscribed to, but only when its {@code connect} method is called. 
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/publishLast.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because by intent it is skipping all values except the
     *      last.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code publishLast} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return a {@link ConnectableObservable} that emits only the last item emitted by the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablepublishlast">RxJava wiki: publishLast</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.publishlast.aspx">MSDN: Observable.PublishLast</a>
     */
    public final ConnectableObservable<T> publishLast() {
        return new OperatorMulticast<T, T>(this, new Func0<Subject<? super T, ? extends T>>() {

            @Override
            public Subject<? super T, ? extends T> call() {
                return AsyncSubject.<T> create();
            }
            
        });
    }

    /**
     * Returns an Observable that emits an item that results from invoking a specified selector on the last item
     * emitted by a {@link ConnectableObservable} that shares a single subscription to the source Observable.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/publishLast.f.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because by intent it is skipping all values except the
     *      last.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code publishLast} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            a function that can use the multicasted source sequence as many times as needed, without
     *            causing multiple subscriptions to the source Observable. Subscribers to the source will only
     *            receive the last item emitted by the source.
     * @return an Observable that emits an item that is the result of invoking the selector on a {@link ConnectableObservable} that shares a single subscription to the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablepublishlast">RxJava wiki: publishLast</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.publishlast.aspx">MSDN: Observable.PublishLast</a>
     */
    public final <R> Observable<R> publishLast(Func1<? super Observable<T>, ? extends Observable<R>> selector) {
        return multicast(new Func0<Subject<T, T>>() {
            @Override
            public final Subject<T, T> call() {
                return AsyncSubject.create();
            }
        }, selector);
    }

    /**
     * Returns an Observable that applies a function of your choosing to the first item emitted by a source
     * Observable, then feeds the result of that function along with the second item emitted by the source
     * Observable into the same function, and so on until all items have been emitted by the source Observable,
     * and emits the final result from the final call to your function as its sole item.
     * <p>
     * <img width="640" height="320" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/reduce.png" alt="">
     * <p>
     * This technique, which is called "reduce" here, is sometimes called "aggregate," "fold," "accumulate,"
     * "compress," or "inject" in other programming contexts. Groovy, for instance, has an {@code inject} method
     * that does a similar operation on lists.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because by intent it will receive all values and reduce
     *      them to a single {@code onNext}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code reduce} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param accumulator
     *            an accumulator function to be invoked on each item emitted by the source Observable, whose
     *            result will be used in the next accumulator call
     * @return an Observable that emits a single item that is the result of accumulating the items emitted by
     *         the source Observable
     * @throws IllegalArgumentException
     *             if the source Observable emits no items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#reduce">RxJava wiki: reduce</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229154.aspx">MSDN: Observable.Aggregate</a>
     * @see <a href="http://en.wikipedia.org/wiki/Fold_(higher-order_function)">Wikipedia: Fold (higher-order function)</a>
     */
    public final Observable<T> reduce(Func2<T, T, T> accumulator) {
        /*
         * Discussion and confirmation of implementation at
         * https://github.com/Netflix/RxJava/issues/423#issuecomment-27642532
         * 
         * It should use last() not takeLast(1) since it needs to emit an error if the sequence is empty.
         */
        return scan(accumulator).last();
    }

    /**
     * Returns an Observable that applies a function of your choosing to the first item emitted by a source
     * Observable and a specified seed value, then feeds the result of that function along with the second item
     * emitted by an Observable into the same function, and so on until all items have been emitted by the
     * source Observable, emitting the final result from the final call to your function as its sole item.
     * <p>
     * <img width="640" height="325" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/reduceSeed.png" alt="">
     * <p>
     * This technique, which is called "reduce" here, is sometimec called "aggregate," "fold," "accumulate,"
     * "compress," or "inject" in other programming contexts. Groovy, for instance, has an {@code inject} method
     * that does a similar operation on lists.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because by intent it will receive all values and reduce
     *      them to a single {@code onNext}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code reduce} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param initialValue
     *            the initial (seed) accumulator value
     * @param accumulator
     *            an accumulator function to be invoked on each item emitted by the source Observable, the
     *            result of which will be used in the next accumulator call
     * @return an Observable that emits a single item that is the result of accumulating the output from the
     *         items emitted by the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#reduce">RxJava wiki: reduce</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229154.aspx">MSDN: Observable.Aggregate</a>
     * @see <a href="http://en.wikipedia.org/wiki/Fold_(higher-order_function)">Wikipedia: Fold (higher-order function)</a>
     */
    public final <R> Observable<R> reduce(R initialValue, Func2<R, ? super T, R> accumulator) {
        return scan(initialValue, accumulator).takeLast(1);
    }

    /**
     * Returns an Observable that repeats the sequence of items emitted by the source Observable indefinitely.
     * <p>
     * <img width="640" height="309" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/repeat.o.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code repeat} operates by default on the {@code trampoline} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that emits the items emitted by the source Observable repeatedly and in sequence
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#repeat">RxJava wiki: repeat</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229428.aspx">MSDN: Observable.Repeat</a>
     */
    public final Observable<T> repeat() {
        return OnSubscribeRedo.<T>repeat(this);
    }

    /**
     * Returns an Observable that repeats the sequence of items emitted by the source Observable indefinitely,
     * on a particular Scheduler.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/repeat.os.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param scheduler
     *            the Scheduler to emit the items on
     * @return an Observable that emits the items emitted by the source Observable repeatedly and in sequence
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#repeat">RxJava wiki: repeat</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229428.aspx">MSDN: Observable.Repeat</a>
     */
    public final Observable<T> repeat(Scheduler scheduler) {
        return OnSubscribeRedo.<T>repeat(this, scheduler);
    }

    /**
     * Returns an Observable that repeats the sequence of items emitted by the source Observable at most
     * {@code count} times.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/repeat.on.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code repeat} operates by default on the {@code trampoline} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param count
     *            the number of times the source Observable items are repeated, a count of 0 will yield an empty
     *            sequence
     * @return an Observable that repeats the sequence of items emitted by the source Observable at most
     *         {@code count} times
     * @throws IllegalArgumentException
     *             if {@code count} is less than zero
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#repeat">RxJava wiki: repeat</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229428.aspx">MSDN: Observable.Repeat</a>
     * @since 0.17
     */
    public final Observable<T> repeat(final long count) {
        return OnSubscribeRedo.<T>repeat(this, count);
    }

    /**
     * Returns an Observable that repeats the sequence of items emitted by the source Observable at most
     * {@code count} times, on a particular Scheduler.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/repeat.ons.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param count
     *            the number of times the source Observable items are repeated, a count of 0 will yield an empty
     *            sequence
     * @param scheduler
     *            the {@link Scheduler} to emit the items on
     * @return an Observable that repeats the sequence of items emitted by the source Observable at most
     *         {@code count} times on a particular Scheduler
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#repeat">RxJava Wiki: repeat()</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229428.aspx">MSDN: Observable.Repeat</a>
     * @since 0.17
     */
    public final Observable<T> repeat(final long count, Scheduler scheduler) {
        return OnSubscribeRedo.<T>repeat(this, count, scheduler);
    }

    /**
     * Returns an Observable that emits the same values as the source Observable with the exception of an
     * {@code onCompleted}. An {@code onCompleted} notification from the source will result in the emission of
     * a {@link Notification} to the Observable provided as an argument to the {@code notificationHandler}
     * function. If the Observable returned {@code onCompletes} or {@code onErrors} then {@code repeatWhen} will
     * call {@code onCompleted} or {@code onError} on the child subscription. Otherwise, this Observable will
     * resubscribe to the source Observable, on a particular Scheduler.
     * <p>
     * <img width="640" height="430" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/repeatWhen.f.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param notificationHandler
     *            receives an Observable of notifications with which a user can complete or error, aborting the repeat.
     * @param scheduler
     *            the {@link Scheduler} to emit the items on
     * @return the source Observable modified with repeat logic
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#repeatwhen">RxJava Wiki: repeatWhen()</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229428.aspx">MSDN: Observable.Repeat</a>
     * @since 0.20
     */
    public final Observable<T> repeatWhen(Func1<? super Observable<? extends Notification<?>>, ? extends Observable<? extends Notification<?>>> notificationHandler, Scheduler scheduler) {
        return OnSubscribeRedo.repeat(this, notificationHandler, scheduler);
    }

    /**
     * Returns an Observable that emits the same values as the source Observable with the exception of an
     * {@code onCompleted}. An {@code onCompleted} notification from the source will result in the emission of
     * a {@link Notification} to the Observable provided as an argument to the {@code notificationHandler}
     * function. If the Observable returned {@code onCompletes} or {@code onErrors} then {@code repeatWhen} will
     * call {@code onCompleted} or {@code onError} on the child subscription. Otherwise, this Observable will
     * resubscribe to the source observable.
     * <p>
     * <img width="640" height="430" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/repeatWhen.f.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code repeatWhen} operates by default on the {@code trampoline} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param notificationHandler
     *            receives an Observable of notifications with which a user can complete or error, aborting the repeat.
     * @return the source Observable modified with repeat logic
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#repeatwhen">RxJava Wiki: repeatWhen()</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229428.aspx">MSDN: Observable.Repeat</a>
     * @since 0.20
     */
    public final Observable<T> repeatWhen(Func1<? super Observable<? extends Notification<?>>, ? extends Observable<? extends Notification<?>>> notificationHandler) {
        return OnSubscribeRedo.repeat(this, notificationHandler);
    }

    /**
     * Returns a {@link ConnectableObservable} that shares a single subscription to the underlying Observable
     * that will replay all of its items and notifications to any future {@link Observer}. A Connectable
     * Observable resembles an ordinary Observable, except that it does not begin emitting items when it is
     * subscribed to, but only when its {@code connect} method is called.
     * <p>
     * <img width="640" height="515" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/replay.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code replay} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return a {@link ConnectableObservable} that upon connection causes the source Observable to emit its
     *         items to its {@link Observer}s
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablereplay">RxJava wiki: replay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.replay.aspx">MSDN: Observable.Replay</a>
     */
    public final ConnectableObservable<T> replay() {
        return new OperatorMulticast<T, T>(this, new Func0<Subject<? super T, ? extends T>>() {

            @Override
            public Subject<? super T, ? extends T> call() {
                return ReplaySubject.<T> create();
            }
            
        });
    }

    /**
     * Returns an Observable that emits items that are the results of invoking a specified selector on the items
     * emitted by a {@link ConnectableObservable} that shares a single subscription to the source Observable.
     * <p>
     * <img width="640" height="450" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/replay.f.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code replay} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            the selector function, which can use the multicasted sequence as many times as needed, without
     *            causing multiple subscriptions to the Observable
     * @return an Observable that emits items that are the results of invoking the selector on a
     *         {@link ConnectableObservable} that shares a single subscription to the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablereplay">RxJava wiki: replay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229653.aspx">MSDN: Observable.Replay</a>
     */
    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector) {
        return create(new OnSubscribeMulticastSelector<T, T, R>(this, new Func0<Subject<T, T>>() {
            @Override
            public final Subject<T, T> call() {
                return ReplaySubject.create();
            }
        }, selector));
    }

    /**
     * Returns an Observable that emits items that are the results of invoking a specified selector on items
     * emitted by a {@link ConnectableObservable} that shares a single subscription to the source Observable,
     * replaying {@code bufferSize} notifications.
     * <p>
     * <img width="640" height="440" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/replay.fn.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code replay} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            the selector function, which can use the multicasted sequence as many times as needed, without
     *            causing multiple subscriptions to the Observable
     * @param bufferSize
     *            the buffer size that limits the number of items the connectable observable can replay
     * @return an Observable that emits items that are the results of invoking the selector on items emitted by
     *         a {@link ConnectableObservable} that shares a single subscription to the source Observable
     *         replaying no more than {@code bufferSize} items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablereplay">RxJava wiki: replay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211675.aspx">MSDN: Observable.Replay</a>
     */
    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, final int bufferSize) {
        return create(new OnSubscribeMulticastSelector<T, T, R>(this, new Func0<Subject<T, T>>() {
            @Override
            public final Subject<T, T> call() {
                return ReplaySubject.<T>createWithSize(bufferSize);
            }
        }, selector));
    }

    /**
     * Returns an Observable that emits items that are the results of invoking a specified selector on items
     * emitted by a {@link ConnectableObservable} that shares a single subscription to the source Observable,
     * replaying no more than {@code bufferSize} items that were emitted within a specified time window.
     * <p>
     * <img width="640" height="445" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/replay.fnt.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code replay} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            a selector function, which can use the multicasted sequence as many times as needed, without
     *            causing multiple subscriptions to the Observable
     * @param bufferSize
     *            the buffer size that limits the number of items the connectable observable can replay
     * @param time
     *            the duration of the window in which the replayed items must have been emitted
     * @param unit
     *            the time unit of {@code time}
     * @return an Observable that emits items that are the results of invoking the selector on items emitted by
     *         a {@link ConnectableObservable} that shares a single subscription to the source Observable, and
     *         replays no more than {@code bufferSize} items that were emitted within the window defined by
     *         {@code time}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablereplay">RxJava wiki: replay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh228952.aspx">MSDN: Observable.Replay</a>
     */
    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, int bufferSize, long time, TimeUnit unit) {
        return replay(selector, bufferSize, time, unit, Schedulers.computation());
    }

    /**
     * Returns an Observable that emits items that are the results of invoking a specified selector on items
     * emitted by a {@link ConnectableObservable} that shares a single subscription to the source Observable,
     * replaying no more than {@code bufferSize} items that were emitted within a specified time window.
     * <p>
     * <img width="640" height="445" height="440" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/replay.fnts.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            a selector function, which can use the multicasted sequence as many times as needed, without
     *            causing multiple subscriptions to the Observable
     * @param bufferSize
     *            the buffer size that limits the number of items the connectable observable can replay
     * @param time
     *            the duration of the window in which the replayed items must have been emitted
     * @param unit
     *            the time unit of {@code time}
     * @param scheduler
     *            the Scheduler that is the time source for the window
     * @return an Observable that emits items that are the results of invoking the selector on items emitted by
     *         a {@link ConnectableObservable} that shares a single subscription to the source Observable, and
     *         replays no more than {@code bufferSize} items that were emitted within the window defined by
     *         {@code time}
     * @throws IllegalArgumentException
     *             if {@code bufferSize} is less than zero
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablereplay">RxJava wiki: replay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229404.aspx">MSDN: Observable.Replay</a>
     */
    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, final int bufferSize, final long time, final TimeUnit unit, final Scheduler scheduler) {
        if (bufferSize < 0) {
            throw new IllegalArgumentException("bufferSize < 0");
        }
        return create(new OnSubscribeMulticastSelector<T, T, R>(this, new Func0<Subject<T, T>>() {
            @Override
            public final Subject<T, T> call() {
                return ReplaySubject.<T>createWithTimeAndSize(time, unit, bufferSize, scheduler);
            }
        }, selector));
    }

    /**
     * Returns an Observable that emits items that are the results of invoking a specified selector on items
     * emitted by a {@link ConnectableObservable} that shares a single subscription to the source Observable,
     * replaying a maximum of {@code bufferSize} items.
     * <p>
     * <img width="640" height="440" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/replay.fns.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            a selector function, which can use the multicasted sequence as many times as needed, without
     *            causing multiple subscriptions to the Observable
     * @param bufferSize
     *            the buffer size that limits the number of items the connectable observable can replay
     * @param scheduler
     *            the Scheduler on which the replay is observed
     * @return an Observable that emits items that are the results of invoking the selector on items emitted by
     *         a {@link ConnectableObservable} that shares a single subscription to the source Observable,
     *         replaying no more than {@code bufferSize} notifications
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablereplay">RxJava wiki: replay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229928.aspx">MSDN: Observable.Replay</a>
     */
    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, final int bufferSize, final Scheduler scheduler) {
        return create(new OnSubscribeMulticastSelector<T, T, R>(this, new Func0<Subject<T, T>>() {
            @Override
            public final Subject<T, T> call() {
                return OperatorReplay.<T> createScheduledSubject(ReplaySubject.<T>createWithSize(bufferSize), scheduler);
            }
        }, selector));
    }

    /**
     * Returns an Observable that emits items that are the results of invoking a specified selector on items
     * emitted by a {@link ConnectableObservable} that shares a single subscription to the source Observable,
     * replaying all items that were emitted within a specified time window.
     * <p>
     * <img width="640" height="435" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/replay.ft.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code replay} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            a selector function, which can use the multicasted sequence as many times as needed, without
     *            causing multiple subscriptions to the Observable
     * @param time
     *            the duration of the window in which the replayed items must have been emitted
     * @param unit
     *            the time unit of {@code time}
     * @return an Observable that emits items that are the results of invoking the selector on items emitted by
     *         a {@link ConnectableObservable} that shares a single subscription to the source Observable,
     *         replaying all items that were emitted within the window defined by {@code time}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablereplay">RxJava wiki: replay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229526.aspx">MSDN: Observable.Replay</a>
     */
    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, long time, TimeUnit unit) {
        return replay(selector, time, unit, Schedulers.computation());
    }

    /**
     * Returns an Observable that emits items that are the results of invoking a specified selector on items
     * emitted by a {@link ConnectableObservable} that shares a single subscription to the source Observable,
     * replaying all items that were emitted within a specified time window.
     * <p>
     * <img width="640" height="440" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/replay.fts.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            a selector function, which can use the multicasted sequence as many times as needed, without
     *            causing multiple subscriptions to the Observable
     * @param time
     *            the duration of the window in which the replayed items must have been emitted
     * @param unit
     *            the time unit of {@code time}
     * @param scheduler
     *            the scheduler that is the time source for the window
     * @return an Observable that emits items that are the results of invoking the selector on items emitted by
     *         a {@link ConnectableObservable} that shares a single subscription to the source Observable,
     *         replaying all items that were emitted within the window defined by {@code time}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablereplay">RxJava wiki: replay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh244327.aspx">MSDN: Observable.Replay</a>
     */
    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, final long time, final TimeUnit unit, final Scheduler scheduler) {
        return create(new OnSubscribeMulticastSelector<T, T, R>(this, new Func0<Subject<T, T>>() {
            @Override
            public final Subject<T, T> call() {
                return ReplaySubject.<T>createWithTime(time, unit, scheduler);
            }
        }, selector));
    }

    /**
     * Returns an Observable that emits items that are the results of invoking a specified selector on items
     * emitted by a {@link ConnectableObservable} that shares a single subscription to the source Observable.
     * <p>
     * <img width="640" height="445" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/replay.fs.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            a selector function, which can use the multicasted sequence as many times as needed, without
     *            causing multiple subscriptions to the Observable
     * @param scheduler
     *            the Scheduler where the replay is observed
     * @return an Observable that emits items that are the results of invoking the selector on items emitted by
     *         a {@link ConnectableObservable} that shares a single subscription to the source Observable,
     *         replaying all items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablereplay">RxJava wiki: replay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211644.aspx">MSDN: Observable.Replay</a>
     */
    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, final Scheduler scheduler) {
        return create(new OnSubscribeMulticastSelector<T, T, R>(this, new Func0<Subject<T, T>>() {
            @Override
            public final Subject<T, T> call() {
                return OperatorReplay.createScheduledSubject(ReplaySubject.<T> create(), scheduler);
            }
        }, selector));
    }

    /**
     * Returns a {@link ConnectableObservable} that shares a single subscription to the source Observable that
     * replays at most {@code bufferSize} items emitted by that Observable. A Connectable Observable resembles
     * an ordinary Observable, except that it does not begin emitting items when it is subscribed to, but only
     * when its {@code connect} method is called.
     * <p>
     * <img width="640" height="515" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/replay.n.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code replay} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param bufferSize
     *            the buffer size that limits the number of items that can be replayed
     * @return a {@link ConnectableObservable} that shares a single subscription to the source Observable and
     *         replays at most {@code bufferSize} items emitted by that Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablereplay">RxJava wiki: replay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211976.aspx">MSDN: Observable.Replay</a>
     */
    public final ConnectableObservable<T> replay(final int bufferSize) {
        return new OperatorMulticast<T, T>(this, new Func0<Subject<? super T, ? extends T>>() {

            @Override
            public Subject<? super T, ? extends T> call() {
                return ReplaySubject.<T>createWithSize(bufferSize);
            }
            
        });
    }

    /**
     * Returns a {@link ConnectableObservable} that shares a single subscription to the source Observable and
     * replays at most {@code bufferSize} items that were emitted during a specified time window. A Connectable
     * Observable resembles an ordinary Observable, except that it does not begin emitting items when it is
     * subscribed to, but only when its {@code connect} method is called. 
     * <p>
     * <img width="640" height="515" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/replay.nt.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code replay} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param bufferSize
     *            the buffer size that limits the number of items that can be replayed
     * @param time
     *            the duration of the window in which the replayed items must have been emitted
     * @param unit
     *            the time unit of {@code time}
     * @return a {@link ConnectableObservable} that shares a single subscription to the source Observable and
     *         replays at most {@code bufferSize} items that were emitted during the window defined by
     *         {@code time}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablereplay">RxJava wiki: replay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229874.aspx">MSDN: Observable.Replay</a>
     */
    public final ConnectableObservable<T> replay(int bufferSize, long time, TimeUnit unit) {
        return replay(bufferSize, time, unit, Schedulers.computation());
    }

    /**
     * Returns a {@link ConnectableObservable} that shares a single subscription to the source Observable and
     * that replays a maximum of {@code bufferSize} items that are emitted within a specified time window. A
     * Connectable Observable resembles an ordinary Observable, except that it does not begin emitting items
     * when it is subscribed to, but only when its {@code connect} method is called.
     * <p>
     * <img width="640" height="515" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/replay.nts.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param bufferSize
     *            the buffer size that limits the number of items that can be replayed
     * @param time
     *            the duration of the window in which the replayed items must have been emitted
     * @param unit
     *            the time unit of {@code time}
     * @param scheduler
     *            the scheduler that is used as a time source for the window
     * @return a {@link ConnectableObservable} that shares a single subscription to the source Observable and
     *         replays at most {@code bufferSize} items that were emitted during the window defined by
     *         {@code time}
     * @throws IllegalArgumentException
     *             if {@code bufferSize} is less than zero
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablereplay">RxJava wiki: replay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211759.aspx">MSDN: Observable.Replay</a>
     */
    public final ConnectableObservable<T> replay(final int bufferSize, final long time, final TimeUnit unit, final Scheduler scheduler) {
        if (bufferSize < 0) {
            throw new IllegalArgumentException("bufferSize < 0");
        }
        return new OperatorMulticast<T, T>(this, new Func0<Subject<? super T, ? extends T>>() {

            @Override
            public Subject<? super T, ? extends T> call() {
                return ReplaySubject.<T>createWithTimeAndSize(time, unit, bufferSize, scheduler);
            }
            
        });
    }

    /**
     * Returns a {@link ConnectableObservable} that shares a single subscription to the source Observable and
     * replays at most {@code bufferSize} items emitted by that Observable. A Connectable Observable resembles
     * an ordinary Observable, except that it does not begin emitting items when it is subscribed to, but only
     * when its {@code connect} method is called. 
     * <p>
     * <img width="640" height="515" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/replay.ns.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param bufferSize
     *            the buffer size that limits the number of items that can be replayed
     * @param scheduler
     *            the scheduler on which the Observers will observe the emitted items
     * @return a {@link ConnectableObservable} that shares a single subscription to the source Observable and
     *         replays at most {@code bufferSize} items that were emitted by the Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablereplay">RxJava wiki: replay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229814.aspx">MSDN: Observable.Replay</a>
     */
    public final ConnectableObservable<T> replay(final int bufferSize, final Scheduler scheduler) {
        return new OperatorMulticast<T, T>(this, new Func0<Subject<? super T, ? extends T>>() {

            @Override
            public Subject<? super T, ? extends T> call() {
                return OperatorReplay.createScheduledSubject(ReplaySubject.<T>createWithSize(bufferSize), scheduler);
            }
            
        });
    }

    /**
     * Returns a {@link ConnectableObservable} that shares a single subscription to the source Observable and
     * replays all items emitted by that Observable within a specified time window. A Connectable Observable
     * resembles an ordinary Observable, except that it does not begin emitting items when it is subscribed to,
     * but only when its {@code connect} method is called. 
     * <p>
     * <img width="640" height="515" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/replay.t.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code replay} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param time
     *            the duration of the window in which the replayed items must have been emitted
     * @param unit
     *            the time unit of {@code time}
     * @return a {@link ConnectableObservable} that shares a single subscription to the source Observable and
     *         replays the items that were emitted during the window defined by {@code time}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablereplay">RxJava wiki: replay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229232.aspx">MSDN: Observable.Replay</a>
     */
    public final ConnectableObservable<T> replay(long time, TimeUnit unit) {
        return replay(time, unit, Schedulers.computation());
    }

    /**
     * Returns a {@link ConnectableObservable} that shares a single subscription to the source Observable and
     * replays all items emitted by that Observable within a specified time window. A Connectable Observable
     * resembles an ordinary Observable, except that it does not begin emitting items when it is subscribed to,
     * but only when its {@code connect} method is called. 
     * <p>
     * <img width="640" height="515" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/replay.ts.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param time
     *            the duration of the window in which the replayed items must have been emitted
     * @param unit
     *            the time unit of {@code time}
     * @param scheduler
     *            the Scheduler that is the time source for the window
     * @return a {@link ConnectableObservable} that shares a single subscription to the source Observable and
     *         replays the items that were emitted during the window defined by {@code time}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablereplay">RxJava wiki: replay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211811.aspx">MSDN: Observable.Replay</a>
     */
    public final ConnectableObservable<T> replay(final long time, final TimeUnit unit, final Scheduler scheduler) {
        return new OperatorMulticast<T, T>(this, new Func0<Subject<? super T, ? extends T>>() {

            @Override
            public Subject<? super T, ? extends T> call() {
                return ReplaySubject.<T>createWithTime(time, unit, scheduler);
            }
            
        });
    }

    /**
     * Returns a {@link ConnectableObservable} that shares a single subscription to the source Observable that
     * will replay all of its items and notifications to any future {@link Observer} on the given
     * {@link Scheduler}. A Connectable Observable resembles an ordinary Observable, except that it does not
     * begin emitting items when it is subscribed to, but only when its {@code connect} method is called.
     * <p>
     * <img width="640" height="515" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/replay.s.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param scheduler
     *            the Scheduler on which the Observers will observe the emitted items
     * @return a {@link ConnectableObservable} that shares a single subscription to the source Observable that
     *         will replay all of its items and notifications to any future {@link Observer} on the given
     *         {@link Scheduler}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#observablereplay">RxJava wiki: replay</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211699.aspx">MSDN: Observable.Replay</a>
     */
    public final ConnectableObservable<T> replay(final Scheduler scheduler) {
        return new OperatorMulticast<T, T>(this, new Func0<Subject<? super T, ? extends T>>() {

            @Override
            public Subject<? super T, ? extends T> call() {
                return OperatorReplay.createScheduledSubject(ReplaySubject.<T> create(), scheduler);
            }
            
        });
    }

    /**
     * Returns an Observable that mirrors the source Observable, resubscribing to it if it calls {@code onError}
     * (infinite retry count).
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/retry.png" alt="">
     * <p>
     * If the source Observable calls {@link Observer#onError}, this method will resubscribe to the source
     * Observable rather than propagating the {@code onError} call.
     * <p>
     * Any and all items emitted by the source Observable will be emitted by the resulting Observable, even
     * those emitted during failed subscriptions. For example, if an Observable fails at first but emits
     * {@code [1, 2]} then succeeds the second time and emits {@code [1, 2, 3, 4, 5]} then the complete sequence
     * of emissions and notifications would be {@code [1, 2, 1, 2, 3, 4, 5, onCompleted]}.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code retry} operates by default on the {@code trampoline} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return the source Observable modified with retry logic
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Error-Handling-Operators#retry">RxJava wiki: retry</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.retry.aspx">MSDN: Observable.Retry</a>
     */
    public final Observable<T> retry() {
        return OnSubscribeRedo.<T>retry(this);
    }

    /**
     * Returns an Observable that mirrors the source Observable, resubscribing to it if it calls {@code onError}
     * up to a specified number of retries.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/retry.png" alt="">
     * <p>
     * If the source Observable calls {@link Observer#onError}, this method will resubscribe to the source
     * Observable for a maximum of {@code count} resubscriptions rather than propagating the
     * {@code onError} call.
     * <p>
     * Any and all items emitted by the source Observable will be emitted by the resulting Observable, even
     * those emitted during failed subscriptions. For example, if an Observable fails at first but emits
     * {@code [1, 2]} then succeeds the second time and emits {@code [1, 2, 3, 4, 5]} then the complete sequence
     * of emissions and notifications would be {@code [1, 2, 1, 2, 3, 4, 5, onCompleted]}.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code retry} operates by default on the {@code trampoline} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param count
     *            number of retry attempts before failing
     * @return the source Observable modified with retry logic
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Error-Handling-Operators#retry">RxJava wiki: retry</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.retry.aspx">MSDN: Observable.Retry</a>
     */
    public final Observable<T> retry(final long count) {
        return OnSubscribeRedo.<T>retry(this, count);
    }

    /**
     * Returns an Observable that mirrors the source Observable, resubscribing to it if it calls {@code onError}
     * and the predicate returns true for that specific exception and retry count.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/retry.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code retry} operates by default on the {@code trampoline} {@link Scheduler}.</dd>
     * </dl>
     *
     * @param predicate
     *            the predicate that determines if a resubscription may happen in case of a specific exception
     *            and retry count
     * @return the source Observable modified with retry logic
     * @see #retry()
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Error-Handling-Operators#retry">RxJava Wiki: retry()</a>
     */
    public final Observable<T> retry(Func2<Integer, Throwable, Boolean> predicate) {
        return nest().lift(new OperatorRetryWithPredicate<T>(predicate));
    }

    /**
     * Returns an Observable that emits the same values as the source observable with the exception of an
     * {@code onError}. An {@code onError} notification from the source will result in the emission of a
     * {@link Notification} to the Observable provided as an argument to the {@code notificationHandler}
     * function. If the Observable returned {@code onCompletes} or {@code onErrors} then {@code retry} will call
     * {@code onCompleted} or {@code onError} on the child subscription. Otherwise, this Observable will
     * resubscribe to the source Observable.    
     * <p>
     * <img width="640" height="430" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/retryWhen.f.png" alt="">
     * 
     * Example:
     * 
     * This retries 3 times, each time incrementing the number of seconds it waits.
     * 
     * <pre> {@code
     *  Observable.create((Subscriber<? super String> s) -> {
     *      System.out.println("subscribing");
     *      s.onError(new RuntimeException("always fails"));
     *  }).retryWhen(attempts -> {
     *      return attempts.zipWith(Observable.range(1, 3), (n, i) -> i).flatMap(i -> {
     *          System.out.println("delay retry by " + i + " second(s)");
     *          return Observable.timer(i, TimeUnit.SECONDS);
     *      });
     *  }).toBlocking().forEach(System.out::println);
     * } </pre>
     * 
     * Output is:
     *
     * <pre> {@code
     * subscribing
     * delay retry by 1 second(s)
     * subscribing
     * delay retry by 2 second(s)
     * subscribing
     * delay retry by 3 second(s)
     * subscribing
     * } </pre>
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code retryWhen} operates by default on the {@code trampoline} {@link Scheduler}.</dd>
     * </dl>
     *
     * @param notificationHandler
     *            receives an Observable of notifications with which a user can complete or error, aborting the
     *            retry
     * @return the source Observable modified with retry logic
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Error-Handling-Operators#retrywhen">RxJava Wiki: retryWhen()</a>
     * @since 0.20
     */
    public final Observable<T> retryWhen(Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler) {
        return OnSubscribeRedo.<T> retry(this, notificationHandler);
    }

    /**
     * Returns an Observable that emits the same values as the source observable with the exception of an {@code onError}.
     * An onError will emit a {@link Notification} to the observable provided as an argument to the notificationHandler 
     * func. If the observable returned {@code onCompletes} or {@code onErrors} then retry will call {@code onCompleted} 
     * or {@code onError} on the child subscription. Otherwise, this observable will resubscribe to the source observable, on a particular Scheduler.    
     * <p>
     * <img width="640" height="430" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/retryWhen.f.png" alt="">
     * <p>
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param notificationHandler
     *            receives an Observable of notifications with which a user can complete or error, aborting the
     *            retry
     * @param scheduler
     *            the {@link Scheduler} on which to subscribe to the source Observable
     * @return the source Observable modified with retry logic
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Error-Handling-Operators#retrywhen">RxJava Wiki: retryWhen()</a>
     * @since 0.20
     */
    public final Observable<T> retryWhen(Func1<? super Observable<? extends Notification<?>>, ? extends Observable<? extends Notification<?>>> notificationHandler, Scheduler scheduler) {
        return OnSubscribeRedo.<T> retry(this, notificationHandler, scheduler);
    }

    /**
     * Returns an Observable that emits the most recently emitted item (if any) emitted by the source Observable
     * within periodic time intervals.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/sample.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code sample} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param period
     *            the sampling rate
     * @param unit
     *            the {@link TimeUnit} in which {@code period} is defined
     * @return an Observable that emits the results of sampling the items emitted by the source Observable at
     *         the specified time interval
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#sample-or-throttlelast">RxJava wiki: sample</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.sample.aspx">MSDN: Observable.Sample</a>
     * @see #throttleLast(long, TimeUnit)
     */
    public final Observable<T> sample(long period, TimeUnit unit) {
        return lift(new OperatorSampleWithTime<T>(period, unit, Schedulers.computation()));
    }

    /**
     * Returns an Observable that emits the most recently emitted item (if any) emitted by the source Observable
     * within periodic time intervals, where the intervals are defined on a particular Scheduler.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/sample.s.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param period
     *            the sampling rate
     * @param unit
     *            the {@link TimeUnit} in which {@code period} is defined
     * @param scheduler
     *            the {@link Scheduler} to use when sampling
     * @return an Observable that emits the results of sampling the items emitted by the source Observable at
     *         the specified time interval
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#sample-or-throttlelast">RxJava wiki: sample</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.sample.aspx">MSDN: Observable.Sample</a>
     * @see #throttleLast(long, TimeUnit, Scheduler)
     */
    public final Observable<T> sample(long period, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorSampleWithTime<T>(period, unit, scheduler));
    }

    /**
     * Returns an Observable that, when the specified {@code sampler} Observable emits an item or completes,
     * emits the most recently emitted item (if any) emitted by the source Observable since the previous
     * emission from the {@code sampler} Observable.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/sample.o.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses the emissions of the {@code sampler}
     *      Observable to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code sample} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param sampler
     *            the Observable to use for sampling the source Observable
     * @return an Observable that emits the results of sampling the items emitted by this Observable whenever
     *         the {@code sampler} Observable emits an item or completes
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#sample-or-throttlelast">RxJava wiki: sample</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.sample.aspx">MSDN: Observable.Sample</a>
     */
    public final <U> Observable<T> sample(Observable<U> sampler) {
        return lift(new OperatorSampleWithObservable<T, U>(sampler));
    }

    /**
     * Returns an Observable that applies a function of your choosing to the first item emitted by a source
     * Observable, then feeds the result of that function along with the second item emitted by the source
     * Observable into the same function, and so on until all items have been emitted by the source Observable,
     * emitting the result of each of these iterations.
     * <p>
     * <img width="640" height="320" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/scan.png" alt="">
     * <p>
     * This sort of function is sometimes called an accumulator.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code scan} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param accumulator
     *            an accumulator function to be invoked on each item emitted by the source Observable, whose
     *            result will be emitted to {@link Observer}s via {@link Observer#onNext onNext} and used in the
     *            next accumulator call
     * @return an Observable that emits the results of each call to the accumulator function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#scan">RxJava wiki: scan</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211665.aspx">MSDN: Observable.Scan</a>
     */
    public final Observable<T> scan(Func2<T, T, T> accumulator) {
        return lift(new OperatorScan<T, T>(accumulator));
    }

    /**
     * Returns an Observable that applies a function of your choosing to the first item emitted by a source
     * Observable and a seed value, then feeds the result of that function along with the second item emitted by
     * the source Observable into the same function, and so on until all items have been emitted by the source
     * Observable, emitting the result of each of these iterations.
     * <p>
     * <img width="640" height="320" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/scanSeed.png" alt="">
     * <p>
     * This sort of function is sometimes called an accumulator.
     * <p>
     * Note that the Observable that results from this method will emit {@code initialValue} as its first
     * emitted item.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code scan} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param initialValue
     *            the initial (seed) accumulator item
     * @param accumulator
     *            an accumulator function to be invoked on each item emitted by the source Observable, whose
     *            result will be emitted to {@link Observer}s via {@link Observer#onNext onNext} and used in the
     *            next accumulator call
     * @return an Observable that emits {@code initialValue} followed by the results of each call to the
     *         accumulator function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#scan">RxJava wiki: scan</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211665.aspx">MSDN: Observable.Scan</a>
     */
    public final <R> Observable<R> scan(R initialValue, Func2<R, ? super T, R> accumulator) {
        return lift(new OperatorScan<R, T>(initialValue, accumulator));
    }

    /**
     * Forces an Observable's emissions and notifications to be serialized and for it to obey the Rx contract
     * in other ways.
     * <p>
     * It is possible for an Observable to invoke its Subscribers' methods asynchronously, perhaps from
     * different threads. This could make such an Observable poorly-behaved, in that it might try to invoke
     * {@code onCompleted} or {@code onError} before one of its {@code onNext} invocations, or it might call
     * {@code onNext} from two different threads concurrently. You can force such an Observable to be
     * well-behaved and sequential by applying the {@code serialize} method to it.
     * <p>
     * <img width="640" height="400" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/synchronize.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code serialize} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @return an {@link Observable} that is guaranteed to be well-behaved and to make only serialized calls to
     *         its observers
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#serialize">RxJava wiki: serialize</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.synchronize.aspx">MSDN: Observable.Synchronize</a>
     * @since 0.17
     */
    public final Observable<T> serialize() {
        return lift(new OperatorSerialize<T>());
    }

    /**
     * Returns a new {@link Observable} that multicasts (shares) the original {@link Observable}. As long as
     * there is more than one {@link Subscriber} this {@link Observable} will be subscribed and emitting data. 
     * When all subscribers have unsubscribed it will unsubscribe from the source {@link Observable}. 
     * <p>
     * This is an alias for {@link #publish()}.{@link ConnectableObservable#refCount()}.
     * <p>
     * <img width="640" height="510" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/publishRefCount.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure because multicasting means the stream is "hot" with
     *      multiple subscribers. Each child will need to manage backpressure independently using operators such
     *      as {@link #onBackpressureDrop} and {@link #onBackpressureBuffer}.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code share} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an {@code Observable} that upon connection causes the source {@code Observable} to emit items
     *         to its {@link Observer}s
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Connectable-Observable-Operators#connectableobservablerefcount">RxJava wiki: refCount</a>
     * @since 0.19
     */
    public final Observable<T> share() {
        return publish().refCount();
    }
    
    /**
     * Returns an Observable that emits the single item emitted by the source Observable, if that Observable
     * emits only a single item. If the source Observable emits more than one item or no items, notify of an
     * {@code IllegalArgumentException} or {@code NoSuchElementException} respectively.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/single.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code single} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that emits the single item emitted by the source Observable
     * @throws IllegalArgumentException
     *             if the source emits more than one item
     * @throws NoSuchElementException
     *             if the source emits no items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#single-and-singleordefault">RxJava wiki: single</a>
     * @see "MSDN: Observable.singleAsync"
     */
    public final Observable<T> single() {
        return lift(new OperatorSingle<T>());
    }

    /**
     * Returns an Observable that emits the single item emitted by the source Observable that matches a
     * specified predicate, if that Observable emits one such item. If the source Observable emits more than one
     * such item or no such items, notify of an {@code IllegalArgumentException} or
     * {@code NoSuchElementException} respectively.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/single.p.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code single} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param predicate
     *            a predicate function to evaluate items emitted by the source Observable
     * @return an Observable that emits the single item emitted by the source Observable that matches the
     *         predicate
     * @throws IllegalArgumentException
     *             if the source Observable emits more than one item that matches the predicate
     * @throws NoSuchElementException
     *             if the source Observable emits no item that matches the predicate
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#single-and-singleordefault">RxJava wiki: single</a>
     * @see "MSDN: Observable.singleAsync"
     */
    public final Observable<T> single(Func1<? super T, Boolean> predicate) {
        return filter(predicate).single();
    }

    /**
     * Returns an Observable that emits the single item emitted by the source Observable, if that Observable
     * emits only a single item, or a default item if the source Observable emits no items. If the source
     * Observable emits more than one item, throw an {@code IllegalArgumentException}.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/singleOrDefault.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code singleOrDefault} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param defaultValue
     *            a default value to emit if the source Observable emits no item
     * @return an Observable that emits the single item emitted by the source Observable, or a default item if
     *         the source Observable is empty
     * @throws IllegalArgumentException
     *             if the source Observable emits more than one item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#single-and-singleordefault">RxJava wiki: single</a>
     * @see "MSDN: Observable.singleOrDefaultAsync"
     */
    public final Observable<T> singleOrDefault(T defaultValue) {
        return lift(new OperatorSingle<T>(defaultValue));
    }

    /**
     * Returns an Observable that emits the single item emitted by the source Observable that matches a
     * predicate, if that Observable emits only one such item, or a default item if the source Observable emits
     * no such items. If the source Observable emits more than one such item, throw an
     * {@code IllegalArgumentException}.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/singleOrDefault.p.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code singleOrDefault} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param defaultValue
     *            a default item to emit if the source Observable emits no matching items
     * @param predicate
     *            a predicate function to evaluate items emitted by the source Observable
     * @return an Observable that emits the single item emitted by the source Observable that matches the
     *         predicate, or the default item if no emitted item matches the predicate
     * @throws IllegalArgumentException
     *             if the source Observable emits more than one item that matches the predicate
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#single-and-singleordefault">RxJava wiki: single</a>
     * @see "MSDN: Observable.singleOrDefaultAsync"
     */
    public final Observable<T> singleOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return filter(predicate).singleOrDefault(defaultValue);
    }

    /**
     * Returns an Observable that skips the first {@code num} items emitted by the source Observable and emits
     * the remainder.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/skip.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code skip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param num
     *            the number of items to skip
     * @return an Observable that is identical to the source Observable except that it does not emit the first
     *         {@code num} items that the source Observable emits
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#skip">RxJava wiki: skip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229847.aspx">MSDN: Observable.skip</a>
     */
    public final Observable<T> skip(int num) {
        return lift(new OperatorSkip<T>(num));
    }

    /**
     * Returns an Observable that skips values emitted by the source Observable before a specified time window
     * elapses.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/skip.t.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code skip} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param time
     *            the length of the time window to skip
     * @param unit
     *            the time unit of {@code time}
     * @return an Observable that skips values emitted by the source Observable before the time window defined
     *         by {@code time} elapses and the emits the remainder
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#skip">RxJava wiki: skip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229847.aspx">MSDN: Observable.skip</a>
     */
    public final Observable<T> skip(long time, TimeUnit unit) {
        return skip(time, unit, Schedulers.computation());
    }

    /**
     * Returns an Observable that skips values emitted by the source Observable before a specified time window
     * on a specified {@link Scheduler} elapses.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/skip.ts.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param time
     *            the length of the time window to skip
     * @param unit
     *            the time unit of {@code time}
     * @param scheduler
     *            the {@link Scheduler} on which the timed wait happens
     * @return an Observable that skips values emitted by the source Observable before the time window defined
     *         by {@code time} and {@code scheduler} elapses, and then emits the remainder
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#skip">RxJava wiki: skip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229847.aspx">MSDN: Observable.skip</a>
     */
    public final Observable<T> skip(long time, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorSkipTimed<T>(time, unit, scheduler));
    }

    /**
     * Returns an Observable that drops a specified number of items from the end of the sequence emitted by the
     * source Observable.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/skipLast.png" alt="">
     * <p>
     * This Observer accumulates a queue long enough to store the first {@code count} items. As more items are
     * received, items are taken from the front of the queue and emitted by the returned Observable. This causes
     * such items to be delayed.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code skipLast} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param count
     *            number of items to drop from the end of the source sequence
     * @return an Observable that emits the items emitted by the source Observable except for the dropped ones
     *         at the end
     * @throws IndexOutOfBoundsException
     *             if {@code count} is less than zero
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#skiplast">RxJava wiki: skipLast</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211750.aspx">MSDN: Observable.SkipLast</a>
     */
    public final Observable<T> skipLast(int count) {
        return lift(new OperatorSkipLast<T>(count));
    }

    /**
     * Returns an Observable that drops items emitted by the source Observable during a specified time window
     * before the source completes.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/skipLast.t.png" alt="">
     * <p>
     * Note: this action will cache the latest items arriving in the specified time window.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code skipLast} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param time
     *            the length of the time window
     * @param unit
     *            the time unit of {@code time}
     * @return an Observable that drops those items emitted by the source Observable in a time window before the
     *         source completes defined by {@code time}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#skiplast">RxJava wiki: skipLast</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211750.aspx">MSDN: Observable.SkipLast</a>
     */
    public final Observable<T> skipLast(long time, TimeUnit unit) {
        return skipLast(time, unit, Schedulers.computation());
    }

    /**
     * Returns an Observable that drops items emitted by the source Observable during a specified time window
     * (defined on a specified scheduler) before the source completes.
     * <p>
     * <img width="640" height="340" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/skipLast.ts.png" alt="">
     * <p>
     * Note: this action will cache the latest items arriving in the specified time window.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param time
     *            the length of the time window
     * @param unit
     *            the time unit of {@code time}
     * @param scheduler
     *            the scheduler used as the time source
     * @return an Observable that drops those items emitted by the source Observable in a time window before the
     *         source completes defined by {@code time} and {@code scheduler}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#skiplast">RxJava wiki: skipLast</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211750.aspx">MSDN: Observable.SkipLast</a>
     */
    public final Observable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorSkipLastTimed<T>(time, unit, scheduler));
    }

    /**
     * Returns an Observable that skips items emitted by the source Observable until a second Observable emits
     * an item.
     * <p>
     * <img width="640" height="375" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/skipUntil.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code skipUntil} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param other
     *            the second Observable that has to emit an item before the source Observable's elements begin
     *            to be mirrored by the resulting Observable
     * @return an Observable that skips items from the source Observable until the second Observable emits an
     *         item, then emits the remaining items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#skipuntil">RxJava wiki: skipUntil</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229358.aspx">MSDN: Observable.SkipUntil</a>
     */
    public final <U> Observable<T> skipUntil(Observable<U> other) {
        return lift(new OperatorSkipUntil<T, U>(other));
    }

    /**
     * Returns an Observable that skips all items emitted by the source Observable as long as a specified
     * condition holds true, but emits all further source items as soon as the condition becomes false.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/skipWhile.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code skipWhile} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param predicate
     *            a function to test each item emitted from the source Observable
     * @return an Observable that begins emitting items emitted by the source Observable when the specified
     *         predicate becomes false
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#skipwhile-and-skipwhilewithindex">RxJava wiki: skipWhile</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229685.aspx">MSDN: Observable.SkipWhile</a>
     */
    public final Observable<T> skipWhile(Func1<? super T, Boolean> predicate) {
        return lift(new OperatorSkipWhile<T>(OperatorSkipWhile.toPredicate2(predicate)));
    }

    /**
     * Returns an Observable that skips all items emitted by the source Observable as long as a specified
     * condition holds true, but emits all further source items as soon as the condition becomes false.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/skipWhileWithIndex.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code skipWhileWithIndex} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param predicate
     *            a function to test each item emitted from the source Observable. It takes the emitted item as
     *            the first parameter and the sequential index of the emitted item as a second parameter.
     * @return an Observable that begins emitting items emitted by the source Observable when the specified
     *         predicate becomes false
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#skipwhile-and-skipwhilewithindex">RxJava wiki: skipWhileWithIndex</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211631.aspx">MSDN: Observable.SkipWhile</a>
     */
    public final Observable<T> skipWhileWithIndex(Func2<? super T, Integer, Boolean> predicate) {
        return lift(new OperatorSkipWhile<T>(predicate));
    }

    /**
     * Returns an Observable that emits the items in a specified {@link Observable} before it begins to emit
     * items emitted by the source Observable.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/startWith.o.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code startWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param values
     *            an Observable that contains the items you want the modified Observable to emit first
     * @return an Observable that emits the items in the specified {@link Observable} and then emits the items
     *         emitted by the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#startwith">RxJava wiki: startWith</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.startwith.aspx">MSDN: Observable.StartWith</a>
     */
    public final Observable<T> startWith(Observable<T> values) {
        return concat(values, this);
    }

    /**
     * Returns an Observable that emits the items in a specified {@link Iterable} before it begins to emit items
     * emitted by the source Observable.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/startWith.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code startWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param values
     *            an Iterable that contains the items you want the modified Observable to emit first
     * @return an Observable that emits the items in the specified {@link Iterable} and then emits the items
     *         emitted by the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#startwith">RxJava wiki: startWith</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.startwith.aspx">MSDN: Observable.StartWith</a>
     */
    public final Observable<T> startWith(Iterable<T> values) {
        return concat(Observable.<T> from(values), this);
    }

    /**
     * Returns an Observable that emits the items in a specified {@link Iterable}, on a specified
     * {@link Scheduler}, before it begins to emit items emitted by the source Observable.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/startWith.s.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param values
     *            an Iterable that contains the items you want the modified Observable to emit first
     * @param scheduler
     *            the Scheduler to emit the prepended values on
     * @return an Observable that emits the items in the specified {@link Iterable} and then emits the items
     *         emitted by the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#startwith">RxJava wiki: startWith</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229372.aspx">MSDN: Observable.StartWith</a>
     */
    public final Observable<T> startWith(Iterable<T> values, Scheduler scheduler) {
        return concat(from(values, scheduler), this);
    }

    /**
     * Returns an Observable that emits a specified item before it begins to emit items emitted by the source
     * Observable.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/startWith.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code startWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            the item to emit
     * @return an Observable that emits the specified item before it begins to emit items emitted by the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#startwith">RxJava wiki: startWith</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.startwith.aspx">MSDN: Observable.StartWith</a>
     */
    public final Observable<T> startWith(T t1) {
        return concat(Observable.<T> from(t1), this);
    }

    /**
     * Returns an Observable that emits the specified items before it begins to emit items emitted by the source
     * Observable.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/startWith.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code startWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            the first item to emit
     * @param t2
     *            the second item to emit
     * @return an Observable that emits the specified items before it begins to emit items emitted by the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#startwith">RxJava wiki: startWith</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.startwith.aspx">MSDN: Observable.StartWith</a>
     */
    public final Observable<T> startWith(T t1, T t2) {
        return concat(Observable.<T> from(t1, t2), this);
    }

    /**
     * Returns an Observable that emits the specified items before it begins to emit items emitted by the source
     * Observable.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/startWith.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code startWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            the first item to emit
     * @param t2
     *            the second item to emit
     * @param t3
     *            the third item to emit
     * @return an Observable that emits the specified items before it begins to emit items emitted by the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#startwith">RxJava wiki: startWith</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.startwith.aspx">MSDN: Observable.StartWith</a>
     */
    public final Observable<T> startWith(T t1, T t2, T t3) {
        return concat(Observable.<T> from(t1, t2, t3), this);
    }

    /**
     * Returns an Observable that emits the specified items before it begins to emit items emitted by the source
     * Observable.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/startWith.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code startWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            the first item to emit
     * @param t2
     *            the second item to emit
     * @param t3
     *            the third item to emit
     * @param t4
     *            the fourth item to emit
     * @return an Observable that emits the specified items before it begins to emit items emitted by the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#startwith">RxJava wiki: startWith</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.startwith.aspx">MSDN: Observable.StartWith</a>
     */
    public final Observable<T> startWith(T t1, T t2, T t3, T t4) {
        return concat(Observable.<T> from(t1, t2, t3, t4), this);
    }

    /**
     * Returns an Observable that emits the specified items before it begins to emit items emitted by the source
     * Observable.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/startWith.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code startWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            the first item to emit
     * @param t2
     *            the second item to emit
     * @param t3
     *            the third item to emit
     * @param t4
     *            the fourth item to emit
     * @param t5
     *            the fifth item to emit
     * @return an Observable that emits the specified items before it begins to emit items emitted by the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#startwith">RxJava wiki: startWith</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.startwith.aspx">MSDN: Observable.StartWith</a>
     */
    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5) {
        return concat(Observable.<T> from(t1, t2, t3, t4, t5), this);
    }

    /**
     * Returns an Observable that emits the specified items before it begins to emit items emitted by the source
     * Observable.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/startWith.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code startWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            the first item to emit
     * @param t2
     *            the second item to emit
     * @param t3
     *            the third item to emit
     * @param t4
     *            the fourth item to emit
     * @param t5
     *            the fifth item to emit
     * @param t6
     *            the sixth item to emit
     * @return an Observable that emits the specified items before it begins to emit items emitted
     *         by the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#startwith">RxJava wiki: startWith</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.startwith.aspx">MSDN: Observable.StartWith</a>
     */
    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5, T t6) {
        return concat(Observable.<T> from(t1, t2, t3, t4, t5, t6), this);
    }

    /**
     * Returns an Observable that emits the specified items before it begins to emit items emitted by the source
     * Observable.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/startWith.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code startWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            the first item to emit
     * @param t2
     *            the second item to emit
     * @param t3
     *            the third item to emit
     * @param t4
     *            the fourth item to emit
     * @param t5
     *            the fifth item to emit
     * @param t6
     *            the sixth item to emit
     * @param t7
     *            the seventh item to emit
     * @return an Observable that emits the specified items before it begins to emit items emitted by the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#startwith">RxJava wiki: startWith</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.startwith.aspx">MSDN: Observable.StartWith</a>
     */
    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5, T t6, T t7) {
        return concat(Observable.<T> from(t1, t2, t3, t4, t5, t6, t7), this);
    }

    /**
     * Returns an Observable that emits the specified items before it begins to emit items emitted by the source
     * Observable.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/startWith.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code startWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            the first item to emit
     * @param t2
     *            the second item to emit
     * @param t3
     *            the third item to emit
     * @param t4
     *            the fourth item to emit
     * @param t5
     *            the fifth item to emit
     * @param t6
     *            the sixth item to emit
     * @param t7
     *            the seventh item to emit
     * @param t8
     *            the eighth item to emit
     * @return an Observable that emits the specified items before it begins to emit items emitted by the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#startwith">RxJava wiki: startWith</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.startwith.aspx">MSDN: Observable.StartWith</a>
     */
    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8) {
        return concat(Observable.<T> from(t1, t2, t3, t4, t5, t6, t7, t8), this);
    }

    /**
     * Returns an Observable that emits the specified items before it begins to emit items emitted by the source
     * Observable.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/startWith.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code startWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param t1
     *            the first item to emit
     * @param t2
     *            the second item to emit
     * @param t3
     *            the third item to emit
     * @param t4
     *            the fourth item to emit
     * @param t5
     *            the fifth item to emit
     * @param t6
     *            the sixth item to emit
     * @param t7
     *            the seventh item to emit
     * @param t8
     *            the eighth item to emit
     * @param t9
     *            the ninth item to emit
     * @return an Observable that emits the specified items before it begins to emit items emitted by the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#startwith">RxJava wiki: startWith</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.startwith.aspx">MSDN: Observable.StartWith</a>
     */
    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9) {
        return concat(Observable.<T> from(t1, t2, t3, t4, t5, t6, t7, t8, t9), this);
    }

    /**
     * Returns an Observable that emits the items from a specified array, on a specified Scheduler, before it
     * begins to emit items emitted by the source Observable.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/startWith.s.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param values
     *            the items you want the modified Observable to emit first
     * @param scheduler
     *            the Scheduler to emit the prepended values on
     * @return an Observable that emits the items from {@code values}, on {@code scheduler}, before it begins to
     *         emit items emitted by the source Observable.
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#startwith">RxJava wiki: startWith</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229372.aspx">MSDN: Observable.StartWith</a>
     */
    public final Observable<T> startWith(T[] values, Scheduler scheduler) {
        return startWith(Arrays.asList(values), scheduler);
    }

    /**
     * Subscribes to an Observable but ignore its emissions and notifications.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code subscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return a {@link Subscription} reference with which the {@link Observer} can stop receiving items before
     *         the Observable has finished sending them
     * @throws OnErrorNotImplementedException
     *             if the Observable tries to call {@code onError}
     */
    public final Subscription subscribe() {
        return subscribe(new Subscriber<T>() {

            @Override
            public final void onCompleted() {
                // do nothing
            }

            @Override
            public final void onError(Throwable e) {
                throw new OnErrorNotImplementedException(e);
            }

            @Override
            public final void onNext(T args) {
                // do nothing
            }

        });
    }

    /**
     * Subscribes to an Observable and provides a callback to handle the items it emits.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code subscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param onNext
     *             the {@code Action1<T>} you have designed to accept emissions from the Observable
     * @return a {@link Subscription} reference with which the {@link Observer} can stop receiving items before
     *         the Observable has finished sending them
     * @throws IllegalArgumentException
     *             if {@code onNext} is null
     * @throws OnErrorNotImplementedException
     *             if the Observable tries to call {@code onError}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable#onnext-oncompleted-and-onerror">RxJava wiki: onNext, onCompleted, and onError</a>
     */
    public final Subscription subscribe(final Action1<? super T> onNext) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        }

        return subscribe(new Subscriber<T>() {

            @Override
            public final void onCompleted() {
                // do nothing
            }

            @Override
            public final void onError(Throwable e) {
                throw new OnErrorNotImplementedException(e);
            }

            @Override
            public final void onNext(T args) {
                onNext.call(args);
            }

        });
    }

    /**
     * Subscribes to an Observable and provides callbacks to handle the items it emits and any error
     * notification it issues.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code subscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param onNext
     *             the {@code Action1<T>} you have designed to accept emissions from the Observable
     * @param onError
     *             the {@code Action1<Throwable>} you have designed to accept any error notification from the
     *             Observable
     * @return a {@link Subscription} reference with which the {@link Observer} can stop receiving items before
     *         the Observable has finished sending them
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable#onnext-oncompleted-and-onerror">RxJava wiki: onNext, onCompleted, and onError</a>
     * @throws IllegalArgumentException
     *             if {@code onNext} is null, or
     *             if {@code onError} is null
     */
    public final Subscription subscribe(final Action1<? super T> onNext, final Action1<Throwable> onError) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        }
        if (onError == null) {
            throw new IllegalArgumentException("onError can not be null");
        }

        return subscribe(new Subscriber<T>() {

            @Override
            public final void onCompleted() {
                // do nothing
            }

            @Override
            public final void onError(Throwable e) {
                onError.call(e);
            }

            @Override
            public final void onNext(T args) {
                onNext.call(args);
            }

        });
    }

    /**
     * Subscribes to an Observable and provides callbacks to handle the items it emits and any error or
     * completion notification it issues.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code subscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param onNext
     *             the {@code Action1<T>} you have designed to accept emissions from the Observable
     * @param onError
     *             the {@code Action1<Throwable>} you have designed to accept any error notification from the
     *             Observable
     * @param onComplete
     *             the {@code Action0} you have designed to accept a completion notification from the
     *             Observable
     * @return a {@link Subscription} reference with which the {@link Observer} can stop receiving items before
     *         the Observable has finished sending them
     * @throws IllegalArgumentException
     *             if {@code onNext} is null, or
     *             if {@code onError} is null, or
     *             if {@code onComplete} is null
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable#onnext-oncompleted-and-onerror">RxJava wiki: onNext, onCompleted, and onError</a>
     */
    public final Subscription subscribe(final Action1<? super T> onNext, final Action1<Throwable> onError, final Action0 onComplete) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        }
        if (onError == null) {
            throw new IllegalArgumentException("onError can not be null");
        }
        if (onComplete == null) {
            throw new IllegalArgumentException("onComplete can not be null");
        }

        return subscribe(new Subscriber<T>() {

            @Override
            public final void onCompleted() {
                onComplete.call();
            }

            @Override
            public final void onError(Throwable e) {
                onError.call(e);
            }

            @Override
            public final void onNext(T args) {
                onNext.call(args);
            }

        });
    }

    /**
     * Subscribes to an Observable and provides an Observer that implements functions to handle the items the
     * Observable emits and any error or completion notification it issues.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code subscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param observer
     *             the Observer that will handle emissions and notifications from the Observable
     * @return a {@link Subscription} reference with which the {@link Observer} can stop receiving items before
     *         the Observable has completed
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable#onnext-oncompleted-and-onerror">RxJava wiki: onNext, onCompleted, and onError</a>
     */
    public final Subscription subscribe(final Observer<? super T> observer) {
        return subscribe(new Subscriber<T>() {

            @Override
            public void onCompleted() {
                observer.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                observer.onError(e);
            }

            @Override
            public void onNext(T t) {
                observer.onNext(t);
            }

        });
    }

    /**
     * Subscribes to an Observable and invokes {@link OnSubscribe} function without any contract protection,
     * error handling, unsubscribe, or execution hooks.
     * <p>
     * Use this only for implementing an {@link Operator} that requires nested subscriptions. For other
     * purposes, use {@link #subscribe(Subscriber)} which ensures the Rx contract and other functionality.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code unsafeSubscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param subscriber
     *              the Subscriber that will handle emissions and notifications from the Observable
     * @return a {@link Subscription} reference with which the {@link Subscriber} can stop receiving items
     *         before the Observable has completed
     * @since 0.17
     */
    public final Subscription unsafeSubscribe(Subscriber<? super T> subscriber) {
        try {
            // new Subscriber so onStart it
            subscriber.onStart();
            // allow the hook to intercept and/or decorate
            hook.onSubscribeStart(this, onSubscribe).call(subscriber);
            return hook.onSubscribeReturn(subscriber);
        } catch (Throwable e) {
            // special handling for certain Throwable/Error/Exception types
            Exceptions.throwIfFatal(e);
            // if an unhandled error occurs executing the onSubscribe we will propagate it
            try {
                subscriber.onError(hook.onSubscribeError(e));
            } catch (OnErrorNotImplementedException e2) {
                // special handling when onError is not implemented ... we just rethrow
                throw e2;
            } catch (Throwable e2) {
                // if this happens it means the onError itself failed (perhaps an invalid function implementation)
                // so we are unable to propagate the error correctly and will just throw
                RuntimeException r = new RuntimeException("Error occurred attempting to subscribe [" + e.getMessage() + "] and then again while trying to pass to onError.", e2);
                // TODO could the hook be the cause of the error in the on error handling.
                hook.onSubscribeError(r);
                // TODO why aren't we throwing the hook's return value.
                throw r;
            }
            return Subscriptions.empty();
        }
    }

    /**
     * Subscribes to an Observable and provides a Subscriber that implements functions to handle the items the
     * Observable emits and any error or completion notification it issues.
     * <p>
     * A typical implementation of {@code subscribe} does the following:
     * <ol>
     * <li>It stores a reference to the Subscriber in a collection object, such as a {@code List<T>} object.</li>
     * <li>It returns a reference to the {@link Subscription} interface. This enables Subscribers to
     * unsubscribe, that is, to stop receiving items and notifications before the Observable completes, which
     * also invokes the Subscriber's {@link Subscriber#onCompleted onCompleted} method.</li>
     * </ol><p>
     * An {@code Observable<T>} instance is responsible for accepting all subscriptions and notifying all
     * Subscribers. Unless the documentation for a particular {@code Observable<T>} implementation indicates
     * otherwise, Subscriber should make no assumptions about the order in which multiple Subscribers will
     * receive their notifications.
     * <p>
     * For more information see the
     * <a href="https://github.com/Netflix/RxJava/wiki/Observable">RxJava wiki</a>.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code subscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param subscriber
     *            the {@link Subscriber} that will handle emissions and notifications from the Observable
     * @return a {@link Subscription} reference with which Subscribers that are {@link Observer}s can
     *         unsubscribe from the Observable
     * @throws IllegalStateException
     *             if {@code subscribe} is unable to obtain an {@code OnSubscribe<>} function
     * @throws IllegalArgumentException
     *             if the {@link Subscriber} provided as the argument to {@code subscribe} is {@code null}
     * @throws OnErrorNotImplementedException
     *             if the {@link Subscriber}'s {@code onError} method is null
     * @throws RuntimeException
     *             if the {@link Subscriber}'s {@code onError} method itself threw a {@code Throwable}
     */
    public final Subscription subscribe(Subscriber<? super T> subscriber) {
        // validate and proceed
        if (subscriber == null) {
            throw new IllegalArgumentException("observer can not be null");
        }
        if (onSubscribe == null) {
            throw new IllegalStateException("onSubscribe function can not be null.");
            /*
             * the subscribe function can also be overridden but generally that's not the appropriate approach
             * so I won't mention that in the exception
             */
        }
        
        // new Subscriber so onStart it
        subscriber.onStart();
        
        /*
         * See https://github.com/Netflix/RxJava/issues/216 for discussion on "Guideline 6.4: Protect calls
         * to user code from within an Observer"
         */
        // if not already wrapped
        if (!(subscriber instanceof SafeSubscriber)) {
            // assign to `observer` so we return the protected version
            subscriber = new SafeSubscriber<T>(subscriber);
        }

        // The code below is exactly the same an unsafeSubscribe but not used because it would add a sigificent depth to alreay huge call stacks.
        try {
            // allow the hook to intercept and/or decorate
            hook.onSubscribeStart(this, onSubscribe).call(subscriber);
            return hook.onSubscribeReturn(subscriber);
        } catch (Throwable e) {
            // special handling for certain Throwable/Error/Exception types
            Exceptions.throwIfFatal(e);
            // if an unhandled error occurs executing the onSubscribe we will propagate it
            try {
                subscriber.onError(hook.onSubscribeError(e));
            } catch (OnErrorNotImplementedException e2) {
                // special handling when onError is not implemented ... we just rethrow
                throw e2;
            } catch (Throwable e2) {
                // if this happens it means the onError itself failed (perhaps an invalid function implementation)
                // so we are unable to propagate the error correctly and will just throw
                RuntimeException r = new RuntimeException("Error occurred attempting to subscribe [" + e.getMessage() + "] and then again while trying to pass to onError.", e2);
                // TODO could the hook be the cause of the error in the on error handling.
                hook.onSubscribeError(r);
                // TODO why aren't we throwing the hook's return value.
                throw r;
            }
            return Subscriptions.empty();
        }
    }

    /**
     * Asynchronously subscribes Observers to this Observable on the specified {@link Scheduler}.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/subscribeOn.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param scheduler
     *            the {@link Scheduler} to perform subscription actions on
     * @return the source Observable modified so that its subscriptions happen on the
     *         specified {@link Scheduler}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#subscribeon">RxJava wiki: subscribeOn</a>
     * @see <a href="http://www.grahamlea.com/2014/07/rxjava-threading-examples/">RxJava Threading Examples</a>
     * @see #observeOn
     */
    public final Observable<T> subscribeOn(Scheduler scheduler) {
        return nest().lift(new OperatorSubscribeOn<T>(scheduler));
    }

    /**
     * Returns a new Observable by applying a function that you supply to each item emitted by the source
     * Observable that returns an Observable, and then emitting the items emitted by the most recently emitted
     * of these Observables.
     * <p>
     * <img width="640" height="350" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/switchMap.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code switchMap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param func
     *            a function that, when applied to an item emitted by the source Observable, returns an
     *            Observable
     * @return an Observable that emits the items emitted by the Observable returned from applying {@code func} to the most recently emitted item emitted by the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#switchmap">RxJava wiki: switchMap</a>
     */
    public final <R> Observable<R> switchMap(Func1<? super T, ? extends Observable<? extends R>> func) {
        return switchOnNext(map(func));
    }

    /**
     * Returns an Observable that emits only the first {@code num} items emitted by the source Observable.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/take.png" alt="">
     * <p>
     * This method returns an Observable that will invoke a subscribing {@link Observer}'s
     * {@link Observer#onNext onNext} function a maximum of {@code num} times before invoking
     * {@link Observer#onCompleted onCompleted}.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code take} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param num
     *            the maximum number of items to emit
     * @return an Observable that emits only the first {@code num} items emitted by the source Observable, or
     *         all of the items from the source Observable if that Observable emits fewer than {@code num} items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#take">RxJava wiki: take</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229852.aspx">MSDN: Observable.Take</a>
     */
    public final Observable<T> take(final int num) {
        return lift(new OperatorTake<T>(num));
    }

    /**
     * Returns an Observable that emits those items emitted by source Observable before a specified time runs
     * out.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/take.t.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code take} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param time
     *            the length of the time window
     * @param unit
     *            the time unit of {@code time}
     * @return an Observable that emits those items emitted by the source Observable before the time runs out
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#take">RxJava wiki: take</a>
     */
    public final Observable<T> take(long time, TimeUnit unit) {
        return take(time, unit, Schedulers.computation());
    }

    /**
     * Returns an Observable that emits those items emitted by source Observable before a specified time (on a
     * specified Scheduler) runs out.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/take.ts.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param time
     *            the length of the time window
     * @param unit
     *            the time unit of {@code time}
     * @param scheduler
     *            the Scheduler used for time source
     * @return an Observable that emits those items emitted by the source Observable before the time runs out,
     *         according to the specified Scheduler
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#take">RxJava wiki: take</a>
     */
    public final Observable<T> take(long time, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorTakeTimed<T>(time, unit, scheduler));
    }

    /**
     * Returns an Observable that emits only the very first item emitted by the source Observable that satisfies
     * a specified condition.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/takeFirstN.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code takeFirst} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param predicate
     *            the condition any item emitted by the source Observable has to satisfy
     * @return an Observable that emits only the very first item emitted by the source Observable that satisfies
     *         the given condition, or that completes without emitting anything if the source Observable
     *         completes without emitting a single condition-satisfying item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#first-and-takefirst">RxJava wiki: takeFirst</a>
     * @see "MSDN: Observable.firstAsync"
     */
    public final Observable<T> takeFirst(Func1<? super T, Boolean> predicate) {
        return filter(predicate).take(1);
    }

    /**
     * Returns an Observable that emits only the last {@code count} items emitted by the source Observable.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/takeLast.n.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code takeLast} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param count
     *            the number of items to emit from the end of the sequence of items emitted by the source
     *            Observable
     * @return an Observable that emits only the last {@code count} items emitted by the source Observable
     * @throws IndexOutOfBoundsException
     *             if {@code count} is less than zero
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#takelast">RxJava wiki: takeLast</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212114.aspx">MSDN: Observable.TakeLast</a>
     */
    public final Observable<T> takeLast(final int count) {
        return lift(new OperatorTakeLast<T>(count));
    }

    /**
     * Returns an Observable that emits at most a specified number of items from the source Observable that were
     * emitted in a specified window of time before the Observable completed.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/takeLast.tn.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code takeLast} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param count
     *            the maximum number of items to emit
     * @param time
     *            the length of the time window
     * @param unit
     *            the time unit of {@code time}
     * @return an Observable that emits at most {@code count} items from the source Observable that were emitted
     *         in a specified window of time before the Observable completed
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#takelast">RxJava wiki: takeLast</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212114.aspx">MSDN: Observable.TakeLast</a>
     */
    public final Observable<T> takeLast(int count, long time, TimeUnit unit) {
        return takeLast(count, time, unit, Schedulers.computation());
    }

    /**
     * Returns an Observable that emits at most a specified number of items from the source Observable that were
     * emitted in a specified window of time before the Observable completed, where the timing information is
     * provided by a given Scheduler.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/takeLast.tns.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param count
     *            the maximum number of items to emit
     * @param time
     *            the length of the time window
     * @param unit
     *            the time unit of {@code time}
     * @param scheduler
     *            the {@link Scheduler} that provides the timestamps for the observed items
     * @return an Observable that emits at most {@code count} items from the source Observable that were emitted
     *         in a specified window of time before the Observable completed, where the timing information is
     *         provided by the given {@code scheduler}
     * @throws IndexOutOfBoundsException
     *             if {@code count} is less than zero
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#takelast">RxJava wiki: takeLast</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212114.aspx">MSDN: Observable.TakeLast</a>
     */
    public final Observable<T> takeLast(int count, long time, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorTakeLastTimed<T>(count, time, unit, scheduler));
    }

    /**
     * Returns an Observable that emits the items from the source Observable that were emitted in a specified
     * window of time before the Observable completed.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/takeLast.t.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code takeLast} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param time
     *            the length of the time window
     * @param unit
     *            the time unit of {@code time}
     * @return an Observable that emits the items from the source Observable that were emitted in the window of
     *         time before the Observable completed specified by {@code time}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#takelast">RxJava wiki: takeLast</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212114.aspx">MSDN: Observable.TakeLast</a>
     */
    public final Observable<T> takeLast(long time, TimeUnit unit) {
        return takeLast(time, unit, Schedulers.computation());
    }

    /**
     * Returns an Observable that emits the items from the source Observable that were emitted in a specified
     * window of time before the Observable completed, where the timing information is provided by a specified
     * Scheduler.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/takeLast.ts.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param time
     *            the length of the time window
     * @param unit
     *            the time unit of {@code time}
     * @param scheduler
     *            the Scheduler that provides the timestamps for the Observed items
     * @return an Observable that emits the items from the source Observable that were emitted in the window of
     *         time before the Observable completed specified by {@code time}, where the timing information is
     *         provided by {@code scheduler}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#takelast">RxJava wiki: takeLast</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212114.aspx">MSDN: Observable.TakeLast</a>
     */
    public final Observable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorTakeLastTimed<T>(time, unit, scheduler));
    }

    /**
     * Returns an Observable that emits a single List containing the last {@code count} elements emitted by the
     * source Observable.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/takeLastBuffer.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code takeLastBuffer} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param count
     *            the number of items to emit in the list
     * @return an Observable that emits a single list containing the last {@code count} elements emitted by the
     *         source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#takelastbuffer">RxJava wiki: takeLastBuffer</a>
     */
    public final Observable<List<T>> takeLastBuffer(int count) {
        return takeLast(count).toList();
    }

    /**
     * Returns an Observable that emits a single List containing at most {@code count} items from the source
     * Observable that were emitted during a specified window of time before the source Observable completed.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/takeLastBuffer.tn.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code takeLastBuffer} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param count
     *            the maximum number of items to emit
     * @param time
     *            the length of the time window
     * @param unit
     *            the time unit of {@code time}
     * @return an Observable that emits a single List containing at most {@code count} items emitted by the
     *         source Observable during the time window defined by {@code time} before the source Observable
     *         completed
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#takelastbuffer">RxJava wiki: takeLastBuffer</a>
     */
    public final Observable<List<T>> takeLastBuffer(int count, long time, TimeUnit unit) {
        return takeLast(count, time, unit).toList();
    }

    /**
     * Returns an Observable that emits a single List containing at most {@code count} items from the source
     * Observable that were emitted during a specified window of time (on a specified Scheduler) before the
     * source Observable completed.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/takeLastBuffer.tns.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param count
     *            the maximum number of items to emit
     * @param time
     *            the length of the time window
     * @param unit
     *            the time unit of {@code time}
     * @param scheduler
     *            the Scheduler that provides the timestamps for the observed items
     * @return an Observable that emits a single List containing at most {@code count} items emitted by the
     *         source Observable during the time window defined by {@code time} before the source Observable
     *         completed
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#takelastbuffer">RxJava wiki: takeLastBuffer</a>
     */
    public final Observable<List<T>> takeLastBuffer(int count, long time, TimeUnit unit, Scheduler scheduler) {
        return takeLast(count, time, unit, scheduler).toList();
    }

    /**
     * Returns an Observable that emits a single List containing those items from the source Observable that
     * were emitted during a specified window of time before the source Observable completed.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/takeLastBuffer.t.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code takeLastBuffer} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param time
     *            the length of the time window
     * @param unit
     *            the time unit of {@code time}
     * @return an Observable that emits a single List containing the items emitted by the source Observable
     *         during the time window defined by {@code time} before the source Observable completed
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#takelastbuffer">RxJava wiki: takeLastBuffer</a>
     */
    public final Observable<List<T>> takeLastBuffer(long time, TimeUnit unit) {
        return takeLast(time, unit).toList();
    }

    /**
     * Returns an Observable that emits a single List containing those items from the source Observable that
     * were emitted during a specified window of time before the source Observable completed, where the timing
     * information is provided by the given Scheduler.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/takeLastBuffer.ts.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param time
     *            the length of the time window
     * @param unit
     *            the time unit of {@code time}
     * @param scheduler
     *            the Scheduler that provides the timestamps for the observed items
     * @return an Observable that emits a single List containing the items emitted by the source Observable
     *         during the time window defined by {@code time} before the source Observable completed, where the
     *         timing information is provided by {@code scheduler}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#takelastbuffer">RxJava wiki: takeLastBuffer</a>
     */
    public final Observable<List<T>> takeLastBuffer(long time, TimeUnit unit, Scheduler scheduler) {
        return takeLast(time, unit, scheduler).toList();
    }

    /**
     * Returns an Observable that emits the items emitted by the source Observable until a second Observable
     * emits an item.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/takeUntil.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code takeUntil} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param other
     *            the Observable whose first emitted item will cause {@code takeUntil} to stop emitting items
     *            from the source Observable
     * @param <E>
     *            the type of items emitted by {@code other}
     * @return an Observable that emits the items emitted by the source Observable until such time as {@code other} emits its first item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#takeuntil">RxJava wiki: takeUntil</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229530.aspx">MSDN: Observable.TakeUntil</a>
     */
    public final <E> Observable<T> takeUntil(Observable<? extends E> other) {
        return lift(new OperatorTakeUntil<T, E>(other));
    }

    /**
     * Returns an Observable that emits items emitted by the source Observable so long as each item satisfied a
     * specified condition, and then completes as soon as this condition is not satisfied.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/takeWhile.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code takeWhile} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param predicate
     *            a function that evaluates an item emitted by the source Observable and returns a Boolean
     * @return an Observable that emits the items from the source Observable so long as each item satisfies the
     *         condition defined by {@code predicate}, then completes
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#takewhile-and-takewhilewithindex">RxJava wiki: takeWhile</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh244231.aspx">MSDN: Observable.TakeWhile</a>
     */
    public final Observable<T> takeWhile(final Func1<? super T, Boolean> predicate) {
        return lift(new OperatorTakeWhile<T>(predicate));
    }

    /**
     * Returns an Observable that emits the items emitted by a source Observable so long as a given predicate
     * remains true, where the predicate operates on both the item and its index relative to the complete
     * sequence of emitted items.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/takeWhileWithIndex.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code takeWhile} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param predicate
     *            a function to test each item emitted by the source Observable for a condition; the second
     *            parameter of the function represents the sequential index of the source item; it returns a
     *            Boolean
     * @return an Observable that emits items from the source Observable so long as the predicate continues to
     *         return {@code true} for each item, then completes
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#takewhile-and-takewhilewithindex">RxJava wiki: takeWhileWithIndex</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229131.aspx">MSDN: Observable.TakeWhile</a>
     */
    public final Observable<T> takeWhileWithIndex(final Func2<? super T, ? super Integer, Boolean> predicate) {
        return lift(new OperatorTakeWhile<T>(predicate));
    }

    /**
     * Returns an Observable that emits only the first item emitted by the source Observable during sequential
     * time windows of a specified duration.
     * <p>
     * This differs from {@link #throttleLast} in that this only tracks passage of time whereas
     * {@link #throttleLast} ticks at scheduled intervals.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/throttleFirst.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code throttleFirst} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param windowDuration
     *            time to wait before emitting another item after emitting the last item
     * @param unit
     *            the unit of time of {@code windowDuration}
     * @return an Observable that performs the throttle operation
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#throttlefirst">RxJava wiki: throttleFirst</a>
     */
    public final Observable<T> throttleFirst(long windowDuration, TimeUnit unit) {
        return lift(new OperatorThrottleFirst<T>(windowDuration, unit, Schedulers.computation()));
    }

    /**
     * Returns an Observable that emits only the first item emitted by the source Observable during sequential
     * time windows of a specified duration, where the windows are managed by a specified Scheduler.
     * <p>
     * This differs from {@link #throttleLast} in that this only tracks passage of time whereas
     * {@link #throttleLast} ticks at scheduled intervals.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/throttleFirst.s.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param skipDuration
     *            time to wait before emitting another item after emitting the last item
     * @param unit
     *            the unit of time of {@code skipDuration}
     * @param scheduler
     *            the {@link Scheduler} to use internally to manage the timers that handle timeout for each
     *            event
     * @return an Observable that performs the throttle operation
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#throttlefirst">RxJava wiki: throttleFirst</a>
     */
    public final Observable<T> throttleFirst(long skipDuration, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorThrottleFirst<T>(skipDuration, unit, scheduler));
    }

    /**
     * Returns an Observable that emits only the last item emitted by the source Observable during sequential
     * time windows of a specified duration.
     * <p>
     * This differs from {@link #throttleFirst} in that this ticks along at a scheduled interval whereas
     * {@link #throttleFirst} does not tick, it just tracks passage of time.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/throttleLast.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code throttleLast} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param intervalDuration
     *            duration of windows within which the last item emitted by the source Observable will be
     *            emitted
     * @param unit
     *            the unit of time of {@code intervalDuration}
     * @return an Observable that performs the throttle operation
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#sample-or-throttlelast">RxJava wiki: throttleLast</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.sample.aspx">MSDN: Observable.Sample</a>
     * @see #sample(long, TimeUnit)
     */
    public final Observable<T> throttleLast(long intervalDuration, TimeUnit unit) {
        return sample(intervalDuration, unit);
    }

    /**
     * Returns an Observable that emits only the last item emitted by the source Observable during sequential
     * time windows of a specified duration, where the duration is governed by a specified Scheduler.
     * <p>
     * This differs from {@link #throttleFirst} in that this ticks along at a scheduled interval whereas
     * {@link #throttleFirst} does not tick, it just tracks passage of time.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/throttleLast.s.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param intervalDuration
     *            duration of windows within which the last item emitted by the source Observable will be
     *            emitted
     * @param unit
     *            the unit of time of {@code intervalDuration}
     * @param scheduler
     *            the {@link Scheduler} to use internally to manage the timers that handle timeout for each
     *            event
     * @return an Observable that performs the throttle operation
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#sample-or-throttlelast">RxJava wiki: throttleLast</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.sample.aspx">MSDN: Observable.Sample</a>
     * @see #sample(long, TimeUnit, Scheduler)
     */
    public final Observable<T> throttleLast(long intervalDuration, TimeUnit unit, Scheduler scheduler) {
        return sample(intervalDuration, unit, scheduler);
    }

    /**
     * Returns an Observable that only emits those items emitted by the source Observable that are not followed
     * by another emitted item within a specified time window.
     * <p>
     * <em>Note:</em> If the source Observable keeps emitting items more frequently than the length of the time
     * window then no items will be emitted by the resulting Observable.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/throttleWithTimeout.png" alt="">
     * <p>
     * Information on debounce vs throttle:
     * <p>
     * <ul>
     * <li><a href="http://drupalmotion.com/article/debounce-and-throttle-visual-explanation">Debounce and Throttle: visual explanation</a></li>
     * <li><a href="http://unscriptable.com/2009/03/20/debouncing-javascript-methods/">Debouncing: javascript methods</a></li>
     * <li><a href="http://www.illyriad.co.uk/blog/index.php/2011/09/javascript-dont-spam-your-server-debounce-and-throttle/">Javascript - don't spam your server: debounce and throttle</a></li>
     * </ul>
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code throttleWithTimeout} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param timeout
     *            the length of the window of time that must pass after the emission of an item from the source
     *            Observable in which that Observable emits no items in order for the item to be emitted by the
     *            resulting Observable
     * @param unit
     *            the {@link TimeUnit} of {@code timeout}
     * @return an Observable that filters out items that are too quickly followed by newer items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#throttlewithtimeout-or-debounce">RxJava wiki: throttleWithTimeout</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.throttle.aspx">MSDN: Observable.Throttle</a>
     * @see #debounce(long, TimeUnit)
     */
    public final Observable<T> throttleWithTimeout(long timeout, TimeUnit unit) {
        return debounce(timeout, unit);
    }

    /**
     * Returns an Observable that only emits those items emitted by the source Observable that are not followed
     * by another emitted item within a specified time window, where the time window is governed by a specified
     * Scheduler.
     * <p>
     * <em>Note:</em> If the source Observable keeps emitting items more frequently than the length of the time
     * window then no items will be emitted by the resulting Observable.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/throttleWithTimeout.s.png" alt="">
     * <p>
     * Information on debounce vs throttle:
     * <p>
     * <ul>
     * <li><a href="http://drupalmotion.com/article/debounce-and-throttle-visual-explanation">Debounce and Throttle: visual explanation</a></li>
     * <li><a href="http://unscriptable.com/2009/03/20/debouncing-javascript-methods/">Debouncing: javascript methods</a></li>
     * <li><a href="http://www.illyriad.co.uk/blog/index.php/2011/09/javascript-dont-spam-your-server-debounce-and-throttle/">Javascript - don't spam your server: debounce and throttle</a></li>
     * </ul>
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param timeout
     *            the length of the window of time that must pass after the emission of an item from the source
     *            Observable in which that Observable emits no items in order for the item to be emitted by the
     *            resulting Observable
     * @param unit
     *            the {@link TimeUnit} of {@code timeout}
     * @param scheduler
     *            the {@link Scheduler} to use internally to manage the timers that handle the timeout for each
     *            item
     * @return an Observable that filters out items that are too quickly followed by newer items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#throttlewithtimeout-or-debounce">RxJava wiki: throttleWithTimeout</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.throttle.aspx">MSDN: Observable.Throttle</a>
     * @see #debounce(long, TimeUnit, Scheduler)
     */
    public final Observable<T> throttleWithTimeout(long timeout, TimeUnit unit, Scheduler scheduler) {
        return debounce(timeout, unit, scheduler);
    }

    /**
     * Returns an Observable that emits records of the time interval between consecutive items emitted by the
     * source Observable.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/timeInterval.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code timeInterval} operates by default on the {@code immediate} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that emits time interval information items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#timeinterval">RxJava wiki: timeInterval</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212107.aspx">MSDN: Observable.TimeInterval</a>
     */
    public final Observable<TimeInterval<T>> timeInterval() {
        return lift(new OperatorTimeInterval<T>(Schedulers.immediate()));
    }

    /**
     * Returns an Observable that emits records of the time interval between consecutive items emitted by the
     * source Observable, where this interval is computed on a specified Scheduler.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/timeInterval.s.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param scheduler
     *            the {@link Scheduler} used to compute time intervals
     * @return an Observable that emits time interval information items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#timeinterval">RxJava wiki: timeInterval</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212107.aspx">MSDN: Observable.TimeInterval</a>
     */
    public final Observable<TimeInterval<T>> timeInterval(Scheduler scheduler) {
        return lift(new OperatorTimeInterval<T>(scheduler));
    }

    /**
     * Returns an Observable that mirrors the source Observable, but notifies observers of a
     * {@code TimeoutException} if either the first item emitted by the source Observable or any subsequent item
     * doesn't arrive within time windows defined by other Observables.
     * <p>
     * <img width="640" height="400" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/timeout5.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code timeout} operates by default on the {@code immediate} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <U>
     *            the first timeout value type (ignored)
     * @param <V>
     *            the subsequent timeout value type (ignored)
     * @param firstTimeoutSelector
     *            a function that returns an Observable that determines the timeout window for the first source
     *            item
     * @param timeoutSelector
     *            a function that returns an Observable for each item emitted by the source Observable and that
     *            determines the timeout window in which the subsequent source item must arrive in order to
     *            continue the sequence
     * @return an Observable that mirrors the source Observable, but notifies observers of a
     *         {@code TimeoutException} if either the first item or any subsequent item doesn't arrive within
     *         the time windows specified by the timeout selectors
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#timeout">RxJava wiki: timeout</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.timeout.aspx">MSDN: Observable.Timeout</a>
     */
    public final <U, V> Observable<T> timeout(Func0<? extends Observable<U>> firstTimeoutSelector, Func1<? super T, ? extends Observable<V>> timeoutSelector) {
        return timeout(firstTimeoutSelector, timeoutSelector, null);
    }

    /**
     * Returns an Observable that mirrors the source Observable, but switches to a fallback Observable if either
     * the first item emitted by the source Observable or any subsequent item doesn't arrive within time windows
     * defined by other Observables.
     * <p>
     * <img width="640" height="400" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/timeout6.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code timeout} operates by default on the {@code immediate} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <U>
     *            the first timeout value type (ignored)
     * @param <V>
     *            the subsequent timeout value type (ignored)
     * @param firstTimeoutSelector
     *            a function that returns an Observable which determines the timeout window for the first source
     *            item
     * @param timeoutSelector
     *            a function that returns an Observable for each item emitted by the source Observable and that
     *            determines the timeout window in which the subsequent source item must arrive in order to
     *            continue the sequence
     * @param other
     *            the fallback Observable to switch to if the source Observable times out
     * @return an Observable that mirrors the source Observable, but switches to the {@code other} Observable if
     *         either the first item emitted by the source Observable or any subsequent item doesn't arrive
     *         within time windows defined by the timeout selectors
     * @throws NullPointerException
     *             if {@code timeoutSelector} is null
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#timeout">RxJava wiki: timeout</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.timeout.aspx">MSDN: Observable.Timeout</a>
     */
    public final <U, V> Observable<T> timeout(Func0<? extends Observable<U>> firstTimeoutSelector, Func1<? super T, ? extends Observable<V>> timeoutSelector, Observable<? extends T> other) {
        if (timeoutSelector == null) {
            throw new NullPointerException("timeoutSelector is null");
        }
        return lift(new OperatorTimeoutWithSelector<T, U, V>(firstTimeoutSelector, timeoutSelector, other));
    }

    /**
     * Returns an Observable that mirrors the source Observable, but notifies observers of a
     * {@code TimeoutException} if an item emitted by the source Observable doesn't arrive within a window of
     * time after the emission of the previous item, where that period of time is measured by an Observable that
     * is a function of the previous item.
     * <p>
     * <img width="640" height="400" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/timeout3.png" alt="">
     * <p>
     * Note: The arrival of the first source item is never timed out.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code timeout} operates by default on the {@code immediate} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <V>
     *            the timeout value type (ignored)
     * @param timeoutSelector
     *            a function that returns an observable for each item emitted by the source
     *            Observable and that determines the timeout window for the subsequent item
     * @return an Observable that mirrors the source Observable, but notifies observers of a
     *         {@code TimeoutException} if an item emitted by the source Observable takes longer to arrive than
     *         the time window defined by the selector for the previously emitted item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#timeout">RxJava wiki: timeout</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.timeout.aspx">MSDN: Observable.Timeout</a>
     */
    public final <V> Observable<T> timeout(Func1<? super T, ? extends Observable<V>> timeoutSelector) {
        return timeout(null, timeoutSelector, null);
    }

    /**
     * Returns an Observable that mirrors the source Observable, but that switches to a fallback Observable if
     * an item emitted by the source Observable doesn't arrive within a window of time after the emission of the
     * previous item, where that period of time is measured by an Observable that is a function of the previous
     * item.
     * <p>
     * <img width="640" height="400" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/timeout4.png" alt="">
     * <p>
     * Note: The arrival of the first source item is never timed out.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code timeout} operates by default on the {@code immediate} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <V>
     *            the timeout value type (ignored)
     * @param timeoutSelector
     *            a function that returns an Observable, for each item emitted by the source Observable, that
     *            determines the timeout window for the subsequent item
     * @param other
     *            the fallback Observable to switch to if the source Observable times out
     * @return an Observable that mirrors the source Observable, but switches to mirroring a fallback Observable
     *         if an item emitted by the source Observable takes longer to arrive than the time window defined
     *         by the selector for the previously emitted item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#timeout">RxJava wiki: timeout</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.timeout.aspx">MSDN: Observable.Timeout</a>
     */
    public final <V> Observable<T> timeout(Func1<? super T, ? extends Observable<V>> timeoutSelector, Observable<? extends T> other) {
        return timeout(null, timeoutSelector, other);
    }

    /**
     * Returns an Observable that mirrors the source Observable but applies a timeout policy for each emitted
     * item. If the next item isn't emitted within the specified timeout duration starting from its predecessor,
     * the resulting Observable terminates and notifies observers of a {@code TimeoutException}.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/timeout.1.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code timeout} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param timeout
     *            maximum duration between emitted items before a timeout occurs
     * @param timeUnit
     *            the unit of time that applies to the {@code timeout} argument.
     * @return the source Observable modified to notify observers of a {@code TimeoutException} in case of a
     *         timeout
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#timeout">RxJava wiki: timeout</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh244283.aspx">MSDN: Observable.Timeout</a>
     */
    public final Observable<T> timeout(long timeout, TimeUnit timeUnit) {
        return timeout(timeout, timeUnit, null, Schedulers.computation());
    }

    /**
     * Returns an Observable that mirrors the source Observable but applies a timeout policy for each emitted
     * item. If the next item isn't emitted within the specified timeout duration starting from its predecessor,
     * the resulting Observable begins instead to mirror a fallback Observable.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/timeout.2.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code timeout} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param timeout
     *            maximum duration between items before a timeout occurs
     * @param timeUnit
     *            the unit of time that applies to the {@code timeout} argument
     * @param other
     *            the fallback Observable to use in case of a timeout
     * @return the source Observable modified to switch to the fallback Observable in case of a timeout
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#timeout">RxJava wiki: timeout</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229512.aspx">MSDN: Observable.Timeout</a>
     */
    public final Observable<T> timeout(long timeout, TimeUnit timeUnit, Observable<? extends T> other) {
        return timeout(timeout, timeUnit, other, Schedulers.computation());
    }

    /**
     * Returns an Observable that mirrors the source Observable but applies a timeout policy for each emitted
     * item using a specified Scheduler. If the next item isn't emitted within the specified timeout duration
     * starting from its predecessor, the resulting Observable begins instead to mirror a fallback Observable.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/timeout.2s.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param timeout
     *            maximum duration between items before a timeout occurs
     * @param timeUnit
     *            the unit of time that applies to the {@code timeout} argument
     * @param other
     *            the Observable to use as the fallback in case of a timeout
     * @param scheduler
     *            the {@link Scheduler} to run the timeout timers on
     * @return the source Observable modified so that it will switch to the fallback Observable in case of a
     *         timeout
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#timeout">RxJava wiki: timeout</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211676.aspx">MSDN: Observable.Timeout</a>
     */
    public final Observable<T> timeout(long timeout, TimeUnit timeUnit, Observable<? extends T> other, Scheduler scheduler) {
        return lift(new OperatorTimeout<T>(timeout, timeUnit, other, scheduler));
    }

    /**
     * Returns an Observable that mirrors the source Observable but applies a timeout policy for each emitted
     * item, where this policy is governed on a specified Scheduler. If the next item isn't emitted within the
     * specified timeout duration starting from its predecessor, the resulting Observable terminates and
     * notifies observers of a {@code TimeoutException}.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/timeout.1s.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param timeout
     *            maximum duration between items before a timeout occurs
     * @param timeUnit
     *            the unit of time that applies to the {@code timeout} argument
     * @param scheduler
     *            the Scheduler to run the timeout timers on
     * @return the source Observable modified to notify observers of a {@code TimeoutException} in case of a
     *         timeout
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#timeout">RxJava wiki: timeout</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh228946.aspx">MSDN: Observable.Timeout</a>
     */
    public final Observable<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout(timeout, timeUnit, null, scheduler);
    }

    /**
     * Returns an Observable that emits each item emitted by the source Observable, wrapped in a
     * {@link Timestamped} object.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/timestamp.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code timestamp} operates by default on the {@code immediate} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that emits timestamped items from the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#timestamp">RxJava wiki: timestamp</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229003.aspx">MSDN: Observable.Timestamp</a>
     */
    public final Observable<Timestamped<T>> timestamp() {
        return timestamp(Schedulers.immediate());
    }

    /**
     * Returns an Observable that emits each item emitted by the source Observable, wrapped in a
     * {@link Timestamped} object whose timestamps are provided by a specified Scheduler.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/timestamp.s.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param scheduler
     *            the {@link Scheduler} to use as a time source
     * @return an Observable that emits timestamped items from the source Observable with timestamps provided by
     *         the {@code scheduler}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#timestamp">RxJava wiki: timestamp</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229003.aspx">MSDN: Observable.Timestamp</a>
     */
    public final Observable<Timestamped<T>> timestamp(Scheduler scheduler) {
        return lift(new OperatorTimestamp<T>(scheduler));
    }

    /**
     * Converts an Observable into a {@link BlockingObservable} (an Observable with blocking operators).
     * 
     * @return a {@code BlockingObservable} version of this Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Blocking-Observable-Operators">RxJava wiki: Blocking Observable Operators</a>
     * @deprecated use {@link #toBlocking()} instead
     */
    @Deprecated
    public final BlockingObservable<T> toBlockingObservable() {
        return BlockingObservable.from(this);
    }

    /**
     * Converts an Observable into a {@link BlockingObservable} (an Observable with blocking operators).
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code toBlocking} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @return a {@code BlockingObservable} version of this Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Blocking-Observable-Operators">RxJava wiki: Blocking Observable Operators</a>
     * @since 0.19
     */
    public final BlockingObservable<T> toBlocking() {
        return BlockingObservable.from(this);
    }

    /**
     * Returns an Observable that emits a single item, a list composed of all the items emitted by the source
     * Observable.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/toList.png" alt="">
     * <p>
     * Normally, an Observable that returns multiple items will do so by invoking its {@link Observer}'s
     * {@link Observer#onNext onNext} method for each such item. You can change this behavior, instructing the
     * Observable to compose a list of all of these items and then to invoke the Observer's {@code onNext}
     * function once, passing it the entire list, by calling the Observable's {@code toList} method prior to
     * calling its {@link #subscribe} method.
     * <p>
     * Be careful not to use this operator on Observables that emit infinite or very large numbers of items, as
     * you do not have the option to unsubscribe.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as by intent it is requesting and buffering everything.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code toList} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @return an Observable that emits a single item: a List containing all of the items emitted by the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#tolist">RxJava wiki: toList</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211848.aspx">MSDN: Observable.ToList</a>
     */
    public final Observable<List<T>> toList() {
        return lift(new OperatorToObservableList<T>());
    }

    /**
     * Returns an Observable that emits a single HashMap containing all items emitted by the source Observable,
     * mapped by the keys returned by a specified {@code keySelector} function.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/toMap.png" alt="">
     * <p>
     * If more than one source item maps to the same key, the HashMap will contain the latest of those items.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as by intent it is requesting and buffering everything.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code toMap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param keySelector
     *            the function that extracts the key from a source item to be used in the HashMap
     * @return an Observable that emits a single item: a HashMap containing the mapped items from the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#tomap-and-tomultimap">RxJava wiki: toMap</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229137.aspx">MSDN: Observable.ToDictionary</a>
     */
    public final <K> Observable<Map<K, T>> toMap(Func1<? super T, ? extends K> keySelector) {
        return lift(new OperatorToMap<T, K, T>(keySelector, Functions.<T>identity()));
    }

    /**
     * Returns an Observable that emits a single HashMap containing values corresponding to items emitted by the
     * source Observable, mapped by the keys returned by a specified {@code keySelector} function.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/toMap.png" alt="">
     * <p>
     * If more than one source item maps to the same key, the HashMap will contain a single entry that
     * corresponds to the latest of those items.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as by intent it is requesting and buffering everything.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code toMap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param keySelector
     *            the function that extracts the key from a source item to be used in the HashMap
     * @param valueSelector
     *            the function that extracts the value from a source item to be used in the HashMap
     * @return an Observable that emits a single item: a HashMap containing the mapped items from the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#tomap-and-tomultimap">RxJava wiki: toMap</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212075.aspx">MSDN: Observable.ToDictionary</a>
     */
    public final <K, V> Observable<Map<K, V>> toMap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector) {
        return lift(new OperatorToMap<T, K, V>(keySelector, valueSelector));
    }

    /**
     * Returns an Observable that emits a single Map, returned by a specified {@code mapFactory} function, that
     * contains keys and values extracted from the items emitted by the source Observable.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/toMap.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as by intent it is requesting and buffering everything.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code toMap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param keySelector
     *            the function that extracts the key from a source item to be used in the Map
     * @param valueSelector
     *            the function that extracts the value from the source items to be used as value in the Map
     * @param mapFactory
     *            the function that returns a Map instance to be used
     * @return an Observable that emits a single item: a Map that contains the mapped items emitted by the
     *         source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#tomap-and-tomultimap">RxJava wiki: toMap</a>
     */
    public final <K, V> Observable<Map<K, V>> toMap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector, Func0<? extends Map<K, V>> mapFactory) {
        return lift(new OperatorToMap<T, K, V>(keySelector, valueSelector, mapFactory));
    }

    /**
     * Returns an Observable that emits a single HashMap that contains an ArrayList of items emitted by the
     * source Observable keyed by a specified {@code keySelector} function.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/toMultiMap.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as by intent it is requesting and buffering everything.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code toMultiMap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param keySelector
     *            the function that extracts the key from the source items to be used as key in the HashMap
     * @return an Observable that emits a single item: a HashMap that contains an ArrayList of items mapped from
     *         the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#tomap-and-tomultimap">RxJava wiki: toMap</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212098.aspx">MSDN: Observable.ToLookup</a>
     */
    public final <K> Observable<Map<K, Collection<T>>> toMultimap(Func1<? super T, ? extends K> keySelector) {
        return lift(new OperatorToMultimap<T, K, T>(keySelector, Functions.<T>identity()));
    }

    /**
     * Returns an Observable that emits a single HashMap that contains an ArrayList of values extracted by a
     * specified {@code valueSelector} function from items emitted by the source Observable, keyed by a
     * specified {@code keySelector} function.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/toMultiMap.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as by intent it is requesting and buffering everything.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code toMultiMap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param keySelector
     *            the function that extracts a key from the source items to be used as key in the HashMap
     * @param valueSelector
     *            the function that extracts a value from the source items to be used as value in the HashMap
     * @return an Observable that emits a single item: a HashMap that contains an ArrayList of items mapped from
     *         the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#tomap-and-tomultimap">RxJava wiki: toMap</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229101.aspx">MSDN: Observable.ToLookup</a>
     */
    public final <K, V> Observable<Map<K, Collection<V>>> toMultimap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector) {
        return lift(new OperatorToMultimap<T, K, V>(keySelector, valueSelector));
    }

    /**
     * Returns an Observable that emits a single Map, returned by a specified {@code mapFactory} function, that
     * contains an ArrayList of values, extracted by a specified {@code valueSelector} function from items
     * emitted by the source Observable and keyed by the {@code keySelector} function.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/toMultiMap.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as by intent it is requesting and buffering everything.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code toMultiMap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param keySelector
     *            the function that extracts a key from the source items to be used as the key in the Map
     * @param valueSelector
     *            the function that extracts a value from the source items to be used as the value in the Map
     * @param mapFactory
     *            the function that returns a Map instance to be used
     * @return an Observable that emits a single item: a Map that contains a list items mapped from the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#tomap-and-tomultimap">RxJava wiki: toMap</a>
     */
    public final <K, V> Observable<Map<K, Collection<V>>> toMultimap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector, Func0<? extends Map<K, Collection<V>>> mapFactory) {
        return lift(new OperatorToMultimap<T, K, V>(keySelector, valueSelector, mapFactory));
    }

    /**
     * Returns an Observable that emits a single Map, returned by a specified {@code mapFactory} function, that
     * contains a custom collection of values, extracted by a specified {@code valueSelector} function from
     * items emitted by the source Observable, and keyed by the {@code keySelector} function.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/toMultiMap.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as by intent it is requesting and buffering everything.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code toMultiMap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param keySelector
     *            the function that extracts a key from the source items to be used as the key in the Map
     * @param valueSelector
     *            the function that extracts a value from the source items to be used as the value in the Map
     * @param mapFactory
     *            the function that returns a Map instance to be used
     * @param collectionFactory
     *            the function that returns a Collection instance for a particular key to be used in the Map
     * @return an Observable that emits a single item: a Map that contains the collection of mapped items from
     *         the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#tomap-and-tomultimap">RxJava wiki: toMap</a>
     */
    public final <K, V> Observable<Map<K, Collection<V>>> toMultimap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector, Func0<? extends Map<K, Collection<V>>> mapFactory, Func1<? super K, ? extends Collection<V>> collectionFactory) {
        return lift(new OperatorToMultimap<T, K, V>(keySelector, valueSelector, mapFactory, collectionFactory));
    }

    /**
     * Returns an Observable that emits a list that contains the items emitted by the source Observable, in a
     * sorted order. Each item emitted by the Observable must implement {@link Comparable} with respect to all
     * other items in the sequence.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/toSortedList.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as by intent it is requesting and buffering everything.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code toSortedList} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @throws ClassCastException
     *             if any item emitted by the Observable does not implement {@link Comparable} with respect to
     *             all other items emitted by the Observable
     * @return an Observable that emits a list that contains the items emitted by the source Observable in
     *         sorted order
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#tosortedlist">RxJava wiki: toSortedList</a>
     */
    public final Observable<List<T>> toSortedList() {
        return lift(new OperatorToObservableSortedList<T>());
    }

    /**
     * Returns an Observable that emits a list that contains the items emitted by the source Observable, in a
     * sorted order based on a specified comparison function.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/toSortedList.f.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as by intent it is requesting and buffering everything.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code toSortedList} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param sortFunction
     *            a function that compares two items emitted by the source Observable and returns an Integer
     *            that indicates their sort order
     * @return an Observable that emits a list that contains the items emitted by the source Observable in
     *         sorted order
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#tosortedlist">RxJava wiki: toSortedList</a>
     */
    public final Observable<List<T>> toSortedList(Func2<? super T, ? super T, Integer> sortFunction) {
        return lift(new OperatorToObservableSortedList<T>(sortFunction));
    }

    /**
     * Modifies the source Observable so that subscribers will unsubscribe from it on a specified
     * {@link Scheduler}.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param scheduler
     *            the {@link Scheduler} to perform unsubscription actions on
     * @return the source Observable modified so that its unsubscriptions happen on the specified
     *         {@link Scheduler}
     * @since 0.17
     */
    public final Observable<T> unsubscribeOn(Scheduler scheduler) {
        return lift(new OperatorUnsubscribeOn<T>(scheduler));
    }

    /**
     * Returns an Observable that emits windows of items it collects from the source Observable. The resulting
     * Observable emits connected, non-overlapping windows. It emits the current window and opens a new one
     * whenever the Observable produced by the specified {@code closingSelector} emits an item.
     * <p>
     * <img width="640" height="485" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/window1.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses the {@code closingSelector} to control data
     *      flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code window} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param closingSelector
     *            a {@link Func0} that returns an {@code Observable} that governs the boundary between windows.
     *            When this {@code Observable} emits an item, {@code window} emits the current window and begins
     *            a new one.
     * @return an Observable that emits connected, non-overlapping windows of items from the source Observable
     *         whenever {@code closingSelector} emits an item
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#window">RxJava wiki: window</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.window.aspx">MSDN: Observable.Window</a>
     */
    public final <TClosing> Observable<Observable<T>> window(Func0<? extends Observable<? extends TClosing>> closingSelector) {
        return lift(new OperatorWindowWithObservable<T, TClosing>(closingSelector));
    }

    /**
     * Returns an Observable that emits windows of items it collects from the source Observable. The resulting
     * Observable emits connected, non-overlapping windows, each containing {@code count} items. When the source
     * Observable completes or encounters an error, the resulting Observable emits the current window and
     * propagates the notification from the source Observable.
     * <p>
     * <img width="640" height="400" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/window3.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses {@code count} to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code window} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param count
     *            the maximum size of each window before it should be emitted
     * @return an Observable that emits connected, non-overlapping windows, each containing at most
     *         {@code count} items from the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#window">RxJava wiki: window</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.window.aspx">MSDN: Observable.Window</a>
     */
    public final Observable<Observable<T>> window(int count) {
        return lift(new OperatorWindowWithSize<T>(count, count));
    }

    /**
     * Returns an Observable that emits windows of items it collects from the source Observable. The resulting
     * Observable emits windows every {@code skip} items, each containing no more than {@code count} items. When
     * the source Observable completes or encounters an error, the resulting Observable emits the current window
     * and propagates the notification from the source Observable.
     * <p>
     * <img width="640" height="365" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/window4.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses {@code count} to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code window} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param count
     *            the maximum size of each window before it should be emitted
     * @param skip
     *            how many items need to be skipped before starting a new window. Note that if {@code skip} and
     *            {@code count} are equal this is the same operation as {@link #window(int)}.
     * @return an Observable that emits windows every {@code skip} items containing at most {@code count} items
     *         from the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#window">RxJava wiki: window</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.window.aspx">MSDN: Observable.Window</a>
     */
    public final Observable<Observable<T>> window(int count, int skip) {
        return lift(new OperatorWindowWithSize<T>(count, skip));
    }

    /**
     * Returns an Observable that emits windows of items it collects from the source Observable. The resulting
     * Observable starts a new window periodically, as determined by the {@code timeshift} argument. It emits
     * each window after a fixed timespan, specified by the {@code timespan} argument. When the source
     * Observable completes or Observable completes or encounters an error, the resulting Observable emits the
     * current window and propagates the notification from the source Observable.
     * <p>
     * <img width="640" height="335" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/window7.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code window} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param timespan
     *            the period of time each window collects items before it should be emitted
     * @param timeshift
     *            the period of time after which a new window will be created
     * @param unit
     *            the unit of time that applies to the {@code timespan} and {@code timeshift} arguments
     * @return an Observable that emits new windows periodically as a fixed timespan elapses
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#window">RxJava wiki: window</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.window.aspx">MSDN: Observable.Window</a>
     */
    public final Observable<Observable<T>> window(long timespan, long timeshift, TimeUnit unit) {
        return lift(new OperatorWindowWithTime<T>(timespan, timeshift, unit, Integer.MAX_VALUE, Schedulers.computation()));
    }

    /**
     * Returns an Observable that emits windows of items it collects from the source Observable. The resulting
     * Observable starts a new window periodically, as determined by the {@code timeshift} argument. It emits
     * each window after a fixed timespan, specified by the {@code timespan} argument. When the source
     * Observable completes or Observable completes or encounters an error, the resulting Observable emits the
     * current window and propagates the notification from the source Observable.
     * <p>
     * <img width="640" height="335" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/window7.s.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param timespan
     *            the period of time each window collects items before it should be emitted
     * @param timeshift
     *            the period of time after which a new window will be created
     * @param unit
     *            the unit of time that applies to the {@code timespan} and {@code timeshift} arguments
     * @param scheduler
     *            the {@link Scheduler} to use when determining the end and start of a window
     * @return an Observable that emits new windows periodically as a fixed timespan elapses
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#window">RxJava wiki: window</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.window.aspx">MSDN: Observable.Window</a>
     */
    public final Observable<Observable<T>> window(long timespan, long timeshift, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorWindowWithTime<T>(timespan, timeshift, unit, Integer.MAX_VALUE, scheduler));
    }

    /**
     * Returns an Observable that emits windows of items it collects from the source Observable. The resulting
     * Observable emits connected, non-overlapping windows, each of a fixed duration specified by the
     * {@code timespan} argument. When the source Observable completes or encounters an error, the resulting
     * Observable emits the current window and propagates the notification from the source Observable.
     * <p>
     * <img width="640" height="375" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/window5.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code window} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param timespan
     *            the period of time each window collects items before it should be emitted and replaced with a
     *            new window
     * @param unit
     *            the unit of time that applies to the {@code timespan} argument
     * @return an Observable that emits connected, non-overlapping windows represending items emitted by the
     *         source Observable during fixed, consecutive durations
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#window">RxJava wiki: window</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.window.aspx">MSDN: Observable.Window</a>
     */
    public final Observable<Observable<T>> window(long timespan, TimeUnit unit) {
        return lift(new OperatorWindowWithTime<T>(timespan, timespan, unit, Integer.MAX_VALUE, Schedulers.computation()));
    }

    /**
     * Returns an Observable that emits windows of items it collects from the source Observable. The resulting
     * Observable emits connected, non-overlapping windows, each of a fixed duration as specified by the
     * {@code timespan} argument or a maximum size as specified by the {@code count} argument (whichever is
     * reached first). When the source Observable completes or encounters an error, the resulting Observable
     * emits the current window and propagates the notification from the source Observable.
     * <p>
     * <img width="640" height="370" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/window6.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code window} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param timespan
     *            the period of time each window collects items before it should be emitted and replaced with a
     *            new window
     * @param unit
     *            the unit of time that applies to the {@code timespan} argument
     * @param count
     *            the maximum size of each window before it should be emitted
     * @return an Observable that emits connected, non-overlapping windows of items from the source Observable
     *         that were emitted during a fixed duration of time or when the window has reached maximum capacity
     *         (whichever occurs first)
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#window">RxJava wiki: window</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.window.aspx">MSDN: Observable.Window</a>
     */
    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, int count) {
        return lift(new OperatorWindowWithTime<T>(timespan, timespan, unit, count, Schedulers.computation()));
    }

    /**
     * Returns an Observable that emits windows of items it collects from the source Observable. The resulting
     * Observable emits connected, non-overlapping windows, each of a fixed duration specified by the
     * {@code timespan} argument or a maximum size specified by the {@code count} argument (whichever is reached
     * first). When the source Observable completes or encounters an error, the resulting Observable emits the
     * current window and propagates the notification from the source Observable.
     * <p>
     * <img width="640" height="370" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/window6.s.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param timespan
     *            the period of time each window collects items before it should be emitted and replaced with a
     *            new window
     * @param unit
     *            the unit of time which applies to the {@code timespan} argument
     * @param count
     *            the maximum size of each window before it should be emitted
     * @param scheduler
     *            the {@link Scheduler} to use when determining the end and start of a window
     * @return an Observable that emits connected, non-overlapping windows of items from the source Observable
     *         that were emitted during a fixed duration of time or when the window has reached maximum capacity
     *         (whichever occurs first)
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#window">RxJava wiki: window</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.window.aspx">MSDN: Observable.Window</a>
     */
    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, int count, Scheduler scheduler) {
        return lift(new OperatorWindowWithTime<T>(timespan, timespan, unit, count, scheduler));
    }

    /**
     * Returns an Observable that emits windows of items it collects from the source Observable. The resulting
     * Observable emits connected, non-overlapping windows, each of a fixed duration as specified by the
     * {@code timespan} argument. When the source Observable completes or encounters an error, the resulting
     * Observable emits the current window and propagates the notification from the source Observable.
     * <p>
     * <img width="640" height="375" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/window5.s.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses time to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     * 
     * @param timespan
     *            the period of time each window collects items before it should be emitted and replaced with a
     *            new window
     * @param unit
     *            the unit of time which applies to the {@code timespan} argument
     * @param scheduler
     *            the {@link Scheduler} to use when determining the end and start of a window
     * @return an Observable that emits connected, non-overlapping windows containing items emitted by the
     *         source Observable within a fixed duration
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#window">RxJava wiki: window</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.window.aspx">MSDN: Observable.Window</a>
     */
    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorWindowWithTime<T>(timespan, timespan, unit, Integer.MAX_VALUE, scheduler));
    }

    /**
     * Returns an Observable that emits windows of items it collects from the source Observable. The resulting
     * Observable emits windows that contain those items emitted by the source Observable between the time when
     * the {@code windowOpenings} Observable emits an item and when the Observable returned by
     * {@code closingSelector} emits an item.
     * <p>
     * <img width="640" height="550" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/window2.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses Observables to control data flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code window} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param windowOpenings
     *            an Observable that, when it emits an item, causes another window to be created
     * @param closingSelector
     *            a {@link Func1} that produces an Observable for every window created. When this Observable
     *            emits an item, the associated window is closed and emitted
     * @return an Observable that emits windows of items emitted by the source Observable that are governed by
     *         the specified window-governing Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#window">RxJava wiki: window</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.window.aspx">MSDN: Observable.Window</a>
     */
    public final <TOpening, TClosing> Observable<Observable<T>> window(Observable<? extends TOpening> windowOpenings, Func1<? super TOpening, ? extends Observable<? extends TClosing>> closingSelector) {
        return lift(new OperatorWindowWithStartEndObservable<T, TOpening, TClosing>(windowOpenings, closingSelector));
    }

    /**
     * Returns an Observable that emits non-overlapping windows of items it collects from the source Observable
     * where the boundary of each window is determined by the items emitted from a specified boundary-governing
     * Observable.
     * <p>
     * <img width="640" height="475" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/window8.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as it uses a {@code boundary} Observable to control data
     *      flow.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code window} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <U>
     *            the window element type (ignored)
     * @param boundary
     *            an Observable whose emitted items close and open windows
     * @return an Observable that emits non-overlapping windows of items it collects from the source Observable
     *         where the boundary of each window is determined by the items emitted from the {@code boundary}
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#window">RxJava wiki: window</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.window.aspx">MSDN: Observable.Window</a>
     */
    public final <U> Observable<Observable<T>> window(Observable<U> boundary) {
        return lift(new OperatorWindowWithObservable<T, U>(boundary));
    }

    /**
     * Returns an Observable that emits items that are the result of applying a specified function to pairs of
     * values, one each from the source Observable and a specified Iterable sequence.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/zip.i.png" alt="">
     * <p>
     * Note that the {@code other} Iterable is evaluated as items are observed from the source Observable; it is
     * not pre-consumed. This allows you to zip infinite streams on either side.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <T2>
     *            the type of items in the {@code other} Iterable
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param other
     *            the Iterable sequence
     * @param zipFunction
     *            a function that combines the pairs of items from the Observable and the Iterable to generate
     *            the items to be emitted by the resulting Observable
     * @return an Observable that pairs up values from the source Observable and the {@code other} Iterable
     *         sequence and emits the results of {@code zipFunction} applied to these pairs
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#zip">RxJava wiki: zip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.zip.aspx">MSDN: Observable.Zip</a>
     */
    public final <T2, R> Observable<R> zip(Iterable<? extends T2> other, Func2<? super T, ? super T2, ? extends R> zipFunction) {
        return lift(new OperatorZipIterable<T, T2, R>(other, zipFunction));
    }

    /**
     * Returns an Observable that emits items that are the result of applying a specified function to pairs of
     * values, one each from the source Observable and another specified Observable.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/zip.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <T2>
     *            the type of items emitted by the {@code other} Observable
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param other
     *            the other Observable
     * @param zipFunction
     *            a function that combines the pairs of items from the two Observables to generate the items to
     *            be emitted by the resulting Observable
     * @return an Observable that pairs up values from the source Observable and the {@code other} Observable
     *         and emits the results of {@code zipFunction} applied to these pairs
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#zip">RxJava wiki: zip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.zip.aspx">MSDN: Observable.Zip</a>
     * @deprecated use {@code zipWith} instead. Changed to match naming convention of {@code mergeWith}, {@code concatWith}, etc.
     */
    @Deprecated
    public final <T2, R> Observable<R> zip(Observable<? extends T2> other, Func2<? super T, ? super T2, ? extends R> zipFunction) {
        return zip(this, other, zipFunction);
    }
    
    /**
     * Returns an Observable that emits items that are the result of applying a specified function to pairs of
     * values, one each from the source Observable and another specified Observable.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/zip.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code zipWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * 
     * @param <T2>
     *            the type of items emitted by the {@code other} Observable
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param other
     *            the other Observable
     * @param zipFunction
     *            a function that combines the pairs of items from the two Observables to generate the items to
     *            be emitted by the resulting Observable
     * @return an Observable that pairs up values from the source Observable and the {@code other} Observable
     *         and emits the results of {@code zipFunction} applied to these pairs
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#zip">RxJava wiki: zip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.zip.aspx">MSDN: Observable.Zip</a>
     * @since 0.20
     */
    public final <T2, R> Observable<R> zipWith(Observable<? extends T2> other, Func2<? super T, ? super T2, ? extends R> zipFunction) {
        return zip(this, other, zipFunction);
    }

    /**
     * An Observable that never sends any information to an {@link Observer}.
     * This Observable is useful primarily for testing purposes.
     * 
     * @param <T>
     *            the type of item (not) emitted by the Observable
     */
    private static class NeverObservable<T> extends Observable<T> {
        public NeverObservable() {
            super(new OnSubscribe<T>() {

                @Override
                public void call(Subscriber<? super T> observer) {
                    // do nothing
                }

            });
        }
    }

    /**
     * An Observable that invokes {@link Observer#onError onError} when the {@link Observer} subscribes to it.
     * 
     * @param <T>
     *            the type of item (ostensibly) emitted by the Observable
     */
    private static class ThrowObservable<T> extends Observable<T> {

        public ThrowObservable(final Throwable exception) {
            super(new OnSubscribe<T>() {

                /**
                 * Accepts an {@link Observer} and calls its {@link Observer#onError onError} method.
                 * 
                 * @param observer
                 *            an {@link Observer} of this Observable
                 */
                @Override
                public void call(Subscriber<? super T> observer) {
                    observer.onError(exception);
                }

            });
        }
    }
}
