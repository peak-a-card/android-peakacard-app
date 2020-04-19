package com.peakacard.app.session.view.model.mapper

import com.peakacard.user.domain.model.User
import com.peakacard.session.view.model.UserUiModel

class UserUiModelMapper {

    fun map(userUiModel: UserUiModel): User {
        return with(userUiModel) {
            User(uid, name, mail)
        }
    }
}
