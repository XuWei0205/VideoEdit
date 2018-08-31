package hanyu.com.videoedit.activities.adpters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import hanyu.com.videoedit.activities.Beans.VideoBean

/**
 * Created by HanYu on 2018/8/31.
 */
class AlbumAdapter() : RecyclerView.Adapter<ViewHolder>() {
    var context: Context? = null
    var dataList: ArrayList<VideoBean> = arrayListOf()


    constructor(context: Context, dataList: ArrayList<VideoBean>) : this() {
        AlbumAdapter@ this.context = context
        AlbumAdapter@ this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}