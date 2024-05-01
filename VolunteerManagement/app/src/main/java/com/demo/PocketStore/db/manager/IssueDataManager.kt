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


class IssueDataManager(context: Context?) {
    private var mContext: Context? = null

    enum class COL(val code: Int) {
        id(0), content(1), orgid(2), status(3);

    }

    //UserData manager_User = new UserData("wky","wky");
    private var mSQLiteDatabase: SQLiteDatabase? = null
    private var mDatabaseHelper: DBManagementHelper? = null

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

    @Throws(SQLException::class)
    fun openDataBase() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = DBManagementHelper(mContext)
            mSQLiteDatabase = mDatabaseHelper!!.writableDatabase
            mDatabaseHelper!!.readableDatabase
        }
    }

    @Throws(SQLException::class)
    fun closeDataBase() {
        mDatabaseHelper!!.close()
    }

    fun insertData(data: IssueData): Long {
        val values = ContentValues()
        //values.put(ID, data.id);
        values.put(CONTENT, data.content)
        values.put(ORG_ID, data.organization_id)
        values.put(STATUS, data.status)
        return mSQLiteDatabase!!.insert(TABLE_NAME, ID, values)
    }

    fun updateUserData(data: IssueData): Boolean {
        //int id = userData.getUserId();
        val values = ContentValues()
        values.put(CONTENT, data.content)
        values.put(ORG_ID, data.organization_id)
        values.put(STATUS, data.status)
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

    val allDataList: List<IssueData>
        get() {
            val cursor = mSQLiteDatabase!!.query(
                TABLE_NAME, null, null, null, null, null,
                null
            )
            val dataList: MutableList<IssueData> = ArrayList()
            while (cursor.moveToNext()) {
                dataList.add(
                    IssueData(
                        cursor.getInt(COL.id.code),
                        cursor.getString(COL.content.code),
                        cursor.getInt(COL.orgid.code),
                        cursor.getString(COL.status.code)
                    )
                )
            }
            return dataList
        }

    fun deleteUserData(id: Int): Boolean {
        return mSQLiteDatabase!!.delete(TABLE_NAME, ID + "=" + id, null) > 0
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

    fun updateDataById(data: IssueData, id: Int): Boolean {
        val values = ContentValues()
        //values.put(ID, data.id);
        values.put(CONTENT, data.content)
        values.put(ORG_ID, data.organization_id)
        values.put(STATUS, data.status)
        return mSQLiteDatabase!!.update(TABLE_NAME, values, ID + "=" + id, null) > 0
    }

    companion object {
        private const val TAG = "EventDataManager"
        private const val DB_NAME = "Organisation"
        private const val TABLE_NAME = "issue"
        const val ID = "_id"
        const val CONTENT = "content"
        const val ORG_ID = "organization_id"
        const val STATUS = "status"
        private const val DB_VERSION = 2
        private const val DB_CREATE = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID + " integer primary key,"
                + CONTENT + " varchar,"
                + ORG_ID + " integer,"
                + STATUS + " varchar  " + ");")
    }

    init {
        mContext = context
        Log.v(TAG, "EventDataManager construction!")
    }
}