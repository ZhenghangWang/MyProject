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
import com.demo.PocketStore.RatingActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.PagerAdapter
import android.widget.LinearLayout
import android.widget.ImageButton
import com.demo.PocketStore.db.manager.UserDataManager
import java.util.HashMap
import android.widget.SimpleAdapter
import android.widget.RelativeLayout
import com.demo.PocketStore.db.manager.RatingDataManager
import com.demo.PocketStore.db.bean.RatingData
import com.demo.PocketStore.adapter.RatingListAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import android.view.LayoutInflater
import android.view.ViewGroup
import com.demo.PocketStore.adapter.ManagerListAdapter
import com.demo.PocketStore.ResolveIssueActivity
import com.demo.PocketStore.SendMsgActivity
import com.demo.PocketStore.RecMsgActivity
import android.content.DialogInterface
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import android.widget.RatingBar
import com.demo.PocketStore.db.manager.MsgDataManager
import com.demo.PocketStore.db.bean.MsgData
import com.demo.PocketStore.adapter.MsgListAdapter
import com.demo.PocketStore.db.manager.IssueDataManager
import com.demo.PocketStore.db.bean.IssueData
import com.demo.PocketStore.adapter.IssueListAdapter
import android.widget.Spinner
import com.demo.PocketStore.db.bean.Volunteer
import com.demo.PocketStore.db.manager.VolDataManager
import android.widget.ArrayAdapter
import java.util.stream.Collectors
import java.util.Locale
import androidx.appcompat.widget.AppCompatButton
import android.os.Build
import android.content.pm.PackageManager
import androidx.appcompat.widget.Toolbar
import com.demo.PocketStore.HomeActivity
import com.demo.PocketStore.MainActivity
import com.demo.PocketStore.SignupActivity
import com.demo.PocketStore.SigninActivity
import com.demo.PocketStore.common.Config.curUser

//EventDetials
class EventDetialsActivity : AppCompatActivity(), View.OnClickListener, OnItemClickListener {
    private val TAG = "EventDetialsActivity"
    var toolbar: Toolbar? = null
    var org_id: TextView? = null
    var input_title: TextView? = null
    var input_desc: TextView? = null
    var input_date: TextView? = null
    var input_max: TextView? = null
    var input_cur: TextView? = null
    var input_dur: TextView? = null
    var input_loc: TextView? = null
    var input_skills: TextView? = null
    private var btnOk: Button? = null
    private var mUserDataManager: EventDataManager? = null
    var eventData: EventData? = null
    private var mDataManager: AppDataManager? = null
    private val dataList: MutableList<AppData> = ArrayList()
    var list: ListView? = null
    var adapter2: AppListAdapter? = null
    private var swipe: SwipeRefreshLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // hide ActionBar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eventdetails)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar!!.title = "Event Details"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { //update return last activity
            setResult(RESULT_OK, Intent())
            finish()
        }
        initViews()
        if (mUserDataManager == null) {
            mUserDataManager = EventDataManager(this)
            mUserDataManager!!.openDataBase()
        }
    }

    private fun initViews() {
        list = findViewById(R.id.list)
        list?.setOnItemClickListener(this)
        btnOk = findViewById<View>(R.id.btn_ok) as Button
        org_id = findViewById(R.id.org_id)
        input_title = findViewById(R.id.input_title)
        input_cur = findViewById(R.id.input_cur)
        input_dur = findViewById(R.id.input_dur)
        input_date = findViewById(R.id.input_date)
        input_desc = findViewById(R.id.input_desc)
        input_loc = findViewById(R.id.input_loc)
        input_max = findViewById(R.id.input_max)
        input_skills = findViewById(R.id.input_skills)
        swipe = findViewById(R.id.swipe)
        btnOk!!.setOnClickListener(this)
        val intent = intent
        eventData = intent.getSerializableExtra("eventdata") as EventData?
        if (eventData != null) {
            initdata()
        }
    }

    private fun initdata() {
        org_id!!.text = "organisation_id:" + curUser!!.userId + ""
        input_title!!.text = "title:" + eventData!!.title + ""
        input_desc!!.text = "description:" + eventData!!.description + ""
        input_date!!.text = "date:" + eventData!!.date + ""
        input_max!!.text = "max_application:" + eventData!!.max_application
        input_cur!!.text = "current_application:" + eventData!!.current_application
        input_dur!!.text = "duration:" + eventData!!.duration
        input_loc!!.text = "location:" + eventData!!.location
        input_skills!!.text = "skills_required:" + eventData!!.skills_required
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
            loadJoinIn()
        }
        loadJoinIn()
    }

    private fun loadJoinIn() {
        if (mDataManager == null) {
            mDataManager = AppDataManager(this)
            mDataManager!!.openDataBase()
        }
        dataList.clear()
        val appDataList = mDataManager!!.allDataList
        for (appData in appDataList) {
            if (appData.event_id == eventData!!.id) {
                dataList.add(appData)
            }
        }
        adapter2 = AppListAdapter(dataList, this)
        list!!.adapter = adapter2
        adapter2!!.notifyDataSetChanged()
    }

    override fun onClick(v: View) {
//        for (AppData data:dataList){
//            if (data.volunteer_id==curUser.getUserId()){
//                Toast.makeText(getApplicationContext(), "already join in!", Toast.LENGTH_SHORT).show();
//                return ;
//            }
//        }
        Log.e(TAG, "current:" + input_cur!!.text.toString())
        val mUser = AppData(1, eventData!!.id, "0")
        mDataManager!!.openDataBase()
        val flag = mDataManager!!.insertData(mUser)
        if (flag == -1L) {
            Toast.makeText(applicationContext, "add failed....", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "add successfully!", Toast.LENGTH_SHORT).show()
            //  startActivity(new Intent(this, ShowUserInfoActivity.class));
            adapter2!!.notifyDataSetChanged()
            eventData!!.current_application++
            input_cur!!.text = "current_application:" + eventData!!.current_application
            mUserDataManager!!.updateUserData(eventData!!)
        }
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val appData = parent.adapter.getItem(position) as AppData
        val intent = Intent(this, RatingActivity::class.java)
        intent.putExtra("appData", appData)
        startActivity(intent)
    }
}