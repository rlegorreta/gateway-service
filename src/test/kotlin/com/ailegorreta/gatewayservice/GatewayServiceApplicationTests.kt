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
 *  GatewayServiceApplicationTests.kt
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.gatewayservice

import com.ailegorreta.commons.security.config.SecurityConfig
import com.ailegorreta.commons.security.repository.SessionRepository
import com.ailegorreta.gatewayservice.user.UserController
import com.ailegorreta.gatewayservice.web.IndexController
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName


/**
 * This tests utilizes a Redis test container
 *
 * @author rlh
 * @project : Gateway service
 * @date June 2023
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
/* ^ Loads a full Spring web application context and a web environment listening on a random port */
//@WebFluxTest(IndexController::class)
@AutoConfigureWebTestClient(timeout = "36000")
@Testcontainers			/* Activates automatic startup and cleanup of test container */
@Import(SecurityConfig::class)
class GatewayServiceApplicationTests {

	companion object {
		const val REDIS_PORT = 6379

		/* Defines a Redis container for testing */
		@Container
		var redis: GenericContainer<*> = GenericContainer(DockerImageName.parse("redis:7.0"))
																		 .withExposedPorts(REDIS_PORT)

		/**
		 * Overwrites the Redis configuration to point to the test Redis instance
		 */
		@DynamicPropertySource
		fun redisProperties(registry: DynamicPropertyRegistry) {
			registry.add("spring.data.redis.host") { redis.host }
			registry.add("spring.data.redis.port") { redis.getMappedPort(REDIS_PORT) }
		}
	}

	@MockBean
	var clientRegistrationRepository: ReactiveClientRegistrationRepository? = null

	@Autowired
	var webClient: WebTestClient? = null

	/**
	 * An empty test used to verify that the application context is loaded correctly and that a connection with Redis
	 * has been established successfully
	 */
	@Test
	fun verifyThatSpringContextLoads() {
		println("ReactiveClientRegistrationRepository:$clientRegistrationRepository")
	}

	@Test
	@Throws(Exception::class)
	fun indexWhenUnAuthenticatedThenRedirect() {
		// @formatter:off
		webClient!!.get()
				   .uri("/")
				   .exchange()
				   .expectStatus().is3xxRedirection()
		// @formatter:on
	}

	@Test
	@WithMockUser
	@Throws(Exception::class)
	fun indexWhenAuthenticatedThenOk() {
		// @formatter:off
		webClient!!.get()
				   .uri("/")
			       .exchange()
			       .expectStatus().isOk()
		// @formatter:on
	}

}
