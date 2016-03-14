package com.pullups.android.Youtube;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.pullups.android.Exercices;
import com.pullups.android.R;

/**
 * Created by Thomas on 27.02.2016.
 */

public class PlayerVideo extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener{

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    //player View
    private YouTubePlayerView youTubePlayerView;
    private String lienYoutube;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video);

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        /* récupération de l'intent qui nous permettra de récupérer ses données*/
        Intent intent = getIntent();
        lienYoutube = intent.getStringExtra("lien");

        //lien entre le playere vidéo et la clef API Google.
        youTubePlayerView.initialize(Config.DEVELOPER_KEY,this);
    }//on create

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                      YouTubeInitializationResult errorReason) {
        if(errorReason.isUserRecoverableError()){
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        }else{
            String errorMessage = String.format(getString(R.string.error_player),
                    errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                     YouTubePlayer youTubePlayer, boolean wasRestored) {
        if(!wasRestored){
            //loadVide() permet de lancé la video automatiquement
            //Use cueVideo() pour ne pas lancé la video automatiquement

            youTubePlayer.loadVideo(lienYoutube);
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

        }//if
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RECOVERY_DIALOG_REQUEST){

            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);

        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider(){
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
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
        if (youTubePlayerView.isActivated()){

        }
         /*si l'utilisateur n'as pas choisi de jour et qu'il appuye sur un bouton
          alors on le renvoie à l'activity pre_workout */
        Intent back_to_exercices = new Intent(PlayerVideo.this, Exercices.class);
        //prend l'ancienne activité pre_workout et la réutilise en l'affichant de nouveau
        //pas de création d'une nouvelle activité
        back_to_exercices.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back_to_exercices);

        //tue l'activité
        finish();
    }
}
