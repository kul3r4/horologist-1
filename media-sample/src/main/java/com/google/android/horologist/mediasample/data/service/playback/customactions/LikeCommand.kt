/*
 * Copyright 2023 The Android Open Source Project
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

package com.google.android.horologist.mediasample.data.service.playback.customactions

import android.os.Bundle
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import com.google.android.horologist.media3.R

class LikeCommand : UampCustomCommand {

    /*
    1. method to return SessionCommand
    2. method to handle onCustomCommand
    3. method to return CommandButton
    */
    private val likeCustomCommand = "Like"

    override fun sessionCommand(): SessionCommand {
        return SessionCommand(likeCustomCommand, Bundle.EMPTY)
    }

    override fun customCommand(): String {
        return likeCustomCommand
    }

    override fun commandButton(): CommandButton{
        return CommandButton.Builder()
            .setIconResId(R.drawable.ic_favorite_border)
            .setSessionCommand(sessionCommand())
            .setDisplayName("Like")
            .setEnabled(true)
            .build()
    }

    override fun onCustomCommand(session: MediaSession, controller: MediaSession.ControllerInfo){
        //TODO add something here if we want to persist like state
    }
}