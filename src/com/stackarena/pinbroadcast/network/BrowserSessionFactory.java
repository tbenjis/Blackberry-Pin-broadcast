package com.stackarena.pinbroadcast.network;
import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.browser.BrowserSession;
import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;

// If you are coding for BlackBerry Device Software 4.3+ and would
// like to check for whether the device has Wi-Fi access, please
// uncomment the following import statement
import net.rim.device.api.system.WLANInfo;

/* There are 5 different browing models available: WAP, BlackBerry
 * Internet Service (BIS), BlackBerry Enterprise Server (BES), WiFi,
 * BlackBerry Unite!
 *
 * (1) BIS: This forces the browser to go through the RIM-hosted BIS
 * (2) WAP: This forces the browser to go through the carrier WAP gateway
 * (3) BES: This forces the browser to go through their corporate BES
 *          (behind their IT firewall) if the user is a corporate user.
 * (4) WiFi: This forces the browser to use the WiFi access point.
 * (5) Unite: This forces the browser to use the Unite gateway.
 */
public class BrowserSessionFactory
{
    // Find all Browser service books...these are used to invoke the web
    // browser with the proper connection service
    private static ServiceBook sb = ServiceBook.getSB();
    private static ServiceRecord[] records =
                       sb.findRecordsByCid("BrowserConfig");

   /*
    * BIS: This forces the browser to go through the RIM-hosted BIS
    *      gateway to access the Internet. Use the BIS Browser if...
    *     - The BIS browser offers a consistent experience across all
    *       networks and does not limit which sites can be accessed,
    *       therefore it should be used whenever possible
    *     - You MUST use the BIS Browser if you are taking the user to a web
    *       page where they may subscribe to special services that are only
    *       available using BIS, such as "Web Signals"
    *
    * @returns BIS BrowserSession
    */
    public static BrowserSession createBISBrowserSession()
    {
        // If there are browser services found - search for the
        // Service Record for the given browserType
        String uid = null;
        int numRecords = records.length;
        for( int i = 0; i < numRecords; i++ )
        {
            ServiceRecord myRecord = records[i];
            ServiceRecordHelper myRecordHelper =
                new ServiceRecordHelper(myRecord);

            if( myRecord.isValid() &&
                !myRecord.isDisabled() &&
                myRecordHelper.getConfigType() ==
                ServiceRecordHelper.SERVICE_RECORD_CONFIG_TYPE_BIS )
            {
                uid = myRecord.getUid();
                break;
            }
        }
        return null == uid ? null : Browser.getSession(uid);
    }

   /*
    * WAP: This forces the browser to go through the carrier WAP gateway
    *      to access the Internet. Only use the WAP Browser if...
    *     - You need to identify the carrier network that the user is coming
    *       from (the WAP gateway will add this info to the header)
    *     - You want to access a site where the user will be able to stream
    *       content using the RTSP protocol
    *
    * @returns WAP BrowserSession
    */
    public static BrowserSession createWAPBrowserSession()
    {
        // If there are browser services found - search for the WAP2
        // Service Record
        String uid = null;
        int numRecords = records.length;
        for( int i = 0; i < numRecords; i++ )
        {
            ServiceRecord myRecord = records[i];
            ServiceRecordHelper myRecordHelper =
                new ServiceRecordHelper(myRecord);
            if( myRecord.isValid() && !myRecord.isDisabled() &&
                (myRecordHelper.getConfigType() ==
                ServiceRecordHelper.SERVICE_RECORD_CONFIG_TYPE_WAP2) &&
                (myRecordHelper.getNavigationType() != -1))
            {
                uid = myRecord.getUid();
                break;
            }
        }
        // If there was no WAP2 Service Record found – search for the
        // WAP Record
        if (uid == null)
        {
            for( int i = 0; i < numRecords; i++ )
            {
                ServiceRecord myRecord = records[i];
                ServiceRecordHelper myRecordHelper =
                    new ServiceRecordHelper(myRecord);
                if( myRecord.isValid() &&
                    !myRecord.isDisabled() &&
                    (myRecordHelper.getConfigType() ==
                    ServiceRecordHelper.SERVICE_RECORD_CONFIG_TYPE_WAP) &&
                    (myRecordHelper.getNavigationType() != -1))
                {
                    uid = myRecord.getUid();
                    break;
                }
            }
        }
        return null == uid ? null : Browser.getSession(uid);
    }

