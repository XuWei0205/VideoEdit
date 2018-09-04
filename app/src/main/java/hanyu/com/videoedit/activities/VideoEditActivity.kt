package hanyu.com.videoedit.activities

import VideoHandle.EpEditor
import VideoHandle.EpVideo
import VideoHandle.OnEditorListener
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import hanyu.com.videoedit.R
import hanyu.com.videoedit.utils.TimeFormatUtil
import kotlinx.android.synthetic.main.activity_edit.*


/**
 * Created by HanYu on 2018/9/4.
 */

class VideoEditActivity : BaseActivity() {
    private var path: String = ""
    private var startEditPoint: Float = 0.0f
    private var endEditpoint: Float = 0.0f

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


    private fun editCutVideo() {
        val epVideo = EpVideo(path)
        epVideo.clip(startEditPoint, endEditpoint)
        val outPath = Environment.getExternalStorageDirectory().path + "/videoEdit/ " + TimeFormatUtil.timeYMDFormat(System.currentTimeMillis()) + ".mp4"
        EpEditor.exec(epVideo, EpEditor.OutputOption(outPath), object : OnEditorListener {
            override fun onSuccess() {
                val intent = Intent(this@VideoEditActivity, PreviewActivity::class.java)
                intent.putExtra("previewPath", outPath)
                startActivity(intent)
                Log.i("outPath", "----->$outPath")
            }

            override fun onFailure() {
                Toast.makeText(this@VideoEditActivity, "编辑失败", Toast.LENGTH_SHORT).show()

            }

            override fun onProgress(v: Float) {

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        vv_edit_video_play.stopPlayback()
    }

}
