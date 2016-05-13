/*
*
* Programm Name: 50 Pull ups
* By:
* First Name: Thomas
* Last Name: Léchaire
* Created on : 19.08.2014.
* Last Modified: 03.10.2014
* But: Class qui permet de faire partir un compte à rebours, qui correspond au temps de pause entre deux séries
*
* */

package com.pullups.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import java.util.concurrent.TimeUnit;

//import android.util.Log;

public class ActiviteCompteARebours extends AdMobActivity {

    //déclaration des attributs
    private       int       compteurTimer,
                            int_rowTime_display,
                            int_total_time_in_millis;
    private       int       compteur_ad;
    private       int       ad;
    private       int       pressed                  = 0;
    private final int       PERCENT                  = 120000;
    private final int       INT_MILLIS               = 1000;
    private final int       INTERVALLE               = 100;
    private final int       AJOUTE_30_SEC            = 30000;
    private       boolean   doubleBackExit;
    private       boolean   bln_addTime              = false;
    private       boolean   tappxInterAdIsCanceled   = false;
    private final String    str_compteurEntrainement = "compteurEntrainement";
    private final String    str_ad                   = "compteur_ad";

    private Handler         handler;
    private TimerRunnable   runnable;
    private Button          btn_passer;
    private TextView        txt_countDown;
    private ProgressBar     progressBar;
    private TappxInterAd    tappxInterAd;
    private Handler         handlertappxInterAd;
    private PublisherInterstitialAd adInterstitial      = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        progressBar   = (ProgressBar)   findViewById(R.id.progressBar);
        btn_passer    = (Button)        findViewById(R.id.btn_entrainement);
        txt_countDown = (TextView)      findViewById(R.id.txtView_countDown);

        int_total_time_in_millis = PERCENT;         //Temps total en mili secondes
        int_rowTime_display      = PERCENT;         //Temps total pour l'affichage

        progressBar.setMax     (PERCENT);           //Valeur max de la progress bar = 2 minutes (120000 ms)
        progressBar.setProgress(PERCENT);           //Fixe la valeur de la progresse bar a 2 minutes

        Intent intent = getIntent();
        compteurTimer = intent.getIntExtra(str_compteurEntrainement, 1);
        compteur_ad   = intent.getIntExtra(str_ad, 0); //pour affiche admob ou tappx

        //Ajoute 30 secondes au timer
        txt_countDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runnable.addTime();                                  //on stop le timer
                handler.removeCallbacks(runnable);        //on annule les message du handler

