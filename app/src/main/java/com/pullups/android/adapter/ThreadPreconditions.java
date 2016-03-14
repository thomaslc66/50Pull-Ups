package com.pullups.android.adapter;

import android.os.Looper;

import com.pullups.android.BuildConfig;

/**
 * Created by Thomas on 13.12.2014.
 */
class ThreadPreconditions {
    /*******************************************
     *
     * checkOnMainThread
     * @goal permet de vérifier que la méthode est appellée uniquement depuis
     * le Thread Principal
     *
     *****************************************/
    public static void checkOnMainThread(){
        if(BuildConfig.DEBUG){
            if(Thread.currentThread() != Looper.getMainLooper().getThread()){
                throw new IllegalStateException("This method should be called from the Main Thread");
            }
        }
    }//checkOnMainThread

}
