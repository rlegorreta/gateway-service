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
 *  IndexController.java
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.gatewayservice.web;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller that receives all possible gateway endpoints
 *
 * @project gateway service
 * @author rlh
 * @date: June 2023
 */
@Controller
public class IndexController {

    /**
     * Redirects the "/" path to the "/index" path. The index path needs security, so if the user is not logged
     * in it is redirected with the Oauth2 Login page, otherwise to the Gateway index page where several functions
     * are amplemented
     * @return always "/index" path
     */
    @GetMapping("/")
    public String root() {
        return "index";
    }

    /**
     * The "/index" path needs security, so if the user is not logged in it is redirected with the Oauth2 Login page,
     * otherwise to the Gateway index page where several debugging operations are showed
     * @return always "/index" path
     */
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * This path is received from the Authorization Spring Server when the user is logged in.
     * @param code : the token
     * @return /index path
     */
    @GetMapping(value = "/authorized", params = "code")
    public String authorization(Model model) {
        String[] messages = {"User authorized"};

        model.addAttribute("messages", messages);

        return "index";
    }

    /**
     * This path is receivde from the Spring authorization Server if an error ocurred
     */
    @GetMapping(value = "/authorized", params = OAuth2ParameterNames.ERROR)
    public String authorizationFailed(Model model) {
        String[] messages = {"** Error in the user authentication ** "};

        model.addAttribute("messages", messages);

        return "index";
    }

    /**
     * This path came from the index.html page when the user clicks the Authorization code link.
     * In this operation the app shows the Oauth2Client information and alsa calls the iam-service to get
     * the authorities from the logged user.
     * @return /index path
     */
    @GetMapping(value = "/authorize", params = "grant_type=authorization_code")
    public String authorizeAuthorizationCodeGrant(Model model, @RegisteredOAuth2AuthorizedClient("gateway-service-oidc")
                                         OAuth2AuthorizedClient authorizedClient) {
        if (authorizedClient != null) {
            String[] messages = {authorizedClient.getClientRegistration().getClientId(),
                    authorizedClient.getPrincipalName(),
                    authorizedClient.getClientRegistration().getScopes().toString()};

            model.addAttribute("messages", messages);
        } else
            model.addAttribute("messages", null);

        return "index";
    }

    /**
     * This path came from the index.html page when the user clicks the Client credentials link.
     * In this operation since it is a host-host communication we just sho a message.
     *
     * Future debugging operations will be implemented that are host-host communication.
     *
     * @return /index path
     */
    @GetMapping(value = "/authorize", params = "grant_type=client_credentials")
    public String authorizeClientCredentialsGrant(Model model) {
        String[] messages = {"Microservice authorized for client_credentials"};

        model.addAttribute("messages", messages);

        return "index";
    }

    /**
     * This path came from the index.html page when the user clicks the Device credentials link.
     * TODO: implement Spring Authorization Server device security.
     *
     * @return /index path
     */
    @GetMapping(value = "/authorize", params = "grant_type=device_code")
    public String authorizeDeviceCodeGrant(Model model) {
        String[] messages = {"To be implemented for mobile devices"};

        model.addAttribute("messages", messages);

        return "index";
    }

    /**
     * TODO: For Spring Security 6.2.0 when Back channel logout will be implemented.
     * @return
     */
    @PostMapping("/logged-out")
    public String loggedOut() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>> IN logged-out (POST)");

        return "logged-out";
    }

    /**
     * TODO: For Spring Security 6.2.0 when Back channel logout will be implemented.
     * @return
     */
    @GetMapping("/logged-out")
    public String loggedOutGet() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>> IN logged-out (GET)");

        return "logged-out";
    }

    /**
     * TODO: For Spring Security 6.2.0 when Back channel logout will be implemented.
     * @return
     */
    @PostMapping("/logout")
    public String loggOut() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>> IN logg-out");

        return "redirect:/index";
    }

}