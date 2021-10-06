import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import java.util.Arrays;

import static org.hyperskill.hstest.common.Utils.sleep;

public class Tests extends StageTest<String> {
    private final int executePause = 50;

    @DynamicTestingMethod
    CheckResult test1_Stage5() {
        final TestedProgram server = new TestedProgram("chat.server");
        final TestedProgram client1 = new TestedProgram("chat.client");
        final TestedProgram client2 = new TestedProgram("chat.client");
        final TestedProgram client3 = new TestedProgram("chat.client");
        client1.setReturnOutputAfterExecution(false);
        client2.setReturnOutputAfterExecution(false);
        client3.setReturnOutputAfterExecution(false);

        server.startInBackground();
        sleep(executePause);
        client1.start();
        sleep(executePause);
        client2.start();
        sleep(executePause);
        client3.start();
        sleep(executePause);
        client3.getOutput();
        client2.getOutput();

        final String client1Start = client1.getOutput().trim();
        if (!client1Start.contains("Server: authorize or register")) {
            return CheckResult.wrong(
                    "Can't find \"Server: authorize or register\" message");
        }

        client1.execute("bla bla bla");
        sleep(executePause);
        final String client1Answer1 = client1.getOutput().trim();
        if (!client1Answer1.contains("Server: you are not in the chat!")) {
            return CheckResult.wrong(
                    "Can't find  \"Server: you are not in the chat!\" " +
                    "message after trying to send a message before /auth or /register commands");
        }

        client1.execute("/auth asdasd asdasd");
        sleep(executePause);
        final String client1Answer2 = client1.getOutput().trim();
        if (!client1Answer2.contains("Server: incorrect login!")) {
            return CheckResult.wrong(
                    "Can't find \"Server: incorrect login!\" " +
                    "message after inputting wrong login and password");
        }

        client2.execute("/registration first pass");
        sleep(executePause);
        final String client2Answer1 = client2.getOutput().trim();
        if (!client2Answer1.contains("Server: the password is too short!")) {
            return CheckResult.wrong(
                    "Can't find \"Server: the password is too short!\" " +
                    "message after trying to register with short password");
        }

        client2.execute("/registration first 12345678");
        sleep(executePause);
        final String client2Answer2 = client2.getOutput().trim();
        if (!client2Answer2.contains("Server: you are registered successfully!")) {
            return CheckResult.wrong(
                    "Can't find \"Server: you are registered successfully!\" " +
                    "message after successful authentication");
        }

        client2.execute("before choosing an addressee");
        sleep(executePause);
        final String client2Answer3 = client2.getOutput().trim();
        if (!client2Answer3.contains("Server: use /list command to choose a user to text!")) {
            return CheckResult.wrong(
                    "Can't find \"Server: use /list command to choose a user to text!\" " +
                    "message before choosing an addressee");
        }

        client2.execute("/list");
        sleep(executePause);
        final String client2Answer4 = client2.getOutput().trim();
        if (!client2Answer4.contains("Server: no one online")) {
            return CheckResult.wrong(
                    "Can't find \"Server: no one online\" message if there are no users online");
        }

        client2.execute("/exit");
        sleep(executePause);
        if (!client2.isFinished()) {
            return CheckResult.wrong("Client should be shut down, after the \"/exit\" command");
        }

        client1.execute("/auth first paasf");
        sleep(executePause);
        final String client1Answer3 = client1.getOutput().trim();
        if (!client1Answer3.contains("Server: incorrect password!")) {
            return CheckResult.wrong(
                    "Can't find \"Server: incorrect password!\" " +
                    "message after inputting a wrong password");
        }

        client1.execute("/auth first 12345678");
        sleep(executePause);
        final String client1Answer4 = client1.getOutput().trim();
        if (!client1Answer4.contains("Server: you are authorized successfully!")) {
            return CheckResult.wrong("Can't find \"Server: you are authorized " +
                    "successfully!\" message after successful authentication");
        }

        client3.execute("/registration first 12345678");
        sleep(executePause);
        final String client3Answer1 = client3.getOutput().trim();
        if (!client3Answer1.contains("Server: this login is already taken! Choose another one.")) {
            return CheckResult.wrong(
                    "Can't find \"Server: this login is already taken! Choose another one.\" " +
                        "message from a client that is trying to register with a login which is already taken");
        }

        client3.execute("/registration second 12345678");
        sleep(executePause);
        final String client3Answer2 = client3.getOutput().trim();
        if (!client3Answer2.contains("Server: you are registered successfully!")) {
            return CheckResult.wrong("Can't get the \"Server: you are registered " +
                    "successfully!\" message after successful authentication");
        }

        client1.execute("/list");
        sleep(executePause);
        final String client1Answer5 = client1.getOutput().trim();
        if (client1Answer5.contains("first")) {
            return CheckResult.wrong("The list of online users contains the client's name, but shouldn't");
        }

        if (!client1Answer5.contains("Server: online: second")) {
            return CheckResult.wrong("A client receive a wrong list of online users. " +
                    "Should be \"Server: online: second\"");
        }

        client1.execute("/chat blabla");
        sleep(executePause);
        final String client1Answer6 = client1.getOutput().trim();
        if (!client1Answer6.contains("Server: the user is not online!")) {
            return CheckResult.wrong("Can't find \"Server: the user is not online!\"" +
                    "after trying to chat using wrong username");
        }

        client1.execute("blabla");
        sleep(executePause);
        final String client1Answer7 = client1.getOutput().trim();
        if (!client1Answer7.contains("Server: use /list command to choose a user to text!")) {
            return CheckResult.wrong("Can't find \"Server: use /list command to " +
                    "choose a user to text!\" after trying to chat without choosing a user");
        }

        client1.execute("/chat second");
        sleep(executePause);

        client1.execute("test");
        sleep(executePause);
        final String client3Answer3 = client3.getOutput().trim();
        if (!client3Answer3.isEmpty()) {
            return CheckResult.wrong("Client \"second\" received a message \"" + client3Answer3 + "\" " +
                    "but shouldn't receive anything");
        }

        client3.execute("/chat first");
        sleep(executePause);
        final String client3Answer4 = client3.getOutput().trim();
        if (!client3Answer4.contains("(new) first: test")) {
            return CheckResult.wrong("Client \"second\" didn't receive " +
                    "a message in a format \"(new) userName: message\". Should be \"(new) first: test\".");
        }

        for (String s : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9")) {
            client1.execute(s);
            sleep(executePause);
        }
        client3.execute("10");
        sleep(executePause);

        final String client1Answer8 = client1.getOutput().trim();
        if (client1Answer8.contains("new")) {
            return CheckResult.wrong("Client \"first\" should not mark " +
                    "it's messages with \"new\" since he's chatting with the \"second\" right now.");
        }
        if (!client1Answer8.equals(
                "first: test\n" +
                "first: 1\n" +
                "first: 2\n" +
                "first: 3\n" +
                "first: 4\n" +
                "first: 5\n" +
                "first: 6\n" +
                "first: 7\n" +
                "first: 8\n" +
                "first: 9\n" +
                "second: 10")) {
            return CheckResult.wrong("Client \"first\" output wrong messages.");
        }

        final String client3Answer5 = client3.getOutput().trim();
        if (client3Answer5.contains("new")) {
            return CheckResult.wrong("Client \"second\" should not mark " +
                    "it's messages with \"new\" since he's chatting with the \"first\" right now.");
        }
        if (!client3Answer5.equals(
                "first: 1\n" +
                "first: 2\n" +
                "first: 3\n" +
                "first: 4\n" +
                "first: 5\n" +
                "first: 6\n" +
                "first: 7\n" +
                "first: 8\n" +
                "first: 9\n" +
                "second: 10")) {
            return CheckResult.wrong("Client \"second\" output wrong messages.");
        }

        return CheckResult.correct();
    }

