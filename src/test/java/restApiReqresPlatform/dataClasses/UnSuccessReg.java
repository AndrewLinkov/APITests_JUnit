package restApiReqresPlatform.data;

public class UnSuccessReg {
    private String error= "error";

    public UnSuccessReg(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
