/*
 * Copyright 2016 FuckBoilerplate
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

package com.refineriaweb.rx_paparazzo.library.workers;

import android.app.Activity;

import com.refineriaweb.rx_paparazzo.library.entities.Response;
import com.refineriaweb.rx_paparazzo.library.entities.TargetUi;
import com.refineriaweb.rx_paparazzo.library.entities.UserCanceledException;

import rx.Observable;
import rx.exceptions.Exceptions;

abstract class Worker {
    private final TargetUi targetUi;

    public Worker(TargetUi targetUi) {
        this.targetUi = targetUi;
    }

    protected <T> Observable.Transformer<T, T> applyOnError() {
        return (observable) -> {
            return observable.onErrorResumeNext(throwable -> {
                if (throwable instanceof UserCanceledException) {
                    return Observable.just((T) new Response(targetUi.ui(), null, Activity.RESULT_CANCELED));
                }
                throw Exceptions.propagate(throwable);
            });
        };
    }
}
