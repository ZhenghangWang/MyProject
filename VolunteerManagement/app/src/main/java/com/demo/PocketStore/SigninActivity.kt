package com.demo.PocketStore

import android.Manifest

import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import com.demo.PocketStore.db.manager.EventDataManager
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import android.widget.TextView
import com.demo.PocketStore.db.manager.UserDataManager
import androidx.appcompat.widget.AppCompatButton
import android.os.Build
import android.content.pm.PackageManager
import com.demo.PocketStore.common.Config.curUser
import com.demo.PocketStore.db.bean.*

class SigninActivity : AppCompatActivity(), View.OnClickListener {
    var inputUsername: EditText? = null
    var inputPassword: EditText? = null
    var btnLogin: AppCompatButton? = null
    var linkSignup: TextView? = null
    private var mUserDataManager: UserDataManager? = null
    private val eventDataManager: EventDataManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        initViews()
        initData()
        if (mUserDataManager == null) {
            mUserDataManager = UserDataManager(this)
            mUserDataManager!!.openDataBase() //建立本地数据库
        }
        //        if (eventDataManager==null){
//            eventDataManager=new EventDataManager(this);
//            eventDataManager.openDataBase();
//        }
    }

    private fun initViews() {
        btnLogin = findViewById<View>(R.id.btn_login) as AppCompatButton
        inputUsername = findViewById<View>(R.id.input_username) as EditText
        inputPassword = findViewById<View>(R.id.input_password) as EditText
        linkSignup = findViewById<View>(R.id.link_signup) as TextView
        btnLogin!!.setOnClickListener(this)
        inputUsername!!.setOnClickListener(this)
        inputPassword!!.setOnClickListener(this)
        linkSignup!!.setOnClickListener(this)
    }

    fun initData() {
        /**
         * 动态获取权限，Android 6.0 新特性，一些保护权限，除了要在AndroidManifest中声明权限，还要使用如下代码动态获取
         */
        if (Build.VERSION.SDK_INT >= 23) {
            val REQUEST_CODE_CONTACT = 101
            val permissions = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_LOGS
            )
            //验证是否许可权限
            for (str in permissions) {
                if (checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    requestPermissions(permissions, REQUEST_CODE_CONTACT)
                    return
                }
            }
        }
    }

    /**
     * 登录方法
     */
    fun login() {
        //如果内容不合法，则直接返回，显示错误。
        if (!validate()) {
            Toast.makeText(applicationContext, "input is invalid", Toast.LENGTH_SHORT).show()
            return
        }
        //获取输入内容
        val username = inputUsername!!.text.toString().trim { it <= ' ' }
        val password = inputPassword!!.text.toString().trim { it <= ' ' }
        if ("admin" == username && "123" == password) {
            curUser = UserData(0, "admin", "123")
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            Toast.makeText(applicationContext, "Admin Login successfully", Toast.LENGTH_SHORT)
                .show()
            finish()
            return
        }
        val userData = mUserDataManager!!.findUserByNameAndPwd(username, password)
        if (userData != null && "1" == userData.userStatus) {
            //返回1说明用户名和密码均正确
            val intent = Intent(this, MainActivity::class.java)
            curUser = userData
            startActivity(intent)
            Toast.makeText(applicationContext, "Login successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else if (userData != null && "0" == userData.userStatus) {
            Toast.makeText(
                applicationContext,
                "Waiting for administrator review!",
                Toast.LENGTH_SHORT
            ).show()
            // startActivity(new Intent(this, RegisterActivity.class));
        } else {
            Toast.makeText(applicationContext, "Login failed！", Toast.LENGTH_SHORT).show()
            // startActivity(new Intent(this, RegisterActivity.class));
        }
    }

    /**
     * 重写返回键的返回方法
     */
    override fun onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }

    /**
     * @return 判断是否账号密码是否合法
     */
    fun validate(): Boolean {
        //设置初值，默认为合法
        var valid = true

        //获取输入内容
        val email = inputUsername!!.text.toString().trim { it <= ' ' }
        val password = inputPassword!!.text.toString().trim { it <= ' ' }

        //判断账号
        if (email.isEmpty()) {
            inputUsername!!.error = "email is empty"
            valid = false
        } else {
            inputUsername!!.error = null
        }
        //判断密码
        if (password.isEmpty()) {
            inputPassword!!.error = "password is empty"
            valid = false
        } else {
            inputPassword!!.error = null
        }
        return valid
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_login -> login()
            R.id.link_signup -> {
                val intent = Intent(applicationContext, SignupActivity::class.java)
                startActivityForResult(intent, REQUEST_SIGNUP)
                finish()
            }
        }
    }

    companion object {
        private const val REQUEST_SIGNUP = 0
    }
}