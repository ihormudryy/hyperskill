package engine.model;

public class Message {

    public boolean success;
    public String feedback;

    private final String CORRECT_MESSAGE = "Congratulations, you're right!";
    private final String INCORRECT_MESSAGE = "Wrong answer! Please, try again.";

    public Message(boolean flag) {
        this.success = flag;
        this.feedback = flag ? CORRECT_MESSAGE : INCORRECT_MESSAGE;
    }
}
