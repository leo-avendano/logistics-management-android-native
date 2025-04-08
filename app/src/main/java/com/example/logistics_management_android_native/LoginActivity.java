package com.example.logistics_management_android_native;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logistics_management_android_native.auth.RegisterActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        EditText usernameEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        TextView registerButton = findViewById(R.id.registerText);

        loginButton.setOnClickListener(view -> loginUser());
        registerButton.setOnClickListener(view -> register());
    }
    private void loginUser() {
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user != null && user.isEmailVerified()) {
                            // Redirigir a HomeActivity
                            startActivity(new Intent(this, HomeActivity.class));
                            finish();
                        } else {
                            // Usuario no verificado por mail
                            Toast.makeText(this, "Debe verificar su correo electrónico antes de ingresar", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                        }

                    } else {
                        Toast.makeText(this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void register() {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
