package com.stackarena.pinbroadcast;

import org.json.me.JSONException;
import org.json.me.JSONObject;
import com.stackarena.pinbroadcast.network.HTTPClient;
import com.stackarena.pinbroadcast.network.WebIcon;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Status;

public class AppNetwork {
	public static String PIN = Integer.toHexString(DeviceInfo.getDeviceId());
	String OS = DeviceInfo.getSoftwareVersion();;
	String details = "";
	String url = "";
	boolean silent = true;
	String verifydetails = "";
	String verifystatus = "";

	/* ***********************************
	 * * Method for all user verification functions
	 * ***********************************
	 */
	Runnable verifyMe = new Runnable() {
		public void run() {
			String vrfy = "";
			try {
				vrfy = HTTPClient.getResponse(PinScreen.SERVERURL
						+ "api/subscription/" + PinScreen.APPID + "/" + PIN
						+ "/" + OS);
			} catch (final Exception e) {
				System.out.println(e.toString());
				// PinScreen.info.setText("Error retrieving upgrade information, check your connection...!");
				// + e.toString());

			}
			verifyJSON(vrfy);
		}
	};

	public void Verification() {
		new Thread(verifyMe).start();
		if (!silent) {
			synchronized (UiApplication.getEventLock()) {
				Status.show("Verifying your account...", 5000);
			}
		}
	}

	// split the verify response and show prompt if error
	public void verifyJSON(String jsonString) {

		try {
			JSONObject json = new JSONObject(jsonString);
			verifydetails = json.getString("details");
			verifystatus = json.getString("status");

		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		finishVerify();
	}

	public void finishVerify() {
		// Start event for verify here...
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {

				if (verifystatus == null || verifystatus == "") {
					// ntwk issue retry activation or continue
					if (!silent) {
						int response = Dialog
								.ask(Dialog.D_YES_NO,
										"Error receiving verification information, please check your connection. \n\nDo you want to retry verification?",
										Dialog.YES);
						if (Dialog.YES == response) {
							// retry the activation
							new Thread(verifyMe).start();

						}
					}

				} else if (verifystatus.equals("1")) // success/registered
				{
					if (!silent) {
						Dialog.alert("Your account has been verified!");
					}
					PinScreen.userActivated = true;
					PinScreen.lblTrial.setText("Expires: " + verifydetails
							+ " days(s)");
					PinScreen.info
							.setText("Account verified. Subscription expiries in "
									+ verifydetails + " days(s)");

				} else if (verifystatus.equals("2")) // success/registered
				{
					int response = Dialog
							.ask(Dialog.D_YES_NO,
									"You have not registered your device for this app. \n"
											+ " \nDo you want to register?",
									Dialog.YES);
					// info.setText(details + " now available.");
					if (Dialog.YES == response) {
						// goto weblink on browser
						WebIcon webIcon = new WebIcon();
						webIcon.launchBrowser(webIcon.BIS_BROWSER,
								verifydetails);
					}
					PinScreen.userActivated = false;

				} else // account not verified display error message
				{
					if (!silent) {
						Dialog.alert("Account Verification Error \n\n"
								+ verifydetails);
					}
					PinScreen.userActivated = false;

				}
			}
		});
	}

	/* ***********************************
	 * Method for all user verification functions
	 * ***********************************
	 */

	/* ***********************************
	 * Method for all update functions ***********************************
	 */
	Runnable updateMe = new Runnable() {
		public void run() {
			String updt = "";
			try {
				updt = HTTPClient.getResponse(PinScreen.SERVERURL
						+ "api/version/" + PinScreen.APPID + "/"
						+ PinScreen.APPVERSION);
			} catch (final Exception e) {
				System.out.println(e.toString());
				// PinScreen.info.setText("Error retrieving upgrade information, check your connection...!");
				// + e.toString());

			}
			upgradeJSON(updt);
		}
	};

	public void UpdateApp() {
		new Thread(updateMe).start();
		if (!silent) {
			synchronized (UiApplication.getEventLock()) {
				Status.show("Searching for Updates...", 5000);
			}
		}
	}

	// split the upgrade response and show prompt if upgrade available
	public void upgradeJSON(String jsonString) {

		try {
			JSONObject json = new JSONObject(jsonString);
			details = json.getString("details");
			url = json.getString("url");

		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		finishUpdate();
	}

	public void finishUpdate() {
		// Start event for update here...
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {

				if (details == null || details == "") {
					if (!silent) {
						Dialog.alert("Error receiving update information, please check your connection.");
					}
					// info.setText("Great! You are using the latest version.");
				} else if (details.equals("0")) {
					if (!silent) {
						Dialog.alert("Great! You are using the latest version.");
					}
				} else {
					int response = Dialog
							.ask(Dialog.D_YES_NO, "Upgrade Available \n"
									+ details + " \nDo you want to download?",
									Dialog.YES);
					// info.setText(details + " now available.");
					if (Dialog.YES == response) {
						// goto weblink on browser
						WebIcon webIcon = new WebIcon();
						webIcon.launchBrowser(webIcon.BIS_BROWSER, url);
					}
				}
			}
		});
	}
	/* ***********************************
	 * Method for all update functions ***********************************
	 */

}
