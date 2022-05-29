package com.anle.bsfmoneytransfer.service.impl

import com.anle.bsfmoneytransfer.dto.TransferMoneyDto
import com.anle.bsfmoneytransfer.dto.TransferredAccountsDto
import com.anle.bsfmoneytransfer.entity.Account
import com.anle.bsfmoneytransfer.exception.DataInvalidException
import com.anle.bsfmoneytransfer.exception.DataNotFoundException
import com.anle.bsfmoneytransfer.extension.validatePositive
import com.anle.bsfmoneytransfer.repository.AccountRepository
import com.anle.bsfmoneytransfer.service.MoneyTransferService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class MoneyTransferServiceImpl(val accountRepository: AccountRepository) : MoneyTransferService {

    private val log: Logger = LoggerFactory.getLogger(MoneyTransferService::class.java)

    override fun getAccount(id: Long): Account = accountRepository.findById(id)
            .orElseThrow { DataNotFoundException("Account not found by id = $id") }

    override fun createAccount(): Account = accountRepository.save(Account())

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    override fun increaseBalance(id: Long, sum: BigDecimal): Account {
        log.info("Start increasing balance, accountId = $id")
        sum.validatePositive()
        val account = getAccount(id)
        account.balance += sum
        return accountRepository.save(account)
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    override fun decreaseBalance(id: Long, sum: BigDecimal): Account {
        log.info("Start decreasing balance, accountId = $id")
        sum.validatePositive()
        val account = getAccount(id)
        account.balance -= sum
        return if (account.balance >= BigDecimal.ZERO) accountRepository.save(account) else
            throw DataInvalidException("Account balance cannot be less than 0")
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    override fun transferMoney(dto: TransferMoneyDto): Any {
        log.info("Start transferring money")
        dto.sum.validatePositive()
        val accountFrom = getAccount(dto.accountFromId)
        val accountTo = getAccount(dto.accountToId)
        accountFrom.balance -= dto.sum
        if (accountFrom.balance < BigDecimal.ZERO) throw DataInvalidException("Account balance cannot be less than 0")
        accountTo.balance += dto.sum
        return TransferredAccountsDto(accountRepository.save(accountFrom), accountRepository.save(accountTo))
    }
}