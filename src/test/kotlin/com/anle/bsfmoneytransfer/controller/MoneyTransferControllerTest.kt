package com.anle.bsfmoneytransfer.controller

import com.anle.bsfmoneytransfer.BsfMoneyTransferApplicationTests
import com.anle.bsfmoneytransfer.dto.TransferMoneyDto
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal

class MoneyTransferControllerTest : BsfMoneyTransferApplicationTests() {

    @Test
    @SqlGroup(Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:sql/accounts.sql"]),
            Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:sql/accounts_clear.sql"]))
    fun getAccount() {
        mvc.perform(getRequest(GET_ACCOUNT.format(1)))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(100L))
    }

    @Test
    @SqlGroup(Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:sql/accounts.sql"]),
            Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:sql/accounts_clear.sql"]))
    fun accountNotFound() {
        mvc.perform(getRequest(GET_ACCOUNT.format(-100)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @SqlGroup(Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:sql/accounts_clear.sql"]))
    fun createAccount() {
        mvc.perform(postRequest(CREATE_ACCOUNT))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(0L))
        val dbAccount = testQueries.getAccountById(1)
        assertEquals(1L, dbAccount.id)
        assertEquals(0, dbAccount.balance.intValueExact())
    }

    @Test
    @SqlGroup(Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:sql/accounts.sql"]),
            Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:sql/accounts_clear.sql"]))
    fun increaseBalance() {
        mvc.perform(putRequest(INCREASE_BALANCE.format(1, 150)))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(250L))
        val dbAccount = testQueries.getAccountById(1)
        assertEquals(1L, dbAccount.id)
        assertEquals(250, dbAccount.balance.intValueExact())
    }

    @Test
    @SqlGroup(Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:sql/accounts.sql"]),
            Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:sql/accounts_clear.sql"]))
    fun increaseBalanceInvalidSum() {
        mvc.perform(putRequest(INCREASE_BALANCE.format(1, -150)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError)
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Sum should be more than 0"))
    }

    @Test
    @SqlGroup(Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:sql/accounts.sql"]),
            Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:sql/accounts_clear.sql"]))
    fun decreaseBalance() {
        mvc.perform(putRequest(DECREASE_BALANCE.format(3, 150)))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(150L))
        val dbAccount = testQueries.getAccountById(3)
        assertEquals(3L, dbAccount.id)
        assertEquals(150, dbAccount.balance.intValueExact())
    }

    @Test
    @SqlGroup(Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:sql/accounts.sql"]),
            Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:sql/accounts_clear.sql"]))
    fun decreaseBalanceFail() {
        mvc.perform(putRequest(DECREASE_BALANCE.format(3, 400)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError)
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Account balance cannot be less than 0"))
    }

    @Test
    @SqlGroup(Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:sql/accounts.sql"]),
            Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:sql/accounts_clear.sql"]))
    fun transferMoney() {
        mvc.perform(patchRequest(TRANSFER_MONEY, TransferMoneyDto(2, 1, BigDecimal(150))))
                .andExpect(MockMvcResultMatchers.status().isOk)
        val dbAccountFrom = testQueries.getAccountById(2)
        val dbAccountTo = testQueries.getAccountById(1)
        assertEquals(50, dbAccountFrom.balance.intValueExact())
        assertEquals(250, dbAccountTo.balance.intValueExact())
    }

    @Test
    @SqlGroup(Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:sql/accounts.sql"]),
            Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:sql/accounts_clear.sql"]))
    fun transferMoneyFail() {
        mvc.perform(patchRequest(TRANSFER_MONEY, TransferMoneyDto(1, 2, BigDecimal(150))))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError)
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Account balance cannot be less than 0"))
    }

    companion object {
        private const val GET_ACCOUNT = "/api/v1/account/%d"
        private const val CREATE_ACCOUNT = "/api/v1/account"
        private const val INCREASE_BALANCE = "/api/v1/account/%d/increase/%d"
        private const val DECREASE_BALANCE = "/api/v1/account/%d/decrease/%d"
        private const val TRANSFER_MONEY = "/api/v1/account/transfer"
    }
}