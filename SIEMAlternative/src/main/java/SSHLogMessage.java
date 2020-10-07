import org.json.JSONObject;

public class SSHLogMessage {
    public String transport;
    public String timestamp;

    public String getTransport() {
        return transport;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public int getIsFailedPassword() {
        return isFailedPassword;
    }

    public String message;
    public int isFailedPassword = 0;

    public SSHLogMessage(String json) {
        JSONObject jo = new JSONObject(json);
        this.transport = (String) jo.get("_TRANSPORT");
        this.message = (String) jo.get("MESSAGE");
        if (!transport.equals("journal")) {
            this.timestamp = (String) jo.get("SYSLOG_TIMESTAMP");
        }
        if (this.message.contains("Failed")) this.isFailedPassword = 1;
    }
}