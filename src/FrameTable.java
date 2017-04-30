import java.util.LinkedList;

public class FrameTable {
    private Process[] processes;
    private LinkedList<Frame> table = new LinkedList<Frame>();
    private int buffer = 512;
    private int pageSize;
    private int faults = 0;
    public enum Algorithms {
        FIFOD, LRUD, SCLRUD, FIFOP, LRUP, SCLRUP
    }
    private Algorithms algo;

    public FrameTable(int buffer, int pageSize, Process[] procs, Algorithms algo) {
        this.buffer = buffer / pageSize;
        this.pageSize = pageSize;
        this.processes = procs;
        this.algo = algo;
    }

    private boolean insertFrame(Frame fr) {
        if(table.size() < buffer) {
            table.addFirst(fr);
            return true;
        }
        return false;
    }

    private boolean checkIfContains(Frame fr) {
        return table.contains(fr);
    }

    private int getFaults() { return this.faults; }

    public boolean insert(int pid, int addr) {
        boolean result = false;
        switch(algo) {
            case FIFOD:
                result = insertFIFOD(pid, addr);
                break;
            case LRUD:
                result = insertLRUD(pid, addr);
                break;
            case SCLRUD:
                result = insertSCLRUD(pid, addr);
                break;
            case FIFOP:
                result = insertFIFOP(pid, addr);
                break;
            case LRUP:
                result = insertLRUP(pid, addr);
                break;
            case SCLRUP:
                result = insertSCLRUP(pid, addr);
                break;
        }
        return result;
    }

    // Returns true if page fault, false if none
    private boolean insertFIFOD(int pid, int addr) {
        Frame fr = new Frame(processes[pid].getPage(addr));
        if(!checkIfContains(fr)) {
            if(!insertFrame(fr)) {
                table.removeLast();
                insertFrame(fr);
            }
            this.faults++;
            return true;
        }
        return false;
    }

    // Returns true if page fault, false if none
    private boolean insertFIFOP(int pid, int addr) {
        boolean result = false;
        result |= insertFIFOD(pid, addr);
        if(processes[pid].getNextPage(addr) != -1)
            result |= insertFIFOD(pid, processes[pid].getNextPage(addr));
        return result;
    }

    // Returns true if page fault, false if none
    private boolean insertLRUD(int pid, int addr) {
        Frame fr = new Frame(processes[pid].getPage(addr));
        if(!checkIfContains(fr)) {
            if(!insertFrame(fr)) {
                table.removeLast();
                insertFrame(fr);
            }
            this.faults++;
            return true;
        }
        else {
            table.remove(fr);
            insertFrame(fr);
        }
        return false;
    }

    // Returns true if page fault, false if none
    private boolean insertLRUP(int pid, int addr) {
        boolean result = false;
        result |= insertLRUD(pid, addr);
        if(processes[pid].getNextPage(addr) != -1)
            result |= insertLRUD(pid, processes[pid].getNextPage(addr));
        return result;
    }

    // Returns true if page fault, false if none
    private boolean insertSCLRUD(int pid, int addr) {
        Frame fr = new Frame(processes[pid].getPage(addr));
        if(!checkIfContains(fr)) {
            if(!insertFrame(fr)) {
                for(int i = table.size() - 1; i >= 0; i--) {
                    if(table.get(i).getBit()) {
                        table.remove(i);
                        insertFrame(fr);
                        this.faults++;
                        return true;
                    }
                    else {
                        Frame tmpFr = table.get(i);
                        tmpFr.setSecondChance();
                        table.remove(i);
                        table.addFirst(tmpFr);
                        //table.get(i).setSecondChance();
                    }
                }
                table.removeLast();
                insertFrame(fr);
            }
            this.faults++;
            return true;
        }
        else {
            table.remove(fr);
            insertFrame(fr);
        }
        return false;
    }

    // Returns true if page fault, false if none
    private boolean insertSCLRUP(int pid, int addr) {
        boolean result = false;
        result |= insertSCLRUD(pid, addr);
        if(processes[pid].getNextPage(addr) != -1)
            result |= insertSCLRUD(pid, processes[pid].getNextPage(addr));
        return result;
    }

    @Override
    public String toString() {
        return algo.name() + ',' + pageSize + ',' + getFaults();
        //return "algo:" + algo.name() + ", page size:" + this.pageSize + " faults:" + getFaults();

    }

    public static void main(String[] args) {
        int pageSize = 2;
        int bufferSpace = 3;

        Process[] procs = new Process[5];
        procs[0] = new Process(0, 4, pageSize);
        procs[1] = new Process(1, 8, pageSize);
        procs[2] = new Process(2, 8, pageSize);
        procs[3] = new Process(3, 4, pageSize);
        procs[4] = new Process(4, 2, pageSize);


        String[] trace = {  "0 0",
                            "2 0",
                            "2 1",
                            "2 2",
                            "2 3",
                            "2 4",
                            "4 0",
                            "4 1"};

        FrameTable ft = new FrameTable(bufferSpace, 2, procs, Algorithms.FIFOD);
        for(String str: trace) {
            String[] splitStr = str.split("\\s");
            int pid = Integer.parseInt(splitStr[0]);
            int addr = Integer.parseInt(splitStr[1]);
            if(ft.insert(pid, addr)) {
                System.out.print("Proc " + pid + " at " + addr + ": Inserted with fault");
            }
            else {
                System.out.print("Proc " + pid + " at " + addr + ": Already exists");
            }
            Frame fr = new Frame(procs[pid].getPage(addr));
            System.out.println("\r\n" + fr);
        }
        System.out.println("\r\n" + ft);
    }
}
