package com.pullups.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tappx.TrackInstall;

/**
 * Class:  MyBroadCastReciever
 * But:    Analyser le nombre d'utlisateur qui intalle un applicattion
 *          grace à Tappx
 * Created by Thomas on 10.04.2016.
 */
public class MyBroadCastReciever extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null){
            if(intent.getAction().equals("com.android.vending.INSTALL-REFERRER")){
                //Tappx enregistre les intallations
                try {
                    TrackInstall tappx = new TrackInstall();
                    tappx.onReceive(context, intent);
                }catch (Exception p_ex){
                    p_ex.printStackTrace();
                }
                //Ajouter ici les autres systèmes tracking
            }//if
        }//if
    }//onRecieve
}//MyBroadCastReciever
