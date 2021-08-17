package com.ngoctm.jms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngoctm.jms.config.JmsConfig;
import com.ngoctm.jms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    public void sendMessage(){
       // System.out.println("I'm Sending a message......... on " );
        HelloWorldMessage helloWorldMessage = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("Hello world ")
                .build();
        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, helloWorldMessage);
       // System.out.println("Message Send!");
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndReceiveMessage() throws JMSException {

        HelloWorldMessage helloWorldMessage = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("Hello world ")
                .build();
        Message messageReceive = jmsTemplate.sendAndReceive(JmsConfig.MY_QUEUE_SR, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message message = null;
                try {
                    message = session.createTextMessage(objectMapper.writeValueAsString(helloWorldMessage));
                } catch (JsonProcessingException e) {
                    throw new JMSException(e.getMessage());
                }
                message.setStringProperty("_type", "com.ngoctm.jms.model.HelloWorldMessage");
                return message;
            }
        });
        System.out.println("Re " + messageReceive.getBody(String.class));
    }
}