   /*
    * BES: If the user is a corporate user, this forces the browser to
    *          go through their corporate BES (behind their IT firewall).
    *          Only use the BES Browser if...
    *     - The website you are addressing is inside the corporate firewall
    *     - The BIS Browser service is not available
    *
    * @returns BES BrowserSession
    */
    public static BrowserSession createBESBrowserSession()
    {
        // If there are browser services found - search for the BES
        // Service Record
        String uid = null;
        int numRecords = records.length;
        for( int i = 0; i < numRecords; i++ )
        {
            ServiceRecord myRecord = records[i];
            ServiceRecordHelper myRecordHelper =
                new ServiceRecordHelper(myRecord);
            if( myRecord.isValid() &&
                !myRecord.isDisabled() &&
                myRecordHelper.getConfigType() ==
                ServiceRecordHelper.SERVICE_RECORD_CONFIG_TYPE_BES &&
                !myRecord.getName().
                    equals(ServiceRecordHelper.SERVICE_RECORD_NAME_UNITE))
            {
                uid = myRecord.getUid();
                break;
            }
        }
        return null == uid ? null : Browser.getSession(uid);
    }

   /*
    * WiFi: This forces the browser to use the WiFi access point.
    *
    * @returns WiFi BrowserSession
    */
    public static BrowserSession createWiFiBrowserSession()
    {
        String uid = null;

        // Check if the device has wifi access before opening the browser
        // This Check can only be used for BlackBerry Device Software 4.3+.
        // If you are coding for BlackBerry Device Software 4.3+ and would
        // like to check for whether the device has Wi-Fi access, please
        // uncomment the following if condition
        if (WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED)
        {
            //If there are browser services found - search for the
            // WiFi Service Record
            int numRecords = records.length;
            for( int i = 0; i < numRecords; i++ )
            {
                ServiceRecord myRecord = records[i];
                ServiceRecordHelper myRecordHelper =
                    new ServiceRecordHelper(myRecord);
                    
                    System.out.println("valid " + myRecord.isValid());
                    System.out.println("disabled " + myRecord.isDisabled());
                    System.out.println("config type " + myRecordHelper.getConfigType());
                    
                if( myRecord.isValid() &&
                    !myRecord.isDisabled() &&
                    myRecordHelper.getConfigType() ==
                    ServiceRecordHelper.SERVICE_RECORD_CONFIG_TYPE_WIFI )
                {
                    System.out.println("uid " + myRecord.getUid());
                    uid = myRecord.getUid();
                    break;
                }
            }
        }
        return null == uid ? null : Browser.getSession(uid);
    }

   /*
    * Unite: This forces the browser to use the Unite gateway.
    *
    * @returns Unite BrowserSession
    */
    public static BrowserSession createUniteBrowserSession()
    {
        String uid = null;
        // If there are browser services found - search for the
        // Unite Service Record
        int numRecords = records.length;
        for( int i = 0; i < numRecords; i++ )
        {
            ServiceRecord myRecord = records[i];
            ServiceRecordHelper myRecordHelper = new
                ServiceRecordHelper(myRecord);
            if( myRecord.isValid() &&
                !myRecord.isDisabled() &&
                myRecordHelper.getConfigType() ==
                ServiceRecordHelper.SERVICE_RECORD_CONFIG_TYPE_BES &&
                myRecord.getName().equals(
                    ServiceRecordHelper.SERVICE_RECORD_NAME_UNITE))
            {
                uid = myRecord.getUid();
                break;
            }
        }
        return null == uid ? null : Browser.getSession(uid);
    }

   /*
    * Default: This returns a session for the default browser
    *
    * @returns Default BrowserSession
    */
    public static BrowserSession createDefaultBrowserSession()
    {
        return Browser.getDefaultSession();
    }
}
