package com.gnoemes.shikimori.entities.common

import com.gnoemes.shikimori.entities.roles.CharacterResponse
import com.gnoemes.shikimori.entities.roles.PersonResponse
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class RolesResponse(
        @SerialName("roles") val roles: List<String>,
        @SerialName("roles_russian") val rolesRu: List<String>,
        @SerialName("character") val character: CharacterResponse?,
        @SerialName("person") val person: PersonResponse?
)