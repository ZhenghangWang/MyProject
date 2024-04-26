
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Button
import com.demo.PocketStore.db.manager.EventDataManager
import android.os.Bundle
import android.content.pm.ActivityInfo
import android.view.Window
import com.demo.PocketStore.R
import android.content.Intent
import android.widget.AdapterView.OnItemClickListener
import com.demo.PocketStore.db.bean.AppData
import java.util.ArrayList
import android.widget.ListView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.widget.AdapterView
import com.demo.PocketStore.db.manager.RatingDataManager
import com.demo.PocketStore.db.bean.RatingData
import com.demo.PocketStore.adapter.RatingListAdapter
import android.widget.RatingBar
import androidx.appcompat.widget.Toolbar
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