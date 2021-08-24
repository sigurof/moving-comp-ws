package no.sigurof.movingcomp.domain

import no.sigurof.movingcomp.dto.OrderOut
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long>, JpaSpecificationExecutor<Order>