package com.one.russell.metroman_20

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.one.russell.metroman_20.presentation.screens.main.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openMainFragment()
    }

    private fun openMainFragment() {
        val mainFragment = MainFragment()

        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.fragment_container,
                mainFragment,
                mainFragment.javaClass::getSimpleName.toString()
            ).commit()
    }
}