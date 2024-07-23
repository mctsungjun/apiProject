package com.myjob.useapi.json;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Properties {
    private String nickname;
    @SerializedName("profile_image")
    private String profileImage;
    @SerializedName("thumbnail_image")
    private String thumbnailImage;
    
   
}
