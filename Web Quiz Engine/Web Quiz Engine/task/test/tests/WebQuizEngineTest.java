package tests;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import engine.WebQuizEngine;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.exception.outcomes.UnexpectedError;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import static org.hyperskill.hstest.common.JsonUtils.getJson;
import static org.hyperskill.hstest.common.JsonUtils.getPrettyJson;
import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.any;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.isArray;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.isInteger;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.isObject;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.isString;

public class WebQuizEngineTest extends SpringTest {
    public WebQuizEngineTest() {
        super(WebQuizEngine.class, "../quizdb.mv.db");
    }

    static void checkStatusCode(HttpResponse resp, int status) {
        if (resp.getStatusCode() != status) {
            throw new WrongAnswer(
                resp.getRequest().getMethod() + " " +
                    resp.getRequest().getLocalUri() +
                    " should respond with status code " + status + ", " +
                    "responded: " + resp.getStatusCode() + "\n\n" +
                    "Response body:\n\n" + resp.getContent()
            );
        }
    }

    private static String quiz1 =
        "{\n" +
            "  \"title\": \"The Java Logo\", \n" +
            "  \"text\": \"What is depicted on the Java logo?\",\n" +
            "  \"options\": [\"Robot\",\"Tea leaf\",\"Cup of coffee\",\"Bug\"],\n" +
            "  \"answer\": [2]\n" +
            "}";

    private static String quiz2 =
        "{\n" +
            "  \"title\": \"The Ultimate Question\",\n" +
            "  \"text\": \"What is the answer to the Ultimate Question of Life, the Universe and Everything?\",\n" +
            "  \"options\": [\"Everything goes right\",\"42\",\"2+2=4\",\"11011100\"],\n" +
            "  \"answer\": [1]\n" +
            "}";

    private static String quiz3 =
        "{\n" +
            "  \"title\": \"Math1\",\n" +
            "  \"text\": \"Which of the following is equal to 4?\",\n" +
            "  \"options\": [\"1+3\",\"2+2\",\"8-1\",\"1+5\"],\n" +
            "  \"answer\": [0,1]\n" +
            "}";

    private static String quiz4 =
        "{\n" +
            "  \"title\": \"Math2\",\n" +
            "  \"text\": \"Which of the following is equal to 4?\",\n" +
            "  \"options\": [\"1+1\",\"2+2\",\"8-1\",\"5-1\"],\n" +
            "  \"answer\": [1,3]\n" +
            "}";

    private static String quiz5 =
        "{\n" +
            "  \"title\": \"Math3\",\n" +
            "  \"text\": \"Which of the following is equal to 4?\",\n" +
            "  \"options\": [\"2*3\",\"5*8\",\"8*0\",\"1*5\"],\n" +
            "  \"answer\": []\n" +
            "}";

    private static String quiz6 =
        "{\n" +
            "  \"title\": \"Math4\",\n" +
            "  \"text\": \"Which of the following is equal to 4?\",\n" +
            "  \"options\": [\"2*3\",\"5*8\",\"8*0\",\"1*5\"]\n" +
            "}";

    private static String quiz7 =
        "{\n" +
            "  \"title\": \"Math5\",\n" +
            "  \"text\": \"Which of the following is equal to 4?\",\n" +
            "  \"options\": [\"2^2\",\"2+2\",\"2-2\",\"2*2\"],\n" +
            "  \"answer\": [0,1,3]\n" +
            "}";

    private static String[] quizzes = new String[] {
        quiz1, quiz2, quiz3, quiz4, quiz5, quiz6, quiz7
    };

    private static int[] quizIds = new int[] {
        0, 0, 0, 0, 0, 0, 0
    };

    private static String error400noTitle =
        "{\n" +
            "  \"text\": \"What is the answer to the Ultimate Question of Life, the Universe and Everything?\",\n" +
            "  \"options\": [\"Everything goes right\",\"42\",\"2+2=4\",\"11011100\"]\n" +
            "  \"answer\": [1]\n" +
            "}";

    private static String error400emptyTitle =
        "{\n" +
            "  \"title\": \"\",\n" +
            "  \"text\": \"What is the answer to the Ultimate Question of Life, the Universe and Everything?\",\n" +
            "  \"options\": [\"Everything goes right\",\"42\",\"2+2=4\",\"11011100\"]\n" +
            "  \"answer\": [1]\n" +
            "}";

