package com.example.data

import com.example.data.network.RemoteDataSource
import javax.inject.Inject


class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource
){

    val remote = remoteDataSource
}