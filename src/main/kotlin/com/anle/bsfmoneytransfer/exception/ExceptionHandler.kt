package com.anle.bsfmoneytransfer.exception

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
@RestController
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(DataNotFoundException::class)
    fun dataNotFoundHandler(ex: DataNotFoundException) = ResponseEntity(ex.message, BAD_REQUEST)

    @ExceptionHandler(DataInvalidException::class)
    fun dataInvalidHandler(ex: DataInvalidException) = ResponseEntity(ex.message, BAD_REQUEST)
}