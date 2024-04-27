package com.demo.PocketStore.ui.dashboard

import android.Manifest

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import com.demo.PocketStore.R
import android.widget.TextView
import android.widget.Toast
import com.demo.PocketStore.db.bean.MsgData
import com.demo.PocketStore.db.manager.RatingDataManager
import com.demo.PocketStore.db.manager.UserDataManager
import com.demo.PocketStore.db.bean.EventData
import android.widget.ImageView
import android.widget.Button
import com.demo.PocketStore.db.bean.IssueData
import com.demo.PocketStore.db.manager.IssueDataManager
import com.demo.PocketStore.db.bean.RatingData
import java.util.ArrayList
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlin.Throws
import android.content.ContentValues
import android.database.Cursor
import com.demo.PocketStore.db.manager.MsgDataManager
import com.demo.PocketStore.db.bean.Volunteer
import com.demo.PocketStore.db.manager.EventDataManager
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.AdapterView.OnItemClickListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.demo.PocketStore.adapter.EventListAdapter
import android.os.Bundle
import com.demo.PocketStore.ui.home.HomeViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import android.content.DialogInterface
import android.content.Intent
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.widget.EditText
import android.text.InputType
import android.app.Activity
import android.net.Uri
import android.provider.MediaStore
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RelativeLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.demo.PocketStore.ui.notifications.NotificationsViewModel
import com.demo.PocketStore.SendIssueActivity
import com.demo.PocketStore.SendMsgActivity
import com.demo.PocketStore.RecMsgActivity
import com.demo.PocketStore.common.Config
import com.demo.PocketStore.databinding.FragmentDashboardBinding
import java.util.Comparator
import java.util.Collections

class DashboardFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentDashboardBinding? = null
    private var img_profile: ImageView? = null
    private val IMAGE_REQUEST_CODE = 2000
    private var mUserDataManager: UserDataManager? = null
    var tv_name: TextView? = null
    var tv_phone: TextView? = null
    var tv_email: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val dashboardViewModel = ViewModelProvider(this).get(
            DashboardViewModel::class.java
        )
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        img_profile = binding!!.imgProfile
        img_profile!!.setOnClickListener(this)
        tv_name = binding!!.tvName
        tv_phone = binding!!.tvPhone
        tv_email = binding!!.tvEmail
        tv_name?.setText(Config.curUser!!.userName + "")
        tv_phone?.setText(Config.curUser!!.userPhone + "")
        tv_email?.setText(Config.curUser!!.userEmail + "")
        //  tv_name.setOnClickListener(this);
        tv_phone!!.setOnClickListener(this)
        tv_email!!.setOnClickListener(this)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_profile -> if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
                selectFromAlbum()
            }
            R.id.tv_phone -> showUpdateDialog(1)
            R.id.tv_email -> showUpdateDialog(2)
        }
    }

    private fun showUpdateDialog(type: Int) {
        val editText = EditText(activity)
        var title = ""
        when (type) {
            1 -> {
                title = "please input phone "
                editText.inputType = InputType.TYPE_CLASS_NUMBER
            }
            2 -> {
                title = "please input email "
                editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
            else -> {}
        }
        AlertDialog.Builder(requireActivity()).setTitle(title)
            .setView(editText)
            .setPositiveButton("Update") { dialogInterface, i ->
                val input = editText.text.toString()
                if (input == "") {
                    Toast.makeText(
                        activity, "input is empty！$input",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    when (type) {
                        1 -> {
                            tv_phone!!.text = input
                            Config.curUser!!.userPhone = input
                        }
                        2 -> {
                            tv_email!!.text = input
                            Config.curUser!!.userEmail = input
                        }
                    }
                    updateUser()
                }
            }.create()
            .show()
    }

    private fun selectFromAlbum() {
        Log.e("dashboard fragment", "click img_profile:")
        val intent2 = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent2, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_REQUEST_CODE -> {
                    val selectedImage = data!!.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = requireActivity().contentResolver.query(
                        selectedImage!!,
                        filePathColumn, null, null, null
                    ) //从系统表中查询指定Uri对应的照片
                    cursor!!.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    val path = cursor.getString(columnIndex)
                    cursor.close()
                    val bitmap = BitmapFactory.decodeFile(path)
                    displayImage(path, bitmap)
                }
            }
        } else {
            Toast.makeText(activity, "canceled", Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun displayImage(path: String, bitmap: Bitmap?) {
        if (bitmap != null) {
            img_profile!!.setImageBitmap(bitmap)
            Config.curUser!!.userPro = path
            updateUser()
        } else {
            Toast.makeText(activity, "failed to get image", Toast.LENGTH_SHORT)
        }
    }

    private fun updateUser() {
        if (mUserDataManager == null) {
            mUserDataManager = UserDataManager(activity)
            mUserDataManager!!.openDataBase()
        }
        val flag = mUserDataManager!!.updateUserDataById(Config.curUser, Config.curUser!!.userId)
        if (flag) {
            Toast.makeText(activity, "update successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "update failed...", Toast.LENGTH_SHORT).show()
        }
    }
}