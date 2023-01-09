package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @class 	CSV
 * @author 	Keshav Saharia
 * @desc 	Reads NFL NGS data from a CSV file, and provides an abstraction layer for processing row-by-row
 * 			data into in-memory data structures by a corresponding subclass.
 */
public abstract class CSV {
	private File file;
	protected int columns = 0;
	
	public CSV(String name) {
		this.setFile(name);
	}
	
	protected void setFile(String name) {
		this.file = new File(name + ".csv");
	}
	
	protected abstract void process(String[] row);
	
	protected void read() {
		try {
			Scanner scanner = new Scanner(file);
			String[] header = CSV.parseHeader(scanner.nextLine());
			this.columns = header.length;
			
			while (scanner.hasNextLine())
				this.process(CSV.parse(scanner.nextLine(), header.length));
			
			scanner.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Could not find file " + file.getAbsolutePath());
		}
	}
	
	private static String[] parseHeader(String line) {
		String[] header = line.split(",");
		for (int i = 0 ; i < header.length ; i++) {
			header[i] = header[i].substring(1, header[i].length() -1);
		}
		return header;
	}

	private static String[] parse(String line, int columns) {
		String[] column = new String[columns];
		
		int start = 0, end = 0, index = 0;
		
		while (end < line.length()) {
			// Detect column overflow
			if (index >= column.length) {
				System.out.println("Invalid column size");
				System.out.println(line);
				break;
			}
			// Empty
			if (line.charAt(start) == ',') {
				index++;
				end = ++start;
			}
			// Quoted string
			else if (line.charAt(start) == '"') {
				end = ++start;
				
				while (line.charAt(end) != '"') {
					end++;
					if (line.charAt(end) == '\\' && line.charAt(end + 1) == '"') {
						end += 2;
					}
				}
				
				column[index] = line.substring(start, end);
				// Skip last quote and possible comma
				end += 2;
				start = end;
				index++;
			}
			// Keep advancing to next comma
			else {
				end = start + 1;
				while (end < line.length() && line.charAt(end) != ',') {
					end++;
				}
				column[index] = line.substring(start, end);
				index++;
				start = end + 1;
				end = start;
			}
			// 
		}
		
		// Detect column underflow
		if (index < column.length) {
			System.out.println("Missing column");
			System.out.println(line);
		}
		
		return column;
	}
	
	public static double parseDouble(String cell) {
		if (cell == null)
			return 0;
		try {
			return Double.parseDouble(cell);
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	public static short parseShort(String number) {
		try {
			if (number == null || number.equals("NA"))
				return 0;
			return Short.parseShort(number);
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	public static String parseString(String value) {
		if (value == null || value.equals("NA"))
			return null;
		return value;
	}
}
