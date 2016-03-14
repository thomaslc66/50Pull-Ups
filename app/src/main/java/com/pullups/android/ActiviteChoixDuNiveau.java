package com.pullups.android;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pullups.android.Realm.JourEntainement;
import com.pullups.android.Realm.JourEntrainementDB;
import com.pullups.android.adapter.LevelAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.RealmResults;

public class ActiviteChoixDuNiveau extends FragmentActivity{


    //déclaration et instanciation des variables
    private String str_numeroNouveauJourChoisi;
    private static final String str_JourEnCours = "jourEnCours";
    private static final String str_NiveauEnCours = "niveauEnCours";
    private int jourEnCours;
    private int int_numeroNouveauJourChoisi;
    private int indexDuTableau;
    private float niveau;
    private boolean premierChargment = true;
    private boolean selected;

    //éléments graphiques
    private TextView txtViewTitreNiveau;
    private TextView textViewNumeroDuJour;
    private ListView listeDesChoixDuNiveau;

    //parcours de tout le base puis ajout des jours dans la ArrayList
    private List<int[]> listeDuNiveau;
    private LevelAdapter levelAdapter;
    private Handler handler;
    private makeAllLevelList runnable;

    //constantes
    private final int UN = 1;
    private final int ZERO = 0;
    private final float LIMITE_INF = 2f;
    private final float LIMITE_SUP = 18f;
    private final List<Float> tableauDesNiveaux =
            new ArrayList<>(Arrays.asList(1f,1.2f,1.3f,2f,3f,4f,5f,6f,7f,8f,9f,10f,11f,12f,13f,14f,15f,16f,17f));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_level);
        super.onCreate(savedInstanceState);

        //initialisation des éléments graphiques
        listeDesChoixDuNiveau      = (ListView) findViewById(R.id.selectLevelList);
        txtViewTitreNiveau         = (TextView) findViewById(R.id.txtViewLevel);
        TextView txtviewArrowLeft  = (TextView) findViewById(R.id.btnArrowLeft);
        TextView txtviewArrowRight = (TextView) findViewById(R.id.btnArrowRight);
        Button btn_validate        = (Button)   findViewById(R.id.btnValidateLevel);

        listeDuNiveau = new ArrayList<>(); //evite l'erreur NullPointerException

        //lors du lancement de l'activité
        if(premierChargment){
            Intent data = getIntent();
            //on récupère le niveau en cours
            niveau = data.getFloatExtra(str_NiveauEnCours, UN);
            //on récupère le jour
            jourEnCours = data.getIntExtra(str_JourEnCours, UN);
        }//if

        handler  = new Handler();
        runnable = new makeAllLevelList();

        indexDuTableau = tableauDesNiveaux.indexOf(niveau); //on récupère le niveau

        //start background action
        handler.post(runnable);

        /**********************************************************************
         *
         * Méthode lors de l'appui sur la flèche Gauche
         * Permet de changer de niveau -> niveau inférieur
         * setOnclickListener
         *
         *******************************************************************/
        txtviewArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //action lors du click sur la flèche gauche
                if(indexDuTableau > ZERO){
                    //vérifie que l'index est plus grand que ZERO
                    indexDuTableau--;
                    affichageDuNiveauSuivant(indexDuTableau);
                }//if
            }//onclick
        });


        /**********************************************************************
         *
         * Méthode lors de l'appui sur la flèche Droite
         * Permet de changer de niveau -> niveau supérieur
         * setOnclickListener
         *
         *******************************************************************/
        txtviewArrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //action lors du click sur la flèche droite
                if(indexDuTableau < LIMITE_SUP){
                    //vérifie que l'index est plus petit que 17
                    indexDuTableau++;
                    affichageDuNiveauSuivant(indexDuTableau);
                }//if
            }//onclick
        });

        /**********************************************************************
         *
         * Méthode lors de l'appui sur le bouton valider
         * Valide le jour choisi
         * setOnclickListener
         *
         *******************************************************************/
        btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the numeroNouveauJourChoisi is selected in the list, we can validate the choice
                if (selected) {
                    Intent activiteAvantEntrainement = new Intent(ActiviteChoixDuNiveau.this, ActiviteAvantEntrainement.class);
                    //here we put the new selected numeroNouveauJourChoisi
                    activiteAvantEntrainement.putExtra(str_JourEnCours, int_numeroNouveauJourChoisi);
                    activiteAvantEntrainement.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(activiteAvantEntrainement);
                    finish();
                }//if
            }//onClick
            }//setOnClickListener
        );
    }//onCreate()

    /*****************************************************
     * onStart method
     * Show ads when user wants to select level
     *****************************************************/
    private void affichageDuNiveauSuivant(int indexDuTableau) {
        String str_niveau;
        Resources res = getResources();
        float niveauSuivant = tableauDesNiveaux.get(indexDuTableau);
        creerLaListeDesJoursDuNiveau(listeDuNiveau, niveauSuivant);
        levelAdapter.updateLevel(listeDuNiveau); //mise a jour de l'affichage
        if(niveauSuivant >= LIMITE_INF){
            str_niveau = String.format(res.getString(R.string.displayLevelFormat),niveauSuivant);
        }
        else{
            str_niveau = String.format(res.getString(R.string.displayLevelFormatComa),niveauSuivant);
        }
        txtViewTitreNiveau.setText(str_niveau); //Modification affichage
    }//onStart()

    /*****************************************************
     * onStart method
     * Show ads when user wants to select level
     *****************************************************/
    @Override
    protected void onStart() {
        super.onStart();
    }//onStart()


    /***************************************************
     * MakeAllLevelList class
     * Make a list of days in a Level
     ***************************************************/
    private class makeAllLevelList implements Runnable{
        private boolean isCanceled = false;

        @Override
        public void run() {
            //check if runnable is Canceled or not
            if(isCanceled){
                runnable = null; //if yes, set runnable to Null
            }else {
                premierChargment = false;
                String titreDuNiveau = String.format(getResources().getString(R.string.TitleLevel),niveau);
                txtViewTitreNiveau.setText(titreDuNiveau); //Modification affichage
                creerLaListeDesJoursDuNiveau(listeDuNiveau, niveau);

                //on passe en paramètre le context et une liste
                levelAdapter = new LevelAdapter(ActiviteChoixDuNiveau.this, listeDuNiveau);
                listeDesChoixDuNiveau.setAdapter(levelAdapter); //valide l'adapter

                /***************************************************
                 * listeDesChoixDuNiveau
                 * Action lors du clic sur un niveau de la liste
                 ***************************************************/
                listeDesChoixDuNiveau.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view.setSelected(true); //valide le jour/vue
                        //on récupère le jour
                        textViewNumeroDuJour = (TextView) view.findViewById(R.id.numeroDuJour);
                        str_numeroNouveauJourChoisi = textViewNumeroDuJour.getText().toString();

                        //on cast le jour en un int
                        int_numeroNouveauJourChoisi = Integer.parseInt(str_numeroNouveauJourChoisi);

                        //on valide le choix de l'utilisateur
                        selected = true; //valide le choix de l'utilisateur
                    }
                });
            }//else
            //
            killrunnable();
        }

        private void killrunnable(){
            isCanceled = true;
        }
    }

    /**********************************************************************************
     * createDayList()
     * @param niveau_
     * goal permet de créer la liste qui sera envoyée pour l'affichage
     ********************************************************************************/
    private void creerLaListeDesJoursDuNiveau(List<int[]>  listeDuNiveau, Float niveau_){
        //create a Realm TrainingDB
        if(listeDuNiveau != null){  //evite null pointer exception
            listeDuNiveau.clear();
        }
        JourEntrainementDB joursDuNiveau = new JourEntrainementDB(ActiviteChoixDuNiveau.this);
        RealmResults<JourEntainement> t = joursDuNiveau.getAllTrainingInLevel(niveau_);
        int size = t.size();
        for (int i = ZERO; i < size; i++){
            /* add series to the list level*/
            listeDuNiveau.add(joursDuNiveau.trouverLesSeriesPourLeChoixDuNiveau(t.get(i)));
        }//for

    }

    /********************************************************************
     *
     * onBackPressed
     * Méthode appellée lorsque l'utilisateur appuie sur le bouton retour
     * Le But de cette méthode et de permettre à l'utilisateur de retourner à l'écran précédent
     *
     ********************************************************************/
    @Override
    public void onBackPressed(){
            /*si l'utilisateur n'as pas choisi de jour et qu'il appuye sur un bouton
               alors on le renvoie à l'activity pre_workout */
            Toast.makeText(ActiviteChoixDuNiveau.this,getResources().getString(R.string.no_level_selected), Toast.LENGTH_SHORT).show();

            Intent activiteAvantEntrainement = new Intent(ActiviteChoixDuNiveau.this, ActiviteAvantEntrainement.class);
            activiteAvantEntrainement.putExtra(str_JourEnCours, jourEnCours);
            //prend l'ancienne activité pre_workout et la réutilise en l'affichant de nouveau
            //pas de création d'une nouvelle activité
            activiteAvantEntrainement.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(activiteAvantEntrainement);
    }//onBackPressed

    /********************************************************************
     *
     * onPause
     * Méthode appellée lorsque l'activité passe a un stat "Paused"
     * Le But de cette méthode et de tuer l'activité afin qu'elle n'occupe pas de mémoire
     *
     ********************************************************************/
    @Override
    protected void onPause() {
        super.onPause();
        if(runnable != null){
            handler.removeCallbacksAndMessages(runnable);
            runnable = null;
        }
        //tue l'activité
        finish();
    }
}//ActiviteChoixDuNiveau
