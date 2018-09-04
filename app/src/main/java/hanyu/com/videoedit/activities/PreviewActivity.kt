package hanyu.com.videoedit.activities

import android.os.Bundle
import android.text.TextUtils
import hanyu.com.videoedit.R
import kotlinx.android.synthetic.main.activity_preview.*

/**
 * Created by HanYu on 2018/9/4.
 */
class PreviewActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_preview
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val path: String = intent.getStringExtra("previewPath")
        if (TextUtils.isEmpty(path)) {
            return
        }
        startPlay(path)
    }

    private fun startPlay(path: String) {
        vv_preview_video_play.setVideoPath(path)
        vv_preview_video_play.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        vv_preview_video_play.stopPlayback()
    }
}