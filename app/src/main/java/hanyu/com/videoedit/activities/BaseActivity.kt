package hanyu.com.videoedit.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder
import hanyu.com.videoedit.R

/**
 * Created by HanYu on 2018/8/31.
 */
open abstract  class BaseActivity : AppCompatActivity() {
    var unbinder: Unbinder? = null
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(getLayout())
        unbinder = ButterKnife.bind(this)
    }

    abstract fun getLayout():Int



    override fun onDestroy() {
        super.onDestroy()
        unbinder?.unbind()
    }

}