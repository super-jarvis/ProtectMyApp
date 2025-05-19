package io.jarvis.pma.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ProcessUtils
import io.jarvis.pma.utils.DeviceTool
import io.jarvis.pma.viewModel.AppListViewModel

class TimeTickReceiver : BroadcastReceiver() {

    companion object {
        @Volatile
        var lastTime: Long = 0L
    }

    override fun onReceive(context: Context, intent: Intent) {
        runCatching {
            if (SystemClock.uptimeMillis() - lastTime < 1000) {
                LogUtils.d("并发了")
                return
            }
            lastTime = SystemClock.uptimeMillis()
            var isFront = false
            if (AppListViewModel.protectPackage.value.isNotBlank()) {
                isFront = DeviceTool.checkIsFront(AppListViewModel.protectPackage.value)
            }
            if (AppUtils.isAppDebug()) {
                LogUtils.d(
                    "守护app中 protecting=${AppListViewModel.protectStatFlow.value} " +
                            "packageName=${AppListViewModel.protectPackage.value} " +
                            "isFront=$isFront " +
                            "process=${ProcessUtils.getForegroundProcessName()}"
                )
            }
            if (AppListViewModel.protectStatFlow.value
                && AppListViewModel.protectPackage.value.isNotBlank()
                && !isFront && !AppUtils.isAppDebug()
            ) AppUtils.launchApp(AppListViewModel.protectPackage.value)
        }.onFailure {
            LogUtils.eTag("TimeTickReceiver", it)
        }
    }
}
