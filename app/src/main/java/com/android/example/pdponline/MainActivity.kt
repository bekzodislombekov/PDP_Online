package com.android.example.pdponline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.navigation.Navigation

class MainActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    private var count: Int = 0

    private var pressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.my_nav_host_fragment).navigateUp()
    }

    override fun onBackPressed() {
        count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            if (pressedOnce) {
                super.onBackPressed()
                return
            }
            handler = Handler(Looper.getMainLooper())
            pressedOnce = true
            Toast.makeText(this, "Chiqish uchun yana bir bor bosing!", Toast.LENGTH_SHORT).show()
            handler.postDelayed({
                pressedOnce = false
            }, 2000)
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}