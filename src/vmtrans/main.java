package vmtrans;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class main {


	public static void main(String[] args) {
		

		ArrayList<String> asm = new ArrayList<String>();
		
		asm = callParser(asm, args[0]);
	
		try {
			writeFile(getOutputName(args[0]), asm);
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		}
	
	
	private static ArrayList<String> callParser(ArrayList<String> asm, String fileName) {
		ArrayList<String> file = new ArrayList<String>();
		Parser parser = new Parser(fileName);
	
		file = parser.getFileLinesArray();
		Iterator<String> itr = file.iterator();

		while (itr.hasNext()) {		
			String currentCommand = itr.next();
			//TODO book mentioned being able to read another vm while parsing the first one (possibly next segment?)
			//to add should be able to insert a check here and then call callParser recursively.
			asm.add(parser.Parse(currentCommand));		
		}
		
		return asm;
	}
	
	private static void writeFile(String fileName, ArrayList<String> asm) throws IOException {
		FileWriter writeFile = new FileWriter(fileName);
		Iterator<String> itr = asm.iterator();
		
		
		while (itr.hasNext()) {
			
			String currentLine = itr.next();
			writeFile.write(currentLine + "\n");
		}
		
		writeFile.close();
	}
	
	private static String getOutputName (String fileName) {
		int index = fileName.indexOf('.');
		String outFile = fileName.substring(0, index) + ".asm";
		return outFile;
	}


}
