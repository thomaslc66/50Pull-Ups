/*
*
* Programm Name: 50 Pull ups
* By:
* First Name: Thomas
* Last Name: Léchaire
* Created on : 19.08.2014.
* Last Modified: 03.10.2014
*
*
* */

package com.pullups.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pullups.android.Realm.JourEntainement;
import com.pullups.android.Realm.JourEntrainementDB;
import com.pullups.android.TextView.FontFitTextView;

import java.util.List;

import static android.text.TextUtils.split;


public class ActiviteEntrainement extends Activity {


    //Variables
    private         int     compteurEntrainement    = 1;
    private final   int     REQUESTCODEWORKOUT      = 1;
    private         int     sommeDesTractionsDuJour;
    private         int     jourEnCours;
    private  List<String>   listeDesSeriesString;
    private         float   flt_niveau;
    private         int     int_jourDuNiveau;
    private         boolean doubleBackExit;
    private         boolean isCanceled;

    private final   int     ZERO                    = 0;
    private final   int     UN                      = 1;
    private final   int     DEUX                    = 2;
    private final   int     TROIS                   = 3;
    private final   int     QUATRE                  = 4;
    private final   int     CINQ                    = 5;
    private final   int     DELAIS_ATTENTE          = 2000;
    //couleur sur la série entrain d'être effectuée
    private final   int SDK = android.os.Build.VERSION.SDK_INT;
    private final   String  str_nomDesPreferences    = "preferences";
    private final   String  str_TotalDeTractions     = "totalDeTractions";
    private final   String  str_NombreDeTractions    = "nombreDeTractions";
    private final   String  str_JourEnCours          = "jourEnCours";
    private final   String  str_compteurEntrainement = "compteurEntrainement";
    private final   String  str_NiveauEnCours        = "niveauEnCours";

