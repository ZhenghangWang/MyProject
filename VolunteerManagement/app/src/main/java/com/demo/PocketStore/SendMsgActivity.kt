package com.demo.PocketStore

import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Button
import android.os.Bundle
import android.content.pm.ActivityInfo
import android.view.Window
import android.content.Intent
import android.widget.Toast
import android.widget.AdapterView
import com.demo.PocketStore.db.manager.UserDataManager
import com.demo.PocketStore.db.manager.MsgDataManager
import android.widget.Spinner
import com.demo.PocketStore.db.manager.VolDataManager
import android.widget.ArrayAdapter
import java.util.stream.Collectors
import androidx.appcompat.widget.Toolbar
import com.demo.PocketStore.common.Config.curUser
import com.demo.PocketStore.db.bean.*
import java.text.SimpleDateFormat
import java.util.*

//send message
class SendMsgActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "SendMsgActivity"
    var toolbar: Toolbar? = null
    var etInput: EditText? = null
    private var btnOk: Button? = null
    var spinner_sd: Spinner? = null
    var spinner_sd1: Spinner? = null
    var appData: AppData? = null
    private val sd_data_list: MutableList<String> = ArrayList()
    private var selectSD = 0
    private var sd_data_list1: MutableList<String?> = ArrayList()
    private var selectSD1 = 0
    private var userDataList: List<UserData> = ArrayList()
    private var mUserDataManager: UserDataManager? = null
    private var volDataList: List<Volunteer> = ArrayList()
    private var mDataManager: VolDataManager? = null
    private var msgDataManager: MsgDataManager? = null
    var rec_id = 0
    var sender_type = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // hide ActionBar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sendmsg)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar!!.title = "Sending Message"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { //update return last activity
            setResult(RESULT_OK, Intent())
            finish()
        }
        initViews()
    }

    private fun initViews() {
        btnOk = findViewById<View>(R.id.btn_ok) as Button
        spinner_sd = findViewById(R.id.spinner_sd)
        spinner_sd1 = findViewById(R.id.spinner_sd1)
        etInput = findViewById(R.id.et_input)
        btnOk!!.setOnClickListener(this)
        sd_data_list.add("volunteer")
        sd_data_list.add("organisation")
        if (curUser!!.userName != "admin") {
            sd_data_list.add("admin")
        }
        val arr_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sd_data_list)
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_sd?.setAdapter(arr_adapter)
        arr_adapter.notifyDataSetChanged()
        spinner_sd?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                sd_data_list1.clear()
                selectSD = position + 1
                when (selectSD) {
                    1 -> sd_data_list1 = volunteerName
                    2 -> sd_data_list1 = organisationName
                    3 -> sd_data_list1.add("admin")
                }
                loadSendObjectName()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        initdata()
    }

    private fun loadSendObjectName() {
        val arr_adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, sd_data_list1)
        arr_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_sd1!!.adapter = arr_adapter1
        arr_adapter1.notifyDataSetChanged()
        spinner_sd1!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                // Toast.makeText(getApplication(), "点击了" + arr_adapter.getItem(position), Toast.LENGTH_SHORT).show();
                selectSD1 = position
                when (selectSD) {
                    1 -> rec_id = volDataList[position].id
                    2 -> rec_id = userDataList[position].userId
                    3 -> rec_id = 0
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private val organisationName: MutableList<String?>
        private get() = userDataList.stream().map { o: UserData -> o.userName }
            .collect(Collectors.toList())
    private val volunteerName: MutableList<String?>
        private get() = volDataList.stream().map { o: Volunteer -> o.lastname }
            .collect(Collectors.toList())

    private fun initdata() {
        if (mUserDataManager == null) {
            mUserDataManager = UserDataManager(this)
            mUserDataManager!!.openDataBase()
        }
        if (mDataManager == null) {
            mDataManager = VolDataManager(this)
            mDataManager!!.openDataBase()
        }
       // userDataList.clear()
        userDataList = mUserDataManager!!.allUserDataList
       // volDataList.clear()
        volDataList = mDataManager!!.allDataList
        if (msgDataManager == null) {
            msgDataManager = MsgDataManager(this)
            msgDataManager!!.openDataBase()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_ok -> sendMsg()
        }
    }

    private fun sendMsg() {
        val msg = etInput!!.text.toString()
        if (msg.isEmpty()) {
            Toast.makeText(applicationContext, "input msg is empty！", Toast.LENGTH_SHORT).show()
            return
        }
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        val datetime = simpleDateFormat.format(Date())
        val message = MsgData(curUser!!.userId, rec_id, sender_type, selectSD, msg, datetime)
        val flag = msgDataManager!!.insertData(message)
        if (flag != -1L) {
            Toast.makeText(applicationContext, "send msg successfully！", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "send msg failed...", Toast.LENGTH_SHORT).show()
        }
    }
}