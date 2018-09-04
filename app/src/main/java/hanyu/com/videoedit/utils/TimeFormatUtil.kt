package hanyu.com.videoedit.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by HanYu on 2018/9/3.
 */
class TimeFormatUtil {
    companion object {
        fun getDurationTime(duration: Long): String {
            val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.CHINA)
            simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT+0:00")
            return simpleDateFormat.format(duration)
        }
    }
}