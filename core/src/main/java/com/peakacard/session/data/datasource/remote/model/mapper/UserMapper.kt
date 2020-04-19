package com.peakacard.session.data.datasource.remote.model.mapper

import com.peakacard.user.data.datasource.remote.model.UserDataModel
import com.peakacard.user.domain.model.User

class UserMapper {

    fun map(user: User): UserDataModel {
        return with(user) {
            UserDataModel(
                uid,
                name,
                mail
            )
        }
    }
}
