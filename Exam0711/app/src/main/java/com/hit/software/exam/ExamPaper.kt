package com.hit.software.exam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import kotlinx.android.synthetic.main.activity_exam_paper.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class ExamPaper : AppCompatActivity() {
    //试题列表对象
    var mPaperList: ArrayList<PaperItem> = arrayListOf()
    /**
     * 重写解析函数，解析json得到，试卷属性信息
     */
    val mExamPaper = object : GetExamPaper.PaperResult{
        override fun getPaperResult(ret: Boolean, tipText: JSONObject) {
            if (!ret) {
                val mm = Message()
                mm.what = 0  // 云端获取试卷信息返回失败
            } else {
                val mm = Message()
                mm.what = 1 //云端获取试卷信息返回成功
                mPaperList.add(PaperItem(tipText.optString("id"),tipText.optString("ep_title"), tipText.optString("ep_start_time"),tipText.optInt("ep_duration") ))
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_paper)
        addFunction()
        /**
         * 联网获取数据
         */
        GlobalScope.launch {
            //直接静态定义一共7个试卷，试卷id 1-7，遍历查询
            for (index in 1..7){
                var i = index.toString()
                GetExamPaper(mExamPaper).checkPaper(i)
                //等待单个查询结果，使得试卷列表顺序正确
                delay(100)
            }
            //等待，使得正确显示试卷列表
            delay(20000)
        }
        //更新UI显示
        var mPaperListAdapter: PaperListAdapter = PaperListAdapter(mPaperList, this)
        paper_list.adapter = mPaperListAdapter

    }

    /**
     * 给“开始答题”按钮添加点击事件
     */
    private fun addFunction() {
        textView2.setOnClickListener {
            when (true) {
                true -> {
                    //选择试卷成功
                    //打开新的页面
                    startActivity(Intent(this, activity_paper_question::class.java).putExtra("papernum", paper_num.text.toString()))
                }
                false -> {
                    //登录失败
                }
            }

        }
    }


}
