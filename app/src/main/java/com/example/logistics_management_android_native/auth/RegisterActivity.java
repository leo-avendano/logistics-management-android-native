package com.example.logistics_management_android_native.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.logistics_management_android_native.R;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword, erConfirmPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        EditText usernameEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        Button registerButton = findViewById(R.id.registerButton);
        TextView loginButton = findViewById(R.id.loginText);
        ImageButton eyeIcon = findViewById(R.id.eyeIcon);
        ImageButton eyeIconConfirm = findViewById(R.id.eyeIconConfirm);


        loginButton.setOnClickListener(view -> login());
        registerButton.setOnClickListener(v -> {
            String email = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            registrarUsuario(email, password, confirmPassword);
        });

        //cambiar visualización de contraseña
        eyeIcon.setOnClickListener(v -> {cambiarVistaPassword(passwordEditText); });

        eyeIconConfirm.setOnClickListener(v -> {cambiarVistaPassword(confirmPasswordEditText);});

    }

    private void registrarUsuario(String email, String password, String confirmPassword) {

        if (isValidEmail(email)) { // anda medio medio
            if(isPasswordSecure(password)) {
                if (password.equals(confirmPassword)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        user.sendEmailVerification().addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Verificación enviada. Revisa tu correo.", Toast.LENGTH_LONG).show();
                                                login();
                                                finish();

                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Error al enviar el correo de verificación", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(this, "Error en el registro: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                else Toast.makeText(this, "Las contraseñas ingresadas no coinciden", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "La contraseña no es segura", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(this, "El correo es inválido", Toast.LENGTH_SHORT).show();



    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordSecure(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!?.*()_\\-]).{8,}$";
        return password.matches(passwordPattern);
    }

    private void cambiarVistaPassword(EditText password) {
        if (password.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            // Muestra la contraseña
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        } else {
            // Oculta la contraseña
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    private void login(){
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}
