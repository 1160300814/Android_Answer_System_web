package com.hit.software.exam

import java.net.IDN

/**
 * 这是一个数据结构，存储生成的问题，包括问题编号、问题内容、问题四个选项、答案及用户选中的答案，主要应用于activity_paper_question.java类中
 */
class Question(pQueId:Int,pQueTitle:String,pQueAnsA:String,pQueAnsB:String,pQueAnsC:String,pQueAnsD:String,pQueAns:Int){
    //问题编号
    var questionId: Int = pQueId
    //问题
    var questionTitle: String =  pQueTitle
    //四个选项
    var answerA: String =  pQueAnsA
    var answerB: String =  pQueAnsB
    var answerC: String =  pQueAnsC
    var answerD: String =  pQueAnsD
    //答案，0->A,1->B,2->C,3->D
    var answer: Int =  pQueAns
    //用户选中的答案,初始为-1
    var selectedAnswer: Int = -1
}