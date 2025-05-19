package io.jarvis.pma.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.LogUtils
import io.jarvis.pma.viewModel.AppListIntent
import io.jarvis.pma.viewModel.AppListViewModel

class PackageIntentReceiver() : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        LogUtils.dTag("PackageChangeReceiver", "onReceive ${intent.action} ${intent.data?.schemeSpecificPart}")
        // 刷新列表
        AppListViewModel.sendIntent(AppListIntent.Refresh)
    }
}