package com.example.findmykidappparents.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String userId;
    private String displayName;
    private Boolean authenticated;
    private String AccessToken;
    private String RefreshToken;

    public LoggedInUserView(String userId, String displayName, Boolean authenticated, String accessToken, String refreshToken) {
        this.userId = userId;
        this.displayName = displayName;
        this.authenticated = authenticated;
        AccessToken = accessToken;
        RefreshToken = refreshToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public String getRefreshToken() {
        return RefreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        RefreshToken = refreshToken;
    }
}