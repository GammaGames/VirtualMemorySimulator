import java.util.LinkedList;
import java.util.HashMap;

import static com.sun.org.apache.xalan.internal.lib.ExsltStrings.split;

public class FrameTable {

    public class Frame {
        int address;
        boolean refBit = false;
        public Frame(int addr) {
            this.address = addr;
        }
        public Frame(int addr, boolean ref) {
            this.address = addr;
            this.refBit = ref;
        }

        public void setBit(boolean ref) {
            this.refBit = ref;
        }

        @Override
        public boolean equals(Object o) {
            Frame fr = (Frame)o;
            return fr.address == this.address;
        }
    }

    private HashMap<String, Process> processMap = new HashMap<String, Process>();
    private LinkedList<Frame> table = new LinkedList<Frame>();
    private int buffer;
    private int faults = 0;

    public FrameTable(int buffer, Process[] procs) {
        this.buffer = buffer;
        for(Process p: procs) {
            processMap.put(p.getId(), p);
        }
    }

    public boolean insertFrame(Frame fr) {
        if(table.size() < buffer) {
            table.addFirst(fr);
            return true;
        }
        return false;
    }

    public boolean checkIfContains(String proc, int addr) {
        Process process = processMap.get(proc);
        return table.contains(new Frame(process.getPage(addr)));
    }

    public int getFaults() { return this.faults; }

    // Returns true if page fault, false if none
    public boolean insertFifo(String proc, int addr) {
        if(!checkIfContains(proc, addr)) {
            Process process = processMap.get(proc);
            Frame fr = new Frame(process.getPage(addr));
            if(!insertFrame(fr)) {
                table.removeLast();
                insertFrame(fr);
            }
            else {
                insertFrame(fr);
            }
            this.faults++;
            return true;
        }
        return false;
    }


    // Returns true if page fault, false if none
    public boolean insertLRU(String proc, int addr) {
        Process process = processMap.get(proc);
        if(!checkIfContains(proc, addr)) {
            Frame fr = new Frame(process.getPage(addr));
            if(!insertFrame(fr)) {
                table.removeLast();
                insertFrame(fr);
            }
            else {
                insertFrame(fr);
            }
            this.faults++;
            return true;
        }
        else {
            int index = table.indexOf(new Frame(process.getPage(addr)));
            Frame tmpFrame = table.get(index);
            table.remove(index);
            insertFrame(tmpFrame);
        }
        return false;
    }

    // Returns true if page fault, false if none
    public boolean insert2LRU(String proc, int addr) {
        Process process = processMap.get(proc);
        if(!checkIfContains(proc, addr)) {
            Frame fr = new Frame(process.getPage(addr));
            if(!insertFrame(fr)) {
                for(int i = table.size(); i >= 0; i++) {
                    if(table.get(i).refBit) {
                        table.removeLast();
                        insertFrame(fr);
                        this.faults++;
                        return true;
                    }
                    else {
                        table.get(i).setBit(true);
                    }
                }
                for(int i = table.size(); i >= 0; i++) {
                    if(table.get(i).refBit) {
                        table.removeLast();
                        insertFrame(fr);
                    }
                }
            }
            else {
                insertFrame(fr);
            }
            this.faults++;
            return true;
        }
        else {
            int index = table.indexOf(new Frame(process.getPage(addr)));
            Frame tmpFrame = table.get(index);
            tmpFrame.setBit(false);
            table.remove(index);
            insertFrame(tmpFrame);
        }
        return false;
    }

    public static void main(String[] args) {

        int pageSize = 2;
        int bufferSpace = 3;
        Process[] procs = new Process[5];
        procs[0] = new Process("0", 1, pageSize);
        procs[1] = new Process("1", 1, pageSize);
        procs[2] = new Process("2", 1, pageSize);
        procs[3] = new Process("3", 1, pageSize);
        procs[4] = new Process("4", 1, pageSize);

        String[] trace = {  "0 1",
                            "1 1",
                            "2 1",
                            "3 1",
                            "2 1",
                            "4 1",
                            "1 1",
                            "4 1",
                            "2 1",
                            "3 1"};

        FrameTable ft = new FrameTable(bufferSpace, procs);
        for(String str: trace) {
            String[] splitStr = str.split("\\s");
            System.out.println("");
            if(ft.insertFifo(splitStr[0], Integer.parseInt(splitStr[1]))) {
                System.out.print("Proc " + splitStr[0] + ": Inserted with fault");
            }
            else {
                System.out.print("Proc "+ splitStr[0] + ": Already exists");
            }
        }
        System.out.println("\n\rTotal faults FIFO: " + ft.getFaults());


        ft = new FrameTable(bufferSpace, procs);
        for(String str: trace) {
            String[] splitStr = str.split("\\s");
            System.out.println("");
            if(ft.insertLRU(splitStr[0], Integer.parseInt(splitStr[1]))) {
                System.out.print("Proc " + splitStr[0] + ": Inserted with fault");
            }
            else {
                System.out.print("Proc "+ splitStr[0] + ": Already exists");
            }
        }
        System.out.println("\n\rTotal faults LRU: " + ft.getFaults());
    }
}
