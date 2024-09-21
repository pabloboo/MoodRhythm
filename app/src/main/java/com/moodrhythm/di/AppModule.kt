package com.moodrhythm.di

import android.content.Context
import com.moodrhythm.utils.SharedPrefsConstants
import com.moodrhythm.utils.SharedPrefsHelper
import com.moodrhythm.utils.SharedPrefsHelperImpl
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
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPrefsHelper {
        val sharedPrefs = context.getSharedPreferences(SharedPrefsConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        return SharedPrefsHelperImpl(sharedPrefs)
    }
}
