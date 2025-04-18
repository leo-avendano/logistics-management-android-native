package com.example.logistics_management_android_native.auth;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logistics_management_android_native.R;
import com.example.logistics_management_android_native.utils.NetworkUtils;
import com.example.logistics_management_android_native.utils.ToastUtil;
import com.example.logistics_management_android_native.utils.ToastMessage;


public class AuthActivity extends AppCompatActivity {
    private ToastUtil toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        toast = new ToastUtil(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.auth_fragment_container, new LoginFragment())
                    .commit();
        }

        if (!NetworkUtils.isConnectedToWifi(this)) {
            toast.showToast(ToastMessage.NETWORK_FAIL);
        }
    }
}