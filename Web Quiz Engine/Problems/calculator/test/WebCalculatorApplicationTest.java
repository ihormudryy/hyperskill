import calculator.WebCalculatorApplication;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import java.util.List;

public class WebCalculatorApplicationTest extends SpringTest {

    public WebCalculatorApplicationTest() {
        super(WebCalculatorApplication.class, 8889);
    }

    @DynamicTestingMethod
    DynamicTesting[] dt = new DynamicTesting[] {
            this::testAddNumbers,
            this::testSubtractNumbers,
            this::testMultNumbers,
            this::testUnknownOperation,
    };

    private List<List<Integer>> dataProvider = List.of(
            List.of(20, 5),
            List.of(100, 10),
            List.of(0, 2),
            List.of(-15, 3),
            List.of(-20, -10)
    );

    private CheckResult testAddNumbers() {
        for (var pair : dataProvider) {
            var response = get("/add")
                    .addParam("a", String.valueOf(pair.get(0)))
                    .addParam("b", String.valueOf(pair.get(1)))
                    .send();

            if (response.getStatusCode() != 200) {
                return CheckResult.wrong("GET /add should respond with " +
                        "status code 200, responded: " + response.getStatusCode() + "\n\n" +
                        "Response body:\n" + response.getContent());
            }


            int result;
            try {
                result = Integer.parseInt(response.getContent());
            } catch (NumberFormatException e) {
                return CheckResult.wrong("Your program didn't output a number");
            }

            if (result != pair.get(0) + pair.get(1)) {
                return CheckResult.wrong("Wrong result of adding numbers");
            }
        }

        return CheckResult.correct();
    }

    private CheckResult testSubtractNumbers() {
        for (var pair : dataProvider) {
            var response = get("/subtract")
                    .addParam("a", String.valueOf(pair.get(0)))
                    .addParam("b", String.valueOf(pair.get(1)))
                    .send();

            if (response.getStatusCode() != 200) {
                return CheckResult.wrong("GET /subtract should respond with " +
                        "status code 200, responded: " + response.getStatusCode() + "\n\n" +
                        "Response body:\n" + response.getContent());
            }

            int result;
            try {
                result = Integer.parseInt(response.getContent());
            } catch (NumberFormatException e) {
                return CheckResult.wrong("Your program didn't output a number");
            }

            if (result != pair.get(0) - pair.get(1)) {
                return CheckResult.wrong("Wrong result of subtracting numbers");
            }
        }

        return CheckResult.correct();
    }

    private CheckResult testMultNumbers() {
        for (var pair : dataProvider) {
            var response = get("/mult")
                    .addParam("a", String.valueOf(pair.get(0)))
                    .addParam("b", String.valueOf(pair.get(1)))
                    .send();

            if (response.getStatusCode() != 200) {
                return CheckResult.wrong("GET /mult should respond with " +
                        "status code 200, responded: " + response.getStatusCode() + "\n\n" +
                        "Response body:\n" + response.getContent());
            }

            int result;
            try {
                result = Integer.parseInt(response.getContent());
            } catch (NumberFormatException e) {
                return CheckResult.wrong("Your program didn't output a number");
            }

            if (result != pair.get(0) * pair.get(1)) {
                return CheckResult.wrong("Wrong result of multiplying numbers");
            }
        }

        return CheckResult.correct();
    }

    private CheckResult testUnknownOperation() {
        var operations = List.of("/divide", "/ad", "/q");
        final var expectedResult = "Unknown operation";

        for (var operation : operations) {
            var response = get(operation)
                    .addParam("a", "1")
                    .addParam("b", "2")
                    .send();

            if (response.getStatusCode() != 200) {
                return CheckResult.wrong("GET /divide should respond with " +
                        "status code 200, responded: " + response.getStatusCode() + "\n\n" +
                        "Response body:\n" + response.getContent());
            }

            if (!response.getContent().equalsIgnoreCase(expectedResult)) {
                return CheckResult.wrong("Wrong result of multiplying numbers");
            }
        }

        return CheckResult.correct();
    }
}