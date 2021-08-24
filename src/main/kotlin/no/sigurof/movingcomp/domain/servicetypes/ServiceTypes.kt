package no.sigurof.movingcomp.domain.servicetypes


import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
open class ServiceType(
        @Id
        @Column(name = "ID", updatable = false, nullable = false)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_type_gen")
        @SequenceGenerator(name = "service_type_gen", sequenceName = "service_type_seq", allocationSize = 1)
        open val id: Long? = null,
)

@Entity
@Table(name = "MOVING_SERVICE")
class MovingService(
        id: Long?,
        @Column(name = "FROM_ADR")
        var fromAdr: String,
        @Column(name = "TO_ADR")
        var toAdr: String
) : ServiceType(id = id)

@Entity
@Table(name = "PACKING_SERVICE")
class PackingService(
        id: Long?,
        @Column(name = "ADR")
        var adr: String
) : ServiceType(id = id)

@Entity
@Table(name = "CLEANING_SERVICE")
class CleaningService(
        id: Long?,
        @Column(name = "ADR")
        var adr: String
) : ServiceType(id = id)
