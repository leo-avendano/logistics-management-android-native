package com.example.logistics_management_android_native.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.logistics_management_android_native.R;
import com.example.logistics_management_android_native.home.HomeActivity;
import com.example.logistics_management_android_native.utils.NetworkUtils;
import com.example.logistics_management_android_native.utils.ToastMessage;
import com.example.logistics_management_android_native.utils.ToastUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private ToastUtil toast;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        toast = new ToastUtil(requireContext());

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        Button loginButton = view.findViewById(R.id.loginButton);
        TextView registerText = view.findViewById(R.id.registerText);

        loginButton.setOnClickListener(v -> loginUser());
        registerText.setOnClickListener(v -> goToRegister());

        return view;
    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            toast.showToast(ToastMessage.EMPTY_FIELDS);
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {

                    if (!NetworkUtils.isConnectedToInternet(requireContext())) {
                        toast.showToast(ToastMessage.NETWORK_FAIL);
                        return;
                    }

                    if (!task.isSuccessful()) {
                        // Usa esto si hay algun bug con el login.
                        // String message = Objects.requireNonNull(task.getException()).getMessage();
                        toast.showToast(ToastMessage.INVALID_LOGIN);
                        return;
                    }

                    FirebaseUser user = mAuth.getCurrentUser();

                    if (user == null) {
                        toast.showToast(ToastMessage.INVALID_LOGIN);
                        return;
                    }

                    if (!user.isEmailVerified()) {
                        toast.showToast(ToastMessage.EMAIL_NOT_CONFIRMED);
                        mAuth.signOut();
                        return;
                    }

                    startActivity(new Intent(requireContext(), HomeActivity.class));
                    requireActivity().finish();
                });
    }

    private void goToRegister() {
        requireActivity().getSupportFragmentManager()
                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.auth_fragment_container, new RegisterFragment())
                .addToBackStack(null)
                .commit();
    }
}