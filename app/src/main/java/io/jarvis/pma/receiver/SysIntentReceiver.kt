package io.jarvis.pma.receiver

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import androidx.core.net.toUri
import kotlin.collections.set

class SysIntentReceiver : BroadcastReceiver() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    companion object {

        /// 下载中的链接
        private val downloadingUrls = mutableSetOf<String>()
        private val fileNameToUrl = mutableMapOf<String, String>()

        /**
         * 使用系统的下载服务
         */
        fun downloadAndInstallApk(url: String) {
            try {
                if (downloadingUrls.contains(url)) {
                    LogUtils.d("已经在下载了 $url")
                    return
                }
                // 指定下载地址
                val request = DownloadManager.Request(url.toUri())
                // 设置通知的显示类型，下载进行时和完成后显示通知
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                val fileName = url.substringAfterLast("/")
                // 设置通知的标题和描述
                request.setTitle("正在下载")
                request.setDescription("下载文件: $fileName")

                // 设置下载文件的保存位置
                val saveFile = File(PathUtils.getExternalDownloadsPath(), fileName)
                request.setDestinationUri(Uri.fromFile(saveFile))
                fileNameToUrl[saveFile.path] = url

                val downloadManager =
                    Utils.getApp().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                // 添加一个下载任务
                val downloadId = downloadManager.enqueue(request)
                LogUtils.d("添加下载任务：$downloadId")
            } catch (e: Exception) {
                LogUtils.e("添加下载任务异常", e)
                downloadingUrls.remove(url)
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        runCatching {
            LogUtils.d("收到系统广播 ${intent.action}")
            when (intent.action) {
                Intent.ACTION_BOOT_COMPLETED -> AppUtils.launchApp(AppUtils.getAppPackageName())

                DownloadManager.ACTION_DOWNLOAD_COMPLETE -> {
                    TimeTickReceiver.disableWatching()
                    val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
                    LogUtils.d("系统下载完成通知 $downloadId")
                    val manager =
                        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                    // 创建一个查询对象
                    val query = DownloadManager.Query()
                    // 根据 下载ID 过滤结果
                    query.setFilterById(downloadId)
                    // 执行查询, 返回一个 Cursor (相当于查询数据库)
                    val cursor = manager.query(query);

                    if (!cursor.moveToFirst()) {
                        cursor.close();
                        return;
                    }

                    // 下载请求的状态
                    val status =
                        cursor.getInt(
                            cursor.getColumnIndex(DownloadManager.COLUMN_STATUS).coerceAtLeast(0)
                        )
                    val localFilename = cursor.getString(
                        cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI).coerceAtLeast(0)
                    )
                    val downloadSize = cursor.getString(
                        cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                            .coerceAtLeast(0)
                    )
                    val totalSize = cursor.getString(
                        cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                            .coerceAtLeast(0)
                    )
                    cursor.close()
                    LogUtils.d("下载文件信息 $downloadId $status $localFilename $downloadSize $totalSize")
                    if (status == DownloadManager.STATUS_SUCCESSFUL && localFilename.endsWith(".apk")) {
                        doInstall(localFilename)
                    }
                    downloadingUrls.remove(fileNameToUrl[localFilename])
                    fileNameToUrl.remove(localFilename)
                }

                else -> {}
            }
        }.onFailure {
            TimeTickReceiver.enableWatching()
        }
    }

    private fun doInstall(localFilename: String) = coroutineScope.launch {
        try {
            val filePath = localFilename.replace("file://", "")
            AppUtils.installApp(filePath)
            delay(10000)
        } catch (e: Exception) {
            LogUtils.e(e)
        } finally {
            TimeTickReceiver.enableWatching()
        }
    }
}