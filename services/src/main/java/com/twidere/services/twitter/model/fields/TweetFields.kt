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
package com.twidere.services.twitter.model.fields

enum class TweetFields(val value: String) {
    attachments("attachments"),
    author_id("author_id"),
    context_annotations("context_annotations"),
    conversation_id("conversation_id"),
    created_at("created_at"),
    entities("entities"),
    geo("geo"),
    id("id"),
    in_reply_to_user_id("in_reply_to_user_id"),
    lang("lang"),
    public_metrics("public_metrics"),
    possibly_sensitive("possibly_sensitive"),
    referenced_tweets("referenced_tweets"),
    source("source"),
    text("text"),
    withheld("withheld"),
    reply_settings("reply_settings")
}
