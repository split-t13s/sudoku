import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import tests.*;

public class TestRunner {
    public static void main(String[] args) {
        Result gameGridResults = JUnitCore.runClasses(GameGridTest.class);

        for (Failure failure : gameGridResults.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(gameGridResults.wasSuccessful());

        Result gridNumberResults = JUnitCore.runClasses(GridNumberTest.class);

        for (Failure failure : gameGridResults.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(gameGridResults.wasSuccessful());
    }
}
