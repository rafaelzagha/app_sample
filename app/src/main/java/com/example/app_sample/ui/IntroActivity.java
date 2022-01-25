package com.example.app_sample.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.app_sample.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;


public class IntroActivity extends AppCompatActivity {

    VideoView videoView;
    MediaPlayer mediaPlayer;
    int currentPosition;
    MaterialButton google, email;
    TextView login;

    int RC_SIGN_IN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        setupVideo();
        RC_SIGN_IN = 1;

        email = findViewById(R.id.sign_up_with_email);
        google = findViewById(R.id.sign_up_with_google);
        login = findViewById(R.id.log_in);

        email.setOnClickListener(v -> startActivity(new Intent(IntroActivity.this, SignUpActivity.class)));
        google.setOnClickListener(v -> googleSignUp());
        login.setOnClickListener(v -> startActivity(new Intent(IntroActivity.this, LoginActivity.class)));


        findViewById(R.id.title).setOnClickListener(v -> startActivity(new Intent(IntroActivity.this, MainActivity.class)));

    }

    void setupVideo(){
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("tag", "firebaseAuthWithGoogle: " + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
                Log.w("tag", "Google sign in failed", e);
            }
        }



    }

    private void firebaseAuthWithGoogle(String idToken) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("tag", "signInWithCredential:success");
                            FirebaseDatabase.getInstance().getReference("UserData").child(firebaseAuth.getCurrentUser().getUid()).child("username").setValue(firebaseAuth.getCurrentUser().getDisplayName());
                        } else {
                            Log.w("tag", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void googleSignUp(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            Log.d("tag", FirebaseAuth.getInstance().getUid() );
            Toast.makeText(this, "Already Logged in" + FirebaseAuth.getInstance().getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
        }

        else{
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(IntroActivity.this, gso);
            startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}