package testpackage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.streams.StreamsConfig;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.*;


import org.apache.kafka.streams.kstream.Produced;
public class XYconsumer {
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
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        
        try (KafkaConsumer<String, JsonNode> consumer = new KafkaConsumer<>(consumerProps)) {
        
            consumer.subscribe(Collections.singletonList("dbserver1.seoulDB.busStation"));
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
                    // 레코드의 값에서 좌표를 가져옴
                    String xcode = record.value ().get ("payload").get ("after").get ("XCODE").toString();
                    xcode = xcode.substring(1, xcode.length() - 1);
                   
                    String ycode = (record.value ().get ("payload").get ("after").get ("YCODE")).toString ();
                    ycode = ycode.substring(1, ycode.length() - 1);
                    System.out.println("xcode After : "+xcode);
                    
                    // 좌표를 경위도로 변환
                    ArrayList<String> addr = coordToAddr(xcode, ycode);
                    System.out.println("addr : "+addr.toString());
                    // 변환된 경위도를 레코드에 추가
                    if (addr.size() == 3){
                    ((ObjectNode) record.value ().get ("payload").get ("after")).put ("REGION_1DEPTH_NAME", addr.get(0));
                    ((ObjectNode) record.value ().get ("payload").get ("after")).put ("REGION_2DEPTH_NAME", addr.get(1));
                    ((ObjectNode) record.value ().get ("payload").get ("after")).put ("REGION_3DEPTH_NAME", addr.get(2));
                    }
                    else {
                        ((ObjectNode) record.value ().get ("payload").get ("after")).put ("REGION_1DEPTH_NAME", "");
                        ((ObjectNode) record.value ().get ("payload").get ("after")).put ("REGION_2DEPTH_NAME", "");
                        ((ObjectNode) record.value ().get ("payload").get ("after")).put ("REGION_3DEPTH_NAME", "");
                    }

                    // 레코드를 outputTest으로 전송

                    // CREATE TABLE BusStation
                    // (
                    //   _id          INT PRIMARY KEY AUTO_INCREMENT,
                    //   BSST_ARS_NO VARCHAR(60),
                    //   BUS_STA_NM VARCHAR(60),

                    // 	REGION_1DEPTH_NAME VARCHAR(60),
                    // 	REGION_2DEPTH_NAME VARCHAR(60),
                    // 	REGION_3DEPTH_NAME VARCHAR(60)

                    // );             
                    
                    // PreparedStatement 객체 생성
                    PreparedStatement pstmt = conn.prepareStatement("insert ignore into BusStation set BSST_ARS_NO = ?,  BUS_STA_NM = ?, REGION_1DEPTH_NAME=?, REGION_2DEPTH_NAME=?, REGION_3DEPTH_NAME=? " );

                    // 파라미터 바인딩
                    String bss_ars_no = record.value ().get ("payload").get ("after").get ("STOP_NO").toString();  
                    String bus_sta_nm = record.value ().get ("payload").get ("after").get ("STOP_NM").toString();
                    pstmt.setString(1, bss_ars_no.substring(1, bss_ars_no.length() - 1));
                    pstmt.setString(2, bus_sta_nm.substring(1, bus_sta_nm.length() - 1));
                    if(addr.size() == 3){
                    pstmt.setString(3, addr.get(0));
                    pstmt.setString(4, addr.get(1));
                    pstmt.setString(5, addr.get(2));
                    }
                    else {
                        pstmt.setString(3, "");
                        pstmt.setString(4, "");
                        pstmt.setString(5, "");
                    }
                    // SQL 문 실행
                    int result = pstmt.executeUpdate();
                                    
                    // 결과 출력
                    if (result > 0) {
                        System.out.println("ROW INSERTED");
                    } else {
                        System.out.println("ROW NOT INSERTED");
                    }




                    producer.send (new ProducerRecord<> ("modifiedBusStation" ,record.value()));
                    }
                    
                }
            }
        }


   
    }
    
       /**
      * 경위도 정보로 주소를 불러오는 메소드
      * @throws UnsupportedEncodingException 
      */
      public static ArrayList<String> coordToAddr(String longitude, String latitude){
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x="+longitude+"&y="+latitude;
        ArrayList<String> addr = new ArrayList<String>();
        try{
           String jsondata = getJSONData(url);
           System.out.println("jsondata : "+jsondata);
          addr.addAll(getRegionAddress(jsondata));
          //LOGGER.info(addr);
        }catch(Exception e){
          System.out.println("주소 api 요청 에러");
          e.printStackTrace();
        }
          return addr;
  
      }

    /**
      * REST API로 통신하여 받은 JSON형태의 데이터를 String으로 받아오는 메소드
      */
	private static String getJSONData(String apiUrl) throws Exception {
    	HttpURLConnection conn = null;
    	StringBuffer response = new StringBuffer();
    	 
    	//인증키 - KakaoAK하고 한 칸 띄워주셔야해요!
    	String auth = "KakaoAK " + "bcab2e30de8e43130afebdb594c04dd4";

    	//URL 설정
        URL url = new URL(apiUrl);
         
        conn = (HttpURLConnection) url.openConnection();
        
        //Request 형식 설정
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-Requested-With", "curl");
        conn.setRequestProperty("Authorization", auth);

        //request에 JSON data 준비
        conn.setDoOutput(true);
         
        //보내고 결과값 받기
        int responseCode = conn.getResponseCode();
        if (responseCode == 400) {
            System.out.println("400:: 해당 명령을 실행할 수 없음");
        } else if (responseCode == 401) {
            System.out.println("401:: Authorization가 잘못됨");
        } else if (responseCode == 500) {
            System.out.println("500:: 서버 에러, 문의 필요");
        } else { // 성공 후 응답 JSON 데이터받기
        	 
        	 Charset charset = Charset.forName("UTF-8");
             BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
             
             String inputLine;
             while ((inputLine = br.readLine()) != null) {
             	response.append(inputLine); 
             } 
         }
         
         return response.toString();
    }
    
     

private static ArrayList<String> getRegionAddress(String jsonString) {
    ArrayList<String> value = new ArrayList<String>();

    try {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);

        JsonNode metaNode = rootNode.get("meta");
        long size = metaNode.get("total_count").asLong();

        if (size > 0) {
            JsonNode documentsNode = rootNode.get("documents");
            JsonNode subJobj = documentsNode.get(0);
            System.out.println("subJobj : " + subJobj);
            JsonNode subsubJobj = subJobj.get("address");
            value.add(subsubJobj.get("region_1depth_name").asText());
            value.add(subsubJobj.get("region_2depth_name").asText());
            value.add(subsubJobj.get("region_3depth_name").asText());

        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return value;
}
    
}
