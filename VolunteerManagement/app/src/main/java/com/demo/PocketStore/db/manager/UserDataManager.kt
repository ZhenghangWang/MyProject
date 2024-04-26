package com.demo.PocketStore.db.manager

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
import com.demo.PocketStore.db.manager.RatingDataManager
import com.demo.PocketStore.db.manager.UserDataManager
import android.widget.ImageView
import android.widget.Button
import com.demo.PocketStore.db.manager.IssueDataManager
import java.util.ArrayList
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlin.Throws
import android.content.ContentValues
import android.database.Cursor
import android.content.Context
import android.database.SQLException

import com.demo.PocketStore.db.bean.*
import java.util.Comparator
import java.util.Collections

class UserDataManager(context: Context?) {
    private var mContext: Context? = null

    enum class COL(val code: Int) {
        id(0), names(1), email(2), phone(3), pro(4), status(5), pwd(6);

    }

    //UserData manager_User = new UserData("wky","wky");
    private var mSQLiteDatabase: SQLiteDatabase? = null
    private var mDatabaseHelper: DataBaseManagementHelper? = null

    private class DataBaseManagementHelper internal constructor(context: Context?) :
        SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            Log.v(TAG, "db.getVersion()=" + db.version)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";")
            db.execSQL(DB_CREATE)
            val values = ContentValues()
            values.put(USER_NAME, "University of Leeds")
            values.put(USER_EMAIL, "leedsuniversity@organisation.com")
            values.put(USER_PWD, "test")
            db.insert(TABLE_NAME, ID, values)
            Log.v(TAG, "db.execSQL(DB_CREATE)")
            Log.e(TAG, DB_CREATE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            Log.v(TAG, "DataBaseManagementHelper onUpgrade")
            onCreate(db)
        }
    }

    //open
    @Throws(SQLException::class)
    fun openDataBase() {
        mDatabaseHelper = DataBaseManagementHelper(mContext)
        mSQLiteDatabase = mDatabaseHelper!!.writableDatabase
    }

    //close
    @Throws(SQLException::class)
    fun closeDataBase() {
        mDatabaseHelper!!.close()
    }

    fun insertUserData(userData: UserData): Long {
        val userName = userData.userName
        val userPwd = userData.userPwd
        val values = ContentValues()
        values.put(USER_NAME, userName)
        values.put(USER_EMAIL, userData.userEmail)
        values.put(USER_PHONE, userData.userPhone)
        values.put(USER_STATUS, "0")
        values.put(USER_PWD, userPwd)
        return mSQLiteDatabase!!.insert(TABLE_NAME, ID, values)
    }

    fun updateUserData(userData: UserData): Boolean {
        //int id = userData.getUserId();
        val userName = userData.userName
        val userPwd = userData.userPwd
        val values = ContentValues()
        values.put(USER_NAME, userName)
        values.put(USER_PWD, userPwd)
        return mSQLiteDatabase!!.update(TABLE_NAME, values, null, null) > 0
        //return mSQLiteDatabase.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
    }

    //
    @Throws(SQLException::class)
    fun fetchUserData(id: Int): Cursor? {
        val mCursor = mSQLiteDatabase!!.query(
            false, TABLE_NAME, null, ID
                    + "=" + id, null, null, null, null, null
        )
        mCursor?.moveToFirst()
        return mCursor
    }

    //
    fun fetchAllUserDatas(): Cursor {
        return mSQLiteDatabase!!.query(
            TABLE_NAME, null, null, null, null, null,
            null
        )
    }

    val allUserDataList: List<UserData>
        get() {
            val cursor = mSQLiteDatabase!!.query(
                TABLE_NAME, null, null, null, null, null,
                null
            )
            val userList: MutableList<UserData> = ArrayList()
            while (cursor.moveToNext()) {
                userList.add(
                    UserData(
                        cursor.getInt(COL.id.code),
                        cursor.getString(COL.names.code),
                        cursor.getString(COL.email.code),
                        cursor.getString(COL.phone.code),
                        cursor.getString(COL.pro.code),
                        cursor.getString(COL.status.code),
                        cursor.getString(COL.pwd.code)
                    )
                )
            }
            return userList
        }

    fun checkUserDataValid(name: String, email: String): Boolean {
        if ("admin" == name) {
            return false
        }
        val cursor = mSQLiteDatabase!!.query(
            TABLE_NAME, null, null, null, null, null,
            null
        )
        val userList: MutableList<UserData> = ArrayList()
        while (cursor.moveToNext()) {
            userList.add(
                UserData(
                    cursor.getInt(COL.id.code),
                    cursor.getString(COL.names.code),
                    cursor.getString(COL.email.code),
                    cursor.getString(COL.phone.code),
                    cursor.getString(COL.pro.code),
                    cursor.getString(COL.status.code),
                    cursor.getString(COL.pwd.code)
                )
            )
        }
        for (userData in userList) {
            val _email = userData.userEmail
            val _name = userData.userName
            if (!_email!!.isEmpty() && _email == email) {
                return false
            }
            if (!_name!!.isEmpty() && _name == name) {
                return false
            }
        }
        return true
    }

    fun deleteUserData(id: Int): Boolean {
        return mSQLiteDatabase!!.delete(TABLE_NAME, ID + "=" + id, null) > 0
    }

    fun deleteUserDatabyname(name: String): Boolean {
        return mSQLiteDatabase!!.delete(TABLE_NAME, USER_NAME + "=" + name, null) > 0
    }

    fun deleteAllUserDatas(): Boolean {
        return mSQLiteDatabase!!.delete(TABLE_NAME, null, null) > 0
    }

    //
    fun getStringByColumnName(columnName: String?, id: Int): String {
        val mCursor = fetchUserData(id)
        var columnValue = ""
        val columnIndex = mCursor!!.getColumnIndex(columnName)
        if (mCursor != null) {
            columnValue = mCursor.getString(columnIndex)
            mCursor.close()
            return columnValue
        }
        return ""
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

    fun updateUserDataById(userData: UserData?, id: Int): Boolean {
        val values = ContentValues()
        values.put(ID, userData?.userId)
        values.put(USER_NAME, userData?.userName)
        values.put(USER_EMAIL, userData?.userEmail)
        values.put(USER_PHONE, userData?.userPhone)
        values.put(USER_STATUS, userData?.userStatus)
        values.put(USER_PRO, userData?.userPro)
        values.put(USER_PWD, userData?.userPwd)
        return mSQLiteDatabase!!.update(TABLE_NAME, values, ID + "=" + id, null) > 0
    }

    fun findUserByName(userName: String): Int {
        Log.v(TAG, "findUserByName , userName=$userName")
        val result = 0
        val cur = mSQLiteDatabase!!.query(
            TABLE_NAME,
            null,
            USER_NAME + "=?",
            arrayOf(userName),
            null,
            null,
            null
        )
        while (cur.moveToNext()) {
            val user = cur.getString(COL.names.code)
            Log.v("db_query", user)
            if (user == userName) {
                cur.close()
                return 1
            } else cur.close()
            return 0
        }
        Log.v(TAG, "findUserByName , result=$result")
        return result
    }

    fun findUserByNameAndPwd(userName: String, pwd: String): UserData? {
        Log.v(TAG, "findUserByNameAndPwd")
        var userData: UserData? = null
        val cur = mSQLiteDatabase!!.query(
            TABLE_NAME,
            null,
            USER_NAME + "=?",
            arrayOf(userName),
            null,
            null,
            null
        )
        while (cur.moveToNext()) {
            val userPwd = cur.getString(COL.pwd.code)
            val userStatus = cur.getString(COL.status.code)
            Log.v("db_query", userPwd)
            if (pwd == userPwd) {
                userData = UserData(
                    cur.getInt(COL.id.code),
                    cur.getString(COL.names.code),
                    cur.getString(COL.email.code),
                    cur.getString(COL.phone.code),
                    cur.getString(COL.pro.code),
                    cur.getString(COL.status.code),
                    cur.getString(COL.pwd.code)
                )
                cur.close()
                return userData
            }
            //            else
//			{
//				cur.close();
//				return 0;
//			}
        }
        return userData
    }

    companion object {
        private const val TAG = "UserDataManager"
        private const val DB_NAME = "Organisation"
        private const val TABLE_NAME = "users"
        const val ID = "_id"
        const val USER_NAME = "name"
        const val USER_EMAIL = "email"
        const val USER_PHONE = "phone_number"
        const val USER_PRO = "profile_picture"
        const val USER_STATUS = "register_status"
        const val USER_PWD = "password"
        private const val DB_VERSION = 2

        //创建用户book表
        private const val DB_CREATE = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID + " integer primary key,"
                + USER_NAME + " varchar,"
                + USER_EMAIL + " varchar,"
                + USER_PHONE + " varchar,"
                + USER_PRO + " varchar,"
                + USER_STATUS + " varchar,"
                + USER_PWD + " varchar" + ");")
    }

    init {
        mContext = context
        Log.v(TAG, "UserDataManager construction!")
    }
}