package com.kanuma.quicksend.Fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.kanuma.quicksend.Constants.C;
import com.kanuma.quicksend.R;
import com.kanuma.quicksend.Service.MusicService;
import com.kanuma.quicksend.Service.MusicServiceBinder;

import java.io.File;
import java.io.Serializable;
import java.util.Queue;


public class MusicFragment extends Fragment {

    private Queue<File> files;
    private MusicServiceBinder musicServiceBinder;
    private Uri path;
    private File musicFile;

    private boolean isServiceBound =false;

    private LottieAnimationView stopButton;

    public MusicFragment() {
        // Required empty public constructor
    }


    public static MusicFragment getMusicQueue(Queue<File> fileList) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putSerializable(C.MUSIC_QUEUE, (Serializable) fileList);
        fragment.setArguments(args);
        return fragment;
    }


    public static MusicFragment playMusic(File file) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putString(C.MUSIC_URI,file.getAbsolutePath());
        args.putSerializable(C.MUSIC_FILE,file);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){
            path = Uri.parse(getArguments().getString(C.MUSIC_URI));
            musicFile = (File) getArguments().getSerializable(C.MUSIC_FILE);
        }


        /*
        if (getArguments() != null) {
            files = (Queue<File>) getArguments().getSerializable(C.MUSIC_QUEUE);
        }*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_music, container, false);
        stopButton =v.findViewById(R.id.stop);
        stopButton.setOnClickListener(stopMusicFunction);
        doBindService();


        return v;
    }

    View.OnClickListener stopMusicFunction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            musicServiceBinder.stopAudio();
        }
    };


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicServiceBinder = (MusicServiceBinder) service;
            if(musicServiceBinder!=null)
            {
                Toast.makeText(getActivity(),
                        "Here Yet",
                        Toast.LENGTH_SHORT).show();
                musicServiceBinder.setContext(getActivity());
                musicServiceBinder.setAudioFileUrl(path);
                musicServiceBinder.startAudio();
            }
            Toast.makeText(getActivity(),R.string.local_service_connected,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicServiceBinder = null;
            Toast.makeText(getActivity(),
                    R.string.local_service_disconnected,
                    Toast.LENGTH_SHORT).show();
        }
    };


    void doBindService(){

        Intent i =new Intent(getActivity(),MusicService.class);
        getActivity().bindService(i,mConnection, Context.BIND_AUTO_CREATE);
        isServiceBound =true;

    }

    void doUnBindService(){
        if (isServiceBound) {
            // Detach our existing connection.
            getActivity().unbindService(mConnection);
            isServiceBound = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        doUnBindService();
    }
}
