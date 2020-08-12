package com.mystartup.videoapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private VideoView myVideo;
    private MediaController mMediaController;
    private Button mPlayButton;
    private MediaPlayer mMediaPlayer;
    AudioManager mAudioManager;
    private SeekBar soundVolume;
    private Button mPlayMusic;
    private Button mPauseMusic;
    private SeekBar mSoundProgress;
    private Timer t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myVideo = findViewById(R.id.myVideo);
        //MediaController controls the video.like start and pause button.
        mMediaController = new MediaController(this);
        mPlayButton = findViewById(R.id.playVideo);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri mMyVideoURI = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video);

                myVideo.setVideoURI(mMyVideoURI);
                myVideo.setMediaController(mMediaController);
                mMediaController.setAnchorView(myVideo);
                myVideo.start();

            }
        });
        mPlayMusic = findViewById(R.id.play_music);
        mPauseMusic = findViewById(R.id.pause_music);
        mPlayMusic.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              mMediaPlayer.start();
                                              t = new Timer();
                                              t.scheduleAtFixedRate(new TimerTask() {
                                                  @Override
                                                  public void run() {

                                                      mSoundProgress.setProgress(mMediaPlayer.getCurrentPosition());

                                                  }
                                              }, 0, 5000);
                                          }


                                      });
        mPauseMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayer.pause();
                t.cancel();

            }
        });

        mMediaPlayer = MediaPlayer.create(this,R.raw.bensound);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //Stream Music is used as it is the music is used.
       final int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
       soundVolume = findViewById(R.id.sound_volume);
       soundVolume.setMax(maxVolume);
       soundVolume.setProgress(currentVolume);
       soundVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               if(b){
                   mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
               }
           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {




           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {

           }
       });
       mSoundProgress = findViewById(R.id.sound_progress);;
       mSoundProgress.setProgress(mMediaPlayer.getCurrentPosition());
       mSoundProgress.setMax(mMediaPlayer.getDuration());
       mSoundProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               mMediaPlayer.seekTo(i);

           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {
               mMediaPlayer.pause();

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {
               mMediaPlayer.start();

           }
       });

       mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
           @Override
           public void onCompletion(MediaPlayer mediaPlayer) {
               t.cancel();
           }
       });

    }
}