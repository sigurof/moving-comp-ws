package no.sigurof.movingcomp

import com.fasterxml.jackson.databind.ObjectMapper
import no.sigurof.movingcomp.TestUtils.withinTransaction
import no.sigurof.movingcomp.domain.Order
import no.sigurof.movingcomp.domain.OrderRepository
import no.sigurof.movingcomp.domain.servicetypes.CleaningService
import no.sigurof.movingcomp.domain.servicetypes.MovingService
import no.sigurof.movingcomp.domain.servicetypes.PackingService
import no.sigurof.movingcomp.dto.EditOrderRequest
import no.sigurof.movingcomp.dto.MovingServiceDto
import no.sigurof.movingcomp.dto.OrderOut
import no.sigurof.movingcomp.dto.PlaceOrderRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import java.time.LocalDate
import javax.persistence.EntityManager
import javax.transaction.Transactional

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class OrderResourceIT(
        @Autowired val testRestTemplate: TestRestTemplate,
        @Autowired val orderRepository: OrderRepository,
        @Autowired val entityManager: EntityManager,
        @Autowired val objectMapper: ObjectMapper
) {

    private val voidType = object : ParameterizedTypeReference<Void>() {}
    private val listOfOrderOut = object : ParameterizedTypeReference<List<OrderOut>>() {}

    @BeforeEach
    fun beforeEach() {
        TestUtils.truncateAllTables(listOf(), entityManager)
    }

    @Test
    fun edit() {
        val oldName = "Ola"
        val newName = "Even"
        val oldToAdress = "Oslo"
        val newToAdress = "Trondheimm"

        val movingOrderRequest = aMovingOrder().copy(name = oldName, serviceType = MovingServiceDto(fromAdr = "Ski", toAdr = oldToAdress))
        val editOrderRequest = anEditOrder().copy(name = newName, serviceType = MovingServiceDto(fromAdr = "Ski", toAdr = newToAdress))

        val id: Long = withinTransaction() {
            testRestTemplate.exchange("/order/place", HttpMethod.POST, HttpEntity(movingOrderRequest), OrderOut::class.java)
                    .body
        }!!.id
        val orderBeforeEdit: Order = withinTransaction { orderRepository.findById(id).get() }

        withinTransaction {
            testRestTemplate.exchange("/order/edit?id=$id", HttpMethod.PUT, HttpEntity(editOrderRequest), voidType)
        }
        val orderAfterEdit: Order = withinTransaction { orderRepository.findById(id).get() }

        assertThat(orderBeforeEdit.name).isEqualTo(oldName)
        assertThat(orderAfterEdit.name).isEqualTo(newName)

        assertThat((orderBeforeEdit.serviceType as MovingService).toAdr).isEqualTo(oldToAdress)
        assertThat((orderAfterEdit.serviceType as MovingService).toAdr).isEqualTo(newToAdress)

        assertThat(orderBeforeEdit.serviceType.id).isEqualTo(orderAfterEdit.serviceType.id)

    }

    @Test
    fun searchReturnererElementerSomInneholderFilteret() {
        // Setup
        val filter = "12"
        val OlaFlytting = anOrder().copy(name = "Ola", phone = "171211111", date = LocalDate.of(2011, 1, 1)) // inneholder 12
        val joergenPakking = anOrder().copy(name = "Jørgen", phone = "111111111", date = LocalDate.of(2012, 1, 1)) // inneholder 12
        val minaVasking = anOrder().copy(name = "Mina", phone = "22222222", date = LocalDate.of(2011, 1, 1)) // inneholder IKKE 12
        val ordersToSave = listOf(OlaFlytting, joergenPakking, minaVasking)
        withinTransaction {
            orderRepository.saveAll(ordersToSave)
        }

        // Execution
        val bestillinger: List<OrderOut> = withinTransaction {
            testRestTemplate.exchange("/order/search?filter=$filter", HttpMethod.GET, null, listOfOrderOut);
        }.body!!

        assertThat(bestillinger).asList().hasSize(2);
        assertThat(bestillinger.map { it.name }).containsExactlyInAnyOrderElementsOf(listOf("Ola", "Jørgen"))
    }

    @Test
    fun searchReturnererAlleNarFilterIkkeTilstede() {
        // Setup
        val OlaFlytting = anOrder()
        val joergenPakking = anOrder().copy(name = "Jørgen", serviceType = PackingService(id = null, adr = "Oslo"))
        val minaVasking = anOrder().copy(name = "Mina", serviceType = CleaningService(id = null, adr = "Drøbak"))
        val ordersToSave = listOf(OlaFlytting, joergenPakking, minaVasking)
        withinTransaction {
            orderRepository.saveAll(ordersToSave)
        }

        // Execution
        val allOrdersNoFilter: List<OrderOut> = withinTransaction {
            testRestTemplate.exchange("/order/search", HttpMethod.GET, null, listOfOrderOut);
        }.body!!
        val allOrdersFilterNotWritten: List<OrderOut> = withinTransaction {
            testRestTemplate.exchange("/order/search?filter=", HttpMethod.GET, null, listOfOrderOut);
        }.body!!
        val allOrdersFilterIsNull: List<OrderOut> = withinTransaction {
            testRestTemplate.exchange("/order/search?filter=null", HttpMethod.GET, null, listOfOrderOut);
        }.body!!

        // Assertion
        assertThat(allOrdersNoFilter).asList().hasSize(ordersToSave.size)
        assertThat(allOrdersFilterNotWritten).asList().hasSize(ordersToSave.size)
        assertThat(allOrdersFilterIsNull).asList().hasSize(0)  // Fordi filter=null gir filter = "null"
    }

    @Test
    fun place() {

        val movingOrderRequest = aMovingOrder()

        // Execute
        val id: Long = withinTransaction() {
            testRestTemplate.exchange("/order/place", HttpMethod.POST, HttpEntity(movingOrderRequest), OrderOut::class.java)
                    .body
        }!!.id

        withinTransaction {
            val all = orderRepository.findAll()
            assertThat(all.size).isEqualTo(1);
            assertThat(all[0].id).isEqualTo(id)
        }
    }

    @Test
    fun delete() {
        // Setup
        val id: Long = withinTransaction {
            orderRepository.save(anOrder())
        }.id!!

        // Execution
        withinTransaction { testRestTemplate.exchange("/order/delete?id=${id}", HttpMethod.DELETE, null, voidType) }

        // Assertion
        withinTransaction {
            assertThat(orderRepository.count()).isEqualTo(0)
            assertThat(orderRepository.findById(id)).isEmpty
        }

    }

    private fun aMovingOrder(): PlaceOrderRequest {
        return PlaceOrderRequest(
                name = "Ola",
                phone = "123456789",
                email = "olanordmann@gmail.com",
                serviceType = MovingServiceDto(fromAdr = "Oslo", toAdr = "Ski"),
                date = LocalDate.now().plusDays(10)
        )

    }


    private fun anEditOrder(): EditOrderRequest {
        return EditOrderRequest(
                name = "Ola",
                phone = "123456789",
                email = "olanordmann@gmail.com",
                serviceType = MovingServiceDto(fromAdr = "Oslo", toAdr = "Ski"),
                date = LocalDate.now().plusDays(10)
        )
    }

    private fun anOrder(): Order {
        return Order(
                name = "Ola",
                phone = "123456789",
                email = "olanordmann@gmail.com",
                serviceType = MovingService(id = null, fromAdr = "Oslo", toAdr = "Ski"),
                date = LocalDate.now().plusYears(1)
        )
    }

}
