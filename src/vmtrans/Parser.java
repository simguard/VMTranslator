package vmtrans;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Integer;
import java.util.Scanner;

/*
 * @author Jeffrey White
 */

public class Parser {
	
	ArrayList<String> lines = new ArrayList<>();	//Array list to contain the initial raw instructions
	ArrayList<String> hack = new ArrayList<>();			
	public enum Command {
		C_ARITHMETIC, C_PUSH, C_POP, C_LABEL, C_GOTO, C_IF, C_FUNCTION, C_RETURN, C_CALL
	}


	
	/*
	 * 
	 */
	public Parser(String file) {
		
		BufferedReader read = null;
		
		try {
			read = new BufferedReader(new FileReader(file));
			try {
				String newLine;
				
				//Reads through the file removing comments and empty lines, and stores the result
				//in ArrayList lines.
				while (null != (newLine = read.readLine())) {
					String[] noComment = newLine.split("//");
					if (noComment[0] != null && !noComment[0].isEmpty()) {
						lines.add(noComment[0]);
					}		
				}
			} catch (IOException error) {
				error.printStackTrace();
			}
		} catch (FileNotFoundException errorF) {
			System.err.println("Error opening file.");
		} finally {
			try {
				if (read != null)
					read.close();
			} catch (IOException errorC) {
				errorC.printStackTrace();
			}
		}
		
				
	}
	
	public String Parse (String command) {

		CodeWriter code = new CodeWriter();
		String asmCommand = command;
		
		switch (commandType(command)) {
		case C_ARITHMETIC:
			asmCommand = code.writeArithmetic(command);
			break;
		case C_PUSH:
			String[] commandArgs = command.split("\\s+");
			int index = Integer.parseInt(commandArgs[2]);
			asmCommand = code.writePush(commandArgs[1], index);
			break;
		case C_POP:
			commandArgs = command.split("\\s+");
			index = Integer.parseInt(commandArgs[2]);
			asmCommand = code.writePop(commandArgs[1], index);
			break;
		case C_GOTO:	case C_IF:	
		case C_LABEL:	case C_RETURN:
			break;
		default:
			break;
		}
		
		return asmCommand;
	}


	
	public ArrayList<String> getFileLinesArray () {
		return lines;
	}
	
	public Command commandType(String currentCheck) {
		
		String firstTwoChar = currentCheck.substring(0, 2);
		
		switch (firstTwoChar) {
		case "ad":	case "su":	case "ne":
		case "eq":	case "gt":	case "lt":
		case "an":	case "or":	case "no":
			return Command.C_ARITHMETIC;
		case "pu":
			return Command.C_PUSH;
		case "po":
			return Command.C_POP;
		case "la":
			return Command.C_LABEL;
		case "go":
			return Command.C_GOTO;
		case "if":
			return Command.C_IF;
		case "fu":
			return Command.C_FUNCTION;
		case "ca":
			return Command.C_CALL;
		case "re":
			return Command.C_RETURN;
		default:
			return null;
		}
		
	}
	
	

}