/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tomaschlapek.nba.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.tomaschlapek.nba.core.ui.BallersTheme
import com.tomaschlapek.nba.core.ui.components.BallersToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            val currentDestination by navController.appCurrentDestinationAsState()

            /**
             * This is a workaround for the fact that the Navigation component doesn't
             * provide a way to know if the back stack is empty or not.
             */
            val (canPop, setCanPop) = remember { mutableStateOf(false) }
            navController.addOnDestinationChangedListener { controller, _, _ ->
                setCanPop(controller.previousBackStackEntry != null)
            }

            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass

            BallersTheme {
                Scaffold(
                    topBar = {
                        BallersToolbar(
                            titleRes = currentDestination?.title,
                            canPop = canPop,
                            onNavigationClick = { navController.navigateUp() }
                        )
                    }) { contentPadding ->

                    CompositionLocalProvider(LocalWindowWidthSize provides widthSizeClass) {
                        Box(modifier = Modifier.padding(contentPadding)) {
                            DestinationsNavHost(
                                navGraph = NavGraphs.root,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

val LocalWindowWidthSize = compositionLocalOf { WindowWidthSizeClass.Compact }
