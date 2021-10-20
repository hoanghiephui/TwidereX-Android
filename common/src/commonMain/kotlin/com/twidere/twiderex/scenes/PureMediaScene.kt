/*
 *  Twidere X
 *
 *  Copyright (C) 2020-2021 Tlaster <tlaster@outlook.com>
 * 
 *  This file is part of Twidere X.
 * 
 *  Twidere X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  Twidere X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Twidere X. If not, see <http://www.gnu.org/licenses/>.
 */
package com.twidere.twiderex.scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.google.android.exoplayer2.ui.PlayerControlView
import com.twidere.twiderex.component.bottomInsetsPadding
import com.twidere.twiderex.component.foundation.InAppNotificationScaffold
import com.twidere.twiderex.component.foundation.VideoPlayerController
import com.twidere.twiderex.component.painterResource
import com.twidere.twiderex.component.stringResource
import com.twidere.twiderex.component.topInsetsPadding
import com.twidere.twiderex.di.ext.getViewModel
import com.twidere.twiderex.extensions.observeAsState
import com.twidere.twiderex.kmp.LocalPlatformWindow
import com.twidere.twiderex.model.MicroBlogKey
import com.twidere.twiderex.preferences.LocalDisplayPreferences
import com.twidere.twiderex.preferences.model.DisplayPreferences
import com.twidere.twiderex.ui.LocalNavController
import com.twidere.twiderex.ui.LocalVideoPlayback
import com.twidere.twiderex.ui.TwidereDialog
import com.twidere.twiderex.viewmodel.PureMediaViewModel
import moe.tlaster.swiper.SwiperState
import moe.tlaster.swiper.rememberSwiperState
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PureMediaScene(belongToKey: MicroBlogKey, selectedIndex: Int) {
    val viewModel = getViewModel<PureMediaViewModel> {
        parametersOf(belongToKey)
    }
    val source by viewModel.source.observeAsState(null)
    TwidereDialog(
        requireDarkTheme = true,
        extendViewIntoStatusBar = true,
        extendViewIntoNavigationBar = true,
    ) {
        source?.let { medias ->
            CompositionLocalProvider(
                LocalVideoPlayback provides DisplayPreferences.AutoPlayback.Always
            ) {
                val window = LocalPlatformWindow.current
                var controlVisibility by remember { mutableStateOf(true) }
                val controlPanelColor = MaterialTheme.colors.surface.copy(alpha = 0.6f)
                val navController = LocalNavController.current
                val pagerState = rememberPagerState(
                    initialPage = selectedIndex,
                    pageCount = medias.size,
                )
                // val currentMedia = medias[pagerState.currentPage]
                // val context = LocalContext.current
                // todo use redefine custom control view by compose
                val videoControl = null
                //     remember(pagerState.currentPage) {
                //     if (currentMedia.type == MediaType.video) {
                //         PlayerControlView(context).apply {
                //             showTimeoutMs = 0
                //         }
                //     } else {
                //         null
                //     }
                // }
                val swiperState = rememberSwiperState(
                    onDismiss = {
                        navController.popBackStack()
                    },
                )
                val display = LocalDisplayPreferences.current
                var isMute by remember {
                    mutableStateOf(display.muteByDefault)
                }
                InAppNotificationScaffold(
                    backgroundColor = Color.Transparent,
                    contentColor = contentColorFor(backgroundColor = MaterialTheme.colors.background),
                    bottomBar = {
                        PureMediaBottomInfo(
                            controlVisibility = controlVisibility,
                            swiperState = swiperState,
                            controlPanelColor = controlPanelColor,
                            videoControl = videoControl,
                            mute = isMute,
                            onMute = { isMute = it }
                        )
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    if (controlVisibility) {
                                        window.hideControls()
                                    } else {
                                        window.showControls()
                                    }
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ),
                    ) {
                        MediaView(
                            media = medias.mapNotNull {
                                it.mediaUrl?.let { it1 ->
                                    MediaData(
                                        it1,
                                        it.type
                                    )
                                }
                            },
                            swiperState = swiperState,
                            customControl = videoControl,
                            pagerState = pagerState,
                            volume = if (isMute) 0f else 1f
                        )
                        val windowBarVisibility by window.windowBarVisibility.observeAsState(true)
                        LaunchedEffect(windowBarVisibility) {
                            controlVisibility = windowBarVisibility
                        }
                        DisposableEffect(Unit) {
                            onDispose {
                                window.showControls()
                            }
                        }
                        PureMediaControlPanel(
                            controlVisibility = controlVisibility,
                            swiperState = swiperState,
                            controlPanelColor = controlPanelColor,
                            onPopBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PureMediaBottomInfo(
    controlVisibility: Boolean,
    swiperState: SwiperState,
    controlPanelColor: Color,
    videoControl: PlayerControlView?,
    mute: Boolean,
    onMute: (Boolean) -> Unit
) {
    AnimatedVisibility(
        visible = controlVisibility && swiperState.progress == 0f,
        enter = fadeIn() + expandVertically(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = controlPanelColor)
                .padding(PureMediaSceneDefaults.ContentPadding)
                .bottomInsetsPadding(),
        ) {
            if (videoControl != null) {
                VideoPlayerController(
                    videoControl = videoControl,
                    mute = mute,
                    onMute = onMute
                )
            }
        }
    }
}

private object PureMediaSceneDefaults {
    val ContentPadding = PaddingValues(8.dp)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PureMediaControlPanel(
    controlVisibility: Boolean,
    swiperState: SwiperState,
    controlPanelColor: Color,
    onPopBack: () -> Unit
) {
    AnimatedVisibility(
        visible = controlVisibility && swiperState.progress == 0f,
        enter = fadeIn() + expandVertically(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .topInsetsPadding()
                .padding(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        color = controlPanelColor,
                        shape = MaterialTheme.shapes.small
                    )
                    .clipToBounds()
            ) {
                IconButton(
                    onClick = {
                        onPopBack.invoke()
                    }
                ) {
                    Icon(
                        painter = painterResource(res = com.twidere.twiderex.MR.files.ic_x),
                        contentDescription = stringResource(
                            res = com.twidere.twiderex.MR.strings.accessibility_common_close
                        )
                    )
                }
            }
        }
    }
}
