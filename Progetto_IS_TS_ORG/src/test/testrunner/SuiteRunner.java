package test.testrunner;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import test.TestSuite;

public class SuiteRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestSuite.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println("The result of test is:");
        if(result.wasSuccessful())
            System.out.println("Successful");
        else
            System.out.println("Error");
    }
}
