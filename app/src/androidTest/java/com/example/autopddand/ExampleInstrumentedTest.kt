package com.example.autopddand

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.example.autopddand", appContext.packageName)
//    }


    @Test
    fun test1(){
        val devices = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        //todo 找到微信图标
        val wechat = devices.findObject(UiSelector().text("微信"))
        wechat.click()

        devices.pressHome()

    }
}