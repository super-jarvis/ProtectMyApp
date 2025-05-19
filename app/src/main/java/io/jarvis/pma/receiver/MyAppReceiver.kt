package io.jarvis.pma.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.LogUtils

class MyAppReceiver : BroadcastReceiver() {
    companion object {
        const val MY_APP_INTENT = "io.jarvis.pma.MyAppIntent"
    }

    private val tag = "MyAppReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        runCatching {
            LogUtils.dTag(
                tag,
                "收到广播 ${intent.action} ${
                    intent.extras?.keySet()?.joinToString { "$it=${intent.extras!!.getString(it)}" }
                }")
            if (intent.action == MY_APP_INTENT) {
                intent.extras?.getString("data")?.let {

                }
            }
        }.onFailure {

        }
    }
}
