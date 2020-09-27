package com.twidere.twiderex.repository.timeline

import com.twidere.services.microblog.model.IStatus
import com.twidere.twiderex.db.AppDatabase
import com.twidere.twiderex.db.mapper.toDbTimeline
import com.twidere.twiderex.db.model.DbTimeline
import com.twidere.twiderex.db.model.DbTimelineWithStatus
import com.twidere.twiderex.db.model.TimelineType
import com.twidere.twiderex.model.UserKey


abstract class TimelineRepository(
    private val userKey: UserKey,
    private val database: AppDatabase,
    private val count: Int = 20,
) {
    protected abstract val type: TimelineType

    val liveData by lazy {
        database.timelineDao().getAllWithLiveData(userKey, type)
    }

    suspend fun refresh(since_id: String?): List<DbTimelineWithStatus> {
        return loadBetween(since_id = since_id)
    }

    suspend fun loadBetween(
        max_id: String? = null,
        since_id: String? = null,
        withGap: Boolean = true,
    ): List<DbTimelineWithStatus> {
        val result = loadData(count = count, since_id = since_id, max_id = max_id)
        val timeline = result.map { it.toDbTimeline(userKey, type) }
        if (withGap) {
            timeline.lastOrNull()?.timeline?.isGap = result.size >= count
        }
        val data = timeline
            .map { listOf(it.status, it.quote, it.retweet) }
            .flatten()
            .filterNotNull()
        database.mediaDao().insertAll(data.map { it.media }.flatten())
        database.statusDao().insertAll(data.map { it.status })
        database.timelineDao().insertAll(timeline.map { it.timeline })
        return timeline
    }

    suspend fun loadMore(max_id: String): List<DbTimelineWithStatus> {
        return loadBetween(max_id = max_id, withGap = false)
    }

    suspend fun update(timeline: DbTimeline) {
        database.timelineDao().update(timeline)
    }

    protected abstract suspend fun loadData(
        count: Int = 20,
        since_id: String? = null,
        max_id: String? = null,
    ): List<IStatus>
}