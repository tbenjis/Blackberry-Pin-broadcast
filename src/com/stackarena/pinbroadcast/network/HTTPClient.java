package com.stackarena.pinbroadcast.network;

import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import com.stackarena.pinbroadcast.AppFunctions;
import com.stackarena.pinbroadcast.PinScreen;

import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.WLANInfo;
import net.rim.device.api.ui.UiApplication;

public class HTTPClient  {
	public static String getResponse(String url ) {
		String response = "";
	
		try {
			 synchronized(UiApplication.getEventLock()){
			//ChatBotScr.info.setText("Connecting to 9jawap server...");
			 }
			StreamConnection s = (StreamConnection)Connector.open(url+getConnectionString());

			InputStream input = s.openInputStream();
			 synchronized(UiApplication.getEventLock()){
			//ChatBotScr.info.setText("Retrieving data...");
			 }
			byte[] data = new byte[256];
			int len = 0;
			StringBuffer raw = new StringBuffer();
			
			while( -1 != (len = input.read(data))) {
				raw.append(new String(data, 0, len));
			}
			
			response = raw.toString();
			 synchronized(UiApplication.getEventLock()){
			//ChatBotScr.info.setText("Getting response from Chat Girl...");
			 }
			input.close();
			s.close();
			
		} catch(Exception e) {
			 synchronized(UiApplication.getEventLock()){
				 //PinScreen.info.setText("Error Connecting check connection...");
			 }
		}

		return response;
	}
	
	public static String getConnectionString()
	{ 
	    // This code is based on the connection code developed by Mike Nelson of AccelGolf.
	    String connectionString = null;               
	 
	    // Simulator behavior is controlled by the USE_MDS_IN_SIMULATOR variable.
	    if(DeviceInfo.isSimulator())
	    {
	    	 synchronized(UiApplication.getEventLock()){
	                    logMessage("Device is a simulator and USE_MDS_IN_SIMULATOR is true");
	                    PinScreen.info.setText("Device connected to network.");
	                    connectionString = ";deviceside=false";
	                    AppFunctions.ntwkCon = true;
	    	 }
	           
	    }                                       
	 
	    // Wifi is the preferred transmission method
	    else if(WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED)
	    {
	    	 synchronized(UiApplication.getEventLock()){
	    		 AppFunctions.ntwkCon = true;
	        logMessage("Device is connected via Wifi.");
	        PinScreen.info.setText("Device is connected via Wifi.");
	        connectionString = ";interface=wifi";
	    	 }
	    }
	 
	    // Is the carrier network the only way to connect?
	    else if((CoverageInfo.getCoverageStatus() & CoverageInfo.COVERAGE_DIRECT) == CoverageInfo.COVERAGE_DIRECT)
	    { synchronized(UiApplication.getEventLock()){
	        logMessage("Carrier coverage.");
	        //ChatBotScr.info.setText("Carrier coverage... Connecting...");
	    }
	        String carrierUid = getCarrierBIBSUid();
	        if(carrierUid == null)
	        { synchronized(UiApplication.getEventLock()){
	            // Has carrier coverage, but not BIBS.  So use the carrier's TCP network
	            logMessage("No Uid");
	           // ChatBotScr.info.setText("No BIS... Connecting through TCP...");
	            connectionString = ";deviceside=true";
	        }
	        }
	        else
	        { synchronized(UiApplication.getEventLock()){
	            // otherwise, use the Uid to construct a valid carrier BIBS request
	            logMessage("uid is: " + carrierUid);
	            PinScreen.info.setText("Connected to BIS");
	            AppFunctions.ntwkCon = true;
	            connectionString = ";deviceside=false;connectionUID="+carrierUid + ";ConnectionType=mds-public";
	        }
	        }
	    }               
	 
	    // Check for an MDS connection instead (BlackBerry Enterprise Server)
	    else if((CoverageInfo.getCoverageStatus() & CoverageInfo.COVERAGE_MDS) == CoverageInfo.COVERAGE_MDS)
	    { synchronized(UiApplication.getEventLock()){
	        logMessage("MDS coverage found");
	        PinScreen.info.setText("MDS coverage found Connecting");
	        AppFunctions.ntwkCon = true;
	        connectionString = ";deviceside=false";
	    }
	    }
	 
	    // If there is no connection available abort to avoid bugging the user unnecssarily.
	    else if(CoverageInfo.getCoverageStatus() == CoverageInfo.COVERAGE_NONE)
	    { synchronized(UiApplication.getEventLock()){
	        logMessage("There are no available connection.");
	        PinScreen.info.setText("Your device is not connected to BIS");
	        AppFunctions.ntwkCon = false;
	    }
	    }
	 
	    // In theory, all bases are covered so this shouldn't be reachable.
	    else
	    { synchronized(UiApplication.getEventLock()){
	        logMessage("no other options found, assuming device.");
	        //ChatBotScr.info.setText("no connection options found, assuming device....");
	        connectionString = ";deviceside=true";
	    }
	    }       
	
	    return connectionString;
	}
	
	 
	private static void logMessage(String string) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Looks through the phone's service book for a carrier provided BIBS network
	 * @return The uid used to connect to that network.
	 */
	private static String getCarrierBIBSUid()
	{
	    ServiceRecord[] records = ServiceBook.getSB().getRecords();
	    int currentRecord;
	 
	    for(currentRecord = 0; currentRecord < records.length; currentRecord++)         {             if(records[currentRecord].getCid().toLowerCase().equals("ippp"))             {                 if(records[currentRecord].getName().toLowerCase().indexOf("bibs") >= 0)
	            {
	                return records[currentRecord].getUid();
	            }
	        }
	    }
	 
	    return null;
	}
}
