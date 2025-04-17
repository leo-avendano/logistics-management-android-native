package com.example.logistics_management_android_native.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.logistics_management_android_native.R;
import com.example.logistics_management_android_native.utils.ToastMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText, confirmPasswordEditText;

    // Constructor
    public RegisterFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText);

        Button registerButton = view.findViewById(R.id.registerButton);
        TextView loginText = view.findViewById(R.id.loginText);

        registerButton.setOnClickListener(v -> registerUser());
        loginText.setOnClickListener(v -> goToLogin());

        ImageButton eyeIcon = view.findViewById(R.id.eyeIcon);
        ImageButton eyeIconConfirm = view.findViewById(R.id.eyeIconConfirm);

        // Cambiar visualización de contraseña
        eyeIcon.setOnClickListener(v -> cambiarVistaPassword(passwordEditText));
        eyeIconConfirm.setOnClickListener(v ->  cambiarVistaPassword(confirmPasswordEditText));

        return view;
    }

    private void registerUser() {
        if (formHasErrors()) return;

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (!task.isSuccessful()) {
                        handleAuthError(task.getException());
                        return;
                    }

                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user == null) {
                        showToast(ToastMessage.REGISTER_UNKNOWN_FAIL);
                        return;
                    }

                    user.sendEmailVerification()
                            .addOnCompleteListener(verifyTask -> {
                                if (verifyTask.isSuccessful()) {
                                    showToast(ToastMessage.REGISTER_SUCCESS);
                                    mAuth.signOut();
                                    goToLogin();
                                } else {
                                    handleAuthError(verifyTask.getException());
                                }
                            });
                });
    }

    private void goToLogin() {
        requireActivity().getSupportFragmentManager()
                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.auth_fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordSecure(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!?.*()_\\-]).{8,}$";
        return password.matches(passwordPattern);
    }

    private void cambiarVistaPassword(EditText password) {
        int inputType = password.getInputType();
        int variation = inputType & InputType.TYPE_MASK_VARIATION;

        if (variation == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            // Mostrar password
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            // Ocultar password
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    private boolean formHasErrors() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            showToast(ToastMessage.EMPTY_FIELDS);
            return true;
        }

        if (!isValidEmail(email)) {
            showToast(ToastMessage.INVALID_EMAIL);
            return true;
        }

        if (!isPasswordSecure(password)) {
            showToast(ToastMessage.PASSWORD_TOO_WEAK);
            return true;
        }

        if (!password.equals(confirmPassword)) {
            showToast(ToastMessage.PASSWORD_MISMATCH);
            return true;
        }

        return false;
    }

    private void handleAuthError(Exception e) {
        if (e != null && e.getMessage() != null) {
            showToastConcat(ToastMessage.REGISTER_FAIL, e.getMessage());
        } else {
            showToast(ToastMessage.REGISTER_FAIL);
        }
    }

    private void showToast(ToastMessage message) {
        Toast.makeText(requireContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void showToastConcat(ToastMessage message, String additionalString) {
        Toast.makeText(requireContext(), message.getMessage() + additionalString, Toast.LENGTH_SHORT).show();
    }
}