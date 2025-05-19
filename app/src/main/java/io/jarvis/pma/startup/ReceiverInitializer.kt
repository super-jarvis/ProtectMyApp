package io.jarvis.pma.startup

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.startup.Initializer
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ShellUtils
import io.jarvis.pma.MainActivity
import io.jarvis.pma.receiver.MyAppReceiver
import io.jarvis.pma.receiver.PackageIntentReceiver
import io.jarvis.pma.receiver.TimeTickReceiver

class ReceiverInitializer : Initializer<Unit> {
    private val tag = "ReceiverInitializer"

    /**
     * 检查所有需要的权限，一次性申请
     */
    @SuppressLint("InlinedApi")
    private fun requestAllPermission() {
        PermissionUtils.permission(
            android.Manifest.permission.QUERY_ALL_PACKAGES,
            android.Manifest.permission.PACKAGE_USAGE_STATS,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.SYSTEM_ALERT_WINDOW,
            android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
        ).request()
    }

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

    private fun setHomeActivity(context: Context) {
        runCatching {
            //设置默认启动器
            ShellUtils.execCmd(
                "pm set-home-activity -a android.intent.action.MAIN -c android.intent.category.HOME ${AppUtils.getAppPackageName()}/.${MainActivity::class.simpleName}",
                AppUtils.isAppRoot()
            ).apply {
                if (errorMsg.isNullOrEmpty()) {
                    LogUtils.dTag(tag, "set-home-activity success")
                }
            }
        }
    }

    override fun create(context: Context) {
        LogUtils.d("初始化接收器")
        requestAllPermission()
        registerPackageChangeReceiver(context)
        registerTimeTickReceiver(context)
        registerMyAppReceiver(context)
        setHomeActivity(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> = emptyList()
}