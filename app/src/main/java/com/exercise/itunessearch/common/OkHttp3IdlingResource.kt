package com.exercise.itunessearch.common

/*
 * Copyright (C) 2016 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.support.annotation.CheckResult
import android.support.test.espresso.IdlingResource
import okhttp3.Dispatcher
import okhttp3.OkHttpClient

class OkHttp3IdlingResource private constructor(private val name: String,
                                                private val dispatcher: Dispatcher) : IdlingResource {
    @Volatile
    internal var callback: IdlingResource.ResourceCallback? = null

    init {
        dispatcher.setIdleCallback {
            val callback = this@OkHttp3IdlingResource.callback
            callback?.onTransitionToIdle()
        }
    }

    override fun getName(): String {
        return name
    }

    override fun isIdleNow(): Boolean {
        return dispatcher.runningCallsCount() == 0
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        this.callback = callback
    }

    companion object {
        /**
         * Create a new [IdlingResource] from `client` as `name`. You must register
         * this instance using `Espresso.registerIdlingResources`.
         */
        @CheckResult
        // Extra guards as a library.
        fun create(name: String, client: OkHttpClient): OkHttp3IdlingResource {
            if (name == null) throw NullPointerException("name == null")
            if (client == null) throw NullPointerException("client == null")
            return OkHttp3IdlingResource(name, client.dispatcher())
        }
    }
}