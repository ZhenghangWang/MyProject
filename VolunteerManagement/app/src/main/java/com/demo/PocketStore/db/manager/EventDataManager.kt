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


class EventDataManager(context: Context?) {
    private var mContext: Context? = null

    enum class COL(val code: Int) {
        id(0), title(1), desc(2), date(3), org_id(4), max(5), curr(6), dur(7), loc(8), skills(9);

    }

    //UserData manager_User = new UserData("wky","wky");
    private var mSQLiteDatabase: SQLiteDatabase? = null
    private var mDatabaseHelper: DBManagementHelper? = null

    //  long i= insertUserData( manager_User);//插入管理员
    //DataBaseManagementHelper继承自SQLiteOpenHelper
    private class DBManagementHelper internal constructor(context: Context?) :
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

        init {
            Log.e(TAG, "DBManagementHelper")
        }
    }

    //打开数据库
    @Throws(SQLException::class)
    fun openDataBase() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = DBManagementHelper(mContext)
            mSQLiteDatabase = mDatabaseHelper!!.writableDatabase
            mDatabaseHelper!!.readableDatabase
        }
    }

    //关闭数据库
    @Throws(SQLException::class)
    fun closeDataBase() {
        mDatabaseHelper!!.close()
    }

    fun insertData(data: EventData): Long {
        val values = ContentValues()
        //values.put(ID, data.id);
        values.put(TITLE, data.title)
        values.put(DESC, data.description)
        values.put(DATE, data.date)
        values.put(ORG_ID, data.organisation_id)
        values.put(MAX, data.max_application)
        values.put(CURR, data.current_application)
        values.put(DUR, data.duration)
        values.put(LOC, data.location)
        values.put(SKILLS, data.skills_required)
        return mSQLiteDatabase!!.insert(TABLE_NAME, ID, values)
    }

    fun updateUserData(data: EventData): Boolean {
        //int id = userData.getUserId();
        val values = ContentValues()
        values.put(TITLE, data.title)
        values.put(DESC, data.description)
        values.put(DATE, data.title)
        values.put(ORG_ID, data.description)
        values.put(MAX, data.title)
        values.put(CURR, data.description)
        values.put(DUR, data.title)
        values.put(LOC, data.description)
        values.put(SKILLS, data.description)
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

    val allDataList: List<EventData>
        get() {
            val cursor = mSQLiteDatabase!!.query(
                TABLE_NAME, null, null, null, null, null,
                null
            )
            val dataList: MutableList<EventData> = ArrayList()
            while (cursor.moveToNext()) {
                dataList.add(
                    EventData(
                        cursor.getInt(COL.id.code),
                        cursor.getString(COL.title.code),
                        cursor.getString(COL.desc.code),
                        cursor.getString(COL.date.code),
                        cursor.getInt(COL.org_id.code),
                        cursor.getInt(COL.max.code),
                        cursor.getInt(COL.curr.code),
                        cursor.getString(COL.dur.code),
                        cursor.getString(COL.loc.code),
                        cursor.getString(COL.skills.code)
                    )
                )
            }
            return dataList
        }

    fun deleteUserData(id: Int): Boolean {
        return mSQLiteDatabase!!.delete(TABLE_NAME, ID + "=" + id, null) > 0
    }

    fun deleteUserDatabyname(name: String): Boolean {
        return mSQLiteDatabase!!.delete(TABLE_NAME, TITLE + "=" + name, null) > 0
    }

    fun deleteAllUserDatas(): Boolean {
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
    fun updateUserDataById(
        columnName: String?, id: Int,
        columnValue: String?
    ): Boolean {
        val values = ContentValues()
        values.put(columnName, columnValue)
        return mSQLiteDatabase!!.update(TABLE_NAME, values, ID + "=" + id, null) > 0
    }

    fun updateDataById(data: EventData, id: Int): Boolean {
        val values = ContentValues()
        //values.put(ID, data.id);
        values.put(TITLE, data.title)
        values.put(DESC, data.description)
        values.put(DATE, data.title)
        values.put(ORG_ID, data.description)
        values.put(MAX, data.title)
        values.put(CURR, data.description)
        values.put(DUR, data.title)
        values.put(LOC, data.description)
        values.put(SKILLS, data.description)
        return mSQLiteDatabase!!.update(TABLE_NAME, values, ID + "=" + id, null) > 0
    }

    fun findDataByName(name: String): Int {
        Log.v(TAG, "findUserByName , userName=$name")
        val result = 0
        val cur =
            mSQLiteDatabase!!.query(TABLE_NAME, null, TITLE + "=?", arrayOf(name), null, null, null)
        while (cur.moveToNext()) {
            val user = cur.getString(COL.title.code)
            Log.v("db_query", user)
            if (user == name) {
                cur.close()
                return 1
            } else cur.close()
            return 0
        }
        Log.v(TAG, "findUserByName , result=$result")
        return result
    }

    companion object {
        //一些宏定义和声明
        private const val TAG = "EventDataManager"
        private const val DB_NAME = "Organisation"
        private const val TABLE_NAME = "event"
        const val ID = "_id"
        const val TITLE = "title"
        const val DESC = "description"
        const val DATE = "date"
        const val ORG_ID = "organisation_id"
        const val MAX = "max_application"
        const val CURR = "current_application"
        const val DUR = "duration"
        const val LOC = "location"
        const val SKILLS = "skills_required"
        private const val DB_VERSION = 2
        private const val DB_CREATE = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID + " integer primary key,"
                + TITLE + " varchar,"
                + DESC + " varchar,"
                + DATE + " varchar,"
                + ORG_ID + " integer,"
                + MAX + " integer,"
                + CURR + " integer DEFAULT 0,"
                + DUR + " varchar,"
                + LOC + " varchar,"
                + SKILLS + " varchar  " + ");")
    }

    init {
        mContext = context
        Log.v(TAG, "EventDataManager construction!")
    }
}