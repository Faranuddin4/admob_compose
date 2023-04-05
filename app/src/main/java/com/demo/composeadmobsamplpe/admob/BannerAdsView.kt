package com.demo.composeadmobsamplpe.admob

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.demo.composeadmobsamplpe.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AndroidAdView(context: Context,modifier: Modifier) {

    val adView = remember {
        val adRequest: AdRequest = AdRequest.Builder().build()
        AdView(context).apply {
            // init test ads
            setAdSize(AdSize.BANNER)
            adUnitId = resources.getString(R.string.test_adbanner_id)
            loadAd(adRequest)
        }
    }

    AndroidView(
        { adView }, modifier = modifier
    )
}

@Composable
fun AndroidAdaptiveView() {
    val adWidth = LocalConfiguration.current.screenWidthDp
    val context = LocalContext.current
    AndroidView(
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    context,
                    adWidth
                ))
                adUnitId = context.getString(R.string.test_adbanner_id)
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

@Preview
@Composable
fun laodAds() {
    AndroidAdView(context = LocalContext.current,Modifier)
}