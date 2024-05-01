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
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import com.demo.PocketStore.db.manager.AppDataManager
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
import android.widget.SimpleAdapter
import android.widget.RelativeLayout
import com.demo.PocketStore.db.manager.RatingDataManager
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
import com.demo.PocketStore.adapter.MsgListAdapter
import com.demo.PocketStore.db.manager.IssueDataManager
import com.demo.PocketStore.adapter.IssueListAdapter
import android.widget.Spinner
import com.demo.PocketStore.db.manager.VolDataManager
import android.widget.ArrayAdapter
import java.util.stream.Collectors
import androidx.appcompat.widget.AppCompatButton
import android.os.Build
import android.content.pm.PackageManager
import com.demo.PocketStore.HomeActivity
import com.demo.PocketStore.MainActivity
import com.demo.PocketStore.SignupActivity
import com.demo.PocketStore.SigninActivity
import com.demo.PocketStore.db.bean.*
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : Activity(), View.OnClickListener, OnItemClickListener {
    private val REQUEST_CODE_1 = 1
    private val REQUEST_CODE_2 = 2
    private var mViewPager: ViewPager? = null
    private var mAdapter: PagerAdapter? = null
    private val mViews: MutableList<View> = ArrayList()
    private var mTabWeixin: LinearLayout? = null
    private var mTabFrd: LinearLayout? = null
    private var mTabSetting: LinearLayout? = null
    private var mWeixinImg: ImageButton? = null
    private var mFrdImg: ImageButton? = null
    private var mSettingImg: ImageButton? = null
    private var tvWeixin: TextView? = null
    private var tvFrd: TextView? = null
    private var tvSetting: TextView? = null
    private var tvTopTitle: TextView? = null
    private val llNewFriend: LinearLayout? = null
    private val listView: ListView? = null
    private var listManager: ListView? = null
    private var mUserDataManager: UserDataManager? = null
    private var userDataList: List<UserData> = ArrayList()
    private val mHashMap: HashMap<String, Any>? = null
    private val mSimpleAdapter: SimpleAdapter? = null
    var mContext: Context? = null
    private var username: String? = null
    private val loanItems = 0
    var l_issue_send: RelativeLayout? = null
    var l_msg_send: LinearLayout? = null
    var l_msg_rec: LinearLayout? = null
    private var mDataManager: RatingDataManager? = null
    private val dataList: MutableList<RatingData> = ArrayList()
    var list: ListView? = null
    var adapter2: RatingListAdapter? = null
    private var swipe: SwipeRefreshLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_home)
        mContext = this
        username = intent.getStringExtra("username")
        initView()
        initData()
        initEvents()
    }

    private fun initEvents() {
        mTabWeixin!!.setOnClickListener(this)
        mTabFrd!!.setOnClickListener(this)
        mTabSetting!!.setOnClickListener(this)
        //viewpager滑动事件
        mViewPager!!.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(arg0: Int) { //当viewpager滑动时，对应的底部导航按钮的图片要变化
                val currentItem = mViewPager!!.currentItem
                resetImg()
                when (currentItem) {
                    0 -> {
                        mWeixinImg!!.setImageResource(R.drawable.tab_01_pressed)
                        tvWeixin!!.setTextColor(resources.getColor(R.color.colorAccentBlue))
                        tvTopTitle!!.text = "Approval"
                    }
                    1 -> {
                        mFrdImg!!.setImageResource(R.drawable.tab_02_pressed)
                        tvFrd!!.setTextColor(resources.getColor(R.color.colorAccentBlue))
                        tvTopTitle!!.text = "Message"
                    }
                    2 -> {
                        mSettingImg!!.setImageResource(R.drawable.tab_03_pressed)
                        tvSetting!!.setTextColor(resources.getColor(R.color.colorAccentBlue))
                        tvTopTitle!!.text = "Resolve"
                    }
                    else -> {}
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
                // TODO Auto-generated method stub
            }

            override fun onPageScrollStateChanged(arg0: Int) {
                // TODO Auto-generated method stub
            }
        })
    }

    private fun initView() { //init all view
        mViewPager = findViewById<View>(R.id.id_viewpager) as ViewPager
        //tabs
        mTabWeixin = findViewById<View>(R.id.id_tab_weixin) as LinearLayout
        mTabFrd = findViewById<View>(R.id.id_tab_frd) as LinearLayout
        mTabSetting = findViewById<View>(R.id.id_tab_setting) as LinearLayout
        //imagebutton
        mWeixinImg = findViewById<View>(R.id.id_tab_weixin_img) as ImageButton
        mFrdImg = findViewById<View>(R.id.id_tab_frd_img) as ImageButton
        mSettingImg = findViewById<View>(R.id.id_tab_setting_img) as ImageButton
        //Bottom TextView
        tvWeixin = findViewById<View>(R.id.id_tab_weixin_tv) as TextView
        tvFrd = findViewById<View>(R.id.id_tab_frd_tv) as TextView
        tvSetting = findViewById<View>(R.id.id_tab_setting_tv) as TextView
        tvTopTitle = findViewById<View>(R.id.tv_top_title) as TextView
        val mInflater = LayoutInflater.from(this)
        val tab01 = mInflater.inflate(R.layout.tab01, null)
        val tab02 = mInflater.inflate(R.layout.tab02, null)
        val tab03 = mInflater.inflate(R.layout.tab03, null)
        listManager = tab01.findViewById<View>(R.id.listManager) as ListView
        mViews.add(tab01)
        mViews.add(tab02)
        mViews.add(tab03)
        //init PagerAdapter
        mAdapter = object : PagerAdapter() {
            override fun destroyItem(
                container: ViewGroup, position: Int,
                `object`: Any
            ) {
                // TODO Auto-generated method stub
                container.removeView(mViews[position])
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val view = mViews[position]
                container.addView(view)
                return view
            }

            override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
                // TODO Auto-generated method stub
                return arg0 === arg1
            }

            override fun getCount(): Int {
                return mViews.size
            }
        }
        mViewPager!!.adapter = mAdapter
        l_issue_send = tab02.findViewById(R.id.l_issue_send)
        l_issue_send?.setOnClickListener(this)
        l_msg_send = tab02.findViewById(R.id.l_msg_send)
        l_msg_send?.setOnClickListener(this)
        l_msg_rec = tab02.findViewById(R.id.l_msg_rec)
        l_msg_rec?.setOnClickListener(this)
        list = tab03.findViewById(R.id.list)
        list?.setOnItemClickListener(this)
        swipe = tab03.findViewById(R.id.swipe)
        swipe?.setColorSchemeColors(resources.getColor(R.color.red), resources.getColor(R.color.red))
        //设置向下拉多少出现刷新
        swipe?.setDistanceToTriggerSync(200)
        //设置刷新出现的位置
        swipe?.setProgressViewEndTarget(false, 200)
        swipe?.setOnRefreshListener(OnRefreshListener {
            swipe?.setRefreshing(false)
            initData()
        })
    }

    private fun initData() {
        if (mUserDataManager == null) {
            mUserDataManager = UserDataManager(this)
            mUserDataManager!!.openDataBase() //建立本地数据库
        }
       // userDataList
        mUserDataManager!!.openDataBase()
        userDataList = mUserDataManager!!.allUserDataList
        val adapter = ManagerListAdapter(userDataList, this)
        listManager!!.adapter = adapter
        adapter.notifyDataSetChanged()
        if (mDataManager == null) {
            mDataManager = RatingDataManager(this)
            mDataManager!!.openDataBase()
        }
        dataList.clear()
        val appDataList = mDataManager!!.allDataList
        for (ratingData in appDataList) {
            if (ratingData.rating <= 3) {
                dataList.add(ratingData)
            }
        }
        adapter2 = RatingListAdapter(dataList, this)
        list!!.adapter = adapter2
        adapter2!!.notifyDataSetChanged()
    }

    override fun onClick(v: View) {
        resetImg() //
        when (v.id) {
            R.id.id_tab_weixin -> {
                mViewPager!!.currentItem = 0
                mWeixinImg!!.setImageResource(R.drawable.tab_01_pressed) //并将按钮颜色点亮
                tvWeixin!!.setTextColor(resources.getColor(R.color.colorAccentBlue)) //并将按钮颜色点亮
                tvTopTitle!!.text = "Approval"
            }
            R.id.id_tab_frd -> {
                mViewPager!!.currentItem = 1
                mFrdImg!!.setImageResource(R.drawable.tab_02_pressed)
                tvFrd!!.setTextColor(resources.getColor(R.color.colorAccentBlue)) //并将字体颜色点亮
                tvTopTitle!!.text = "Message"
            }
            R.id.id_tab_setting -> {
                mViewPager!!.currentItem = 3
                mSettingImg!!.setImageResource(R.drawable.tab_03_pressed)
                tvSetting!!.setTextColor(resources.getColor(R.color.colorAccentBlue)) //并将字体颜色点亮
                tvTopTitle!!.text = "Resolve"
            }
            R.id.l_issue_send -> startActivity(Intent(this, ResolveIssueActivity::class.java))
            R.id.l_msg_send -> startActivity(Intent(this, SendMsgActivity::class.java))
            R.id.l_msg_rec -> startActivity(Intent(this, RecMsgActivity::class.java))
            else -> {}
        }
    }

    private fun resetImg() {
        mWeixinImg!!.setImageResource(R.drawable.tab_01_normal)
        mFrdImg!!.setImageResource(R.drawable.tab_02_normal)
        mSettingImg!!.setImageResource(R.drawable.tab_03_normal)
        tvWeixin!!.setTextColor(resources.getColor(R.color.black))
        tvFrd!!.setTextColor(resources.getColor(R.color.black))
        tvSetting!!.setTextColor(resources.getColor(R.color.black))
    }

    private val currentDateTime: String
        private get() {
            val format = SimpleDateFormat("MMM d yyyy")
            return format.format(Date())
        }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val ratingData = parent.adapter.getItem(position) as RatingData
        AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("Delete")
            .setMessage("Delete this Rating ?").setPositiveButton("Yes") { dialogInterface, i ->
                mDataManager!!.deleteUserData(ratingData.id)
                initData()
                Toast.makeText(applicationContext, "Delete Successfully!", Toast.LENGTH_SHORT)
                    .show()
            }.setNegativeButton("No", null).show()
    }

    companion object {
        const val RESULT_OK = 0
    }
}