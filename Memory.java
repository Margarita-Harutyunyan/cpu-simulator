public class Memory {

    public Memory() {
        memory = new int[32];
        for (int i = 0; i < memory.length; ++i) {
            memory[i] = 0;
        }
    }

    public String read(int address) {
        return Integer.toBinaryString(memory[address]);
    }

    public void write(int address, int data) {
        memory[address] = data;
    }

    private int[] memory;
}
