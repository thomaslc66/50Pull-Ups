package com.pullups.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.pullups.android.R;

import java.util.List;

/**
 * Created by Thomas on 12.02.2015.
 */
//class interne a la class ActiviteStatistiques
public class ChartDataAdapter extends ArrayAdapter<BarData> {

    private final List<String> title;

    public ChartDataAdapter(Context context, List<BarData> objects, List<String> titre) {
        super(context, 0, objects);
        //on ajoute la liste a l'objet
        this.title = titre;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BarData data = getItem(position);
        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();

            //on prend le layout list_item_barchat pour l'afficher dans la listView de l'activité
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_barchart, null);
            //de ce layout on prend R.id.Chart qui est une textView
            holder.titreGraph = (TextView) convertView.findViewById(R.id.txtView_Stats_Level);
            holder.chart = (BarChart) convertView.findViewById(R.id.chart);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //format des valeurs de l'axe Y pour ne pas afficher les décimales
        YAxisValueFormatter YvalueFormatter = new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return String.format("%.0f", value);
            }
        };


        // apply styling
        holder.chart.setDescription("");
        holder.chart.setDrawGridBackground(false);
        holder.chart.setDrawBarShadow(true);
        //Desactiver le zoom sur les graphiques
        holder.chart.setDoubleTapToZoomEnabled(false);
        holder.chart.setScaleEnabled(false);
        //Change la couleur du texte
        data.setValueTextColor(Color.WHITE);
        //titre du niveau
        holder.titreGraph.setText(title.get(position));


        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        //xAxis.setDrawGridLines(true);

        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setLabelCount(5, true);
        leftAxis.setValueFormatter(YvalueFormatter);
        leftAxis.setSpaceTop(10f);
        leftAxis.setDrawGridLines(true);

        //on met la valeur maximum a 150
        //leftAxis.setAxisMaxValue(150f);

        YAxis rightAxis = holder.chart.getAxisRight();
        rightAxis.setEnabled(false);

        // set data
        holder.chart.setData(data);

        // do not forget to refresh the chart
        //holder.chart.invalidate();
        holder.chart.animateY(700);

        return convertView;
    }

    //Pour définir la vue afin de ne pas avoir à le faire pour chaque ligne de la listView
    private class ViewHolder {
        BarChart chart;
        TextView titreGraph;
    }
}
