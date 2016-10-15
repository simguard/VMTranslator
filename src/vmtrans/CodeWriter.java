package vmtrans;

/*
 * @author Jeffrey White   10/08/2016
 * CodeWriter is a class that translates VM commands into their Hack assembly language equivalent
 */
public class CodeWriter {

	//Integer used to count each time a label is created to make unique labels for the code.
	public static int labelInc = 0;	
	
	/*
	 * writeArithmetic takes VM arithmetic commands (e.g. add, sub) and translates it into their Hack assembly language equivalent 
	 * @param String command - the command to be translated into Hack assembly
	 * @return String - the translated code
	 */
	public String writeArithmetic (String command) {
		String asmCommands = null;
		
		switch (command) {
		case "add":	
			asmCommands = "@SP\n"
					+ "AM=M-1\n"				//decrements stack pointer and sets A to the address of the value at the top of the stack
					+ "D=M\n"					//D=y
					+ "A=A-1\n"					//A=address of x
					+ "M=D+M";					//M=x+y
			break;
		case "sub":	
			asmCommands = "@SP\n"
					+ "AM=M-1\n"				//decrements stack pointer and sets A to the address of the value at the top of the stack
					+ "D=M\n"					//D=y
					+ "A=A-1\n"					//A=address of x
					+ "M=M-D";					//M=x-y
			break;
		case "neg":
			asmCommands = "@SP\n"
					+ "A=M-1\n"					//A=address of y
					+ "M=-M";					//M=-y
			break;
			
		/*
		 * Reference:
		 * culchie(2010, Jan 21) "Logical operations". Message posted to http://nand2tetris-questions-and-answers-forum.32033.n3.nabble.com/Logical-operations-td72625.html
		 */
		case "eq":	
			asmCommands = "@SP\n"
					+ "AM=M-1\n"							//decrements stack pointer and sets A to the address of the value at the top of the stack
					+ "D=M\n"								//D=y
					+ "A=A-1\n"								//A=address of x
					+ "D=M-D\n"								//D=x-y
					+ "M=0\n"								//M=false
					+ "@END." + labelInc +"\n"				//address of unique label
					+ "D;JNE\n"								//if (x!=y)
					+ "@SP\n"
					+ "A=M-1\n"
					+ "M=-1\n"								//else M=true	
					+ "(END." + labelInc++ +")";
			break;
		case "gt":	
			asmCommands = "@SP\n"
					+ "AM=M-1\n"							//decrements stack pointer and sets A to the address of the value at the top of the stack
					+ "D=M\n"								//D=y
					+ "A=A-1\n"								//A=address of x
					+ "D=D-M\n"								//D=y-x
					+ "M=0\n"								//M=false
					+ "@END." + labelInc +"\n"
					+ "D;JGE\n"								//if (x<=0)
					+ "@SP\n"
					+ "A=M-1\n"
					+ "M=-1\n"								//else M=true
					+ "(END." + labelInc++ +")";
			break;
		case "lt":
			asmCommands = "@SP\n"
					+ "AM=M-1\n"							//decrements stack pointer and sets A to the address of the value at the top of the stack
					+ "D=M\n"								//D=y
					+ "A=A-1\n"								//A=address of x
					+ "D=D-M\n"								//D=y-x
					+ "M=0\n"								//M=false
					+ "@END." + labelInc +"\n"
					+ "D;JLE\n"								//if (x>=0)
					+ "@SP\n"
					+ "A=M-1\n"
					+ "M=-1\n"								//else M=true
					+ "(END." + labelInc++ +")";
			break;
		case "and":	
			asmCommands = "@SP\n"
					+ "AM=M-1\n"							//decrements stack pointer and sets A to the address of the value at the top of the stack
					+ "D=M\n"								//D=y
					+ "A=A-1\n"
					+ "M=D&M";								//M=y and x
			break;
		case "or":	
			asmCommands = "@SP\n"
					+ "AM=M-1\n"							//decrements stack pointer and sets A to the address of the value at the top of the stack
					+ "D=M\n"								//D=y
					+ "A=A-1\n"
					+ "M=D|M";								//M=y or x
			break;
		case "not":
			asmCommands = "@SP\n"
					+ "A=M-1\n"								//M=y
					+ "M=!M";								//M=not y
			break;
		}
		
			
		return asmCommands;
		
	}
	
