import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        String programList = args[0] + ".txt";
        String commandList = args[1] + ".txt";
        int pageSize = Integer.parseInt(args[2]);
        if(pageSize % 2 != 0 || pageSize / 2 > 4) {
            System.out.println("Page size must be a multiple of 2 between 2 and 16");
            return;
        }

        String algo = args[3];
        //one more arg?

        List<Process> processes = new ArrayList<Process>();

        try {
            Scanner fileScanner = new Scanner(new File(programList));
            while (fileScanner.hasNextLine()) {
                Process proc = new Process(fileScanner.next(), fileScanner.nextInt(), pageSize);
                processes.add(proc);
            }
        }
        catch(Exception FileNotFoundException) {
            System.out.println("could not fine file " + programList);
        }
    }
}
