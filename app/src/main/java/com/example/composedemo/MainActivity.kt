package com.example.composedemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.composedemo.ui.list.CurrencyListScreen
import com.example.composedemo.ui.list.CurrencyListViewModel
import com.example.composedemo.ui.theme.ComposeDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: CurrencyListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("DEBUG", "BASE_URL: ${BuildConfig.BASE_URL}")
        Log.d("DEBUG", "API_KEY: ${BuildConfig.CURRENCY_API_KEY}")
        setContent {
            ComposeDemoTheme {
                CurrencyListScreen(viewModel)
            }
        }
    }

}
