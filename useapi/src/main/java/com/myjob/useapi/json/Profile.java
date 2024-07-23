package com.myjob.useapi.json;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profile {
    private String nickname;
    @SerializedName("thumbnail_image_url")
    private String thumbnailImageUrl;
    @SerializedName("profile_image_url")
    private String profileImageUrl;
    @SerializedName("is_default_image")
    private String isDefaultImage;
    @SerializedName("is_default_nickname")
    private String isDefaultNickname;
  
}