    private static String error400noText =
        "{\n" +
            "  \"title\": \"123123123\",\n" +
            "  \"options\": [\"Everything goes right\",\"42\",\"2+2=4\",\"11011100\"]\n" +
            "  \"answer\": [1]\n" +
            "}";

    private static String error400emptyText =
        "{\n" +
            "  \"title\": \"The Ultimate Question\",\n" +
            "  \"text\": \"\",\n" +
            "  \"options\": [\"Everything goes right\",\"42\",\"2+2=4\",\"11011100\"]\n" +
            "  \"answer\": [1]\n" +
            "}";

    private static String error400noOptions =
        "{\n" +
            "  \"title\": \"The Ultimate Question\",\n" +
            "  \"text\": \"123123123\",\n" +
            "  \"answer\": [1]\n" +
            "}";

    private static String error400emptyOptions =
        "{\n" +
            "  \"title\": \"The Ultimate Question\",\n" +
            "  \"text\": \"What is the answer to the Ultimate Question of Life, the Universe and Everything?\",\n" +
            "  \"options\": [ ]\n" +
            "  \"answer\": [ ]\n" +
            "}";

    private static String error400oneOption =
        "{\n" +
            "  \"title\": \"The Ultimate Question\",\n" +
            "  \"text\": \"What is the answer to the Ultimate Question of Life, the Universe and Everything?\",\n" +
            "  \"options\": [\"Everything goes right\"]\n" +
            "  \"answer\": [0]\n" +
            "}";


    private static String login1 = "test@google.com";
    private static String pass1 = "qwerty";

    private static String login2 = "user@google.com";
    private static String pass2 = "12345";

    private static HttpRequest auth(HttpRequest req, int user) {
        String login;
        String pass;
        if (user == 1) {
            login = login1;
            pass = pass1;
        } else if (user == 2) {
            login = login2;
            pass = pass2;
        } else {
            login = fakeLogin;
            pass = fakePass;
        }
        return req.basicAuth(login, pass);
    }

    private static String fakeLogin = "qwerty@google.com";
    private static String fakePass = "456534";

    private static String noAtInLogin_login = "google.com";
    private static String noAtInLogin_pass = "asddfggh";

    private static String noDotInLogin_login = "well@googlecom";
    private static String noDotInLogin_pass = "rtyfgcxsfd";

    private static String loginExist_login = "test@google.com";
    private static String loginExist_pass = "rtyfgcxsfd1";

    private static String shortPassword_login = "someuser@gmail.com";
    private static String shortPassword_pass = "1234";

    private static String shortPassword2_login = "someuser2@gmail.com";
    private static String shortPassword2_pass = "123";

