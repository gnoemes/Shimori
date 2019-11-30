package com.gnoemes.shikimori.entities.common

import com.gnoemes.shikimori.entities.roles.CharacterResponse
import com.gnoemes.shikimori.entities.roles.PersonResponse
import com.google.gson.annotations.SerializedName

internal data class RolesResponse(
        @field:SerializedName("roles") val roles: List<String>,
        @field:SerializedName("roles_russian") val rolesRu: List<String>,
        @field:SerializedName("character") val character: CharacterResponse?,
        @field:SerializedName("person") val person: PersonResponse?
)