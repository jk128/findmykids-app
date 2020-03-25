package com.example.findmykidappparents.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private Boolean authenticated;
    private String AccessToken;
    private String RefreshToken;

    public LoggedInUser(String userId, String displayName, Boolean authenticated, String accessToken, String refreshToken) {
        this.userId = userId;
        this.displayName = displayName;
        this.authenticated = authenticated;
        AccessToken = accessToken;
        RefreshToken = refreshToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public String getRefreshToken() {
        return RefreshToken;
    }
}
