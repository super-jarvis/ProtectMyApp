package io.jarvis.pma

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import io.jarvis.pma.ui.AppListScreen
import io.jarvis.pma.ui.theme.ProtectMyAppTheme
import io.jarvis.pma.viewModel.AppListViewModel

class MainActivity : ComponentActivity() {

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { checkIfDefaultLauncher() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProtectMyAppTheme {
                MainView()
            }
        }
        requestAllPermission()
        checkIfDefaultLauncher()
    }

    private fun checkIfDefaultLauncher() {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }
        val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        LogUtils.d("当前默认启动器: ${resolveInfo?.activityInfo?.packageName}")
        val isDefault = resolveInfo?.activityInfo?.packageName == packageName
        if (!isDefault) {
            // 当前不是默认启动器，请求设置为默认
            launcher.launch(Intent(Settings.ACTION_HOME_SETTINGS))
        }
    }

    /**
     * 检查所有需要的权限，一次性申请
     */
    private fun requestAllPermission() {
        PermissionUtils.permissionGroup(
            PermissionConstants.STORAGE,
            PermissionConstants.PHONE,
        ).request()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            PermissionUtils.permission(
                android.Manifest.permission.QUERY_ALL_PACKAGES,
                android.Manifest.permission.PACKAGE_USAGE_STATS
            ).request()
        }
//        val packagePermission = PermissionUtils.getPermissions()
//        LogUtils.d("app需要的权限清单: $packagePermission")
//        packagePermission.filter { it.startsWith("android") }.forEach {
//            if (!PermissionUtils.isGranted(it)) PermissionUtils.permission(it).request()
//        }
    }
}

@Composable
fun MainView() {
    Scaffold(
        floatingActionButton = { FloatButtons() }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            AppListScreen(AppListViewModel)
        }
    }
}

@Composable
fun FloatButtons() {
    val protectState by AppListViewModel.protectStatFlow.collectAsState()
    var hidden by remember { mutableStateOf(true) }
    AnimatedVisibility(visible = !hidden, enter = expandIn(), exit = shrinkOut()) {
        if (!hidden)
            Column(horizontalAlignment = Alignment.End) {
                if (protectState)
                    LineButton(
                        "暂停保护",
                        Icons.Filled.Warning
                    ) { AppListViewModel.disableProtect() }
                else
                    LineButton(
                        "启动保护",
                        Icons.Filled.PlayArrow
                    ) { AppListViewModel.enableProtect() }

                LineButton(
                    "设置",
                    Icons.Filled.Settings
                ) { ActivityUtils.startActivity(SettingActivity::class.java) }

                LineButton("隐藏菜单", Icons.Filled.Close) { hidden = true }
            }
    }
    AnimatedVisibility(visible = hidden, enter = expandIn(), exit = shrinkOut()) {
        if (hidden)
            IconButton(onClick = { hidden = false }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "显示菜单"
                )
            }
    }
}

@Composable
fun LineButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
        Text(text)
        IconButton(onClick) {
            Icon(
                imageVector = icon,
                contentDescription = text
            )
        }
    }
}