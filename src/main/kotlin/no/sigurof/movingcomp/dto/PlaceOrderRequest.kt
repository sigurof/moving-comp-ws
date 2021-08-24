package no.sigurof.movingcomp.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate


data class PlaceOrderRequest(val name: String,
                             val phone: String,
                             val email: String,
                             val serviceType: ServiceTypeDto,
                             @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
                             val date: LocalDate
)

