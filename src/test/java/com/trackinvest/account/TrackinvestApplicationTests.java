package com.trackinvest.account;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "COGNITO_JWK_SET_URI=https://cognito-idp.fake.amazonaws.com/.well-known/jwks.json",
        "COGNITO_ISSUER_URI=https://cognito-idp.fake.amazonaws.com",
        "COGNITO_CLIENT_ID=fake-client-id",
        "COGNITO_CLIENT_SECRET=fake-client-secret",
        "COGNITO_URI=https://fake-cognito.com"
})
class TrackinvestApplicationTests {
	@Test
	void contextLoads() {
	}
}
