package com.demo.PocketStore.db.bean

import java.io.Serializable


class EventData : Serializable, Comparable<EventData> {
    @kotlin.jvm.JvmField
    var id = 0
    @kotlin.jvm.JvmField
    var title: String? = null
    @kotlin.jvm.JvmField
    var description: String? = null
    @kotlin.jvm.JvmField
    var date: String? = null
    var organisation_id = 0
    @kotlin.jvm.JvmField
    var max_application = 0
    @kotlin.jvm.JvmField
    var current_application = 0
    @kotlin.jvm.JvmField
    var duration: String? = null
    @kotlin.jvm.JvmField
    var location: String? = null
    @kotlin.jvm.JvmField
    var skills_required: String? = null

    constructor() : super() {}
    constructor(
        id: Int, title: String?, description: String?, date: String?,
        organisation_id: Int, max_application: Int, current_application: Int,
        duration: String?, location: String?, skills_required: String?
    ) {
        this.id = id
        this.title = title
        this.description = description
        this.date = date
        this.organisation_id = organisation_id
        this.max_application = max_application
        this.current_application = current_application
        this.duration = duration
        this.location = location
        this.skills_required = skills_required
    }

    constructor(
        title: String?, description: String?, date: String?,
        organisation_id: Int, max_application: Int, current_application: Int,
        duration: String?, location: String?, skills_required: String?
    ) {
        this.title = title
        this.description = description
        this.date = date
        this.organisation_id = organisation_id
        this.max_application = max_application
        this.current_application = current_application
        this.duration = duration
        this.location = location
        this.skills_required = skills_required
    }

    override fun compareTo(data: EventData): Int {
        return data.title!!.compareTo(title!!)
    }
}