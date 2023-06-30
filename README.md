# CPU Simulator
The CPU Simulator is a Java-based program that simulates the execution of assembly instructions on a virtual CPU. It reads assembly instructions from a text file and performs operations such as addition, subtraction, logical AND, logical OR, comparison, multiplication, division, jumping, and moving data. It utilizes general-purpose registers for storing and manipulating data during execution.
Instructions Supported

The CPU Simulator supports the following assembly instructions:

    ADD: Performs addition between two operands.
    SUB: Performs subtraction between two operands.
    AND: Performs logical AND between two operands.
    OR: Performs logical OR between two operands.
    NOT: Performs logical negation of an operand.
    CMP: Compares two operands and sets a comparison flag.
    MUL: Performs multiplication between two operands, one of which is provided by default.
    DIV: Performs division between two operands, one of which is provided by default.
    JMP: Jumps to a specified address.
    JG: Jumps to a specified address if the previous comparison was greater.
    JL: Jumps to a specified address if the previous comparison was less.
    JE: Jumps to a specified address if the previous comparison was equal.
    MOV: Moves data between registers or memory locations.

General Registers

The CPU Simulator provides the following general registers for storing data:

    AYB
    BEN
    GIM
    DA
    ECH
    ZA

You can use these registers to store intermediate results, operands, or any other data required during the execution of instructions.
Usage

To use the CPU Simulator, follow these steps:

    Compile all the Java files provided using a Java compiler. You can use the following command:

javac *.java

Prepare an assembly text file containing the instructions you want to execute. The file should follow the assembly syntax supported by the simulator. You can modify the example text file provided to suit your needs.

Run the Main file to execute the simulator.


    The simulator will read the assembly instructions from the file, execute them, and display the memory state after running the program. The memory state shows the values stored in memory locations.

    Review the output to observe the execution of the instructions and the resulting memory state.

Note: Make sure you have Java installed on your system before running the simulator.

Feel free to modify the provided Java files and assembly text file according to your requirements. You can define additional instructions, registers, or modify the existing logic to extend the functionality of the simulator.

Enjoy simulating your assembly programs with the FakeCPU Simulator!
