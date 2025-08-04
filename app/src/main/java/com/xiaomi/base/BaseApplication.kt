package com.xiaomi.base

import androidx.multidex.MultiDexApplication
import androidx.profileinstaller.ProfileInstaller
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        // Initialize logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Initialize ProfileInstaller for baseline profiles
        ProfileInstaller.writeProfile(this)

        // LeakCanary is automatically initialized in debug builds
        // No manual setup required
    }
}
