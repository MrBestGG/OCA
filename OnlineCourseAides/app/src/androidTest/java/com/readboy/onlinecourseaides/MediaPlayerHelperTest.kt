package com.readboy.onlinecourseaides

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.readboy.onlinecourseaides.utils.MediaPlayerHelper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MediaPlayerHelperTest {

    @Test
    fun testGetMediaDuration() {
        var durations = MediaPlayerHelper.getMediaFileDuration(arrayOf())
        for (dur in durations) {
            Assert.assertEquals(0L, dur)
        }

        durations = MediaPlayerHelper.getMediaFileDuration(arrayOf(
            "",
            "　",
            "/sdcard/DCIM/OCA/wav/OCA_SoundRecord_20221208_03856.wav"
        ))
        for (dur in durations) {
            Assert.assertEquals(0L, dur)
        }

        durations = MediaPlayerHelper.getMediaFileDuration(arrayOf(
            "/sdcard/DCIM/OCA/wav/OCA_SoundRecord_20221208_083653.wav",
            "/sdcard/DCIM/OCA/wav/OCA_SoundRecord_20221208_083756.wav",
            "/sdcard/DCIM/OCA/wav/OCA_SoundRecord_20221208_083856.wav"
        ))

        println(durations.contentToString())

        durations = MediaPlayerHelper.getMediaFileDuration(arrayOf(
//            "/sdcard/DCIM/OCA/sound/OCA_SoundRecord_20221208_104719.mp3",
            "/sdcard/DCIM/OCA/sound/OCA_SoundRecord_20221208_104829.mp3",
//            "/sdcard/DCIM/OCA/wav/OCA_SoundRecord_20221208_083756.wav",
//            "/sdcard/DCIM/OCA/wav/OCA_SoundRecord_20221208_083856.wav"
        ))

        println(durations.contentToString())
    }
}