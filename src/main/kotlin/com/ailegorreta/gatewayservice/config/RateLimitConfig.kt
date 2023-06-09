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
 *  RateLimiterConfig.kt
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.gatewayservice.config

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.security.Principal

/**
 * The RequestRateLimiter filter relies on a KeyResolver bean to determine which bucket to use for each request.
 * By default, it uses the currently authenticated user in Spring Security.
 *
 * Apply the rate limits to each user independently.
 *
 * @author rlh
 * @project : Gateway service
 * @date June 2023
 */
@Configuration
class RateLimiterConfig {

    @Bean
    fun keyResolver(): KeyResolver {
        return KeyResolver { exchange: ServerWebExchange ->
            /** Gets the currently authenticated user (the principal) from the current
             *  request (the exchange)                                                  */
            exchange.getPrincipal<Principal>()
                    .map { obj: Principal -> obj.name }
                    /** ^ Extracts the username from the principal */
                    .defaultIfEmpty("anonymous")
                    /** ^If the request is unauthenticated, it uses “anonymous” as
                 *  the default key to apply rate-limiting.                        */
        }
    }
}