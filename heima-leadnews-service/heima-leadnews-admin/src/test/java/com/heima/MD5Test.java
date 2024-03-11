package com.heima;

import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

public class MD5Test {

    public static void main(String[] args) {
        String psw = "123456";
        String salt = "123abc";

        String dbpsw = DigestUtils.md5DigestAsHex((psw + salt).getBytes());

        System.out.println(dbpsw);
    }

}
