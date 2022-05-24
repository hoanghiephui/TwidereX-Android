package com.ads

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView

private var nativeAd: MaxAd? = null

@Composable
fun NativeAdLoader(
    adUnitId: String,
    content: (MaxNativeAdView?) -> Unit
) {
    val context = LocalContext.current
    val nativeAdLoader = MaxNativeAdLoader(adUnitId, context)
    nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {
        override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd?) {
            super.onNativeAdLoaded(nativeAdView, ad)
            if (nativeAd != null) {
                nativeAdLoader.destroy(nativeAd)
            }

            // Save ad for cleanup.
            nativeAd = ad
            content(nativeAdView)
        }

        override fun onNativeAdLoadFailed(p0: String?, p1: MaxError?) {
            super.onNativeAdLoadFailed(p0, p1)
            content(null)
        }
    })
}