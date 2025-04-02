package com.example.paymeinternapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import com.example.paymeinternapp.screens.main.MainScreen
import com.example.paymeinternapp.ui.theme.PaymeInternAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaymeInternAppTheme {
                Navigator(screen = MainScreen())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TTT", "onDestroy: Activity kill")
    }
}
