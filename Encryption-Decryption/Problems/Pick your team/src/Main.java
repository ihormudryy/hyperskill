import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class SelectionContext {

    private PersonSelectionAlgorithm algorithm;

    public void setAlgorithm(PersonSelectionAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Person[] selectPersons(Person[] persons) {
        return algorithm.select(persons);
    }
}

interface PersonSelectionAlgorithm {

    Person[] select(Person[] persons);
}

class TakePersonsWithStepAlgorithm implements PersonSelectionAlgorithm {

    private int k;

    public TakePersonsWithStepAlgorithm(int step) {
        k = step;
    }

    @Override
    public Person[] select(Person[] persons) {
        AtomicInteger i = new AtomicInteger();
        return Arrays.stream(persons)
                     .filter(person -> i.getAndIncrement() % k == 0)
                     .toArray(Person[]::new);
    }
}


class TakeLastPersonsAlgorithm implements PersonSelectionAlgorithm {

    private int k;

    public TakeLastPersonsAlgorithm(int count) {
        k = count;
    }

    @Override
    public Person[] select(Person[] persons) {
        return Arrays.stream(persons)
                     .skip(persons.length - k)
                     .toArray(Person[]::new);
    }
}

class Person {

    String name;

    public Person(String name) {
        this.name = name;
    }
}

/* Do not change code below */
public class Main {

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int count = Integer.parseInt(scanner.nextLine());
        final Person[] persons = new Person[count];

        for (int i = 0; i < count; i++) {
            persons[i] = new Person(scanner.nextLine());
        }

        final String[] configs = scanner.nextLine().split("\\s+");

        final PersonSelectionAlgorithm alg = create(configs[0], Integer.parseInt(configs[1]));
        SelectionContext ctx = new SelectionContext();
        ctx.setAlgorithm(alg);

        final Person[] selected = ctx.selectPersons(persons);
        for (Person p : selected) {
            System.out.println(p.name);
        }
    }

    public static PersonSelectionAlgorithm create(String algType, int param) {
        switch (algType) {
            case "STEP": {
                return new TakePersonsWithStepAlgorithm(param);
            }
            case "LAST": {
                return new TakeLastPersonsAlgorithm(param);
            }
            default: {
                throw new IllegalArgumentException("Unknown algorithm type " + algType);
            }
        }
    }
}