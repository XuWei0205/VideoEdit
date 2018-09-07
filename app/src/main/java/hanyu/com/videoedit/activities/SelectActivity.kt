package hanyu.com.videoedit.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.widget.Toast
import hanyu.com.videoedit.R
import hanyu.com.videoedit.adpters.AlbumAdapter
import hanyu.com.videoedit.adpters.ThumbnailAdapter
import hanyu.com.videoedit.beans.VideoBean
import hanyu.com.videoedit.providers.VideoProvider
import kotlinx.android.synthetic.main.activity_select.*
import java.lang.ref.WeakReference

/**
 * Created by HanYu on 2018/8/31.
 */
class SelectActivity : BaseActivity(), AlbumAdapter.IItemClick {

    private var mAlbumAdapter: AlbumAdapter? = null
    private var mDataList: ArrayList<VideoBean>? = arrayListOf()
    private var handler: InnerHandler = InnerHandler(this)


    companion object {
        private var SUCCESS: Int = 1
        private var FAIL: Int = 2
    }




    override fun getLayout(): Int {
        return R.layout.activity_select
    }


    override fun itemClicked(path: String) {
        val intent: Intent = Intent(this, VideoEditActivity::class.java)
        intent.putExtra("path", path)
        startActivity(intent)
        //Toast.makeText(this, path, Toast.LENGTH_LONG).show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAlbumAdapter = AlbumAdapter(this, mDataList!!, this)
        rv_video_album.adapter = mAlbumAdapter
        rv_video_album?.layoutManager = GridLayoutManager(this, 4)
        getData()
        imgv_select_back.setOnClickListener({
            finish()
        })
    }

    private fun getData() {
        MyThread(this).start()
    }


    internal inner class MyThread(var context: Context) : Thread() {
        override fun run() {
            super.run()
            val dataList: ArrayList<VideoBean>
            val videoProvider = VideoProvider(context)
            dataList = videoProvider.getDataList()
            val message = Message()
            message.what = SelectActivity.SUCCESS
            message.obj = dataList
            handler.sendMessage(message)
        }

    }

    private class InnerHandler constructor(activity: SelectActivity) : Handler() {
        private var weakActivity: WeakReference<SelectActivity> = WeakReference(activity)
        private var theActivity = weakActivity.get()
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                SUCCESS -> {
                    @Suppress("UNCHECKED_CAST")
                    val tepList: ArrayList<VideoBean> = msg.obj as? ArrayList<VideoBean> ?: return
                    theActivity?.mDataList?.addAll(tepList)
                    theActivity?.mAlbumAdapter?.notifyDataSetChanged()
                }
                else -> Toast.makeText(theActivity, "获取数据失败", Toast.LENGTH_LONG).show()
            }
        }

    }
}