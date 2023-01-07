package com.gnoemes.shimori.data.repositories.character

import com.gnoemes.shimori.data.core.database.daos.LastRequestDao
import com.gnoemes.shimori.data.core.entities.app.Request
import com.gnoemes.shimori.data.repositories.lastrequest.GroupLastRequestStore

class CharacterDetailsLastRequestStore(
    dao : LastRequestDao
) : GroupLastRequestStore(Request.CHARACTER_DETAILS, dao)