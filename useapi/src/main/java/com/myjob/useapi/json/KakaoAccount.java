package com.myjob.useapi.json;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KakaoAccount {
    @SerializedName("profile_nickname_needs_agreement")
    private boolean profileNicknameNeedsAgreement;
    @SerializedName("profile_image_needs_agreement")
    private boolean profileImageNeedsAgreement;

    private Profile profile;
    @SerializedName("has_email")
    private boolean hasEmail;
    @SerializedName("email_needs_agreement")
    private boolean emailNeedsAgreement;
    @SerializedName("is_email_valid")
    private boolean isEmailValid;
    @SerializedName("is_email_verified")
    private boolean isEmailVerified;
    private String email;
    private String nickName;
    private String birthyear;
    private String birthday;
    private String name;

}
