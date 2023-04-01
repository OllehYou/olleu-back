package com.example.olleuback.domain.user.dto;

import lombok.Data;


public class LoginUserDto {

    @Data
    public static class Request {
        private String email;
        private String password;
    }
    @Data
    public static class Response {
        private Long id;

        public static Response ofCreate(Long id) {
            Response res = new Response();
            res.id = id;
            return res;
        }
    }
}
