package com.kanuma.quicksend.Fragments;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.kanuma.quicksend.Constants.C;
import com.kanuma.quicksend.R;
import com.kanuma.quicksend.Service.MusicService;
import com.kanuma.quicksend.Service.MusicServiceBinder;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


public class MusicFragment extends Fragment implements MediaPlayer.OnCompletionListener {

    private ArrayList<File> files;
    private MusicServiceBinder musicServiceBinder;
    private Uri path;
    private File musicFile;
    private Integer index;
    private Handler audioProgressUpdateHandler = null;
    private ProgressBar progressBar;

    private boolean isServiceBound =false;

    private ImageView stopButton;
    private ImageView nextButton;
    private ImageView prevButton;
    private TextView songName;
    private TextView songArtist;
    private Boolean isPlaying =true;


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


    public static MusicFragment playMusic(File file, List<File> fileDetailsList) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putString(C.MUSIC_URI,file.getAbsolutePath());
        args.putSerializable(C.MUSIC_FILE,file);
        args.putSerializable(C.MUSIC_QUEUE, (Serializable) fileDetailsList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){
            path = Uri.parse(getArguments().getString(C.MUSIC_URI));
            musicFile = (File) getArguments().getSerializable(C.MUSIC_FILE);
            files= (ArrayList<File>) getArguments().getSerializable(C.MUSIC_QUEUE);
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
        progressBar=v.findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        progressBar.setProgress(0);
        prevButton=v.findViewById(R.id.prev);
        nextButton=v.findViewById(R.id.next);
        songName=v.findViewById(R.id.songName);
        songArtist=v.findViewById(R.id.songArtist);

        //Listeners
        stopButton.setOnClickListener(stopMusicFunction);
        nextButton.setOnClickListener(nextMusicFunction);
        prevButton.setOnClickListener(prevMusicFunction);
        doBindService();


        return v;
    }

    View.OnClickListener stopMusicFunction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(isPlaying) {
                stopButton.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                isPlaying =false;
                musicServiceBinder.pauseAudio();
            }else{
                musicServiceBinder.resumeAudio();
                stopButton.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
                isPlaying = true;
            }


        }
    };
    View.OnClickListener nextMusicFunction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = (files.indexOf(musicFile)+1)%files.size();
            musicFile =files.get(i);
            Toast.makeText(getActivity(),""+files.size(),Toast.LENGTH_SHORT).show();
            path = Uri.parse(files.get(i).getAbsolutePath());
            progressBar.setProgress(0);
            musicServiceBinder.stopAudio();
            stopButton.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
            musicServiceBinder.setAudioFileUrl(path);
            musicServiceBinder.startAudio();
        }
    };


    View.OnClickListener prevMusicFunction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = (files.indexOf(musicFile)-1);
            if(i<0){progressBar.setProgress(0);
                stopButton.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                musicServiceBinder.stopAudio();
                return;
            }
            musicFile =files.get(i);
            Toast.makeText(getActivity(),""+files.get(i).getAbsolutePath(),Toast.LENGTH_SHORT).show();
            path = Uri.parse(files.get(i).getAbsolutePath());
            progressBar.setProgress(0);
            musicServiceBinder.stopAudio();
            stopButton.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
            musicServiceBinder.setAudioFileUrl(path);
            musicServiceBinder.startAudio();
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
                // Start audio in background service.
                path = Uri.parse(musicFile.getAbsolutePath());
                Toast.makeText(getActivity(),""+path,Toast.LENGTH_SHORT).show();
                musicServiceBinder.setAudioFileUrl(path);
                musicServiceBinder.startAudio();
                // Initialize audio progress bar updater Handler object.
                createAudioProgressbarUpdater();
                musicServiceBinder.setAudioProgressUpdateHandler(audioProgressUpdateHandler);



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



    // Create audio player progressbar updater.
    // This updater is used to update progressbar to reflect audio play process.
    private void createAudioProgressbarUpdater()
    {
        /* Initialize audio progress handler. */
        if(audioProgressUpdateHandler==null) {
            audioProgressUpdateHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // The update process message is sent from AudioServiceBinder class's thread object.
                    if (msg.what == musicServiceBinder.UPDATE_AUDIO_PROGRESS_BAR) {

                        if( musicServiceBinder != null) {
                            // Calculate the percentage.
                            int currProgress =musicServiceBinder.getAudioProgress();

                            // Update progressbar. Make the value 10 times to show more clear UI change.
                            progressBar.setProgress(currProgress*10);
                        }
                    }
                }
            };
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        int i = (files.indexOf(musicFile)+1)%files.size();
        path = Uri.parse(files.get(i).getAbsolutePath());
        musicServiceBinder.pauseAudio();
        musicServiceBinder.setAudioFileUrl(path);
        musicServiceBinder.startAudio();
    }
}
