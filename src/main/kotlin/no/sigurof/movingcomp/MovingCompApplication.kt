

package no.sigurof.movingcomp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("hello")
@SpringBootApplication
class MovingCompApplication {


    @GetMapping("world")
    fun hello(@RequestParam(name = "name", defaultValue = "World") name: String): String {
        return "Hello $name!"
    }

}

fun main(args: Array<String>) {
    runApplication<MovingCompApplication>(*args)
}
