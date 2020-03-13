package com.peakacard.app.start.domain.model

data class SessionDomainModel(
    val name: NameDomainModel,
    val code: CodeDomainModel
)

inline class NameDomainModel(val value: String)
inline class CodeDomainModel(val value: String)