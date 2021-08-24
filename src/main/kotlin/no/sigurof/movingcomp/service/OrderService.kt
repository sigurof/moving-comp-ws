package no.sigurof.movingcomp.service

import no.sigurof.movingcomp.domain.Order
import no.sigurof.movingcomp.domain.OrderRepository
import no.sigurof.movingcomp.dto.EditOrderRequest
import no.sigurof.movingcomp.dto.OrderOut
import no.sigurof.movingcomp.dto.PlaceOrderRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class OrderService(
        @Autowired private val orderRepository: OrderRepository,
        @Autowired private val serviceTypeService: ServiceTypeService,
        @Autowired private val converter: Converter
) {

    fun placeOrder(placeOrderRequest: PlaceOrderRequest): OrderOut {
        val orderToSave = converter.map(placeOrderRequest)
        val savedOrder = orderRepository.save(orderToSave)
        return converter.map(savedOrder)
    }

    fun editOrder(id: Long, editOrderRequest: EditOrderRequest) {
        val order = orderRepository.findById(id)
                .orElseThrow { error("Fant ikke Order med id = $id") }
        order.date = editOrderRequest.date
        order.phone = editOrderRequest.phone
        order.name = editOrderRequest.name
        order.email = editOrderRequest.email
        order.serviceType = serviceTypeService.edit(order.serviceType, editOrderRequest.serviceType)
        orderRepository.save(order);
    }

    fun delete(id: Long) {
        orderRepository.deleteById(id);
    }

    fun findByFilter(filter: String?): List<OrderOut> {
        return if (filter == null || filter.isEmpty()) {
            orderRepository.findAll()
        } else {
            orderRepository.findAll(anyFieldContains(filter))
        }
                .map { converter.map(it) }
    }

    private fun anyFieldContains(filter: String): Specification<Order> {
        val filterString = "%${filter}%"
        return Specification.where(matcherId(filterString))
                .or(matcherDate(filterString))
                .or(matcherPhone(filterString))
                .or(matcherEmail(filterString))
                .or(matcherName(filterString))
    }

    private fun matcherId(filter: String): Specification<Order> {
        return Specification { root, criteriaQuery, criteriaBuilder ->
            criteriaBuilder.like(root.get<Long>("id").`as`(String::class.java), filter)
        }
    }

    private fun matcherPhone(filter: String): Specification<Order> {
        return Specification { root, criteriaQuery, criteriaBuilder ->
            criteriaBuilder.like(root.get("phone"), filter)
        }
    }

    private fun matcherName(filter: String): Specification<Order> {
        return Specification { root, criteriaQuery, criteriaBuilder ->
            criteriaBuilder.like(root.get("name"), filter)
        }
    }

    private fun matcherEmail(filter: String): Specification<Order> {
        return Specification { root, criteriaQuery, criteriaBuilder ->
            criteriaBuilder.like(root.get("email"), filter)
        }
    }

    private fun matcherDate(filter: String): Specification<Order> {
        return Specification { root, criteriaQuery, criteriaBuilder ->
            criteriaBuilder.like(root.get<LocalDate>("date").`as`(String::class.java), filter)
        }
    }


}