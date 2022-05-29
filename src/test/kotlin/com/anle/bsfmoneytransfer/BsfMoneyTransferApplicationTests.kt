package com.anle.bsfmoneytransfer

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@TestPropertySource(locations = ["classpath:test.properties"])
@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestQueries::class)
class BsfMoneyTransferApplicationTests {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var testQueries: TestQueries

    @Autowired
    lateinit var mvc: MockMvc

    protected fun getRequest(url: String): RequestBuilder {
        return MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
    }

    protected fun postRequest(url: String): RequestBuilder {
        return MockMvcRequestBuilders.post(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
    }

    protected fun putRequest(url: String): RequestBuilder {
        return MockMvcRequestBuilders.put(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
    }

    protected fun patchRequest(url: String, request: Any?): RequestBuilder {
        return MockMvcRequestBuilders.patch(url)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
    }
}
