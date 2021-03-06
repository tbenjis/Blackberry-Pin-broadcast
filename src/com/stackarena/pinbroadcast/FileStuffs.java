package com.stackarena.pinbroadcast;

import java.io.*;

import javax.microedition.io.Connector;
import javax.microedition.io.file.*;
import com.stackarena.pinbroadcast.PinSender;
import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.io.LineReader;

public class FileStuffs {
	String fileResponse = "";
	boolean verifypin;
	int countpin = 0;
	int badpins = 0;
	long filesize = 0;
	static String fileName = "file:///store/home/user/system/sys_.rim";
	static String dirName = "file:///store/home/user/system/";

	public boolean getTrialFile( ) {
		
		try {
			FileConnection fileConnection = (FileConnection) Connector.open(fileName);
			if (!fileConnection.exists()) {
				return false;
			}
			
			fileConnection.close();
		} catch (IOException e) {
			e.printStackTrace();			
			return false;
		}
		return true;
	}
	

	public boolean createTrialFile( ) {
		
		try {
			FileConnection fileConnection = (FileConnection) Connector.open(
					fileName,
					Connector.READ_WRITE);
			if (!fileConnection.exists()) {
				fileConnection.create();
			}
			fileConnection.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public  void updateTrialFile(int trialVal) {
		
		FileConnection fconn = null ;
		OutputStream os = null ;
		try {
			//create directory
			createDIR();
			fconn = (FileConnection) Connector
					.open(fileName, Connector.READ_WRITE);
			if (!fconn.exists()) {
				fconn.create();
			}
			fconn.truncate(0);
			os = fconn.openOutputStream();
			String data = trialVal+"";
			os.write(data.getBytes());
		} catch (Exception e) {
			System.out.println("Output file error: " + e.getMessage());
		} finally {
			try {
				os.close();
				fconn.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

		}
	}

	private void createDIR()
	{
		FileConnection dir;
		try {
		    dir = (FileConnection)Connector.open(dirName, Connector.READ_WRITE);
		    if (!dir.exists()){             
		        dir.mkdir();    
		    }
		    dir.close();    

		} catch (IOException e) {
		    System.out.println(e.getMessage());
		}
	}
	public  String getTrialFileContent() {
	
		byte[] data = null;
		FileConnection fconn = null;
		DataInputStream is = null;
		try {
			fconn = (FileConnection) Connector.open(fileName, Connector.READ);
			is = fconn.openDataInputStream();
			data = IOUtilities.streamToBytes(is);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (null != is)
					is.close();
					fconn.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		String outputData = new String(data);
		return outputData;
	}
		

	public String ReadFile(String filename) throws IOException {

		// Get an input stream from the file.
		FileConnection fc = (FileConnection) Connector.open(filename);
		boolean bFileExists = fc.exists();
		if (bFileExists) {
			filesize = fc.fileSize();
			// System.err.println(filesize);
		}
		if (bFileExists && filesize < 319488) {

			InputStream stream = fc.openInputStream();
			LineReader lineReader = new LineReader(stream);

			// We read data from input stream one line at a time until we
			// reach end of file. Each line is parsed to extract data.
			for (;;) {
				try {
					String line = new String(lineReader.readLine());
					// verify each line
					PinSender ps = new PinSender();
					verifypin = ps.VerifyPin(line);
					if (verifypin == false) {
						// record number of bad pins
						badpins++;
						continue;
					}

					// store the contact pins in an array
					PinScreen.ContactArray.addElement(line);
					// count the number of pins
					countpin++;

				} catch (EOFException eof) {
					// We've reached the end of the file.
					fileResponse = countpin + " contacts selected, " + badpins
							+ " invalid PINS";
					// System.out.println(fileResponse);
					break;
				} catch (IOException ioe) {
					// Error reading data from file
					fileResponse = "An error occured while reading file.";
				}
			}
			PinScreen.info.setText("Contacts selected!");
			if (countpin < 1) {
				PinScreen.FileSel = null;
				PinScreen.info.setText("No Contacts selected!");
				PinScreen.ContactArray.removeAllElements();
			}
			fc.close();
			System.out.println(PinScreen.ContactArray.capacity());
			System.out.println(PinScreen.ContactArray.size());
		} else {
			fileResponse = "Please select a file less than 300KB";
		}

		return fileResponse;

	}
}
