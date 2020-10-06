import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompileException;
import com.espertech.esper.compiler.client.EPCompiler;
import com.espertech.esper.compiler.internal.util.EPCompilerImpl;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class Demo {
    public static void main(String[] args) {
        EPCompiler epc = new EPCompilerImpl();
        Configuration configuration = new Configuration();
        configuration.getCommon().addEventType(SSHLogMessage.class);
        EPRuntime runtime = EPRuntimeProvider.getDefaultRuntime(configuration);
//        CompilerArguments arg = new CompilerArguments(configuration);
//        EPCompiled epCompiled;
//        String expression = "select * from SSHLogMessage.win:time(1 hour)";
//        try {
//            epCompiled = epc.compile(expression, arg);
//        }
//        catch (EPCompileException ex) {
//            throw new RuntimeException(ex);
//        }
        // MyListener listener = new MyListener();
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "journalctl -u ssh.service -o json");
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // System.out.println(line);
                SSHLogMessage lm = new SSHLogMessage(line);
                System.out.println(lm.timestamp + "\t" + lm.transport + "\t" + lm.message);
                runtime.getEventService().sendEventBean(lm, "SSHLogMessage");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
