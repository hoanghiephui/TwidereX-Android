/*
 *  Twidere X
 *
 *  Copyright (C) TwidereProject and Contributors
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
package com.twidere.twiderex.model.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.twidere.twiderex.model.MicroBlogKey
import com.twidere.twiderex.model.enums.PlatformType
import com.twidere.twiderex.model.ui.mastodon.MastodonUserExtra
import com.twidere.twiderex.model.ui.twitter.TwitterUserExtra

@Immutable
data class UiUser(
    val id: String,
    val userKey: MicroBlogKey,
    val acct: MicroBlogKey,
    val name: String,
    val screenName: String,
    val profileImage: String,
    val profileBackgroundImage: String?,
    val metrics: UserMetrics,
    val rawDesc: String,
    val htmlDesc: String,
    val website: String?,
    val location: String?,
    val verified: Boolean,
    val protected: Boolean,
    val platformType: PlatformType,
    val extra: UserExtra? = null
) {
    val displayName
        get() = name.takeUnless { it.isEmpty() } ?: screenName

    fun getDisplayScreenName(host: String?): String {
        return if (host != null && host != acct.host) {
            "@$screenName@${acct.host}"
        } else {
            "@$screenName"
        }
    }
    val twitterExtra: TwitterUserExtra? = if (extra is TwitterUserExtra) extra else null

    val mastodonExtra: MastodonUserExtra? = if (extra is MastodonUserExtra) extra else null

    companion object {
        @Composable
        fun sample() = UiUser(
            id = "",
            name = "Bitcoin Wallet: NFT Market",
            screenName = "Bitcoin",
            profileImage = "https://play-lh.googleusercontent.com/rXqo1MqW_4Tf8-7WIkp6XuGRBZKD2WSspzezBpbg6o9tRn8XCEAOwLfTBhytQl0cAOl1=s360", // painterResource(res = com.twidere.twiderex.MR.files.ic_profile_image_twidere),
            profileBackgroundImage = null,
            metrics = UserMetrics(
                fans = 0,
                follow = 0,
                status = 0,
                listed = 0
            ),
            rawDesc = "",
            htmlDesc = "",
            website = null,
            location = null,
            verified = false,
            protected = false,
            userKey = MicroBlogKey.Empty,
            platformType = PlatformType.Twitter,
            acct = MicroBlogKey.twitter("TwitterProject")
        )

        @Composable
        fun sampleBitcoin() = UiUser(
            id = "",
            name = "Bitcoin Wallet: Blockchain NFT",
            screenName = "BitcoinWallet",
            profileImage = "https://play-lh.googleusercontent.com/p_Y1r5qB0_yGy4oekvlSBrfUvfsC6xSwyP4RYIL7GUu_EtkiVWTYKEmTEk8LAba6lA=s360", // painterResource(res = com.twidere.twiderex.MR.files.ic_profile_image_twidere),
            profileBackgroundImage = null,
            metrics = UserMetrics(
                fans = 0,
                follow = 0,
                status = 0,
                listed = 0
            ),
            rawDesc = "",
            htmlDesc = "",
            website = null,
            location = null,
            verified = false,
            protected = false,
            userKey = MicroBlogKey.Empty,
            platformType = PlatformType.Twitter,
            acct = MicroBlogKey.twitter("TwitterProject")
        )
    }
}

interface UserExtra

data class UserMetrics(
    val fans: Long,
    val follow: Long,
    val status: Long,
    val listed: Long
)
