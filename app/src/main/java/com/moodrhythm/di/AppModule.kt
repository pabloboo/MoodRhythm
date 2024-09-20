package com.moodrhythm.di

import android.content.Context
import com.moodrhythm.utils.SharedPreferencesHelper
import com.moodrhythm.utils.SharedPrefsConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferencesHelper {
        val sharedPrefs = context.getSharedPreferences(SharedPrefsConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        return SharedPreferencesHelper(sharedPrefs)
    }
}
