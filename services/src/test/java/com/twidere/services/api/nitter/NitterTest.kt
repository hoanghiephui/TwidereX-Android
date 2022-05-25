/*
 *  Twitter
 *
 *  Copyright (C) 2021-2022 Living Solutions <living.solutions.vn@gmail.com>
 * 
 *  This file is part of Twitter.
 * 
 *  Twitter is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  Twitter is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Twitter. If not, see <http://www.gnu.org/licenses/>.
 */
package com.twidere.services.api.nitter

import com.twidere.services.nitter.model.ConversationTimeline
import com.twidere.services.nitter.model.UserTimeline
import kotlinx.coroutines.runBlocking
import moe.tlaster.hson.Hson
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.assertNotNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NitterTest {
    @Test
    fun userTest() = runBlocking {
        val file = File("src/test/resources/api/nitter/sample_user.html").readText()
        val statuses = Hson.deserializeKData<UserTimeline>(file)
        assert(statuses.statuses.any())
    }

    @Test
    fun conversationTest(): Unit = runBlocking {
        val file = File("src/test/resources/api/nitter/sample_conversation.html").readText()
        val timeline = Hson.deserializeKData<ConversationTimeline>(file)
        assert(timeline.items.any())
        assertNotNull(timeline.nextPage)
    }
}
