package com.anle.bsfmoneytransfer.extension

import com.anle.bsfmoneytransfer.exception.DataInvalidException
import java.math.BigDecimal

fun BigDecimal.validatePositive() = if (this <= BigDecimal.ZERO) {
    throw DataInvalidException("Amount should be more than zero")
} else {
}