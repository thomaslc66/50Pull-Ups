package com.pullups.android.Statistiques;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.pullups.android.R;
import com.pullups.android.Realm.JourEntainement;
import com.pullups.android.Realm.JourEntrainementDB;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmResults;

/**
 * Created by Thomas on 12.07.2015.
 */
public class StatByDayFragment extends Fragment {

    //Constantes
    private final int ZERO = 0;
    private final int UN = 1;
    private final int CINQ = 5;
    private final int ANIMATE = 700;

    //Declaration
    private int compteur;
    private ArrayList<BarData> list;
    private BarData barData;
    private BarChart chart;

    //private static dayFromDB jourdb;
    private JourEntrainementDB t;
    private ArrayList<String> xValues;
    private ArrayList<BarEntry> entries;
    private AsyncRunnable runDataBase;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_stats_tab, container, false);

        chart = (BarChart) v.findViewById(R.id.chart_fullpage);

        // apply styling
        chart.setDescription("");
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(CINQ, true);
        leftAxis.setSpaceTop(10f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        //on appelle l'AsyncTask pour faire l'execution en Background
        //RunDataBase et une class interne
        runDataBase = new AsyncRunnable(StatByDayFragment.this, v.getContext());
        runDataBase.execute();

        return v;

        }//onCreate

class AsyncRunnable extends AsyncTask<Context, Void, ArrayList<BarDataSet>> {

        //référence faible à l'activité
        private WeakReference<StatByDayFragment> mTestDb = null;
        private SweetAlertDialog pDialog;
        private ArrayList<BarDataSet> sets = null;
        private Context context;

        public AsyncRunnable(StatByDayFragment BarChartActivity, Context context){
            link(BarChartActivity);
            this.context = context;

        }

        public void link(StatByDayFragment BarChartActivity){
            mTestDb = new WeakReference<>(BarChartActivity);
        }

        @Override
        protected void onPreExecute() {
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.setTitleText(context.getResources().getString(R.string.recuperationStatistiques));
            pDialog.setCancelable(false);
            pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.bg));
            pDialog.show();
        }

        @Override
        protected ArrayList<BarDataSet> doInBackground(Context... context) {
            try {
                //reset graphics element/data to 0;
                barData = new BarData();
                entries = new ArrayList<>();
                xValues = new ArrayList<>();
                Resources res = this.context.getResources();

                //Create a instance of the DataBase
                t = new JourEntrainementDB(this.context);

                //get number of line in the DB
                RealmResults r = t.getAllDB();
                int nbr = r.size();
                int pullUpDone;
                compteur = ZERO;

                for (int i = ZERO; i < nbr; i++) {
                    //on r?cup?re le jour
                    JourEntainement trainingDay = t.trouveJourAvecIdentifiant(i + UN);
                    pullUpDone = trainingDay.getTotalDeTractions();
                    //if the day is done we add it to the tab
                    if (pullUpDone != ZERO){
                        //on ajoute le total du jour aau graphique
                        entries.add(new BarEntry(pullUpDone,compteur));
                        //on augmente
                        compteur++;
                        xValues.add(compteur+"");

                    }
                }//for

                //DataSet for all columns
                BarDataSet dataSet = new BarDataSet(entries, res.getString(R.string.nbr_tractions));
                //DataSet Design
                dataSet.setBarSpacePercent(10f);
                dataSet.setColor(res.getColor(R.color.action_bar));


                //ArrayList of DataSet
                ArrayList<BarDataSet> sets = new ArrayList<>();
                sets.add(dataSet);

                //Create BarData object
                barData = new BarData(xValues,sets);

            }catch (Exception e){
                e.printStackTrace();
                Log.w("TAG ", "Error in getting data");
            }

            return sets;
        }

        @Override
        protected void onPostExecute(ArrayList<BarDataSet> sets) {
            pDialog.dismiss();
            //p.dismiss();
            chart.setData(barData);

            // do not forget to refresh the chart
            chart.animateXY(ANIMATE, ANIMATE);
            chart.invalidate();
        }
    }

    public static StatByDayFragment newInstance(String text) {

        StatByDayFragment f = new StatByDayFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}