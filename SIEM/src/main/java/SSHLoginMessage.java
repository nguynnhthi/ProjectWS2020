import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class SSHLoginMessage {
    public String ipAddress;
    public String timestamp;
    public String message;
    public long rtTimestamp;
    public int portNumber;
    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public long getRtTimestamp() {
        return rtTimestamp;
    }
    public String getIpAddress() {
        return ipAddress;
    }
    public int getPortNumber() {
        return portNumber;
    }
    public SSHLoginMessage(String message, long rtTimestamp) {
        this.message = message;
        this.rtTimestamp = rtTimestamp;
        long epochTimestamp = this.rtTimestamp / 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = Date.from(Instant.ofEpochMilli(epochTimestamp));
        this.timestamp = sdf.format(date);
        String[] splitMessage = this.message.split(" ");
        for (int i = 0; i < splitMessage.length; i++) {
            if (splitMessage[i].equals("from")) {
                this.ipAddress = splitMessage[i + 1];
            }
            else if (splitMessage[i].equals("port")) {
                this.portNumber = Integer.valueOf(splitMessage[i + 1]);
                break;
            }
        }
        if (splitMessage[0].equals("Accepted")) {
            System.out.println("INFO :\t\tSuccessful authentication attempt from " + this.getIpAddress() +
                " port " + this.getPortNumber() + " at " + this.getTimestamp() + "\n");
        }
        else System.out.println("WARNING :\tFailed authentication attempt from " + this.getIpAddress() +
                " port " + this.getPortNumber() + " at " + this.getTimestamp() + "\n");
    }
}
