
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Test
{
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Set<String> Students1 = new HashSet<>();
        Set<String> Students2 = new HashSet<>();
        System.out.println("Enter names for Students1 (type 'exit' to finish):");
        while (true) {
            String name = scanner.nextLine();
            if (name.equalsIgnoreCase("exit")) {
                break;
            }
            Students1.add(name);
        }
        scanner.close();
        System.out.println("Students1: " + Students1);
    }
}