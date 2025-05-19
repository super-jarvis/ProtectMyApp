package io.jarvis.pma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import com.blankj.utilcode.util.ActivityUtils
import io.jarvis.pma.ui.AppListScreen
import io.jarvis.pma.ui.theme.ProtectMyAppTheme
import io.jarvis.pma.viewModel.AppListViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProtectMyAppTheme {
                MainView()
            }
        }
    }
}

@Composable
fun MainView() {
    val protectState by AppListViewModel.protectStatFlow.collectAsState()
    Scaffold(
        floatingActionButton = {
            Column {
                IconButton(onClick = {
                    if (protectState) AppListViewModel.disableProtect()
                    else AppListViewModel.enableProtect()
                }) {
                    Icon(
                        imageVector = if (protectState) Icons.Default.Close else Icons.Default.PlayArrow,
                        contentDescription = "启动暂停保护"
                    )
                }
                IconButton(onClick = { ActivityUtils.startActivity(SettingActivity::class.java) }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "启动器设置"
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            AppListScreen(AppListViewModel)
        }
    }
}