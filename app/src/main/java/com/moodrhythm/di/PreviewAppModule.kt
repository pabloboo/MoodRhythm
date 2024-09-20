package com.moodrhythm.di

import android.content.SharedPreferences
import com.moodrhythm.utils.MockSharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreviewAppModule {

    @Provides
    @Singleton
    fun provideFakeSharedPreferences(): SharedPreferences {
        // Fake SharedPreferences implementation for Preview
        return MockSharedPreferences()
    }
}
