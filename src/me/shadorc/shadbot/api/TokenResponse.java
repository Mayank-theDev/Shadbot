package me.shadorc.shadbot.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {

	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("expires_in")
	private int expiresIn;

	public String getAccessToken() {
		return this.accessToken;
	}

	public int getExpiresIn() {
		return this.expiresIn;
	}

	@Override
	public String toString() {
		return String.format("TokenResponse [accessToken=%s, expiresIn=%s]", this.accessToken, this.expiresIn);
	}

}