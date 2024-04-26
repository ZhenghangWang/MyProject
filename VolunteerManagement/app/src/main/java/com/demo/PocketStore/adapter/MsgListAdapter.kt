package com.demo.PocketStore.adapter

import com.demo.PocketStore.db.bean.AppData
import android.widget.BaseAdapter
import android.widget.ListView
import com.demo.PocketStore.db.manager.AppDataManager
import com.demo.PocketStore.db.manager.VolDataManager
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import com.demo.PocketStore.R
import android.widget.TextView
import android.widget.Toast
import com.demo.PocketStore.db.bean.MsgData
import com.demo.PocketStore.db.manager.RatingDataManager
import com.demo.PocketStore.db.manager.UserDataManager
import com.demo.PocketStore.db.bean.EventData
import android.widget.ImageView
import android.widget.Button
import com.demo.PocketStore.db.bean.IssueData
import com.demo.PocketStore.db.manager.IssueDataManager
import com.demo.PocketStore.db.bean.RatingData
import java.util.ArrayList
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlin.Throws
import android.content.ContentValues
import android.database.Cursor
import com.demo.PocketStore.db.manager.MsgDataManager
import com.demo.PocketStore.db.bean.Volunteer
import com.demo.PocketStore.db.manager.EventDataManager
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.AdapterView.OnItemClickListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.demo.PocketStore.adapter.EventListAdapter
import android.os.Bundle
import com.demo.PocketStore.ui.home.HomeViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import android.content.DialogInterface
import android.content.Intent
import com.demo.PocketStore.AddEventActivity
import com.demo.PocketStore.ShowReportActivity
import com.demo.PocketStore.utils.ListSortUtils
import android.widget.AdapterView
import com.demo.PocketStore.EventDetialsActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.demo.PocketStore.ui.dashboard.DashboardViewModel
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.widget.EditText
import android.text.InputType
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RelativeLayout
import android.widget.LinearLayout
import com.demo.PocketStore.ui.notifications.NotificationsViewModel
import com.demo.PocketStore.SendIssueActivity
import com.demo.PocketStore.SendMsgActivity
import com.demo.PocketStore.RecMsgActivity
import java.util.Comparator
import java.util.Collections

class MsgListAdapter(private val list: List<MsgData>, private val mContext: Context) :
    BaseAdapter() {
    private var listview: ListView? = null
    private val mUserDataManager: RatingDataManager? = null
    private var mDataManager: UserDataManager? = null
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (listview == null) {
            listview = parent as ListView
        }
        var holder: ViewHolder? = null
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.context).inflate(
                R.layout.msg_list_item, null
            )
            holder = ViewHolder()
            holder!!.tv_id = convertView.findViewById<View>(R.id.tv_id) as TextView
            holder.tv_name = convertView.findViewById<View>(R.id.tv_name) as TextView
            holder.tv_msg = convertView.findViewById<View>(R.id.tv_msg) as TextView
            holder.tv_date = convertView.findViewById<View>(R.id.tv_date) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val meal = list[position]
        holder!!.tv_id!!.text = meal.id.toString() + ""
        holder.tv_name!!.text = mDataManager!!.getStringByColumnName("name", meal.sender_id) + ""
        holder.tv_msg!!.text = meal.message + ""
        holder.tv_date!!.text = meal.sent_time + ""
        return convertView!!
    }

    internal inner class ViewHolder {
        var tv_id: TextView? = null
        var tv_name: TextView? = null
        var tv_msg: TextView? = null
        var tv_date: TextView? = null
    }

    init {
        if (mDataManager == null) {
            mDataManager = UserDataManager(mContext)
            mDataManager?.openDataBase()
        }
    }
}