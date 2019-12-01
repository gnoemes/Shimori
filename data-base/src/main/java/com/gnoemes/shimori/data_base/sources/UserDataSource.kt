package com.gnoemes.shimori.data_base.sources

interface UserDataSource {

   suspend fun getMyUser()
}