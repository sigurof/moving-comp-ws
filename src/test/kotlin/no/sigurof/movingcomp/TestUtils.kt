package no.sigurof.movingcomp

import org.springframework.test.context.transaction.TestTransaction
import javax.persistence.EntityManager
import javax.persistence.Table

object TestUtils {

    private val defaultTablesNotToTruncate: List<String> = listOf()

    fun <T> withinTransaction(myFunc: () -> T): T {
        if (!TestTransaction.isActive()) {
            TestTransaction.start()
        }
        val value: T = myFunc.invoke()
        TestTransaction.flagForCommit()
        TestTransaction.end()
        return value
    }

    fun truncateAllTables(
            tablesNotToTruncate: List<String> = defaultTablesNotToTruncate,
            entityManager: EntityManager
    ) {
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        entityManager
                .metamodel
                .managedTypes.stream()
                .map { it.javaType.getAnnotation(Table::class.java) }
                .filter { it != null }
                .map(Table::name)
                .filter { !tablesNotToTruncate.contains(it) }
                .forEach { name -> entityManager.createNativeQuery("TRUNCATE TABLE " + name).executeUpdate() };
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}