    @DynamicTestingMethod
    CheckResult test2_lastMessagesExtendedEdition() {
        final TestedProgram server2 = new TestedProgram("chat.server");
        final TestedProgram client1 = new TestedProgram("chat.client");
        final TestedProgram client2 = new TestedProgram("chat.client");
        final TestedProgram admin = new TestedProgram("chat.client");
        client1.setReturnOutputAfterExecution(false);
        client2.setReturnOutputAfterExecution(false);
        admin.setReturnOutputAfterExecution(false);
        server2.startInBackground();
        sleep(executePause);
        client1.start();
        sleep(executePause);
        client2.start();
        sleep(executePause);
        admin.start();
        sleep(executePause);
        client1.getOutput();
        client2.getOutput();
        admin.getOutput();

        client1.execute("/auth first 12345678");
        sleep(executePause);
        final String clientAnswer1 = client1.getOutput().trim();
        if (!clientAnswer1.contains("Server: you are authorized successfully!")) {
            return CheckResult.wrong("A registered client can't be authenticated after rebooting a server");
        }

        client2.execute("/auth second 12345678");
        sleep(executePause);
        client2.execute("/chat first");
        client2.execute("unread");
        sleep(executePause);

        client1.execute("/chat second");
        sleep(executePause);
        final String client1Answer2 = client1.getOutput().trim();
        if (!client1Answer2.equals(
                "first: 1\n" +
                "first: 2\n" +
                "first: 3\n" +
                "first: 4\n" +
                "first: 5\n" +
                "first: 6\n" +
                "first: 7\n" +
                "first: 8\n" +
                "first: 9\n" +
                "second: 10\n" +
                "(new) second: unread")) {
            return CheckResult.wrong("A client should receive ALL unread messages marked with \"new\" and " +
                    "10 additional last messages from the end of the conversation");
        }

        return CheckResult.correct();
    }

