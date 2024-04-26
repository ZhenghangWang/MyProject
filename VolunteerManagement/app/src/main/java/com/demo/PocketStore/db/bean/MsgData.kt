package com.demo.PocketStore.db.bean

class MsgData {
    var id = 0
    var sender_id = 0
    @kotlin.jvm.JvmField
    var receiver_id = 0
    var sender_type = 0
    @kotlin.jvm.JvmField
    var receiver_type = 0
    var message: String? = null
    var sent_time: String? = null

    constructor(
        id: Int, sender_id: Int, receiver_id: Int, sender_type: Int, receiver_type: Int,
        message: String?, sent_time: String?
    ) : super() {
        this.id = id
        this.sender_id = sender_id
        this.receiver_id = receiver_id
        this.sender_type = sender_type
        this.receiver_type = receiver_type
        this.message = message
        this.sent_time = sent_time
    }

    constructor(
        sender_id: Int, receiver_id: Int, sender_type: Int, receiver_type: Int,
        message: String?, sent_time: String?
    ) : super() {
        this.sender_id = sender_id
        this.receiver_id = receiver_id
        this.sender_type = sender_type
        this.receiver_type = receiver_type
        this.message = message
        this.sent_time = sent_time
    }

    constructor() : super() {}
}