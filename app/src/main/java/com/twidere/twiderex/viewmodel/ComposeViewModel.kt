/*
 *  Twidere X
 *
 *  Copyright (C) 2020 Tlaster <tlaster@outlook.com>
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
package com.twidere.twiderex.viewmodel

import android.Manifest.permission
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.annotation.RequiresPermission
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.twidere.services.microblog.LookupService
import com.twidere.services.twitter.TwitterService
import com.twidere.twiderex.di.assisted.IAssistedFactory
import com.twidere.twiderex.extensions.getCachedLocation
import com.twidere.twiderex.model.AccountDetails
import com.twidere.twiderex.model.ui.UiStatus
import com.twidere.twiderex.repository.twitter.TwitterTweetsRepository
import com.twidere.twiderex.scenes.ComposeType
import com.twidere.twiderex.utils.ComposeQueue

class ComposeViewModel @AssistedInject constructor(
    private val locationManager: LocationManager,
    private val composeQueue: ComposeQueue,
    private val factory: TwitterTweetsRepository.AssistedFactory,
    @Assisted private val account: AccountDetails,
    @Assisted private val statusId: String?,
) : ViewModel(), LocationListener {
    @AssistedInject.Factory
    interface AssistedFactory : IAssistedFactory {
        fun create(account: AccountDetails, statusId: String?): ComposeViewModel
    }

    private val service by lazy {
        account.service as TwitterService
    }
    private val repository by lazy {
        factory.create(
            account.key,
            account.service as LookupService,
        )
    }
    val images = MutableLiveData<List<Uri>>(emptyList())
    val location = MutableLiveData<Location>()
    val locationEnabled = MutableLiveData(false)
    val status = liveData {
        statusId?.let {
            emitSource(repository.loadTweetFromCache(it))
        } ?: run {
            emit(null)
        }
    }

    fun compose(content: String, composeType: ComposeType, status: UiStatus? = null) {
        composeQueue.commit(
            service,
            content,
            images = images.value ?: emptyList(),
            replyTo = if (composeType == ComposeType.Reply) status?.statusId else null,
            quoteTo = if (composeType == ComposeType.Quote) status?.statusId else null,
        )
    }

    fun putImages(value: List<Uri>) {
        images.value?.let {
            value + it
        }?.let {
            it.take(4)
        }?.let {
            images.postValue(it)
        }
    }

    @RequiresPermission(anyOf = [permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION])
    fun trackingLocation() {
        locationEnabled.postValue(true)
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        val provider = locationManager.getBestProvider(criteria, true) ?: return
        locationManager.requestLocationUpdates(provider, 0, 0f, this)
        locationManager.getCachedLocation()?.let {
            location.postValue(it)
        }
    }

    fun disableLocation() {
        locationEnabled.postValue(false)
        locationManager.removeUpdates(this)
    }

    override fun onLocationChanged(location: Location) {
        this.location.postValue(location)
    }

    override fun onCleared() {
        if (locationEnabled.value == true) {
            locationManager.removeUpdates(this)
        }
    }

    fun removeImage(item: Uri) {
        images.value?.let {
            it - item
        }?.let {
            images.postValue(it)
        }
    }
}