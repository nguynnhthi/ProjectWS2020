import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class SSHAlert {
    private final String timestamp;
    private final String message;
    private final long rtTimestamp;
    private final String ipAddress;
    private final int portNumber;

    public String getIpAddress() {
        return ipAddress;
    }
    public int getPortNumber() {
        return portNumber;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public String getMessage() {
        return message;
    }
    public long getRtTimestamp() {
        return rtTimestamp;
    }

    public SSHAlert(long rtTimestamp, String ipAddress, int portNumber) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.rtTimestamp = rtTimestamp;
        long epochTimestamp = this.rtTimestamp / 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = Date.from(Instant.ofEpochMilli(epochTimestamp));
        this.timestamp = sdf.format(date);
        this.message = "ALERT :\t\t" + "Three consecutive unsuccessful log-in " +
                "attempts from " + this.ipAddress + " port " + this.portNumber + " detected at " +
                this.getTimestamp() + "\n";
        System.out.println(this.getMessage());
    }
}
