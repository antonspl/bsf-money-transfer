package com.anle.bsfmoneytransfer

import com.anle.bsfmoneytransfer.entity.Account
import org.assertj.core.util.Maps
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent
import javax.persistence.EntityManager
import javax.persistence.Query

@Suppress("UNCHECKED_CAST")
@TestComponent
class TestQueries {

    @Autowired
    private lateinit var entityManager: EntityManager

    fun testQuery(query: String, params: Map<String?, Any?>): Query {
        val jpaQuery = entityManager.createQuery(query)
        params.forEach { (name: String?, value: Any?) -> jpaQuery.setParameter(name, value) }
        return jpaQuery
    }

    fun getAccountById(id: Long): Account {
        return testQuery(GET_ACCOUNT_BY_ID, Maps.newHashMap<String?, Any?>(FIELD_ID, id)).singleResult as Account
    }

    companion object {
        private const val FIELD_ID = "id"
        private const val GET_ACCOUNT_BY_ID = "SELECT o FROM Account o WHERE o.id = :id"
    }

}