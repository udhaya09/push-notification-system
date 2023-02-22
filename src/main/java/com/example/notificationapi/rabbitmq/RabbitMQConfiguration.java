package com.example.notificationapi.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

	@Value("${rabbitmq.queue.name}")
	public static final String QUEUE = "pn_poc_new_product_queue";

	@Value("${rabbitmq.exchange.name}")
	public static final String EXCHANGE = "pn_poc_new_product_exchange";

	@Value("${rabbitmq.routingkey.name}")
	public static final String ROUTING_KEY = "pn_poc_new_product_routing_key";

	// Defines the RabbitMQ queue
	@Bean
	public Queue queue() {
		return new Queue(QUEUE);
	}

	// Defines the RabbitMQ exchange
	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(EXCHANGE);
	}

	// Defines the binding between the queue and the exchange
	@Bean
	public Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
	}

	// Defines the message converter to be used for message serialization
	@Bean
	public MessageConverter converter() {
		return new Jackson2JsonMessageConverter();
	}

	// Defines the RabbitMQ template to be used for sending messages
	@Bean
	public AmqpTemplate template(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(converter());
		return rabbitTemplate;
	}

}
