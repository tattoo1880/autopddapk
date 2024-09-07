package com.example.autopddand

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RecentTaskInfo
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.RemoteException
import android.provider.Settings
import android.text.TextUtils
import android.view.accessibility.AccessibilityNodeInfo
import java.io.File


class MainActivity : Activity() {

    private val handler = Handler(Looper.getMainLooper())
    private val initialDelay = 5000L // 初始延迟5秒后启动拼多多应用
    private val launchInterval = 30000L // 每30秒后重新启动拼多多应用
    private val closeInterval = 20000L // 启动拼多多应用后20秒关闭应用

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //todo使用adb 命令启动pinduoduo
//        val intent = Intent()
//        intent.setClassName("com.xunmeng.pinduoduo", "com.xunmeng.pinduoduo.ui.activity.MainFrameActivity")
//        startActivity(intent)

        //todo 使用adb命令关闭pinduoduo

        restartPinduoduoApp()

    }

//        handler.postDelayed({
//                launchPinduoduoApp()
//            }, initialDelay)
//    }
    private fun isAccessibilitySettingsOn(): Boolean {
        val service = packageName + "/" + AutoCloseService::class.java.canonicalName
        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        var isEnabled = 0
        try {
            isEnabled = Settings.Secure.getInt(
                contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        if (isEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            colonSplitter.setString(settingValue)
            while (colonSplitter.hasNext()) {
                if (colonSplitter.next().equals(service, ignoreCase = true)) {
                    return true
                }
            }
        }
        return false
    }

    private fun launchPinduoduoApp() {
        val pddPackageName = "com.xunmeng.pinduoduo"
        val intent = packageManager.getLaunchIntentForPackage(pddPackageName)
        if (intent != null) {
            startActivity(intent)

            // 在启动拼多多应用后20秒尝试关闭拼多多应用
            handler.postDelayed({
                closePinduoduoApp()
            }, closeInterval)

            // 安排30秒后再次启动拼多多应用
            handler.postDelayed({
                launchPinduoduoApp()
            }, launchInterval)
        } else {
            // 如果拼多多没有安装，则打开其应用商店页面
            val marketIntent = Intent(Intent.ACTION_VIEW)
            marketIntent.data = Uri.parse("market://details?id=$pddPackageName")
            startActivity(marketIntent)
        }
    }

    fun closePinduoduoApp() {
        val command = "adb shell am force-stop com.xunmeng.pinduoduo"
        executeAdbCommand(command)
    }

    fun restartPinduoduoApp() {
        // 关闭拼多多应用

//        循环10次每次间隔5s
        for (i in 1..100) {
            val stopCommand = "am force-stop com.xunmeng.pinduoduo"
            executeAdbCommand(stopCommand)

            // 启动拼多多应用
            val startCommand = "monkey -p com.xunmeng.pinduoduo -c android.intent.category.LAUNCHER 1"
            executeAdbCommand(startCommand)
            Thread.sleep(5*60*1000)
        }

    }

    fun regsms(){
        val startCommand = "monkey -p com.xunmeng.pinduoduo -c android.intent.category.LAUNCHER 1"
        executeAdbCommand(startCommand)

        Thread.sleep(10000)

        val tapcm1 = "input tap 1265 2250"
        executeAdbCommand(tapcm1)

        Thread.sleep(3000)

        //!todo 使用adb命令页面下翻到底
        val swipeCommand = "input swipe 500 1500 500 500"
        executeAdbCommand(swipeCommand)

        Thread.sleep(3000)

        //！todo 页面上翻到顶
        val swipeCommand2 = "input swipe 500 500 500 1500"
        executeAdbCommand(swipeCommand2)

        Thread.sleep(3000)
        val swipeCommand3 = "input swipe 500 500 500 1500"
        executeAdbCommand(swipeCommand3)

        Thread.sleep(3000)

        //使用adb 点击“设置”按钮
        val tapcomd2 = "input tap 1358 150"
        executeAdbCommand(tapcomd2)

        val swipeCommand4 = "input swipe 500 1500 500 500"

        for (i in 1..10) {
            executeAdbCommand(swipeCommand4)
            Thread.sleep(1000)
        }

        val quit = "input tap 750 2080"
        executeAdbCommand(quit)

        val quitconfirm ="input tap 1050 1410"
        executeAdbCommand(quitconfirm)

        var login = "input tap 780 1500"
        executeAdbCommand(login)

        var login2 = "input tap 893 1888"
        executeAdbCommand(login2)

//+16592860240
        Thread.sleep(13000)
        val input = "input text 6592860240"
        executeAdbCommand(input)

        val confirmsendsms = "input tap 770 835"
        executeAdbCommand(confirmsendsms)
    }
    fun executeAdbCommand(command: String): String? {
        return try {
            // 使用 su -c 以 root 权限执行命令
            val process = Runtime.getRuntime().exec("su -c $command")

            // 读取命令执行结果
            val reader = process.inputStream.bufferedReader()
            val output = reader.readText()
            reader.close()

            // 等待命令执行完成
            process.waitFor()
            output
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 清除所有未执行的任务，防止内存泄漏
        handler.removeCallbacksAndMessages(null)
    }


    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }
}
