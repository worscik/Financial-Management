package pl.financemanagement.JWToken.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private final String secretKey;

    public JwtServiceImpl(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = secretKey;
    }

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
