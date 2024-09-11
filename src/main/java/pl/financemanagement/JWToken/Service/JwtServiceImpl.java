package pl.financemanagement.JWToken.Service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService{

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Override
    public String generateUserToken(String login, String role) throws JOSEException {
        JWSAlgorithm algorithm = JWSAlgorithm.HS256;
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + 3600 * 1000);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(login)
                .issueTime(now)
                .claim("role", role)
                .expirationTime(expirationTime)
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(algorithm), claimsSet);
        JWSSigner signer = new MACSigner(secretKey);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }
}
