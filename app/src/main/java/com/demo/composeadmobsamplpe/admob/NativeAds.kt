package com.demo.composeadmobsamplpe.admob

import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.demo.composeadmobsamplpe.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

@Composable
fun MyNativeAdView(adUnitId: String) {
    val context = LocalContext.current
    val nativeAdFlow = remember { mutableStateOf<NativeAd?>(null) }

    val adLoader = remember {
        AdLoader.Builder(context, adUnitId)
            .forNativeAd { nativeAd ->
                nativeAdFlow.value = nativeAd
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    nativeAdFlow.value = null
                    println("AdmobNativeAd onAdFailedToLoad $p0")
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    println("AdmobNativeAd onAdLoaded")
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    println("AdmobNativeAd onAdImpression")
                    // FIXME Here is not being executed
                }
            })
            .build()
    }

    LaunchedEffect(key1 = Unit, block = {
        adLoader.loadAd(AdRequest.Builder().build())
    })

    AndroidView(
        factory = { context ->
            LayoutInflater.from(context)
                .inflate(R.layout.native_ad, null) as NativeAdView
        },
        modifier = Modifier.fillMaxWidth(),
        update = { adView ->
            // Find the view IDs defined in the XML layout file
            val adIconView = adView.findViewById<ImageView>(R.id.ad_pp_icon)
            val adHeadlineView = adView.findViewById<TextView>(R.id.ad_headline)
            val adBodyView = adView.findViewById<TextView>(R.id.ad_description)
            val adCallToActionView = adView.findViewById<Button>(R.id.ad_button)
            val adAdvertiserView = adView.findViewById<TextView>(R.id.ad_price)
            val adMediaView = adView.findViewById<MediaView>(R.id.adMedia)

            // Register the view IDs with the native ad view.
            adView.headlineView = adHeadlineView
            adView.bodyView = adBodyView
            adView.callToActionView = adCallToActionView
            adView.iconView = adIconView
            adView.advertiserView = adAdvertiserView
            adView.mediaView = adMediaView

            // Populate the native ad view.
            val nativeAd = nativeAdFlow.value
            nativeAd?.let {
                adIconView.setImageDrawable(nativeAd.icon?.drawable)
                adHeadlineView.text = nativeAd.headline
                adBodyView.text = nativeAd.body
                adCallToActionView.text = nativeAd.callToAction
                adAdvertiserView.text = nativeAd.advertiser

                adMediaView.mediaContent = nativeAd.mediaContent
                adMediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)

                // Register the native ad view with the native ad object.
                adView.setNativeAd(nativeAd)
            }?.run {

            }
        }
    )
}