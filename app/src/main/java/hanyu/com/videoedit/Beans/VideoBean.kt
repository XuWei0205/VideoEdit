package hanyu.com.videoedit.Beans

/**
 * Created by HanYu on 2018/8/31.
 */
class VideoBean {
    // private var id: Int = 0
    // private var title: String? = null
    //private var album: String? = null
    //private var artist: String? = null
    //private var displayName: String? = null
    // private var mimeType: String? = null
    private var path: String? = null //视频路径
    //private var size: Long = 0
    private var duration: Long = 0 //视频时长
    //private var thumbnail: Bitmap? = null


    fun setParh(path: String) {
        this.path = path
    }

    fun getPath(): String {
        return path!!
    }

    fun setDuration(duration: Long) {
        this.duration = duration
    }

    fun getDuration(): Long {
        return duration
    }

}