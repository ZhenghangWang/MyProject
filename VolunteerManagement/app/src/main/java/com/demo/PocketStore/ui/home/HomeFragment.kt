package com.demo.PocketStore.ui.home

import MyRatingActivity
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import com.demo.PocketStore.R
import com.demo.PocketStore.db.bean.MsgData
import com.demo.PocketStore.db.manager.RatingDataManager
import com.demo.PocketStore.db.manager.UserDataManager
import com.demo.PocketStore.db.bean.EventData
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
import com.demo.PocketStore.EventDetialsActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.demo.PocketStore.ui.dashboard.DashboardViewModel
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.text.InputType
import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.provider.MediaStore
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.*
import androidx.fragment.app.Fragment
import com.demo.PocketStore.ui.notifications.NotificationsViewModel
import com.demo.PocketStore.SendIssueActivity
import com.demo.PocketStore.SendMsgActivity
import com.demo.PocketStore.RecMsgActivity
import com.demo.PocketStore.common.Config
import com.demo.PocketStore.databinding.FragmentHomeBinding
import java.util.Comparator
import java.util.Collections

class HomeFragment : Fragment(), OnItemLongClickListener, OnItemClickListener {
    private var binding: FragmentHomeBinding? = null
    private var listManager: ListView? = null
    private var searchView: SearchView? = null
    private var swipe: SwipeRefreshLayout? = null
    private var btnok: ImageView? = null
    private var mDataManager: EventDataManager? = null
    private var dataList: List<EventData> = ArrayList()
    var adapter2: EventListAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val homeViewModel = ViewModelProvider(this).get(
            HomeViewModel::class.java
        )
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        listManager = binding!!.list
        listManager!!.onItemClickListener = this
        listManager!!.onItemLongClickListener = this
        searchView = binding!!.searchview
        btnok = binding!!.btnOk
        btnok!!.setOnClickListener { showMenuDialog() }
        swipe = binding!!.swipe
        //改变加载显示的颜色
        swipe!!.setColorSchemeColors(
            resources.getColor(R.color.red),
            resources.getColor(R.color.red)
        )
        //设置向下拉多少出现刷新
        swipe!!.setDistanceToTriggerSync(200)
        //设置刷新出现的位置
        swipe!!.setProgressViewEndTarget(false, 200)
        swipe!!.setOnRefreshListener {
            swipe!!.isRefreshing = false
            initData()
        }
        initData()
        return root
    }

    private fun showMenuDialog() {
        AlertDialog.Builder(context).setTitle("please select")
            .setItems(
                arrayOf("add event", "show report", "order by title", "order by date", "My Rating")
            ) { dialog, which ->
                when (which) {
                    0 -> startActivity(Intent(activity, AddEventActivity::class.java))
                    1 -> startActivity(Intent(activity, ShowReportActivity::class.java))
                    2 -> {
                        ListSortUtils.listSortingByDate(dataList)
                        adapter2!!.notifyDataSetChanged()
                    }
                    3 -> {
                        ListSortUtils.listSortingByName(dataList)
                        adapter2!!.notifyDataSetChanged()
                    }
                    4 -> startActivity(Intent(activity, MyRatingActivity::class.java))
                }
            }.create().show()
    }

    //初始化
    private fun initData() {
        if (mDataManager == null) {
            mDataManager = EventDataManager(activity)
            mDataManager!!.openDataBase()
        }
       // dataList!!.clear()
        Config.myEventList.clear()
        dataList = mDataManager!!.allDataList;
        for (eventData in dataList!!) {
            if (eventData!!.organisation_id == Config.curUser!!.userId) {
                Config.myEventList.add(eventData)
            }
        }
        adapter2 = EventListAdapter(dataList, activity)
        listManager!!.adapter = adapter2
        adapter2!!.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onItemLongClick(
        parent: AdapterView<*>,
        view: View,
        position: Int,
        id: Long
    ): Boolean {
        val eventData = parent.adapter.getItem(position) as EventData
        AlertDialog.Builder(activity).setIcon(R.mipmap.ic_launcher).setTitle("delete")
            .setMessage("delete this event?")
            .setPositiveButton("confirm") { dialogInterface, i -> //ToDo: 你想做的事情
                mDataManager!!.openDataBase()
                mDataManager!!.deleteUserData(eventData.id)
                initData()
                Toast.makeText(activity, "delete successfully", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("cancel", null).show()
        return true
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val eventData = parent.adapter.getItem(position) as EventData
        val intent = Intent(activity, EventDetialsActivity::class.java)
        intent.putExtra("eventdata", eventData)
        startActivity(intent)
    }
}