package com.pullups.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Thomas on 09.07.2015.
 */
public class AdMobActivity extends Activity{

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAd();
        //Log.i("AdMob", "AdMob loaded: " + mInterstitialAd.isLoaded());
    }

    public void loadAd (){
        AdRequest adRequest = new AdRequest.Builder()
     //         .addTestDevice(AdRequest.DEVICE_ID_EMULATOR
     //         .addTestDevice("TEST_ID")
                .build();
        mInterstitialAd.loadAd(adRequest);
    }


    public void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(getApplicationContext(), "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mInterstitialAd != null){
            mInterstitialAd = null;
        }
    }
}
