public class Frame {
    private int address;
    private boolean refBit = false;
    public Frame(int addr) {
        this.address = addr;
    }
    public Frame(int addr, boolean ref) {
        this.address = addr;
        this.refBit = ref;
    }

    public void setSecondChance() {
        this.refBit = true;
    }

    public boolean getBit() {
        return this.refBit;
    }

    @Override
    public String toString() {
        return "page:" + this.address + " ref:" + this.refBit;
    }

    @Override
    public boolean equals(Object o) {
        Frame fr = (Frame)o;
        return fr.address == this.address;
    }
}
