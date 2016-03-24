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
import android.widget.ListView;

import com.pullups.android.adapter.ExercicesAdapter;
import com.pullups.android.model.ExerciceObject;

import java.util.ArrayList;

public class Exercices extends Activity {

    private ListView                    exoListView;
    private ExercicesAdapter            exercicesAdapter;
    private ArrayList<ExerciceObject>   exoObjList;
    private ExerciceObject              objExercice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercices);
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
                = { "Débutant"      ,
                    "Standard"      ,
                    "Avancé"        ,
                    "Avancé"        ,
                    "Intermédiaire" };
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

        exercicesAdapter = new ExercicesAdapter(Exercices.this, exoObjList);
        exoListView.setAdapter(exercicesAdapter);

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
}