    @DynamicTestingMethod
    CheckResult test3_kickAndGrant() {
        final TestedProgram server = new TestedProgram("chat.server");
        final TestedProgram client1 = new TestedProgram("chat.client");
        final TestedProgram client2 = new TestedProgram("chat.client");
        final TestedProgram client3 = new TestedProgram("chat.client");
        final TestedProgram admin = new TestedProgram("chat.client");
        client1.setReturnOutputAfterExecution(false);
        client2.setReturnOutputAfterExecution(false);
        client3.setReturnOutputAfterExecution(false);
        admin.setReturnOutputAfterExecution(false);
        server.startInBackground();
        sleep(executePause);
        client1.start();
        sleep(executePause);
        client2.start();
        sleep(executePause);
        client3.start();
        sleep(executePause);
        admin.start();
        sleep(executePause);
        client1.getOutput();
        client2.getOutput();
        client3.getOutput();
        admin.getOutput();

        client1.execute("/auth first 12345678");
        sleep(executePause);
        final String client1Answer1 = client1.getOutput().trim();
        if (!client1Answer1.contains("Server: you are authorized successfully!")) {
            return CheckResult.wrong("A registered client can't be authenticated after" +
                    " rebooting a server");
        }

        client2.execute("/auth second 12345678");
        sleep(executePause);
        client2.getOutput();

        admin.execute("/auth admin 12345678");
        sleep(executePause);
        final String adminAnswer1 = admin.getOutput().trim();
        if (!adminAnswer1.contains("Server: you are authorized successfully!")) {
            return CheckResult.wrong("Admin can't log in! Use login \"admin\" and password \"12345678\"");
        }

        admin.execute("/kick admin");
        sleep(executePause);
        final String adminAnswer2 = admin.getOutput().trim();
        if (!adminAnswer2.contains("Server: you can't kick yourself!")) {
            return CheckResult.wrong("Can't find \"Server: you can't kick " +
                    "yourself!\" message after admin's attempt to kick himself");
        }

        admin.execute("/kick first");
        sleep(executePause);
        final String adminAnswer3 = admin.getOutput().trim();
        if (!adminAnswer3.contains("Server: first was kicked!")) {
            return CheckResult.wrong("Can't find \"Server: USER was " +
                    "kicked!\" message after a user \"first\" was kicked by admin");
        }

        admin.execute("/list");
        sleep(executePause);
        final String adminAnswer4 = admin.getOutput().trim();
        if (!adminAnswer4.contains("Server: online: second")) {
            return CheckResult.wrong("Admin received a wrong list of users. " +
                    "Should be \"Server: online: second\"");
        }

        final String client1Answer2 = client1.getOutput().trim();
        if (!client1Answer2.contains("Server: you have been kicked out of the server!")) {
            return CheckResult.wrong("Can't get the message \"Server: you have been " +
                    "kicked out of the server!\" from the \"first\" client perspective");
        }

        client1.execute("I'm not authed");
        sleep(executePause);
        final String client1Answer3 = client1.getOutput().trim();
        if (!client1Answer3.contains("Server: you are not in the chat!")) {
            return CheckResult.wrong("Can't find the message \"Server: you are not in " +
                    "the chat!\" after trying to send a message after being kicked");
        }

        client1.execute("/auth first 12345678");
        sleep(executePause);
        final String client1Answer4 = client1.getOutput().trim();
        if (!client1Answer4.contains("Server: you are banned!")) {
            return CheckResult.wrong("Can't find message \"Server: you are banned!\"");
        }

        client3.execute("/registration first2 12345678");
        sleep(executePause);
        client3.getOutput();

        admin.execute("/grant first2");
        sleep(executePause);
        final String adminAnswer5 = admin.getOutput().trim();
        if (!adminAnswer5.contains("Server: first2 is the new moderator!")) {
            return CheckResult.wrong("Can't find message " +
                    "\"Server: first2 is the new moderator!\" after admin's command \"/grant first2\"");
        }

        final String client3Answer1 = client3.getOutput().trim();
        if (!client3Answer1.contains("Server: you are the new moderator now!")) {
            return CheckResult.wrong("Can't find message \"Server: you are the new " +
                    "moderator now!\" after a user become a moderator");
        }

        admin.execute("/grant first2");
        sleep(executePause);
        final String adminAnswer6 = admin.getOutput().trim();
        if (!adminAnswer6.contains("Server: this user is already a moderator!")) {
            return CheckResult.wrong("Can't find message \"Server: this user is " +
                    "already a moderator!\" after doing the \"/grant\" command on a moderator");
        }

        client3.execute("/kick second");
        sleep(executePause);
        final String client3Answer3 = client3.getOutput().trim();
        if (!client3Answer3.contains("Server: second was kicked!")) {
            return CheckResult.wrong("Can't find message \"Server: second was " +
                    "kicked!\" message after a user was kicked by a moderator");
        }

        final String client2Answer1 = client2.getOutput().trim();
        if (!client2Answer1.contains("Server: you have been kicked out of the server!")) {
            return CheckResult.wrong("Can't find message \"Server: you have been " +
                    "kicked out of the server!\" after a successful kick of a user");
        }

        admin.execute("/list");
        sleep(executePause);
        final String adminAnswer8 = admin.getOutput().trim();
        if (!adminAnswer8.contains("Server: online: first2")) {
            return CheckResult.wrong("Admin received a wrong list of users. Should be \"Server: online: first2\"");
        }

        admin.execute("/kick first2");
        sleep(executePause);
        final String adminAnswer9 = admin.getOutput().trim();
        if (!adminAnswer9.contains("Server: first2 was kicked!")) {
            return CheckResult.wrong("It looks like an admin can't kick a moderator, but should");
        }

        final String client3Answer6 = client3.getOutput().trim();
        if (!client3Answer6.contains("Server: you have been kicked out of the server!")) {
            return CheckResult.wrong("It is look like an admin can't kick a moderator");
        }

        admin.execute("/list");
        sleep(executePause);
        final String adminAnswer10 = admin.getOutput().trim();
        if (!adminAnswer10.contains("Server: no one online")) {
            return CheckResult.wrong("Admin received a wrong list of users. Should be \"Server: no one online\"");
        }

        return CheckResult.correct();
    }

