package no.sigurof.movingcomp.service

import no.sigurof.movingcomp.domain.Order
import no.sigurof.movingcomp.domain.servicetypes.CleaningService
import no.sigurof.movingcomp.domain.servicetypes.MovingService
import no.sigurof.movingcomp.domain.servicetypes.PackingService
import no.sigurof.movingcomp.domain.servicetypes.ServiceType
import no.sigurof.movingcomp.dto.*
import org.springframework.stereotype.Component


@Component
class Converter {

    fun map(order: Order): OrderOut {
        return OrderOut(
                id = order.id!!,
                name = order.name,
                phone = order.phone,
                email = order.email,
                serviceType = map(order.serviceType),
                date = order.date
        )
    }

    private fun map(serviceType: ServiceType): ServiceTypeDto {
        return when (serviceType) {
            is CleaningService -> CleaningServiceDto(adr = serviceType.adr)
            is PackingService -> PackingServiceDto(adr = serviceType.adr)
            is MovingService -> MovingServiceDto(fromAdr = serviceType.fromAdr, toAdr = serviceType.toAdr)
            else -> error("Ikke implementert for type ${serviceType.javaClass.simpleName}")
        }
    }

    fun map(source: PlaceOrderRequest): Order {
        return Order(
                name = source.name,
                phone = source.phone,
                email = source.email,
                serviceType = map(source.serviceType),
                date = source.date
        )
    }

    fun map(source: ServiceTypeDto): ServiceType {
        return when (source) {
            is CleaningServiceDto -> CleaningService(id = null, adr = source.adr)
            is PackingServiceDto -> PackingService(id = null, adr = source.adr)
            is MovingServiceDto -> MovingService(id = null, fromAdr = source.fromAdr, toAdr = source.toAdr)
        }
    }

}