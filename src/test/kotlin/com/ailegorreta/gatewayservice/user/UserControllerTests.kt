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
 *  UserControllerTests.kt
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.gatewayservice.user

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.StandardClaimNames
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.OidcLoginMutator
import org.springframework.test.web.reactive.server.WebTestClient
import com.ailegorreta.commons.security.config.SecurityConfig
import org.assertj.core.api.Assertions.assertThat
import org.springframework.context.annotation.Import

/**
 * This tests is to check that the UserController returns the user that it is in session
 *
 * @author rlh
 * @project : Gateway service
 * @date June 2023
 */
@WebFluxTest(UserController::class)
@Import(SecurityConfig::class)
class UserControllerTests {

    @Autowired
    var webClient: WebTestClient? = null

    @MockBean
    var clientRegistrationRepository: ReactiveClientRegistrationRepository? = null

    @Test
    fun whenNotAuthenticatedThen401() {
        webClient!!.get()
                    .uri("/user")
                    .exchange()
                    .expectStatus().is3xxRedirection()
    }

    @Test
    fun whenAuthenticatedThenReturnUser() {
        val expectedUser = User("jon.snow", "Jon", "Snow", listOf("employee", "customer"))

        webClient!!.mutateWith(configureMockOidcLogin(expectedUser))
                    .get()
                    .uri("/user")
                    .exchange()
                    .expectStatus().is2xxSuccessful()
                    .expectBody(User::class.java)
                    .value { user -> assertThat(user).isEqualTo(expectedUser) }
    }


    private fun configureMockOidcLogin(expectedUser: User): OidcLoginMutator {
        return SecurityMockServerConfigurers.mockOidcLogin().idToken { builder: OidcIdToken.Builder ->
                                    builder.claim(StandardClaimNames.PREFERRED_USERNAME, expectedUser.username())
                                    builder.claim(StandardClaimNames.GIVEN_NAME, expectedUser.firstName())
                                    builder.claim(StandardClaimNames.FAMILY_NAME, expectedUser.lastName())
                                    builder.claim("roles", expectedUser.roles())
        }
    }

}

