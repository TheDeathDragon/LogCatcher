package com.sunritel.logcatcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sunritel.logcatcher.fragment.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.log_catcher_settings, SettingsFragment()).commit()
        }
    }
}