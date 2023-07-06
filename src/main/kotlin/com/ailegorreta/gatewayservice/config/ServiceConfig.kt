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
 *  ServiceConfig.kt
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.gatewayservice.config

import com.ailegorreta.commons.security.config.SecurityServiceConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

/**
 * Configuration class to read all application properties. Including Security Service Config
 *
 * @project gateway service
 * @author rlh
 * @date: June 2023
 */
@Configuration
class ServiceConfig : SecurityServiceConfig {

    @Value("\${spring.security.oauth2.client.provider.spring.issuer-uri}")
    private val issuerUri: String = "Issuer uri not defined"
    override fun getIssuerUri() = issuerUri

    @Value("\${security.clientId}")
    private val securityClientId: String = "ClientID not defined"
    override fun getSecurityClientId() = securityClientId

    @Value("\${server.port}")
    private val serverPort: Int = 0
    override fun getServerPort() = serverPort

    @Value("\${security.iam.clientId}")
    private val securityIamClientId: String = "Issuer uri not defined"
    override fun getSecurityIAMClientId() = securityIamClientId

    @Value("\${security.iam.provider-uri}")
    private val securityIamProvider: String = "Issuer uri not defined"
    override fun getSecurityIAMProvider() = securityIamProvider
}