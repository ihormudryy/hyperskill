import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

public class Tests extends StageTest<String> {
    @DynamicTest(order = 1)
    CheckResult simpleRouteTest() {
        TestedProgram main = new TestedProgram();
        main.start("./test/london.json");

        String output = main.execute("/route \"Waterloo & City line\" \"Waterloo\" \"Waterloo & City line\" \"Bank\"");
        String[] sOutput = output.trim().split("\n");

        if (sOutput.length != 2) {
            return CheckResult.wrong("There is an incorrect number of station in the route");
        }

        if (!sOutput[0].trim().equals("Waterloo") || !sOutput[1].trim().equals("Bank")) {
            return CheckResult.wrong("There are incorrect stations in the route");
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 2)
    CheckResult severalLinesRoute() {
        TestedProgram main = new TestedProgram();
        main.start("./test/london.json");

        String output = main.execute("/route \"Victoria line\" \"Victoria\" \"Northern line\" \"Oval\"");
        String[] sOutput = output.toLowerCase().split("\n");

        String[] route = {"Victoria", "Pimlico", "Vauxhall", "Stockwell", "Northern line", "Stockwell", "Oval"};

        if (sOutput.length != route.length) {
            return CheckResult.wrong("There is an incorrect number of station in the route");
        }

        if (assertRoute(sOutput, route)) {
            return CheckResult.wrong("The route is incorrect. Wrong stations were displayed");
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 3)
    CheckResult severalLines() {
        TestedProgram main = new TestedProgram();
        main.start("./test/london.json");

        String output = main.execute("/route \"Victoria line\" \"Green Park\" \"Northern line\" \"Oval\"");
        String[] sOutput = output.toLowerCase().split("\n");

        String[] route = {"Green Park", "Jubilee line", "Green Park", "Westminster", "Waterloo",
            "Northern line", "Waterloo", "Kennington", "Oval"};

        if (sOutput.length != route.length) {
            return CheckResult.wrong("There is an incorrect number of station in the route");
        }

        if (assertRoute(sOutput, route)) {
            return CheckResult.wrong("The route is incorrect. Wrong stations were displayed.");
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 4)
    CheckResult forkTest() {
        TestedProgram main = new TestedProgram();
        main.start("./test/london.json");

        String output = main.execute("/route \"Piccadilly line\" \"Heathrow Terminal 5\" " +
            "\"Piccadilly line\" \"Hounslow West\"");
        String[] sOutput = output.toLowerCase().split("\n");

        String[] route = {"Heathrow Terminal 5", "Heathrow Terminals 1—2—3", "Hatton Cross", "Hounslow West"};

        if (sOutput.length != route.length) {
            return CheckResult.wrong("There is an incorrect number of station in the route");
        }

        if (assertRoute(sOutput, route)) {
            return CheckResult.wrong("The route is incorrect. Wrong stations were displayed.");
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 5)
    CheckResult forkTest2() {
        TestedProgram main = new TestedProgram();
        main.start("./test/london.json");

        String output = main.execute("/route \"Piccadilly line\" \"Hatton Cross\" " +
            "\"Piccadilly line\" \"Heathrow Terminal 4\"");
        String[] sOutput = output.toLowerCase().split("\n");

        String[] route = {"Hatton Cross", "Heathrow Terminal 4"};

        if (sOutput.length != route.length) {
            return CheckResult.wrong("There is an incorrect number of station in the route");
        }

        if (assertRoute(sOutput, route)) {
            return CheckResult.wrong("The route is incorrect. Wrong stations were displayed. The fork was ignored.");
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 6)
    CheckResult simpleTimeTest() {
        TestedProgram main = new TestedProgram();
        main.start("./test/london.json");

        String output = main.execute("/fastest-route \"District line\" \"Richmond\" \"District line\" \"Gunnersbury\"");
        String[] sOutput = output.toLowerCase().split("\n");

        String[] route = {"Richmond", "Kew Gardens", "Gunnersbury", "12"};

        if (sOutput.length != route.length) {
            return CheckResult.wrong("Your program printed an incorrect number of stations in the route. Expected 3 stations and the total time!");
        }

        if (!sOutput[sOutput.length - 1].contains(route[route.length - 1])) {
            return CheckResult.wrong("The program incorrectly calculates the trip duration.");
        }

        if (assertRoute(sOutput, route)) {
            return CheckResult.wrong("The route is incorrect. Wrong stations were displayed.");
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 7)
    CheckResult advancedTimeTest() {
        TestedProgram main = new TestedProgram();
        main.start("./test/london.json");

        String output = main.execute("/fastest-route \"Victoria line\" \"Brixton\" \"Northern line\" \"Old Street\"");
        String[] sOutput = output.toLowerCase().split("\n");

        String[] route = {"Brixton", "Stockwell", "Northern line", "Stockwell", "Oval", "Kennington", "Waterloo",
            "Waterloo & City line", "Waterloo", "Bank",
            "Northern line", "Bank", "Moorgate", "Old Street", "Angel", "47"};

        if (sOutput.length != route.length) {
            return CheckResult.wrong("Your program printed an incorrect number of stations in the route. Expected 9 stations, 3 transitions and the total time!");
        }

        if (!sOutput[sOutput.length - 1].contains(route[route.length - 1])) {
            return CheckResult.wrong("The program incorrectly calculates the trip duration. " +
                "Maybe the wrong way was chosen.");
        }

        if (assertRoute(sOutput, route)) {
            return CheckResult.wrong("The route is incorrect. Wrong stations were displayed. " +
                "Maybe the wrong way was chosen.");
        }

        return CheckResult.correct();
    }

    boolean assertRoute(String[] stations, String[] correctRoute) {
        int index = 0;
        for (String station : stations) {
            if (!station.toLowerCase().trim().contains(correctRoute[index].toLowerCase())) {
                return true;
            }
            index++;
        }
        return false;
    }
}
