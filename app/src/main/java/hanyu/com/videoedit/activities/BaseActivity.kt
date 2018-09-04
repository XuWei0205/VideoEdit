package hanyu.com.videoedit.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by HanYu on 2018/8/31.
 */
open abstract  class BaseActivity : AppCompatActivity() {
    //var unbinder: Unbinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        //unbinder = ButterKnife.bind(this)
    }

    abstract fun getLayout():Int





}