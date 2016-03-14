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
                = {"Négatives"              , "Chin-ups"        , "Wide Pull-ups"       , "Military Pull-up"    };
        String[] musclesTravailles
                = {"Biceps, Grand dorsal, " , "Biceps, Trapèze" , "Trapèze, Romboïdes"  , "Trapèze"             };
        String[] niveauTraction
                = {"Débutant", "Standard"   , "Avancé"          , "Avancé"              , "Intermédiaire"       };
        String[] codeVideo
                = {"CgE_UY7wW1w"            , "yhyJ0tBcgJE"     , "VJ8EhO5kW-w"         , "oviw1pJ5M-c"         };

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
