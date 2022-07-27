package edu.uob;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HashMap;

public class Encryption {
    private final BCryptPasswordEncoder encoder;
    private final Pbkdf2PasswordEncoder pbkdf2PasswordEncoder;

    public Encryption() {
        encoder = new BCryptPasswordEncoder();
        pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
    }

    /**
     * To use BCryptPasswordEncoder, you need to add a dependency (spring-boot-starter-security)
     * BCrypt will store the salt inside the hash value itself
     * For instance, the following hash value:
     * $2a$10$ZLhnHxdpHETcxmtEStgpI./Ri1mksgJ9iDP36FmfMdYyVg9g0b2dq
     * Separates three fields by $:
     * The “2a” represents the BCrypt algorithm version
     * The “10” represents the strength of the algorithm
     * The “ZLhnHxdpHETcxmtEStgpI.” part is actually the randomly generated salt
     * The remaining part of the last field is the actual hashed version of the plain text
     * See: https://www.baeldung.com/spring-security-registration-password-encoding-bcrypt
     * */
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    public String getFinalPwd(String pwd) {
        return encoder.encode(pwd);
    }

    /**
     * In the first version of Encryption class,
     * I wrote our own Spring password encoding scheme based on salts and peppers.
     * But after doing some research, I found it might be not safe.
     * You could see the first version here:
     * https://github.com/Group-J-NHS-Doctor-Rota/Doctor-Rota/commit/66c6ab8e744151466ce719b05590c4fc5960ce77
     *
     * In the second version, I used PBKDF2WithHmacSHA1 algorithm to hash password,
     * and used SecureRandom to generate unique salt.
     * Reference:
     * https://stackoverflow.com/questions/18142745/how-do-i-generate-a-salt-in-java-for-salted-hash
     * The second version:
     * https://github.com/Group-J-NHS-Doctor-Rota/Doctor-Rota/commit/a0a5ade4c8e62c92accfed4c38663de2eefd59b1
     *
     * Recently, I found BCryptPasswordEncoder was simple to use,
     * and it seems to be more suitable for spring boot.
     * So I wrote the third version using BCryptPasswordEncoder.
     * I haven't combined this encoder with spring boot as the tutorial told,
     * I just wrote a function which could create a secure password using BCryptPasswordEncoder.
     * */

    // Some people don't advise using pepper?
//    https://stackoverflow.com/questions/71813290/how-do-i-implement-a-pepper-in-spring-mvc
//    https://stackoverflow.com/questions/16891729/best-practices-salting-peppering-passwords

    private String getPepper() {
        return ConnectionTools.getEnvOrSysVariable("PEPPER");
    }

    //https://docs.spring.io/spring-security/site/docs/5.1.4.RELEASE/api/org/springframework/security/crypto/password/Pbkdf2PasswordEncoder.html
    public String hashPassword(String password) {
        String passwordStr = password + getPepper();
        return pbkdf2PasswordEncoder.encode(passwordStr);
    }

    public boolean passwordMatches(String password, String hashedPassword) {
        String passwordStr = password + getPepper();
        return pbkdf2PasswordEncoder.matches(passwordStr, hashedPassword);
    }

    public String getRandomToken() {
        return RandomStringUtils.randomAlphanumeric(20);
    }

}
