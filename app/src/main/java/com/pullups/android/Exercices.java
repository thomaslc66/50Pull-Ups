/*
*
* Programm Name: 50 Pull ups
* By:
* First Name: Thomas
* Last Name: Léchaire
* Created on : 19.08.2014.
* Last Modified: 03.10.2014
* Affiche une liste de vidéos pour faire les exercies
*
* */

package com.pullups.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.pullups.android.adapter.ExercicesAdapter;
import com.pullups.android.model.ExerciceObject;

import java.util.ArrayList;

public class Exercices extends Activity {

    private ListView                    exoListView;
    private ExercicesAdapter            exercicesAdapter;
    private ArrayList<ExerciceObject>   exoObjList;
    private ExerciceObject              objExercice;
    //object declaration
    private TappxInterAd                tappxInterAd;
    private Handler                     handlertappxInterAd;
    private boolean                     tappxInterAdIsCanceled = false;
    private PublisherInterstitialAd     adInterstitial      = null;
    private int                         ad;

    private ExerciceAdapter             exerciceAdapter;
    private Handler                     handlerexerciceAdapter;
    private boolean                     exerciceAdapterisCanceled   = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercices);

        ad         = 0;
        exoObjList = new ArrayList<>();

        String[] nomDesExercices
                = {getString(R.string.maj_negatives)   ,
                   getString(R.string.maj_chin_ups)    ,
                   getString(R.string.maj_wide_pull_up),
                    getString(R.string.maj_military_pullup)};
        String[] musclesTravailles
                = { getString(R.string.muscles_negatives),
                    getString(R.string.muscles_chin_ups) ,
                    getString(R.string.muscle_wide)  ,
                    getString(R.string.muscles_military)};
        String[] niveauTraction
                = { getString(R.string.niveau_debutant)      ,
                    getString(R.string.niveau_standard)      ,
                    getString(R.string.niveau_avance)        ,
                    getString(R.string.niveau_intermediaire) };
        String[] codeVideo
                = { "CgE_UY7wW1w" ,
                    "yhyJ0tBcgJE" ,
                    "oviw1pJ5M-c" ,
                    "VJ8EhO5kW-w" };

        exoListView = (ListView) findViewById(R.id.exoListView);



        for (int i = 0; i < nomDesExercices.length; i++){
            //on créée les objets pour la liste des vidéos.
            objExercice = new ExerciceObject(   nomDesExercices[i],
                                                musclesTravailles[i],
                                                niveauTraction[i],
                                                codeVideo[i]);
            //on l'ajoute à la liste
            exoObjList.add(objExercice);
        }

        //Affiche la listView avec l'adapter
        exerciceAdapter                = new ExerciceAdapter();
        handlerexerciceAdapter         = new Handler();
        handlerexerciceAdapter.post(exerciceAdapter);

    }
    /********************************************************************
     *
     * onBackPressed = gère l'appui sur la touche retour
     * @param *
     *
     *
     ********************************************************************/
    @Override
    protected void onResume() {
        super.onResume();
        //Récupère les pubs
        tappxInterAd                = new TappxInterAd();
        handlertappxInterAd         = new Handler();
        handlertappxInterAd.post(tappxInterAd);
    }

    /********************************************************************
     *
     * onBackPressed = gère l'appui sur la touche retour
     * @param *
     *
     *
     ********************************************************************/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //on est va vers l'accueil alors on tue l'activité
        Intent accueil = new Intent(Exercices.this, ActiviteAccueil.class);
        accueil.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(accueil);
        finish();
    }

    /****************************************************************
     *
     * Runnable to get Interstitial from tappX
     *
     * ***************************************************************/
    private class TappxInterAd implements Runnable{

        @Override
        public void run() {
            if(tappxInterAdIsCanceled){
                tappxInterAd = null;
            }else{
                if(ad == 0) {
                    final String TAPPX_KEY = "/120940746/Pub-9623-Android-3570";
                    adInterstitial = com.tappx.TAPPXAdInterstitial.Configure(Exercices.this, TAPPX_KEY,
                        new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                ad++;
                            }

                            @Override
                            public void onAdLoaded() {
                                com.tappx.TAPPXAdInterstitial.Show(adInterstitial);
                            }
                        });
                }
                killRunnable();
            }//else
        }//run

        public void killRunnable(){
            tappxInterAdIsCanceled = true;
        }//killRunnable
    }//class interne prefsRunnable


    /****************************************************************
     *
     * Runnable to set Adapter for the View
     *
     * ***************************************************************/
    private class ExerciceAdapter implements Runnable{

        @Override
        public void run() {
            if(exerciceAdapterisCanceled){
                tappxInterAd = null;
            }else{
                exercicesAdapter = new ExercicesAdapter(Exercices.this, exoObjList);
                exoListView.setAdapter(exercicesAdapter);
                killRunnable();
            }//else
        }//run

        public void killRunnable(){
            exerciceAdapterisCanceled = true;
        }//killRunnable
    }//class interne prefsRunnable

}


