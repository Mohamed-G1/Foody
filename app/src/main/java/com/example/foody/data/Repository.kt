package com.example.foody.data

import com.example.foody.data.network.RemoteDataSource
import javax.inject.Inject


class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource
){

    val remote = remoteDataSource
}