package hanyu.com.videoedit.providers

/**
 * Created by HanYu on 2018/9/3.
 */
abstract class AbstractProvider<T> {
    abstract fun getDataList(): List<T>

}