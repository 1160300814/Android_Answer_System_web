package com.hit.software.exam

/**
 * 此结构是试卷结构，主要应用于ExamPaper.java类
 */
class PaperItem (pPaperId:String,pPaperTitle:String,pPaperStartTime:String,pPaperDuration:Int) {
    var paperId: String = pPaperId//试卷ID
    var paperTitle: String =  pPaperTitle//试卷名称
    var paperStartTime: String =  pPaperStartTime//试卷创建时间
    var paperDuration: Int =  pPaperDuration//时间
}