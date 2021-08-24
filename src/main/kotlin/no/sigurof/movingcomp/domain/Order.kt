package no.sigurof.movingcomp.domain

import no.sigurof.movingcomp.domain.servicetypes.ServiceType
import java.time.LocalDate
import javax.persistence.*


@Entity
@Table(name = "ORDERS")
data class Order(
        @Id
        @Column(name = "ID", updatable = false, nullable = false)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_gen")
        @SequenceGenerator(name = "order_gen", sequenceName = "order_seq", allocationSize = 1)
        val id: Long? = null,
        @Column(name = "name")
        var name: String,
        @Column(name = "phone")
        var phone: String,
        @Column(name = "email")
        var email: String,
        @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
        @JoinColumn(name = "ORDER_ID")
        var serviceType: ServiceType,
        @Column(name = "dato")
        var date: LocalDate
) {
    constructor(
            name: String,
            phone: String,
            email: String,
            serviceType: ServiceType,
            date: LocalDate
    ) : this(id = null,
            name = name,
            phone = phone,
            email = email,
            serviceType = serviceType,
            date = date)
}