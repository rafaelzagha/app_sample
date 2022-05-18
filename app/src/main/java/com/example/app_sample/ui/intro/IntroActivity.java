package com.example.app_sample.ui.intro;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_sample.R;
import com.example.app_sample.data.remote.FirebaseManager;
import com.example.app_sample.ui.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

@SuppressWarnings("FieldCanBeLocal")
public class IntroActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaPlayer mediaPlayer;
    private int currentPosition;
    private MaterialButton google, email;
    private TextView login;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private CustomProgressDialog progressDialog;

    int RC_SIGN_IN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupVideo();
        }
        RC_SIGN_IN = 1;
        email = findViewById(R.id.sign_up_with_email);
        google = findViewById(R.id.sign_up_with_google);
        login = findViewById(R.id.log_in);
        progressDialog = new CustomProgressDialog();

        email.setOnClickListener(v -> startActivity(new Intent(IntroActivity.this, SignUpActivity.class)));
        google.setOnClickListener(v -> googleSignUp());
        login.setOnClickListener(v -> startActivity(new Intent(IntroActivity.this, LoginActivity.class)));

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try{
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            progressDialog.dismiss();
                            Toast.makeText(IntroActivity.this, "Google sign in failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    void setupVideo(){
        videoView = findViewById(R.id.video);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.final_video);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            videoView.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE);
        }
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(mp -> {
            mediaPlayer = mp;

            mediaPlayer.setLooping(true);
            if(currentPosition != 0){
                mediaPlayer.seekTo(currentPosition);
                mediaPlayer.start();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer != null)
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


    private void firebaseAuthWithGoogle(String idToken) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                        new FirebaseManager().setUsername(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());
                        startActivity(new Intent(IntroActivity.this, MainActivity.class));
                    } else {
                        Log.w("tag", "signInWithCredential:failure", task.getException());
                    }
                });
    }

    private void googleSignUp(){
        progressDialog.show(getSupportFragmentManager(), "dialog", "Signing in");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();

            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(IntroActivity.this, gso);
            activityResultLauncher.launch(googleSignInClient.getSignInIntent());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}