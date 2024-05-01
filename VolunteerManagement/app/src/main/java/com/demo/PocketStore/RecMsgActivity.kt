package com.demo.PocketStore
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Button
import com.demo.PocketStore.db.manager.EventDataManager
import android.os.Bundle
import android.content.pm.ActivityInfo
import android.view.Window
import com.demo.PocketStore.R
import android.app.Activity
import android.content.Intent
import android.util.Log
import com.demo.PocketStore.db.bean.EventData
import android.widget.Toast
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import com.demo.PocketStore.db.manager.AppDataManager
import com.demo.PocketStore.db.bean.AppData
import java.util.ArrayList
import android.widget.ListView
import com.demo.PocketStore.adapter.AppListAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import android.widget.AdapterView

import com.demo.PocketStore.db.manager.MsgDataManager
import com.demo.PocketStore.db.bean.MsgData
import com.demo.PocketStore.adapter.MsgListAdapter
import androidx.appcompat.widget.Toolbar

import com.demo.PocketStore.common.Config.curUser

//Receive message
class RecMsgActivity : AppCompatActivity(), View.OnClickListener, OnItemClickListener {
    private val TAG = "RecMsgActivity"
    var toolbar: Toolbar? = null
    var appData: AppData? = null
    private var mDataManager: MsgDataManager? = null
    private val dataList: MutableList<MsgData> = ArrayList()
    var list: ListView? = null
    var adapter2: MsgListAdapter? = null
    private var swipe: SwipeRefreshLayout? = null
   // var mRatingBar: RatingBar? = null
    var curType = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // hide ActionBar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_msg_received)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar!!.title = "Message received"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { //update return last activity
            setResult(RESULT_OK, Intent())
            finish()
        }
        curType = if (curUser!!.userName == "admin") {
            3
        } else {
            2
        }
        initViews()
    }

    private fun initViews() {
      //  mRatingBar = findViewById<View>(R.id.rb_weidao) as RatingBar
        list = findViewById(R.id.list)
        list?.setOnItemClickListener(this)
        swipe = findViewById(R.id.swipe)
        initdata()
    }

    private fun initdata() {
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
            loadRating()
        }
        loadRating()
    }

    private fun loadRating() {
        if (mDataManager == null) {
            mDataManager = MsgDataManager(this)
            mDataManager!!.openDataBase()
        }
        val appDataList = mDataManager!!.allDataList
        for (ratingData in appDataList) {
            if (curUser!!.userId == ratingData.receiver_id && ratingData.receiver_type == curType) {
                dataList.add(ratingData)
            }
        }
        adapter2 = MsgListAdapter(dataList, this)
        list!!.adapter = adapter2
        adapter2!!.notifyDataSetChanged()
    }

    override fun onClick(v: View) {}
    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
}