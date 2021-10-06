import java.util.ArrayList;
import java.util.LinkedList;

class ListOperations {
    public static void transferAllElements(LinkedList<String> linkedList, ArrayList<String> arrayList) {
        for (int i = 0; i < linkedList.size(); i++) {
            String tmp = linkedList.get(i);
            linkedList.set(i, arrayList.get(i));
            arrayList.set(i, tmp);
        }
    }
}