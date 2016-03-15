/*
*
* Program Name: 50 Pull ups
* By:
* First Name: Thomas
* Last Name: Léchaire
* Created on : 19.08.2014.
* Last Modified: 14.03.2016
*
*
* */

package com.pullups.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pullups.android.Alarm.AlarmActivity;
import com.pullups.android.Realm.JourEntrainementDB;
import com.pullups.android.ResideMenu.ResideMenu;
import com.pullups.android.ResideMenu.ResideMenuItem;


public class ActiviteAccueil extends Activity implements View.OnClickListener {

    //déclaration des éléments
    private Button              btnEntrainement;
    private Button              btnStats;
    private Button              btnExercices;
    private Button              btnTotal;
    private TextView            totalView;
    private TextView            txtViewRecordTractions;
    private ImageView           imgViewRectangleCentral;
    private SharedPreferences   preferencesPartagees;

    //déclaration des variables
    private final   int       ZERO                        = 0;
    private final   int       PREMIER_JOUR_D_ENTRAINEMENT = 1;
    private final   int       DERNIER_JOUR_D_ENTRAINEMENT = 115;
    private final   int       DELAIS_ATTENTE              = 2000;
    private final   String    str_nomDesPreferences       = "preferences";
    private final   String    str_TotalDeTractions        = "totalDeTractions";
    private final   String    str_NombreDeTractions       = "nombreDeTractions";
    private final   String    str_PremierChargement       = "premierChargement";
    private final   String    str_JourEnCours             = "jourEnCours";

    private         int       int_totalDeTractions;
    private         int       jourEnCours                 = 1;
    private         int       pressed                     = 0;
    private         int       int_nombreDeTractions;
    private         boolean   premierChargement;
    private         boolean   isCanceled                  = false;
    private         boolean   doubleBackExit;
    private         String    recordDeTractions;

    //object declaration
    private prefsRunnable prefsRunnable;
    private Handler           handler;

    /* Reside Menu */
    private ResideMenu resideMenu;
    private ResideMenuItem itemAlarme;
    private ResideMenuItem    itemAbout;
    private ResideMenuItem    itemRate;
    private ResideMenuItem    itemShare;
    private ResideMenuItem    itemReset;


    /********************************************************************
     *
     * Method Name : void onCreate
     * @param savedInstanceState
     * Goal: Méthode principale de l'activité accueil.. appellée lors de la création
     * de l'activité
     *
     ********************************************************************/

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        //instanciation of the elements. Ready to use in the code
        btnEntrainement         = (Button)      findViewById(R.id.btn_entrainement);
        btnExercices            = (Button)      findViewById(R.id.btnExo);
        btnTotal                = (Button)      findViewById(R.id.btnTotal);
        btnStats                = (Button)      findViewById(R.id.btnStats);
        totalView               = (TextView)    findViewById(R.id.txtview_nbrtotal);
        txtViewRecordTractions  = (TextView)    findViewById(R.id.txtView_topRectangle);
        imgViewRectangleCentral = (ImageView)   findViewById(R.id.rectangle_center);

        getActionBar().setHomeButtonEnabled(true); //affiche le bouton du menu
        setUpMenu();

        //Ouvre les préférences
        if(preferencesPartagees == null){
            preferencesPartagees = getSharedPreferences(str_nomDesPreferences,ZERO);
        }

        //Récupère les préférences de l'application
        prefsRunnable   = new prefsRunnable();
        handler         = new Handler();
        handler.post(prefsRunnable);

