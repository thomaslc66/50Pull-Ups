package com.pullups.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.pullups.android.Realm.CreationBaseDeDonnees;
import com.pullups.android.Realm.JourEntrainementDB;

public class SplashScreen extends Activity{
    private final   int         ZERO                    = 0;
    private final   String      str_nomDesPreferences   = "preferences";
    private final   String      str_baseDeDonnee        = "dbExists";
    private         Handler     handler;
    private         boolean     bln_laDbExiste;
    Runnable                    splashScreenRunnable;
    Runnable                    dataBaserunnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //variables locales
        final int SPLASH_SCREEN_TIMER = 1500;
        splashScreenRunnable    = new splashScreenRunnable();
        dataBaserunnable        = new dataBaseConstruction();

        //check id dbExists is into the preferences if not set value to false
        bln_laDbExiste = getSharedPreferences(str_nomDesPreferences, ZERO)
                            .getBoolean(str_baseDeDonnee, false);

        handler = new Handler();
        handler.post        (dataBaserunnable);
        handler.postDelayed (splashScreenRunnable, SPLASH_SCREEN_TIMER);

        splashScreenRunnable    = null;
        dataBaserunnable        = null;
    }


    /*......................Class: splashScreenRunnable ............................*/
    private class splashScreenRunnable implements Runnable{

        @Override
        public void run() {
            Intent ActiviteAccueil = new Intent(getApplicationContext(), ActiviteAccueil.class);
            startActivity(ActiviteAccueil);
            finish();
        }
    }//splashScreenRunnable Class

    /*......................Class: dataBAseConstruction............................*/
    private class dataBaseConstruction implements Runnable {
        @Override
        public void run() {

            if(!bln_laDbExiste){
                //creation et remplissage de la base de donnees
                CreationBaseDeDonnees baseDeDonnees = new CreationBaseDeDonnees ();
                JourEntrainementDB    trainingDayDb = new JourEntrainementDB    (getApplicationContext());
                trainingDayDb.remplirLaBaseDeDonnees(baseDeDonnees.getTrainingList());


                //apply the new value "true" of the dbExist Parameters
                SharedPreferences.Editor editor =
                        getSharedPreferences(str_nomDesPreferences,
                                getApplicationContext().MODE_PRIVATE).edit();
                editor.putBoolean(str_baseDeDonnee, true);
                editor.apply();

                trainingDayDb.closeRealm();
                baseDeDonnees = null;
            }
        }//run
    }

    @Override
    protected void onPause() {
        super.onPause();

        handler.removeCallbacksAndMessages(dataBaserunnable);
        handler.removeCallbacksAndMessages(splashScreenRunnable);
        dataBaserunnable      = null;
        splashScreenRunnable  = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler = null;
    }
}//splashScreen Activity

