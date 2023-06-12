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
 *  WebEndpoints.kt
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.gatewayservice.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

/**
 * Functional endpoints in Spring WebFlux are defined as routes in a RouterFunction<ServerResponse> bean, using the
 * fluent API provided by RouterFunctions. For each route, it is necessary to define the endpoint URL,
 * a method, and a handler.
 *
 * @author rlh
 * @project : Gateway service
 * @date May 2023
 */
@Configuration
class WebEndpoints {
    /** Functional endPoints are defined in a bean **/
    @Bean
    fun routerFunction(): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
                              .GET("/test-service-fallback") { _: ServerRequest ->
                                ServerResponse.ok().body( Mono.just("NO EN LINEA"), String::class.java)} /* GET returns an empty string */
                              .POST("/test-service-fallback") { _: ServerRequest ->
                                ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build() } /* return HTTP 503 error */
                              .build()
    }
}