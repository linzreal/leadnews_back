package com.heima.com.heima.kafka.sample;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerTest {

    //1.kafka链接配置信息

    //2.创建生产者对象，

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.200.130:9092");

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");


        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        ProducerRecord<String,String> producerRecord = new ProducerRecord<>("topic-first","key-001","hello kafka");

        producer.send(producerRecord);
        System.out.println("&&&&&&&");
        producer.close();
    }

}
