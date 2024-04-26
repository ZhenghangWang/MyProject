package com.demo.PocketStore.ui.notifications

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
import android.widget.RelativeLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.demo.PocketStore.ui.notifications.NotificationsViewModel
import com.demo.PocketStore.SendIssueActivity
import com.demo.PocketStore.SendMsgActivity
import com.demo.PocketStore.RecMsgActivity
import com.demo.PocketStore.databinding.FragmentNotificationsBinding
import java.util.Comparator
import java.util.Collections

class NotificationsFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentNotificationsBinding? = null
    var l_issue_send: RelativeLayout? = null
    var l_msg_send: LinearLayout? = null
    var l_msg_rec: LinearLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val notificationsViewModel = ViewModelProvider(this).get(
            NotificationsViewModel::class.java
        )
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        l_issue_send = binding!!.lIssueSend
        l_issue_send!!.setOnClickListener(this)
        l_msg_send = binding!!.lMsgSend
        l_msg_send!!.setOnClickListener(this)
        l_msg_rec = binding!!.lMsgRec
        l_msg_rec!!.setOnClickListener(this)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.l_issue_send -> startActivity(Intent(activity, SendIssueActivity::class.java))
            R.id.l_msg_send -> startActivity(Intent(activity, SendMsgActivity::class.java))
            R.id.l_msg_rec -> startActivity(Intent(activity, RecMsgActivity::class.java))
        }
    }
}