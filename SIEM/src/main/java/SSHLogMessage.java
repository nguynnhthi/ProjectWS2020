import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class SSHLogMessage {
    public String transport;
    public String timestamp;
    public String message;
    public long rtTimestamp;

    public String getTransport() {
        return transport;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public String getMessage() {
        return message;
    }
    public long getRtTimestamp() { return rtTimestamp;}

    public SSHLogMessage() {
    }
    public SSHLogMessage(String json) {
        JSONObject jo = new JSONObject(json);
        this.transport = (String) jo.get("_TRANSPORT");
        this.message = (String) jo.get("MESSAGE");
        this.rtTimestamp = Long.valueOf((String) jo.get("__REALTIME_TIMESTAMP"));
        long epochTimestamp = this.rtTimestamp / 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = Date.from(Instant.ofEpochMilli(epochTimestamp));
        this.timestamp = sdf.format(date);
    }
}