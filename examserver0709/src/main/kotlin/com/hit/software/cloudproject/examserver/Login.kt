package com.hit.software.cloudproject.examserver

class Login {
    var user_name:String? = null
    var user_password:String? = null

    /**
     * 重写toString方法
     */
    override fun toString(): String {
         return "${this.user_name},${this.user_password}"
    }

}