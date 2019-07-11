package com.hit.software.cloudproject.examserver

class Question {
    /**
     * 问题表各列标签属性
     */
    var q_id:String? = null          //问题id
    var question :String? = null     //问题内容
    var a:String? = null             //选项A
    var b:String? = null             //选项B
    var c:String? = null             //选项C
    var d:String? = null             //选项D
    var correct :String? = null      //正确答案

    /**
     * 重写toString方法
     */
    override fun toString(): String {
        return "${this.q_id},${this.question},${this.a},${this.b},${this.c},${this.d},${this.correct}"
    }

}