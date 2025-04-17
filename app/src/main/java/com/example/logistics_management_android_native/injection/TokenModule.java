package com.example.logistics_management_android_native.injection;

import android.content.Context;
import com.example.logistics_management_android_native.data.repository.token.TokenRepository;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class TokenModule {

    @Provides
    @Singleton
    public TokenRepository provideTokenRepository(@ApplicationContext Context context) {
        return new TokenRepository(context);
    }
}