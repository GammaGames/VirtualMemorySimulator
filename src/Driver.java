import java.io.*;
import java.util.*;

public class Driver {
    public static void main(String[] args) {
        if(args.length != 5){
            System.out.println("java Driver programlist commandlist [1 | 2 | 4 | 8 | 16] [lru | fifo | sclru] [d | p]");
            return;
        }

        File programList = new File(args[0]);
        File commandList = new File(args[1]);

        int pageSize = Integer.parseInt(args[2]);
        int bufferSpace = 512;
        if((pageSize != 1 && pageSize % 2 != 0) || Math.sqrt(pageSize) > 4) {
            System.out.println("Page size must be a multiple of 2 between 1 and 16");
            return;
        }
        String algoStr = args[3];
        FrameTable.Algorithms algo = FrameTable.Algorithms.FIFOD;
        boolean demand = args[4].equals("d") ? true : false;

        switch(algoStr) {
            case "fifo":
                if(demand)
                    algo = FrameTable.Algorithms.FIFOD;
                else
                    algo = FrameTable.Algorithms.FIFOP;
                break;
            case "lru":
                if(demand)
                    algo = FrameTable.Algorithms.LRUD;
                else
                    algo = FrameTable.Algorithms.LRUP;
                break;
            case "sclru":
                if(demand)
                    algo = FrameTable.Algorithms.SCLRUD;
                else
                    algo = FrameTable.Algorithms.SCLRUP;
                break;
            default:
                System.out.println("Please enter a valid algorithm");
                return;
        }

        List<Process> processes = new ArrayList<Process>();

        try {
            Scanner fileScanner = new Scanner(programList);
            while(fileScanner.hasNext()) {
                int pid = fileScanner.nextInt();
                int procSize = fileScanner.nextInt();
                Process proc = new Process(pid, procSize, pageSize);
                processes.add(proc);
            }
        }
        catch(FileNotFoundException e) {
            System.out.println("Working Directory = " +
                    System.getProperty("user.dir"));
            System.out.println("Could not find file " + programList);
            return;
        }

        FrameTable ft = new FrameTable(bufferSpace, pageSize, processes.toArray(new Process[processes.size()]), algo);

        try {
            Scanner fileScanner = new Scanner(commandList);
            while(fileScanner.hasNext()) {
                int pid = fileScanner.nextInt();
                int addr = fileScanner.nextInt();

                ft.insert(pid, addr);
            }
        }
        catch(FileNotFoundException e) {
            System.out.println("Working Directory = " +
                    System.getProperty("user.dir"));
            System.out.println("Could not find file " + commandList);
            return;
        }

        System.out.println(ft);
    }
}
