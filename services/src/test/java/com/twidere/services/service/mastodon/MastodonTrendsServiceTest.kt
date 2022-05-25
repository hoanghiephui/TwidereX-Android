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
package com.twidere.services.service.mastodon

import com.twidere.services.api.common.mockMastodonService
import com.twidere.services.microblog.TrendService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MastodonTrendsServiceTest {
    private lateinit var trendsService: TrendService

    @BeforeAll
    fun setUp() {
        trendsService = mockMastodonService()
    }

    @Test
    fun trends(): Unit = runBlocking {
        val result = trendsService.trends("1")
        assert(result.isNotEmpty())
    }
}
