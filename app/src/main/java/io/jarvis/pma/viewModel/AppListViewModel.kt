package io.jarvis.pma.viewModel

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.AppUtils.AppInfo
import com.blankj.utilcode.util.IntentUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.SPStaticUtils
import io.jarvis.pma.receiver.SysIntentReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object AppListViewModel : ViewModel() {

    private val _state = MutableStateFlow<AppListViewState>(AppListViewState.Loading)
    val state: StateFlow<AppListViewState> = _state

    private val _intent = MutableSharedFlow<AppListIntent>()
    val intent: SharedFlow<AppListIntent> = _intent

    /// 待保护包名
    val protectPackage = MutableStateFlow("")

    init {
        handleIntent()
        loadInstalledApps()
        loadProtectPackage()
    }

    fun loadProtectPackage() = viewModelScope.launch(Dispatchers.IO) {
        SPStaticUtils.getString("protect_package")?.let { protectPackage.tryEmit(it) }
    }

    fun onIntent(intent: AppListIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    private fun handleIntent() = viewModelScope.launch {
        intent.collect { appListIntent ->
            when (appListIntent) {
                is AppListIntent.Refresh -> loadInstalledApps()
                is AppListIntent.LaunchApp -> {
                    AppUtils.launchApp(appListIntent.packageName)
                }

                is AppListIntent.Protect -> {
                    protectPackage.tryEmit(appListIntent.packageName)
                    SPStaticUtils.put("protect_package", appListIntent.packageName)
                }

                is AppListIntent.Install -> {
                    //使用系统下载器下载文件，然后安装
                    SysIntentReceiver.downloadAndInstallApk(appListIntent.url)
                }

                is AppListIntent.Uninstall -> {
                    AppUtils.uninstallApp(appListIntent.packageName)
                }
            }
        }
    }

    private fun loadInstalledApps() = viewModelScope.launch(Dispatchers.IO) {
        _state.value = AppListViewState.Loading
        try {
            val apps = getInstalledApps()
            _state.value = AppListViewState.Success(apps)
        } catch (e: Exception) {
            _state.value = AppListViewState.Error(e.message ?: "Unknown error")
        }
    }

    private fun getInstalledApps(): List<AppInfo> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            PermissionUtils.permission(android.Manifest.permission.QUERY_ALL_PACKAGES).request()
            if (!PermissionUtils.isGranted(android.Manifest.permission.QUERY_ALL_PACKAGES))
                return emptyList()
        }
        return AppUtils.getAppsInfo().asSequence().sortedBy { it.name }
            .filter { null != IntentUtils.getLaunchAppIntent(it.packageName) }
            .filter { it.packageName != AppUtils.getAppPackageName() }
            .toList()
    }

}

sealed class AppListViewState {
    object Loading : AppListViewState()
    data class Success(val apps: List<AppInfo>) : AppListViewState()
    data class Error(val message: String) : AppListViewState()
}

sealed class AppListIntent {
    object Refresh : AppListIntent()

    /// 启动应用
    data class LaunchApp(val packageName: String) : AppListIntent()

    /// 保护应用
    data class Protect(val packageName: String) : AppListIntent()

    /// 安装应用
    data class Install(val url: String) : AppListIntent()

    /// 卸载应用
    data class Uninstall(val packageName: String) : AppListIntent()
}