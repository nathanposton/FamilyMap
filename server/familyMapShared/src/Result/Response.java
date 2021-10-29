package Result;

public abstract class Response {
    protected String message;
    protected boolean success;

    public Response() {
    }

    //failure
    public Response(Exception ex) {
        this.message = "Error: " + ex.toString();
        this.success = false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
