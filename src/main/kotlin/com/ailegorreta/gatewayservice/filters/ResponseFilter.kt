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
 *  ResponseFilter.kt
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.gatewayservice.filters

import com.ailegorreta.commons.utils.HasLogger
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

/**
 * This post filter where it adds the correlation_id to the caller.
 * Other usefully operation can be added.
 *
 * We set the trackedId from Sleuth as the correlation-id and add it to the header
 *
 * @author rlh
 * @project : gateway-service
 * @date May 202
 */
@Order(2)
@Configuration
class ResponseFilter(/* val tracer: Tracer, */ val filterUtils: FilterUtils): HasLogger {

    @Bean
    fun postGlobalFilter(): GlobalFilter {
        return GlobalFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
                                chain.filter(exchange).then(Mono.fromRunnable {
                                    val correlationId = filterUtils.getCorrelationId(exchange.request.headers)

                                    if (correlationId != null) {
                                        exchange.response.headers.add(FilterUtils.CORRELATION_ID, correlationId)
                                        logger.debug("Post filter: Completing outgoing request for {}. Adding correlation-id", exchange.request.uri)
                                    } else {
                                        logger.error("Post filter: try to add the correlation id but no exist. This must be fixed. Zipkin will not work correctly")
                                    }
                                })
                            }
    }

}
