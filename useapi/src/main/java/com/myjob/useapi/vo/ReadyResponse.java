package com.myjob.useapi.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReadyResponse {
    
    private String tid; // 결제고유번호
    private String next_redirect_pc_url; //요청클라이언트 pc 카카오톡으로 결제 요청 메세지 보내기위한
    private String created_at;
}
