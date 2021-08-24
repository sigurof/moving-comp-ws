package no.sigurof.movingcomp.web

import no.sigurof.movingcomp.dto.EditOrderRequest
import no.sigurof.movingcomp.dto.OrderOut
import no.sigurof.movingcomp.dto.PlaceOrderRequest
import no.sigurof.movingcomp.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping("order")
class OrderResource(@Autowired private val orderService: OrderService) {

    @PutMapping("edit")
    fun edit(@RequestParam(name = "id") id: Long, @RequestBody editOrderRequest: EditOrderRequest) {
        orderService.editOrder(id, editOrderRequest);
    }

    @PostMapping("place")
    fun place(@RequestBody placeOrderRequest: PlaceOrderRequest): OrderOut {
        return orderService.placeOrder(placeOrderRequest);
    }

    @GetMapping("search")
    fun search(@RequestParam(name = "filter", required = false) filter: String?): List<OrderOut> {
        return orderService.findByFilter(filter);
    }

    @DeleteMapping("delete")
    fun delete(@RequestParam(name = "id") id: Long) {
        orderService.delete(id)
    }

}