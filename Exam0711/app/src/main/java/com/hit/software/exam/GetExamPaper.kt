package com.hit.software.exam
import org.json.JSONObject

class GetExamPaper(pPaperResult: PaperResult): GeneralInterface {
    var mExamPaper = pPaperResult
    //response函数，得到服务器相应的内容
    override fun response(flag: Boolean, str: String?) {
        if (flag){
            val mJSON = JSONObject(str!!)
            when (mJSON.optString("code", "400")) {
                "100" -> {
                    mExamPaper.getPaperResult(true,mJSON)
                }
                "101" -> {
                    mExamPaper.getPaperResult(false,mJSON)
                }
            }


        } else {
            println("Something Wrong!")
        }
    }

    /**
     * 接收一个参数，试卷id，发送到云端，判断，并返回结果
     */
    fun checkPaper(id: String) {
        //通过接收的参数，构造出JSON
        val mJson = "{\"id\":\"$id\"}"
        //访问网络
        Http.send("http://192.168.199.183:8080/exam/paper", this, mJson)
    }
    //接口：得到服务器返回的结果，进行json解析
    interface PaperResult{
        fun getPaperResult(ret:Boolean, tipText: JSONObject)
    }

}
