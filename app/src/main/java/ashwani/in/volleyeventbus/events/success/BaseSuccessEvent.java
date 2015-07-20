package ashwani.in.volleyeventbus.events.success;

import ashwani.in.volleyeventbus.utils.Json;

public class BaseSuccessEvent {

    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public <T> T getTypedResponse(Class<T> tClass) throws ClassNotFoundException {
        return Json.deSerialize(response, tClass);
    }
}
