package com.hit.software.cloudproject.examserver

class Paper {
    /**
     * 试卷表各列标签属性
     */
    var id: String? = null              //试卷id
    var ep_title: String? = null        //试卷标题
    var ep_start_time: String? = null   //试卷开始时间
    var ep_duration: String? = null     //答题时长

    /**
     * 重写toString方法
     */
    override fun toString(): String {
        return "${this.id},${this.ep_title},${this.ep_start_time},${this.ep_duration}"
    }
}