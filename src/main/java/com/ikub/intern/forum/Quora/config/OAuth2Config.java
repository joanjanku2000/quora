package com.ikub.intern.forum.Quora.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.UnAuthenticatedServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static java.lang.String.format;
import static java.util.Arrays.asList;

//@Configuration
public class OAuth2Config {
//        @Bean("authProvider") //bean qualifier
//        WebClient mifeWebClient(ReactiveClientRegistrationRepository clientRegistrations) {
//            ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
//                    new ServerOAuth2AuthorizedClientExchangeFilterFunction(
//                            clientRegistrations,
//                            new UnAuthenticatedServerOAuth2AuthorizedClientRepository());
//            oauth.setDefaultClientRegistrationId("authProvider");
//            return WebClient.builder()
//                    .filter(oauth)
//                    .build();
//
//    }
//    @Bean
//    public ReactiveClientRegistrationRepository get(){
//            return new ReactiveClientRegistrationRepository() {
//                @Override
//                public Mono<ClientRegistration> findByRegistrationId(String s) {
//                    return null;
//                }
//            };
//    }
}
