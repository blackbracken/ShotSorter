package black.bracken.shotsorter

import android.app.Application
import android.content.Context

class ShotSorter : Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext
    }

}