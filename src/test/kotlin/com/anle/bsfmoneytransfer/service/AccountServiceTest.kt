package com.anle.bsfmoneytransfer.service

import com.anle.bsfmoneytransfer.BsfMoneyTransferApplicationTests
import com.anle.bsfmoneytransfer.entity.Account
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import java.math.BigDecimal
import java.util.*
import junit.framework.TestCase.assertEquals
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors

class AccountServiceTest : BsfMoneyTransferApplicationTests() {

    @Test
    @SqlGroup(Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:sql/accounts.sql"]),
            Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:sql/accounts_clear.sql"]))
    fun increaseBalance2ConcurrentThreadsExecution() {
        val executorService = Executors.newFixedThreadPool(2)
        val callableList: MutableList<Callable<Account>> = ArrayList(2)
        for (i in 0..1) {
            callableList.add(Callable { accountService.increaseBalance(1, BigDecimal.ONE) })
        }
        val futures = executorService.invokeAll(callableList)
        Assertions.assertThrows(ExecutionException::class.java) { futures.map { it.get() } }
        assertEquals(101, testQueries.getAccountById(1).balance.intValueExact())
    }
}