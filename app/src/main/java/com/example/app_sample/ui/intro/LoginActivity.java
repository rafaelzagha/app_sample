package com.example.app_sample.ui.intro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_sample.R;
import com.example.app_sample.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText inputEmail, inputPassword;
    private TextView tv_login;
    private TextInputLayout emailLayout, passwordLayout;
    private String email, password;
    private MaterialButton login;
    private TextView forgotPassword;
    private FirebaseAuth firebaseAuth;
    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.et_email);
        inputPassword = findViewById(R.id.et_password);
        emailLayout = findViewById(R.id.textfield_email);
        passwordLayout = findViewById(R.id.textfield_password);
        forgotPassword = findViewById(R.id.forgot_password);
        login = findViewById(R.id.login);
        tv_login = findViewById(R.id.title);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new CustomProgressDialog();

        addTextWatchers();


        login.setOnClickListener(v -> {

            if(passwordLayout.getVisibility() == View.VISIBLE){
                if (!validateEmail() | !validatePassword()) {
                    return;
                }

                progressDialog.show(getSupportFragmentManager(), "tag", "Signing in...");
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, getString(R.string.ui_signed_in), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Log.d("tag", task.getException().getMessage());
                            Snackbar.make(findViewById(android.R.id.content), task.getException().getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else{
                if(validateEmail())
                    progressDialog.show(getSupportFragmentManager(), "tag", "Sending email...");
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful())
                                Toast.makeText(LoginActivity.this, getString(R.string.ui_reset_link_sent), Toast.LENGTH_SHORT).show();
                            else
                                Snackbar.make(findViewById(android.R.id.content), task.getException().getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();

                        }
                    });
            }

        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResetPasswordScreen(passwordLayout.getVisibility() == View.VISIBLE);
            }
        });


    }

    private void addTextWatchers() {

        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    emailLayout.setError(getString(R.string.ui_required_field));
                else
                    emailLayout.setError(null);
            }
        });

        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    passwordLayout.setError(getString(R.string.ui_required_field));
                else
                    passwordLayout.setError(null);
            }
        });

    }

    private boolean validateEmail() {
        email = Objects.requireNonNull(inputEmail.getText()).toString().trim();
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError(getString(R.string.ui_required_field));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError(getString(R.string.ui_not_valid_email));
            return false;
        } else {
            emailLayout.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        password = Objects.requireNonNull(inputPassword.getText()).toString();
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError(getString(R.string.ui_required_field));
            return false;
        } else if (password.contains(" ")) {
            passwordLayout.setError(getString(R.string.ui_password_has_space));
            return false;
        } else if (inputPassword.length() < 6) {
            passwordLayout.setError(getString(R.string.ui_password_minimum));
            return false;
        } else {
            passwordLayout.setError(null);
            return true;
        }
    }



    private void setResetPasswordScreen(boolean set){
        if(set == (passwordLayout.getVisibility() == View.GONE)){
            return;
        }
        else{
            passwordLayout.setVisibility(set? View.GONE : View.VISIBLE);
            login.setText(set? "send email" : "Login");
            tv_login.setText(set? "Reset Password" : "Login");
            forgotPassword.setText(set? "Login" : "Forgot password?");
        }
    }
}