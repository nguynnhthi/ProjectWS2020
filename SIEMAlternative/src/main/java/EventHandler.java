import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import org.springframework.beans.factory.InitializingBean;
import com.espertech.esper.client.EPServiceProvider;

public class EventHandler implements InitializingBean {
    private EPServiceProvider epService;


    private StatementSubscriber failedLogMessageSubscriber;
    private StatementSubscriber alertSubscriber;

    public void initService() {
        Configuration config = new Configuration();
        config.addEventType(SSHLogMessage.class);
        epService = EPServiceProviderManager.getDefaultProvider(config);
        createFailedLogMessageExpression();
        createAlertExpression();
    }

    private void createAlertExpression() {
        EPStatement alertStatement = epService.getEPAdministrator().createEPL(alertSubscriber.getStatement());
        alertStatement.setSubscriber(alertSubscriber);
    }

    private void createFailedLogMessageExpression() {
        EPStatement failedLogMessageStatement = epService.getEPAdministrator().createEPL(failedLogMessageSubscriber.getStatement());
        failedLogMessageStatement.setSubscriber(failedLogMessageSubscriber);
    }

    public void handle(SSHLogMessage mes) {
        epService.getEPRuntime().sendEvent(mes);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initService();
    }
}
