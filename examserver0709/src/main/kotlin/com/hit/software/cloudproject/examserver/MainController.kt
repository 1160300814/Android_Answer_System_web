package com.hit.software.cloudproject.examserver

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowCallbackHandler
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.ResultSet
import java.util.*

@Controller
@RequestMapping("/")
class MainController {

    //定义查询数据库对象
    @Autowired
    var mJdbcTemplate: JdbcTemplate? = null

    /**
     * 首页登录
     */
    @RequestMapping("/")
    fun firstMethod(): String {
        return "sign-in"
    }


    /**
     * 登录成功页面
     */
    @RequestMapping("/index")
    fun mainPage(): String {
        return "index"
    }

    @RequestMapping("/user")
    fun getUser(): String {
        return "user"
    }

    @RequestMapping("/media")
    fun getMedia(): String? {
        return "media"
    }

    @RequestMapping("/calendar")
    fun getCalendar(): String? {
        return "calendar"
    }
    @RequestMapping("/privacy-policy")
    fun getPrivacyPolicy(): String? {
        return "privacy-policy"
    }
    @RequestMapping("/privacy-terms-and-conditions")
    fun  getTermsAndConditions(): String? {
        return "terms-and-conditions"
    }
    @RequestMapping("/help")
    fun  getHelp(): String? {
        return "help"
    }
    @RequestMapping("/faq")
    fun  getFaq(): String? {
        return "faq"
    }


    @ResponseBody
    @PostMapping("/check")
    fun checkUser(@RequestBody login: Login): String? {
        /**
         * 处理数据
         * 查询数据库
         */
        var hasResult = false
        val sql = "select * from user_info where user_name = '${login.user_name}'"
        println(sql)
        mJdbcTemplate?.query(sql, object : RowCallbackHandler {
            override fun processRow(rs: ResultSet) {
                println("has Result")
                hasResult = true
            }
        })
        return if (hasResult) {
            "{\"code\":\"100\",\"result\":\"success\",\"location\":\"index\"}"
        } else {
            "{\"code\":\"101\",\"result\":\"Login failure\"}"
        }

    }


    /**
     * 登录验证
     */

    @ResponseBody
    @PostMapping("/login")
    fun loginMethod(@RequestBody login: Login): String? {
        /**
         * 处理数据
         * 查询数据库
         */
        var hasResult = false
        //查询数据库中是否存在输入的用户名密码
        val sql = "select * from user_info where user_name = '${login.user_name}' and user_password = '${login.user_password}'"
        println(sql)
        mJdbcTemplate?.query(sql, object : RowCallbackHandler {
            override fun processRow(rs: ResultSet) {
                println("has Result")
                hasResult = true
            }
        })
        //如果再数据库中有查询结果，返回正确
        return if (hasResult) {
            "{\"code\":\"100\",\"result\":\"success\",\"location\":\"index\"}"
        } else {
            "{\"code\":\"101\",\"result\":\"Login failure\"}"
        }

    }

    /**
     * 获取试卷
     */

    @ResponseBody
    @PostMapping("/paper")
    fun paperMethod(@RequestBody paper: Paper): String? {
        /**
         * 处理数据
         * 查询数据库
         */
        var hasResult = false
        //根据试卷id查询数据库中该试卷的全部信息
        val sql = "select * from exam_paper where id = '${paper.id}' "
        var re = ""
        println(sql)
        mJdbcTemplate?.query(sql, object : RowCallbackHandler {
            override fun processRow(rs: ResultSet) {
                println(rs.getString("id"))
                re = "{\"code\":\"100\",\"result\":\"success\"" +                                   //正确代码
                        ",\"id\":\"${rs.getString("id")}\""+                          //试卷id
                        ",\"ep_title\":\"${rs.getString("ep_title")}\"" +             //试卷标题
                        ",\"ep_start_time\":\"${rs.getString("ep_start_time")}\"" +   //试卷开始时间
                        ",\"ep_duration\":\"${rs.getString("ep_duration")}\"}"        //试卷答题时长
                print(re)
                hasResult = true
            }
        })
        //返回给Android
        return if (hasResult) {
            re
        } else {
            "{\"code\":\"101\",\"result\":\"Paper failure\"}"
        }
    }

    /**
     * 获取所选试卷的所有的试题号
     */

    @ResponseBody
    @PostMapping("/paperquestions")
    fun paperMethod(@RequestBody paperq: PaperQuestions): String? {
        /**
         * 处理数据
         * 查询数据库
         */
        var hasResult = false
        //根据试卷id在试卷详细表中进行查询
        val sql1 = "select * from exam_have_question where ehp_id = '${paperq.ehp_id}' "
        //正确代码
        var re = "{\"code\":\"100\",\"result\":\"success\""
        println(sql1)
        var j = 0
        mJdbcTemplate?.query(sql1, object : RowCallbackHandler {
            override fun processRow(rs: ResultSet) {
                println(rs.getString("id"))
                //对结果json进行拼接，将试卷中所有题号拼接在一个json中，各自的标签分别是ehp_que + 索引值（j）
                re = re + ",\"ehp_que" +
                        "${j}" +
                        "\":\"${rs.getString("ehp_que")}\""
                j = j + 1
                print(re)
                hasResult = true
            }
        })
        //拼接json，得到一张卷子的题目总数
        re = re  + ",\"num\":\"$j\""
        println(re)
        //根据试卷id，查询数据库，得到试卷答题时长
        val sql2 = "select * from exam_paper where id = '${paperq.ehp_id}' "
        mJdbcTemplate?.query(sql2, object : RowCallbackHandler {
            override fun processRow(rs: ResultSet) {
                println(rs.getString("id"))
                re = re + ",\"ep_duration\":\"${rs.getString("ep_duration")}\"}"
                print(re)
                hasResult = true
            }
        })
        //返回给Android
        return if (hasResult) {
            re
        } else {
            "{\"code\":\"101\",\"result\":\"Paper Questions failure\"}"
        }
    }

    /**
     * 获取对应试题号的详细信息，题目，选项，正确答案
     */
    @ResponseBody
    @PostMapping("/questions")
    fun paperMethod(@RequestBody q: Question): String? {
        /**
         * 处理数据
         * 查询数据库
         */
        var hasResult = false
        //根据题目id查询题目数据库，得到详细信息
        val sql = "select * from question  where q_id = '${q.q_id}' "
        var re = ""
        println(sql)
        mJdbcTemplate?.query(sql, object : RowCallbackHandler {
            override fun processRow(rs: ResultSet) {
                re = "{\"code\":\"100\",\"result\":\"success\"" +                        //正确代码
                        ",\"q_id\":\"${rs.getString("q_id")}\""+           //问题id
                        ",\"question\":\"${rs.getString("question")}\"" +  //问题内容
                        ",\"a\":\"${rs.getString("a")}\"" +                //选项A
                        ",\"b\":\"${rs.getString("b")}\"" +                //选项B
                        ",\"c\":\"${rs.getString("c")}\"" +                //选项C
                        ",\"d\":\"${rs.getString("d")}\"" +                //选项D
                        ",\"correct\":\"${rs.getString("correct")}\"}"     //正确答案
                print(re)
                hasResult = true
            }
        })
        return if (hasResult) {
            //返回给Android
            re
        } else {
            "{\"code\":\"101\",\"result\":\"Questions failure\"}"
        }
    }

}