package com.peakacard.app.session.data.model

sealed class JoinSessionResponseDataModel {
    object Success : JoinSessionResponseDataModel()

    sealed class Error : JoinSessionResponseDataModel() {
        object NoSessionFound : Error()
        object RemoteException : Error()
    }
}