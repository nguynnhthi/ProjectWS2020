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
}
