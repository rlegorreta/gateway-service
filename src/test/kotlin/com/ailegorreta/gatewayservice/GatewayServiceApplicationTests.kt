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

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

/**
 * This tests utilizes a Redis test container
 *
 * @author rlh
 * @project : Gateway service
 * @date May 2023
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
/* ^ Loads a full Spring web application context and a web environment listening on a random port */
@Testcontainers			/* Activates automatic startup and cleanup of test container */
class GatewayServiceApplicationTests {

	companion object { const val REDIS_PORT = 6379 }

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

	/**
	 * An empty test used to verify that the application context is loaded correctly and that a connection with Redis
	 * has been established successfully
	 */
	@Test
	fun verifyThatSpringContextLoads() {
	}

}
