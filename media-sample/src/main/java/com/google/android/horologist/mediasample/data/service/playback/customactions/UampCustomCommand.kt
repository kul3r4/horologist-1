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

import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import com.google.android.horologist.mediasample.data.service.playback.customactions.UampCustomActionHandler.UampCommands.LIKE
import com.google.android.horologist.mediasample.data.service.playback.customactions.UampCustomActionHandler.UampCommands.UNLIKE

public interface UampCustomCommand {
    fun customCommand(): String
    fun commandButton(): CommandButton
    fun onCustomCommand(session: MediaSession, controller: MediaSession.ControllerInfo)
    fun sessionCommand(): SessionCommand
}

public class UampCustomActionHandler {

    private val customLayoutMap = mutableMapOf<String, UampCustomCommand>()
    public val customLayout: List<UampCustomCommand> get() = customLayoutMap.values.toList()
    public val customCommands = getAvailableCustomCommands()

    private fun getAvailableCustomCommands() = mapOf(
        LIKE to LikeCommand(),
        UNLIKE to UnlikeCustomCommand()
    )

    init {
        loadCustomLayout()
    }

    private fun loadCustomLayout() = customLayoutMap.run {
        this[LIKE] = customCommands.getValue(LIKE)
    }

    public fun setLikeUnlikeCommand(action: String) {
        if (LIKE == action) {
            customLayoutMap.run {
                this[UNLIKE] = customCommands.getValue(UNLIKE)
                this.remove(LIKE)
            }
        }
        else if (UNLIKE == action) {
            customLayoutMap.run {
                this[LIKE] = customCommands.getValue(LIKE)
                this.remove(UNLIKE)
            }
        }
    }

    public fun getButtonLayout() : List<CommandButton> {
        var commandButtons = mutableListOf<CommandButton>()
        customLayout.forEach{
            commandButtons.add(it.commandButton())
        }
        return commandButtons
    }

    object UampCommands {
        const val LIKE = "Like"
        const val UNLIKE = "Unlike"
    }
}