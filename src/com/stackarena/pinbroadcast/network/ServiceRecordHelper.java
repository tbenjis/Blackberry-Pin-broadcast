package com.stackarena.pinbroadcast.network;

import java.io.EOFException;
import net.rim.device.api.synchronization.ConverterUtilities;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.util.DataBuffer;

/**
 * ServiceRecordHelper is a utility class for parsing the data in the service books 
 */  
public class ServiceRecordHelper
{
    private static final int SERVICE_RECORD_NAVIGATION_TYPE = 7;
    private static final int SERVICE_RECORD_CONFIG_TYPE = 12;
    
    public static final int SERVICE_RECORD_CONFIG_TYPE_WAP  = 0;
    public static final int SERVICE_RECORD_CONFIG_TYPE_BES  = 1;
    public static final int SERVICE_RECORD_CONFIG_TYPE_WIFI = 2;
    public static final int SERVICE_RECORD_CONFIG_TYPE_BIS  = 4;
    public static final int SERVICE_RECORD_CONFIG_TYPE_WAP2 = 7;
        
    public static final String SERVICE_RECORD_NAME_UNITE = "Unite";
        
    private ServiceRecord record = null;
       
    /**
     * Constructs the ServiceRecordHelper with a ServiceRecord object
     * @param record ServiceRecord
     */
    public ServiceRecordHelper(ServiceRecord record)
    {
        this.record = record;
    }
        
    /**
     * This method will return the config type of the service record.
     * This information is contained in the service records application data (12th field)
     *
     * 
     * @return  configType
     */
    public int getConfigType()
    {
        return parseInt(getDataBuffer(SERVICE_RECORD_CONFIG_TYPE));
    }

        
    /**
     * This method will return the navigation type of the service record.  
     * This information is contained in the service records application data (7th field).
     * The navigation type indicates whether or not the user can navigate to other 
     * web pages from within the browser instance.
     * 
     * @return  navigationType
     */
    public int getNavigationType()
    {
        return parseInt(getDataBuffer(SERVICE_RECORD_NAVIGATION_TYPE));
    }

    /**
     * Converts the data buffer for the given type to an int value 
     * to the given type 
     * 
     * @param DataBuffer
     * @return data buffer as an int or -1 if the data buffer is null or can't be read
     */
    private int parseInt(DataBuffer buffer)
    {
        if (buffer != null)
        {
            try 
            {
                return ConverterUtilities.readInt(buffer);
            } 
            catch (EOFException e) 
            {
                return -1;
            }
         }
         
         return -1;
    }
    
    /**
     * Returns a data buffer from the ServiceRecord based on the given type  
     * 
     * @param type data type
     * @return Corresponding data buffer
     */
    private DataBuffer getDataBuffer(int type) 
    {
        DataBuffer buffer = null;
        byte[] data = record.getApplicationData();
        
        if (data != null) 
        {
            buffer = new DataBuffer(data, 0, data.length, true);
            
            try 
            {
                buffer.readByte();
            } 
            catch (EOFException e1) 
            {
                buffer = null;
            }
            
            if (!ConverterUtilities.findType(buffer, type)) 
            {
                 buffer = null;
            }
        }
        
        return buffer;
    }
}
 
