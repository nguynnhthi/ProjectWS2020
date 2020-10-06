import org.json.*;
public class SSHLogMessage {
    public String transport;
    public String timestamp;
    public String message;
    public SSHLogMessage getEvent() {
        return this;
    }
    public SSHLogMessage(String json) {
        JSONObject jo = new JSONObject(json);
        this.transport = (String) jo.get("_TRANSPORT");
        this.message = (String) jo.get("MESSAGE");
        if (!transport.equals("journal")) {
            this.timestamp = (String) jo.get("SYSLOG_TIMESTAMP");
        }
    }
}
