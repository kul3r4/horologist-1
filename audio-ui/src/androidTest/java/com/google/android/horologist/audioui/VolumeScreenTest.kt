/*
 * Copyright 2022 The Android Open Source Project
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

@file:OptIn(ExperimentalTestApi::class)

package com.google.android.horologist.audioui

import TestHaptics
import android.os.Vibrator
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import org.junit.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performRotaryScrollInput
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.test.junit4.createComposeRule
import com.google.android.horologist.audio.SystemAudioOutputRepository
import com.google.android.horologist.audio.SystemVolumeRepository
import com.google.android.horologist.audio.VolumeState
import com.google.android.horologist.audioui.devices.RotaryInput
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule

/*
 * Copyright 2022 The Android Open Source Project
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

class VolumeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testHaptics() = runTest {

        val volumeRepository = FakeVolumeRepository(VolumeState(50,0,100,false))
        val audioOutputRepository = FakeAudioOutputRepository()

        val state = VolumeScrollableState(volumeRepository, audioOutputRepository, )
        val focusRequester = FocusRequester()

        val haptics = TestHaptics()
        val rotaryInput = RotaryInput.Emulator

        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalHaptics provides haptics,
                LocalRotaryInput provides rotaryInput
            ) {
                VolumeScreen(
                    modifier = Modifier
                        .fillMaxSize(),
                )
            }
        }

        composeTestRule.runOnIdle {
            focusRequester.requestFocus()
        }


        composeTestRule.onRoot().performRotaryScrollInput {
            rotateToScrollVertically(1.25f * rotaryInput.rotaryPixelsForVolume)
        }
        composeTestRule.waitForIdle()

        composeTestRule.onRoot().performRotaryScrollInput {
            rotateToScrollVertically(1.25f * rotaryInput.rotaryPixelsForVolume)
        }
        composeTestRule.waitForIdle()

        assertThat(haptics.hapticEvents).containsExactly("performScrollTick", "performScrollTick")
        assertThat(volumeRepository.volumeState.current state).isEqualsTo(52)
    }
}