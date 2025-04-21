package com.example.logistics_management_android_native.data.interceptors;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.Tasks;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final FirebaseAuth firebaseAuth;

    public AuthInterceptor(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            return chain.proceed(originalRequest);
        }

        try {
            // Wait for the token with a timeout
            String token = Tasks.await(currentUser.getIdToken(true), 10, TimeUnit.SECONDS).getToken();
            
            if (token != null) {
                Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
                return chain.proceed(newRequest);
            }
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            // If we can't get the token, proceed with the original request
            return chain.proceed(originalRequest);
        }

        return chain.proceed(originalRequest);
    }
} 