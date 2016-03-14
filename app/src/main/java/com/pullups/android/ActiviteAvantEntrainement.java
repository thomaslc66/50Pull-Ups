package com.pullups.android;

/**
 * Created by Thomas on 18.11.2014.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pullups.android.Realm.JourEntainement;
import com.pullups.android.Realm.JourEntrainementDB;
import java.util.ArrayList;
import java.util.List;

public class ActiviteAvantEntrainement extends Activity {

    //Constantes
    private final int       REQUESTCODEWORKOUT              =   1;
    private final int       ZERO                            =   0;
    private final int       UN                              =   1;
    private final int       DEUX                            =   2;
    private final int       TROIS                           =   3;
    private final int       QUATRE                          =   4;
    private final int       CINQ                            =   5;
    private final int       SIX                             =   6;
    private final int       DERNIER_JOUR_D_ENTRAINEMENT     =   115;
    private final int       DELAIS_ATTENTE                  =   2000;
    private final String    str_NombreDeTractions           =   "nombreDeTractions";
    private final String    str_JourEnCours                 =   "jourEnCours";

    //déclaration des variables
    private       int           jourEnCours;
    private       int           jourDansLeNiveau;
    private       int           sommeDesTractionDuJour;
    private       float         niveauEnCours;
    private       boolean       doubleBackExit;

    private       creationTableauDesSeriesDuJour tableauDesSeriesDuJour;
    private       Handler       handler;

    //Views
    private       TextView      vueDeLaSerie1,
                                vueDeLaSerie2,
                                vueDeLaSerie3,
                                vueDeLaSerie4,
                                vueDeLaSerie5;
    private       TextView      txtViewNiveauEnCours,
                                txtViewEntrainement,
                                txtView_TractionsNegatives,
                                txtView_Nbrtotal;
    private       Button        btnEntrainement,
                                btnChoixDuNiveau;
    private       ProgressBar   progressBar;
    private       ImageView     imgView_level;


    /********************************************************************
     *
     * Method Name : void onCreate
     * @param savedInstanceState
     * Goal: Méthode principale de l'activité AvantEntrianement.
     *
     ********************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_workout);

        //Vues et boutons
        btnChoixDuNiveau           = (Button)      findViewById(R.id.btn_Level);
        btnEntrainement            = (Button)      findViewById(R.id.btn_entrainement);
        vueDeLaSerie1              = (TextView)    findViewById(R.id.viewSerie1);
        vueDeLaSerie2              = (TextView)    findViewById(R.id.viewSerie2);
        vueDeLaSerie3              = (TextView)    findViewById(R.id.viewSerie3);
        vueDeLaSerie4              = (TextView)    findViewById(R.id.viewSerie4);
        vueDeLaSerie5              = (TextView)    findViewById(R.id.viewSerie5);
        txtViewNiveauEnCours       = (TextView)    findViewById(R.id.txtView_level);
        txtViewEntrainement        = (TextView)    findViewById(R.id.txtView_Entrainement);
        txtView_Nbrtotal           = (TextView)    findViewById(R.id.txt_NbrTotal);
        imgView_level              = (ImageView)   findViewById(R.id.img_view_levelProgression);
        progressBar                = (ProgressBar) findViewById(R.id.progressBar_level);
        txtView_TractionsNegatives = (TextView)    findViewById(R.id.txtView_info);

        //AccueilActivity envoi le jourEnCours qui sera utilisé pour l'entrainement
        Intent intent           = getIntent();
        jourEnCours             = intent.getExtras().getInt(str_JourEnCours);

        //lance le thread pour selectionner le jour dans la base de donnée
        tableauDesSeriesDuJour  = new creationTableauDesSeriesDuJour();
        handler                 = new Handler();

        progressBar .setVisibility  (View.GONE);//invisible
        handler     .post           (tableauDesSeriesDuJour);

        /************************************************
         *
         * set the action for the click on the btn_workout
         *
         **************************************************/
        btnEntrainement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Créate New Intent for the workout
                Intent activiteEntrainement = new Intent(ActiviteAvantEntrainement.this,
                                                         ActiviteEntrainement.class);
                activiteEntrainement.putExtra(str_JourEnCours       , jourEnCours);
                activiteEntrainement.putExtra(str_NombreDeTractions , sommeDesTractionDuJour);
                activiteEntrainement.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult       (activiteEntrainement  , REQUESTCODEWORKOUT);
            }
        });//btnEntrainement.setOnClickListener

        /************************************************
         *
         * action btnChoixDuNiveau
         *
         **************************************************/
        btnChoixDuNiveau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String str_NiveauEnCours  = "niveauEnCours";

                //Créer le lien et l'activité pour le choix du niveau
                Intent activiteChoixDuNiveau    = new Intent(ActiviteAvantEntrainement.this, ActiviteChoixDuNiveau.class);
                activiteChoixDuNiveau.putExtra(str_JourEnCours      , jourEnCours); //jour d'entraînement en cours
                activiteChoixDuNiveau.putExtra(str_NiveauEnCours    , niveauEnCours); //niveau En Cours
                activiteChoixDuNiveau.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity                 (activiteChoixDuNiveau); //démarrage de l'activité
                finish();
            }
        });

    }//onCreate

    @Override
    protected void onResume(){
        super.onResume();
    }

    /**************************************************************************
     *
     * @ Runnable creationTableauDesSeriesDuJour
     * permet de faire un tableau des series du jour avec un Jour
     *
     *
     *****************************************************************************/
    private class creationTableauDesSeriesDuJour implements Runnable{
        boolean isCanceled = false;

        @Override
        public void run() {
            boolean blnNegatives;
            List<String> tabNbr     = new ArrayList<>();
            Bitmap imageArrierePlan = null;

            if(isCanceled){
                handler.removeCallbacks(tableauDesSeriesDuJour);
                tableauDesSeriesDuJour              = null;
            }else {
                Resources           res             = getResources();
                JourEntrainementDB  t               = new JourEntrainementDB(ActiviteAvantEntrainement.this);
                JourEntainement     jourEntainement = t.trouveJourAvecIdentifiant(jourEnCours);

                niveauEnCours                       = jourEntainement.getNiveau(); //Niveau en Cours
                jourDansLeNiveau                    = jourEntainement.getJourDansLeNiveau(); //Jours dans ce niveau

                //getTabOfSeries crée un tableau des séries du jours.
                tabNbr                              = t.creationTableauDesSeriesPourAffichage(jourEntainement);
                blnNegatives                        = verificationSeriesNegatives(tabNbr); //vérifie les séries négatives

                //somme total des séries du jour en cours
                sommeDesTractionDuJour              = t.sommeDesTractionsDuJour(jourEntainement);

                //Switch pour l'affichage de la progression
                switch (jourDansLeNiveau) {
                    case UN:
                        imageArrierePlan = BitmapFactory.decodeResource(getResources(),R.drawable.level_progress_1);
                        break;
                    case DEUX:
                        imageArrierePlan = BitmapFactory.decodeResource(getResources(), R.drawable.level_progress_2);
                        break;
                    case TROIS:
                        imageArrierePlan = BitmapFactory.decodeResource(getResources(), R.drawable.level_progress_3);
                        break;
                    case QUATRE:
                        imageArrierePlan = BitmapFactory.decodeResource(getResources(), R.drawable.level_progress_4);
                        break;
                    case CINQ:
                        imageArrierePlan = BitmapFactory.decodeResource(getResources(), R.drawable.level_progress_5);
                        break;
                    case SIX:
                        imageArrierePlan = BitmapFactory.decodeResource(getResources(), R.drawable.level_progress_6);
                        break;
                }

                imgView_level.setImageBitmap(imageArrierePlan); //definir l'image d'arrière plan

                //Series du tableau avec uniquement les jours/
                vueDeLaSerie1.setText(tabNbr.get(ZERO));
                vueDeLaSerie2.setText(tabNbr.get(UN));
                vueDeLaSerie3.setText(tabNbr.get(DEUX));
                vueDeLaSerie4.setText(tabNbr.get(TROIS));
                vueDeLaSerie5.setText(tabNbr.get(QUATRE));

                String texte = String.format(res.getString(R.string.displayLevelFormatComa),niveauEnCours); //string placeHolder
                txtViewNiveauEnCours.setText(texte);
                //info négatives
                //s'il y a des tractions négatives alors on affiche un texte spécifique autrement un autre
                if (blnNegatives) {
                    txtView_TractionsNegatives.setText(res.getString(R.string.negativeTrue));
                } else {
                    txtView_TractionsNegatives.setText(res.getString(R.string.negativeFalse));
                }

                txtView_Nbrtotal.setText(String.valueOf(sommeDesTractionDuJour)); //affiche la somme

                this.killRunnable();
            }
        }
        public void killRunnable(){
            isCanceled = true;
        }
    }//class SelectFromDB

    /********************************************************************
     *
     * @ verificationSeriesNegatives permet de vérifier s'il faut afficher le texte "négative" ou non
     * @param tableau
     * @ return String[] tableau
     *
     ********************************************************************/
    private boolean verificationSeriesNegatives(List<String> tableau){
        boolean blnNegatives = false;
        for (int i = UN; i < tableau.size() && !blnNegatives; i++){
            if(tableau.get(i).contains("n")){
                //si il y a une négative le boolean sera passé à true
                blnNegatives = true;
                break;
            }
        }//for
        return blnNegatives;
    }//verificationSeriesNegatives

    /*******************************************************************
     *
     * OnActivityResult
     * Méthode appellée lorsque l'activité ActiviteEntrainement nous retourne le résultat
     *
     *******************************************************************/
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        final SharedPreferences.Editor editeurDePreferences;
        SharedPreferences preferencesPartagees;
        final String str_nomDesPreferences = "preferences";
        final String str_TotalDeTractions = "totalDeTractions";
        final Intent activiteAccueil = new Intent(ActiviteAvantEntrainement.this, ActiviteAccueil.class);

        //si les codes de retour et de résultat sont OK
        if(requestCode == REQUESTCODEWORKOUT && resultCode == RESULT_OK && data != null ){

            /*lorsque l'utilisateur à terminé sont entrainement on appele l'activité ActiviteAvantEntrainement encore une fois
             cela permet à l'utilisateur de choisir s'il souhaite passer au jour suivant ou recommancer le
            jour car il a trouvé cela trop difficile.*/

            //Partie Affichage en premier
            txtViewEntrainement.setText(R.string.str_entrainement_termine);
            txtView_TractionsNegatives.setText(R.string.str_bravo);
            imgView_level.setVisibility(View.GONE); //cacher l'imageView
            progressBar.setProgress(ZERO);
            progressBar.setProgress(jourDansLeNiveau); //le progres en cours
            progressBar.setVisibility(View.VISIBLE);
            btnChoixDuNiveau.setText(R.string.same_day); //change l'action/texte du bouton Niveau
            btnEntrainement.setText(R.string.next_day);

            //final Intent activiteAccueil = new Intent(ActiviteAvantEntrainement.this, ActiviteAccueil.class);
            activiteAccueil.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //si ActiviteAccueil n'est pas morte

            preferencesPartagees = getSharedPreferences(str_nomDesPreferences, ZERO);
            editeurDePreferences =  preferencesPartagees.edit(); //editeur des préférence

            int totalDeTractions = preferencesPartagees.getInt(str_TotalDeTractions, ZERO); //total en tout
            int maximumDeTractionsEffectuees = preferencesPartagees.getInt(str_NombreDeTractions, ZERO); //maximum du jour

            /* Si le nombre max de PullUp est plus grand que le record déjà présent
            alors on affecte la nouvelle valeur et on l'enregistre dans les préférences */
            int maxPullUp = data.getIntExtra(str_NombreDeTractions, ZERO); //traction max de la série
            if(maximumDeTractionsEffectuees < maxPullUp){
                editeurDePreferences.putInt(str_NombreDeTractions, maxPullUp);
            }

            //Ajoute le nouveau total de traction
            int total = sommeDesTractionDuJour + totalDeTractions;
            editeurDePreferences.putInt(str_TotalDeTractions, total);

            //lors du click sur Répéter le jour
            btnChoixDuNiveau.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*on devrait normalement rien faire car le jour restera le même
                    pas besoin donc d'enregistrer le jour dans les Prefs*/
                    startActivity(activiteAccueil);
                    //on enregistre juste la somme qui a été passée plus haut
                    editeurDePreferences.apply();
                    finish();
                }
            });//setOnClickListener

            //lors du click sur jour suivant
            btnEntrainement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (jourEnCours == DERNIER_JOUR_D_ENTRAINEMENT) {
                        /* nous somme ici au dernier jours d'entraînement */
                        Toast.makeText(ActiviteAvantEntrainement.this, getResources().getString(R.string.lastDay), Toast.LENGTH_LONG).show();
                    }
                     jourEnCours++; // ici on augmente quand même le jour afin qu'il puisse correspondre au INT_LAST_DAY
                    editeurDePreferences.putInt(str_JourEnCours, jourEnCours);
                    startActivity(activiteAccueil);
                    editeurDePreferences.apply(); //enregistre les modifs sur les préférences
                    finish();
                }
            });
        }//if
        else{
            //en cas d'échec un message d'erreur s'affiche
            Toast.makeText(this,getResources().getString(R.string.lost_progression), Toast.LENGTH_SHORT).show();
        }
    }//onActivityResult


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
            Intent accueil = new Intent(this, ActiviteAccueil.class);
            accueil.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(accueil);
            finish();
        }

        //après le premier passage dans la boucle on change la valeur de doubleBackExit
        this.doubleBackExit = true;

        //display a Toast telling the user to press again if he wants to leave
        String str_backToMain = getResources().getString(R.string.str_backToMain);
        Toast.makeText(this,str_backToMain, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackExit = false;
            }
        },DELAIS_ATTENTE);
    }//onBackPressed


    @Override
    protected void onPause() {
        super.onPause();
        if (tableauDesSeriesDuJour != null){
            handler.removeCallbacks(tableauDesSeriesDuJour);
            tableauDesSeriesDuJour = null;
            handler= null;
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
    }
}//public class ActiviteAvantEntrainement
