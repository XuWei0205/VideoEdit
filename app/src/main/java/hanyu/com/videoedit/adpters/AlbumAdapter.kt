package hanyu.com.videoedit.adpters

import android.content.Context
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import hanyu.com.videoedit.beans.VideoBean
import hanyu.com.videoedit.R
import hanyu.com.videoedit.utils.TimeFormatUtil
import java.io.File

/**
 * Created by HanYu on 2018/8/31.
 */
class AlbumAdapter() : RecyclerView.Adapter<AlbumAdapter.MyViewHolder>() {
    var context: Context? = null
    var dataList: ArrayList<VideoBean> = arrayListOf()
    var listener: IItemClick? = null


    constructor(context: Context, dataList: ArrayList<VideoBean>, listener: IItemClick) : this() {
        this.context = context
        this.dataList = dataList
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_my_video, parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val videoBean = dataList[position]
        Glide.with(context)
                .load(Uri.fromFile(File(videoBean.getPath())))
                .into(holder.imgvCover)
        holder.tvDuration?.text = TimeFormatUtil.getDurationTime(videoBean.getDuration())
        holder.clContent?.setOnClickListener({
            listener?.itemClicked(videoBean.getPath())
        })

    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgvCover: ImageView? = null
        var tvDuration: TextView? = null
        var clContent: ConstraintLayout? = null

        init {
            imgvCover = view.findViewById(R.id.imgv_item_cover)
            tvDuration = view.findViewById(R.id.tv_item_duration)
            clContent = view.findViewById(R.id.cl_content)
        }

    }

    interface IItemClick {
        fun itemClicked(path: String)
    }

}