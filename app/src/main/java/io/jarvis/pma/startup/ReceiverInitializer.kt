package io.jarvis.pma.startup

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.startup.Initializer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import io.jarvis.pma.receiver.MyAppReceiver
import io.jarvis.pma.receiver.PackageIntentReceiver
import io.jarvis.pma.receiver.TimeTickReceiver


class ReceiverInitializer : Initializer<Unit> {
    private val tag = "ReceiverInitializer"

    /**
     * 注册包安装卸载广播
     */
    private fun registerPackageChangeReceiver(context: Context) {
        val packageChangeFilter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_CHANGED)
            addDataScheme("package")
        }
        ContextCompat.registerReceiver(
            context, PackageIntentReceiver(), packageChangeFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    /**
     * 注册时间变化广播
     */
    private fun registerTimeTickReceiver(context: Context) {
        val timeFilter = IntentFilter().apply {
            addAction(Intent.ACTION_TIME_TICK)
        }
        ContextCompat.registerReceiver(
            context, TimeTickReceiver(), timeFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    /**
     * 注册自定义广播
     */
    private fun registerMyAppReceiver(context: Context) {
        val myAppFilter = IntentFilter().apply {
            addAction(MyAppReceiver.MY_APP_INTENT)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.registerReceiver(
                context,
                MyAppReceiver(),
                myAppFilter,
                ContextCompat.RECEIVER_EXPORTED
            )
        } else {
            ContextCompat.registerReceiver(
                context,
                MyAppReceiver(),
                myAppFilter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
    }

    override fun create(context: Context) {
        LogUtils.d("执行初始化任务")
        registerPackageChangeReceiver(context)
        registerTimeTickReceiver(context)
        registerMyAppReceiver(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> = emptyList()
}