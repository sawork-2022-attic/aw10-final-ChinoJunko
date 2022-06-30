package com.micropos.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.webflux.dsl.WebFlux;
import org.springframework.stereotype.Component;

@Component
public class HttpInboundGateway {
    @Bean
    public IntegrationFlow inGate() {
        return IntegrationFlows.from(WebFlux.inboundGateway("/delivery/{id}")
                        .requestMapping(m -> m.methods(HttpMethod.GET))
                        .payloadExpression("#pathVariables.id"))
                .headerFilter("accept-encoding", false)
                .channel("pos_channel")
                .get();
    }
}
