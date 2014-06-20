package com.stackarena.pinbroadcast;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
 
public class HeaderField extends Field {
 
    int fieldWidth = Display.getWidth();
    int fieldHeight;
    String text;
    Font headerFont;
    //gradient items:
    int[] upperX_PTS;
    int[] upperY_PTS;
    int[] upperDrawColors;
    int[] lowerX_PTS;
    int[] lowerY_PTS;
    int[] lowerDrawColors;
 
    public HeaderField(String text) {
        this.text = text;
        headerFont = fieldFont();
        if (fieldWidth < 321) {
            fieldHeight = 36;
        } else {
            fieldHeight = 50;
        }
 
        upperX_PTS = new int[]{0, 0, fieldWidth, fieldWidth};
        upperY_PTS = new int[]{0, fieldHeight / 2, fieldHeight / 2, 0};
        upperDrawColors = new int[]{0xa0acc2, 0x7788a4, 0x7788a4, 0xa0acc2};
        lowerX_PTS = new int[]{0, 0, fieldWidth, fieldWidth};
        lowerY_PTS = new int[]{fieldHeight / 2, fieldHeight, fieldHeight, fieldHeight / 2};
        lowerDrawColors = new int[]{0x70819d, 0x606e88, 0x606e88, 0x70819d};
    }
    public int getPreferredWidth() {
        return fieldWidth;
    }
 
    public int getPreferredHeight() {
        return fieldHeight;
    }
 
    protected void layout(int width, int height) {
        setExtent(getPreferredWidth(), getPreferredHeight());
    }
 
    protected void paint(Graphics graphics) {
    	graphics.drawShadedFilledPath(upperX_PTS, upperY_PTS, null, upperDrawColors, null);
    	graphics.drawShadedFilledPath(lowerX_PTS, lowerY_PTS, null, lowerDrawColors, null);

        PinScreen.logoImage.paint(graphics, 5, 0, 146, fieldHeight);
        graphics.setFont(headerFont);
        int textWidth = headerFont.getAdvance(text);
        int yLoc = (fieldHeight - headerFont.getHeight()) / 2;
 
        //slight embossed style:
      //  graphics.setColor(0x424546);
       // graphics.drawText(text, (fieldWidth - textWidth) / 2, yLoc - 2);
 
        graphics.setColor(0xffffff);
        graphics.drawText(text, (fieldWidth - textWidth)-10 , yLoc);
        //draw the logo 
        //PinScreen.appImage.paint(graphics, (fieldWidth-58)/2, 0, 58, fieldHeight);
    }
 
    private Font fieldFont() {
        if (fieldWidth < 321) {
            try {
                FontFamily theFam = FontFamily.forName("BBMillbank");
                return theFam.getFont(Font.BOLD, 14);
            } catch (ClassNotFoundException ex) {
                return Font.getDefault();
            }
        } else {
            try {
                FontFamily theFam = FontFamily.forName("BBAlpha Sans");
                return theFam.getFont(Font.BOLD, 20);
            } catch (ClassNotFoundException ex) {
                return Font.getDefault();
            }
        }
    }
}