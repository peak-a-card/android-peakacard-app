package com.peakacard.user.data.repository

import com.peakacard.session.data.datasource.remote.model.mapper.UserDataModelMapper
import com.peakacard.session.data.datasource.remote.model.mapper.UserMapper
import com.peakacard.user.domain.model.User
import com.peakacard.user.data.datasource.local.UserLocalDataSource

class UserRepository(
    private val userLocalDataSource: UserLocalDataSource,
    private val userMapper: UserMapper,
    private val userDataModelMapper: UserDataModelMapper
) {

    fun saveCurrentUser(user: User) {
        userLocalDataSource.saveUser(userMapper.map(user))
    }

    fun getCurrentUser(): User? {
        return userDataModelMapper.map(userLocalDataSource.getUser())
    }
}
