package hanyu.com.videoedit.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import butterknife.BindView
import hanyu.com.videoedit.R


class MainActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    @BindView(R.id.bt_select_video)
    var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        button?.setOnClickListener({

        })

    }
}
