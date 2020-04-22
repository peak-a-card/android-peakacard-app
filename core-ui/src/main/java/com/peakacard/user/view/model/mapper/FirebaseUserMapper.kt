package com.peakacard.user.view.model.mapper

import com.google.firebase.auth.FirebaseUser
import com.peakacard.user.view.model.UserUiModel

class FirebaseUserMapper {

  fun map(firebaseUser: FirebaseUser?): UserUiModel? {
    if (firebaseUser == null) return null
    return with(firebaseUser) {
      UserUiModel(
        uid,
        displayName ?: "",
        email ?: ""
      )
    }
  }
}
