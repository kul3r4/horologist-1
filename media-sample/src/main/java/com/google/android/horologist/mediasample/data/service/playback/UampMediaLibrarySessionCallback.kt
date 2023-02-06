/*
 * Copyright 2021 The Android Open Source Project
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

package com.google.android.horologist.mediasample.data.service.playback

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.android.horologist.media3.logging.ErrorReporter
import com.google.android.horologist.media3.service.SuspendingMediaLibrarySessionCallback
import com.google.android.horologist.mediasample.data.service.playback.customactions.UampCustomActionHandler
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope

class UampMediaLibrarySessionCallback(
    serviceScope: CoroutineScope,
    appEventLogger: ErrorReporter,
    uampCustomActionHandler: UampCustomActionHandler
) : SuspendingMediaLibrarySessionCallback(serviceScope, appEventLogger) {

    private val uampCustomActionHandler = uampCustomActionHandler

    override suspend fun onGetLibraryRootInternal(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: MediaLibraryService.LibraryParams?
    ): LibraryResult<MediaItem> {
        // TODO implement
        return LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE)
    }
    //Declare custom commands in the available session commands when a controller connects
    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult {
        val connectionResult = super.onConnect(session, controller)
        val sessionCommandsBuilder =
            connectionResult.availableSessionCommands
                .buildUpon()
        uampCustomActionHandler.customLayout.forEach {
            it.sessionCommand()?.let { sessionCommand -> sessionCommandsBuilder.add(sessionCommand) }
        }
        return MediaSession.ConnectionResult.accept(
            sessionCommandsBuilder.build(), connectionResult.availablePlayerCommands)
    }

    //Handle custom actions
    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {
        // Execute the custom command.
        uampCustomActionHandler.customCommands[customCommand.customAction]?.onCustomCommand(session, controller)
        //TODO since only supporting like/unlike at the moment, this method is always called. Need to add logic here to udnerstand what to do
        uampCustomActionHandler.setLikeUnlikeCommand(customCommand.customAction)
        // Send the updated custom layout to controllers.
        session.setCustomLayout(controller, uampCustomActionHandler.getButtonLayout())
        return Futures.immediateFuture(
            SessionResult(SessionResult.RESULT_SUCCESS)
        )
    }

    override fun onPostConnect(session: MediaSession, controller: MediaSession.ControllerInfo) {
        if (uampCustomActionHandler.customCommands.isNotEmpty()) {
            // Let the controller now about the custom layout right after it connected.
            session.setCustomLayout(controller, uampCustomActionHandler.getButtonLayout())
        }
        super.onPostConnect(session, controller)
    }

    override suspend fun onGetItemInternal(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        mediaId: String
    ): LibraryResult<MediaItem> {
        // TODO implement
        return LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE)
    }

    override suspend fun onGetChildrenInternal(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): LibraryResult<ImmutableList<MediaItem>> {
        // TODO implement
        return LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE)
    }
}
