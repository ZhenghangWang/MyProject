package com.demo.PocketStore.utils

import com.demo.PocketStore.db.bean.EventData

import java.util.Comparator
import java.util.Collections

object ListSortUtils {
    fun listSortingByName(listInAppxList: List<EventData?>?): List<EventData?>? {
        val comparator: Comparator<EventData?> = Comparator { details1, details2 ->
            details1!!.title!!.compareTo(details2!!.title!!)
            // return details1.compareTo(details2);
//                if(details1.getHits() < details2.getHits())
//                    return 1;
//                else {
//                    return -1;
//                }
        }
        //这里就会自动根据规则进行排序
        Collections.sort(listInAppxList, comparator)
        return listInAppxList
    }

    fun listSortingByDate(listInAppxList: List<EventData?>?): List<EventData?>? {
        val comparator: Comparator<EventData?> = Comparator { details1, details2 ->
            details1!!.date!!.compareTo(details2!!.date!!)
            // return details1.compareTo(details2);
//                if(details1.getHits() < details2.getHits())
//                    return 1;
//                else {
//                    return -1;
//                }
        }
        //这里就会自动根据规则进行排序
        Collections.sort(listInAppxList, comparator)
        return listInAppxList
    }
}