        /********************************************************************
         *
         * Method Name :  btnEntrainement.setOnClickListener
         * @param new View.OnClickListener()
         * Vérifie quel jour d'entraînement l'utilisateur doit faire et
         * lance l'activité entrainement
         ********************************************************************/
        btnEntrainement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //If it is the FirstLoad of the app we launch the test page
                //Si c'est le premier lancement on charge la page de test
                if (premierChargement) {
                    Intent activiteDeTest = new Intent(getApplicationContext(), ActiviteTest.class);
                    //activiteDeTest.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(activiteDeTest);
                }//if
                /* Here we check if the user is at the end of the DataBase app */
                else if (jourEnCours == DERNIER_JOUR_D_ENTRAINEMENT) {
                    /* In this case we congratulate him for his work */
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.lastDay), Toast.LENGTH_LONG).show();
                }//else if
                else {
                    Intent activiteAvantEntrainement = new Intent(getApplicationContext(), ActiviteAvantEntrainement.class);
                    activiteAvantEntrainement.putExtra(str_JourEnCours, jourEnCours); //envoie le jourEnCours en cours au l'activité avant entrainement
                    //activiteAvantEntrainement.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(activiteAvantEntrainement);
                    finish();
                }//else
            }//onclick
        });

        /********************************************************************
         *
         * Method Name :  btnExercices.setOnClickListener
         * @param new View.OnClickListener()
         * Lance l'activité Exercices
         *
         ********************************************************************/
        btnExercices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activiteExercices = new Intent(getApplicationContext(), Exercices.class);
                //activiteExercices.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(activiteExercices);
                finish();

            }
        });

        /********************************************************************
         *
         * Method Name : btnStats.setOnClickListener
         * @param new View.OnClickListener()
         * for: Listener for the btnStats click
         *
         ********************************************************************/
        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ActiviteStatistiques = new Intent(getApplicationContext(), ActiviteStatistiques.class);
                //ActiviteStatistiques.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(ActiviteStatistiques);
                finish();
            }
        });


        /********************************************************************
         *
         * Method Name : btnTotal.setOnClickListener
         * @param new View.OnClickListener()
         * for: Listener for the btnTotal click
         *
         ********************************************************************/
        btnTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* NO ACTION FOR NOW */

            }
        });

        //first run of the app
        premierChargement = preferencesPartagees.getBoolean(str_PremierChargement, true);

    }//onCreate

    /***************************************
     *
     * Method Name: setUpMenu()
     * @param: empty
     * for: Setting up the menu of the app on the firstPage
     *
     ****************************************/
    private void setUpMenu() {

        // attach to current activity;
        resideMenu  = new ResideMenu(getApplicationContext());
        resideMenu.setBackgroundColor(getResources().getColor(R.color.footer_bg));
        resideMenu.attachToActivity(this);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.5f);

        // create menu items;
        itemAlarme  = new ResideMenuItem(getApplicationContext(), R.drawable.icon_alarm, getResources().getString(R.string.alarme));
        itemAbout   = new ResideMenuItem(getApplicationContext(), R.drawable.icon_info,  getResources().getString(R.string.about));
        itemRate    = new ResideMenuItem(getApplicationContext(), R.drawable.icon_star, getResources().getString(R.string.vote));
        itemReset   = new ResideMenuItem(getApplicationContext(), R.drawable.icon_trash, getResources().getString(R.string.deleteData));
        itemShare   = new ResideMenuItem(getApplicationContext(),R.drawable.icon_share, getResources().getString(R.string.share));

        //set item onClickListener
        itemAlarme. setOnClickListener(this);
        itemAbout.  setOnClickListener(this);
        itemRate.   setOnClickListener(this);
        itemReset.  setOnClickListener(this);
        itemShare.  setOnClickListener(this);

        /* add Item for better app design */
        resideMenu.addMenuItem(itemAlarme,  ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAbout,   ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemRate,    ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemReset,   ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemShare,   ResideMenu.DIRECTION_LEFT);

        // You can disable a direction by setting ->
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    /********************************************************************
     *
     * But: Menu
     * @param item
     *
     *
     ********************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case android.R.id.home:
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }//onOptionItemSelected

    /************************************************************************
     *
     * method name: onClick
     * @param view
     * for: Depending on the choice of the user this method select what to do
     * after the user choixe
     *
     ************************************************************************/
    @Override
    public void onClick(View view) {
        //action lors du click dans le menu alarme
        if (view == itemAlarme){
            //Permet de régler une alarme pour l'entraînement
            Intent alarme = new Intent(getApplicationContext(), AlarmActivity.class);
            startActivity(alarme);
            finish();

        }else if (view == itemAbout){
            Toast.makeText(getApplicationContext(),"INFO",Toast.LENGTH_SHORT).show();

        }else if (view == itemRate){
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }

        }else if (view == itemShare){
            String shareBody        = getString(R.string.shareText);
            Intent sharingIntent    = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType   ("text/plain");
            sharingIntent.putExtra  (android.content.Intent.EXTRA_SUBJECT,    getString(R.string.shareSubject));
            sharingIntent.putExtra  (android.content.Intent.EXTRA_TEXT,       shareBody);
            startActivity           (Intent.createChooser(sharingIntent,      getResources().getString(R.string.share_using)));
        }else if (view == itemReset){

            SharedPreferences.Editor editor = getSharedPreferences(str_nomDesPreferences, MODE_PRIVATE).edit();

            //Effacer la progression dans la base de données
            JourEntrainementDB t_db = new JourEntrainementDB(getApplicationContext());
            t_db.resetDataBase();

            //Restaurer les préférences partagées
            //Rechercher si les préférences contiennent
            if (preferencesPartagees.contains(str_JourEnCours)){
                //met le premier jour à 0
                editor.putInt(str_JourEnCours,PREMIER_JOUR_D_ENTRAINEMENT);
            }//if
            if (preferencesPartagees.contains(str_TotalDeTractions)){
                editor.putInt(str_TotalDeTractions, ZERO); //total de traction à ZERO
            }//if
            if(preferencesPartagees.contains(str_NombreDeTractions)){
                //set nbrMaxTractions
                editor.putInt(str_NombreDeTractions,ZERO);
            }//if
            if(preferencesPartagees.contains(str_PremierChargement)){
                //set premierChargement to true
                editor.putBoolean(str_PremierChargement, true);
            }

            editor.apply();

            //Recuperation des valeurs dans les preferences
            int_totalDeTractions    = preferencesPartagees.getInt       (str_TotalDeTractions,  ZERO);
            int_nombreDeTractions   = preferencesPartagees.getInt       (str_NombreDeTractions, ZERO);
            premierChargement       = preferencesPartagees.getBoolean   (str_PremierChargement, true);

            //Affichage
            txtViewRecordTractions.setText(recordDeTractions );
            totalView.setText("" + int_totalDeTractions);

            //Annonce user reset is completed
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.reinitialisation),Toast.LENGTH_SHORT).show();
        }

        resideMenu.closeMenu();
    }//onclick

     /********************************************************************
     *
     * Method name: onBackPressed
     * @param *
     * for: Handle the backPress Button onClick Action
     *
     ********************************************************************/
    @Override
    public void onBackPressed() {
        String str_pressAgain = getResources().getString(R.string.pressAgain);
        if(doubleBackExit){
            //called to quit
            super.onBackPressed();
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(intent);
            finish();
        }

        this.doubleBackExit = true;
        //display a Toast telling the user to press again if he wants to leave
        if(pressed == ZERO) {
            Toast.makeText(getApplicationContext(), str_pressAgain, Toast.LENGTH_SHORT).show();
            pressed = 1;
        }//if
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackExit = false;
                pressed = ZERO;
            }
        },DELAIS_ATTENTE);

    }//onBackPressed

    /********************************************************************
     * onStop()
     * @param : empty
     *
     ********************************************************************/
    @Override
    protected void onStop(){
        super.onStop();

        //Open a Preferences Editor to add the new values
        SharedPreferences.Editor editor = preferencesPartagees.edit();
        if(premierChargement){
            editor.putBoolean(str_PremierChargement, false);
        }

        //applique les changements aux Preferences
        editor.putInt(str_NombreDeTractions, int_nombreDeTractions);
        editor.apply();

        handler.removeCallbacksAndMessages(prefsRunnable);
        prefsRunnable = null;

        finish();
    }//OnStop


    /********************************************************************
     * onDestroy()
     * @param : empty
     *
     ********************************************************************/
    @Override
    protected void onDestroy() {
        super.onDestroy();

        imgViewRectangleCentral.setBackgroundDrawable(null);
        btnEntrainement.setBackgroundDrawable(null);
        btnExercices.setBackgroundDrawable(null);
        btnTotal.setBackgroundDrawable(null);
        btnStats.setBackgroundDrawable(null);
        totalView.setBackgroundDrawable(null);

        resideMenu = null;

    }


    /********************************************************************
     * onStart()
     * @param : empty
     *
     ********************************************************************/
    @Override
    protected void onStart() {
        super.onStart();
        //first run of the app
        premierChargement = preferencesPartagees.getBoolean(str_PremierChargement, true);
    }

    /****************************************************************
     *
     * Runnable to get Preferences of the app
     *
     * ***************************************************************/
    private class prefsRunnable implements Runnable{

        @Override
        public void run() {
            if(isCanceled){
                prefsRunnable = null;
            }else{
                //Rechercher dans les préférences
                if (preferencesPartagees.contains(str_JourEnCours)){
                    //Jour d'entraînement en cours
                    jourEnCours = preferencesPartagees.getInt(str_JourEnCours,PREMIER_JOUR_D_ENTRAINEMENT);
                }//if
                if (preferencesPartagees.contains(str_TotalDeTractions)){
                    //Total des tractions effectuées
                    int_totalDeTractions = preferencesPartagees.getInt(str_TotalDeTractions, ZERO);
                }//if
                if(preferencesPartagees.contains(str_NombreDeTractions)){
                    //Nombre de tracations max effectuées
                    int_nombreDeTractions = preferencesPartagees.getInt(str_NombreDeTractions,ZERO);
                }//if

                //Change le total des tractions et le record de traction dans l'affichage
                totalView.setText(""+int_totalDeTractions);
                recordDeTractions = String.format(getResources().getString(R.string.recordDeTraction),int_nombreDeTractions);
                txtViewRecordTractions.setText(recordDeTractions);

                killRunnable();
            }//else
        }//run

        public void killRunnable(){
            isCanceled = true;
        }//killRunnable

    }//class interne prefsRunnable

}//Activity ActiviteAccueil