package com.readboy.onlinecourseaides

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.readboy.onlinecourseaides.utils.EyesUtils
import com.readboy.onlinecourseaides.utils.Logger
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EyesUtilsTest {

    @Test
    fun testGetSwitch() {
        val map = EyesUtils.getSwitch()
        Logger.d("wocao", "testGetSwitch: $map")
    }

    @Test
    fun testCloseAllStatus() {
        EyesUtils.closeAllStatus()
        val map = EyesUtils.getSwitch()
        for (key in map.keys) {
            Assert.assertFalse(EyesUtils.getSwitch(map, key))
        }
    }

}