/*
 * Copyright 2018 Google LLC
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

package com.gnoemes.shimori.appinitializers

import android.app.Application
import com.gnoemes.shimori.base.appinitializers.AppInitializer
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.zone.ZoneRulesProvider
import javax.inject.Inject

class ThreeTenBpInitializer @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers
) : AppInitializer {
    override fun init(app: Application) {
        AndroidThreeTen.init(app)

        // Query the ZoneRulesProvider so that it is loaded on a background coroutine
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(dispatchers.io) {
            ZoneRulesProvider.getAvailableZoneIds()
        }
    }
}
