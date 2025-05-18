
package io.jarvis.pma.utils;

import android.content.Context;
import android.provider.Settings;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ShellUtils;

/**
 * 自动启动无障碍服务
 * 参考：https://blog.csdn.net/weixin_43821676/article/details/105924912
 */
public class StartAccessibilityServiceUtil {
    public static void startService(Context context, String pkg, String target) {
        LogUtils.d("启动无障碍服务 " + pkg + "/" + target);
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "pm grant " + pkg + " android.permission.WRITE_SECURE_SETTINGS",
                true
        );
        boolean r = Settings.Secure.putString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, pkg + "/" + target);
        boolean r2 = Settings.Secure.putInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 1);
        LogUtils.d("启动无障碍服务结果 ", r, r2);
    }
}
