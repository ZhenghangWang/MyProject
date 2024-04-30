
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

//Rating
class MyRatingActivity : AppCompatActivity(), View.OnClickListener, OnItemClickListener {
    private val TAG = "MyRatingActivity"
    var toolbar: Toolbar? = null
    var etInput: EditText? = null
    private var btnOk: Button? = null
    private var mUserDataManager: EventDataManager? = null
    var appData: AppData? = null
    private var mDataManager: RatingDataManager? = null
    private val dataList: MutableList<RatingData> = ArrayList()
    var list: ListView? = null
    var adapter2: RatingListAdapter? = null
    private var swipe: SwipeRefreshLayout? = null
    var mRatingBar: RatingBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // hide ActionBar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myrating)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar!!.title = "My Rating"
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
        mRatingBar = findViewById<View>(R.id.rb_weidao) as RatingBar
        list = findViewById(R.id.list)
        list?.setOnItemClickListener(this)
        btnOk = findViewById<View>(R.id.btn_ok) as Button
        etInput = findViewById(R.id.et_input)
        swipe = findViewById(R.id.swipe)
        btnOk!!.setOnClickListener(this)
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
            mDataManager = RatingDataManager(this)
            mDataManager!!.openDataBase()
        }
        dataList.clear()
        val appDataList = mDataManager!!.allDataList
        for (ratingData in appDataList) {
            if (curUser!!.userId == ratingData.volunteer_id) {
                dataList.add(ratingData)
            }
        }
        adapter2 = RatingListAdapter(dataList, this)
        list!!.adapter = adapter2
        adapter2!!.notifyDataSetChanged()
    }

    override fun onClick(v: View) {}
    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
}