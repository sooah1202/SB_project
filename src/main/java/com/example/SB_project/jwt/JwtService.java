package com.example.SB_project.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class JwtService {

    //비밀키
    private static final String ENCRYPT_STRING = "SecretKey_JwtTest_Using_SpringBoot_API";

    //로그
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    //key
    private static final String DATA_KEY = "user";


    //Mapper
    @Autowired
    private ObjectMapper objectMapper;

    //[0] User 정보를 이용해서 - JWT 생성
    public String createLoginToken(User user) {
        //현재 시간
        long curTime = System.currentTimeMillis();

        //[1] Jwts 라이브러리로부터 jwt 생성 - builder 패턴

        return Jwts.builder()

                .setSubject("Test JWT")

                //[2]
                //setHeaderParam 메소드를 통해 JWT 헤더가 지닐 정보들을 담는다.
                //alg 의 경우는 default 값이 SHA256이므로 따로 설정할 필요는 없다.
                //typ 를 셋팅 안해주면 오류 발생한다.
                .setHeaderParam("typ", "JWT")

                //[3] 만료 시간
                .setExpiration(new Date(curTime + 3600000))

                //[4] 발급 시간
                .setIssuedAt(new Date(curTime))

                //[5] Payload 에 Private Claims 를 담기 위해 claim 메소드를 이용한다.
                //private claim으로 VO 객체를 추가할 수 있음
                .claim(DATA_KEY, user)

                //[6] 복호화 할때 사용하는 시그니처 설정.
                //header의 인코딩 값 + payload의 인코딩 값 + 비밀키 = 시그니처
                //signWith api는 해상알고리즘과 비밀키가 필요하다.
                .signWith(SignatureAlgorithm.HS256, this.generateKey())

                //생성
                .compact();
    }

    //비밀키 생성 메소드
    private byte[] generateKey()
    {
        byte[] key = null;
        try
        {
            //ENCRYPT_STRING = pretty
            key = ENCRYPT_STRING.getBytes("UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            LOGGER.error("Making secret Key Error :: " , e);
        }

        System.out.println("비밀 key : " + key);

        return key;
    }

    //JWT 복호화
    public User getUser(String jwt)
    {
        //결과 값 = Claims
        Jws<Claims> claims = null;

        try
        {
            //비밀키를 이용해서 복호화하는 작업
            //jwt가 유효한지, 위변조 되지 않았는지 판단한다.
            //이 비밀키는 서버에 존재해야되고, 유출되어서는 안된다.
            claims = Jwts.parser()
                                    .setSigningKey(this.generateKey())
                                    .parseClaimsJws(jwt);
        }
        catch(Exception e)
        {
            LOGGER.debug(e.getMessage(), e);
            throw new JWTException("decoding failed");
        }

        //반환 타입은 LinkedHashMap이다. 이를 User 타입으로 변환하기 위해 ObjectMapper 사용
        return objectMapper.convertValue(claims.getBody().get(DATA_KEY), User.class);
    }
}
