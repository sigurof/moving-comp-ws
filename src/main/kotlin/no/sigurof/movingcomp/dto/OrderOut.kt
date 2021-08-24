package no.sigurof.movingcomp.dto

import java.time.LocalDate


data class OrderOut(val id: Long,
                    val name: String,
                    val phone: String,
                    val email: String,
                    val serviceType: ServiceTypeDto,
                    val date: LocalDate
)

