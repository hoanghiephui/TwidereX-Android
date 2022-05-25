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
package com.twidere.services.twitter.model

import com.twidere.services.microblog.model.ISearchResponse
import com.twidere.services.microblog.model.IStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TwitterSearchResponseV1(
    val statuses: List<Status>? = null,

    @SerialName("search_metadata")
    val searchMetadata: SearchMetadataV1? = null
) : ISearchResponse {
    override val nextPage: String?
        get() = searchMetadata?.nextResults?.split("&")?.firstOrNull { it.contains("max_id=") }?.let {
            it.substring(it.indexOf("max_id=") + "max_id=".length)
        }
    override val status: List<IStatus>
        get() = statuses ?: emptyList()
}
