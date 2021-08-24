package no.sigurof.movingcomp.jackson

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Jsr310JavaTimeModuleProvider {

    @Bean
    fun javaTimeModule(): JavaTimeModule {
        return JavaTimeModule()
    }
}