package com.example.foody.data

import com.example.foody.data.network.LocalDataSource
import com.example.foody.data.network.RemoteDataSource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@ViewModelScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
){

    val remote = remoteDataSource
    val local = localDataSource
}