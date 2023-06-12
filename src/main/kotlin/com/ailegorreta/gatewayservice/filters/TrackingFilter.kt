/* Copyright (c) 2023, LegoSoft Soluciones, S.C.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 *  TrackingFilter.kt
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.gatewayservice.filters

import com.ailegorreta.commons.utils.HasLogger
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.*


/**
 * This is a Pre-filter for the Gateway routes.
 *
 * What is validated:
 * - If the calling has a correlation-id is present. If it is not present then a new one is added.
 *   nota: normally it is present because we´re using Sleuth but Sleuth does not make it public,
 *
 * @author rlh
 * @project : gateway-service
 * @date May 2023
 */
@Order(1)
@Component
class TrackingFilter(val filterUtils: FilterUtils) : GlobalFilter, HasLogger {

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val requestHeaders = exchange.request.headers
        var exch = exchange

        if (isCorrelationIdPresent(requestHeaders))
            logger.debug("lm-correlation-id found in tracking filter: {}. ",filterUtils.getCorrelationId(requestHeaders))
        else {
            val correlationID = generateCorrelationId()

            exch = filterUtils.setCorrelationId(exchange, correlationID)
            logger.debug("lm-correlation-id generated in tracking filter: {}.", correlationID)
        }

        return chain.filter(exch)
    }

    private fun isCorrelationIdPresent(requestHeaders: HttpHeaders) =  filterUtils.getCorrelationId(requestHeaders) != null

    private fun generateCorrelationId() = UUID.randomUUID().toString()

}