    @DynamicTestingMethod
    CheckResult test4_revoke() {
        final TestedProgram server = new TestedProgram("chat.server");
        final TestedProgram moderator = new TestedProgram("chat.client");
        final TestedProgram client2 = new TestedProgram("chat.client");
        final TestedProgram admin = new TestedProgram("chat.client");
        moderator.setReturnOutputAfterExecution(false);
        client2.setReturnOutputAfterExecution(false);
        admin.setReturnOutputAfterExecution(false);
        server.startInBackground();
        sleep(executePause);
        moderator.start();
        sleep(executePause);
        client2.start();
        sleep(executePause);
        admin.start();
        sleep(executePause);
        moderator.execute("/registration moderator 12345678");
        sleep(executePause);
        client2.execute("/registration 999 12345678");
        sleep(executePause);
        admin.execute("/auth admin 12345678");
        sleep(executePause);
        admin.execute("/grant moderator");
        sleep(executePause);
        moderator.getOutput();
        client2.getOutput();
        admin.getOutput();

        client2.execute("/revoke admin");
        sleep(executePause);
        final String client2Answer1 = client2.getOutput().trim();
        if (!client2Answer1.equals("Server: you are not an admin!")) {
            return CheckResult.wrong("Can't find message \"Server: you are not an admin!\" " +
                    "after using the \"/revoke\" command by someone who is not an admin");
        }

        moderator.execute("/revoke 999");
        sleep(executePause);
        final String moderatorAnswer1 = moderator.getOutput().trim();
        if (!moderatorAnswer1.equals("Server: you are not an admin!")) {
            return CheckResult.wrong("Can't find message \"Server: you are not an admin!\" " +
                    "after using the \"/revoke\" command by someone who is not an admin");
        }

        admin.execute("/revoke moderator");
        sleep(executePause);
        final String adminAnswer1 = admin.getOutput().trim();
        if (!adminAnswer1.equals("Server: moderator is no longer a moderator!")) {
            return CheckResult.wrong("Can't find message \"Server: USER is no longer" +
                    " a moderator!\" after using the \"/revoke\" command by an admin");
        }

        final String moderatorAnswer2 = moderator.getOutput().trim();
        if (!moderatorAnswer2.equals("Server: you are no longer a moderator!")) {
            return CheckResult.wrong("Can't get the message \"Server: you are no longer" +
                    " a moderator\" after using the \"/revoke\" command by an admin");
        }

        moderator.execute("/kick 999");
        sleep(executePause);
        final String moderatorAnswer3 = moderator.getOutput().trim();
        if (!moderatorAnswer3.equals("Server: you are not a moderator or an admin!")) {
            return CheckResult.wrong("Can't find message " +
                    "\"Server: you are not a moderator or an admin!\" " +
                    "after using the \"/kick\" command by someone who is not an admin or a moderator");
        }

        return CheckResult.correct();
    }

