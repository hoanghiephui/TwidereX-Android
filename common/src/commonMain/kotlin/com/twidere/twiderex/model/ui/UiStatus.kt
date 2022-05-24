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
import com.twidere.twiderex.dataprovider.mapper.autolink
import com.twidere.twiderex.model.MicroBlogKey
import com.twidere.twiderex.model.enums.MastodonStatusType
import com.twidere.twiderex.model.enums.PlatformType
import com.twidere.twiderex.model.enums.ReferenceType
import com.twidere.twiderex.model.ui.mastodon.MastodonStatusExtra
import com.twidere.twiderex.model.ui.twitter.TwitterStatusExtra

@Immutable
data class UiStatus(
    val statusId: String,
    val statusKey: MicroBlogKey,
    val htmlText: String,
    val rawText: String,
    val timestamp: Long,
    val metrics: StatusMetrics,
    val sensitive: Boolean,
    val retweeted: Boolean,
    val liked: Boolean,
    val geo: UiGeo,
    val hasMedia: Boolean,
    val user: UiUser,
    val media: List<UiMedia>,
    val source: String,
    val isGap: Boolean,
    val url: List<UiUrlEntity>,
    val platformType: PlatformType,
    val spoilerText: String? = null,
    val card: UiCard? = null,
    val poll: UiPoll? = null,
    val referenceStatus: Map<ReferenceType, UiStatus> = emptyMap(),
    val inReplyToUserId: String? = null,
    val inReplyToStatusId: String? = null,
    val extra: StatusExtra? = null
) {
    val mastodonExtra: MastodonStatusExtra? = if (extra is MastodonStatusExtra) extra else null

    val twitterExtra: TwitterStatusExtra? = if (extra is TwitterStatusExtra) extra else null

    val retweet: UiStatus? by lazy {
        if (platformType == PlatformType.Mastodon && mastodonExtra != null && mastodonExtra.type != MastodonStatusType.Status) {
            referenceStatus[ReferenceType.MastodonNotification]
        } else {
            referenceStatus[ReferenceType.Retweet]?.copy(
                referenceStatus = referenceStatus.filterNot { it.key == ReferenceType.Retweet }
            )
        }
    }

    val quote: UiStatus? by lazy {
        referenceStatus[ReferenceType.Quote]
    }

    fun isInThread(detailStatusId: String? = null): Boolean {
        return if (detailStatusId == null) {
            // show all reply as thread
            inReplyToStatusId != null
        } else {
            // in detail scene only show thread when reply to other status
            // or reply to self
            inReplyToStatusId != detailStatusId || inReplyToUserId == user.id
        }
    }

    fun generateShareLink() = "https://${statusKey.host}" + when (platformType) {
        PlatformType.Twitter -> "/${user.screenName}/status/$statusId"
        PlatformType.StatusNet -> TODO()
        PlatformType.Fanfou -> TODO()
        PlatformType.Mastodon -> "/web/statuses/$statusId"
    }

    companion object {
        @Composable
        fun sample() = UiStatus(
            statusId = "",
            htmlText = autolink.autoLink("Multi-wallet for #Bitcoin, #Ethereum, #Binance Smart Chain and other emerging blockchains. Non-custodial storage, decentralized exchange, and extensive analytics for thousands of tokens and #NFTs \n\n\uD83D\uDC49 Download it here: https://bit.ly/3orSiko"),
            timestamp = System.currentTimeMillis(),
            metrics = StatusMetrics(
                retweet = 189200,
                like = 89000,
                reply = 290300,
            ),
            retweeted = false,
            liked = false,
            geo = UiGeo(""),
            hasMedia = true,
            user = UiUser.sample(),
            media = UiMedia.sample(),
            source = "TwidereX",
            isGap = false,
            url = emptyList(),
            statusKey = MicroBlogKey.CoinHub,
            rawText = "",
            platformType = PlatformType.Twitter,
            sensitive = false
        )

        @Composable
        fun sampleBitcoin() = UiStatus(
            statusId = "",
            htmlText = autolink.autoLink("A secure and decentralized #Bitcoin and other cryptocurrency wallet for Android phones. Supports #Bitcoin, #Ethereum, #EOS, #Binance Chain, Bitcoin Cash, #DASH. Non-custodial storage, decentralized exchange, and extensive analytics for thousands of tokens and #NFTs \n\n\uD83D\uDC49 Download it here: https://bit.ly/3orSiko"),
            timestamp = System.currentTimeMillis(),
            metrics = StatusMetrics(
                retweet = 199200,
                like = 89000,
                reply = 390300,
            ),
            retweeted = false,
            liked = false,
            geo = UiGeo(""),
            hasMedia = true,
            user = UiUser.sampleBitcoin(),
            media = UiMedia.sampleBitcoin(),
            source = "TwidereX",
            isGap = false,
            url = emptyList(),
            statusKey = MicroBlogKey.BitcoinWallet,
            rawText = "",
            platformType = PlatformType.Twitter,
            sensitive = false
        )
    }
}

interface StatusExtra

data class StatusMetrics(
    val like: Long,
    val reply: Long,
    val retweet: Long
)
