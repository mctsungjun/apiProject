package com.myjob.useapi.json;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoAccessToken {
    
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("id_token")
    private String idToken;
    @SerializedName("expires_in")
    private String expiresIn;
    private String scope;
    @SerializedName("refresh_token_expires_in")
    private String refreshTokenExpiresIn;

}
