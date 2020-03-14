package com.peakacard.app.start.data.model

sealed class StartSessionResponseDataModel {
    object Success : StartSessionResponseDataModel()

    sealed class Error : StartSessionResponseDataModel() {
        object NoSessionFound : Error()
        object RemoteException : Error()
    }
}