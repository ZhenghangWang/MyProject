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

class AppListAdapter(private val list: List<AppData>, private val mContext: Context) :
    BaseAdapter() {
    private var listview: ListView? = null
    private var mUserDataManager: AppDataManager? = null
    private var mDataManager: VolDataManager? = null
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
                R.layout.app_list_item, null
            )
            holder = ViewHolder()
            holder!!.tv_id = convertView.findViewById<View>(R.id.tv_id) as TextView
            holder.tv_volid = convertView.findViewById<View>(R.id.tv_volid) as TextView
            holder.tv_eventid = convertView.findViewById<View>(R.id.tv_eventid) as TextView
            holder.tv_status = convertView.findViewById<View>(R.id.tv_status) as TextView
            holder.tv_name = convertView.findViewById<View>(R.id.tv_name) as TextView
            holder.tv_email = convertView.findViewById<View>(R.id.tv_email) as TextView
            holder.btnOK = convertView.findViewById<View>(R.id.btnOK) as TextView
            holder.btnNO = convertView.findViewById<View>(R.id.btnNO) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val meal = list[position]
        holder!!.tv_id!!.text = meal.id.toString() + ""
        holder.tv_name!!.text =
            mDataManager!!.getStringByColumnName("lastname", meal.volunteer_id) + ""
        holder.tv_email!!.text =
            mDataManager!!.getStringByColumnName("email", meal.volunteer_id) + ""
        holder.tv_volid!!.text = meal.volunteer_id.toString() + ""
        holder.tv_eventid!!.text = meal.event_id.toString() + ""
        holder.tv_status!!.text = meal.status + ""
        holder.btnOK!!.setOnClickListener {
            meal.status = "1"
            modifyDataBase(meal)
        }
        holder.btnNO!!.setOnClickListener {
            meal.status = "-1"
            modifyDataBase(meal)
        }
        return convertView!!
    }

    internal inner class ViewHolder {
        var tv_id: TextView? = null
        var tv_volid: TextView? = null
        var tv_eventid: TextView? = null
        var tv_status: TextView? = null
        var tv_name: TextView? = null
        var tv_email: TextView? = null
        var btnOK: TextView? = null
        var btnNO: TextView? = null
    }

    fun modifyDataBase(mUser: AppData) {
        if (mUserDataManager == null) {
            mUserDataManager = AppDataManager(mContext)
            mUserDataManager!!.openDataBase()
        }
        mUserDataManager!!.openDataBase()
        val flag = mUserDataManager!!.updateDataById(mUser, mUser.id)
        if (flag) {
            Toast.makeText(mContext, "modify successfully", Toast.LENGTH_SHORT).show()
        } else {
        }
    }

    init {
        if (mDataManager == null) {
            mDataManager = VolDataManager(mContext)
            mDataManager!!.openDataBase()
        }
    }
}