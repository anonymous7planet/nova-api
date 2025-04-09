package com.nova.anonymousplanet.gateway.filter;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 모든 응답 데이터(JSON)에 path와 RequestId설정
 */
@Component
public class GlobalResponseFilter implements GlobalFilter {

    private final ObjectMapper objectMapper;

    public GlobalResponseFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();

        return chain.filter(exchange.mutate().response(new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (getStatusCode() != null && getStatusCode().is2xxSuccessful()
                        && getHeaders().getContentType() != null
                        && getHeaders().getContentType().includes(MediaType.APPLICATION_JSON)) {

                    Flux<? extends DataBuffer> modifiedBody = Flux.from(body)
                            .map(dataBuffer -> modifyResponse(exchange, dataBuffer, bufferFactory));

                    return super.writeWith(modifiedBody);
                }
                return super.writeWith(body);
            }
        }).build());
    }

    private DataBuffer modifyResponse(ServerWebExchange exchange, DataBuffer dataBuffer, DataBufferFactory bufferFactory) {
        try {
            byte[] content = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(content);
            DataBufferUtils.release(dataBuffer);

            String body = new String(content, StandardCharsets.UTF_8);
            JsonNode jsonNode = objectMapper.readTree(body);

            // requestId & path 추가
            ((ObjectNode) jsonNode).put("requestId", exchange.getRequest().getHeaders().getFirst("X-Request-Id"));
            ((ObjectNode) jsonNode).put("path", exchange.getRequest().getURI().getPath());

            byte[] modifiedBody = objectMapper.writeValueAsBytes(jsonNode);
            return bufferFactory.wrap(modifiedBody);
        } catch (IOException e) {
            throw new RuntimeException("Response modification failed", e);
        }
    }
}