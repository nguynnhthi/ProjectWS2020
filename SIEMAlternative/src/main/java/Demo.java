import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompileException;
import com.espertech.esper.compiler.client.EPCompiler;
import com.espertech.esper.compiler.internal.util.EPCompilerImpl;
import com.espertech.esper.runtime.client.*;
import com.espertech.esper.client.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Demo {
    public static void main(String args[]) {
        // EPCompiler epc = new EPCompilerImpl();

        EPServiceProvider epService;
        com.espertech.esper.client.EPStatement failedLogMessageStatement;
        com.espertech.esper.client.EPStatement alertStatement;


        StatementSubscriber failedLogMessageSubscriber = new FailedLogMessageSubscriber();
        StatementSubscriber alertSubscriber = new AlertSubscriber();

        com.espertech.esper.client.Configuration config = new Configuration();
        config.addEventType(SSHLogMessage.class);
        epService = EPServiceProviderManager.getDefaultProvider(config);
        failedLogMessageStatement = epService.getEPAdministrator().createEPL(failedLogMessageSubscriber.getStatement());
        failedLogMessageStatement.setSubscriber(failedLogMessageSubscriber);
        // alertStatement = epService.getEPAdministrator().createEPL(alertSubscriber.getStatement());
        // alertStatement.setSubscriber(alertSubscriber);

        /*EPRuntime runtime = EPRuntimeProvider.getDefaultRuntime(config);
        CompilerArguments arguments = new CompilerArguments(config);
        EPCompiled epCompiled = null;
        try {
            // epCompiled = epc.compile("@name('failed-statement') select timestamp, message from SSHLogMessage", arguments);
            epCompiled = epc.compile("@name('my-statement') select timestamp, message from SSHLogMessage", arguments);
        }
        catch (EPCompileException e) {
            System.out.println("EPCompileException");
            e.printStackTrace();
        }
        EPDeployment deployment = null;
        try {
            deployment = runtime.getDeploymentService().deploy(epCompiled);
        }
        catch (EPDeployException ex) {
            System.out.println("EPDeployException");
            ex.printStackTrace();
        }
        EPStatement statement = runtime.getDeploymentService().getStatement(deployment.getDeploymentId(),
                "my-statement");
        statement.addListener((newData, oldData, statement1, runtime1) -> {
            String timestamp = (String) newData[0].get("TIMESTAMP");
            String message = (String) newData[0].get("MESSAGE");
            System.out.println("TIMESTAMP: " + timestamp + ", MESSAGE: " + message);
        });*/
        try {
            File file = new File("D:\\My Learning\\Year 4\\Project\\JSONTest.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                SSHLogMessage lm = new SSHLogMessage(line);
                // System.out.println(lm.timestamp + "\t" + lm.transport + "\t" + lm.message);
                epService.getEPRuntime().sendEvent(lm);
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
