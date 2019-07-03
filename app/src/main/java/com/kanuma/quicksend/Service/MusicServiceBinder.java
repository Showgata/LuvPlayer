package com.kanuma.quicksend.Service;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.Toast;

import com.kanuma.quicksend.Constants.C;

public class MusicServiceBinder extends Binder {

    private Context context;
    private Uri audioFileUrl;
    private boolean streamAudio = false;
    private MediaPlayer player = null;

    private static final String TAG = "MusicServiceBinder";

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Uri getAudioFileUrl() {
        return audioFileUrl;
    }

    public void setAudioFileUrl(Uri audioFileUrl) {
        this.audioFileUrl = audioFileUrl;
    }



    public void startAudio()
    {
        initAudioPlayer();
        if(player!=null) {
            player.start();
        }
    }

    private void initAudioPlayer() {

        try{
            if(player == null) {
                player = new MediaPlayer();
                player.setDataSource(getContext(), getAudioFileUrl());
                player.prepare();
                player.setOnCompletionListener(completionListener);
            }
        }catch(Exception e){
            Log.e(TAG, "initAudioPlayer: invalid URI: "+ getAudioFileUrl() );
            //Toast.makeText(MusicService.this,"Check your URI..",Toast.LENGTH_SHORT).show();
        }


    }

    public void pauseAudio()
    {
        if(player!=null) {
            player.pause();
        }
    }

    public void stopAudio()
    {
        if(player!=null) {
            player.stop();
            destroyAudioPlayer();
        }
    }

    // Destroy audio player.
    private void destroyAudioPlayer()
    {
        if(player!=null)
        {
            if(player.isPlaying())
            {
                player.stop();
            }

            player.release();

            player = null;
        }
    }

    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            //Toast.makeText(MusicService.this,"Music ended",Toast.LENGTH_SHORT).show();
        }
    };

}
