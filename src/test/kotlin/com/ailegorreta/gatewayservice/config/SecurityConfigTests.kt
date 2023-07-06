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
 *  SecurityConfigTests.kt
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.gatewayservice.config

import com.ailegorreta.commons.security.config.SecurityConfig
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

/**
 * This tests is to check that the security configuration is working
 *
 * @author rlh
 * @project : Gateway service
 * @date June 2023
 */
@WebFluxTest
@Import(SecurityConfig::class)
class SecurityConfigTests {

    @Autowired
    var webClient: WebTestClient? = null

    @MockBean
    var clientRegistrationRepository: ReactiveClientRegistrationRepository? = null

    @Test
    fun whenLogoutNotAuthenticatedAndNoCsrfTokenThen403() {
        webClient!!.post()
            .uri("/logout")
            .exchange()
            .expectStatus().isForbidden()
    }

    @Test
    fun whenLogoutAuthenticatedAndNoCsrfTokenThen403() {
        webClient!!.mutateWith(SecurityMockServerConfigurers.mockOidcLogin())
            .post()
            .uri("/logout")
            .exchange()
            .expectStatus().isForbidden()
    }

    @Test
    fun whenLogoutAuthenticatedAndWithCsrfTokenThen302() {
        Mockito.`when`(clientRegistrationRepository!!.findByRegistrationId("test"))
            .thenReturn(Mono.just(testClientRegistration()))

        webClient!!.mutateWith(SecurityMockServerConfigurers.mockOidcLogin())
            .mutateWith(SecurityMockServerConfigurers.csrf())
            .post()
            .uri("/logout")
            .exchange()
            .expectStatus().isFound()
    }

    private fun testClientRegistration(): ClientRegistration {
        return ClientRegistration.withRegistrationId("test")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .clientId("gateway-service")
            .authorizationUri("https://auth-service/auth")
            .tokenUri("https://auth-service/token")
            .redirectUri("https://auth-service")
            .build()
    }

}
