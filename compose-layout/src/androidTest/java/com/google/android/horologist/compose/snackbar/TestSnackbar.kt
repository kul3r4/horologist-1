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

package com.google.android.horologist.compose.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Confirmation
import com.google.android.horologist.compose.navscaffold.ExperimentalComposeLayoutApi

@ExperimentalComposeLayoutApi
@Composable
public fun TestSnackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier,
) {
    val duration = data.duration.toMillis(data.actionLabel != null, null)
    Confirmation(
        modifier = modifier,
        onTimeout = { data.dismiss() },
        durationMillis = duration
    ) {
        Text(
            modifier = Modifier.align(CenterHorizontally),
            text = data.message,
            style = MaterialTheme.typography.display3
        )
        Button(
            modifier = Modifier.align(CenterHorizontally),
            onClick = { data.performAction() }
        ) {
            Text(text = "press")
        }
        Button(
            modifier = Modifier.align(CenterHorizontally),
            onClick = { data.dismiss() }
        ) {
            Text(text = "dismiss")
        }
    }
}
