package no.sigurof.movingcomp.web

import no.sigurof.movingcomp.domain.Order
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(path = "bestilling")
interface OrderRestRepository : PagingAndSortingRepository<Order, Long> {

}