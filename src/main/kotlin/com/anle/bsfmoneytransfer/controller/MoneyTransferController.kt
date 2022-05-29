package com.anle.bsfmoneytransfer.controller

import com.anle.bsfmoneytransfer.dto.TransferMoneyDto
import com.anle.bsfmoneytransfer.service.MoneyTransferService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/account")
class MoneyTransferController(val moneyTransferService: MoneyTransferService) {

    @GetMapping("/{id}")
    fun getAccount(@PathVariable id: Long) = moneyTransferService.getAccount(id)

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    fun createAccount() = moneyTransferService.createAccount()

    @PutMapping("/{id}/increase/{sum}")
    fun increaseBalance(@PathVariable id: Long, @PathVariable sum: BigDecimal) = moneyTransferService.increaseBalance(id, sum)

    @PutMapping("/{id}/decrease/{sum}")
    fun decreaseBalance(@PathVariable id: Long, @PathVariable sum: BigDecimal) = moneyTransferService.decreaseBalance(id, sum)

    @PatchMapping("/transfer")
    fun transferMoney(@RequestBody dto: TransferMoneyDto) = moneyTransferService.transferMoney(dto)

}
