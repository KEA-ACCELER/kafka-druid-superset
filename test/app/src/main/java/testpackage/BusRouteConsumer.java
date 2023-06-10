package testpackage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import java.time.Duration;
import java.util.Collections;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;

import com.fasterxml.jackson.databind.JsonNode;


import java.sql.*;


public class BusRouteConsumer {
  
      public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        // mysql 데이터베이스에 연결하기 위한 정보
        String url = "jdbc:mysql://10.0.0.162/seoulDB?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"; // test는 데이터베이스 이름
        String user = "root"; // 사용자 이름
        String password = "Acceler1234!"; // 비밀번호
            
        // Connection 객체 생성
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("conn"+ conn);


        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19094,localhost:29094,localhost:39094");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "BUSROUTE");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        
        try (KafkaConsumer<String, JsonNode> consumer = new KafkaConsumer<>(consumerProps)) {
        
            consumer.subscribe(Collections.singletonList("dbserver1.seoulDB.seoulRide"));
            //consumer.seekToBeginning();
            Properties producerProps = new Properties();
            producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19094,localhost:29094,localhost:39094");
            producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

            try (KafkaProducer<String, JsonNode> producer = new KafkaProducer<>(producerProps)) {
                // 레코드를 가져오는 루프
                while (true) {
                // input-topic에서 레코드를 가져옴After 
                ConsumerRecords<String, JsonNode> records = consumer.poll (Duration.ofMillis (100));
                for (ConsumerRecord<String, JsonNode> record : records) {
                    
                    /**
                    * CREATE TABLE BusRoute (
                    _id INT PRIMARY KEY AUTO_INCREMENT,
                    BUS_ROUTE_NO VARCHAR(20) unique,
                    BUS_ROUTE_NM VARCHAR(60)
                    );
                    */           
                    
                    // PreparedStatement 객체 생성
                    PreparedStatement pstmt = conn.prepareStatement("insert ignore into BusRoute set BUS_ROUTE_NO=?, BUS_ROUTE_NM =?" );

                    // 파라미터 바인딩
                    String bss_ars_no = record.value ().get ("payload").get ("after").get ("BUS_ROUTE_NO").toString();  
                    String bus_sta_nm = record.value ().get ("payload").get ("after").get ("BUS_ROUTE_NM").toString();
                    pstmt.setString(1, bss_ars_no.substring(1, bss_ars_no.length() - 1));
                    pstmt.setString(2, bus_sta_nm.substring(1, bus_sta_nm.length() - 1));
                
                   
                    // SQL 문 실행
                    int result = pstmt.executeUpdate();
                                    
                    // 결과 출력
                    if (result > 0) {
                        System.out.println("ROW INSERTED");
                    } else {
                        // System.out.println("ROW NOT INSERTED");
                    }
                    }
                    
                }
            }
        }
    }


   
    
}
