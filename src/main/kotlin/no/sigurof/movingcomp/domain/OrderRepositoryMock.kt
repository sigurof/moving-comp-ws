package no.sigurof.movingcomp.domain

import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryMock {

    private val orders = mutableListOf<Order>()

    fun findAll(): List<Order> {
        return orders;
    }

    fun save(order: Order) {
//        orders
//                .find { it.id == order.id }
//                ?.let { overwrite(it, order) }
//                ?: orders.add(order);
        orders.add(order)
    }

    fun overwrite(toOverWrite: Order, data: Order): Order {
        toOverWrite.date = data.date
        toOverWrite.email = data.email
        toOverWrite.name = data.name
        toOverWrite.phone = data.phone
//        toOverWrite.serviceType =
        return toOverWrite;
    }
}