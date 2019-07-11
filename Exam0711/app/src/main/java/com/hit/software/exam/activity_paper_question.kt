package com.hit.software.exam

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.*
import org.json.JSONObject
import java.util.ArrayList
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 此类为答题页面，获取某一个试卷具体题、换题、查看错题等实现都在此类
 */
class activity_paper_question : AppCompatActivity() {
    //当前第几题
    var corrent = 0
    //初始化试卷问题表为空
    var list = arrayListOf<Question>()
    //问题号表
    var qlist = arrayListOf<String>()
    //一共多少道题
    var count = 0;
    //答错题表
    var wrongList = arrayListOf<Int>()
    //是否进入错题模式
    var wrongMode = false
    //选项
    var mRadioButton = arrayOfNulls<RadioButton>(4)
    //问题
    private var tv_title: TextView? = null
    //上一题
    private var btn_up: Button? = null
    //下一题
    private var btn_down: Button? = null
    //详情
    private var tv_result: TextView? = null
    //容器
    private var mRadioGroup: RadioGroup? = null

    /**
     * 初始化View
     */
    private fun initView() {

        wrongMode = false

        tv_title = findViewById(R.id.tv_title) as TextView

        mRadioButton[0] = findViewById(R.id.RadioA) as RadioButton
        mRadioButton[1] = findViewById(R.id.RadioB) as RadioButton
        mRadioButton[2] = findViewById(R.id.RadioC) as RadioButton
        mRadioButton[3] = findViewById(R.id.RadioD) as RadioButton

        btn_down = findViewById(R.id.btn_down) as Button
        btn_up = findViewById(R.id.btn_up) as Button

        tv_result = findViewById(R.id.tv_result) as TextView

        mRadioGroup = findViewById(R.id.mRadioGroup) as RadioGroup
    }

    /*
   解析服务器返回的json
    */
    val mQuestions = object : GetPaperQuestions.QuestionsResult {
        override fun getQuestionsResult(ret: Boolean, tipText: JSONObject) {
            var m = tipText.optString("num").toInt()
            //time = tipText.optString("ep_duration").toInt()
           // Log.d("time",time.toString())
            for (index in 0..m-1){
                //转格式
                var i = index.toString()
                //日志
                Log.d("paper_question",tipText.optString("ehp_que"+i))
                Log.d("paper_question",i)

                //eho_que0、eho_que1、eho_que2......
                var t = "ehp_que"+i
                //日志
                Log.d("paper_question",t)
                Log.d("test1",tipText.optString(t))
                //添加问题号表
                qlist.add(tipText.optString(t))
                //日志
                qlist.forEach {
                    Log.d("test1",it)
                }

            }

        }
    }
    /**
     * 获得服务器返回的JSON，得到具体问题，加入问题表list中
     */
    val mQuestion = object : GetQuestions.QuestionResult {
        override fun getQuestionResult(ret: Boolean, tipText: JSONObject) {
            //解析返回的json，得到问题的属性值，添加到问题表list中
            val q = Question(tipText.optString("q_id").toInt(),tipText.optString("question"),
                tipText.optString("a"), tipText.optString("b"),tipText.optString("c"),
                tipText.optString("d"),tipText.optString("correct").toInt())
            //加入问题表list中
            list.add(q)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paper_question)
        //接受传过来的试卷编号
        val papernum = this.intent.getStringExtra("papernum")

        //向服务端请求试卷具体内容

        GlobalScope.launch {
            //获得问题号表qlist
            GetPaperQuestions(mQuestions).checkQuestions(papernum)
                delay(2000)
            Log.d("test3","OK")
            //遍历问题号表qlist，获得问题表list
            qlist.forEach {
                Log.d("test3","GOOD")
                Log.d("test2",it)
                GetQuestions(mQuestion).checkQuestion(it)
                delay(100)
            }
            //初始化界面和相关数据，显示第一题
            initView()
            count = list.size
            val q = list.get(0)
            tv_title!!.setText(q.questionTitle)
            mRadioButton[0]!!.setText(q.answerA)
            mRadioButton[1]!!.setText(q.answerB)
            mRadioButton[2]!!.setText(q.answerC)
            mRadioButton[3]!!.setText(q.answerD)
            //启动监听按钮
            addNextFunction()
            addLastFunction()
            addSelectFunction()
        }
    }

    /**
     * 统计答题结果
     */
    private fun checkAnswer(){
        //循环一编问题表list
        for(i in 0 until count){
            //判断答题对错
            if(list.get(i).answer != list.get(i).selectedAnswer ){
                //将错误的题号添加进错误表里
                wrongList.add(i)
            }
        }
    }

