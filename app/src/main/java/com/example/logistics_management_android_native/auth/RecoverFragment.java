package com.example.logistics_management_android_native.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.logistics_management_android_native.utils.ToastMessage;
import com.example.logistics_management_android_native.utils.ToastUtil;
import com.google.firebase.auth.FirebaseAuth;

public class RecoverFragment extends Fragment {

    private EditText emailEditText;
    private ToastUtil toast;

    public RecoverFragment() {}

    private static final long COOLDOWN_TIME_MS = 30000; // 1/2 minuto, cambiar si es necesario

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recover, container, false);

        TextView loginText = view.findViewById(R.id.loginText);
        emailEditText = view.findViewById(R.id.emailEditText);
        Button sendEmailButton = view.findViewById(R.id.recoverButton);
        toast = new ToastUtil(requireContext());

        loginText.setOnClickListener(v -> goToLogin());

        sendEmailButton.setOnClickListener(v -> recoverPassword());

        return view;
    }

    private void recoverPassword() {
        String email = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            toast.showToast(ToastMessage.EMPTY_FIELDS);
            return;
        }

        if (!isValidEmail(email)) {
            toast.showToast(ToastMessage.INVALID_EMAIL);
            return;
        }


        SharedPreferences prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        long lastSentTime = prefs.getLong("last_reset_email_time", 0);
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastSentTime < COOLDOWN_TIME_MS) {
            showCooldownFragment();
            return;
        }

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putLong("last_reset_email_time", System.currentTimeMillis());
                        editor.apply();

                        goToConfirmation();

                    } else {
                        if (task.getException() != null && task.getException().getMessage() != null) {
                            toast.showToastConcat(ToastMessage.RECOVER_FAIL, task.getException().getMessage());
                        } else {
                            toast.showToast(ToastMessage.RECOVER_FAIL);
                        }
                    }
                });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

    private void goToConfirmation() {
        requireActivity().getSupportFragmentManager()
                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.auth_fragment_container, new ConfirmationFragment())
                .addToBackStack(null)
                .commit();
    }

    private void showCooldownFragment(){
        requireActivity().getSupportFragmentManager()
                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.auth_fragment_container, new MailCooldownFragment())
                .addToBackStack(null)
                .commit();
    }

}
