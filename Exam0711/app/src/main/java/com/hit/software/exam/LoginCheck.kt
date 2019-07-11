package com.hit.software.exam

import org.json.JSONObject


class LoginCheck(pLoginResult: LoginResult): GeneralInterface {

    var mLoginCheck = pLoginResult
    //response函数，得到服务器相应的内容
    override fun response(flag: Boolean, str: String?) {
        if (flag) {
            val mJSON = JSONObject(str!!)
            when (mJSON.optString("code", "400")) {
                "100" -> {
                    mLoginCheck.getLoginResult(true,mJSON.optString("result",""))
                }
                "101" -> {
                    mLoginCheck.getLoginResult(false,mJSON.optString("result",""))
                }
            }


        } else {
            println("Something Wrong!")
        }
    }


    /**
     * 接收两个参数，分别是用户名和密码，将用户名和密码发送到云端，判断，并返回结果
     */
    fun checkUserNameAndPassword(userName: String, userPassword: String = "") {
        //通过接收的参数，构造出JSON
        val mJson = "{\"user_name\":\"$userName\",\"user_password\":\"$userPassword\"}"
        //访问网络
        Http.send("http://192.168.199.183:8080/exam/login", this, mJson)

    }

    interface LoginResult{
        fun getLoginResult(ret:Boolean,tipText:String)
    }
}