    @DynamicTestingMethod
    DynamicTesting[] dt = new DynamicTesting[] {
        // Test login
        () -> testRegister(login1, pass1, 200),
        () -> testRegister(login2, pass2, 200),
        () -> testRegister(noAtInLogin_login, noAtInLogin_pass, 400),
        () -> testRegister(noDotInLogin_login, noDotInLogin_pass, 400),
        () -> testRegister(loginExist_login, loginExist_pass, 400),
        () -> testRegister(shortPassword_login, shortPassword_pass, 400),
        () -> testRegister(shortPassword2_login, shortPassword2_pass, 400),


        // Add 2 quizzes and check their existence
        () -> testAllQuizzes(0, 0,1),
        () -> testAllQuizzes(0, 0,2),

        () -> testCreateQuiz(0, 1),
        () -> testQuizExists(0, 2),
        () -> testQuizNotExists(0, 2, 125),

        () -> testAllQuizzes(1, 0,1),

        () -> testCreateQuiz(1, 2),
        () -> testQuizExists(1, 2),
        () -> testQuizNotExists(1, 2, 125),

        () -> testAllQuizzes(2, 0,2),


        // No auth operations tests
        () -> testAllQuizzesNoAuth(),
        () -> testCreateQuizNoAuth(1),
        () -> testCreateQuizNoAuth(2),
        () -> testSolveQuizNoAuth(quizIds[0], "[0]"),
        () -> testSolveQuizNoAuth(quizIds[1], "[1]"),
        () -> testDeleteQuizzesNoAuth(quizIds[0]),
        () -> testDeleteQuizzesNoAuth(quizIds[1]),
        () -> testCompletedQuizzesNoAuth(),


        // Fake auth operations tests
        () -> testAllQuizzesFakeAuth(),
        () -> testCreateQuizFakeAuth(1),
        () -> testCreateQuizFakeAuth(2),
        () -> testSolveQuizFakeAuth(quizIds[0], "[0]"),
        () -> testSolveQuizFakeAuth(quizIds[1], "[1]"),
        () -> testDeleteQuizzesFakeAuth(quizIds[0]),
        () -> testDeleteQuizzesFakeAuth(quizIds[1]),
        () -> testCompletedQuizzesFakeAuth(),


        // Solve two quizzes
        () -> checkQuizSuccess(quizIds[0], "[0]", false, 1),
        () -> checkQuizSuccess(quizIds[0], "[1]", false, 2),
        () -> checkQuizSuccess(quizIds[0], "[2]", true, 1),
        () -> checkQuizSuccess(quizIds[0], "[3]", false, 2),

        () -> checkQuizSuccess(quizIds[1], "[0]", false, 1),
        () -> checkQuizSuccess(quizIds[1], "[1]", true, 2),
        () -> checkQuizSuccess(quizIds[1], "[2]", false, 1),
        () -> checkQuizSuccess(quizIds[1], "[3]", false, 2),


        // Check completed
        () -> testCompletedQuizzes(1, 1, 0 ,0, quizIds[0]),
        () -> testCompletedQuizzes(1, 2, 0, 0, quizIds[1]),


        // Test database save
        () -> testAllQuizzes(2, 0, 1),
        () -> testAllQuizzes(2, 0,2),
        () -> reloadServer(),
        () -> testAllQuizzes(2, 0,1),
        () -> testAllQuizzes(2, 0,2),
        () -> checkQuizSuccess(quizIds[0], "[2]", true, 2),
        () -> checkQuizSuccess(quizIds[0], "[3]", false, 1),
        () -> checkQuizSuccess(quizIds[1], "[0]", false, 2),
        () -> checkQuizSuccess(quizIds[1], "[1]", true, 1),


        // Check completed
        () -> testCompletedQuizzes(2, 1, 0 ,1, quizIds[0]),
        () -> testCompletedQuizzes(2, 1, 0 ,0, quizIds[1]),

        () -> testCompletedQuizzes(2, 2, 0, 1, quizIds[1]),
        () -> testCompletedQuizzes(2, 2, 0, 0, quizIds[0]),


        // Test wrongly created quizzes
        () -> addIncorrectQuiz(error400noTitle, 1),
        () -> addIncorrectQuiz(error400emptyTitle, 2),
        () -> addIncorrectQuiz(error400noText, 1),
        () -> addIncorrectQuiz(error400emptyText, 2),
        () -> addIncorrectQuiz(error400noOptions, 1),
        () -> addIncorrectQuiz(error400emptyOptions, 2),
        () -> addIncorrectQuiz(error400oneOption, 1),


        // Test multiple answers
        () -> testCreateQuiz(2, 1),
        () -> testQuizExists(2, 1),
        () -> testQuizNotExists(2, 1, 125),
        () -> checkQuizSuccess(quizIds[2], "[]", false, 1),
        () -> checkQuizSuccess(quizIds[2], "[0]", false, 2),
        () -> checkQuizSuccess(quizIds[2], "[1]", false, 1),
        () -> checkQuizSuccess(quizIds[2], "[2]", false, 2),
        () -> checkQuizSuccess(quizIds[2], "[3]", false, 1),
        () -> checkQuizSuccess(quizIds[2], "[0,1]", true, 2),
        () -> checkQuizSuccess(quizIds[2], "[0,2]", false, 1),
        () -> checkQuizSuccess(quizIds[2], "[0,3]", false, 2),
        () -> checkQuizSuccess(quizIds[2], "[1,2]", false, 1),
        () -> checkQuizSuccess(quizIds[2], "[1,3]", false, 2),
        () -> checkQuizSuccess(quizIds[2], "[2,3]", false, 1),
        () -> checkQuizSuccess(quizIds[2], "[0,1,2]", false, 2),
        () -> checkQuizSuccess(quizIds[2], "[0,1,3]", false, 1),
        () -> checkQuizSuccess(quizIds[2], "[1,2,3]", false, 2),
        () -> checkQuizSuccess(quizIds[2], "[0,1,2,3]", false, 1),

        () -> testCreateQuiz(3, 1),
        () -> testQuizExists(3, 1),
        () -> testQuizNotExists(3, 1, 125),
        () -> checkQuizSuccess(quizIds[3], "[]", false, 1),
        () -> checkQuizSuccess(quizIds[3], "[0]", false, 2),
        () -> checkQuizSuccess(quizIds[3], "[1]", false, 1),
        () -> checkQuizSuccess(quizIds[3], "[2]", false, 2),
        () -> checkQuizSuccess(quizIds[3], "[3]", false, 1),
        () -> checkQuizSuccess(quizIds[3], "[0,1]", false, 2),
        () -> checkQuizSuccess(quizIds[3], "[0,2]", false, 1),
        () -> checkQuizSuccess(quizIds[3], "[0,3]", false, 2),
        () -> checkQuizSuccess(quizIds[3], "[1,2]", false, 1),
        () -> checkQuizSuccess(quizIds[3], "[1,3]", true, 2),
        () -> checkQuizSuccess(quizIds[3], "[2,3]", false, 1),
        () -> checkQuizSuccess(quizIds[3], "[0,1,2]", false, 2),
        () -> checkQuizSuccess(quizIds[3], "[0,1,3]", false, 1),
        () -> checkQuizSuccess(quizIds[3], "[1,2,3]", false, 2),
        () -> checkQuizSuccess(quizIds[3], "[0,1,2,3]", false, 1),

        () -> testCreateQuiz(4, 1),
        () -> testQuizExists(4, 1),
        () -> testQuizNotExists(4, 1, 125),
        () -> checkQuizSuccess(quizIds[4], "[]", true, 1),
        () -> checkQuizSuccess(quizIds[4], "[0]", false, 2),
        () -> checkQuizSuccess(quizIds[4], "[1]", false, 1),
        () -> checkQuizSuccess(quizIds[4], "[2]", false, 2),
        () -> checkQuizSuccess(quizIds[4], "[3]", false, 1),
        () -> checkQuizSuccess(quizIds[4], "[0,1]", false, 2),
        () -> checkQuizSuccess(quizIds[4], "[0,2]", false, 1),
        () -> checkQuizSuccess(quizIds[4], "[0,3]", false, 2),
        () -> checkQuizSuccess(quizIds[4], "[1,2]", false, 1),
        () -> checkQuizSuccess(quizIds[4], "[1,3]", false, 2),
        () -> checkQuizSuccess(quizIds[4], "[2,3]", false, 1),
        () -> checkQuizSuccess(quizIds[4], "[0,1,2]", false, 1),
        () -> checkQuizSuccess(quizIds[4], "[0,1,3]", false, 2),
        () -> checkQuizSuccess(quizIds[4], "[1,2,3]", false, 1),
        () -> checkQuizSuccess(quizIds[4], "[0,1,2,3]", false, 2),

        () -> testCreateQuiz(5, 1),
        () -> testQuizExists(5, 1),
        () -> testQuizNotExists(5, 1, 125),
        () -> checkQuizSuccess(quizIds[5], "[]", true, 1),
        () -> checkQuizSuccess(quizIds[5], "[0]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[1]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[2]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[3]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[0,1]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[0,2]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[0,3]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[1,2]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[1,3]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[2,3]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[0,1,2]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[0,1,3]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[1,2,3]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[0,1,2,3]", false, 1),

        () -> testCreateQuiz(6, 1),
        () -> testQuizExists(6, 1),
        () -> testQuizNotExists(6, 1, 125),
        () -> checkQuizSuccess(quizIds[6], "[]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[0]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[1]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[2]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[3]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[0,1]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[0,2]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[0,3]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[1,2]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[1,3]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[2,3]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[0,1,2]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[0,1,3]", true, 2),
        () -> checkQuizSuccess(quizIds[6], "[1,2,3]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[0,1,2,3]", false, 2),

        () -> testAllQuizzes(7, 0,2),
        () -> reloadServer(),
        () -> testAllQuizzes(7, 0,2),
        () -> checkQuizSuccess(quizIds[5], "[]", true, 1),
        () -> checkQuizSuccess(quizIds[5], "[0]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[0,1,2]", false, 1),
        () -> checkQuizSuccess(quizIds[6], "[0,1,3]", true, 2),


        // Check completed
        () -> testCompletedQuizzes(5, 1, 0 ,4, quizIds[0]),
        () -> testCompletedQuizzes(5, 1, 0 ,3, quizIds[1]),
        () -> testCompletedQuizzes(5, 1, 0 ,2, quizIds[4]),
        () -> testCompletedQuizzes(5, 1, 0 ,1, quizIds[5]),
        () -> testCompletedQuizzes(5, 1, 0 ,0, quizIds[5]),

        () -> testCompletedQuizzes(6, 2, 0, 5, quizIds[1]),
        () -> testCompletedQuizzes(6, 2, 0, 4, quizIds[0]),
        () -> testCompletedQuizzes(6, 2, 0, 3, quizIds[2]),
        () -> testCompletedQuizzes(6, 2, 0, 2, quizIds[3]),
        () -> testCompletedQuizzes(6, 2, 0, 1, quizIds[6]),
        () -> testCompletedQuizzes(6, 2, 0, 0, quizIds[6]),


        // Test pagination completed quizzes
        () -> checkQuizSuccess(quizIds[4], "[]", true, 1),
        () -> checkQuizSuccess(quizIds[4], "[]", true, 1),
        () -> checkQuizSuccess(quizIds[4], "[]", true, 1),
        () -> checkQuizSuccess(quizIds[4], "[]", true, 1),
        () -> checkQuizSuccess(quizIds[4], "[]", true, 1),
        () -> checkQuizSuccess(quizIds[6], "[0,1,3]", true, 1),

        () -> checkQuizSuccess(quizIds[3], "[1,3]", true, 2),
        () -> checkQuizSuccess(quizIds[3], "[1,3]", true, 2),
        () -> checkQuizSuccess(quizIds[3], "[1,3]", true, 2),
        () -> checkQuizSuccess(quizIds[3], "[1,3]", true, 2),
        () -> checkQuizSuccess(quizIds[3], "[1,3]", true, 2),
        () -> checkQuizSuccess(quizIds[5], "[]", true, 2),

        () -> testCompletedQuizzes(10, 1, 0 ,0, quizIds[6]),
        () -> testCompletedQuizzes(10, 1, 0 ,1, quizIds[4]),
        () -> testCompletedQuizzes(10, 1, 0 ,2, quizIds[4]),
        () -> testCompletedQuizzes(10, 1, 0 ,3, quizIds[4]),
        () -> testCompletedQuizzes(10, 1, 0 ,4, quizIds[4]),
        () -> testCompletedQuizzes(10, 1, 0 ,5, quizIds[4]),
        () -> testCompletedQuizzes(1, 1, 1 ,0, quizIds[0]),

        () -> testCompletedQuizzes(10, 2, 0 ,0, quizIds[5]),
        () -> testCompletedQuizzes(10, 2, 0 ,1, quizIds[3]),
        () -> testCompletedQuizzes(10, 2, 0 ,2, quizIds[3]),
        () -> testCompletedQuizzes(10, 2, 0 ,3, quizIds[3]),
        () -> testCompletedQuizzes(10, 2, 0 ,4, quizIds[3]),
        () -> testCompletedQuizzes(10, 2, 0 ,5, quizIds[3]),
        () -> testCompletedQuizzes(2, 2, 1 ,1, quizIds[1]),
        () -> testCompletedQuizzes(2, 2, 1 ,0, quizIds[0]),


        // Test delete
        () -> testDelete(quizIds[0], 1, 204),
        () -> testDelete(quizIds[0], 1, 404),
        () -> testQuizNotExists(0, 1, 0),
        () -> testQuizNotExists(0, 2, 0),
        () -> testQuizExists(1, 1),
        () -> testQuizExists(1, 2),

        () -> testDelete(quizIds[1], 1, 403),
        () -> testDelete(quizIds[1], 1, 403),
        () -> testDelete(quizIds[1], 2, 204),
        () -> testDelete(quizIds[1], 2, 404),
        () -> testDelete(quizIds[1], 1, 404),
        () -> testQuizNotExists(0, 1, 0),
        () -> testQuizNotExists(0, 2, 0),
        () -> testQuizNotExists(1, 1, 0),
        () -> testQuizNotExists(1, 2, 0),

        () -> testAllQuizzes(5, 0,1),
        () -> reloadServer(),
        () -> testAllQuizzes(5, 0,2),
        () -> testQuizNotExists(0, 1, 0),
        () -> testQuizNotExists(0, 2, 0),
        () -> testQuizNotExists(1, 1, 0),
        () -> testQuizNotExists(1, 2, 0),


        // Test pagination all quizzes
        () -> testCreateQuiz(6, 1),
        () -> testCreateQuiz(6, 2),
        () -> testCreateQuiz(6, 1),
        () -> testCreateQuiz(6, 1),
        () -> testCreateQuiz(6, 1),
        () -> testCreateQuiz(6, 2),
        () -> testCreateQuiz(6, 1),
        () -> testCreateQuiz(6, 2),
        () -> testCreateQuiz(6, 1),
        () -> testCreateQuiz(6, 2),
        () -> testAllQuizzes(10, 0,1),
        () -> testAllQuizzes(10, 0,2),
        () -> testAllQuizzes(5, 1,1),
        () -> testAllQuizzes(5, 1,2),
    };

