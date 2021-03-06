package com.example.app_sample.ui.intro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_sample.R;
import com.example.app_sample.data.remote.FirebaseManager;
import com.example.app_sample.ui.MainActivity;
import com.example.app_sample.utils.CustomProgressDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

@SuppressWarnings("FieldCanBeLocal")
public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText inputUsername, inputEmail, inputPassword;
    private TextInputLayout usernameLayout, emailLayout, passwordLayout;
    private MaterialButton signup;
    private FirebaseAuth firebaseAuth;
    private String username, email, password;
    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        inputUsername = findViewById(R.id.et_username);
        inputEmail = findViewById(R.id.et_email);
        inputPassword = findViewById(R.id.et_password);
        usernameLayout = findViewById(R.id.textfield_username);
        emailLayout = findViewById(R.id.textfield_email);
        passwordLayout = findViewById(R.id.textfield_password);
        signup = findViewById(R.id.signup);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new CustomProgressDialog();

        addTextWatchers();

        signup.setOnClickListener(v -> {
            if (!validateUsername() | !validateEmail() | !validatePassword()) {
                return;
            }
            progressDialog.show(getSupportFragmentManager(), "tag", "Signing up...");
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "User created Successfully", Toast.LENGTH_LONG).show();
                    new FirebaseManager().setUsername(username).addOnCompleteListener(task1 -> {
                        progressDialog.dismiss();
                        if (!task1.isSuccessful())
                            Toast.makeText(getApplicationContext(), "Username save failure", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    });
                } else {
                    String errorMessage = Objects.requireNonNull(task.getException()).toString();
                    Snackbar.make(findViewById(android.R.id.content), errorMessage, BaseTransientBottomBar.LENGTH_LONG).show();
                }
            });

        });


    }

    private void addTextWatchers() {

        inputUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    usernameLayout.setError(getString(R.string.ui_required_field));
                else
                    usernameLayout.setError(null);
            }
        });

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


    private boolean validateUsername() {
        username = Objects.requireNonNull(Objects.requireNonNull(inputUsername.getText()).toString().trim());
        if (TextUtils.isEmpty(username)) {
            usernameLayout.setError(getString(R.string.ui_required_field));
            return false;
        } else {
            emailLayout.setError(null);
            return true;
        }
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}