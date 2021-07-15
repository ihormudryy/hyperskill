import java.util.*;     

interface Observable {
    public void addObserver(Observer observer);

    public void updateObservers();

    public void release(String game);
}

/** Concrete Observable */
class RockstarGames implements Observable {

    public String releaseGame;
    private List<Observer> observers = new LinkedList<>();

    @Override
    public void release(String game) {
        this.releaseGame = game;
        updateObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void updateObservers() {
        observers.stream().forEach(observer -> observer.update(releaseGame));
    }
}

interface Observer {

    public void update(String game);
}

/** Concrete Observer */
class Gamer implements Observer {

    private String name;
    private String reaction;
    private Set<String> games = new HashSet<>();
    private Observable observable;

    public Gamer(String name, String reaction, Observable observable) {
        this.reaction = reaction;
        this.observable = observable;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public void update(String game) {
        System.out.println("Inform message to : " + name);
        System.out.println(String.format("%s says: %s", name, reaction));
    }
}

/** Main Class */

public class Main {
    public static void main(String[] args) {

        final Scanner scanner = new Scanner(System.in);

        String game = null;

        RockstarGames rockstarGames = new RockstarGames();

        Gamer garry = new Gamer("Garry Rose", "I want to pre-order", rockstarGames);
        Gamer peter = new Gamer("Peter Johnston", "Pinch me...", rockstarGames);
        Gamer helen = new Gamer("Helen Jack", "Jesus, it's new game from Rockstar!", rockstarGames);

        rockstarGames.addObserver(garry);
        rockstarGames.addObserver(peter);
        rockstarGames.addObserver(helen);

        game = scanner.nextLine();
        System.out.println("It's happened! RockstarGames releases new game - " + game + "!");

        rockstarGames.release(game);

        scanner.close();
    }
}