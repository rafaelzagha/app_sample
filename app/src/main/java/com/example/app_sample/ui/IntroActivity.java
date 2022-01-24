package com.example.app_sample.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.example.app_sample.R;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;

import pl.droidsonroids.gif.GifImageView;

public class IntroActivity extends AppCompatActivity {

    VideoView videoView;
    MediaPlayer mediaPlayer;
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        videoView = findViewById(R.id.video);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.final_video);

        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer = mp;

                mediaPlayer.setLooping(true);
                if(currentPosition != 0){
                    mediaPlayer.seekTo(currentPosition);
                    mediaPlayer.start();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentPosition = mediaPlayer.getCurrentPosition();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}