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
import com.twidere.services.microblog.ListsService
import com.twidere.services.service.ListServiceTest

class MastodonListServiceTest : ListServiceTest() {
    override fun createService(): ListsService {
        return mockMastodonService()
    }

    override fun testListId(): String {
        return "13585"
    }

    override fun testUserId(): String {
        return "23634"
    }
}
