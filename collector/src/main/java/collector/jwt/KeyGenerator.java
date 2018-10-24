package collector.jwt;

import java.security.Key;

import io.jsonwebtoken.SignatureAlgorithm;


public interface KeyGenerator {

    Key generateKey(SignatureAlgorithm algorithm);
}