package hanyu.com.videoedit.activities

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import hanyu.com.videoedit.R
import kotlinx.android.synthetic.main.activity_edit.*


/**
 * Created by HanYu on 2018/9/4.
 */

class VideoEditActivity : BaseActivity() {
    private var path: String = ""


    override fun getLayout(): Int {
        return R.layout.activity_edit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideBar()
        path = intent.extras.getString("path")
        Log.i("EditActivity", "path------>$path")
        videoStart(path)
        vv_edit_video_play.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            vv_edit_video_play.setVideoPath(path)
            videoStart(path)

        })
        tv_edit_cancel.setOnClickListener({
            finish()
        })

    }


    private fun videoStart(path: String) {
        vv_edit_video_play.setVideoPath(path)
        vv_edit_video_play.start()
    }

    private fun hideBar() {
        val decorView = window.decorView
        val option = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = option
    }

    override fun onDestroy() {
        super.onDestroy()
        vv_edit_video_play.stopPlayback()
    }

}
