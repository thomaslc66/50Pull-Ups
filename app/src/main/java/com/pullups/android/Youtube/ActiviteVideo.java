package com.pullups.android.Youtube;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.pullups.android.Exercices;
import com.pullups.android.R;

/**
 * Created by Thomas on 27.02.2015.
 */
public class ActiviteVideo extends Activity {

    /* déclation des variables et attributs de la view */
    VideoView video;
    MediaController mediaController;


    /********************************************************************
     *
     * Method Name : void onCreate
     * @param savedInstanceState
     * Goal: Méthode principale de l'activité ActiviteVideo.. appellée lors de la création
     * de l'activité pour la lécture de la vidéo
     *
     ********************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* permet d'afficher la vidéo en horizontal sur la largeur du téléphone*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video);
        /* récupération de l'intent qui nous permettra de récupérer ses données*/
        Intent intent = getIntent();
        //on récupère le titre de l'intent et le met dans la barre d'action
        String title = intent.getStringExtra("title");
        /* on change le titre de l'action bar afin qu'elle prenne le nom de la vidéo en cours de lécture */
        setTitle(title);

        /* on assigne la VideoView */
        //video = (VideoView) findViewById(R.id.videoView);
        /* on ajoute les mediaController a la vidéo */
        mediaController = new MediaController(this);
        mediaController.setAnchorView(this.video);
        mediaController.setMediaPlayer(this.video);
        video.setMediaController(mediaController);

        /* on récupère l'uri de la vidéo et on l'assigne à notre VideoView */
        Uri uriVideo = intent.getData();
        video.setVideoURI(uriVideo);

        /* on demande l'attention et puis on démarre la lécture*/
        video.requestFocus();
        video.start();
    }

    /********************************************************************
     *
     * onBackPressed = gère l'appui sur la touche retour
     * @param *
     *
     ********************************************************************/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (video.isPlaying()){
            video.pause();
        }
         /*si l'utilisateur n'as pas choisi de jour et qu'il appuye sur un bouton
          alors on le renvoie à l'activity pre_workout */
        Intent back_to_exercices = new Intent(ActiviteVideo.this, Exercices.class);
        //prend l'ancienne activité pre_workout et la réutilise en l'affichant de nouveau
        //pas de création d'une nouvelle activité
        back_to_exercices.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(back_to_exercices);

        //tue l'activité
        finish();
    }
}
