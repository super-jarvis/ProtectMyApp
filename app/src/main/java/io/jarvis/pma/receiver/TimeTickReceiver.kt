package io.jarvis.pma.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import java.util.concurrent.atomic.AtomicBoolean

class TimeTickReceiver : BroadcastReceiver() {

    companion object {
        @Volatile
        var lastTime: Long = 0L

        //// 是否在监听
        var watching = AtomicBoolean(true)

        fun enableWatching() {
            watching.set(true)
        }

        fun disableWatching() {
            watching.set(false)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        runCatching {
            if (SystemClock.uptimeMillis() - lastTime < 1000) {
                LogUtils.d("并发了")
                return
            }
            lastTime = SystemClock.uptimeMillis()
            if (AppUtils.isAppDebug()) {
                LogUtils.d("保活app中 ${intent.action}")
            }
            // TODO: 2022/3/28 添加保活逻辑
            if (watching.get()) {

            }
        }.onFailure {

        }
    }
}
