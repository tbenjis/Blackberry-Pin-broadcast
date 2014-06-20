package com.stackarena.pinbroadcast.network;


import net.rim.blackberry.api.browser.BrowserSession;

/**
 * This class allows easy access for launching a browser for devices
 * pre-software 4.0.
 *
 * To create your own web icon application you need to complete the
 * following 4 steps which are further deteailed in the accompanying
 * documentation:
 * 1. Create a new class which extends this WebIcon class
 *    eg.
 *      // package com.acme.blackberry.webicon;
 *      class BlackBerryWebIcon extends WebIcon
 *      {
 *          public static void main(String[] args)
 *          {
 *              BlackBerryWebIcon webIcon = new BlackBerryWebIcon();
 *              webIconlaunchBrowser(BIS_BROWSER, "http://www.blackberry.com");
 *              webIcon.close();
 *          }
 *      }
 * 2. Change the package name on all classes to your own name
 * 3. Set the title of the application to your own title
 * 4. Remove the sampleIcon.png from the project and add your own image icon
 *
 */
public class WebIcon 
{
    public static final int DEFAULT_BROWSER     = 0;
    public static final int WAP_BROWSER         = 1;
    public static final int BES_BROWSER         = 2;
    public static final int WIFI_BROWSER        = 3;
    public static final int BIS_BROWSER         = 4;
    public static final int UNITE_BROWSER       = 5;

   /*
    * Attempts to launch the given browser to the given url.
    * If launching this browser is not successful then the default browser
    * will be launched.
    *
    * @param int browserType
    * @param String url
    */
    public void launchBrowser(int browserType, String url)
    {
        BrowserSession browserSession = createBrowserSession(browserType);
        browserSession.displayPage(url);
        browserSession.showBrowser();
    }

   /*
    * Attempts to create a browser session of a specific type given.
    * If the system is unable to create the given browser session then
    * the default browser will be launched.
    *
    * @param int browserType
    * @return BrowserSession
    */
    private BrowserSession createBrowserSession(int browserType)
    {
        BrowserSession browserSession = null;

        switch (browserType)
        {
            case BIS_BROWSER:
            {
                browserSession = BrowserSessionFactory.createBISBrowserSession();
                break;
            }
            case WAP_BROWSER:
            {
                browserSession = BrowserSessionFactory.createWAPBrowserSession();
                break;
            }
            case BES_BROWSER:
            {
                browserSession = BrowserSessionFactory.createBESBrowserSession();
                break;
            }
            case WIFI_BROWSER:
            {
                browserSession = BrowserSessionFactory.createWiFiBrowserSession();
                break;
            }
            case UNITE_BROWSER:
            {
                browserSession = BrowserSessionFactory.createUniteBrowserSession();
                break;
            }
        }

        return null == browserSession ?
            BrowserSessionFactory.createDefaultBrowserSession() : browserSession;
    }


}
