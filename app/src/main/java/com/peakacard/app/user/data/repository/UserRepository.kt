package com.peakacard.app.user.data.repository

import com.peakacard.app.session.data.datasource.remote.model.mapper.UserDataModelMapper
import com.peakacard.app.session.data.datasource.remote.model.mapper.UserMapper
import com.peakacard.app.session.domain.model.User
import com.peakacard.app.user.data.datasource.local.UserLocalDataSource

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
