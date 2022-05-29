package com.anle.bsfmoneytransfer.repository

import com.anle.bsfmoneytransfer.entity.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository: JpaRepository<Account, Long> {
}