                int_total_time_in_millis =
                        runnable.getTimeInMillis() + AJOUTE_30_SEC ; //recupere le temps et ajoute 30 sec
                bln_addTime              = false;                    //sinon le timer ne se lance pas
                int_rowTime_display      = int_total_time_in_millis; //pour l'affiche du nouveau temps
                progressBar.setMax(int_total_time_in_millis);        //fixe le max
                progressBar.setProgress(int_total_time_in_millis);   //fixe la valeur de la progressBar
                handler.postDelayed(runnable, INTERVALLE);           //relance le timer
            }
        });

        btn_passer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                    //Action lors du click sur le boutton "passer"
                if (runnable != null || runnable.isRunning()) {
                    runnable.killRunnable();                        //Retour à l'activite precedente
                }
            }
        });

        runnable                 = new TimerRunnable();
        handler                  = new Handler();
        handler.postDelayed(runnable, 100);

    }//onCreate


    @Override
    protected void onResume() {
        super.onResume();

        //Run Timer
        //call handler post delayed to resume the runnable
        //TODO empêcher l'appel au handler s'il existe déjà
        if(!runnable.isRunning()){
            runnable                 = new TimerRunnable();
            handler                  = new Handler();
            handler.postDelayed(runnable, 100);
        }

        //tappx
        if (compteur_ad % 2 != 0) {
            tappxInterAd = new TappxInterAd();
            handlertappxInterAd = new Handler();
            ad = 0;
        }
    }

    /*******************************************************************************************************
     *
     * @ class Task
     * @ new Runnable
     *
     *******************************************************************************************************/
    private class TimerRunnable implements Runnable{

        private final int      INT_SEC             = 60;
        private       boolean  isCanceled          = false;
        private       int      int_progress_value;
        final         Vibrator vibreur             = (Vibrator) getSystemService(VIBRATOR_SERVICE);     //service de vibration

        @Override
        public void run() {

                //if time is added
                if (bln_addTime) {
                    handler.removeCallbacksAndMessages(runnable);
                    runnable = null;

                }//if the timer is canceled or the total time = the progress time
                else if (isCanceled || int_total_time_in_millis == int_progress_value) {
                    //set runnable to null
                    runnable = null;
                    vibreur.vibrate(200);
                    workoutIntent();
                    return;
                } else {
                    int_progress_value  += INTERVALLE;  //on incremente le temps
                    int_rowTime_display -= INTERVALLE; //on met à jour l'affichage
                    progressBar.setSecondaryProgress(int_progress_value); //mise à jour de la progressBar

                    String temps = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(int_rowTime_display),
                            TimeUnit.MILLISECONDS.toSeconds(int_rowTime_display) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(int_rowTime_display)));

                    txt_countDown.setText(temps);       //affiche le temps restant

                    //vibre lors des dernieres secondes
                    if (int_rowTime_display == 300 || int_rowTime_display == 200) {
                        vibreur.vibrate(100);
                    }

                    /* Affichage des publicités */
                    if ((compteurTimer == 2 || compteurTimer == 4) && int_progress_value == 5000) {
                        if (compteur_ad % 2 == 0) {
                            showInterstitial();  //adMob
                        }else{

                            //handlertappxInterAd.post(tappxInterAd);
                            final String TAPPX_KEY = "/120940746/Pub-9623-Android-3570";
                            adInterstitial = com.tappx.TAPPXAdInterstitial.Configure(ActiviteCompteARebours.this, TAPPX_KEY,
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
                    }
                }

                handler.postDelayed(this, INTERVALLE);
        }//run

        public void killRunnable(){
            //when timer is paused or stopped isCanceled is set to true.
            isCanceled = true;
        }

        public boolean isRunning(){
            return !isCanceled;
        }

        public int getTimeInMillis(){
            return int_rowTime_display;
        }

        public void addTime(){
            //when timer is paused or stopped isCanceled is set to true.
            bln_addTime = true;
        }
    }//Runnable


    /****************************************************************
     *
     * Runnable to get Interstitial from tappX
     * RUNNABLE CALLED FROM AN OTHER RUNNABLE
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
                    adInterstitial = com.tappx.TAPPXAdInterstitial.Configure(ActiviteCompteARebours.this, TAPPX_KEY,
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
            adInterstitial = null;
        }//killRunnable
    }//class interne prefsRunnable



    /*******************************************************************************************************
     *
     * @ workoutIntent
     * @ Méthode pour lancer l'intent workout
     * @ doit permettre au timer de continuer à compter les secondes
     *******************************************************************************************************/

    void workoutIntent() {
        //création d'un nouvel intent vers l'entraînement
        Intent workout = new Intent(ActiviteCompteARebours.this, ActiviteEntrainement.class);

        //on augmente le compteur ce qui permettra de passer à la série suivante sur l'intent entrainement
        compteurTimer++;

        //ajout de l'extra pour l'intent entrainement
        workout.putExtra(str_compteurEntrainement, compteurTimer);
        //on cherche si une activité workout existe et on la ramène en première position
        workout.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //Comme l'activité ActiviteEntrainement attend un résultat on retourne le résultat OK
        setResult(RESULT_OK, workout);

        // fin de l'activité ActiviteCompteARebours
        finish();
    }

    /*******************************************************************************************************
     *
     * @ onPause
     * @ Méthode lors de la mise ne pause de l'activité
     * @ doit permettre au timer de continuer à compter les secondes
     *******************************************************************************************************/
    @Override
    protected void onPause() {
        super.onPause();

        //Log.d("TIMER", "Arrêt du time, onPause method");
    }

    /*******************************************************************************************************
     *
     * @ onStop
     * @ Méthode lors de l'arrêt de l'activité
     * @ doit permettre a l'activité de ne rien laisser en cours de traitement
     *******************************************************************************************************/
    @Override
    protected void onStop() {
        super.onStop();
        //Log.d("TIMER", "onStop method");
    }

    protected void onDestroy() {
        super.onDestroy();
        //Log.d("TIMER", "Arrêt du timer, onDestroy Method");
        if(runnable != null){
            runnable.killRunnable();
            handler.removeCallbacks(runnable);
            runnable = null;
        }
    }

    /********************************************************************
     *
     * onBackPressed
     * Méthode appellée lorsque l'utilisateur appuie sur le bouton retour
     *
     ********************************************************************/
    @Override
    public void onBackPressed(){
        //lors du premier passage doubleBackExit vaut false
        //c'est lors du second passage qu'il vaudra true et ce qui est dans le if
        //sera alors effectué
        if(doubleBackExit){
            //called to quit
            btn_passer.performClick();
        }

        //après le premier passage dans la boucle on change la valeur de doubleBackExit
        this.doubleBackExit = true;

        //display a Toast telling the user to press again if he wants to leave
        String str_backtoTraining = getResources().getString(R.string.str_backtoTraining);
        if(pressed == 0) {
            Toast.makeText(this, str_backtoTraining, Toast.LENGTH_SHORT).show();
            pressed = 1;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackExit = false;
                pressed = 0;
            }
        },2500);
    }//onBackPressed
}
