package vmtrans;

import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {

	public static int labelInc = 0;
	//TODO delete comments
	public CodeWriter() {
	}
	
	
	public String writeArithmetic (String command) {
		String asmCommands = null;
		
		switch (command) {
		case "add":	
			asmCommands = "\n//add starts here \n@SP\n"
					+ "AM=M-1\n"
					+ "D=M\n"
					+ "A=A-1\n"
					+ "M=D+M\n//add ends here\n";
			break;
		case "sub":	
			asmCommands = "\n//sub starts here \n@SP\n"
					+ "AM=M-1\n"
					+ "D=M\n"
					+ "A=A-1\n"
					+ "M=M-D\n//sub ends here\n";
			break;
		case "neg":
			asmCommands = "\n//neg starts here \n@SP\n"
					+ "A=M-1\n"
					+ "M=-M\n//neg ends here\n";
			break;
			
		/*
		 * Reference:
		 * culchie(2010, Jan 21) "Logical operations". Message posted to http://nand2tetris-questions-and-answers-forum.32033.n3.nabble.com/Logical-operations-td72625.html
		 */
		case "eq":	
			asmCommands = "\n//eq starts here\n@SP\n"
					+ "AM=M-1\n"
					+ "D=M\n"
					+ "A=A-1\n"
					+ "D=M-D\n"								//D=x-y
					+ "M=0\n"
					+ "@END." + labelInc +"\n"
					+ "D;JNE\n"
					+ "@SP\n"
					+ "A=M-1\n"
					+ "M=-1\n"
					+ "(END." + labelInc++ +")\n//eq ends here \n";
			break;
		case "gt":	
			asmCommands = "\n//gt starts here\n@SP\n"
					+ "AM=M-1\n"
					+ "D=M\n"
					+ "A=A-1\n"
					+ "D=D-M\n"								//D=y-x
					+ "M=0\n"
					+ "@END." + labelInc +"\n"
					+ "D;JGE\n"
					+ "@SP\n"
					+ "A=M-1\n"
					+ "M=-1\n"
					+ "(END." + labelInc++ +")\n//gt ends here \n";
			break;
		case "lt":
			asmCommands = "\n//lt starts here\n@SP\n"
					+ "AM=M-1\n"
					+ "D=M\n"
					+ "A=A-1\n"
					+ "D=D-M\n"								//D=y-x
					+ "M=0\n"
					+ "@END." + labelInc +"\n"
					+ "D;JLE\n"
					+ "@SP\n"
					+ "A=M-1\n"
					+ "M=-1\n"
					+ "(END." + labelInc++ +")\n//lt ends here \n";
			break;
		case "and":	
			asmCommands = "\n//and starts here \n@SP\n"
					+ "AM=M-1\n"
					+ "D=M\n"
					+ "A=A-1\n"
					+ "M=D&M\n//and ends here\n";
			break;
		case "or":	
			asmCommands = "\n//or starts here \n@SP\n"
					+ "AM=M-1\n"
					+ "D=M\n"
					+ "A=A-1\n"
					+ "M=D|M\n//or ends here\n";
			break;
		case "not":
			asmCommands = "\n//not starts here \n@SP\n"
					+ "A=M-1\n"
					+ "M=!M\n//not ends here\n";
			break;
		}
		
			
		return asmCommands;
		
	}
	
	public String writePush (String segment, int index) {
		String asmCommands = null;
		
		switch(segment){
		case "constant":
			asmCommands = "\n//pushcons start here \n@" + index +"\n"
					+ "D=A\n"
					+ "@SP\n"
					+ "AM=M+1\n"
					+ "A=A-1\n"
					+ "M=D\n//pushcons ends here\n";
			break;
		}
		return asmCommands;
		
	}
	
	public String writePop (String segment, int index) {
		String asmCommands = null;
		
		switch (segment) {
		case "local":
			asmCommands = "\n//poplocal start here \n";
		}
		return segment;
		
	}
	
}
