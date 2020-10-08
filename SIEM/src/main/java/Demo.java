import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompileException;
import com.espertech.esper.compiler.client.EPCompiler;
import com.espertech.esper.compiler.internal.util.EPCompilerImpl;
import com.espertech.esper.runtime.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Demo {
    public static void main(String[] args) throws IOException, NullPointerException {
        EPCompiler epc = new EPCompilerImpl();
        Configuration configuration = new Configuration();
        configuration.getCommon().addEventType(SSHLogMessage.class);
        configuration.getCommon().addEventType(SSHLoginMessage.class);
        EPRuntime runtime = EPRuntimeProvider.getDefaultRuntime(configuration);
        CompilerArguments arg = new CompilerArguments(configuration);
        EPCompiled epCompiled;
        EPDeployment deployment;

        try {
            epCompiled = epc.compile("@name('failed-login-statement') select * from SSHLogMessage (message " +
                    "like \"%password for%\")", arg);
        } catch (EPCompileException ex) {
            System.out.println("EPCompileException");
            throw new RuntimeException(ex);
        }
        try {
            deployment = runtime.getDeploymentService().deploy(epCompiled);
        } catch (EPDeployException ex) {
            System.out.println("EPDeployException");
            throw new RuntimeException(ex);
        }
        EPStatement statement = runtime.getDeploymentService().getStatement(deployment.getDeploymentId(),
                "failed-login-statement");
        statement.addListener((newData, oldData, statement1, runtime1) -> {
            String SYSLOG_TIMESTAMP = (String) newData[0].get("timestamp");
            String MESSAGE = (String) newData[0].get("message");
            long RTTIMESTAMP = (Long) newData[0].get("rtTimestamp");
            SSHLoginMessage lim = new SSHLoginMessage(MESSAGE, RTTIMESTAMP);
            // System.out.println(String.format("TIMESTAMP: %s, MESSAGE: %s", SYSLOG_TIMESTAMP, MESSAGE));
            runtime.getEventService().sendEventBean(lim, "SSHLoginMessage");
        });
        String alertExpression = "@name('alert-statement') select * from SSHLoginMessage "
                + "match_recognize ( "
                + "measures A as event1, B as event2, C as event3 "
                + "pattern (A B C) "
                + "define "
                + " A as A.message like '%Failed%',"
                + " B as B.message like '%Failed%' and B.ipAddress like A.ipAddress and B.portNumber = A.portNumber,"
                + " C as C.message like '%Failed%' and C.ipAddress like A.ipAddress and C.portNumber = A.portNumber)";
        try {
            epCompiled = epc.compile(alertExpression, arg);
        } catch (EPCompileException ex2) {
            System.out.println("EPCompileException");
            throw new RuntimeException(ex2);
        }
        try {
            deployment = runtime.getDeploymentService().deploy(epCompiled);
        } catch (EPDeployException ex2) {
            System.out.println("EPDeployException");
            throw new RuntimeException(ex2);
        }
        EPStatement statement2 = runtime.getDeploymentService().getStatement(deployment.getDeploymentId(),
                "alert-statement");
        statement2.addListener((newData, oldData, statement1, runtime2) -> {
            long RTTIMESTAMP = (Long) newData[0].get("event3.rtTimestamp");
            String IPADDRESS = (String) newData[0].get("event3.ipAddress");
            int PORTNUMBER = (Integer) newData[0].get("event3.portNumber");
            SSHAlert sa = new SSHAlert(RTTIMESTAMP, IPADDRESS, PORTNUMBER);
            runtime.getEventService().sendEventBean(sa, "SSHAlert");
        });
        int numberOfLines = 0;
        while (true) {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", "journalctl -u ssh.service -o json");
            try {
                Process process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String lineOfJSON = null;
                ArrayList<String> jsonList = new ArrayList<>();
                while((lineOfJSON = reader.readLine()) != null) jsonList.add(lineOfJSON);
                int updatedNumberOfLines = jsonList.size();
                for (int i = numberOfLines; i < updatedNumberOfLines; i++) {
                    SSHLogMessage lm = new SSHLogMessage(jsonList.get(i));
                    runtime.getEventService().sendEventBean(lm, "SSHLogMessage");
                }
                numberOfLines = updatedNumberOfLines;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}