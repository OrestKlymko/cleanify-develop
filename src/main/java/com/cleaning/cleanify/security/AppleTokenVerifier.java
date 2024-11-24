package com.cleaning.cleanify.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

public class AppleTokenVerifier {

	private static final String APPLE_KEYS_URL = "https://appleid.apple.com/auth/keys";

	/**
	 * Verify the Apple token and return its claims.
	 *
	 * @param token The JWT token from Apple.
	 * @return The claims from the token.
	 * @throws Exception If token verification fails.
	 */
	public static Claims verifyAppleToken(String token) throws Exception {
		// Extract the 'kid' from the token header
		String kid = getKidFromTokenHeader(token);

		// Get the corresponding public key from Apple
		PublicKey publicKey = getApplePublicKey(kid);

		// Verify the token using the public key
		return Jwts.parserBuilder()
				.setSigningKey(publicKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	/**
	 * Get the 'kid' from the token header.
	 *
	 * @param token The JWT token.
	 * @return The 'kid' value.
	 * @throws Exception If the token format is invalid.
	 */
	private static String getKidFromTokenHeader(String token) throws Exception {
		String[] parts = token.split("\\.");
		if (parts.length != 3) {
			throw new IllegalArgumentException("Invalid JWT token format");
		}

		String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readTree(headerJson).get("kid").asText();
	}

	/**
	 * Get the Apple public key for the specified 'kid'.
	 *
	 * @param kid The Key ID from the token header.
	 * @return The PublicKey object.
	 * @throws Exception If the public key cannot be found or parsed.
	 */
	private static PublicKey getApplePublicKey(String kid) throws Exception {
		// Fetch public keys from Apple
		URL url = new URL(APPLE_KEYS_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode response = mapper.readTree(connection.getInputStream());
		JsonNode keys = response.get("keys");

		for (JsonNode key : keys) {
			if (key.get("kid").asText().equals(kid)) {
				BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(key.get("n").asText()));
				BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(key.get("e").asText()));

				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				return keyFactory.generatePublic(new RSAPublicKeySpec(modulus, exponent));
			}
		}

		throw new RuntimeException("Public key with kid not found");
	}
}
