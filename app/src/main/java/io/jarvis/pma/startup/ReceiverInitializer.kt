package io.jarvis.pma.startup

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.startup.Initializer
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ShellUtils
import io.jarvis.pma.MainActivity
import io.jarvis.pma.receiver.PackageChangeReceiver
import io.jarvis.pma.receiver.TimeTickReceiver

class ReceiverInitializer : Initializer<Unit> {
    private val tag = "ReceiverInitializer"
    override fun create(context: Context) {
        LogUtils.d("初始化接收器")
        val packageChangeFilter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_CHANGED)
            addDataScheme("package")
        }
        context.registerReceiver(PackageChangeReceiver(), packageChangeFilter)

        val timeFilter = IntentFilter().apply {
            addAction(Intent.ACTION_TIME_TICK)
        }
        context.registerReceiver(TimeTickReceiver(), timeFilter)

        runCatching {
            //设置默认启动器
            ShellUtils.execCmd(
                "pm set-home-activity -a android.intent.action.MAIN -c android.intent.category.HOME ${AppUtils.getAppPackageName()}/.${MainActivity::class.simpleName}",
                AppUtils.isAppRoot()
            ).apply {
                if (errorMsg.isNullOrEmpty()) {
                    LogUtils.dTag(tag, "set-home-activity success")
                } else {
                    LogUtils.dTag(tag, "set-home-activity fail $errorMsg")
                }
            }
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> = emptyList()
}