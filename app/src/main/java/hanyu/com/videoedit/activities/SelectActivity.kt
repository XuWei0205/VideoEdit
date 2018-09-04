package hanyu.com.videoedit.activities

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.widget.Toast
import hanyu.com.videoedit.R
import hanyu.com.videoedit.activities.Beans.VideoBean
import hanyu.com.videoedit.activities.adpters.AlbumAdapter
import hanyu.com.videoedit.activities.providers.VideoProvider
import kotlinx.android.synthetic.main.activity_select.*
import java.lang.ref.WeakReference

/**
 * Created by HanYu on 2018/8/31.
 */
class SelectActivity : BaseActivity(), AlbumAdapter.IItemClick {


    private var myAdapter: AlbumAdapter? = null
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
        Toast.makeText(this, path, Toast.LENGTH_LONG).show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = AlbumAdapter(this, mDataList!!, this)
        rv_video_album.adapter = myAdapter
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
                    var tepList: ArrayList<VideoBean> = msg.obj as? ArrayList<VideoBean> ?: return
                    theActivity?.mDataList?.addAll(tepList)
                    theActivity?.myAdapter?.notifyDataSetChanged()
                }
                else -> Toast.makeText(theActivity, "获取数据失败", Toast.LENGTH_LONG).show()
            }
        }

    }
}