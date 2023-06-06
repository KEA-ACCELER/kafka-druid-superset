/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package stream;


import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KGroupedTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.kstream.WindowedSerdes;
import org.apache.kafka.streams.state.KeyValueStore;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;


public class App {
  
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-station");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19094,localhost:29094,localhost:39094"); // 부트스트랩 서버는 카프카 클러스터에 접속하기 위한 초기 접점입니다.
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass()); // 키의 기본 직렬화/역직렬화 클래스를 설정
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass()); // 값의 기본 직렬화/역직렬화 클래스를 설정

        final Serializer<JsonNode> jsonNodeSerializer = new JsonSerializer();
        final Deserializer<JsonNode> jsonNodeDeserializer = new JsonDeserializer();
        final Serde<JsonNode> jsonNodeSerde = Serdes.serdeFrom(jsonNodeSerializer,jsonNodeDeserializer);

       // Create a StreamsBuilder object
        StreamsBuilder builder = new StreamsBuilder();


        // Create a KStream from the seoulRide topic
        KStream<String, JsonNode> rawRideStream = builder.stream("dbserver1.seoulDB.seoulRide", Consumed.with(Serdes.String(), jsonNodeSerde));

        //pick schema and payload from the stream
        //MNTN_TYP_CD VARCHAR(20), 교통수단 타입 코드
        // MNTN_TYP_NM VARCHAR(20), 교통수단 타입 명
        KStream<String, JsonNode> modifiedStream = rawRideStream.mapValues(value -> {
            System.out.println("MNTM_TYP_NM : "+value.get("payload").get("after").get("MNTN_TYP_CD").toString());
            
            
            ObjectNode modifiedValue= new ObjectNode(new ObjectMapper().getNodeFactory());
            modifiedValue.put("MNTN_TYP_NM", value.get("payload").get("after").get("MNTN_TYP_NM").toString());
            modifiedValue.put("MNTN_TYP_CD", value.get("payload").get("after").get("MNTN_TYP_CD").toString());
       
            
            return modifiedValue;
        });
        modifiedStream.to("busType", Produced.with(Serdes.String(), jsonNodeSerde));
/*
 *   BUS_ROUTE_NO VARCHAR(20), 노선 번호
    BUS_ROUTE_NM VARCHAR(60), 노선 이름 ex, 101(하계동~용산구청)
 */
        KStream<String, JsonNode> busRouteStream = rawRideStream.mapValues(value -> {
            System.out.println("BUS_ROUTE_NM : "+value.get("payload").get("after").get("BUS_ROUTE_NM").toString());
            
            
            ObjectNode modifiedValue= new ObjectNode(new ObjectMapper().getNodeFactory());
            modifiedValue.put("BUS_ROUTE_NO", value.get("payload").get("after").get("BUS_ROUTE_NO").toString());
            modifiedValue.put("BUS_ROUTE_NM", value.get("payload").get("after").get("BUS_ROUTE_NM").toString());
            return modifiedValue;
        });
        busRouteStream.to("busRoute", Produced.with(Serdes.String(), jsonNodeSerde));

 
        KStream<String, JsonNode> plusRide = rawRideStream.mapValues(value -> {
            Integer SUM = value.get("payload").get("after").get("MIDNIGHT_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("ONE_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("TWO_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("THREE_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("FOUR_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("FIVE_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("SIX_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("SEVEN_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("EIGHT_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("NINE_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("TEN_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("ELEVEN_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("TWELVE_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("THIRTEEN_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("FOURTEEN_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("FIFTEEN_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("SIXTEEN_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("SEVENTEEN_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("EIGHTEEN_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("NINETEEN_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("TWENTY_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("TWENTY_ONE_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("TWENTY_TWO_RIDE_NUM").asInt();
            SUM += value.get("payload").get("after").get("TWENTY_THREE_RIDE_NUM").asInt();

            Integer SUM2 = value.get("payload").get("after").get("MIDNIGHT_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("ONE_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("TWO_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("THREE_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("FOUR_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("FIVE_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("SIX_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("SEVEN_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("EIGHT_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("NINE_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("TEN_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("ELEVEN_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("TWELVE_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("THIRTEEN_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("FOURTEEN_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("FIFTEEN_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("SIXTEEN_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("SEVENTEEN_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("EIGHTEEN_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("NINETEEN_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("TWENTY_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("TWENTY_ONE_ALIGHT_NUM").asInt();
            SUM2 += value.get("payload").get("after").get("TWENTY_TWO_ALIGHT_NUM").asInt(); 
            SUM2 += value.get("payload").get("after").get("TWENTY_THREE_ALIGHT_NUM").asInt();



            System.out.println("SUM : " + SUM);
            
            ObjectNode node = (ObjectNode) value.get("payload").get("after");

            node.put("SUM_OF_RIDE", SUM);
            node.put("SUM_OF_ALIGHT", SUM2);
            node.remove("BUS_ROUTE_NO");

            node.remove("STND_BSST_ID");
            node.remove("BSST_STA_NM");
            
            node.remove("MNTN_TYP_NM");
            node.remove("WORK_DT");


            return node;
       
            

        });
        
        plusRide.to("test", Produced.with(Serdes.String(), jsonNodeSerde));



     
       



        
        final Topology topology = builder.build();
        System.out.println(topology.describe());

        final KafkaStreams streams = new KafkaStreams(topology, props);
        final CountDownLatch latch = new CountDownLatch(1);// 이걸 통해서 만약 메인 스레드는 계속 대기하고 Ctrl+C가 입력되면 종료

        // Ctrl+C를 처리하기 위한 핸들러 추가
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
                System.out.println("topology terminated");
            }
        });

        try {
            streams.start();
            System.out.println("topology started");
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }


     

    
    }


