package edu.gatech.MovieRecommenderFX.model;

import java.util.HashMap;
import java.util.Map;

public class User implements Comparable<User> {
    private String name;
    private String email;
    private String username;
    private String passwordHash;
    private String status;
    private String memberSince;
    private Profile profile;

    public User(String name, String email, String username, String passwordHash, String memberSince) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.memberSince = memberSince;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getStatus() { return status; }
    public String getMemberSince() { return memberSince; }
    public Profile getProfile() { return profile; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setStatus(String status) { this.status = status; }
    public void setMemberSince(String memberSince) { this.memberSince = memberSince; }
    public void setProfile(Profile profile) { this.profile = profile; }

    public Map<String, String> toMap() {
        Map<String, String> toMap = new HashMap<String, String>();

        toMap.put("name", name);
        toMap.put("email", email);
        toMap.put("username", username);
        toMap.put("passwordHash", passwordHash);
        toMap.put("status", status);
        toMap.put("memberSince", memberSince);

        if (profile == null) {
            toMap.put("major", "");
            toMap.put("description", "");
        } else {
            toMap.put("major", profile.getMajor());
            toMap.put("description", profile.getDescription());
        }

        return toMap;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", status='" + status + '\'' +
                ", memberSince='" + memberSince + '\'' +
                ", profile=" + profile +
                '}';
    }

    @Override
    public int compareTo(User o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (name != null ? !name.equals(user.name) : user.name != null)
            return false;
        if (email != null ? !email.equals(user.email) : user.email != null)
            return false;
        if (username != null ? !username.equals(user.username) : user.username != null)
            return false;
        if (passwordHash != null ? !passwordHash.equals(user.passwordHash) : user.passwordHash != null)
            return false;
        if (status != null ? !status.equals(user.status) : user.status != null)
            return false;
        if (memberSince != null ? !memberSince.equals(user.memberSince) : user.memberSince != null)
            return false;
        return profile != null ? profile.equals(user.profile) : user.profile == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (passwordHash != null ? passwordHash.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (memberSince != null ? memberSince.hashCode() : 0);
        result = 31 * result + (profile != null ? profile.hashCode() : 0);
        return result;
    }
}
