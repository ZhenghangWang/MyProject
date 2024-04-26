package com.demo.PocketStore.db.bean

import java.io.Serializable
import java.util.Comparator
import java.util.Collections

class IssueData : Serializable {
    var id = 0
    var content: String? = null
    var organization_id = 0
    @kotlin.jvm.JvmField
    var status: String? = null

    constructor() : super() {}
    constructor(id: Int, content: String?, organization_id: Int, status: String?) {
        this.id = id
        this.content = content
        this.organization_id = organization_id
        this.status = status
    }

    constructor(content: String?, organization_id: Int, status: String?) {
        this.content = content
        this.organization_id = organization_id
        this.status = status
    }
}