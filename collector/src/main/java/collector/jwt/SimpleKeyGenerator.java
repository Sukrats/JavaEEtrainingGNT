package collector.jwt;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.SignatureAlgorithm;

public class SimpleKeyGenerator implements KeyGenerator {
	
    @Override
	public Key generateKey( SignatureAlgorithm algorithm) {
		String keyString = "simplekeygeratedforhsmac256tokens";
		Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length,algorithm.getJcaName());
		return key;
	}

}
