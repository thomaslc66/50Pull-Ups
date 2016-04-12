package com.pullups.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.tappx.TAPPXAdInterstitial;

/**
 * Class:   TappxActivity
 * But:     Afficher et promouvoir des applications tiers
 *          Afin de gagner des points et promouvoir 50 pullups
 * Created by Thomas on 10.04.2016.
 */
public class TappxActivity extends Activity{

    private PublisherInterstitialAd tappxAdInterstitial;
    private final String TAPPX_KEY = "/120940746/Pub-9623-Android-3570";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the InterstitialAd and set the adUnitId.
        tappxAdInterstitial = TAPPXAdInterstitial.Configure(getParent(), TAPPX_KEY, new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Toast.makeText(getApplicationContext(),"Ad closed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                Toast.makeText(getApplicationContext(), "Error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.w("TAG", "add Loaded");

            }
        });
    }//onCreate

    public void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (tappxAdInterstitial != null && tappxAdInterstitial.isLoaded()) {
            TAPPXAdInterstitial.Show(tappxAdInterstitial);
        } else {
            Toast.makeText(getApplicationContext(), "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }


}//class TappxActivity
