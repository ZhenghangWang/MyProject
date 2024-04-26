package com.demo.PocketStore

import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import android.widget.TextView
import com.demo.PocketStore.db.manager.UserDataManager
import androidx.appcompat.widget.AppCompatButton
import com.demo.PocketStore.db.bean.*

class SignupActivity : AppCompatActivity(), View.OnClickListener {
    //@BindView(R.id.input_name)
    var _nameText: EditText? = null
    var _emailText: EditText? = null
    var _phoneText: EditText? = null

    //@BindView(R.id.input_password)
    var _passwordText: EditText? = null

    // @BindView(R.id.input_reEnterPassword)
    var _reEnterPasswordText: EditText? = null
    var _signupButton: AppCompatButton? = null
    var _linkSignin: TextView? = null
    private var mUserDataManager //用户数据管理类
            : UserDataManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        initViews()
    }

    private fun initViews() {
        _nameText = findViewById<View>(R.id.input_username) as EditText
        _emailText = findViewById<View>(R.id.input_email) as EditText
        _phoneText = findViewById<View>(R.id.input_phone) as EditText
        _reEnterPasswordText = findViewById<View>(R.id.input_reEnterPassword) as EditText
        _passwordText = findViewById<View>(R.id.input_password) as EditText
        _signupButton = findViewById<View>(R.id.btn_signup) as AppCompatButton
        _linkSignin = findViewById<View>(R.id.link_login) as TextView
        _signupButton!!.setOnClickListener(this)
        _linkSignin!!.setOnClickListener(this)
    }

    fun signup() {
        //判断是否合法
        if (!validate()) {
            onSignupFailed(0)
            return
        }
        _signupButton!!.isEnabled = false

        //获取数据
        val username = _nameText!!.text.toString()
        val email = _emailText!!.text.toString()
        val phone = _phoneText!!.text.toString()
        val password = _passwordText!!.text.toString()
        //String repwd = _passwordText.getText().toString();
        val mUser = UserData(username, password, email, phone)
        if (mUserDataManager == null) {
            mUserDataManager = UserDataManager(this)
            mUserDataManager!!.openDataBase() //建立本地数据库
        }
        if (!mUserDataManager!!.checkUserDataValid(username, email)) {
            Toast.makeText(
                applicationContext,
                "register failed: username or email is already registered", Toast.LENGTH_SHORT
            ).show()
            return
        }
        val flag = mUserDataManager!!.insertUserData(mUser) //注册用户信息
        if (flag == -1L) {
            Toast.makeText(applicationContext, "register failed...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                applicationContext,
                "register successfully,please login!",
                Toast.LENGTH_SHORT
            ).show()
            //Toast.makeText(getApplicationContext(),"文件保存成功", Toast.LENGTH_SHORT).show();
            val intent = Intent(this, SigninActivity::class.java)
            intent.putExtra("name", username)
            intent.putExtra("pwd", password)
            startActivity(intent)
            finish()
        }
    }

    /**
     * 注册失败，按钮置为可用
     * 依据传参不同，进行不同吐司
     */
    fun onSignupFailed(i: Int) {
        if (i == 1) {
            Toast.makeText(baseContext, "email is existed", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(baseContext, "register failed", Toast.LENGTH_LONG).show()
        }
        _signupButton!!.isEnabled = true
    }

    /**
     * @return 输入内容是否合法
     */
    fun validate(): Boolean {
        var valid = true
        //      从控件中获取数据
        val name = _nameText!!.text.toString()
        val email = _nameText!!.text.toString()
        val phone = _passwordText!!.text.toString()
        val password = _passwordText!!.text.toString()
        val reEnterPassword = _reEnterPasswordText!!.text.toString()
        //检测账号是否正确
        if (name.isEmpty()) {
            _nameText!!.error = "name is empty"
            valid = false
        } else {
            _nameText!!.error = null
        }
        if (email.isEmpty()) {
            _emailText!!.error = "email is empty"
            valid = false
        } else {
            _emailText!!.error = null
        }
        if (phone.isEmpty()) {
            _phoneText!!.error = "phone is empty"
            valid = false
        } else {
            _phoneText!!.error = null
        }
        //检测密码是否正确
        if (password.isEmpty()) {
            _passwordText!!.error = "password is empty"
            valid = false
        } else {
            _passwordText!!.error = null
        }
        //检测重复密码是否正确
        if (reEnterPassword.isEmpty() || reEnterPassword != password) {
            _reEnterPasswordText!!.error = "password is different"
            valid = false
        } else {
            _reEnterPasswordText!!.error = null
        }
        return valid
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_signup -> signup()
            R.id.link_login -> {
                //点击登录连接，跳转到登录页面
                val intent = Intent(applicationContext, SigninActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}