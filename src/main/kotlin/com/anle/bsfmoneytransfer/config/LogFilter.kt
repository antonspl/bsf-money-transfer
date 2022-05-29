package com.anle.bsfmoneytransfer.config

import org.slf4j.LoggerFactory
import org.springframework.util.AntPathMatcher
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LogFilter : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger("REST")

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {

        val requestWrapper = ContentCachingRequestWrapper(request)
        val responseWrapper = ContentCachingResponseWrapper(response)

        try {
            filterChain.doFilter(requestWrapper, responseWrapper)
        } finally {
            logHttpRequest(requestWrapper, responseWrapper)
            responseWrapper.copyBodyToResponse()
        }
    }

    private fun logHttpRequest(requestWrapper: ContentCachingRequestWrapper, responseWrapper: ContentCachingResponseWrapper) {
        if (!excludeFromFilter(requestWrapper.requestURL.toString())) {
            try {
                log.info(">> request {} from remote addr/host/port [{}]/[{}]/[{}] - {}: \r\n{} {}, {}================================\r\n",
                        requestWrapper.method,
                        requestWrapper.remoteAddr,
                        requestWrapper.remoteHost,
                        requestWrapper.remotePort,
                        URLDecoder.decode(
                                String.format("%s%s", requestWrapper.requestURL.toString(),
                                        Optional.ofNullable(requestWrapper.queryString).map { v -> String.format("?%s", v) }.orElse("")), "UTF-8"),
                        formatBodyString("headers", getHeaderString(requestWrapper)),
                        formatBodyString("request", "content - ${getPayload(requestWrapper.contentAsByteArray)}, parameters - ${(requestWrapper.parameterMap)}"),
                        formatBodyString("response", getPayload(responseWrapper.contentAsByteArray))
                )
            } catch (e: Exception) {
                log.error("Error on logging request/response", e)
            }

        }
    }

    private fun excludeFromFilter(path: String): Boolean {
        val pathMatcher = AntPathMatcher()
        return pathMatcher.match("**/swagger**", path)
    }

    private fun formatBodyString(prefix: String, body: String): String {
        return if (StringUtils.hasText(prefix)) {
            String.format("%s: [%s]", prefix, body)
        } else {
            ""
        }
    }

    private fun getPayload(buf: ByteArray?): String {
        return if (buf != null && buf.isNotEmpty()) {
            String(buf, 0, buf.size, Charset.forName("UTF-8"))
        } else {
            ""
        }
    }

    private fun getHeaderString(httpRequest: HttpServletRequest): String {
        return httpRequest.headerNames.toList().joinToString { "${it}=${httpRequest.getHeader(it)}" }
    }
}