    @DynamicTestingMethod
    CheckResult test5_statsAndUnread() {
        final TestedProgram server = new TestedProgram("chat.server");
        final TestedProgram client1 = new TestedProgram("chat.client");
        final TestedProgram client2 = new TestedProgram("chat.client");
        final TestedProgram admin = new TestedProgram("chat.client");
        client1.setReturnOutputAfterExecution(false);
        client2.setReturnOutputAfterExecution(false);
        server.startInBackground();
        sleep(executePause);
        client1.start();
        sleep(executePause);
        client2.start();
        sleep(executePause);
        admin.start();
        sleep(executePause);
        client1.execute("/registration client1 12345678");
        sleep(executePause);
        client2.execute("/registration client2 12345678");
        sleep(executePause);
        admin.execute("/auth admin 12345678");
        sleep(executePause);
        client1.getOutput();
        client2.getOutput();
        admin.getOutput();

        admin.execute("/chat client2");
        sleep(executePause);

        client1.execute("/chat client2");
        sleep(executePause);
        client1.execute("1");
        sleep(executePause);
        client1.execute("2");
        sleep(executePause);
        client1.execute("3");
        sleep(executePause);

        admin.execute("/chat client2");
        sleep(executePause);
        admin.execute("1");
        sleep(executePause);
        admin.execute("2");
        sleep(executePause);
        admin.execute("3");
        sleep(executePause);

        client2.execute("/unread");
        sleep(executePause);
        final String client2Answer1 = client2.getOutput().trim();
        if (!client2Answer1.contains("Server: unread from: admin client1")) {
            return CheckResult.wrong("List of unread messages is not correct");
        }

        admin.execute("/unread");
        sleep(executePause);
        final String adminAnswer1 = admin.getOutput().trim();
        if (!adminAnswer1.contains("Server: no one unread")) {
            return CheckResult.wrong("List of unread messages is not correct");
        }

        client2.execute("/chat admin");
        sleep(executePause);
        client2.execute("1");
        sleep(executePause);
        client2.execute("1");
        sleep(executePause);
        client2.execute("1");
        sleep(executePause);

        admin.execute("/chat client2");
        sleep(executePause);

        admin.getOutput();
        admin.execute("/stats");
        sleep(executePause);
        final String adminAnswer2 = admin.getOutput().trim();
        if (!adminAnswer2.equals(
                "Server:\n" +
                "Statistics with client2:\n" +
                "Total messages: 6\n" +
                "Messages from admin: 3\n" +
                "Messages from client2: 3"
        )) {
            return CheckResult.wrong("Stats information is not correct");
        }


        admin.execute("1");
        sleep(executePause);

        client2.getOutput();
        client2.execute("/stats");
        sleep(executePause);
        final String client2Answer = client2.getOutput().trim();
        System.out.println(adminAnswer2);
        if (!client2Answer.equals(
                "Server:\n" +
                "Statistics with admin:\n" +
                "Total messages: 7\n" +
                "Messages from client2: 3\n" +
                "Messages from admin: 4"
        )) {
            return CheckResult.wrong("Stats information is not correct");
        }

        return CheckResult.correct();
    }

