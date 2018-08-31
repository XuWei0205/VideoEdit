package hanyu.com.videoedit.activities

import android.os.Bundle
import android.os.PersistableBundle
import hanyu.com.videoedit.R
import hanyu.com.videoedit.activities.Beans.VideoBean
import hanyu.com.videoedit.activities.adpters.AlbumAdapter

/**
 * Created by HanYu on 2018/8/31.
 */
class SelectActivity : BaseActivity() {
    var myAdapter: AlbumAdapter? = null
    var mDataList: ArrayList<VideoBean>? = arrayListOf()
    override fun getLayout(): Int {
        return R.layout.activity_select
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        myAdapter = AlbumAdapter(this, mDataList!!)
    }
}