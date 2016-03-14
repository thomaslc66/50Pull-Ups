package com.pullups.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pullups.android.Youtube.ActiviteVideo;
import com.pullups.android.R;
import com.pullups.android.Youtube.PlayerVideo;
import com.pullups.android.model.ExerciceObject;

import java.util.ArrayList;

/**
 * Created by Thomas on 21.02.2015.
 */
public class ExercicesAdapter extends BaseAdapter{

    //attributs de la classe
    private String Video;
    private final ArrayList<ExerciceObject> exerciceObjectArrayList;
    private final LayoutInflater mInflater;


    public ExercicesAdapter(Context context, ArrayList<ExerciceObject> exerciceObjectArrayList){
        this.mInflater = LayoutInflater.from(context);
        this.exerciceObjectArrayList = exerciceObjectArrayList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return exerciceObjectArrayList.get(position);
    }

    @Override
    public int getCount() {
        return this.exerciceObjectArrayList.size();
    }

    /**********************************************************
     *
     * getView réecriture de la méthode getView présente dans la class BaseAdpater afin de permettre
     * un recyclage de views qui ne sont plus visibles et d'implémenter les vues selon la liste exercieObjectArrayList
     * @param position
     * @param convertView
     * @param parent
     * @return
     *******************************************************************/
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final TextView title;
        final TextView muscles;
        final TextView intensity;
        final ImageView video;

        if (convertView == null){
            convertView  = mInflater.inflate(R.layout.exercice_list_item,parent,false);

            title = (TextView) convertView.findViewById(R.id.textView_titre_exo);
            video = (ImageView) convertView.findViewById(R.id.exoListView);
            muscles = (TextView) convertView.findViewById(R.id.textViewMuscle);
            intensity = (TextView) convertView.findViewById(R.id.textViewIntensity);
            convertView.setTag(new ViewHolder(title, muscles, intensity, video));

        }else{
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            title = viewHolder.txtViewTilte;
            video = viewHolder.videoView;
            muscles = viewHolder.muscles;
            intensity = viewHolder.intensity;
        }

        title.setText(exerciceObjectArrayList.get(position).getTitle());
        muscles.setText(exerciceObjectArrayList.get(position).getMuscles());
        intensity.setText(exerciceObjectArrayList.get(position).getIntensity());



        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(parent.getContext(), PlayerVideo.class );
                videoIntent.putExtra("lien", exerciceObjectArrayList.get(position).getLien() );
                parent.getContext().startActivity(videoIntent);
            }
        });


        return convertView;
    }//getview

    private class ViewHolder{
        //titre de la vidéo
        final TextView txtViewTilte;
        //vidéo de l'exercice
        final ImageView videoView;
        //TextView pour les muscles travaillés
        final TextView muscles;
        //TextView pour l'intensité de l'exrecice
        final TextView intensity;

        public ViewHolder(TextView txtViewTilte, TextView muscles, TextView intensity, ImageView video){
            this.txtViewTilte = txtViewTilte;
            this.videoView = video;
            this.muscles = muscles;
            this.intensity = intensity;
        }
    }
}
