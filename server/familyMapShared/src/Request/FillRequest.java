package Request;

/**
 * Fill API Requests
 */
public class FillRequest {
    /**
     * stores request body username parameter
     */
    private String username;
    /**
     * stores request body generations parameter
     */
    private int generations;

    public FillRequest(String username, int generations) {
        this.username = username;
        this.generations = generations;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }

}
