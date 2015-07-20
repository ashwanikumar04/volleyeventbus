package ashwani.in.volleyeventbus.events.error;


import ashwani.in.volleyeventbus.entities.ErrorData;

public class BaseErrorEvent {
    private ErrorData errorData;

    public ErrorData getErrorData() {
        return errorData;
    }

    public void setErrorData(ErrorData errorData) {
        this.errorData = errorData;
    }

}
