

package no.sigurof.movingcomp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class MovingCompApplication {

}

fun main(args: Array<String>) {
    runApplication<MovingCompApplication>(*args)
}
