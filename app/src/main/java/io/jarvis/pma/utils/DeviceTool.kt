package io.jarvis.pma.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.ShellUtils
import com.blankj.utilcode.util.Utils
import io.jarvis.pma.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

object DeviceTool {

    fun deviceId(): String {
        return DeviceUtils.getUniqueDeviceId()
    }

    fun exec(cmd: String): ShellUtils.CommandResult {
        val r = ShellUtils.execCmd(cmd, DeviceUtils.isDeviceRooted())
        LogUtils.d(cmd, r)
        return r
    }

    private fun launchApp(pkg: String) {
        exec("am start -n ${pkg}/${ActivityUtils.getLauncherActivity(pkg)}").apply {
            if (result != 0) {
                AppUtils.launchApp(pkg)
            }
        }
    }

    /**
     * 通过shell判断app是否在前台运行
     */
    fun checkIsFront(pkg: String): Boolean {
        return ShellUtils.execCmd(
            "dumpsys window | grep mCurrentFocus",
            AppUtils.isAppRoot()
        ).let {
            if (AppUtils.isAppDebug()) LogUtils.d("当前运行应用：", it)
            return it.successMsg.contains(pkg)
        }
//                || AppUtils.isAppForeground(pkg)//这个程序在后台的时候无法获取到数据
    }

    /**
     * 通过shell判断应用是否安装
     */
    private fun checkIsInstall(pkg: String): Boolean {
        return AppUtils.isAppInstalled(pkg) || exec("pm path $pkg").result == 0
    }

    /**
     * 截屏
     */
    fun screenCapture(): String {
        val path =
            PathUtils.getExternalDownloadsPath() + File.separator + "screen"
        FileUtils.createOrExistsDir(path)
        val f = path + File.separator + UUID.randomUUID()
            .toString() + ".jpg"
        val r = ShellUtils.execCmd("/system/bin/screencap -p $f", true)
        LogUtils.d(r)
        return f
    }


    private var delayJob: Job? = null

    //未安装检测计次，超过5次则自动安装
    private var notInstallCount = 0

    fun checkApp(pkg: String?, delay: Long = 1000) {
        pkg?.let {
            delayJob?.cancel()
//            delayJob = CoroutineScope(Dispatchers.IO).launch {
//                delay(delay)
//                if (SysIntentReceiver.downloadAndInstall) return@launch
//                val isRun = checkIsFront(pkg)
//                if (!isRun) {
//                    val isInstall = checkIsInstall(pkg)
//                    if (!isInstall) {
//                        if (notInstallCount++ >= 5) {
//                            LogUtils.d("$pkg 程序未安装，启动安装")
//                            SPStaticUtils.getString(AppConstants.INTENT_SET_BASE, null)?.let {
//                                ApiUtils.downloadAndInstallApk(it)
//                            }
//                        } else {
//                            LogUtils.d("$pkg 程序未安装，等待计次$notInstallCount")
//                        }
//                    } else {
//                        notInstallCount = 0
//                        LogUtils.d("$pkg 程序未启动，启动")
//                        launchApp(pkg)
//                    }
//                }
//                delayJob = null
//            }
        }
    }

    fun delayStartApp(pkg: String?, delay: Long = 1000) = CoroutineScope(Dispatchers.IO).launch {
        pkg?.let {
            delay(delay)
            launchApp(pkg)
        }
    }

    fun delayStartSelf(delay: Long = 30000) {
        val app = Utils.getApp()
        val am = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(app, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(app, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        am.set(AlarmManager.RTC, System.currentTimeMillis() + delay, pendingIntent)
    }
}