    private CheckResult testRegister(String login, String password, int status) {
        JsonObject json = new JsonObject();
        json.addProperty("email", login);
        json.addProperty("password", password);

        String url = "/api/register";
        HttpRequest req = post(url, getPrettyJson(json));
        HttpResponse resp = req.send();

        checkStatusCode(resp, status);
        return CheckResult.correct();
    }

    private CheckResult testCreateQuizNoAuth(int quizNum) {
        String url = "/api/quizzes";
        HttpRequest req = post(url, quizzes[quizNum]);
        HttpResponse resp = req.send();
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testCreateQuizFakeAuth(int quizNum) {
        String url = "/api/quizzes";
        HttpRequest req = post(url, quizzes[quizNum]);
        HttpResponse resp = auth(req, 3).send();
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testSolveQuizNoAuth(int quizNum, String answerSent) {
        String url = "/api/quizzes/" + quizNum + "/solve";
        HttpRequest req = post(url, "{" + " \"answer\" : " + answerSent + "}");
        HttpResponse resp = req.send();
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testSolveQuizFakeAuth(int quizNum, String answerSent) {
        String url = "/api/quizzes/" + quizNum + "/solve";
        HttpRequest req = post(url, "{" + " \"answer\" : " + answerSent + "}");
        HttpResponse resp = auth(req, 3).send();
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testAllQuizzesNoAuth() {
        String url = "/api/quizzes";
        HttpResponse resp = get(url).send();
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testAllQuizzesFakeAuth() {
        String url = "/api/quizzes";
        HttpResponse resp = auth(get(url), 3).send();
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testDeleteQuizzesNoAuth(int quizNum) {
        String url = "/api/quizzes/" + quizNum;
        HttpRequest req = delete(url);
        HttpResponse resp = req.send();
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testDeleteQuizzesFakeAuth(int quizNum) {
        String url = "/api/quizzes/" + quizNum;
        HttpRequest req = delete(url);
        HttpResponse resp = auth(req, 3).send();
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testCompletedQuizzesNoAuth() {
        String url = "/api/quizzes/completed";
        HttpRequest req = get(url).addParam("page", "0");
        HttpResponse resp = req.send();
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testCompletedQuizzesFakeAuth() {
        String url = "/api/quizzes/completed";
        HttpRequest req = get(url).addParam("page", "0");
        HttpResponse resp = auth(req, 3).send();
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testCreateQuiz(int quizNum, int user) {
        String url = "/api/quizzes";
        HttpRequest req = post(url, quizzes[quizNum]);
        HttpResponse resp = auth(req, user).send();
        checkStatusCode(resp, 200);

        expect(resp.getContent()).asJson().check(
            isObject()
                .value("id", isInteger(i -> {
                    quizIds[quizNum] = i;
                    return true;
                }))
                .anyOtherValues()
        );

        return CheckResult.correct();
    }

    private CheckResult testQuizExists(int quizNum, int user) {
        int quizId = quizIds[quizNum];
        String quiz = quizzes[quizNum];

        String url = "/api/quizzes/" + quizId;

        HttpResponse resp = auth(get(url), user).send();
        checkStatusCode(resp, 200);

        JsonObject rightQuiz = getJson(quiz).getAsJsonObject();
        rightQuiz.remove("answer");
        rightQuiz.addProperty("id", quizId);

        expect(getPrettyJson(rightQuiz)).asJson().check(
            isObject()
                .value("id", quizId)
                .value("title", isString())
                .value("text", isString())
                .value("options", isArray(any()))
        );

        JsonElement json = resp.getJson();

        CheckResult wrongResponse = CheckResult.wrong(
            "The quiz sent to the program looked like this:\n" +
                getPrettyJson(rightQuiz) + "\n\n" +
                "But the received quiz looks like that:\n" +
                getPrettyJson(json)
        );

        if (!json.isJsonObject()) {
            return wrongResponse;
        }

        JsonObject obj = json.getAsJsonObject();

        if (!rightQuiz.equals(obj)) {
            return wrongResponse;
        }

        return CheckResult.correct();
    }

    private CheckResult testQuizNotExists(int quizNum, int user, int shift) {
        int quizId = quizIds[quizNum];

        String url = "/api/quizzes/" + (quizId + shift);
        HttpResponse resp = auth(get(url), user).send();
        checkStatusCode(resp, 404);

        return CheckResult.correct();
    }

    private CheckResult testAllQuizzes(int count, int page, int user) {
        String url = "/api/quizzes";
        HttpRequest req = get(url).addParam("page", "" + page);
        HttpResponse resp = auth(req, user).send();
        checkStatusCode(resp, 200);

        expect(resp.getContent()).asJson().check(
            isObject()
                .value("content", isArray(count, isObject()
                    .value("id", isInteger())
                    .value("title", isString())
                    .value("text", isString())
                    .value("options", isArray(any()))
                ))
                .anyOtherValues()
        );

        return CheckResult.correct();
    }

    private CheckResult testCompletedQuizzes(int count, int user, int page, int indexForCheckingQuizId, int quizNum) {
        String url = "/api/quizzes/completed";
        HttpRequest req = get(url).addParam("page", "" + page);
        HttpResponse resp = auth(req, user).send();
        checkStatusCode(resp, 200);

        expect(resp.getContent()).asJson().check(
            isObject()
                .value("content",
                    isArray(count,
                        isObject()
                            .value("id", isInteger())
                            .value("completedAt", isString()))

                        .item(indexForCheckingQuizId,
                            isObject()
                                .value("id", quizNum)
                                .value("completedAt", isString()))
                )
                .anyOtherValues()
        );

        return CheckResult.correct();
    }

    private CheckResult checkQuizSuccess(int quizNum, String answerSent, boolean shouldResponse, int user) {
        String url = "/api/quizzes/" + quizNum + "/solve";

        HttpRequest req = post(url, "{" + " \"answer\" : " + answerSent + "}");
        HttpResponse resp = auth(req, user).send();
        checkStatusCode(resp, 200);

        expect(resp.getContent()).asJson().check(
            isObject()
                .value("success", shouldResponse)
                .value("feedback", isString())
        );

        return CheckResult.correct();
    }

    private CheckResult addIncorrectQuiz(String quiz, int user) {
        String url = "/api/quizzes";
        HttpRequest req = post(url, quiz);
        HttpResponse resp = auth(req, user).send();
        checkStatusCode(resp, 400);
        return CheckResult.correct();
    }

    private CheckResult testDelete(int quizNum, int user, int status) {
        String url = "/api/quizzes/" + quizNum;
        HttpRequest req = delete(url);
        HttpResponse resp = auth(req, user).send();
        checkStatusCode(resp, status);
        return CheckResult.correct();
    }

    private CheckResult reloadServer() {
        try {
            reloadSpring();
        } catch (Exception ex) {
            throw new UnexpectedError(ex.getMessage(), ex);
        }
        return CheckResult.correct();
    }
}
