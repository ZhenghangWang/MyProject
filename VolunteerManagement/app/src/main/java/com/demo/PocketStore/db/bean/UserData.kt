package com.demo.PocketStore.db.bean


class UserData {
    @kotlin.jvm.JvmField

    var userName
            : String? = null

    @kotlin.jvm.JvmField

    var userEmail
            : String? = null
    @kotlin.jvm.JvmField


    var userPhone
            : String? = null

    @kotlin.jvm.JvmField

    var userPwd
            : String? = null

    @kotlin.jvm.JvmField

    var userStatus
            : String? = null

    @kotlin.jvm.JvmField

    var userPro: String? = null

    @kotlin.jvm.JvmField

    var userId
            = 0

    constructor(userName: String?, userPwd: String?) : super() {
        this.userName = userName
        this.userPwd = userPwd
    }

    constructor(
        userName: String?,
        userPwd: String?,
        userEmail: String?,
        userPhone: String?
    ) : super() {  //这里只采用用户名和密码
        this.userName = userName
        this.userPwd = userPwd
        this.userEmail = userEmail
        this.userPhone = userPhone
    }

    constructor(id: Int, userName: String?, userPwd: String?) : super() {  //这里只采用用户名和密码
        userId = id
        this.userName = userName
        this.userPwd = userPwd
    }

    constructor(
        id: Int, userName: String?, userEmail: String?, userPhone: String?,
        userPro: String?, userStatus: String?, userPwd: String?
    ) : super() {
        userId = id
        this.userName = userName
        this.userPwd = userPwd
        this.userEmail = userEmail
        this.userPhone = userPhone
        this.userStatus = userStatus
        this.userPro = userPro
    }

    constructor() : super() {
    }
}