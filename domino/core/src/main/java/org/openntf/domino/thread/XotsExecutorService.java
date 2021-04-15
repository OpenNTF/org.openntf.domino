/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.thread;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.openntf.domino.thread.AbstractDominoExecutor.DominoFutureTask;

public interface XotsExecutorService extends ScheduledExecutorService {

	List<DominoFutureTask<?>> getTasks(Comparator<DominoFutureTask<?>> comparator);

	<V> ScheduledFuture<V> schedule(Callable<V> callable, Scheduler scheduler);

	ScheduledFuture<?> schedule(Runnable runnable, Scheduler scheduler);

	ScheduledFuture<?> scheduleTasklet(String moduleName, String className, Scheduler scheduler, Object... ctorArgs);

	ScheduledFuture<?> runTasklet(String moduleName, String className, Object... ctorArgs);

}
