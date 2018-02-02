package com.edinarobotics.utils.log;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Logging {

	private PrintWriter writer;
	private String filename;
	
	public Logging(String name) {
		
		filename = name;
		
		try {
			writer = new PrintWriter(new FileWriter("/home/lvuser/" + filename + ".csv"), true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//"/home/lvuser/Output6.txt"
	}
	
	public void log(String whatToWrite) {
		
		try {
			writer.println(whatToWrite);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void close() {
		try {
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
