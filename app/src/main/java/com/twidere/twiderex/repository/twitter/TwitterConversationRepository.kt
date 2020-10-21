/*
 *  TwidereX
 *
 *  Copyright (C) 2020 Tlaster <tlaster@outlook.com>
 * 
 *  This file is part of TwidereX.
 * 
 *  TwidereX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  TwidereX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with TwidereX. If not, see <http://www.gnu.org/licenses/>.
 */
package com.twidere.twiderex.repository.twitter

import androidx.lifecycle.map
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.twidere.services.microblog.LookupService
import com.twidere.services.microblog.SearchService
import com.twidere.services.twitter.model.ReferencedTweetType
import com.twidere.services.twitter.model.StatusV2
import com.twidere.services.twitter.model.TwitterSearchResponseV2
import com.twidere.twiderex.db.AppDatabase
import com.twidere.twiderex.db.CacheDatabase
import com.twidere.twiderex.db.mapper.toDbTimeline
import com.twidere.twiderex.db.model.DbTimelineWithStatus
import com.twidere.twiderex.db.model.TimelineType
import com.twidere.twiderex.defaultLoadCount
import com.twidere.twiderex.model.UserKey
import com.twidere.twiderex.model.ui.UiStatus
import com.twidere.twiderex.model.ui.UiStatus.Companion.toUi
import javax.inject.Singleton

data class ConversationResult(
    val result: List<StatusV2>,
    val nextPage: String?,
)

@Singleton
class TwitterConversationRepository @AssistedInject constructor(
    private val database: AppDatabase,
    private val cache: CacheDatabase,
    @Assisted private val userKey: UserKey,
    @Assisted private val searchService: SearchService,
    @Assisted private val lookupService: LookupService,
) {

    @AssistedInject.Factory
    interface AssistedFactory {
        fun create(
            userKey: UserKey,
            searchService: SearchService,
            lookupService: LookupService,
        ): TwitterConversationRepository
    }

    val liveData by lazy {
        cache.timelineDao().getAllWithLiveData(userKey, TimelineType.Conversation).map { list ->
            list.map { status ->
                status.toUi()
            }
        }
    }

    suspend fun loadPrevious(statusV2: StatusV2): List<StatusV2> {
        var current = statusV2
        val list = arrayListOf<StatusV2>()
        while (true) {
            val referencedTweetId = current.referencedTweets
                ?.firstOrNull { it.type == ReferencedTweetType.replied_to }?.id
            if (referencedTweetId == null) {
                break
            } else {
                val result = lookupService.lookupStatus(referencedTweetId) as StatusV2
                val db = result.toDbTimeline(userKey, TimelineType.Conversation)
                saveData(listOf(db))
                list.add(result)
                current = result
            }
        }
        return list.reversed()
    }

    suspend fun loadTweet(status: UiStatus): StatusV2 {
        return lookupService.lookupStatus(status.statusId) as StatusV2
    }

    suspend fun toUiStatus(status: StatusV2): UiStatus {
        val db = status.toDbTimeline(userKey, TimelineType.Conversation)
        saveData(listOf(db))
        return db.toUi()
    }

    private fun buildConversation(
        status: StatusV2,
        searchResponse: List<StatusV2>
    ): List<List<StatusV2>> {
        return searchResponse.filter {
            it.referencedTweets?.firstOrNull {
                it.type == ReferencedTweetType.replied_to
            }?.id == status.id
        }
            .map {
                listOf(it) + buildConversation(it, searchResponse).flatten()
            }
    }

    suspend fun loadConversation(tweet: StatusV2, nextPage: String? = null): ConversationResult {
        val conversationId = tweet.conversationID ?: return ConversationResult(emptyList(), null)
        val searchResponse = searchService.searchTweets(
            "conversation_id:$conversationId",
            count = defaultLoadCount,
            nextPage = nextPage
        ) as TwitterSearchResponseV2
        val status = searchResponse.data ?: emptyList()
        val result = buildConversation(tweet, status)
        val db = result.flatten().map { it.toDbTimeline(userKey, TimelineType.Conversation) }
        saveData(db)
        return ConversationResult(result.flatten(), searchResponse.nextPage)
    }

    private suspend fun saveData(timeline: List<DbTimelineWithStatus>) {
        val data = timeline
            .map { listOf(it.status, it.quote, it.retweet) }
            .flatten()
            .filterNotNull()
        data.map { it.user }.let {
            cache.userDao().insertAll(it)
            database.userDao().update(*it.toTypedArray())
        }
        cache.mediaDao().insertAll(data.map { it.media }.flatten())
        data.map { it.status }.let {
            cache.statusDao().insertAll(it)
            database.statusDao().update(*it.toTypedArray())
        }
        timeline.map { it.timeline }.let {
            cache.timelineDao().insertAll(it)
            database.timelineDao().update(*it.toTypedArray())
        }
    }
}
