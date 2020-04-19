package com.peakacard.session.data.datasource.remote.model.mapper

import com.peakacard.session.data.datasource.remote.model.UserDataModel
import com.peakacard.user.domain.model.User

class UserDataModelMapper {

    fun map(userDataModel: UserDataModel?): User? {
        return userDataModel?.let {
            with(it) {
                User(uid, name, email)
            }
        }
    }
}
