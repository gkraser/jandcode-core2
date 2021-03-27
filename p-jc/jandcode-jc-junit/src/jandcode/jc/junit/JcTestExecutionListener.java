package jandcode.jc.junit;

import jandcode.commons.*;
import jandcode.commons.error.*;
import org.junit.platform.engine.*;
import org.junit.platform.engine.support.descriptor.*;
import org.junit.platform.launcher.*;

public class JcTestExecutionListener implements TestExecutionListener {

    private int counter = 0;
    private int counterError = 0;

    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (testIdentifier.getSource().isPresent()) {
            String idn = testIdentifier.getUniqueId();
            TestSource src = testIdentifier.getSource().get();
            if (src instanceof MethodSource) {
                counter++;
                MethodSource ms = (MethodSource) src;
                idn = ms.getClassName() + "#" + ms.getMethodName();
            }
            if (testExecutionResult.getStatus() == TestExecutionResult.Status.FAILED) {
                counterError++;
                ConsoleWrap.println("\r" + "Error in test: " + idn);
                if (testExecutionResult.getThrowable().isPresent()) {

                    ErrorInfo ei = UtError.createErrorInfo(testExecutionResult.getThrowable().get());
                    String s = UtString.indent(ei.getText(), "  | ");
                    ConsoleWrap.println(s);

                    ConsoleWrap.println("");
                }
            } else {
                // Если System.console()==null, значит идет перенаправление в файл
                if (System.console() != null) {
                    ConsoleWrap.print("\r#" + counter);
                    ConsoleWrap.flush();
                }
            }
        }
    }

    public void testPlanExecutionFinished(TestPlan testPlan) {
        ConsoleWrap.print("\r" + counter + " tests completed");
        if (counterError > 0) {
            ConsoleWrap.print(", " + counterError + " tests failed");
        }
        ConsoleWrap.println("");
    }

}
