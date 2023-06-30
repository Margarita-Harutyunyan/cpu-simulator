import java.util.HashMap;
import java.util.Map;

public class RegisterFile {

    private int[] registerFile;
    private Map<String, Integer> registerMap;

    public RegisterFile() {
        registerFile = new int[8];

        registerMap = new HashMap<>();
        registerMap.put("AYB", 0);
        registerMap.put("BEN", 1);
        registerMap.put("GIM", 2);
        registerMap.put("DA", 3);
        registerMap.put("ECH", 4);
        registerMap.put("ZA", 5);
        registerMap.put("GH", 6);
        registerMap.put("GT", 7);
    }

    public int getRegisterIndex(String regName) {
        return registerMap.getOrDefault(regName, -1);
    }

    public int getRegisterValue(String regName) {
        int index = registerMap.getOrDefault(regName, -1);
        return registerFile[index];
    }

    public int getRegisterValue(int index) {
        return registerFile[index];
    }

    public void setRegisterValue(String regName, int value) {
        int index = registerMap.getOrDefault(regName, -1);
        registerFile[index] = value;
    }

    public void setRegisterValue(int index, int value) {
        registerFile[index] = value;
    }

    public boolean hasKey(String keyName) {
        return registerMap.containsKey(keyName);
    }
}