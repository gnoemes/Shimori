/*
 * Copyright 2019 Google LLC
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

package com.gnoemes.shimori.data.repositories.lastrequests

import com.gnoemes.shimori.data.daos.LastRequestDao
import com.gnoemes.shimori.model.app.LastRequest
import com.gnoemes.shimori.model.app.Request
import org.threeten.bp.Instant

open class GroupLastRequestStore(
    private val request: Request,
    private val dao: LastRequestDao
) {
    suspend fun getRequestInstant(id: Long): Instant? {
        return dao.lastRequest(request, id)?.timestamp
    }

    suspend fun isRequestBefore(instant: Instant): Boolean {
        return getRequestInstant(DEFAULT_ID)?.isBefore(instant) ?: true
    }

    suspend fun isRequestBefore(instant: Instant, id: Long): Boolean {
        return getRequestInstant(id)?.isBefore(instant) ?: true
    }

    suspend fun updateLastRequest(
        timestamp: Instant = Instant.now(),
        id : Long = DEFAULT_ID
    ) {
        dao.insert(LastRequest(request = request, entityId = id, timestamp = timestamp))
    }

    companion object {
        private const val DEFAULT_ID = 0L
    }
}
