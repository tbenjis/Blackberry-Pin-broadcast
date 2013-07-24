package com.stackarena.pinbroadcast;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.io.*;
import javax.microedition.io.file.*;
import javax.microedition.lcdui.Font;
import net.rim.device.api.ui.picker.*;
import com.blackberry.toolkit.ui.container.NegativeMarginVerticalFieldManager;
import com.stackarena.pinbroadcast.CustomTextBox;
import com.stackarena.pinbroadcast.TableLayoutManager;
import com.stackarena.pinbroadcast.HeaderField;

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.UiEngineInstance;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.image.Image;
import net.rim.device.api.ui.image.ImageFactory;
import com.stackarena.pinbroadcast.AppNetwork;
import com.stackarena.pinbroadcast.FileStuffs;
import com.stackarena.pinbroadcast.network.HTTPClient;
import com.stackarena.pinbroadcast.network.WebIcon;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class PinScreen extends MainScreen implements FilePicker.Listener,
		FieldChangeListener {
	FieldChangeListener listener;
	ButtonField btnSend;
	public static LabelField info;
	public static String FileSel = null;
	public static Vector ContactArray = new Vector();
	LabelField lblSubject;
	LabelField lblMessage;
	LabelField lblTo;
	LabelField lblFrom;
	public static LabelField lblTrial;
	VerticalFieldManager _manager;
	CustomTextBoxML msgs;
	CustomTextBox subj;
	public static CustomTextBox from;
	NegativeMarginVerticalFieldManager negManager;
	private FilePicker _filePicker;
	private ButtonField _buttonField;
	private LabelField _labelField;
	ProgressBar pb;
	String message;
	String subject;
	public static boolean ntwkCon = false;
	public static boolean userActivated = false;
	public int trialCount = 0; // the current sent contacts number
	public int MAX_TRIAL = 500; // maximum contact that u can send msg to on
								// trial
	public static Image logoImage, appImage, contactImage, aboutImage;
	public static String APPID = "1";
	public static String APPVERSION = "1.9";
	public static String SERVERURL = "http://apps.stackarena.com/";
	public static boolean ALLOW_REPLY = false;

	/**
	 * Creates a new MyScreen object
	 */
	public PinScreen() {

		UiApplication.getUiApplication().invokeAndWait(new Runnable() {
			public void run() {
				// request file permission
				try {
					ApplicationPermissionsManager apm = ApplicationPermissionsManager
							.getInstance();
					ApplicationPermissions original = apm
							.getApplicationPermissions();
					if (original
							.getPermission(ApplicationPermissions.PERMISSION_FILE_API) == ApplicationPermissions.VALUE_DENY) {
						ApplicationPermissions permRequest = new ApplicationPermissions();
						permRequest
								.addPermission(ApplicationPermissions.PERMISSION_FILE_API);
						apm.invokePermissionsRequest(permRequest);
					}
					if (original
							.getPermission(ApplicationPermissions.PERMISSION_INTERNET) == ApplicationPermissions.VALUE_DENY) {
						ApplicationPermissions permRequest = new ApplicationPermissions();
						permRequest
								.addPermission(ApplicationPermissions.PERMISSION_INTERNET);
						apm.invokePermissionsRequest(permRequest);
					}
				} catch (Exception e) {
					//System.exit(0);
					
				}
			}
		});
		// end of permission request

		logoImage = ImageFactory.createImage(Bitmap
				.getBitmapResource("stackarena.png"));
		appImage = ImageFactory.createImage(Bitmap
				.getBitmapResource("logo.png"));
		// contactImage =
		// ImageFactory.createImage(Bitmap.getBitmapResource("contacts.png"));
		// aboutImage =
		// ImageFactory.createImage(Bitmap.getBitmapResource("logo.jpg"));

		// set acceptable directions
		int acceptableDirections = 0;
		acceptableDirections = acceptableDirections | Display.DIRECTION_NORTH;
		UiEngineInstance ui = Ui.getUiEngineInstance();
		ui.setAcceptableDirections(acceptableDirections);

		// run verification check
		// sleep for 5secs
		// run update check
		Runnable updt = new Runnable() {
			public void run() {
				try {
				} catch (final Exception e) {
				}
				AppNetwork AppN = new AppNetwork();
				AppN.silent = true;
				AppN.UpdateApp();
			}
		};
		// end of update thread
		// run verify check
		Runnable ver = new Runnable() {
			public void run() {
				try {
				} catch (final Exception e) {
				}
				AppNetwork AppN = new AppNetwork();
				AppN.silent = true;
				AppN.Verification();
			}
		};
		// end of verify thread
		// start the thread
		new Thread(updt).start();
		new Thread(ver).start();

		// listener for button click
		listener = new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				try {
					SendPinMsg();
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}
		};

		/*
		 * *****************************
		 * Begin Screen elements ******************************
		 */
		// here is the colored header
		HeaderField header = new HeaderField("Pin Broadcast");
		// here we set a vertical field manager that we can use for the for
		// contents
		VerticalFieldManager vfm = new VerticalFieldManager(USE_ALL_WIDTH
				| VERTICAL_SCROLL) {
			public void paint(Graphics graphics) {
				graphics.setBackgroundColor(0x455163);
				graphics.clear();
				subpaint(graphics);
			}

		};
		// the status bar
		info = new LabelField("Idle...", LabelField.ELLIPSIS
				| LabelField.HCENTER | LabelField.USE_ALL_WIDTH);

		// the Send message button to click
		btnSend = new ButtonField("Send Message", ButtonField.CONSUME_CLICK
				| ButtonField.FIELD_RIGHT);
		btnSend.setChangeListener(listener);

		// set trial count label
		lblTrial = new LabelField("", LabelField.ELLIPSIS
				| LabelField.FIELD_VCENTER) {
			public void paint(Graphics graphics) {
				graphics.setColor(0xffffff);

				super.paint(graphics);
			}
		};

		// lblTrial.setMargin(10, 0, 0, 10);
		// lblTrial.setFont(lblTrial.getFont().derive(Font.STYLE_BOLD));
		// set a new column field that displays below the app.
		TableLayoutManager colFMgr = new TableLayoutManager(new int[]

		{ (int) TableLayoutManager.USE_PREFERRED_SIZE,
				TableLayoutManager.USE_PREFERRED_SIZE,

		}, Manager.NO_HORIZONTAL_SCROLL | Manager.USE_ALL_WIDTH);
		colFMgr.setMargin(7, 15, 7, 15);

		// now we need to design the status screen
		VerticalFieldManager statusScreen = new VerticalFieldManager(
				USE_ALL_WIDTH | VERTICAL_SCROLL) {
			public void paint(Graphics graphics) {
				graphics.setBackgroundColor(0x70819d);
				graphics.clear();
				subpaint(graphics);
			}

		};
		statusScreen.add(colFMgr);
		
		// textbox for from
		from = new CustomTextBox();
		from.height = 45;
		from.topPadding = 8;
		from.bottomMargin = 5;
		from.editField.setMaxSize(50);

		// textbox for subject
		subj = new CustomTextBox();
		subj.height = 45;
		subj.topPadding = 8;
		subj.bottomMargin = 5;
		subj.editField.setMaxSize(100);

		// the textbox to type msgs
		msgs = new CustomTextBoxML();
		msgs.height = 200;
		msgs.topPadding = 8;
		msgs.bottomMargin = 5;
		msgs.editField.setMaxSize(5000);

		// add everything to the screen now
		vfm.add(header);
		vfm.add(info);

		// set title and status
		setTitle(vfm);
		setStatus(statusScreen);

		// use this to wrap the color for the screen
		negManager = new NegativeMarginVerticalFieldManager(USE_ALL_WIDTH
				| VERTICAL_SCROLL);
		_manager = (VerticalFieldManager) getMainManager();
		Background bg = BackgroundFactory.createSolidBackground(0xe3e3e3);
		_manager.setBackground(bg);
		_manager.add(negManager);

		// the remaining screen objects
		lblFrom = new LabelField("From:", LabelField.ELLIPSIS
				| LabelField.USE_ALL_WIDTH);
		lblFrom.setMargin(10, 0, 0, msgs.leftMargin);
		lblFrom.setFont(lblFrom.getFont().derive(Font.STYLE_BOLD));
		
		lblSubject = new LabelField("Subject:", LabelField.ELLIPSIS
				| LabelField.USE_ALL_WIDTH);
		lblSubject.setMargin(10, 0, 0, msgs.leftMargin);
		lblSubject.setFont(lblSubject.getFont().derive(Font.STYLE_BOLD));

		lblMessage = new LabelField("Message:", LabelField.ELLIPSIS
				| LabelField.USE_ALL_WIDTH);
		lblMessage.setMargin(10, 0, 0, msgs.leftMargin);
		lblMessage.setFont(lblMessage.getFont().derive(Font.STYLE_BOLD));

		lblTo = new LabelField("To:", LabelField.ELLIPSIS);
		lblTo.setMargin(10, 10, 0, msgs.leftMargin);
		lblTo.setFont(lblTo.getFont().derive(Font.STYLE_BOLD));

		// file button
		_buttonField = new ButtonField("Choose File...",
				ButtonField.CONSUME_CLICK | ButtonField.NEVER_DIRTY);
		_buttonField.setChangeListener(this);

		BitmapField cntImg = new BitmapField(
				Bitmap.getBitmapResource("contacts.png"), BitmapField.RIGHT);
		cntImg.setMargin(0, msgs.rightMargin, 0, 0);
		HorizontalFieldManager hf = null;
		hf = new HorizontalFieldManager();
		hf.add(lblTo);
		hf.add(_buttonField);
		hf.add(cntImg);
		// appImage..paint(graphics, (fieldWidth-58)/2, 0, 58, fieldHeight);

		// _labelfield is the display of the file path
		_labelField = new LabelField("", LabelField.ELLIPSIS
				| LabelField.USE_ALL_WIDTH);
		_labelField.setMargin(5, 0, 0, msgs.leftMargin);

		_manager.setMargin(15, 0, 15, 0);
		_manager.add(hf);
		_manager.add(_labelField);
		_manager.add(new SeparatorField());
		_manager.add(lblFrom);
		_manager.add(from);
		_manager.add(lblSubject);
		_manager.add(subj);
		_manager.add(lblMessage);
		_manager.add(msgs);

		// Add send button under
		colFMgr.add(lblTrial);
		colFMgr.add(btnSend);

		/*
		 * *****************************
		 * End of Screen elements ******************************
		 */

		// Get the FilePicker instance
		_filePicker = FilePicker.getInstance();

		// Set the file picker to only display txt files
		_filePicker.setFilter(".txt");
		try {
			// Obtain the default system document directory to open
			// the file picker in.
			String path = System.getProperty("fileconn.dir.documents");

			// Set the directory to open the file picker in if the
			// directory exists
			FileConnection fconn = (FileConnection) Connector.open(path);
			if (fconn.exists()) {
				_filePicker.setPath(path);
			}
		} catch (final Exception ioe) {
			/*
			 * UiApplication.getUiApplication().invokeLater(new Runnable() {
			 * public void run() {
			 * Dialog.alert("Could not find your documents directory"+
			 * ioe.toString()); } });
			 */
		}

		// Make this class a file picker listener
		_filePicker.setListener(this);

		// initialize app, trial module
		Runnable trlMod = new Runnable() {
			public void run() {
				try {
				} catch (final Exception e) {
				}
				initializeApp();
			}
		};
		// start the thread
		new Thread(trlMod).start();

	}

	public void initializeApp() {
		// initialize the tmp file that stores trial count and update trial
		// count variable avl
		// get trial file -> if not exist, create and display alert for info
		// if trial file exist, update trialcount var to trial file val
		// set trial text number too
		if (!userActivated) {
			FileStuffs fs = new FileStuffs();
			boolean exists = fs.getTrialFile();
			if (exists) {
				String fcontent = fs.getTrialFileContent();
				trialCount = Integer.parseInt(fcontent);
				final int lft = MAX_TRIAL - trialCount;
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						lblTrial.setText("Trial Messages: " + lft);
					}
				});

			} else {

				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Dialog.inform("It seems you have not yet subscribed or this is your first time of using Pin Broadcast. If you have already subscribed, goto the menu and select 'Verify Account' to validate your subscription. \n\nYou can still try the app for free to send up to "
								+ MAX_TRIAL
								+ " messages. \n\nSelect the 'Subscribe' menu for information on subscription.");

					}
				});
				final int lft = MAX_TRIAL - trialCount;
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						lblTrial.setText("Trial Messages: " + lft);
					}
				});
				// fs.createTrialFile();

				trialUpdate(trialCount);
			}

		}

	}

	// update trial file
	public void trialUpdate(final int trialVal) {
		// update trial text number too
		Runnable trlupdt = new Runnable() {
			public void run() {
				try {
				} catch (final Exception e) {
				}
				FileStuffs fs = new FileStuffs();
				fs.updateTrialFile(trialVal);
				final int lft = MAX_TRIAL - trialVal;
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						lblTrial.setText("Trial Messages: " + lft);
					}
				});

			}
		};
		// start the thread
		new Thread(trlupdt).start();

	}

	/**
	 * @see FilePicker.Listener#selectionDone(String)
	 */
	public void selectionDone(final String selection) {
		if (selection != null && selection.length() > 0) {
			// begin file manipulation and verification
			// show wait screen
			Runnable vrfypin = new Runnable() {

				public void run() {
					try {
					} catch (final Exception e) {
						System.out.println("send error");
					}
					ContactArray.removeAllElements();
					pb = new ProgressBar("Verifying PINS...", 10, 500);
					pb.start();

					FileSel = selection;
					FileStuffs fl = new FileStuffs();
					String flResponse = null;
					try {
						flResponse = fl.ReadFile(selection);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						_labelField
								.setText("Please check the file, error reading file");
						FileSel = null;
					}
					// Display response on the screen
					_labelField.setText(flResponse);

					// remove wait screen
					pb.remove();
				}
			};
			new Thread(vrfypin).start();

		}
	}

	/**
	 * @see FieldChangeListener#fieldChanged(Field, int)
	 */
	public void fieldChanged(Field field, int context) {
		if (field == _buttonField) {
			_filePicker.show();
		}
	}

	public boolean onClose() {
		int response = Dialog.ask(Dialog.D_YES_NO,
				"Do you want to close Pin Broadcast?");
		if (Dialog.YES == response) {
			System.exit(0);
			return true;
		}

		return false;
	}

	// method that sends the pin message
	Runnable sndmsg = new Runnable() {

		public void run() {
			try {
			} catch (final Exception e) {
				System.out.println("send error");
			}
			// start wait screen
			pb = new ProgressBar("Sending message, please wait...", 10, 500);
			pb.start();
			// convert the vector array pins to normal array
			String[] arrayPins = new String[ContactArray.size()];
			ContactArray.copyInto(arrayPins);
			// start to send the message now
			PinSender ps = new PinSender();
			if (!userActivated) {
				message = message
						+ "\n\nSent with Pin Broadcast (http://apps.stackarena.com/)";
			}
			boolean resp = ps.SendMessage(arrayPins, subject, message);

			// findout what the server says while sending message
			if (resp) {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Dialog.inform("Message sent successfully! ");
					}
				});
				info.setText("Message sent!");
			} else {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Dialog.alert("Could not send your message, please try again");
						info.setText("Error sending message.");
					}
				});

			}
			// empty the vector and normal array and clear all
			// fields
			ContactArray.removeAllElements();
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					msgs.setText("");
					subj.setText("");
					FileSel = null;
					_labelField.setText("No file selected.");
				}
			});
			if (!userActivated) {
				trialCount = trialCount + arrayPins.length;
				// update the trial file

				trialUpdate(trialCount);
			}

			System.out.println("pins:" + arrayPins.length);
			// remove wait screen
			pb.remove();
			arrayPins = null;
		}
	};

	public void SendPinMsg() {

		message = msgs.getText();
		subject = subj.getText();
		HTTPClient hc = new HTTPClient();
		hc.getConnectionString();

		// find out if the user has a network connection
		if (!ntwkCon) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Dialog.inform("Please check your network, Pin Broadcast requires BIS or Wifi");
					info.setText("Check your network connection");
				}
			});
		} else if (!userActivated && trialCount > MAX_TRIAL) {
			// user not activated and trial exceeded max
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Dialog.inform("You have exceeded the number of messages you can send. ("
							+ MAX_TRIAL
							+ "). Please subscribe. Select the 'Subscribe' menu for information on subscription. Select 'Verify Account' on the menu after subscription to send unlimited messages.");
					info.setText("Trial number exceeded.");
				}
			});

		} else if (!userActivated
				&& ((trialCount + ContactArray.size()) > MAX_TRIAL)) {
			final int avl = MAX_TRIAL - trialCount;
			// user not activated and copied contact exceeds max
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Dialog.inform("The contacts in your message exceeds what you have left for trial ("
							+ avl
							+ "). Please subscribe and select 'Verify Account' on the menu for unlimited contacts or reduce the number of your contacts to "
							+ avl);
					// info.setText("Trial number exceeded.");
				}
			});

		} else {

			if (message.length() < 5 || subject.length() < 3 || from.getText().length() < 2 || FileSel == null) {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Dialog.inform("Please verify that From, Subject, Message and your Contact File has been entered properly before sending this message.");
						info.setText("Error in message, please check again.");
					}
				});
			} else {

				new Thread(sndmsg).start();
			}
		}
	}

	// the blackberry menu items
	protected void makeMenu(Menu menu, int instance) {
		super.makeMenu(menu, instance);

		menu.add(new MenuItem("Send Message", 10, 0) {
			public void run() {
				// call the pin sender function
				listener.fieldChanged(btnSend, 0);
			}
		});
		menu.addSeparator();
		if (!userActivated) {
			menu.add(new MenuItem("Subscribe", 10, 220) {
				public void run() {
					// goto weblink on browser
					WebIcon webIcon = new WebIcon();
					webIcon.launchBrowser(webIcon.BIS_BROWSER, SERVERURL
							+ "subscribe/");
				}
			});
		}
		if (!userActivated) {
			menu.add(new MenuItem("Verify Account", 200000, 0) {
				public void run() {
					// create a new instance of the network class and display
					// response for verification
					AppNetwork AppN = new AppNetwork();
					AppN.silent = false;
					AppN.Verification();
				}
			});
		}

		menu.add(new MenuItem("Update App", 200100, 10) {
			public void run() {
				// create a new instance of the network class and display
				// response for update
				AppNetwork AppN = new AppNetwork();
				AppN.silent = false;
				AppN.UpdateApp();
			}
		});
		menu.add(new MenuItem("About", 200200, 10) {
			public void run() {
				String abt = "Pin Broadcast helps you send bulk PIN messages to your contacts. You can send a message to 30,000+ contacts at once.\n\n"
						+ "Usage:\n"
						+ "Create a text file (.txt) with each Blackberry PIN on a single line, store the file on your phone and select the file when sending a message from Pin Broadcast."
						+ "\n\nVisit our website or send us an email for subscription inquiries or select 'Subscribe' on the menu."
						+ "\n\nStackArena. info@stackarena.com, http://apps.stackarena.com/";
				Dialog.inform(abt);
			}
		});
	}

}