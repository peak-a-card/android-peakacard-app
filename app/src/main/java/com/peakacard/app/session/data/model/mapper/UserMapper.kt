package com.peakacard.app.session.data.model.mapper

import com.peakacard.app.session.data.model.UserDataModel
import com.peakacard.app.session.domain.model.User

class UserMapper {

    fun map(user: User): UserDataModel {
        return with(user) {
            UserDataModel(uid, name, mail)
        }
    }
}
