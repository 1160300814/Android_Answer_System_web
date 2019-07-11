package com.hit.software.exam

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

/**
 * 此类与ListView表有关
 */
class PaperListAdapter(pData:ArrayList<PaperItem>,pContext: Context):BaseAdapter (){

    var mData = pData
    var mContext = pContext

    override fun getCount(): Int = mData.size

    override fun getItem(p0: Int): Any   = mData[p0]

    override fun getItemId(p0: Int): Long = mData[p0].paperId.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var cv =p1
        var holder:ViewHolder?
        if (p1 ==null){
            cv = LayoutInflater.from(mContext).inflate(R.layout.paper_list_item,p2,false)
            holder = ViewHolder()
            holder.mPaperTitle = cv?.findViewById(R.id.paper_list_item_title)
            holder.mPaperDiscription = cv?.findViewById(R.id.paper_list_item_discription)
            cv.tag = holder

        }else{
           holder = cv!!.tag as ViewHolder
        }
        holder.mPaperTitle?.text = mData[p0].paperTitle
        holder.mPaperDiscription?.text = mData[p0].paperDuration.toString()
        return cv!!
    }




   internal  class ViewHolder{ // 用来保存布局的内容
       var mPaperTitle : TextView? = null
       var mPaperDiscription:TextView? = null
   }

}