package com.example.app_sample.ui.intro;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.AutoTransition;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.ui.MainActivity;
import com.example.app_sample.ui.home.HomeFragment;
import com.example.app_sample.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    ImageView img;
    Handler handler;
    FirebaseAuth firebaseAuth;
    TextView txt;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        img = findViewById(R.id.logo);
        txt = findViewById(R.id.app_name);
        firebaseAuth = FirebaseAuth.getInstance();

        handleLink();

        Animation fade_down = AnimationUtils.loadAnimation(this, R.anim.fade_down);
        Animation fade_up = AnimationUtils.loadAnimation(this, R.anim.fade_up);

        img.startAnimation(fade_down);
        txt.startAnimation(fade_up);

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            getWindow().setExitTransition(new AutoTransition());
            if(firebaseAuth.getCurrentUser() == null)
                startActivity(new Intent(SplashScreen.this, IntroActivity.class));
            else
                startActivity(new Intent(SplashScreen.this, MainActivity.class).putExtra(Constants.RECIPE_KEY, str));
        }, 700);
    }

    private void handleLink() {
        Uri uri = getIntent().getData();
        if(uri != null){
            str = uri.toString();
        }

    }

}