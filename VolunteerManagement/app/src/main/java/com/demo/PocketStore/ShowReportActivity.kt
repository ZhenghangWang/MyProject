package com.demo.PocketStore

import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.os.Bundle
import android.view.Window
import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.demo.PocketStore.db.manager.AppDataManager
import com.demo.PocketStore.db.bean.AppData
import java.util.ArrayList
import androidx.appcompat.widget.Toolbar
import com.demo.PocketStore.common.Config.myEventList

class ShowReportActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null
    private var mContext: Context? = null
    var username: TextView? = null
    var carname: TextView? = null
    private var mDataManager: AppDataManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide ActionBar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_showreport)
        mContext = this
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar!!.title = "Reports"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { //update return last activity
            setResult(RESULT_OK, Intent())
            finish()
        }
        username = findViewById(R.id.tv_table_name)
        carname = findViewById(R.id.tv_table_carname)
        username?.setText(myEventList.size.toString() + "")
        if (mDataManager == null) {
            mDataManager = AppDataManager(this)
            mDataManager!!.openDataBase()
        }
        val appDataList = mDataManager!!.allDataList
        val myAppList: MutableList<AppData> = ArrayList()
        for (eventData in myEventList) {
            for (appData in appDataList) {
                if (appData.event_id == eventData!!.id) {
                    myAppList.add(appData)
                }
            }
        }
        carname?.setText(myAppList.size.toString() + "")
    }
}