package com.example.logistics_management_android_native.auth;

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

        sendEmailButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                toast.showToast(ToastMessage.EMPTY_FIELDS);
                return;
            }

            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            toast.showToast(ToastMessage.REGISTER_SUCCESS);
                            requireActivity().getSupportFragmentManager().popBackStack(); // Volver a login
                        } else {
                            toast.showToast(ToastMessage.NETWORK_FAIL);
                        }
                    });
        });

        return view;
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

}
