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
import com.demo.PocketStore.db.manager.IssueDataManager
import com.demo.PocketStore.db.bean.IssueData
import androidx.appcompat.widget.Toolbar
import com.demo.PocketStore.common.Config.curUser

//send issue
class SendIssueActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "SendIssueActivity"
    var toolbar: Toolbar? = null
    var etInput: EditText? = null
    private var btnOk: Button? = null
    private var msgDataManager: IssueDataManager? = null
    var rec_id = 0
    var sender_type = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // hide ActionBar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sendissue)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar!!.title = "Sending Issue"
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
        etInput = findViewById(R.id.et_input)
        btnOk!!.setOnClickListener(this)
        initdata()
    }

    private fun initdata() {
        if (msgDataManager == null) {
            msgDataManager = IssueDataManager(this)
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
            Toast.makeText(applicationContext, "input issue is empty！", Toast.LENGTH_SHORT).show()
            return
        }
        val message = IssueData(msg, curUser!!.userId, "0")
        val flag = msgDataManager!!.insertData(message)
        if (flag != -1L) {
            Toast.makeText(applicationContext, "issue msg successfully！", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "issue msg failed...", Toast.LENGTH_SHORT).show()
        }
    }
}