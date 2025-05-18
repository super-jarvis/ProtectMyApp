package io.jarvis.pma.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.jarvis.pma.viewModel.AppListIntent
import io.jarvis.pma.viewModel.AppListViewModel

class PackageChangeReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_PACKAGE_ADDED,
            Intent.ACTION_PACKAGE_REMOVED,
            Intent.ACTION_PACKAGE_CHANGED -> {
                AppListViewModel.onIntent(AppListIntent.Refresh)
            }
        }
    }
}