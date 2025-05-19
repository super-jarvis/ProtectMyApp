package io.jarvis.pma.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import io.jarvis.pma.utils.DeviceTool
import io.jarvis.pma.viewModel.AppListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimeTickReceiver : BroadcastReceiver() {

    companion object {
        @Volatile
        var lastTime: Long = 0L
        private val coroutineScope = CoroutineScope(Dispatchers.IO)
    }

    override fun onReceive(context: Context, intent: Intent) {
        runCatching {
            if (SystemClock.uptimeMillis() - lastTime < 1000) {
                LogUtils.d("并发了")
                return
            }
            lastTime = SystemClock.uptimeMillis()
            if (AppUtils.isAppDebug()) {
                LogUtils.d("守护app中...")
            }
            coroutineScope.launch {
                AppListViewModel.protectStatFlow.collect {
                    if (AppUtils.isAppDebug()) {
                        LogUtils.d("保活app中 $it")
                    }
                    if (it) DeviceTool.checkIsFront(AppListViewModel.protectPackage.value)
                }
            }
        }.onFailure {

        }
    }
}