    //Objects
    private     IntentExtraRunnable runnable;
    private     Handler             handler;
    private     Button              btnComplet;
    private     TextView            viewSerie1,
                                    viewSerie2,
                                    viewSerie3,
                                    viewSerie4,
                                    viewSerie5,
                                    txtNiveau,
                                    viewNegative,
                                    txtView_Jour;
    private     FontFitTextView     viewSerieEnCours;
    private     LinearLayout        linearLayout;
    /********************************************************************
     *
     * onCreate Is the first method called. Build the interface
     * @param savedInstanceState
     *
     *********************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        btnComplet       = (Button)          findViewById(R.id.btn_valider);
        viewSerie1       = (TextView)        findViewById(R.id.view_nbr1);
        viewSerie2       = (TextView)        findViewById(R.id.view_nbr2);
        viewSerie3       = (TextView)        findViewById(R.id.view_nbr3);
        viewSerie4       = (TextView)        findViewById(R.id.view_nbr4);
        viewSerie5       = (TextView)        findViewById(R.id.view_nbr5);
        viewNegative     = (TextView)        findViewById(R.id.view_negative);
        viewSerieEnCours = (FontFitTextView) findViewById(R.id.txtView_nombre);
        txtView_Jour     = (TextView)        findViewById(R.id.txtView_Jour);
        txtNiveau        = (TextView)        findViewById(R.id.txtView_welcome);

        handler     = new Handler();
        runnable    = new IntentExtraRunnable();
        handler.post(runnable);

         /****************************************************************
         *
         * Take place when the btnComplet is hit at the end of the serie
         * btnComplet.setOnClickListener
         * @param View.OnClickListener()
         *
         *****************************************************************/
        btnComplet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent activiteCompteARebours = new Intent(ActiviteEntrainement.this, ActiviteCompteARebours.class);
            activiteCompteARebours.putExtra(str_compteurEntrainement, compteurEntrainement);
            //StartActivityForResult car on attend un retour
            activiteCompteARebours.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityForResult(activiteCompteARebours, REQUESTCODEWORKOUT);
            }
        }); //onclickListener
    }//onCreate Méthode

    /**************************************************************************
     *
     * @ Thread getIntentExtra
     * @ new Runnable
     * permet de récupérer les Extras passé à cet intent et d'afficher dans le
     * UI Thread les séries du jour sans faire d'appel à la base de données
     *
     *****************************************************************************/
    private class IntentExtraRunnable implements Runnable{
        private int[] listeDesSeriesNombres;
        private Resources res = getResources();

        @Override
        public void run() {
            if(isCanceled){
                runnable = null;
            }else {

                Intent intent           = getIntent(); //Recuperation des Extras
                jourEnCours             = intent.getExtras().getInt(str_JourEnCours); //joursEnCours
                sommeDesTractionsDuJour = intent.getExtras().getInt(str_NombreDeTractions); //Somme des tractions du jour

                JourEntrainementDB t    = new JourEntrainementDB(ActiviteEntrainement.this);
                JourEntainement JourEntrainement = t.trouveJourAvecIdentifiant(jourEnCours);

                //getTabOfSeries crée un tableau de string des séries du jours.
                listeDesSeriesString    = t.creationTableauDesSeriesPourAffichage(JourEntrainement);
                listeDesSeriesNombres   = t.creationTableauDesSeries(JourEntrainement); //tableau des nombres des tractions

                flt_niveau              = JourEntrainement.getNiveau(); //récupère le niveau du jour
                int_jourDuNiveau        = JourEntrainement.getJourDansLeNiveau();

                //assignation du texte des autres séries
                viewSerie1.setText(String.valueOf(listeDesSeriesNombres[ZERO]));
                changerLaCouleurDuFond(null, viewSerie1); //changement des valeurs
                affichageNegativeOuTractions(listeDesSeriesString.get(ZERO));

                viewSerie2.setText(String.valueOf(listeDesSeriesNombres[UN]));
                viewSerie3.setText(String.valueOf(listeDesSeriesNombres[DEUX]));
                viewSerie4.setText(String.valueOf(listeDesSeriesNombres[TROIS]));
                viewSerie5.setText(String.valueOf(listeDesSeriesNombres[QUATRE]));

                //Affichage de la première série dans la txtView_A faire
                //Le compteurEntrainement n'est pas encore définit
                //Il est définit lorsque l'activité ActiviteCompteARebours nous retourne ce dernier

                String jour   = String.format(res.getString(R.string.displayDayActiviteChoixDuNiveau),int_jourDuNiveau);
                String niveau = String.format(res.getString(R.string.displayLevelFormat),flt_niveau);
                txtNiveau.setText(niveau);
                txtView_Jour.setText(jour);

                killRunnable();
            }//else
        }//run

        public void killRunnable(){
            //when timer is paused or stopped isCanceled is set to true.
            isCanceled = true;
        }
    }//class IntentExtraRunnable

    /*******************************************************************
     *
     * Méthode appellée lorsque l'activité ActiviteCompteARebours nous retourne le résultat attendu
     * @param requestCode
     * @param resultCode
     * @param data
     *
     *******************************************************************/
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //Si le code attendu, le code_resultat et l'intent ne sont pas nul alors..
        if (requestCode == REQUESTCODEWORKOUT && resultCode == RESULT_OK && data != null ) {
            //On récupère le nombre du compteurEntrainement que l'on assigne à la variable du même nom
            compteurEntrainement = data.getIntExtra(str_compteurEntrainement, UN);
            //on effectue la suite du programme selon ce nombre
            //on passe donc se nombre à la fonction de traitement

            switch (compteurEntrainement){
                case DEUX:
                    changerLaCouleurDuFond(viewSerie1, viewSerie2);
                    affichageNegativeOuTractions(listeDesSeriesString.get(UN));
                    break;
                case TROIS:
                    changerLaCouleurDuFond(viewSerie2, viewSerie3);
                    affichageNegativeOuTractions(listeDesSeriesString.get(DEUX));
                    break;
                case QUATRE:
                    changerLaCouleurDuFond(viewSerie3, viewSerie4);
                    affichageNegativeOuTractions(listeDesSeriesString.get(TROIS));
                    break;
                case CINQ:
                    changerLaCouleurDuFond(viewSerie4, viewSerie5);
                    affichageNegativeOuTractions(listeDesSeriesString.get(QUATRE));
                    //on change le btnComplet afin qu'il permette à l'utilisateur de revenir à l'acceuil, car il

                    btnComplet.setText(R.string.end_training); //entrainement terminé, change le texte du bouton
                    btnComplet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent activiteAvantEntrainement = new Intent();

                            //update total of traction
                            JourEntrainementDB t = new JourEntrainementDB(ActiviteEntrainement.this);
                            t.miseAJourDuTotal(jourEnCours, sommeDesTractionsDuJour); //ajoute la somme au jour

                            activiteAvantEntrainement.putExtra(str_NombreDeTractions, t.trouverLeRecordDeTractions(t.trouveJourAvecIdentifiant(jourEnCours)));
                            activiteAvantEntrainement.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            setResult(RESULT_OK, activiteAvantEntrainement);

                            finish();//
                        }//onClick
                    });
                    break;
                default:
                    viewSerieEnCours.setText(viewSerie1.getText().toString());
                    break;
            }//switch
        }//if
    }//onActivityResult

    /********************************************
     *
     * Method: affichageNegativeOuTractions()
     * Param: Aucun
     * But: Permet l'affichage du mot négatives ou tractions
     *
     ***********************************************/
    private void affichageNegativeOuTractions(String toTest){
        String[] tabStrView;
        Resources res = getResources();
        if(toTest.contains("n")){
            tabStrView = split(toTest, "\\s");
            viewSerieEnCours.setText(tabStrView[ZERO]);
            viewNegative.setText(res.getString(R.string.negatives));
        }else{
            viewNegative.setText(R.string.pull_up);
            viewSerieEnCours.setText(toTest);
        }
    }//affichageNegativeOuTractions

    /********************************************
     *
     * Method: changerLaCouleurDuFond()
     * Param: TextView view, TextView pastView
     * But: changer la couleur de la série en cours
     * Appel: méthode affichageNegativeOuTractions
     *
     **********************************************/
    @SuppressLint("NewApi")
    private void changerLaCouleurDuFond(TextView vuePrecedente, TextView vueSuivante){
        Resources res = getResources();
        Drawable cercleNoir;
        Drawable cercleRouge;

        //récupération des images selon le SDK
        if(SDK >= Build.VERSION_CODES.LOLLIPOP){
            cercleNoir = ContextCompat.getDrawable(this, R.drawable.circle_black_border);
            cercleRouge = ContextCompat.getDrawable(this, R.drawable.circle_red_border);
        }else{
            cercleNoir = res.getDrawable(R.drawable.circle_black_border);
            cercleRouge = res.getDrawable(R.drawable.circle_red_border);
        }

        //si c'est la première série
        if(vuePrecedente == null){
            vueSuivante.setTypeface(null, Typeface.BOLD);
            if(SDK > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                vueSuivante.setBackground(cercleRouge);
            } else {
                vueSuivante.setBackgroundDrawable(cercleRouge);
            }
        }else{
            if(SDK > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                vuePrecedente.setBackground(cercleNoir);
                vuePrecedente.setTypeface(null, Typeface.NORMAL);
                vueSuivante.setBackground(cercleRouge);
                vueSuivante.setTypeface(null, Typeface.BOLD);
            } else {
                vuePrecedente.setBackgroundDrawable(cercleNoir);
                vuePrecedente.setTypeface(null, Typeface.NORMAL);
                vueSuivante.setBackgroundDrawable(cercleRouge);
                vueSuivante.setTypeface(null, Typeface.BOLD);
            }
        }
    }//changerLaCouleurDuFond

    /**************************************************************************
     *
     * @ Thread getSizeofRelativeLayout
     * @ new Runnable
     * permet de récupérer la taille du rectangle du milieu et de diviser sa taille
     * entre le rectangle blanc et le rectangle noir
     *
     *****************************************************************************/
    @SuppressLint("NewApi")
    private Thread getSizeofRelativeLayout = new Thread(new Runnable() {
        @Override
        public void run() {
            linearLayout = (LinearLayout) findViewById(R.id.relativeLayout2);
            linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    //est appelé après que le layout est contruit mais avant l'affichage4
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                        linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                    viewSerieEnCours.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

                    int totalWidth = linearLayout.getWidth();
                    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                    float value = totalWidth / TROIS ;

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewSerieEnCours.getLayoutParams();
                    params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value,displayMetrics);
                    viewSerieEnCours.setLayoutParams(params);
                }
            });
        }
    });

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
            Intent intent_preWorkout = new Intent(this, ActiviteAvantEntrainement.class);
            intent_preWorkout.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent_preWorkout);
            finish();
        }

        //après le premier passage dans la boucle on change la valeur de doubleBackExit
        this.doubleBackExit = true;

        //display a Toast telling the user to press again if he wants to leave
        String str_backToMain = getResources().getString(R.string.str_backToMain);
        Toast.makeText(this, str_backToMain, Toast.LENGTH_SHORT).show();

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackExit = false;
            }
        },DELAIS_ATTENTE);
    }//onBackPressed


    /**************************************************************************
     * onDestroy()
     **************************************************************************/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //getIntentExtra.interrupt();
        getSizeofRelativeLayout.interrupt();
        getSizeofRelativeLayout = null;
        //getIntentExtra = null;

        if(runnable != null){
            handler.removeCallbacks(runnable);
            runnable.killRunnable();
            runnable = null;
        }
    }

}//class ActiviteEntrainement
