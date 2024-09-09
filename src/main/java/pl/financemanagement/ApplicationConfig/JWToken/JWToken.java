package pl.financemanagement.ApplicationConfig.JWToken;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.financemanagement.User.UserModel.UserCredentialsRequest;

import java.util.Date;

import static pl.financemanagement.ApplicationConfig.JWToken.JWTokenStatus.ERROR;
import static pl.financemanagement.ApplicationConfig.JWToken.JWTokenStatus.SUCCESS;

@RestController
@CrossOrigin(value = "*")
public class JWToken {

    private static final String USER = "user";
    @Value("${jwt.secret.key}")
    private String secretKey;


    @PostMapping("/auth")
    public ResponseEntity<JWTokenResponse> getToken(@RequestBody UserCredentialsRequest userCredentialsRequest)
            throws JOSEException {
        if (USER.equals(userCredentialsRequest.login())) {
            return ResponseEntity.ok().body(new JWTokenResponse(
                    createToken(userCredentialsRequest.login()), "", SUCCESS.getStatus()));
        }
        return ResponseEntity.status(403).body(new JWTokenResponse("",
                "Incorrect login information sent", ERROR.getStatus()));
    }

    private String createToken(String login) throws JOSEException {
        JWSAlgorithm algorithm = JWSAlgorithm.HS256;
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + 3600 * 1000);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(login)
                .issueTime(now)
                .expirationTime(expirationTime)
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(algorithm), claimsSet);
        JWSSigner signer = new MACSigner(secretKey);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    @Bean
    public WebMvcConfigurer corsConfiguration() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedMethods(CorsConfiguration.ALL)
                        .allowedHeaders(CorsConfiguration.ALL)
                        .allowedOriginPatterns(CorsConfiguration.ALL);
            }
        };
    }


}
