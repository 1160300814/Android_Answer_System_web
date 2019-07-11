package com.hit.software.exam

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaType
import java.util.concurrent.TimeUnit


/**
 * Created by XiaoMingliang on 9/28/17.
 * Common http request sender and call backs
 * 使用OKHttp 包
 */
object Http {
    // OkHttpClient
    //定义OkHttp客户端，使用此客户端来发送请求
    private val mOkHttpClient: OkHttpClient = OkHttpClient()


    /**
     * Send http POST request
     * Needs implements call backs
     */
    fun send(
        url: String,
        mHttpCallBack: GeneralInterface? = null,
        postData: String = "",
        mContentType: String = "application/x-www-form-urlencoded"
    ) {
        GlobalScope.launch {
            val cb: Callback = object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    mHttpCallBack?.response(false)
                }

                override fun onResponse(call: Call, response: Response) {
                    mHttpCallBack?.response(true, response?.body?.string())
                }
            }
            mOkHttpClient.newBuilder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS).build()


            mOkHttpClient.newCall(
                Request.Builder()
                    .url(url)
                    .post(RequestBody.create(("application/json; charset=utf-8").toMediaType(), postData))
                    .header("Content-Type", mContentType)
                    .build()
            ).enqueue(cb)
        }
    }


    fun sendGet(
        url: String,
        mHttpCallBack: GeneralInterface? = null,
        mContentType: String = "application/x-www-form-urlencoded"
    ) {
        //Log.w("Http prepare request:$url")
        /* New Thread
     Thread { ... }.start()
     But the code below is using co-routine, a light-weight experimental state of new Thread
    */
        GlobalScope.launch {
            try {
                val cb: Callback = object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        mHttpCallBack?.response(false)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        mHttpCallBack?.response(true, response?.body?.string())
                    }

                }
                /*Use OkHttpClient */
                //mOkHttpClient.setConnectTimeout(mHttpConnectionTimeout, TimeUnit.SECONDS)
                //mOkHttpClient.setReadTimeout(mHttpReadTimeout, TimeUnit.SECONDS)
                mOkHttpClient.newCall(
                    Request.Builder()
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                        .url(url)
                        .header("Content-Type", mContentType)
                        .build()
                ).enqueue(cb)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    /*
    interface HttpCallBack {
        /**
         * Http Response call back （回调）
         * Only one callback contains response status and result
         * @param flag If is true, response is ok, else no response
         * @param str Response String
         */
        fun response(flag: Boolean, str: String? = null)
    }
    */

}



