package io.jarvis.pma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.jarvis.pma.ui.theme.ProtectMyAppTheme

class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProtectMyAppTheme {
                SettingMainLayout()
            }
        }
    }
}

@Composable
fun SettingMainLayout() {
    Scaffold() { padding ->
        Box(Modifier.padding(padding)) {

        }
    }
}