    @DynamicTestingMethod
    CheckResult test6_history100and250() {
        final TestedProgram server = new TestedProgram("chat.server");
        final TestedProgram client3 = new TestedProgram("chat.client");
        final TestedProgram client4 = new TestedProgram("chat.client");
        client3.setReturnOutputAfterExecution(false);
        client4.setReturnOutputAfterExecution(false);
        server.startInBackground();
        sleep(executePause);
        client3.start();
        sleep(executePause);
        client4.start();
        sleep(executePause);
        client3.execute("/registration client3 12345678");
        sleep(executePause);
        client4.execute("/registration client4 12345678");
        sleep(executePause);
        client3.getOutput();
        client4.getOutput();

        client3.execute("/chat client4");
        sleep(executePause);

        for (int i = 1; i <= 50; i++) {
            client3.execute("" + i);
            sleep(executePause);
        }

        client4.execute("/chat client3");
        sleep(executePause);

        final String client4Answer1 = client4.getOutput().trim();
        if (!client4Answer1.equals(
            "(new) client3: 26\n" +
            "(new) client3: 27\n" +
            "(new) client3: 28\n" +
            "(new) client3: 29\n" +
            "(new) client3: 30\n" +
            "(new) client3: 31\n" +
            "(new) client3: 32\n" +
            "(new) client3: 33\n" +
            "(new) client3: 34\n" +
            "(new) client3: 35\n" +
            "(new) client3: 36\n" +
            "(new) client3: 37\n" +
            "(new) client3: 38\n" +
            "(new) client3: 39\n" +
            "(new) client3: 40\n" +
            "(new) client3: 41\n" +
            "(new) client3: 42\n" +
            "(new) client3: 43\n" +
            "(new) client3: 44\n" +
            "(new) client3: 45\n" +
            "(new) client3: 46\n" +
            "(new) client3: 47\n" +
            "(new) client3: 48\n" +
            "(new) client3: 49\n" +
            "(new) client3: 50"
        )) {
            return CheckResult.wrong("A user get wrong messages. Maybe, your server " +
                    "sent more than 25 unread messages? \"client3\" should " +
                    "receive messages from 26 to 50 inclusive " +
                    "(exactly 25 messages).");
        }


        client4.execute("/history test");
        sleep(executePause);
        final String client4Answer2 = client4.getOutput().trim();
        if (!client4Answer2.contains("Server: test is not a number!")) {
            return CheckResult.wrong("Can't get the message \"Server: test is not a number!\"");
        }

        client4.execute("/history 35");
        sleep(executePause);
        final String client4Answer3 = client4.getOutput().trim();
        if (!client4Answer3.equals(
                "Server:\n" +
                "client3: 16\n" +
                "client3: 17\n" +
                "client3: 18\n" +
                "client3: 19\n" +
                "client3: 20\n" +
                "client3: 21\n" +
                "client3: 22\n" +
                "client3: 23\n" +
                "client3: 24\n" +
                "client3: 25\n" +
                "client3: 26\n" +
                "client3: 27\n" +
                "client3: 28\n" +
                "client3: 29\n" +
                "client3: 30\n" +
                "client3: 31\n" +
                "client3: 32\n" +
                "client3: 33\n" +
                "client3: 34\n" +
                "client3: 35\n" +
                "client3: 36\n" +
                "client3: 37\n" +
                "client3: 38\n" +
                "client3: 39\n" +
                "client3: 40"
        )) {
            return CheckResult.wrong("A user received wrong messages after the " +
                    "\"/history X\". Maybe your server sent more than 25 messages?\n" +
                    "For the command \"/history 35\" server should sent from 35-th to 11-th messages inclusive" +
                    "from the end of the conversation.\n" +
                    "Which is from \"client3: 16\" to \"client3: 40\".");
        }
        return CheckResult.correct();
    }

}
