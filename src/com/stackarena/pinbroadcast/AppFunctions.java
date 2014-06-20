package com.stackarena.pinbroadcast;

import javax.microedition.global.Formatter;

import net.rim.device.api.ui.UiApplication;

public class AppFunctions {
	public static boolean ntwkCon = false;
	public static int trialCount = 0; // the current sent contacts number
	public static int MAX_TRIAL = 500; // maximum contact that u can send msg to on
								// trial
	
	public static String APPID = "100";
	public static String APPVERSION = "2.0";
	public static String SERVERURL = "http://apps.stackarena.com/";
	public static boolean ALLOW_REPLY = false;
	
	public AppFunctions(){}
	// update trial file
	public static void trialUpdate(final int trialVal) {
		// update trial text number too
		Runnable trlupdt = new Runnable() {
			public void run() {
				try {
				} catch (final Exception e) {
				}
				FileStuffs fs = new FileStuffs();
				fs.updateTrialFile(trialVal);
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						//PinScreen.lblTrial.setText("Messages Left: " + lft);
					}
				});
			}
		};
		// start the thread
		new Thread(trlupdt).start();

	}
	
	public static String formatNumber(double number, int decimals,
			String digitGrouping) {
		Formatter f = new Formatter("en");
		String rawNumber = f.formatNumber(number, decimals + 1);

		String rawIntString = rawNumber.substring(0, rawNumber.indexOf(".")); // Basically
																				// intString
																				// without
																				// digit
																				// grouping
		StringBuffer intString = new StringBuffer();
		StringBuffer decString = new StringBuffer(rawNumber.substring(rawNumber
				.indexOf(".") + 1));
		StringBuffer formattedNumber = new StringBuffer();
		int workingVal = 0;
		int newNum = 0;
		boolean roundNext;

		// Add digit grouping
		int grouplen = 0;
		int firstDigit;
		if (rawIntString.charAt(0) == '-') {
			firstDigit = 1;
		} else {
			firstDigit = 0;
		}
		for (int n = rawIntString.length() - 1; n >= firstDigit; n--) {
			intString.insert(0, rawIntString.substring(n, n + 1));
			grouplen++;
			if (grouplen == 3 && n > firstDigit) {
				intString.insert(0, digitGrouping);
				grouplen = 0;
			}
		}

		// First, check the last digit
		workingVal = Integer.parseInt(String.valueOf(decString.charAt(decString
				.length() - 1)));
		if (workingVal >= 5) {
			roundNext = true;
		} else {
			roundNext = false;
		}
		// Get the decimal values, round if needed, and add to formatted string
		// buffer
		for (int n = decString.length() - 2; n >= 0; n--) {
			workingVal = Integer.parseInt(String.valueOf(decString.charAt(n)));
			if (roundNext == true) {
				newNum = workingVal + 1;
				if (newNum == 10) {
					roundNext = true;
					newNum = 0;
				} else {
					roundNext = false;
				}
				formattedNumber.insert(0, newNum);
			} else {
				formattedNumber.insert(0, workingVal);
			}
		}
		// Now get the integer values, round if needed, and add to formatted
		// string buffer
		formattedNumber.insert(0, ".");
		for (int n = intString.length() - 1; n >= 0; n--) {
			try {
				workingVal = Integer.parseInt(String.valueOf(intString
						.charAt(n)));
			} catch (Exception e) {
				formattedNumber.insert(0, intString.charAt(n));
				continue;
			}
			if (roundNext == true) {
				newNum = workingVal + 1;
				if (newNum == 10) {
					roundNext = true;
					newNum = 0;
				} else {
					roundNext = false;
				}
				formattedNumber.insert(0, newNum);
			} else {
				formattedNumber.insert(0, workingVal);
			}
		}

		// Just in case its a number like 9999.99999 (if it rounds right to the
		// end
		if (roundNext == true) {
			formattedNumber.insert(0, 1);

		}

		// re-add the minus sign if needed
		if (firstDigit == 1)
			formattedNumber.insert(0, rawIntString.charAt(0));

		if (digitGrouping.length() > 0) {
			if (formattedNumber.toString().indexOf(".") == -1) {
				// no decimal
				if (formattedNumber.toString().indexOf(digitGrouping) > 3 + firstDigit) {
					formattedNumber.insert(1 + firstDigit, digitGrouping);
				}

				if (formattedNumber.toString().length() == 4 + firstDigit) {
					formattedNumber.insert(1 + firstDigit, digitGrouping);
				}
			} else {
				// no decimal
				if (formattedNumber.toString().indexOf(digitGrouping) > 3 + firstDigit) {
					formattedNumber.insert(1 + firstDigit, digitGrouping);
				}

				String intportion = formattedNumber.toString().substring(0,
						formattedNumber.toString().indexOf("."));
				if (intportion.length() == 4 + firstDigit) {
					formattedNumber.insert(1 + firstDigit, digitGrouping);
				}
			}
		}

		// now remove trailing zeros
		String tmp = formattedNumber.toString();
		int newLength = tmp.length();
		for (int n = tmp.length() - 1; n >= 0; n--) {
			if (tmp.substring(n, n + 1).equalsIgnoreCase("0")) {
				newLength--;
			} else {
				if (tmp.substring(n, n + 1).equalsIgnoreCase("."))
					newLength--;
				break;
			}
		}
		formattedNumber.setLength(newLength);

		return formattedNumber.toString();
	}
	
}