	/*
	 * writePush takes VM push commands determines their segment then translates it to Hack assembly
	 * @param String segment - which array holds the value to be pushed onto the stack
	 * @param int index - the index of the array which holds the value to be pushed onto the stack
	 * @param String vmName - used to give a unique symbol for static commands (@vmName.index)
	 * @return String - the translated code
	 */
	public String writePush (String segment, int index, String vmName) {
		String asmCommands = null;
		
		switch(segment){
		case "constant":
			asmCommands = "@" + index +"\n"					//A=int
					+ "D=A\n"								//D=int
					+ "@SP\n"
					+ "AM=M+1\n"							//increments stack pointer and sets A to the top of the stack
					+ "A=A-1\n"								//A points to top value of the stack (one less than stack pointer)
					+ "M=D";								//M=int
			break;
		case "local":
			asmCommands = "@LCL\n"
					+ "D=M\n"								//D=*(local)
					+ "@" + index + "\n"
					+ "A=D+A\n"								//A=*(local+index)
					+ "D=M\n"								//D=y
					+ "@SP\n"
					+ "AM=M+1\n"							//increments stack pointer and sets A to the top of the stack
					+ "A=A-1\n"								//A points to top value of the stack (one less than stack pointer)
					+ "M=D";								//M=y
			break;
		case "argument":
			asmCommands = "@ARG\n"
					+ "D=M\n"								//D=*(argument)
					+ "@" + index + "\n"
					+ "A=D+A\n"								//A=*(argument+index)
					+ "D=M\n"								//D=y
					+ "@SP\n"
					+ "AM=M+1\n"							//increments stack pointer and sets A to the top of the stack
					+ "A=A-1\n"
					+ "M=D";								//M=y
			break;
		case "this":
			asmCommands = "@THIS\n"
					+ "D=M\n"								//D=*(this)
					+ "@" + index + "\n"
					+ "A=D+A\n"								//A=*(this+index)
					+ "D=M\n"								//D=y
					+ "@SP\n"
					+ "AM=M+1\n"							//increments stack pointer and sets A to the top of the stack
					+ "A=A-1\n"
					+ "M=D";								//M=y
			break;
		case "that":
			asmCommands = "@THAT\n"
					+ "D=M\n"								//D=*(that)
					+ "@" + index + "\n"					
					+ "A=D+A\n"								//A=*(that+index)
					+ "D=M\n"								//D=y
					+ "@SP\n"
					+ "AM=M+1\n"							//increments stack pointer and sets A to the top of the stack
					+ "A=A-1\n"
					+ "M=D";								//M=y
			break;
		case "pointer":
			asmCommands = "@R" + (3 + index) +"\n"			//A=*(pointer+index)
					+ "D=M\n"								//D=y
					+ "@SP\n"
					+ "AM=M+1\n"							//increments stack pointer and sets A to the top of the stack
					+ "A=A-1\n"
					+ "M=D";								//M=y
			break;
		case "temp":
			asmCommands = "@R" + (5 + index) +"\n"			//A=*(temp+index)
					+ "D=M\n"								//D=y
					+ "@SP\n"
					+ "AM=M+1\n"							//increments stack pointer and sets A to the top of the stack
					+ "A=A-1\n"
					+ "M=D";								//M=y
			break;
		case "static":
			asmCommands = "@" + (vmName + "." + index )+ "\n"	//A=*(vmName.index) unique symbol
					+ "D=M\n"									//D=y
					+ "@SP\n"
					+ "AM=M+1\n"								//increments stack pointer and sets A to the top of the stack
					+ "A=A-1\n"
					+ "M=D";									//M=y
			break;
		}
		return asmCommands;
		
	}
	
