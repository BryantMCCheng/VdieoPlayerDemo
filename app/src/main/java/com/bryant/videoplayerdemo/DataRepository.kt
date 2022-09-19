package com.bryant.videoplayerdemo

import com.bryant.videoplayerdemo.apis.Service

object DataRepository {
    suspend fun getVideoDataList() = Service.playseeApi.getVideoDataList()
}