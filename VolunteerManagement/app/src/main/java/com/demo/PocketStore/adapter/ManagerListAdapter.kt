package com.demo.PocketStore.adapter

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
import com.demo.PocketStore.db.manager.RatingDataManager
import com.demo.PocketStore.db.manager.UserDataManager
import android.widget.ImageView
import android.widget.Button
import com.demo.PocketStore.db.manager.IssueDataManager
import java.util.ArrayList
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlin.Throws
import android.content.ContentValues
import android.database.Cursor
import com.demo.PocketStore.db.manager.MsgDataManager
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
import com.demo.PocketStore.db.bean.*
import java.util.Comparator
import java.util.Collections

class ManagerListAdapter(private val list: List<UserData>, private val mContext: Context) :
    BaseAdapter() {
    private var listview: ListView? = null
    private var mUserDataManager: UserDataManager? = null
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
                R.layout.manager_list_item, null
            )
            holder = ViewHolder()
            holder!!.iv = convertView.findViewById<View>(R.id.item_iv_cover) as ImageView
            holder.item_tv_id = convertView.findViewById<View>(R.id.item_tv_id) as TextView
            holder.item_tv_title = convertView.findViewById<View>(R.id.item_tv_title) as TextView
            holder.item_tv_desc = convertView.findViewById<View>(R.id.item_tv_desc) as TextView
            holder.btnOK = convertView.findViewById<View>(R.id.btnOK) as Button
            holder.btnNO = convertView.findViewById<View>(R.id.btnNO) as Button
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val meal = list[position]
        holder!!.item_tv_id?.setText(meal.userId.toString() + "")
        holder.item_tv_title?.setText(meal.userName + "")
        holder.item_tv_desc!!.text = "Status: " + meal.userStatus + ""
        holder.btnOK!!.setOnClickListener {
            meal.userStatus = "1"
            modifyDataBase(meal)
        }
        holder.btnNO!!.setOnClickListener {
            meal.userStatus = "-1"
            modifyDataBase(meal)
        }
        return convertView!!
    }

    internal inner class ViewHolder {
        var iv: ImageView? = null
        var item_tv_id: TextView? = null
        var item_tv_title: TextView? = null
        var item_tv_type: TextView? = null
        var item_tv_desc: TextView? = null
        var item_tv_price: TextView? = null
        var item_tv_date: TextView? = null
        var btnOK: Button? = null
        var btnNO: Button? = null
    }

    fun modifyDataBase(mUser: UserData) {
        if (mUserDataManager == null) {
            mUserDataManager = UserDataManager(mContext)
            mUserDataManager!!.openDataBase()
        }
        val flag = mUserDataManager!!.updateUserDataById(mUser, mUser.userId)
        if (flag) {
            Toast.makeText(mContext, "modify successfully", Toast.LENGTH_SHORT).show()
        } else {
        }
    }
}