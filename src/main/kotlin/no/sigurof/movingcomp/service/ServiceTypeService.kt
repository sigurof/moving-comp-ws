package no.sigurof.movingcomp.service

import no.sigurof.movingcomp.domain.servicetypes.*
import no.sigurof.movingcomp.dto.CleaningServiceDto
import no.sigurof.movingcomp.dto.MovingServiceDto
import no.sigurof.movingcomp.dto.PackingServiceDto
import no.sigurof.movingcomp.dto.ServiceTypeDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ServiceTypeService(
        @Autowired private val cleaningServiceRepository: CleaningServiceRepository,
        @Autowired private val packingServiceRepository: PackingServiceRepository,
        @Autowired private val movingServiceRepository: MovingServiceRepository,
) {
    fun edit(serviceType: ServiceType, data: ServiceTypeDto): ServiceType {
        return when (data) {
            is CleaningServiceDto -> editCleaning(serviceType, data)
            is PackingServiceDto -> editPacking(serviceType, data)
            is MovingServiceDto -> editMoving(serviceType, data)
        }

    }

    private fun editMoving(serviceType: ServiceType, data: MovingServiceDto): MovingService {
        return when (serviceType) {
            is MovingService -> serviceType.apply {
                this.fromAdr = data.fromAdr
                this.toAdr = data.toAdr
            }
            else -> error("Kan ikke endre MovingService til en ${serviceType.javaClass.simpleName}")
        }
    }

    private fun editPacking(serviceType: ServiceType, data: PackingServiceDto): PackingService {
        return when (serviceType) {
            is PackingService -> serviceType.apply {
                this.adr = data.adr
            }
            else -> error("Kan ikke endre PackingService til en ${serviceType.javaClass.simpleName}")
        }
    }

    private fun editCleaning(serviceType: ServiceType, data: CleaningServiceDto): CleaningService {
        return when (serviceType) {
            is CleaningService -> serviceType.apply {
                this.adr = data.adr
            }
            else -> error("Kan ikke endre CleaningService til en ${serviceType.javaClass.simpleName}")
        }
    }


}