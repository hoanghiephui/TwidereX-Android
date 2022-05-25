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
package com.twidere.services.api.mastodon

import com.twidere.services.api.common.mockRetrofit
import com.twidere.services.mastodon.api.TrendsResources
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MastodonTrendsApiTest {
    private lateinit var trendsResources: TrendsResources

    @BeforeAll
    fun setUp() {
        trendsResources = mockRetrofit("https://test.mastodon.com/", MastodonRequest2AssetPathConvertor())
    }

    @Test
    fun trends_listIsNotEmpty(): Unit = runBlocking {
        val result = trendsResources.trends()
        assert(result.isNotEmpty())
    }
}
