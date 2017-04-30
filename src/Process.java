public class Process {
    private int pid;
    private int procSize;
    private int startPage;
    private static int currentMaxPage = 0;
    private static int PAGE_SIZE = 1;

    public Process(int id, int procSize, int pageSize) {
        this.PAGE_SIZE = pageSize;
        this.pid = id;
        this.procSize = procSize;
        this.startPage = currentMaxPage;

        currentMaxPage += procSize / PAGE_SIZE;
    }

    public int getPid() { return this.pid; }
    public int getProcSize() { return this.procSize; }
    public void setPageSize(int ps) { PAGE_SIZE = ps; }
    public int getPageSize() { return PAGE_SIZE; }

    public int getPage(int addr) {
        return addr < procSize ?
                this.startPage + (addr / PAGE_SIZE) : -1;
    }

    public int getNextPage(int addr) {
        return addr < procSize ?
                this.startPage + 1 + (addr / PAGE_SIZE) : -1;
    }


    @Override
    public String toString() {
        return "proc" + this.pid + "- size: " + this.procSize + ", startPage: " + this.startPage;
    }

    public static void main(String[] args) {
        Process[] procs = new Process[8];
        int ps = 2;

        procs[0] = new Process(0, 512, ps);
        procs[1] = new Process(1, 256, ps);
        procs[2] = new Process(2, 128, ps);
        procs[3] = new Process(3, 64, ps);
        procs[4] = new Process(4, 1024, ps);
        procs[5] = new Process(5, 1024, ps);
        procs[6] = new Process(6, 1024, ps);
        procs[7] = new Process(7, 1024, ps);

        for(Process p:procs) {
            System.out.println(p);
        }

        System.out.println("\r\nGetting proc0 at 32: page" + procs[0].getPage(32));
        System.out.println("Getting proc0 at 64: page" + procs[0].getPage(64));
        System.out.println("Getting proc1 at 65: page" + procs[1].getPage(65));
        System.out.println("Getting proc1 at 128: page" + procs[1].getPage(128));
        System.out.println("Getting proc2 at 129: page" + procs[2].getPage(129));
        System.out.println("Getting proc3 at 63: page" + procs[3].getPage(63));
        System.out.println("Getting proc4 at 1023: page" + procs[4].getPage(1023));
    }
}
