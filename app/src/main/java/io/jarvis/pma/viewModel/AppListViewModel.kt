package io.jarvis.pma.viewModel

import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.AppUtils.AppInfo
import com.blankj.utilcode.util.IntentUtils
import com.blankj.utilcode.util.SPStaticUtils
import io.jarvis.pma.receiver.SysIntentReceiver
import io.jarvis.pma.viewModel.mvi.BaseViewModel
import io.jarvis.pma.viewModel.mvi.IUiIntent
import io.jarvis.pma.viewModel.mvi.IUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object AppListViewModel : BaseViewModel<AppListViewState, AppListIntent>() {

    /// 待保护包名
    val protectPackage = MutableStateFlow("")

    /// 是否保护
    val protectStatFlow = MutableStateFlow(true)

    init {
        loadInstalledApps()
        loadProtectPackage()
    }

    fun enableProtect() {
        protectStatFlow.update { true }
    }

    fun disableProtect() {
        protectStatFlow.update { false }
    }

    fun loadProtectPackage() = viewModelScope.launch(Dispatchers.IO) {
        SPStaticUtils.getString("protect_package")?.let { protectPackage.tryEmit(it) }
    }

    private fun loadInstalledApps() = viewModelScope.launch(Dispatchers.IO) {
        sendState(AppListViewState.Loading)
        try {
            val apps = getInstalledApps()
            sendState(AppListViewState.Success(apps))
        } catch (e: Exception) {
            sendState(AppListViewState.Error(e.message ?: "Unknown error"))
        }
    }

    private fun getInstalledApps(): List<AppInfo> {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            PermissionUtils.permission(android.Manifest.permission.QUERY_ALL_PACKAGES).request()
//            if (!PermissionUtils.isGranted(android.Manifest.permission.QUERY_ALL_PACKAGES))
//                return emptyList()
//        }
        return AppUtils.getAppsInfo().asSequence().sortedBy { it.name }
            .filter { null != IntentUtils.getLaunchAppIntent(it.packageName) }
            .filter { it.packageName != AppUtils.getAppPackageName() }
            .toList()
    }

    override fun initialState(): AppListViewState = AppListViewState.Loading

    override suspend fun handleEvent(intent: IUiIntent) {
        when (intent) {
            is AppListIntent.Refresh -> loadInstalledApps()
            is AppListIntent.LaunchApp -> {
                AppUtils.launchApp(intent.packageName)
            }

            is AppListIntent.Protect -> {
                protectPackage.tryEmit(intent.packageName)
                SPStaticUtils.put("protect_package", intent.packageName)
            }

            is AppListIntent.Install -> {
                //使用系统下载器下载文件，然后安装
                SysIntentReceiver.downloadAndInstallApk(intent.url)
            }

            is AppListIntent.Uninstall -> {
                AppUtils.uninstallApp(intent.packageName)
            }
        }
    }

}

sealed class AppListViewState : IUiState {
    object Loading : AppListViewState()
    data class Success(val apps: List<AppInfo>) : AppListViewState()
    data class Error(val message: String) : AppListViewState()
}

sealed class AppListIntent : IUiIntent {
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