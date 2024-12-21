package hlm.excel.util.v2test.complex;

public class Project {
    private String projectName;

    private int duration; // Duration in months

    // Constructor
    public Project(String projectName, int duration) {
        this.projectName = projectName;
        this.duration = duration;
    }

    // Getters
    public String getProjectName() {
        return projectName;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectName='" + projectName + '\'' +
                ", duration=" + duration +
                '}';
    }
}

