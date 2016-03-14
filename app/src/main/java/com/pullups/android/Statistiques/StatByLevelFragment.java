package com.pullups.android.Statistiques;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.pullups.android.R;
import com.pullups.android.Realm.JourEntainement;
import com.pullups.android.Realm.JourEntrainementDB;
import com.pullups.android.adapter.ChartDataAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;

import io.realm.RealmResults;

/**
 * Created by Thomas on 12.07.2015.
 */
public class StatByLevelFragment extends Fragment {


    //Declaration
    private int compteur;
    private ChartDataAdapter cda;
    private ArrayList<BarData> list;
    private ArrayList<String> titreGraph;
    //private static dayFromDB jourdb;
    private JourEntrainementDB t;
    private TextView txtViewTitreNiveauGraphique;
    final private float tab[] = {1,1.2f,1.3f,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};
    private ArrayList<String> xValues = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6"));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_stats, container, false);

        //TextView pour afficher le niveau au dessus de chaque graphique
        TextView titre = (TextView) v.findViewById(R.id.txtViewTitreStats);

        //Liste pour l'affichage des ActiviteStatistiques
        ListView lv = (ListView) v.findViewById(R.id.listView2);
        list = new ArrayList<>();
        titreGraph = new ArrayList<>();

        //on appelle l'AsyncTask pour faire l'execution en Background
        //RunDataBase et une class interne
        RunDataBase runDataBase = new RunDataBase(StatByLevelFragment.this, v.getContext());
        runDataBase.execute();

        //nouvel objet ChartDataAdapter
        cda = new ChartDataAdapter(container.getContext(), list, titreGraph);
        lv.setAdapter(cda);

        return v;
    }//fin de la méthode OnCreate(Bundle savedInstanceState)


    //class interne
    class RunDataBase extends AsyncTask<Context,Void,Integer[]> {
        //référence faible à l'activité
        private WeakReference<StatByLevelFragment> mStats = null;
        Context context;

        public RunDataBase(StatByLevelFragment statsActivity, Context context){
            link(statsActivity);
            this.context = context;
        }

        //méthode qui effectue les tâches en background, aucune tâche n'est executée dans l'UI Thread dans cette méthode
        @Override
        protected Integer[] doInBackground(Context... params) {
            try{
                t = new JourEntrainementDB(this.context);
                //trouver combien de ligne comporte la base de données
                RealmResults r = t.getAllDB();
                int nbr = r.size();
                Integer[] tabInt = new Integer[nbr];

                Log.d("TAG", nbr + "");
                //On parcourt tout le tableau des niveaux
                for (int i = 0; i < nbr; i++) {
                    //on récupère le jour
                    JourEntainement trainingDay = t.trouveJourAvecIdentifiant(i + 1);
                    //on ajoute le total du jour au tableau
                    tabInt[i] = trainingDay.getTotalDeTractions();
                }//for
                return tabInt;
            }//try
            catch(Exception e){
                Log.w("Erreur", e.getMessage().toString());
                Integer[] tabInteger = new Integer[0];
                return tabInteger;
            }//catch
        }//doInBackGround

        @Override
        protected void onPostExecute (Integer[] result) {
            if(result.length > 0){

                //On construit ici les graphiques par niveau
                //entries correspond aux valeurs des colonnes
                int l = 0;
                for (int i = 0; i < tab.length; i++){

                    ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

                    int indexOftabAllPullUp = 0;

                    for( int j = 1; j < 7 ; j++){
                        //on ajoute la valeur en premier paramètre
                        //on ajoute la colonne en second paramètre
                        entries.add(new BarEntry(result[l], indexOftabAllPullUp));
                        //on augemente l'index chauque fois que l'on rajoute un jour
                        indexOftabAllPullUp++;

                        l++;
                    }//for j

                    //on ajoute le tableau de jour au niveau
                    BarDataSet d = new BarDataSet(entries, context.getString(R.string.statsTotalDeTractions));

                    //largeur de l'espacement entre les colonnes
                    d.setBarSpacePercent(20f);
                    //couleur du fond de la barre
                    d.setBarShadowColor(Color.rgb(203, 203, 203));

                    if(compteur == 1){
                        d.setColor(getResources().getColor(R.color.action_bar));
                        compteur = 0;
                    }//if
                    else{
                        d.setColor(getResources().getColor(R.color.bg));
                        compteur = 1;
                    }//else

                    ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
                    sets.add(d);

                    //on ajoute le niveau au dessu du graph
                    String titreDuGraphique = String.format(getResources().getString(R.string.displayLevelFormatComa),tab[i]);
                    titreGraph.add(titreDuGraphique);


                    BarData cd = new BarData(xValues, sets);
                    //ajoute le graph à la listView
                    list.add(cd);
                    cda.notifyDataSetChanged();
                }//for i
            }//if
            else{
                Toast.makeText(this.context, R.string.erreurDansAffichageStatistiques, Toast.LENGTH_SHORT).show();
            }//else
        }//onPostExecute

        public void link(StatByLevelFragment statsActivity){
            mStats = new WeakReference<>(statsActivity);
        }

    }//class statique RunDataBAse

    public static StatByLevelFragment newInstance(String text) {

        StatByLevelFragment f = new StatByLevelFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
