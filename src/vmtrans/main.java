package vmtrans;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/*
 * @author Jeffrey White 10/08/2016
 * Main gets the file name(s), then sends it to Parser to open and parse, which sends it to CodeWriter to translate to Hack assembly, and then writes it out to a ".asm" file of the same name.
 */

public class Main {

	/*
	 * main calls Parser and writes to a ".asm" file
	 * @param String args - command line argument for the file name(s) to translate
	 */
	public static void main(String[] args) {
		
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept (File dir, String name) {
				if (name.endsWith(".vm")) {
					return true;
				} else {
					return false;
				}
			}
		};
		
		for (int i=0; i<args.length; i++) {
			File clInput = new File(args[i]);
			FileWriter writeFile = null;
		
			if (clInput.isDirectory()) {
				
				File[] filePaths = clInput.listFiles(filter);

				
				
				try {
					writeFile = new FileWriter(clInput.toString() + ".asm");

			
				
					for (int j=0; j<filePaths.length; j++){
						ArrayList<String> asm = new ArrayList<String>();
						asm = callParser(filePaths[i].toString());
						try {
							writeFile(writeFile, asm);
						}catch (IOException er){
							er.printStackTrace();
						}
					}
					
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						writeFile.close();
					} catch (IOException err) {
						// TODO Auto-generated catch block
						err.printStackTrace();
					}
				}
			} else {
				
				String file = clInput.toString();
				if (file.endsWith(".vm")) {
					try {
						writeFile = new FileWriter(getOutputName(file) + ".asm");
						ArrayList<String> asm = new ArrayList<String>();
						asm = callParser(file);
						try {
							writeFile(writeFile, asm);
						}catch (IOException er){
							er.printStackTrace();
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							writeFile.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
		

							
	
	
	/*
	 * callParser creates a parser that opens the file to translate, which parses the file and returns it translated to assembly
	 * @param String fileName - the name of the file for static purposes while translating
	 * @return ArrayList<String> - the translated code
	 */
	private static ArrayList<String> callParser(String fileName) {
		ArrayList<String> asm = new ArrayList<String>();
		ArrayList<String> file = new ArrayList<String>();
		Parser parser = new Parser(fileName);
	
		file = parser.getFileLinesArray();
		Iterator<String> itr = file.iterator();

		while (itr.hasNext()) {		
			String currentCommand = itr.next();
			asm.add(parser.parse(currentCommand, getOutputName(fileName)));		
		}
		
		return asm;
	}
	
	/*
	 * writeFile writes the translated code out to a ".asm" file of the same name
	 * @param String fileName - the file's name to be created
	 * @param ArrayList<String> - code to write to the file
	 */
	private static void writeFile(FileWriter file, ArrayList<String> asm) throws IOException {
		Iterator<String> itr = asm.iterator();
		
		
		while (itr.hasNext()) {
			
			String currentLine = itr.next();
			file.write(currentLine + "\n");
		}
		
		
	}
	
	/*
	 * getOutputName returns a file's name without it's extension
	 * @param String fileName - the file's name to parse
	 * @return String - the file's name without it's extension
	 */
	private static String getOutputName (String fileName) {
		int index = 0;
		index = fileName.indexOf('.');
		if (index > 0) {
			fileName = fileName.substring(0, index);
		}

		return fileName;
	}


}
