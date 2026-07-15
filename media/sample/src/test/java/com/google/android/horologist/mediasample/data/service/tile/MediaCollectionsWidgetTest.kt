/*
 * Copyright 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.horologist.mediasample.data.service.tile

import android.app.PendingIntent
import android.content.Intent
import androidx.compose.remote.creation.compose.action.pendingIntentAction
import androidx.compose.remote.creation.profile.RcPlatformProfiles
import androidx.compose.remote.tooling.preview.RemoteContentPreview
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.horologist.mediasample.ui.app.MediaActivity
import androidx.compose.ui.graphics.asImageBitmap
import com.google.android.horologist.screenshots.rng.WearDevice
import com.google.android.horologist.screenshots.rng.WearDeviceScreenshotTest
import org.junit.Test

class MediaCollectionsWidgetTest(device: WearDevice) : WearDeviceScreenshotTest(device = device) {
    @Test
    fun widgetScreenshot() {
        runTest {
            val context = LocalContext.current
            val intent = Intent(context, MediaActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            val action = pendingIntentAction { _ -> pendingIntent }
            val dummyBitmap = android.graphics.BitmapFactory.decodeResource(
                context.resources,
                com.google.android.horologist.mediasample.R.drawable.kyoto
            )
            val dummyImageBitmap = dummyBitmap.asImageBitmap()

            RemoteContentPreview(
                modifier = Modifier,
                profile = RcPlatformProfiles.WEAR_WIDGETS,
            ) {
                WidgetContent(
                    playlistName = "Liked Songs",
                    playlistAction = action,
                    playlistArtwork = dummyImageBitmap,
                )
            }
        }
    }
}
