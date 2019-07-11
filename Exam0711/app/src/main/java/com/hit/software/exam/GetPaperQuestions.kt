package com.hit.software.exam

import org.json.JSONObject

class GetPaperQuestions(pQsResult: QuestionsResult) : GeneralInterface{
    var mQuestions = pQsResult
    //response函数，得到服务器相应的内容
    override fun response(flag: Boolean, str: String?) {
        if (flag){
            val mJSON = JSONObject(str!!)
            when (mJSON.optString("code", "400")) {
                "100" -> {
                    mQuestions.getQuestionsResult(true,mJSON)
                }
                "101" -> {
                    mQuestions.getQuestionsResult(false,mJSON)
                }
            }


        } else {
            println("Something Wrong!")
        }
    }

    /**
     * 接收参数，试卷id，判断，并返回结果
     */
    fun checkQuestions(id: String) {
        //通过接收的参数，构造出JSON
        val mJson = "{\"ehp_id\":\"$id\"}"
        //访问网络
        Http.send("http://192.168.199.183:8080/exam/paperquestions", this, mJson)

    }
    interface QuestionsResult{
        fun getQuestionsResult(ret:Boolean, tipText: JSONObject)
    }
}