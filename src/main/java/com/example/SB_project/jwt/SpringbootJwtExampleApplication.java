package com.example.SB_project.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.Arrays;

@SpringBootApplication
public class SpringbootJwtExampleApplication implements CommandLineRunner {

    @Autowired
    private JwtService jwtService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringbootJwtExampleApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJwtExampleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        User u = new User();
        u.setUserId("bean");
        u.setName("ParkSooah");
        u.setPassword("123456789");
        u.setAuthority(Arrays.asList("USER"));

        LOGGER.debug("oreating jwt...");

        String token = jwtService.createLoginToken(u);
        LOGGER.debug("jwt 생성 : " + token);

        LOGGER.debug("jwt decoding...");
        User user = jwtService.getUser(token);
        LOGGER.debug("디코드 된 jwt : " + user);
    }
}
