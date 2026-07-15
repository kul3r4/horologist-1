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
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.remote.creation.compose.action.pendingIntentAction
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.layout.RemoteImage
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.modifier.width
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.runtime.Composable
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.WearWidgetBrush
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.color
import androidx.glance.wear.core.WearWidgetParams
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteColorScheme
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import coil.ImageLoader
import com.google.android.horologist.media.repository.PlaylistRepository
import com.google.android.horologist.mediasample.ui.app.MediaActivity
import kotlinx.coroutines.flow.first
import androidx.compose.remote.creation.compose.action.Action
import androidx.compose.ui.graphics.ImageBitmap
import coil.request.ImageRequest

/**
 * A Widget providing link to a playlist.
 */
class MediaCollectionsWidget(
    private val playlistRepository: PlaylistRepository,
    private val imageLoader: ImageLoader,
) : GlanceWearWidget() {

    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData {
        val playlists = playlistRepository.getAll().first()
        val firstPlaylist = playlists.first()

        val playlistIntent = Intent(context, MediaActivity::class.java).apply {
            putExtra(MediaActivity.CollectionKey, firstPlaylist.id)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val playlistPendingIntent = PendingIntent.getActivity(
            context,
            1,
            playlistIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val artworkBitmap = firstPlaylist.artworkUri?.let { uri ->
            val request = ImageRequest.Builder(context)
                .data(uri)
                .size(48)
                .allowHardware(false)
                .build()
            val result = imageLoader.execute(request)
            (result.drawable as? BitmapDrawable)?.bitmap
        }
        val playlistArtwork = artworkBitmap?.asImageBitmap()

      val remoteColorScheme = RemoteColorScheme()
        return WearWidgetDocument(background = WearWidgetBrush.color(remoteColorScheme.primary)) {
            WidgetContent(
                playlistName = firstPlaylist.name,
                playlistAction = pendingIntentAction { _ -> playlistPendingIntent },
                playlistArtwork = playlistArtwork,
            )
        }
    }
}

@RemoteComposable
@Composable
fun WidgetContent(
    playlistName: String,
    playlistAction: Action,
    playlistArtwork: ImageBitmap?,
) {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().padding(8.rdp),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteColumn(
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteButton(onClick = playlistAction) {
                if (playlistArtwork != null) {
                    RemoteImage(
                        bitmap = playlistArtwork,
                        contentDescription = "Artwork".rs,
                        modifier = RemoteModifier.size(48.rdp)
                    )
                    RemoteBox(modifier = RemoteModifier.width(8.rdp))
                }
                RemoteText(
                    text = playlistName.rs,
                  color = RemoteMaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
