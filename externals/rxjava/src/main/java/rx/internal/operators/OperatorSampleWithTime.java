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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import rx.Observable.Operator;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.observers.SerializedSubscriber;

/**
 * Returns an Observable that emits the results of sampling the items emitted by the source
 * Observable at a specified time interval.
 * <p>
 * <img width="640" src="https://github.com/Netflix/RxJava/wiki/images/rx-operators/sample.png" alt="">
 * 
 * @param <T> the value type
 */
public final class OperatorSampleWithTime<T> implements Operator<T, T> {
    final long time;
    final TimeUnit unit;
    final Scheduler scheduler;

    public OperatorSampleWithTime(long time, TimeUnit unit, Scheduler scheduler) {
        this.time = time;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    @Override
    public Subscriber<? super T> call(Subscriber<? super T> child) {
        final SerializedSubscriber<T> s = new SerializedSubscriber<T>(child);
        final Worker worker = scheduler.createWorker();
        child.add(worker);
        
        SamplerSubscriber<T> sampler = new SamplerSubscriber<T>(s);
        worker.schedulePeriodically(sampler, time, time, unit);

        return sampler;
    }
    /**
     * The source subscriber and sampler.
     */
    static final class SamplerSubscriber<T> extends Subscriber<T> implements Action0 {
        private final Subscriber<? super T> subscriber;
        /** Indicates that no value is available. */
        private static final Object EMPTY_TOKEN = new Object();
        /** The shared value between the observer and the timed action. */
        volatile Object value = EMPTY_TOKEN;
        /** Updater for the value field. */
        @SuppressWarnings("rawtypes")
        static final AtomicReferenceFieldUpdater<SamplerSubscriber, Object> VALUE_UPDATER
                = AtomicReferenceFieldUpdater.newUpdater(SamplerSubscriber.class, Object.class, "value");
        public SamplerSubscriber(Subscriber<? super T> subscriber) {
            this.subscriber = subscriber;
        }
        
        @Override
        public void onStart() {
            request(Long.MAX_VALUE);
        }
        
        @Override
        public void onNext(T t) {
            value = t;
        }

        @Override
        public void onError(Throwable e) {
            subscriber.onError(e);
            unsubscribe();
        }

        @Override
        public void onCompleted() {
            subscriber.onCompleted();
            unsubscribe();
        }

        @Override
        public void call() {
            Object localValue = VALUE_UPDATER.getAndSet(this, EMPTY_TOKEN);
            if (localValue != EMPTY_TOKEN) {
                try {
                    @SuppressWarnings("unchecked")
                    T v = (T)localValue;
                    subscriber.onNext(v);
                } catch (Throwable e) {
                    onError(e);
                }
            }
        }
    }
}
