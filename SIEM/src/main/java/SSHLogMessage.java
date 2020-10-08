import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class SSHLogMessage {
    private final String timestamp;
    private final String message;
    private final long rtTimestamp;

    public String getTimestamp() {
        return timestamp;
    }
    public String getMessage() {
        return message;
    }
    public long getRtTimestamp() { return rtTimestamp;}

    public SSHLogMessage(String json) {
        JSONObject jo = new JSONObject(json);
        this.message = (String) jo.get("MESSAGE");
        this.rtTimestamp = Long.parseLong((String) jo.get("__REALTIME_TIMESTAMP"));
        long epochTimestamp = this.rtTimestamp / 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = Date.from(Instant.ofEpochMilli(epochTimestamp));
        this.timestamp = sdf.format(date);
    }
}