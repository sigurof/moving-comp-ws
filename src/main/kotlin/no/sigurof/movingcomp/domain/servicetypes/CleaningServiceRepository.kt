package no.sigurof.movingcomp.domain.servicetypes

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
public interface CleaningServiceRepository : JpaRepository<MovingService, Long> {
}