    /**
     * 给上一题按钮添加点击事件
     */
    private fun addLastFunction() {
        btn_up!!.setOnClickListener {
            if (corrent > 0) {//如果上一题存在不是第一题
                corrent--
                //显示上一题内容
                var q = list.get(corrent);
                tv_title!!.setText(q.questionTitle)
                mRadioButton[0]!!.setText(q.answerA)
                mRadioButton[1]!!.setText(q.answerB)
                mRadioButton[2]!!.setText(q.answerC)
                mRadioButton[3]!!.setText(q.answerD)
                //答案，答题时不显示；查看错题模式时显示
                if(q.answer==0){
                    tv_result!!.setText("正确答案：A")
                }else if(q.answer==1){
                    tv_result!!.setText("正确答案：B")
                }else if(q.answer==2){
                    tv_result!!.setText("正确答案：C")
                }else if(q.answer==3){
                    tv_result!!.setText("正确答案：D")
                }else{
                    tv_result!!.setText("E: \"${q.answer}\"")
                }

                mRadioGroup!!.clearCheck()

                //设置用户选中
                if (q.selectedAnswer != -1) {
                    mRadioButton[q.selectedAnswer]!!.setChecked(true)
                }
            }


        }
    }

    /**
     * 给下一题按钮添加点击事件
     */
    private fun addNextFunction() {
        btn_down!!.setOnClickListener {

            if (corrent < count - 1) {//如果没到最后一题
                corrent++
                //显示下一题内容
                var q = list.get(corrent);
                tv_title!!.setText(q.questionTitle)

                mRadioButton[0]!!.setText(q.answerA)
                mRadioButton[1]!!.setText(q.answerB)
                mRadioButton[2]!!.setText(q.answerC)
                mRadioButton[3]!!.setText(q.answerD)

                //答案，答题时不显示；查看错题模式时显示
                if(q.answer==0){
                    tv_result!!.setText("正确答案：A")
                }else if(q.answer==1){
                    tv_result!!.setText("正确答案：B")
                }else if(q.answer==2){
                    tv_result!!.setText("正确答案：C")
                }else if(q.answer==3){
                    tv_result!!.setText("正确答案：D")
                }else{
                    tv_result!!.setText("E: \"${q.answer}\"")
                }

                mRadioGroup!!.clearCheck()

                //设置用户选中
                if (q.selectedAnswer != -1) {
                    mRadioButton[q.selectedAnswer]!!.setChecked(true)
                }
            } else if(corrent == count - 1 && wrongMode == true){ //检测准确性时的最后一题（查看错题模式）
                //Toast.makeText(this@activity_paper_question, "最后一题啦,请退出！", Toast.LENGTH_SHORT).show()
                AlertDialog.Builder(this@activity_paper_question).setTitle("提示").setMessage("已经到达最后一道题，是否退出？")
                    .setPositiveButton("确定",DialogInterface.OnClickListener{dialogInterface, i ->
                            finish();
                    }).setNegativeButton("取消",null).create().show()
            }else{ //没有下一题，开始检测准确性
                checkAnswer()
                //得到结果，弹出可选框可供用户查看和选择
                if(wrongList.isEmpty()){//全答对
                   // Toast.makeText(this@activity_paper_question, "提示：你好列害,全答对了!", Toast.LENGTH_SHORT).show()
                    AlertDialog.Builder(this@activity_paper_question).setTitle("提示").setMessage("你好厉害，答对了所有题！")
                        .setPositiveButton("确定", DialogInterface.OnClickListener{dialogInterface, i ->
                            finish();
                        }).setNegativeButton("取消",null).create().show();
                }else{//有错题，点击确定，查看错题，进入错题模式
                    AlertDialog.Builder(this@activity_paper_question).setTitle("恭喜，答题完成！").setMessage(
                        "答对了" + (list.size - wrongList.size) + "道题" + "\n"
    + "答错了" + wrongList.size + "道题" + "\n" + "是否查看错题？").setPositiveButton("确定", DialogInterface.OnClickListener { dialogInterface, i ->
                        wrongMode = true
                        val newList = ArrayList<Question>()
                        //将题按照错误表序号提取出来
                        for(i in 0 until wrongList.size){
                            newList.add(list.get(wrongList.get(i)))
                        }
                        list.clear()
                        //循环新错误题表,提取到list表
                        for(i in 0 until newList.size){
                            list.add(newList.get(i))
                        }
                        //更新初始化的界面参数
                        corrent = 0
                        count = list.size

                        //更新显示错题内容
                        val q = list[corrent]

                        tv_title!!.setText(q.questionTitle)

                        mRadioButton[0]!!.setText(q.answerA)
                        mRadioButton[1]!!.setText(q.answerB)
                        mRadioButton[2]!!.setText(q.answerC)
                        mRadioButton[3]!!.setText(q.answerD)
                        //答案，查看错题模式时显示
                        if(q.answer==0){
                            tv_result!!.setText("正确答案：A")
                        }else if(q.answer==1){
                            tv_result!!.setText("正确答案：B")
                        }else if(q.answer==2){
                            tv_result!!.setText("正确答案：C")
                        }else if(q.answer==3){
                            tv_result!!.setText("正确答案：D")
                        }else{
                            tv_result!!.setText("E: \"${q.answer}\"")
                        }
                        mRadioButton[q.answer]!!.setChecked(true)
                        //将答案设为可见
                        tv_result!!.visibility = View.VISIBLE
                    }).setNegativeButton("取消", null).create().show()
                }

            }

        }
    }

    /**
     * 给选项添加点击事件
     */
    private fun addSelectFunction() {
        mRadioGroup!!.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                for (i in 0 until 4) {
                    if (mRadioButton[i]!!.isChecked() == true) {
                        list.get(corrent).selectedAnswer = i
                    }
                }

            }
        })
    }
}
