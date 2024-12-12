package edu.westminster.ticketingsystem.ticketing_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocketConfig configures the WebSocket message broker for the ticketing system.
 * This class enables WebSocket communication and defines the endpoints and messaging
 * protocols used by the system for real-time updates.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configures the message broker for the WebSocket system.
     * - Enables a simple in-memory message broker for subscriptions on "/topic".
     * - Sets the prefix "/app" for application destination mappings (client-to-server messages).
     *
     * @param config The MessageBrokerRegistry to configure.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Broker for subscriptions
        config.setApplicationDestinationPrefixes("/app"); // Prefix for client-to-server messages
    }

    /**
     * Registers the WebSocket endpoints for the application.
     * - Defines "/ws-status" as the main WebSocket endpoint for clients to connect.
     * - Allows cross-origin requests from any origin using "*" (can be restricted for security).
     *
     * @param registry The StompEndpointRegistry to register endpoints.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-status") // Main WebSocket endpoint
                .setAllowedOriginPatterns("*"); // Allow connections from all frontend origins
    }
}
