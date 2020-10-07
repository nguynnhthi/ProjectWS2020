import java.util.Map;

public class AlertSubscriber implements StatementSubscriber {
    @Override
    public String getStatement() {
        String alertExpression = "select * from SSHLogMessage "
                + "match_recognize ( "
                + "measures A as event1, B as event2, C as event3 "
                + "pattern (A B C) "
                + "define "
                + " A as A.message like '%Failed%',"
                + " B as B.message like '%Failed%',"
                + " C as C.message like '%Failed%')";
        return alertExpression;
    }
    public void update(Map<String, SSHLogMessage> eventMap) {
        SSHLogMessage event1 = (SSHLogMessage) eventMap.get("event1");
        SSHLogMessage event2 = (SSHLogMessage) eventMap.get("event2");
        SSHLogMessage event3 = (SSHLogMessage) eventMap.get("event3");
        System.out.println("ALERT : Three consecutive failed log-in method occurred at :");
        System.out.println(event1.getTimestamp() + " " + event2.getTimestamp() + " " + event3.getTimestamp());
    }
}
