package com.demo.PocketStore.db.bean


class Volunteer {
    @kotlin.jvm.JvmField
    var id = 0
    var firstname: String? = null
    @kotlin.jvm.JvmField
    var lastname: String? = null
    var email: String? = null
    var password: String? = null
    var about: String? = null
    var city: String? = null
    var region: String? = null
    var country: String? = null
    var location: String? = null
    var skills: String? = null
    var last_login: String? = null
    var profile_picture: String? = null
    var active_status = 0
    var birthday: String? = null
    var phone_number: String? = null

    constructor(
        id: Int, firstname: String?, lastname: String?, email: String?,
        password: String?, about: String?, city: String?, region: String?,
        country: String?, location: String?, skills: String?, last_login: String?,
        profile_picture: String?, active_status: Int, birthday: String?, phone_number: String?
    ) : super() {
        this.id = id
        this.firstname = firstname
        this.lastname = lastname
        this.email = email
        this.password = password
        this.about = about
        this.city = city
        this.region = region
        this.country = country
        this.location = location
        this.skills = skills
        this.last_login = last_login
        this.profile_picture = profile_picture
        this.active_status = active_status
        this.phone_number = phone_number
        this.birthday = birthday
    }

    constructor(
        firstname: String?, lastname: String?, email: String?,
        password: String?, about: String?, city: String?, region: String?,
        country: String?, location: String?, skills: String?, last_login: String?,
        profile_picture: String?, active_status: Int, birthday: String?,
        phone_number: String?
    ) : super() {
        this.firstname = firstname
        this.lastname = lastname
        this.email = email
        this.password = password
        this.about = about
        this.city = city
        this.region = region
        this.country = country
        this.location = location
        this.skills = skills
        this.last_login = last_login
        this.profile_picture = profile_picture
        this.active_status = active_status
        this.phone_number = phone_number
        this.birthday = birthday
    }

    constructor() : super() {
    }
}