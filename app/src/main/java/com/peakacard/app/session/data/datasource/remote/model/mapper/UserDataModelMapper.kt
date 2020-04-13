package com.peakacard.app.session.data.datasource.remote.model.mapper

import com.peakacard.app.session.data.datasource.remote.model.UserDataModel
import com.peakacard.app.session.domain.model.User

class UserDataModelMapper {

    fun map(userDataModel: UserDataModel?): User? {
        return userDataModel?.let {
            with(it) {
                User(uid, name, email)
            }
        }
    }
}
