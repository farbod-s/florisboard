/*
 * Copyright (C) 2025 The FlorisBoard Contributors
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

package com.ethica.monitoring

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KeystrokeTracker(private val context: Context) {
    companion object {
        private const val TABLE_NAME = "keystrokes"
        private const val CONTENT_AUTHORITY = "com.ethica.logger.provider.text_tracker"
        private val BASE_CONTENT_URI: Uri = "content://$CONTENT_AUTHORITY".toUri()
        private val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build()

        // Column names
        const val CNK_TIMESTAMP = "timestamp"
        const val CNK_TEXT = "text"
        const val CNK_SOURCE_PACKAGE = "source_package"
    }

    suspend fun track(text: String, sourcePackage: String?) = withContext(Dispatchers.IO) {
        val values = ContentValues().apply {
            put(CNK_TIMESTAMP, System.currentTimeMillis())
            put(CNK_TEXT, text)
            put(CNK_SOURCE_PACKAGE, sourcePackage ?: "Unknown")
        }
        context.contentResolver.insert(CONTENT_URI, values)
    }
}
