public class Process {
    private String procId;
    private int procSize;
    private int pageSize;
    private int startPage;
    private static int currentMaxPage = 0;

    public Process(String id, int procSize, int pageSize) {
        this.procId = id;
        this.procSize = procSize;
        this.pageSize = pageSize;
        this.startPage = currentMaxPage;

        this.currentMaxPage += (int)Math.ceil((double)procSize / pageSize);
    }

    public String getId() { return this.procId; }
    public int getProcSize() { return this.procSize; }
    public int getPageSize() { return this.pageSize; }
    public int getPage(int addr) {
        return this.startPage + (int)Math.floor((double)addr / this.pageSize);
    }

    @Override
    public String toString() {
        return procId + "- size: " + procSize + ", startPage: " + startPage;
    }
}
