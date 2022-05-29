package com.anle.bsfmoneytransfer.extension

import com.anle.bsfmoneytransfer.exception.DataInvalidException
import java.math.BigDecimal

fun BigDecimal.validatePositive() = if (this <= BigDecimal.ZERO) {
    throw DataInvalidException("Sum should be more than 0")
} else {
}