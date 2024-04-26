package com.demo.PocketStore.db.bean

import java.io.Serializable
import java.util.Comparator
import java.util.Collections

class AppData : Serializable {
    @kotlin.jvm.JvmField
    var id = 0
    var volunteer_id = 0
    @kotlin.jvm.JvmField
    var event_id = 0
    var status: String? = null

    constructor() : super() {}
    constructor(id: Int, volunteer_id: Int, event_id: Int, status: String?) {
        this.id = id
        this.volunteer_id = volunteer_id
        this.event_id = event_id
        this.status = status
    }

    constructor(volunteer_id: Int, event_id: Int, status: String?) {
        this.volunteer_id = volunteer_id
        this.event_id = event_id
        this.status = status
    }
}