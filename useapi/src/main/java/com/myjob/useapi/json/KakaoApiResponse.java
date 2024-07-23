package com.myjob.useapi.json;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoApiResponse {
    private String id;
    @SerializedName("connected_at")
    private String connectedAt;
    private Properties properties;
    @SerializedName("kakao_account")
    private KakaoAccount kakaoAccount;
    
}
