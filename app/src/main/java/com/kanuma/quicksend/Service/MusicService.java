package com.kanuma.quicksend.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.kanuma.quicksend.Constants.C;

import androidx.annotation.Nullable;

public class MusicService extends Service {

    private final IBinder mBinder = new MusicServiceBinder();


    @Override
    public void onCreate() {
        super.onCreate();




    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }








}
