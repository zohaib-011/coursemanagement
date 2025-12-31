package com.example.coursemanagement.model;

public class Course {

    private String id;
    private String courseName;
    private String courseCode;
    private int creditHours;
    private String courseType; // "Lab" or "Theory"
    private long timestamp;

    // No-argument constructor required by Firebase
    public Course() {
        this("", "", "", 0, "", System.currentTimeMillis());
    }

    public Course(String id,
                  String courseName,
                  String courseCode,
                  int creditHours,
                  String courseType,
                  long timestamp) {
        this.id = id != null ? id : "";
        this.courseName = courseName != null ? courseName : "";
        this.courseCode = courseCode != null ? courseCode : "";
        this.creditHours = creditHours;
        this.courseType = courseType != null ? courseType : "";
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (creditHours != course.creditHours) return false;
        if (timestamp != course.timestamp) return false;
        if (id != null ? !id.equals(course.id) : course.id != null) return false;
        if (courseName != null ? !courseName.equals(course.courseName) : course.courseName != null)
            return false;
        if (courseCode != null ? !courseCode.equals(course.courseCode) : course.courseCode != null)
            return false;
        return courseType != null ? courseType.equals(course.courseType) : course.courseType == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (courseName != null ? courseName.hashCode() : 0);
        result = 31 * result + (courseCode != null ? courseCode.hashCode() : 0);
        result = 31 * result + creditHours;
        result = 31 * result + (courseType != null ? courseType.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", creditHours=" + creditHours +
                ", courseType='" + courseType + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
