package edu.gatech.MovieRecommenderFX.model;

public class Profile implements Comparable<Profile> {
    private String major = "CS";
    private String description = "Enter a description.";

    public Profile(String major, String description) {
        this.major = major;
        this.description = description;
    }

    public String getMajor() { return major; }
    public String getDescription() { return description; }

    public void setMajor(String major) { this.major = major; }
    public void setDescription(String desc) { this.description = desc; }

    @Override
    public String toString() {
        return "Profile{" +
                "major='" + major + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int compareTo(Profile o) {
        return this.getMajor().compareTo(o.getMajor());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profile profile = (Profile) o;

        if (major != null ? !major.equals(profile.major) : profile.major != null)
            return false;
        return description != null ? description.equals(profile.description) : profile.description == null;

    }

    @Override
    public int hashCode() {
        int result = major != null ? major.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
