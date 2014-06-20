package com.stackarena.pinbroadcast;

import com.blackberry.toolkit.ui.container.HyperlinkButtonField;
import com.stackarena.pinbroadcast.network.WebIcon;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class AboutDialog extends PopupScreen implements FieldChangeListener {
	private String _title;
	private HyperlinkButtonField _devlink;
	private final static String DEVURL = "http://apps.stackarena.com";
	private final static String TUNDEURL = "http://twitter.com/tbenjis";
	private final static String NIYIURL = "http://twitter.com/noadek";
	private final static String KUNLEURL = "http://twitter.com/kunlay";
	private final static String STACKURL = "http://twitter.com/stackarena";

	ButtonField btnOK;
	private String _features;
	private String _team;
	private HyperlinkButtonField _tundelink;
	private HyperlinkButtonField _niyilink;
	private HyperlinkButtonField _kunlelink;
	private HyperlinkButtonField _akposlink;

	public AboutDialog() {
		super(new VerticalFieldManager(Manager.VERTICAL_SCROLL
				| Manager.VERTICAL_SCROLLBAR));

		_title = "About Pin Broadcast";
		_features = "Usage";
		_team = "Team";

		_tundelink = new HyperlinkButtonField("@tbenjis", Color.DEEPSKYBLUE,
				Color.BLACK, 0, 0);
		_tundelink.setChangeListener(this);
		_tundelink.setMargin(5, 5, 0, 0);
		_niyilink = new HyperlinkButtonField("@noadek", Color.DEEPSKYBLUE,
				Color.BLACK, 0, 0);
		_niyilink.setChangeListener(this);
		_niyilink.setMargin(5, 5, 0, 0);
		_kunlelink = new HyperlinkButtonField("@kunlay", Color.DEEPSKYBLUE,
				Color.BLACK, 0, 0);
		_kunlelink.setChangeListener(this);
		_kunlelink.setMargin(5, 5, 0, 0);
		_akposlink = new HyperlinkButtonField("@stackarena", Color.DEEPSKYBLUE,
				Color.BLACK, 0, 0);
		_akposlink.setChangeListener(this);

		_devlink = new HyperlinkButtonField("apps.stackarena.com",
				Color.DEEPSKYBLUE, Color.BLACK, 1, 0);
		_devlink.setChangeListener(this);

		String message = "Pin Broadcast helps you send bulk PIN messages to your contacts. You can send a message to 30,000+ contacts at once.\n";

		String _features_msg = "Create a text file (.txt) with each Blackberry PIN on a single line, store the file on your phone and select the file when sending a message from Pin Broadcast.";
		String _team_msg = "App by: ";
		
		SeparatorField line1 = new SeparatorField();

		LabelField titleLabel = new LabelField(_title, FOCUSABLE | FIELD_VCENTER){
			protected void drawFocus(Graphics g, boolean on) {
				if (on) {
				}
			}
		};
		FontFamily fontFamily[] = FontFamily.getFontFamilies();
		Font font = fontFamily[1].getFont(FontFamily.SCALABLE_FONT, Font
				.getDefault().getHeight() + 2);
		titleLabel.setFont(font);
		titleLabel.setMargin(0, 0, 5, 10);
		
		VerticalFieldManager vftop = new VerticalFieldManager( NO_VERTICAL_SCROLL | FIELD_VCENTER);
		vftop.add(titleLabel);
		vftop.add(line1);
		
		HorizontalFieldManager hftop = new HorizontalFieldManager(NO_HORIZONTAL_SCROLL);
		Bitmap logo = Bitmap.getBitmapResource("logo.png");
		BitmapField logo_view = new BitmapField(logo, FIELD_VCENTER	);
		
		hftop.add(logo_view);
		hftop.add(vftop);

		
		SeparatorField line2 = new SeparatorField();
		SeparatorField line3 = new SeparatorField();
		SeparatorField line4 = new SeparatorField();

		font = fontFamily[1].getFont(FontFamily.SCALABLE_FONT, Font
				.getDefault().getHeight());
		LabelField messageArea1 = new LabelField(message);
		messageArea1.setMargin(5, 5, 15, 5);
		LabelField messageArea2 = new LabelField(_features);
		messageArea2.setFont(font);
		messageArea2.setMargin(0, 0, 5, 0);
		LabelField messageArea3 = new LabelField(_features_msg);
		messageArea3.setMargin(5, 5, 15, 5);
		LabelField messageArea4 = new LabelField(_team);
		messageArea4.setFont(font);
		messageArea4.setMargin(0, 0, 5, 0);
		LabelField messageArea5 = new LabelField(_team_msg);
		LabelField messageArea6 = new LabelField("Follow ");
		
		HorizontalFieldManager hf = new HorizontalFieldManager();
		hf.setMargin(0, 0, 5, 0);
		hf.add(_tundelink);
		hf.add(_niyilink);
		hf.add(_kunlelink);

		HorizontalFieldManager hf2 = new HorizontalFieldManager();
		hf2.add(messageArea5);
		hf2.add(_devlink);

		HorizontalFieldManager hf3 = new HorizontalFieldManager();
		hf2.setMargin(0, 0, 5, 0);
		hf3.add(messageArea6);
		hf3.add(_akposlink);
		hf3.setMargin(0, 0, 5, 0);
		btnOK = new ButtonField("OK", Field.FIELD_HCENTER);
		btnOK.setMargin(10, 0, 0, 0);
		btnOK.setChangeListener(this);

		add(hftop);
		add(messageArea1);
		add(messageArea2);
		add(line2);
		add(messageArea3);
		add(messageArea4);
		add(line3);
		add(hf);
		add(hf2);
		add(hf3);
		add(line4);
		add(btnOK);
	
	}

	// pop screen on escape button
	public boolean keyChar(char key, int status, int time) {
		if (key == Characters.ESCAPE) {
			UiApplication.getUiApplication().popScreen(this);
			return true;
		}
		return super.keyChar(key, status, time);
	}

	public void fieldChanged(Field field, int context) {
		if (field == btnOK) {
			UiApplication.getUiApplication().popScreen(this);
		}
		if (field == _devlink) {
			WebIcon webIcon = new WebIcon();
			webIcon.launchBrowser(WebIcon.BIS_BROWSER, DEVURL);
		}
		if (field == _tundelink) {
			WebIcon webIcon = new WebIcon();
			webIcon.launchBrowser(WebIcon.BIS_BROWSER, TUNDEURL);
		}
		if (field == _niyilink) {
			WebIcon webIcon = new WebIcon();
			webIcon.launchBrowser(WebIcon.BIS_BROWSER, NIYIURL);
		}
		if (field == _kunlelink) {
			WebIcon webIcon = new WebIcon();
			webIcon.launchBrowser(WebIcon.BIS_BROWSER, KUNLEURL);
		}
		if (field == _akposlink) {
			WebIcon webIcon = new WebIcon();
			webIcon.launchBrowser(WebIcon.BIS_BROWSER, STACKURL);
		}
	}

}