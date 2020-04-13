package com.peakacard.app.session.data.datasource.remote.model.mapper

import com.peakacard.app.session.data.datasource.remote.model.UserDataModel
import com.peakacard.app.session.domain.model.User

class UserMapper {

    fun map(user: User): UserDataModel {
        return with(user) {
            UserDataModel(uid, name, mail)
        }
    }
}
