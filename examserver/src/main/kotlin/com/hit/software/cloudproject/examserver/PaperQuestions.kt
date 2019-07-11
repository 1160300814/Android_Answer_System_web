package com.hit.software.cloudproject.examserver

class PaperQuestions {
    /**
     * 试卷详细表各列属性
     */
    var id:String? = null         //id
    var ehp_id:String? = null     //试卷id
    var ehp_que:String? = null    //题目id

    /**
     * 重写toString方法
     */
    override fun toString(): String {
        return "${this.id},${this.ehp_id},${this.ehp_que}"
    }

}