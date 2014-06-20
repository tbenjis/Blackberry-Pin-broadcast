/**
 * Defines ProgressBar object. Creates popup screen with title, and perpetually updating
 * progress gauge. Instantiate and run as thread to start progress update. Call
 * remove() method when finished to remove popup screen and shutdown thread.
 */
package com.stackarena.pinbroadcast;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.GaugeField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.DialogFieldManager;
import net.rim.device.api.ui.container.PopupScreen;

public class ProgressBar extends Thread {

	private int maximum, timeout;

	private boolean useful;

	private PopupScreen popup;

	private GaugeField gaugeField;

	/**
	 * Object constructor
	 * 
	 * @param title
	 *           Text to display on popup area
	 * @param maximum
	 *           Range / width of the gauge field of progress bar
	 * @param timeout
	 *           Milliseconds to pause between updates to progress bar
	 * @see GaugeField
	 * @see Thread
	 * @see PopupScreen
	 */

	public ProgressBar(String title, int maximum, int timeout) {
		this.maximum = maximum;
		this.timeout = timeout;

		DialogFieldManager manager = new DialogFieldManager();

		popup = new PopupScreen(manager);
		gaugeField = new GaugeField(null, 1, maximum, 1, GaugeField.NO_TEXT);

		manager.addCustomField(new LabelField(title));
		manager.addCustomField(gaugeField);
	}

	/**
	 * run() method for starting thread
	 */

	public void run() {
		useful = true;

		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				UiApplication.getUiApplication().pushScreen(popup);
			}
		});

		int iterations = 0;

		while (useful) {
			try {
				Thread.sleep(timeout);
			} catch (Exception e) {
			}

			if (++iterations > maximum)
				iterations = 1;

			gaugeField.setValue(iterations);
		}

		if (popup.isDisplayed()) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					UiApplication.getUiApplication().popScreen(popup);
				}
			});
		}
	}

	/**
	 * Method to shutdown the thread and remove the popup screen
	 *  
	 */

	public synchronized void remove() {
		useful = false;
	}
}