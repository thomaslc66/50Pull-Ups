package com.pullups.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pullups.android.R;

import java.util.List;


/**
 *
 * Created by Thomas on 13.12.2014.
 * Level adapter est une classe qui étend la classe BaseAdapter
 * et qui permet d'afficher un liste avec les niveaux que l'on souhaite
 * choisir
 *
 */
public class LevelAdapter extends BaseAdapter {

    //Constantes
    private final int ZERO      = 0;
    private final int UN        = 1;
    private final int DEUX      = 2;
    private final int TROIS     = 3;
    private final int QUATRE    = 4;
    private final int CINQ      = 5;
    private final int SIX       = 6;

    //attributs de la classe
    private         List<int[]>     listeDuNiveau;
    private final   LayoutInflater  mInflater;
    private         Context         context;

    //le context est nécessaire pour inflate views dans getView()
    public LevelAdapter(Context context, List<int[]> listeNiveau ){
        this.mInflater = LayoutInflater.from(context);
        this.listeDuNiveau = listeNiveau;
        this.context = context;
    }

    public void updateLevel(List<int[]> niveau){
        ThreadPreconditions.checkOnMainThread();
        this.listeDuNiveau = niveau;
        notifyDataSetChanged(); //indique à l'activité que les données on changées
    }

    /********************************************
     *
     * Méthodes de la class BaseAdapter à implementer
     *
     **********************************************/
    @Override
    public int getCount() {
        return listeDuNiveau.size();
    }

    @Override
    public Object getItem(int position) {
        return listeDuNiveau.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**********************************************************
     *
     * getView réecriture de la méthode getView présente dans la class BaseAdpater afin de permettre
     * un recyclage de views qui ne sont plus visibles
     * @param position
     * @param convertView
     * @param parent
     * @return
     *******************************************************************/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView numeroDuJour;
        TextView series;
        TextView indexDuJour;
        View view;

        if (convertView == null){
            view            = mInflater.inflate(R.layout.selectlevel_list_item,parent,false);
            numeroDuJour    = (TextView) view.findViewById(R.id.title);
            series          = (TextView) view.findViewById(R.id.series);
            indexDuJour     = (TextView) view.findViewById(R.id.numeroDuJour);
            view.setTag(new ViewHolder(numeroDuJour,series, indexDuJour));
        }else{
            view = convertView;
            ViewHolder viewHolder   = (ViewHolder) view.getTag();
            numeroDuJour            = viewHolder.dayNbr;
            series                  = viewHolder.series;
            indexDuJour             = viewHolder.date;
        }

        //récupère et affiche les données pour chaque jours dans la liste
        int tabOfSeries[]   = listeDuNiveau.get(position);
        String str_jour     = String.format(context.getString(R.string.displayDayActiviteChoixDuNiveau),tabOfSeries[SIX]);
        numeroDuJour.setText(str_jour);
        series.setText(tabOfSeries[ZERO]+" / "+tabOfSeries[UN]+" / "+tabOfSeries[DEUX]+" / "+tabOfSeries[TROIS]+" / "+tabOfSeries[QUATRE]);
        indexDuJour.setText("" + tabOfSeries[CINQ]);

        return view;
    }

    /**********************************/
    private class ViewHolder {
        public final TextView dayNbr;
        public final TextView series;
        public final TextView date;

        public ViewHolder(TextView dayNbr, TextView series, TextView date){
            this.dayNbr = dayNbr;
            this.date = date;
            this.series = series;
        }
    }
}//class LevelAdapter

