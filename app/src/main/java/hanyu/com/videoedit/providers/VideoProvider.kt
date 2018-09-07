package hanyu.com.videoedit.providers

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import hanyu.com.videoedit.beans.VideoBean

/**
 * Created by HanYu on 2018/9/3.
 */
class VideoProvider(private var context: Context) : AbstractProvider<VideoBean>() {


    override fun getDataList(): ArrayList<VideoBean> {
        val dataList: ArrayList<VideoBean> = arrayListOf()
        val contentResolver: ContentResolver = context.contentResolver
        val cursor: Cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
        while (cursor.moveToNext()) {
            val path: String = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)) //var cover :String = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM))
            val duration: Long = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
            val videoBean = VideoBean()
            videoBean.setDuration(duration)
            videoBean.setParh(path)
            dataList.add(videoBean)
        }
        cursor.close()
        return dataList
    }
}