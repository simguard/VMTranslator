package vmtrans;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Integer;

/*
 * @author Jeffrey White  10/08/2016
 * Parser opens a file to be read, parses it, then sends it to CodeWriter for translation and stores the result in an ArrayList
 */


public class Parser {
	
	ArrayList<String> lines = new ArrayList<>();	//Array list to contain the initial raw instructions
	ArrayList<String> hack = new ArrayList<>();		//holds translated commands 
	public enum Command {
		C_ARITHMETIC, C_PUSH, C_POP, C_LABEL, C_GOTO, C_IF, C_FUNCTION, C_RETURN, C_CALL
	}


	
	/*
	 * Constructor opens a file to be read and removes any comments or empty lines and stores the result in an ArrayList
	 * @param String file -  name of file to be read
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
	
	/*
	 * parse parses a command, determines its type and sends the appropriate information to CodeWriter for translation
	 * @param String command - the command to be parsed
	 * @param String vmName - the name of the file for static purposes
	 * @return String - the translated assembly language 
	 */
	public String parse (String command, String vmName) {

		CodeWriter code = new CodeWriter();
		vmName = removePath(vmName);
		String asmCommand = command;
		
		switch (commandType(command)) {
		case C_ARITHMETIC:
			asmCommand = code.writeArithmetic(command);
			break;
		case C_PUSH:
			String[] commandArgs = command.split("\\s+");
			int index = Integer.parseInt(commandArgs[2]);
			asmCommand = code.writePush(commandArgs[1], index, vmName);
			break;
		case C_POP:
			commandArgs = command.split("\\s+");
			index = Integer.parseInt(commandArgs[2]);
			asmCommand = code.writePop(commandArgs[1], index, vmName);
			break;
		case C_GOTO:
			commandArgs = command.split("\\s+");
			asmCommand = code.writeGoto(commandArgs[1], vmName);
			break;
		case C_IF:
			commandArgs = command.split("\\s+");
			asmCommand = code.writeIf(commandArgs[1], vmName);
			break;
		case C_LABEL:
			commandArgs = command.split("\\s+");
			asmCommand = code.writeLabel(commandArgs[1], vmName);
			break;
		case C_RETURN:
			break;
		default:
			break;
		}
		
		return asmCommand;
	}


	/*
	 * getFileLinesArray simply returns the ArrayList containing the untranslated commands
	 * @return ArrayList<String> - ArrayList of VM commands
	 */
	public ArrayList<String> getFileLinesArray () {
		return lines;
	}
	
	/*
	 * commandType determines the type of command and returns it as an enum
	 * @param String currentCheck - the current command to check
	 * @return Command- the enum for the current command being checked 
	 */
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
	
	/*
	 * removePath removes the path from the String and returns only the VM name
	 * @param String vmName - the name to check for removing files
	 * @return vmName - the VM name with no path
	 */
	private String removePath (String vmName) {
		int index = 0;
		index = vmName.lastIndexOf("/");
		index = vmName.lastIndexOf("\\");
		if (index > 0) {
			vmName = vmName.substring(index+1);
		}
		return vmName;
	}
	

}