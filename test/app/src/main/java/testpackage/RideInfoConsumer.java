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


public class RideInfoConsumer {
    
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
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "RIDEINFO");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        
        try (KafkaConsumer<String, JsonNode> consumer = new KafkaConsumer<>(consumerProps)) {
        
            consumer.subscribe(Collections.singletonList("rideTopic"));
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
                    * CREATE TABLE BusRide (
                    _id INT PRIMARY KEY AUTO_INCREMENT,
                        
                        USE_MON VARCHAR(20),

                        BUS_ROUTE_NM VARCHAR(60),
                        BSST_ARS_NO VARCHAR(20),
                        MNTN_TYP_CD VARCHAR(20),

                        SUM_OF_RIDE INTEGER,
                        SUM_OF_ALIGHT INTEGER,

                        MIDNIGHT_RIDE_NUM INTEGER,
                        MIDNIGHT_ALIGHT_NUM INTEGER,
                        ONE_RIDE_NUM INTEGER,
                        ONE_ALIGHT_NUM INTEGER,
                        TWO_RIDE_NUM INTEGER,
                        TWO_ALIGHT_NUM INTEGER,
                        THREE_RIDE_NUM INTEGER,
                        THREE_ALIGHT_NUM INTEGER,
                        FOUR_RIDE_NUM INTEGER,
                        FOUR_ALIGHT_NUM INTEGER,
                        FIVE_RIDE_NUM INTEGER,
                        FIVE_ALIGHT_NUM INTEGER,
                        SIX_RIDE_NUM INTEGER,
                        SIX_ALIGHT_NUM INTEGER,
                        SEVEN_RIDE_NUM INTEGER,
                        SEVEN_ALIGHT_NUM INTEGER,
                        EIGHT_RIDE_NUM INTEGER,
                        EIGHT_ALIGHT_NUM INTEGER,
                        NINE_RIDE_NUM INTEGER,
                        NINE_ALIGHT_NUM INTEGER,
                        TEN_RIDE_NUM INTEGER,
                        TEN_ALIGHT_NUM INTEGER,
                        ELEVEN_RIDE_NUM INTEGER,
                        ELEVEN_ALIGHT_NUM INTEGER,
                        TWELVE_RIDE_NUM INTEGER,
                        TWELVE_ALIGHT_NUM INTEGER,
                        
                        THIRTEEN_RIDE_NUM INTEGER,
                        THIRTEEN_ALIGHT_NUM INTEGER,
                        FOURTEEN_RIDE_NUM INTEGER,
                        FOURTEEN_ALIGHT_NUM INTEGER,
                        FIFTEEN_RIDE_NUM INTEGER,
                        FIFTEEN_ALIGHT_NUM INTEGER,
                        SIXTEEN_RIDE_NUM INTEGER,
                        SIXTEEN_ALIGHT_NUM INTEGER,
                        SEVENTEEN_RIDE_NUM INTEGER,
                        SEVENTEEN_ALIGHT_NUM INTEGER,
                        EIGHTEEN_RIDE_NUM INTEGER,
                        EIGHTEEN_ALIGHT_NUM INTEGER,
                        NINETEEN_RIDE_NUM INTEGER,
                        NINETEEN_ALIGHT_NUM INTEGER,
                        TWENTY_RIDE_NUM INTEGER,
                        TWENTY_ALIGHT_NUM INTEGER,
                        TWENTY_ONE_RIDE_NUM INTEGER,
                        TWENTY_ONE_ALIGHT_NUM INTEGER,
                        TWENTY_TWO_RIDE_NUM INTEGER,
                        TWENTY_TWO_ALIGHT_NUM INTEGER,
                        TWENTY_THREE_RIDE_NUM INTEGER,
                        TWENTY_THREE_ALIGHT_NUM INTEGER      
                    );
                    */           
                    
                    // PreparedStatement 객체 생성
                    PreparedStatement pstmt = conn.prepareStatement("insert into BusRide set USE_MON = ?, BUS_ROUTE_NM = ?, BSST_ARS_NO = ?, MNTN_TYP_CD = ?, SUM_OF_RIDE = ?, SUM_OF_ALIGHT = ?, MIDNIGHT_RIDE_NUM = ?, MIDNIGHT_ALIGHT_NUM = ?, ONE_RIDE_NUM = ?, ONE_ALIGHT_NUM = ?, TWO_RIDE_NUM = ?, TWO_ALIGHT_NUM = ?, THREE_RIDE_NUM = ?, THREE_ALIGHT_NUM = ?, FOUR_RIDE_NUM = ?, FOUR_ALIGHT_NUM = ?, FIVE_RIDE_NUM = ?, FIVE_ALIGHT_NUM = ?, SIX_RIDE_NUM = ?, SIX_ALIGHT_NUM = ?, SEVEN_RIDE_NUM = ?, SEVEN_ALIGHT_NUM = ?, EIGHT_RIDE_NUM = ?, EIGHT_ALIGHT_NUM = ?, NINE_RIDE_NUM = ?, NINE_ALIGHT_NUM = ?, TEN_RIDE_NUM = ?, TEN_ALIGHT_NUM = ?, ELEVEN_RIDE_NUM = ?, ELEVEN_ALIGHT_NUM = ?, TWELVE_RIDE_NUM = ?, TWELVE_ALIGHT_NUM = ?, THIRTEEN_RIDE_NUM = ?, THIRTEEN_ALIGHT_NUM = ?, FOURTEEN_RIDE_NUM = ?, FOURTEEN_ALIGHT_NUM = ?, FIFTEEN_RIDE_NUM = ?, FIFTEEN_ALIGHT_NUM = ?, SIXTEEN_RIDE_NUM = ?, SIXTEEN_ALIGHT_NUM = ?, SEVENTEEN_RIDE_NUM = ?, SEVENTEEN_ALIGHT_NUM = ?, EIGHTEEN_RIDE_NUM = ?, EIGHTEEN_ALIGHT_NUM = ?, NINETEEN_RIDE_NUM = ?, NINETEEN_ALIGHT_NUM = ?, TWENTY_RIDE_NUM = ?, TWENTY_ALIGHT_NUM = ?, TWENTY_ONE_RIDE_NUM = ?, TWENTY_ONE_ALIGHT_NUM = ?, TWENTY_TWO_RIDE_NUM = ?, TWENTY_TWO_ALIGHT_NUM = ?, TWENTY_THREE_RIDE_NUM = ?, TWENTY_THREE_ALIGHT_NUM = ?");

                    // 파라미터 바인딩
                    String use_mon = record.value ().get ("USE_MON").toString();
                    String bus_route_nm= record.value().get ("BUS_ROUTE_NM").toString();
                    String bss_ars_no= record.value ().get ("BSST_ARS_NO").toString();
                    String mntn_typ_cd= record.value ().get ("MNTN_TYP_CD").toString();
                    
                    pstmt.setString(1, use_mon.substring(1, use_mon.length() - 1));
                    pstmt.setString(2, bus_route_nm.substring(1, bus_route_nm.length() - 1));
                    pstmt.setString(3, bss_ars_no.substring(1, bss_ars_no.length() - 1));
                    pstmt.setString(4, mntn_typ_cd.substring(1, mntn_typ_cd.length() - 1));

                    pstmt.setInt(5, record.value().get("SUM_OF_RIDE").asInt());
                    pstmt.setInt(6, record.value().get("SUM_OF_ALIGHT").asInt());

                    pstmt.setInt(7, record.value().get("MIDNIGHT_RIDE_NUM").asInt());
                    pstmt.setInt(8, record.value().get("MIDNIGHT_ALIGHT_NUM").asInt());
                    pstmt.setInt(9, record.value().get("ONE_RIDE_NUM").asInt());
                    pstmt.setInt(10, record.value().get("ONE_ALIGHT_NUM").asInt());
                    pstmt.setInt(11, record.value().get("TWO_RIDE_NUM").asInt());
                    pstmt.setInt(12, record.value().get("TWO_ALIGHT_NUM").asInt());
                    pstmt.setInt(13, record.value().get("THREE_RIDE_NUM").asInt());
                    pstmt.setInt(14, record.value().get("THREE_ALIGHT_NUM").asInt());
                    pstmt.setInt(15, record.value().get("FOUR_RIDE_NUM").asInt());
                    pstmt.setInt(16, record.value().get("FOUR_ALIGHT_NUM").asInt());
                    pstmt.setInt(17, record.value().get("FIVE_RIDE_NUM").asInt());
                    pstmt.setInt(18, record.value().get("FIVE_ALIGHT_NUM").asInt());
                    pstmt.setInt(19, record.value().get("SIX_RIDE_NUM").asInt());
                    pstmt.setInt(20, record.value().get("SIX_ALIGHT_NUM").asInt());
                    pstmt.setInt(21, record.value().get("SEVEN_RIDE_NUM").asInt());
                    pstmt.setInt(22, record.value().get("SEVEN_ALIGHT_NUM").asInt());
                    pstmt.setInt(23, record.value().get("EIGHT_RIDE_NUM").asInt());
                    pstmt.setInt(24, record.value().get("EIGHT_ALIGHT_NUM").asInt());
                    pstmt.setInt(25, record.value().get("NINE_RIDE_NUM").asInt());
                    pstmt.setInt(26, record.value().get("NINE_ALIGHT_NUM").asInt());
                    pstmt.setInt(27, record.value().get("TEN_RIDE_NUM").asInt());
                    pstmt.setInt(28, record.value().get("TEN_ALIGHT_NUM").asInt());
                    pstmt.setInt(29, record.value().get("ELEVEN_RIDE_NUM").asInt());
                    pstmt.setInt(30, record.value().get("ELEVEN_ALIGHT_NUM").asInt());
                    pstmt.setInt(31, record.value().get("TWELVE_RIDE_NUM").asInt());
                    pstmt.setInt(32, record.value().get("TWELVE_ALIGHT_NUM").asInt());
                    pstmt.setInt(33, record.value().get("THIRTEEN_RIDE_NUM").asInt());
                    pstmt.setInt(34, record.value().get("THIRTEEN_ALIGHT_NUM").asInt());
                    pstmt.setInt(35, record.value().get("FOURTEEN_RIDE_NUM").asInt());
                    pstmt.setInt(36, record.value().get("FOURTEEN_ALIGHT_NUM").asInt());
                    pstmt.setInt(37, record.value().get("FIFTEEN_RIDE_NUM").asInt());
                    pstmt.setInt(38, record.value().get("FIFTEEN_ALIGHT_NUM").asInt());
                    pstmt.setInt(39, record.value().get("SIXTEEN_RIDE_NUM").asInt());
                    pstmt.setInt(40, record.value().get("SIXTEEN_ALIGHT_NUM").asInt());
                    pstmt.setInt(41, record.value().get("SEVENTEEN_RIDE_NUM").asInt());
                    pstmt.setInt(42, record.value().get("SEVENTEEN_ALIGHT_NUM").asInt());
                    pstmt.setInt(43, record.value().get("EIGHTEEN_RIDE_NUM").asInt());
                    pstmt.setInt(44, record.value().get("EIGHTEEN_ALIGHT_NUM").asInt());
                    pstmt.setInt(45, record.value().get("NINETEEN_RIDE_NUM").asInt());
                    pstmt.setInt(46, record.value().get("NINETEEN_ALIGHT_NUM").asInt());
                    pstmt.setInt(47, record.value().get("TWENTY_RIDE_NUM").asInt());
                    pstmt.setInt(48, record.value().get("TWENTY_ALIGHT_NUM").asInt());
                    pstmt.setInt(49, record.value().get("TWENTY_ONE_RIDE_NUM").asInt());
                    pstmt.setInt(50, record.value().get("TWENTY_ONE_ALIGHT_NUM").asInt());
                    pstmt.setInt(51, record.value().get("TWENTY_TWO_RIDE_NUM").asInt());
                    pstmt.setInt(52, record.value().get("TWENTY_TWO_ALIGHT_NUM").asInt());
                    pstmt.setInt(53, record.value().get("TWENTY_THREE_RIDE_NUM").asInt());
                    pstmt.setInt(54, record.value().get("TWENTY_THREE_ALIGHT_NUM").asInt());


                
                   
                    // SQL 문 실행
                    int result = pstmt.executeUpdate();
                                    
                    // 결과 출력
                    if (result > 0) {
                        System.out.println("ROW INSERTED");
                    } else {
                        System.out.println("ROW NOT INSERTED");
                    }
                    }
                    
                }
            }
        }
    }

}
