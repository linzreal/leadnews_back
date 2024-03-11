package com.heima.com.heima.kafka.sample;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerTest {

    public static void main(String[] args) {

        Properties properties = new Properties();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.200.130:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");

        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"group-002");

        KafkaConsumer<String,String> kafkaConsumer = new KafkaConsumer<>(properties);

        kafkaConsumer.subscribe(Collections.singletonList("topic-first"));


        while(true){
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));

            for(ConsumerRecord record: records){
                System.out.println(record.key());
                System.out.println(record.value());
            }
        }




    }
}
