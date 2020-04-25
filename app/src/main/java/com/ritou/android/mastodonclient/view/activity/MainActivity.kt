package com.ritou.android.mastodonclient.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ritou.android.mastodonclient.R
import com.ritou.android.mastodonclient.view.fragment.TootListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val fragment =
                TootListFragment()
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container,
                    fragment,
                    TootListFragment.TAG
                )
                .commit()
        }
     }
}
