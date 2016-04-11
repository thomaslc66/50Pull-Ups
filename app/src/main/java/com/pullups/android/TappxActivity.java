package com.pullups.android;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.tappx.TAPPXAdInterstitial;

/**
 * Class:   TappxActivity
 * But:     Afficher et promouvoir des applications tiers
 *          Afin de gagner des points et promouvoir 50 pullups
 * Created by Thomas on 10.04.2016.
 */
public class TappxActivity extends Activity{

    private TAPPXAdInterstitial tappxAdInterstitial;
    private PublisherInterstitialAd publisherInterstitialAd = null;
    private final String TAPPX_KEY = getString(R.string.tappx_key);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the InterstitialAd and set the adUnitId.
        publisherInterstitialAd = TAPPXAdInterstitial.Configure(getParent(), TAPPX_KEY);

    }
}//class TappxActivity
