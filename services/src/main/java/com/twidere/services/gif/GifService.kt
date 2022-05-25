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
package com.twidere.services.gif

import com.twidere.services.gif.model.GifPaging
import com.twidere.services.microblog.DownloadMediaService

interface GifService : DownloadMediaService {
    suspend fun trending(nextPage: String?, limit: Int = 25): GifPaging

    suspend fun search(query: String, lang: String = "en", nextPage: String?, limit: Int = 25): GifPaging
}
