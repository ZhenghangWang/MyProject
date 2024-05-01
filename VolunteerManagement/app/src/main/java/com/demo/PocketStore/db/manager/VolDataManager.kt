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
import com.demo.PocketStore.db.manager.MsgDataManager
import com.demo.PocketStore.db.bean.Volunteer
import android.content.Context
import android.database.SQLException


class VolDataManager(context: Context?) {
    private var mContext: Context? = null

    enum class COL(val code: Int) {
        id(0), fname(1), lname(2), email(3), pwd(4), about(5), city(6), region(7), country(8), loc(9), skills(
            10
        ),
        l_login(11), prof(12), a_status(13), birth(14), phone(15);

    }

    private var mSQLiteDatabase: SQLiteDatabase? = null
    private var mDatabaseHelper: DBManagementHelper? = null

    private class DBManagementHelper internal constructor(context: Context?) :
        SQLiteOpenHelper(context, TABLE_NAME, null, DB_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            Log.v(TAG, "db.getVersion()=" + db.version)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";")
            db.execSQL(DB_CREATE)
            val values = ContentValues()
            values.put(F_NAME, "Suriana")
            values.put(L_NAME, "Zulhilmi")
            values.put(EMAIL, "suriana@gmail.com")
            values.put(PWD, "test")
            values.put(ABOUT, "A passionate volunteer who is looking for opportunity to contribute")
            values.put(CITY, "Leeds")
            db.insert(TABLE_NAME, ID, values)
            values.put(F_NAME, "Adam")
            values.put(L_NAME, "Brown")
            values.put(EMAIL, "ab1302@gmail.com")
            values.put(PWD, "test")
            values.put(ABOUT, "Adam here")
            values.put(CITY, "Manchester")
            db.insert(TABLE_NAME, ID, values)
            values.put(F_NAME, "John")
            values.put(L_NAME, "Miller")
            values.put(EMAIL, "johnmiller@gmail.com")
            values.put(PWD, "test")
            values.put(
                ABOUT,
                "Hi I am John! Currently, I have 5 years of experience in volunteering and I look forward for more great opportunity. Feel free to reach out"
            )
            values.put(CITY, "Leeds")
            db.insert(TABLE_NAME, ID, values)
            values.put(F_NAME, "John")
            values.put(L_NAME, "Sina")
            values.put(EMAIL, "johnsina@outlook.com")
            values.put(PWD, "john")
            values.put(ABOUT, "Hello there")
            values.put(CITY, "Hulk")
            db.insert(TABLE_NAME, ID, values)
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

    val allDataList: List<Volunteer>
        get() {
            val cursor = mSQLiteDatabase!!.query(
                TABLE_NAME, null, null, null, null, null,
                null
            )
            val dataList: MutableList<Volunteer> = ArrayList()
            while (cursor.moveToNext()) {
                dataList.add(
                    Volunteer(
                        cursor.getInt(COL.id.code),
                        cursor.getString(COL.fname.code),
                        cursor.getString(COL.lname.code),
                        cursor.getString(COL.email.code),
                        cursor.getString(COL.pwd.code),
                        cursor.getString(COL.about.code),
                        cursor.getString(COL.city.code),
                        cursor.getString(COL.region.code),
                        cursor.getString(COL.country.code),
                        cursor.getString(COL.loc.code),
                        cursor.getString(COL.skills.code),
                        cursor.getString(COL.l_login.code),
                        cursor.getString(COL.prof.code),
                        cursor.getInt(COL.a_status.code),
                        cursor.getString(COL.birth.code),
                        cursor.getString(COL.phone.code)
                    )
                )
            }
            return dataList
        }

    fun getStringByColumnName(columnName: String?, id: Int): String {
        val mCursor = fetchData(id)
        val columnIndex = mCursor!!.getColumnIndex(columnName)
        val columnValue = mCursor.getString(columnIndex)
        mCursor.close()
        return columnValue
    }

    companion object {
        private const val TAG = "EventDataManager"
        private const val DB_NAME = "Organisation"
        private const val TABLE_NAME = "volunteer"
        const val ID = "_id"
        const val F_NAME = "firstname"
        const val L_NAME = "lastname"
        const val EMAIL = "email"
        private const val PWD = "password"
        private const val ABOUT = "about"
        private const val CITY = "city"
        const val REGION = "region"
        const val COUNTRY = "country"
        const val LOC = "location"
        const val SKILLS = "skills"
        private const val L_LOGIN = "last_login"
        const val PROF = "profile_picture"
        const val A_STATUS = "active_status"
        const val BIRTH = "birthday"
        const val PHONE = "phone_number"
        private const val DB_VERSION = 2
        private const val DB_CREATE = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID + " integer primary key,"
                + F_NAME + " varchar,"
                + L_NAME + " varchar,"
                + EMAIL + " varchar,"
                + PWD + " varchar,"
                + ABOUT + " varchar,"
                + CITY + " varchar,"
                + REGION + " varchar,"
                + COUNTRY + " varchar,"
                + LOC + " varchar,"
                + SKILLS + " varchar,"
                + L_LOGIN + " varchar,"
                + PROF + " varchar,"
                + A_STATUS + " integer,"
                + BIRTH + " varchar,"
                + PHONE + " varchar  " + ");")
    }

    init {
        mContext = context
        Log.v(TAG, "EventDataManager construction!")
    }
}