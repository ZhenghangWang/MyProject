package com.demo.PocketStore.db.manager

import com.demo.PocketStore.db.bean.AppData
import android.widget.BaseAdapter
import android.widget.ListView
import com.demo.PocketStore.db.manager.AppDataManager
import com.demo.PocketStore.db.manager.VolDataManager
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
import android.content.Context
import android.database.SQLException


class MsgDataManager(context: Context?) {
    private var mContext: Context? = null

    enum class COL(val code: Int) {
        id(0), s_id(1), r_id(2), s_type(3), r_type(4), msg(5), s_time(6);

    }

    //UserData manager_User = new UserData("wky","wky");
    private var mSQLiteDatabase: SQLiteDatabase? = null
    private var mDatabaseHelper: DataBaseManagementHelper? = null


    private class DataBaseManagementHelper internal constructor(context: Context?) :
        SQLiteOpenHelper(context, TABLE_NAME, null, DB_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            Log.v(TAG, "db.getVersion()=" + db.version)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";")
            db.execSQL(DB_CREATE)
            Log.v(TAG, "db.execSQL(DB_CREATE)")
            Log.e(TAG, DB_CREATE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            Log.v(TAG, "DataBaseManagementHelper onUpgrade")
            onCreate(db)
        }
    }

    //打开数据库
    @Throws(SQLException::class)
    fun openDataBase() {
        mDatabaseHelper = DataBaseManagementHelper(mContext)
        mSQLiteDatabase = mDatabaseHelper!!.writableDatabase
    }

    //关闭数据库
    @Throws(SQLException::class)
    fun closeDataBase() {
        mDatabaseHelper!!.close()
    }

    fun insertData(userData: MsgData): Long {
        val values = ContentValues()
        values.put(S_ID, userData.sender_id)
        values.put(R_ID, userData.receiver_id)
        values.put(S_TYPE, userData.sender_type)
        values.put(R_TPYE, userData.receiver_type)
        values.put(MSG, userData.message)
        values.put(S_TIME, userData.sent_time)
        return mSQLiteDatabase!!.insert(TABLE_NAME, ID, values)
    }

    fun updateData(userData: MsgData): Boolean {
        val values = ContentValues()
        values.put(S_ID, userData.sender_id)
        values.put(R_ID, userData.receiver_id)
        values.put(S_TYPE, userData.sender_type)
        values.put(R_TPYE, userData.receiver_type)
        values.put(MSG, userData.message)
        values.put(S_TIME, userData.sent_time)
        return mSQLiteDatabase!!.update(TABLE_NAME, values, null, null) > 0
        //return mSQLiteDatabase.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
    }

    //
    @Throws(SQLException::class)
    fun fetchData(id: Int): Cursor? {
        val mCursor = mSQLiteDatabase!!.query(
            false, TABLE_NAME, null, ID
                    + "=" + id, null, null, null, null, null
        )
        mCursor?.moveToFirst()
        return mCursor
    }

    //
    fun fetchAllDatas(): Cursor {
        return mSQLiteDatabase!!.query(
            TABLE_NAME, null, null, null, null, null,
            null
        )
    }

    val allDataList: List<MsgData>
        get() {
            val cursor = mSQLiteDatabase!!.query(
                TABLE_NAME, null, null, null, null, null,
                null
            )
            val userList: MutableList<MsgData> = ArrayList()
            while (cursor.moveToNext()) {
                userList.add(
                    MsgData(
                        cursor.getInt(COL.id.code),
                        cursor.getInt(COL.s_id.code),
                        cursor.getInt(COL.r_id.code),
                        cursor.getInt(COL.s_type.code),
                        cursor.getInt(COL.r_type.code),
                        cursor.getString(COL.msg.code),
                        cursor.getString(COL.s_time.code)
                    )
                )
            }
            return userList
        }

    fun deleteData(id: Int): Boolean {
        return mSQLiteDatabase!!.delete(TABLE_NAME, ID + "=" + id, null) > 0
    }

    fun deleteAllDatas(): Boolean {
        return mSQLiteDatabase!!.delete(TABLE_NAME, null, null) > 0
    }

    //
    fun getStringByColumnName(columnName: String?, id: Int): String {
        val mCursor = fetchData(id)
        val columnIndex = mCursor!!.getColumnIndex(columnName)
        val columnValue = mCursor.getString(columnIndex)
        mCursor.close()
        return columnValue
    }

    //
    fun updateDataById(
        columnName: String?, id: Int,
        columnValue: String?
    ): Boolean {
        val values = ContentValues()
        values.put(columnName, columnValue)
        return mSQLiteDatabase!!.update(TABLE_NAME, values, ID + "=" + id, null) > 0
    }

    fun updateDataById(userData: MsgData, id: Int): Boolean {
        val values = ContentValues()
        values.put(ID, userData.id)
        values.put(S_ID, userData.sender_id)
        values.put(R_ID, userData.receiver_id)
        values.put(S_TYPE, userData.sender_type)
        values.put(R_TPYE, userData.receiver_type)
        values.put(MSG, userData.message)
        values.put(S_TIME, userData.sent_time)
        return mSQLiteDatabase!!.update(TABLE_NAME, values, ID + "=" + id, null) > 0
    }

    companion object {
        private const val TAG = "UserDataManager"
        private const val DB_NAME = "Organisation"
        private const val TABLE_NAME = "message"
        const val ID = "_id"
        const val S_ID = "sender_id"
        const val R_ID = "receiver_id"
        const val S_TYPE = "sender_type"
        const val R_TPYE = "rec_type"
        const val MSG = "message"
        const val S_TIME = "sent_time"
        private const val DB_VERSION = 2
        private const val DB_CREATE = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID + " integer primary key,"
                + S_ID + " integer,"
                + R_ID + " integer,"
                + S_TYPE + " integer,"
                + R_TPYE + " integer,"
                + MSG + " varchar,"
                + S_TIME + " varchar" + ");")
    }

    init {
        mContext = context
        Log.v(TAG, "UserDataManager construction!")
    }
}