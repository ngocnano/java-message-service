package com.ngoctm.jms.listener;

import com.ngoctm.jms.config.JmsConfig;
import com.ngoctm.jms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HelloListener {

    private final JmsTemplate jmsTemplate;

    //@JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage, @Header MessageHeaders messageHeaders,
                       Message message){
        System.out.println("I got a message!!!");
        System.out.println(helloWorldMessage);
    }

    @JmsListener(destination = JmsConfig.MY_QUEUE_SR)
    public void listenForHello(@Payload HelloWorldMessage helloWorldMessage, //@Header MessageHeaders messageHeaders,
                       Message message) throws JMSException {
        HelloWorldMessage helloWorldMessageSend = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("Send message")
                .build();
        jmsTemplate.convertAndSend((Destination) message.getJMSReplyTo(), helloWorldMessageSend);
    }

}