	/*
	 * writePop pops off the value at the top of the stack and stores it in its appropriate memory location
	 * @param String segment - the array to store the value from the top of the stack
	 * @param int index - the index in the array for the value to be stored
	 * @param String vmName - unique name for symbols for static
	 * @return String - the translated assembly code
	 */
	public String writePop (String segment, int index, String vmName) {
		String asmCommands = null;
		
		switch (segment) {
		case "local":
			asmCommands = "@LCL\n"
					+ "D=M\n"									//D=*(local)
					+ "@" + index + "\n"
					+ "D=D+A\n"									//D=*(local+index)
					+ "@R13\n"
					+ "M=D\n"									//R13=*(local+index)
					+ "@SP\n"
					+ "AM=M-1\n"								//decrements stack pointer and sets A to the address of the value at the top of the stack
					+ "D=M\n"									//D=y
					+ "@R13\n"									
					+ "A=M\n"									//A=*(local +index)
					+ "M=D";									//M=y
			break;
		case "argument":
			asmCommands = "@ARG\n"
					+ "D=M\n"									//D=*(argument)
					+ "@" + index + "\n"
					+ "D=D+A\n"									//D=*(argument+index)
					+ "@R13\n"
					+ "M=D\n"									//R13=*(argument+index)
					+ "@SP\n"
					+ "AM=M-1\n"								//decrements stack pointer and sets A to the address of the value at the top of the stack
					+ "D=M\n"									//D=y
					+ "@R13\n"
					+ "A=M\n"									//A=*(argument+index)
					+ "M=D";									//M=y
			break;
		case "this":
			asmCommands = "@THIS\n"
					+ "D=M\n"									//D=*(this)
					+ "@" + index + "\n"
					+ "D=D+A\n"									//D=*(this+index)
					+ "@R13\n"
					+ "M=D\n"									//R13=*(this+index)
					+ "@SP\n"
					+ "AM=M-1\n"								//decrements stack pointer and sets A to the address of the value at the top of the stack
					+ "D=M\n"									//D=y
					+ "@R13\n"
					+ "A=M\n"									//A=*(this+index)
					+ "M=D";									//M=y
			break;
		case "that":
			asmCommands = "@THAT\n"
					+ "D=M\n"									//D=*(that)
					+ "@" + index + "\n"
					+ "D=D+A\n"									//D=*(that+index)
					+ "@R13\n"
					+ "M=D\n"									//R13=*(that+index)
					+ "@SP\n"
					+ "AM=M-1\n"								//decrements stack pointer and sets A to the address of the value at the top of the stack
					+ "D=M\n"									//D=y
					+ "@R13\n"
					+ "A=M\n"									//A=*(that+index)
					+ "M=D";									//M=y
			break;
		case "pointer":
			asmCommands = "@SP\n"
					+ "AM=M-1\n"								//decrements stack pointer and sets A to the address of the value at the top of the stack
					+ "D=M\n"									//D=y
					+ "@R" + (3 + index) + "\n"					//A=*(pointer+index)
					+ "M=D";									//M=y
			break;
		case "temp":
			asmCommands = "@SP\n"
					+ "AM=M-1\n"								//decrements stack pointer and sets A to the address of the value at the top of the stack
					+ "D=M\n"									//D=y
					+ "@R" + (5 + index) + "\n"					//A=*(temp+index)
					+ "M=D";									//M=y
			break;
		case "static":
			asmCommands = "@SP\n"
					+ "AM=M-1\n"								//decrements stack pointer and sets A to the address of the value at the top of the stack
					+ "D=M\n"									//D=y
					+ "@" + (vmName + "." + index) + "\n"		//A=*(vmName.index) unique symbol
					+ "M=D";									//M=y
		}
		return asmCommands;
		
	}
	
	
	public String writeLabel (String label, String vmName) {
		String asmCommands = "(" + label + "." + vmName + ")\n";
		return asmCommands;
	}
	
	public String writeGoto (String label, String vmName) {
		String asmCommands = "@" + label + "." + vmName + "\n"
				+ "D;JMP\n";
		return asmCommands;
	}
	
	public String writeIf (String label, String vmName) {
		String asmCommands = "@SP\n"
				+ "AM=M-1\n"
				+ "D=M\n"
				+ "@" + label + "." + vmName + "\n"
				+ "D;JNE\n";
		return asmCommands;
	}
	
}
