package no.sigurof.movingcomp.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
sealed class ServiceTypeDto

data class MovingServiceDto(
        val fromAdr: String,
        val toAdr: String
) : ServiceTypeDto()

data class PackingServiceDto(
        val adr: String
) : ServiceTypeDto()

data class CleaningServiceDto(
        val adr: String
) : ServiceTypeDto()

