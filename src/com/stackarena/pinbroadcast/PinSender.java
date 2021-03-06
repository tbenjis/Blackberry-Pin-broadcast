package com.stackarena.pinbroadcast;
import net.rim.blackberry.api.mail.*;

public class PinSender {
	String myPIN;
	String myName;
	public boolean VerifyPin(String pin)
	{
		pin = pin.trim();
		if (pin.length() == 8)
		{
		  boolean ret;
	        try {
	            // try to parse the string to an integer, using 16 as radix
	            int t = Integer.parseInt(pin, 16);
	            // parsing succeeded, string is valid hex number
	            ret = true;
	        } catch (NumberFormatException e) {
	            // parsing failed, string is not a valid hex number
	            ret = false;
	        }
	        return ret;
		}
		return false;
	}

	public boolean SendMessage(String[] pins, String subject, String message)
	{
		 //Store store = Session.getDefaultInstance().getStore();
	      //retrieve the sent folder
	     // Folder[] folders = store.list(Folder.SENT);
	     // Folder sentfolder = folders[0];
	      //create a new message and dont store it
	      Message msg = new Message();
	      PINAddress recipients[] = new PINAddress[pins.length];
	     // String[] pins={"27AE1C90", "21EE4D94", "27AE1C90", "2686432E", "26E95A13"};
	      try{
	      //create a pin address with destination address of 20000000
	    	  
	    	  for(int i =0;i<pins.length;i++)
	    	  {
	    		  recipients[i]= new PINAddress(pins[i], "user"+i);
	    	  }
	      }
	      catch (AddressException ae)
	      {
	    	  //invalid pin address
	    	  System.err.println(ae);
	    	  return false;
	      }
	      try{
	    	  //add sender FROM
	    	  PINAddress frm = new PINAddress(AppNetwork.PIN, PinScreen.from.getText());
		      msg.setFrom(frm);
	    	 
		      //add the recipient list to the message
		      msg.addRecipients(Message.RecipientType.BCC, recipients);
		      //set a subject for the message
		      msg.setSubject(subject);
		      //sets the body of the message
		      msg.setContent(message);
		      
		    //set the reply address to null if no reply is checked
		      if (AppFunctions.ALLOW_REPLY){
		       myPIN = AppNetwork.PIN;
		       myName = PinScreen.from.getText();
		      }else{
		    	   myPIN = "00000000";
		    	   myName = "No Reply"; 
		      }
		      PINAddress pa = new PINAddress(myPIN, myName);
		      Address[] rplAdd = {pa};
		      msg.setReplyTo(rplAdd);
		      
		      //send the message
		      Transport.send(msg);
		    
		      //display the result
		      return true;
	      }
	      catch (MessagingException me)
	      {
	    	  //error formed in the message or send error
		      System.err.println(me);
		      return false;
	      }
	}
}
