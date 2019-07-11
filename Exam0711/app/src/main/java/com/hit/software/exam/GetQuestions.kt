package com.hit.software.exam
import org.json.JSONObject

class GetQuestions(pQuestionResult: QuestionResult): GeneralInterface {
    var mQuestion = pQuestionResult
    //response函数，得到服务器相应的内容
    override fun response(flag: Boolean, str: String?) {
        if (flag){
            val mJSON = JSONObject(str!!)
            when (mJSON.optString("code", "400")) {
                "100" -> {
                    mQuestion.getQuestionResult(true,mJSON)
                }
                "101" -> {
                    mQuestion.getQuestionResult(true,mJSON)
                }
            }


        } else {
            println("Something Wrong!")
        }
    }

    /**
     * 接收一个参数，问题id ，发送到云端，判断，并返回结果
     */
    fun checkQuestion(id: String) {
        //通过接收的参数，构造出JSON
        val mJson = "{\"q_id\":\"$id\"}"
        //访问网络
        Http.send("http://192.168.199.183:8080/exam/questions", this, mJson)
        //新建协程

    }
    interface QuestionResult{
        fun getQuestionResult(ret:Boolean, tipText: JSONObject)
    }
}