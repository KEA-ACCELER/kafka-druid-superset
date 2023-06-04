package stream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class XyAllocator {

    public static void main(String[] args) {
        String longitude = "126.9783882";
        String latitude = "37.5766207111";
        String addr = coordToAddr(longitude, latitude);
        System.out.println(addr);
    }
    
        /**
      * 경위도 정보로 주소를 불러오는 메소드
      * @throws UnsupportedEncodingException 
      */
      public static String coordToAddr(String longitude, String latitude){
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x="+longitude+"&y="+latitude;
        String addr = "";
        try{
          addr = getRegionAddress(getJSONData(url));
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
    
 

private static String getRegionAddress(String jsonString) {
    String value = "";

    try {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);

        JsonNode metaNode = rootNode.get("meta");
        System.out.println("metaNode : " + metaNode);
        long size = metaNode.get("total_count").asLong();

        if (size > 0) {
            JsonNode documentsNode = rootNode.get("documents");
            // System.out.println("documentsNode : " + documentsNode);
            JsonNode subJobj = documentsNode.get(0);
            System.out.println("subJobj : " + subJobj);
            JsonNode subsubJobj = subJobj.get("address");

            value = subsubJobj.get("address_name").asText();
            // JsonNode roadAddress = subJobj.get("road_address");

            // if (roadAddress == null) {
            //     JsonNode subsubJobj = subJobj.get("address");
            //     value = subsubJobj.get("address_name").asText();
            // } else {
            //     value = roadAddress.get("address_name").asText();
            // }

            // if (value.equals("") || value == null) {
            //     subJobj = documentsNode.get(1);
            //     subJobj = subJobj.get("address");
            //     value = subJobj.get("address_name").asText();
            }
            System.out.println("value : " + value );
        
    } catch (Exception e) {
        e.printStackTrace();
    }

    return value;
}

}
