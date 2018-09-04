package hanyu.com.videoedit.activities

import android.content.Intent
import android.os.Bundle
import hanyu.com.videoedit.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_main
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bt_select_video.setOnClickListener({
            startActivity(Intent(this, SelectActivity::class.java))
        })

    }
}
