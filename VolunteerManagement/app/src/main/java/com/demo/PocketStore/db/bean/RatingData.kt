package com.demo.PocketStore.db.bean

import java.io.Serializable
import java.util.Comparator
import java.util.Collections

class RatingData : Serializable {
    @kotlin.jvm.JvmField
    var id = 0
    @kotlin.jvm.JvmField
    var rating = 0
    var review: String? = null
    @kotlin.jvm.JvmField
    var volunteer_id = 0
    @kotlin.jvm.JvmField
    var organisation_id = 0

    constructor() : super() {}
    constructor(id: Int, rating: Int, review: String?, volunteer_id: Int, organisation_id: Int) {
        this.id = id
        this.rating = rating
        this.volunteer_id = volunteer_id
        this.review = review
        this.organisation_id = organisation_id
    }

    constructor(rating: Int, review: String?, volunteer_id: Int, organisation_id: Int) {
        this.rating = rating
        this.volunteer_id = volunteer_id
        this.review = review
        this.organisation_id = organisation_id
    }
}