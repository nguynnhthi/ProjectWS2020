import java.util.Map;

public class FailedLogMessageSubscriber implements StatementSubscriber {
    @Override
    public String getStatement() {
        String failedLogExpression = "select * from SSHLogMessage (message like \"%Failed%\")";
        return failedLogExpression;
    }
    public void update(SSHLogMessage lm) {
        System.out.print("Failed password log-in occurred at :");
        System.out.println(lm.getTimestamp());
    }
}
