import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class FakeCPU{

    private RegisterFile registerFile;
    private Memory memory; 
    private ALU alu;
    private CU cu;
    private boolean jumped;
    
    private Map<String, Integer> opcodeMap;
    private Map<String, Integer> labelMap;

    // Constructor
    public FakeCPU() {
        registerFile = new RegisterFile();
        memory = new Memory();
        alu = new ALU();
        cu = new CU(registerFile, memory);
        jumped = false;
        labelMap = new HashMap<>();

        opcodeMap = new HashMap<>();
        opcodeMap.put("ADD", 0);
        opcodeMap.put("SUB", 1);
        opcodeMap.put("AND", 2);
        opcodeMap.put("OR", 3);
        opcodeMap.put("CMP", 4);
        opcodeMap.put("NOT", 5);
        opcodeMap.put("MUL", 6);
        opcodeMap.put("DIV", 7);
        opcodeMap.put("JMP", 8);
        opcodeMap.put("JG", 9);
        opcodeMap.put("JL", 10);
        opcodeMap.put("JE", 11);
        opcodeMap.put("MOV", 12);
    }

    public int getLabelIndex(String labelName) {
        return labelMap.getOrDefault(labelName, -1);
    }

    public void setLabel(String labelName, int index){
        labelMap.put(labelName, index);
    }

    public void run(String filePath) {
        loadLabels(filePath);
        loadInstructions(filePath);
        int i = 0;
        registerFile.setRegisterValue("GH", i);
        while (registerFile.getRegisterValue("GH") < registerFile.getRegisterValue("GT")) {
            execute(decode(fetch()));
            if (!jumped) {
                ++i;
                registerFile.setRegisterValue("GH", i); 
            } else {
                i = registerFile.getRegisterValue("GH");
                jumped = false;
            }
        }
        dumpMemory();
    }

    public void loadLabels(String filePath) {
        int index = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check if it's a label 
                if (line.contains(":")) {
                    int colonIndex = line.indexOf(":");
                    String labelName = line.substring(0, colonIndex);
                    setLabel(labelName, index);
                }
                
                else {
                    index ++;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadInstructions(String filePath) {
        int index = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check if it's a label first
                if (line.contains(":")) {
                    // Do nothing
                }
                // make the instruction code
                else {
                    int instruction = encode(line);
                    memory.write(index, instruction);
                    index ++;
                }

            }
        } catch (IOException e) {
            System.out.println("Invalid file in loadInstructions");
            e.printStackTrace();
        }
        //have a register to hold the count of lines in the program
        registerFile.setRegisterValue("GT", index + 1);
    }
    
    public int encode(String line) {
        line = line.trim();
        String[] tokens = line.split(" ");

        int oc;
        int aM1;
        int addr1;
        int aM2 = 3;
        int addr2 = 0;

        String opcode;
        String addressMode1;
        String address1;
        String addressMode2;
        String address2;

        oc = opcodeMap.getOrDefault(tokens[0], -1);
        opcode = fill(oc, 4);

        String cleaned = tokens[1];
        if (cleaned.contains(",")) {
            int index = cleaned.indexOf(",");
            cleaned = cleaned.substring(0, index);
        }

        aM1 = getMode(cleaned);
        addressMode1 = fill(aM1, 2);
 
        addr1 =  getAddress(cleaned);
        address1 = fill(addr1, 5);

        if (tokens.length == 3) {
            aM2 = getMode(tokens[2]);
            addr2 = getAddress(tokens[2]);
        }
        addressMode2 = fill(aM2, 2);
        address2 = fill(addr2, 5);

        StringBuilder reversedStr = new StringBuilder(address2).reverse();
        String address2Rev = reversedStr.toString(); 

        String instr = opcode + addressMode1 + address1 + addressMode2 + address2Rev;
        StringBuilder reversedInstruction = new StringBuilder(instr).reverse();
        instr = reversedInstruction.toString();
        int instruction = Integer.parseInt(instr, 2);

        return instruction;
    }
    
    public String fetch() {
        int index = registerFile.getRegisterValue("GH");
        String reversedInstruction = memory.read(index);
        while(reversedInstruction.length() < 18) {
            reversedInstruction = "0" + reversedInstruction;
        }
        StringBuilder instructionBuilder = new StringBuilder(reversedInstruction).reverse();
        String instruction = instructionBuilder.toString();
        return instruction;
    }
    public ArrayList<Integer> decode(String instruction) {
        ArrayList<Integer> decodedList = new ArrayList<>();
        // opcode = read 4 bits
        int opcode = Integer.parseInt(instruction.substring(0, 4), 2);
        // addressMode1 = read 2 bits
        int addressMode1 = Integer.parseInt(instruction.substring(4, 6), 2);
        // address1 = read 5 bits
        int address1 = Integer.parseInt(instruction.substring(6, 11), 2);

        int operand1 = getValue(addressMode1, address1);

        decodedList.add(opcode);
        decodedList.add(addressMode1);
        decodedList.add(address1);
        decodedList.add(operand1);
    

        // check if the operation has a second operand
        int addressMode2 = Integer.parseInt(instruction.substring(11, 13), 2);
        if (addressMode2 != 3) {

            String addr2Reversed = instruction.substring(13);
            StringBuilder operandBuilder = new StringBuilder(addr2Reversed).reverse();
            int address2 = Integer.parseInt(operandBuilder.toString(), 2);
            int operand2 = getValue(addressMode2, address2);
            decodedList.add(operand2);
        }
    
        // pass an array of 4 or 5 integers [opcode, addressMode1, address1, operand1, (operand2)]
        return decodedList;

    }

    public void execute(ArrayList<Integer> decodedList) {
        int opcode = decodedList.get(0);
        int addressMode = decodedList.get(1);
        int address = decodedList.get(2);
        int operand1 = decodedList.get(3);
        int operand2;
        
        if (opcode <= 4) {
            operand2 = decodedList.get(4);
            executeBinary(opcode, addressMode, address, operand1, operand2);

        }

        else if (opcode == 5 || opcode == 6 || opcode == 7) {
            executeMulDivNot(opcode, operand1);
        }

        else if (opcode >= 8 && opcode <= 11) {
            executeJump(opcode, address);
        }
        
        else if(opcode == 12) {
            operand2 = decodedList.get(4);
            executeMov(addressMode, address, operand2);
        }
        else {
            System.out.println("Invalid instruction in execute");
        }
    }
    public void executeBinary(int opcode, int addressMode, int address, int operand1, int operand2) {
        int res = 0;

        switch(opcode) {
            case 0:
                res = alu.add(operand1, operand2);
                break;
            case 1:
                res = alu.sub(operand1, operand2);
                break;
            case 2:
                res = alu.and(operand1, operand2);
                break;
            case 3:
                res = alu.or(operand1, operand2);
                break;
            case 4:
                alu.cmp(operand1, operand2);
                if (alu.getSignFlag()) {
                    registerFile.setRegisterValue("DA", -1);
                }
                else if (alu.getZeroFlag()) {
                    registerFile.setRegisterValue("DA", 0);
                }
                else {
                    registerFile.setRegisterValue("DA", 1);
                }
                return;
            default:
                System.out.println("Invalid instruction from executeBinary");
                break;     
        }
        writeBack(addressMode, address, res);
    }

    public void writeBack(int addressMode, int address, int res) {
        switch(addressMode) {
            case 0: // register
                registerFile.setRegisterValue(address, res);
                break;
            case 1: // address in memory
                memory.write(address, res);
                break;
            default:
                System.out.println("Invalid instruction in writeBack");

        }
    };

    public void executeMulDivNot(int opcode, int operand1) {
        int res = 0;
        int operandAYB = registerFile.getRegisterValue("AYB");
                
        switch(opcode) {
            case 5:
                res = alu.not(operand1);
                break;
            case 6:
                res = alu.mul(operand1, operandAYB);
                break;
            case 7:
                res = alu.div(operandAYB, operand1);
                break;
            default:
                System.out.println("Invalid instruction in executeMulDivNot");
        }
        registerFile.setRegisterValue("AYB", res);
    }

    public void executeJump(int opcode, int address) {
        switch(opcode) {
            case 8:
                cu.jmp(address);
                jumped = true;
                break;
            case 9:
                cu.jg(address);
                jumped = true;
                break;
            case 10:
                cu.jl(address);
                jumped = true;
                break;
            case 11:
                cu.je(address);
                jumped = true;
                break;
            default:
                System.out.println("Invalid instruction in executeJump");
                break;
        }
    }

    public void executeMov(int addressMode, int address, int operand) {
        switch(addressMode) {
            case 0:
                cu.movToReg(address, operand);
                break;
            case 1:
                cu.movToMemory(address, operand);
                break;
            default:
                System.out.println("Invalid instruction in executeMov");
                break;
        }
    }

    // Dump Memory
    public void dumpMemory(){
        System.out.println("Memory state after running the program:\n");
        for (int i = 0; i < 32; ++i) {
            System.out.println(i + ": " + memory.read(i));
        }
    }
    // Helper methods
    public String fill(int number, int length) {
        String filled = Integer.toBinaryString(number);
        while (filled.length() < length) {
            filled = "0" + filled;
        }
        return filled;
    }

    public int getMode(String token) {
        if (token.contains(",")) {
            int index = token.indexOf(",");
            token = token.substring(0, index);
        }
        // 0 if register
        if (registerFile.hasKey(token)) {
            return 0;
        }
        // 1 if memory address
        else if (token.contains("[") && token.contains("]")) {
            return 1;
        }
        // 2 if literal
        else if (token.matches("\\d+")) {
            return 2;
        }
        // 3 if label
        else {
            return 3;
        }
    }

    public int getAddress(String token) {
        int mode = getMode(token);
        int address = -1;
        // 0 if register
        if (mode == 0) {
            address = registerFile.getRegisterIndex(token);
        }
        // 1 if memory address
        else if (mode == 1) {
            address = Integer.parseInt(token.substring(1, token.length() - 1));
        }
        // 2 if literal
        else if (mode == 2) {
            address = Integer.parseInt(token);
        }
        // 3 if label
        else if (mode == 3) {
            address = getLabelIndex(token);
        }
        else {
            System.out.println("Invalid instruction");
        }
        return address;
    }

    public int getValue(int addressMode, int address) {
        int value = -1;
        // 0 if register
        if (addressMode == 0) {
            value = registerFile.getRegisterValue(address);
        }
        // 1 if memory address
        else if (addressMode == 1) {
            value = Integer.parseInt(memory.read(address), 2);
        }
        // 2 if literal
        else if (addressMode == 2) {
            value = address;
        }
        // 3 if label
        else if (addressMode == 3) {
            value = 0;
        }
        else {
            System.out.println("Invalid instruction");
        }
        return value;
    }

}