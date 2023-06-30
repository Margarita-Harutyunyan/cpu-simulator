public class CU {
    private RegisterFile registerFile;
    private Memory memory;

    public CU(RegisterFile regFile, Memory memo) {
        registerFile = regFile;
        memory = memo;
    }

    // JMP
    public void jmp(int labelIndex) {
        registerFile.setRegisterValue("GH", labelIndex);
    }

    // JG
    public void jg(int labelIndex) {
        if (registerFile.getRegisterValue("DA") > 0) {
            registerFile.setRegisterValue("GH", labelIndex);
        }
    }

    // JL
    public void jl(int labelIndex) {
        if (registerFile.getRegisterValue("DA") < 0) {
            registerFile.setRegisterValue("GH", labelIndex);
        }
    }

    // JE
    public void je(int labelIndex) {
        if (registerFile.getRegisterValue("DA") == 0) {
            registerFile.setRegisterValue("GH", labelIndex);
        }
    }

    // MOV to register
    public void movToReg(int address, int operand) {
        registerFile.setRegisterValue(address, operand);
    }

    // MOV to memory
    public void movToMemory(int address, int operand) {
        memory.write(address, operand);
    }
}