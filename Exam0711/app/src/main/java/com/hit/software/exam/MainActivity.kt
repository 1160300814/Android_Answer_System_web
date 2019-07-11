package com.hit.software.exam

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author 姜思琪、樊静
 * @since 20190711
 */
class MainActivity : Activity() {

    /**
     * 用于验证登陆是否成功
     */
    val mLoginCheck = object : LoginCheck.LoginResult {
        override fun getLoginResult(ret: Boolean, tipText: String) {
            if (!ret) {
                val mm = Message()
                mm.what = 0  // 云端登录返回失败
                mm.obj = tipText
                mHandler.sendMessage(mm)
                mHandler.obtainMessage()
            } else {
                val mm = Message()
                mm.what = 1 //云端登录返回成功
                mm.obj = tipText
                mHandler.sendMessage(mm)
                mHandler.obtainMessage()
            }
        }
    }

    /**
     * 提示框
     */
    val mHandler = Handler {
        when (it.what) {
            0 -> {
                putTipText(it.obj.toString())
            }
            1 -> {
                putTipText("")
                startActivity(Intent(this, ExamPaper::class.java))
                this.finish()
            }
        }
        true
    }


    /**
     * 这是程序运行的时候执行的第一个方法
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //启动按钮监听器
        addFunction()
    }

    /**
     * 给按钮添加点击事件
     */
    private fun addFunction() {
        login.setOnClickListener {
            when (checkUser()) {
                true -> {
                    //登录成功
                    //打开新的页面
                    startActivity(Intent(this, ExamPaper::class.java).putExtra("username", user_name.text.toString()))
                }
                false -> {
                    //登录失败
                }
            }
        }
    }

    /**
     * 向云端发送用户名和密码，等待返回结果
     */
    private fun checkUser(): Boolean {
        val userName = user_name.text.toString()
        val userPasssword = user_password.text.toString()

        //发送给云端服务器去判断，等待返回结果，如果返回的结果是正确的，那么登录App，否则提示用户名或密码输入错误
        LoginCheck(mLoginCheck).checkUserNameAndPassword(userName, userPasssword)

        return false
    }

    /**
     * 转手数据
     */
    fun putTipText(text: String) {
        tip.text = text
    }


}
