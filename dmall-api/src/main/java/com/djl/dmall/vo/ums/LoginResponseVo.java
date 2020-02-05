package com.djl.dmall.vo.ums;

import lombok.Data;

@Data
public class LoginResponseVo {

    private String username;

    private String nickname;

    private String phone;

    private Long memberLevelId;

    private String accessToken;//访问令牌


}
