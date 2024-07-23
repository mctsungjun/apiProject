package com.myjob.useapi.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.myjob.useapi.vo.MemberVo;

@Service
public class NaverApi {
    public String gettoken(String code, String state){
        
       
        String accessToken = "";
        String refreshToken = "";
        String idToken = "";

        // 토큰 발급 요청을 보낼 주소
        String reqUrl = "https://nid.naver.com/oauth2.0/token";

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // POST 요청 본문 작성
            String params = "client_id=De0kPxQwyc06a1EHZFqe" +
                            "&client_secret=7zmWR2aGN_" +
                            "&grant_type=authorization_code" +
                            "&state=" + state +
                            "&code=" + code;

            // 요청 본문 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(params);
            bw.flush();
            bw.close();

            // 응답 코드 확인
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            // 응답 데이터 읽기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            String result = response.toString();
            System.out.println("response body : " + result);

            // Gson 라이브러리를 사용하여 JSON 파싱 토큰 문자열추출
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            if (element.isJsonObject()) {
                JsonObject jsonObject = element.getAsJsonObject();
                if (jsonObject.has("access_token")) {
                    accessToken = jsonObject.get("access_token").getAsString();
                    System.out.println("access_token : " + accessToken);
                }
                if (jsonObject.has("refresh_token")) {
                    refreshToken = jsonObject.get("refresh_token").getAsString();
                    System.out.println("refresh_token : " + refreshToken);
                }
                if (jsonObject.has("id_token")) {
                    idToken = jsonObject.get("id_token").getAsString();
                    System.out.println("id_token: " + idToken);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        // 토큰을 보냄
        return accessToken;
    }

    public MemberVo userInfo(String accessToken) throws IOException {
        MemberVo vo = new MemberVo();

        // 네이버 회원정보 요청
        String reqUrl = "https://openapi.naver.com/v1/nid/me";
        String token = accessToken;
        String header = "Bearer " + token;
        String id = "";
        String nickname = "";
        String name = "";
        String email = "";
        String birthday = "";
        String birthyear = "";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", header);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode: " + responseCode);

            // 입력스트림을 가지고 오고 데이터 읽기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            br.close();

            System.out.println("response body:" + result.toString());

            // JSON 파싱 회원 정보 문자열 추출
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(result.toString());

            if (je.isJsonObject()) {
                JsonObject jo = je.getAsJsonObject();
                if (jo.has("response")) {
                    JsonObject responseObj = jo.getAsJsonObject("response");
                    if (responseObj.has("id")) {
                        id = responseObj.get("id").getAsString();
                    }
                    if (responseObj.has("nickname")) {
                        nickname = responseObj.get("nickname").getAsString();
                    }
                    if (responseObj.has("name")) {
                        name = responseObj.get("name").getAsString();
                    }
                    if (responseObj.has("email")) {
                        email = responseObj.get("email").getAsString();
                    }
                    if (responseObj.has("birthday")){
                        birthday = responseObj.get("birthday").getAsString();
                    }
                    if (responseObj.has("birthyear")){
                        birthyear = responseObj.get("birthyear").
                            getAsString();
                        
                    }
                }
                vo.setBirthday(birthyear+"-"+birthday);
                
                vo.setNaverId(id);
                //vo.setNickname(nickname);
                vo.setName(name);
                vo.setEmail(email);
            }

            //System.out.println("nickname: " + vo.getNickname() + ", name: " + vo.getName() + ", id: " + vo.getId());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("API 요청과 응답 실패", e);
        }

        return vo;
    }
    // 연결끊기

    public String connectionOff(String token){
        String msg="";
        String secret = "7zmWR2aGN_";
        String client_id = "De0kPxQwyc06a1EHZFqe";
        String access_token= token;
        String reqUrl = "https://nid.naver.com/oauth2.0/token?";
        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
              // POST 요청 본문 작성
            String params = "grant_type=delete&client_id=De0kPxQwyc06a1EHZFqe" +"&client_secret=_1X0GuKC9F" +
              "&access_token=" +token+
              "&service_provider=NAVER";

               // 요청 본문 전송
               BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
               bw.write(params);
               bw.flush();
               bw.close();
   
               // 응답 코드 확인
               int responseCode = conn.getResponseCode();
               System.out.println("responseCode : " + responseCode);
   
               // 응답 데이터 읽기
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
               StringBuilder response = new StringBuilder();
               String line;
               while ((line = br.readLine()) != null) {
                   response.append(line);
               }
               br.close();
   
               String result = response.toString();
               System.out.println("response body : " + result);
   
               // Gson 라이브러리를 사용하여 JSON 파싱
               JsonParser parser = new JsonParser();
               JsonElement element = parser.parse(result);
               if (element.isJsonObject()) {
                JsonObject jo = element.getAsJsonObject();
                if (jo.has("result")){
                    msg = jo.get("result").toString();
                } 
        }
        }catch(Exception e){
            e.printStackTrace();

    }
    return msg;
    }

}
