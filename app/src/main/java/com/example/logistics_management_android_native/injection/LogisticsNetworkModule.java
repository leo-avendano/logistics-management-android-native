package com.example.logistics_management_android_native.injection;

import android.content.Context;

import com.example.logistics_management_android_native.data.interceptors.AuthInterceptor;
import com.example.logistics_management_android_native.data.interfaces.LogisticsApiService;
import com.example.logistics_management_android_native.data.interfaces.LogisticsService;
import com.example.logistics_management_android_native.data.services.LogisticsRetrofitService;
import com.google.firebase.auth.FirebaseAuth;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import java.io.File;

@Module
@InstallIn(SingletonComponent.class)
public class LogisticsNetworkModule {
    @Provides
    @Singleton
    Cache provideCache(@ApplicationContext Context context) {
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        File cacheDir = new File(context.getCacheDir(), "http-cache");
        return new Cache(cacheDir, cacheSize);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache, AuthInterceptor authInterceptor) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(authInterceptor)
                .cache(cache)
                .addNetworkInterceptor(chain -> chain.proceed(chain.request())
                        .newBuilder()
                        .header("Cache-Control", "public, max-age=60") // Cache por 60 segundos
                        .build())
                .build();
    }

    @Provides
    @Singleton
    AuthInterceptor provideAuthInterceptor(FirebaseAuth firebaseAuth) {
        return new AuthInterceptor(firebaseAuth);
    }

    @Provides
    @Singleton
    FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://logistics-management-26709.web.app/api/")
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    LogisticsApiService provideLogisticsApiService(Retrofit retrofit) {
        return retrofit.create(LogisticsApiService.class);
    }

    @Provides
    @Singleton
    LogisticsService provideLogisticsService(LogisticsRetrofitService logisticsRetrofitService) {
        return logisticsRetrofitService;
    }
}