package com.myjob.useapi.searchtrendapi;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
public class SearchTrend {
    
    String clientId = "37lRUDlawCXgMjDNSpco";
    String clientSecret = "7HTQBDAoVD";

    public String apiUrl = "https://openapi.naver.com/v1/datalab/search";

    String startDate = "";
    String endDate = "";
    String timeUnit = "";
    String groupName = "";
    String[] keywords = {};
    String groupName2 = "";
    String[] keywords2 = {};
    String device = "";
    String[] ages = {};
    String gender = "";
    Map<String, String> requestHeaders = getHeaders();
    String requestBody = getBody(startDate, endDate, timeUnit, groupName, keywords, groupName2, keywords2, device, ages, gender);
    //헤더정보
    public Map<String, String> getHeaders(){
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        requestHeaders.put("Content-Type", "application/json; charset=utf-8");
        return requestHeaders;

    }

    //바디정보
    public String getBody(String startDate, String endDate, String timeUnit, String groupName, String[] keywords, String groupName2, String[] keywords2, String device, String[] ages, String gender ){
        StringBuilder requestBody = new StringBuilder();
        requestBody.append("{")
            .append("\"startDate\":\"").append(startDate).append("\",")
            .append("\"endDate\":\"").append(endDate).append("\",")
            .append("\"timeUnit\":\"").append(timeUnit).append("\",")
            .append("\"keywordGroups\":[{\"groupName\":\"").append(groupName).append("\",")
            .append("\"keywords\":").append(arrayToJson(keywords)).append("},")
            .append("{\"groupName\":\"").append(groupName2).append("\",")
            .append("\"keywords\":").append(arrayToJson(keywords2)).append("}],")
            .append("\"device\":\"").append(device).append("\",")
            .append("\"ages\":").append(arrayToJson(ages)).append(",")
            .append("\"gender\":\"").append(gender).append("\"")
            .append("}");
        System.out.println(requestBody.toString());
    return requestBody.toString();
    }

    //배열을 json형식으로
    public String arrayToJson(String[] array){
        StringBuilder json = new StringBuilder();
        json.append("[");
        for(int i =0; i< array.length; i++){
            json.append("\"").append(array[i]).append("\"");
            if(i< array.length-1){
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    //접속
    public String post(String apiUrl, Map<String, String> requestHeaders, String requestBody ){
        HttpURLConnection conn = connect(apiUrl);

        try{
            conn.setRequestMethod("POST");
            for(Map.Entry<String, String> header : requestHeaders.entrySet()){
                conn.setRequestProperty(header.getKey(), header.getValue());
            }

            conn.setDoOutput(true);
            try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())){
                wr.write(requestBody.getBytes(StandardCharsets.UTF_8));
                wr.flush();
            }
            int responseCode = conn.getResponseCode();
            System.out.println(responseCode);
            if(responseCode == HttpURLConnection.HTTP_OK){
                return readBody(conn.getInputStream());
            } else {  // 에러 응답
                return readBody(conn.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            conn.disconnect(); // Connection을 재활용할 필요가 없는 프로세스일 경우
        }
        }
    

    public HttpURLConnection connect(String apiUrl){
        try{
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        }catch(MalformedURLException e){
            throw new RuntimeException("API URL이 잘못되었습니다.:" + apiUrl, e);
        }catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    public String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body, StandardCharsets.UTF_8);
        try (BufferedReader linReader = new BufferedReader(streamReader)){
            StringBuilder responseBody = new StringBuilder();

            String line;
            while((line=linReader.readLine())!=null){
                responseBody.append(line);
            }
            return responseBody.toString();
        }catch (IOException e){
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.",e);
        }
    }

}   

