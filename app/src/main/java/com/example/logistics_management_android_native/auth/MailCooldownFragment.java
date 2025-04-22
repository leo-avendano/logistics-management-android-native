package com.example.logistics_management_android_native.auth;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.logistics_management_android_native.R;
import com.example.logistics_management_android_native.utils.ToastUtil;

public class MailCooldownFragment extends Fragment {
    private EditText emailEditText;
    private ToastUtil toast;

    public MailCooldownFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mail_cooldown, container, false);

        Button continueButton = view.findViewById(R.id.continueButton);
        toast = new ToastUtil(requireContext());
        continueButton.setOnClickListener(v -> goToLogin());

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
