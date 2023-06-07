package com.test.digitec

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import com.test.digitec.data.setup.di.dataModule
import com.test.digitec.domain.setup.di.domainModule
import com.test.digitec.presentation.setup.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "onCreate: CompressorApplication!")

        startKoin {
            androidLogger()
            androidContext(this@Application)
            modules(
                listOf(
                    dataModule,
                    domainModule,
                    presentationModule
                )
            )
        }
    }
}