package com.peakacard.app.card.data.datasource.remote.model.mapper

import com.peakacard.app.card.data.datasource.remote.model.VoteRequest
import com.peakacard.app.card.domain.model.Vote

class VoteMapper {

    fun map(vote: Vote): VoteRequest {
        return with(vote) {
            VoteRequest(sessionId, votingTitle, uid, score)
        }
    }
}
