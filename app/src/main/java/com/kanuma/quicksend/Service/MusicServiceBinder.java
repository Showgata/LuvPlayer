package com.kanuma.quicksend.Service;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.kanuma.quicksend.Constants.C;

public class MusicServiceBinder extends Binder {

    private Context context;
    private Uri audioFileUrl;
    private boolean streamAudio = false;
    private MediaPlayer player = null;
    private int length=0;

    // This Handler object is a reference to the caller activity's Handler.
    // In the caller activity's handler, it will update the audio play progress.
    private Handler audioProgressUpdateHandler;

    // This is the message signal that inform audio progress updater to update audio progress.
    public final int UPDATE_AUDIO_PROGRESS_BAR = 1;

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

    public void setAudioProgressUpdateHandler(Handler audioProgressUpdateHandler) {
        this.audioProgressUpdateHandler = audioProgressUpdateHandler;
    }

    public Handler getAudioProgressUpdateHandler() {
        return audioProgressUpdateHandler;
    }

    private void initAudioPlayer() {

        try{
            if(player == null) {
                player = new MediaPlayer();
                player.setDataSource(getContext(), getAudioFileUrl());
                player.prepare();
                player.setOnCompletionListener((MediaPlayer.OnCompletionListener) context);

                // This thread object will send update audio progress message to caller activity every 1 second.
                Thread updateAudioProgressThread = new Thread()
                {
                    @Override
                    public void run() {
                        while(true)
                        {
                            // Create update audio progress message.
                            Message updateAudioProgressMsg = new Message();
                            updateAudioProgressMsg.what = UPDATE_AUDIO_PROGRESS_BAR;

                            // Send the message to caller activity's update audio prgressbar Handler object.
                            audioProgressUpdateHandler.sendMessage(updateAudioProgressMsg);

                            // Sleep one second.
                            try {
                                Thread.sleep(1000);
                            }catch(InterruptedException ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                    }
                };
                // Run above thread object.
                updateAudioProgressThread.start();
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

    public void resumeAudio()
    {
        if(player!=null) {
            length=player.getCurrentPosition();
            player.seekTo(length);
            player.start();
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




    // Return current audio play position.
    public int getCurrentAudioPosition()
    {
        int ret = 0;
        if(player != null)
        {
            ret = player.getCurrentPosition();
        }
        return ret;
    }

    // Return total audio file duration.
    public int getTotalAudioDuration()
    {
        int ret = 0;
        if(player != null)
        {
            ret = player.getDuration();
        }
        return ret;
    }

    // Return current audio player progress value.
    public int getAudioProgress()
    {
        int ret = 0;
        int currAudioPosition = getCurrentAudioPosition();
        int totalAudioDuration = getTotalAudioDuration();
        if(totalAudioDuration > 0) {
            ret = (currAudioPosition * 100) / totalAudioDuration;
        }
        return ret;
    }

}
