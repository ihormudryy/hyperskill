import javax.swing.table.DefaultTableModel;
import java.util.Scanner;

class TriangleTable {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[] {"X", "Y"});
        while (s.hasNext()) {
            Object[] row = new Object[] {s.nextInt(), s.nextInt()};
            model.addRow(row);
        }
        String[] columns = {"Employee Name" , "Job Title" , "Salary"};
        // do not remove the code below
        TableModelTest.test(model);
    }
}