import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class SSHAlert {
    public String timestamp;
    public String message;
    public long rtTimestamp;
    public String ipAddress;
    public int portNumber;
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
        this.message = "ALERT :\t\t" + this.getTimestamp() + "\tThree consecutive unsuccessful log-in " +
                "attempts from " + this.ipAddress + " port " + this.portNumber + " detected at " +
                this.timestamp + "\n";
        System.out.println(this.getMessage());
    }
}
