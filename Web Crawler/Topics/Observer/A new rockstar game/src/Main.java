import java.util.*;     

/**
 * Observable interface
**/
interface Observable {
    public void updateObservers();
    public void release(String game);
}

/**
 * Concrete Observable - Rockstar Games
**/
class RockstarGames implements Observable {

    public String releaseGame;
    private List<Observer> list = new LinkedList();

    public void addObserver(Observer observer) {
        list.add(observer);
    }

    public void release(String game) {
        this.releaseGame = game;
        updateObservers();
    }

    @Override
    public void updateObservers() {
        list.stream().forEach(o -> o.update(this.releaseGame));
    }
}

/**
 * Observer interface
**/
interface Observer {
    public void update(String domain);
}

/**
 * Concrete observer - Gamer
**/
class Gamer implements Observer {

    private String name;
    private Observable observable;
    private Set<String> games = new HashSet<>();

    public Gamer(String name, Observable observable) {
        this.name = name;
        this.observable = observable;
    }

    public void buyGame(String game) {
        System.out.println(name + " says : \"Oh, Rockstar releases new game " + game + " !\"");
        games.add(game);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public void update(String game) {
        System.out.println("Notification for gamer : " + name);
        if (games.contains(game)) {
            System.out.println("What? They've already released this game ... I don't understand");
        } else {
            buyGame(game);
        }
    }
}

/**
 * Main class
**/
public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        String game = null;

        RockstarGames rockstarGames = new RockstarGames();

        Gamer garry = new Gamer("Garry Rose", rockstarGames);
        Gamer peter = new Gamer("Peter Johnston", rockstarGames);
        Gamer helen = new Gamer("Helen Jack", rockstarGames);

        rockstarGames.addObserver(garry);
        rockstarGames.addObserver(peter);
        rockstarGames.addObserver(helen);

        for (int i = 0; i < 2; i++) {
            game = scanner.nextLine();
            rockstarGames.release(game);
        }

        scanner.close();
    }
}