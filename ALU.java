public class ALU {
    // boolean carryFlag;
    boolean overflowFlag;
    boolean zeroFlag;
    boolean signFlag;
    int REM;

    // Getters
    public boolean getOverflowFlag() {
        return overflowFlag;
    }

    public boolean getZeroFlag() {
        return zeroFlag;
    }

    public boolean getSignFlag() {
        return signFlag;
    }

    public int getRemainder() {
        return REM;
    }

    // Arithmetic
    public int add(int operand1, int operand2) {
        int res = operand1 + operand2;
        if (res > Short.MAX_VALUE) {
            overflowFlag = true;
        }
        return res;
    }

    public int sub(int operand1, int operand2) {
        int res = operand1 - operand2;
        if (res < 0) {
            signFlag = true;
        }
        return res;
    }

    public int mul(int operand1, int operand2) {
        int res = operand1 * operand2;
        return res;
    }

    public int div(int operand1, int operand2) {
        int res = operand1 / operand2;
        REM = operand1 % operand2;
        return res;
    }
    
    // Logic
    public int and(int operand1, int operand2) {
        int res = operand1 & operand2;
        return res;
    }

    public int or(int operand1, int operand2) {
        int res = operand1 | operand2;
        return res;
    }

    public int not(int operand){
        int res = ~operand;
        return res;
    }

    public void cmp(int operand1, int operand2) {
        zeroFlag = false;
        signFlag = false;

        int difference = operand1 - operand2;
        if (difference < 0) {
            signFlag = true;
        }
        else if (difference == 0) {
            zeroFlag = true;
        }
    }
}