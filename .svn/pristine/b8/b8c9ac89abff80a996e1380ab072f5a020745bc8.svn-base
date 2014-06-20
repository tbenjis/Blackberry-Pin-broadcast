package com.stackarena.pinbroadcast;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class CustomTextBoxML extends Manager
{
	
    /**
     * Default margins
     */
    private final static int DEFAULT_LEFT_MARGIN = 15;
    private final static int DEFAULT_RIGHT_MARGIN = 15;
    private final static int DEFAULT_TOP_MARGIN = 5;
    private final static int DEFAULT_BOTTOM_MARGIN = 5;
    
    /**
     * Default paddings
     */
    private final static int DEFAULT_LEFT_PADDING = 10;
    private final static int DEFAULT_RIGHT_PADDING = 10;
    private final static int DEFAULT_TOP_PADDING = 5;
    private final static int DEFAULT_BOTTOM_PADDING = 5;
    
    /**
     * Margins around the text box
     */
    int topMargin = DEFAULT_TOP_MARGIN;
    int bottomMargin = DEFAULT_BOTTOM_MARGIN;
    int leftMargin = DEFAULT_LEFT_MARGIN;
    int rightMargin = DEFAULT_RIGHT_MARGIN;
    
    /**
     * Padding around the text box
     */
    int topPadding = DEFAULT_TOP_PADDING;
    int bottomPadding = DEFAULT_BOTTOM_PADDING;
    int leftPadding = DEFAULT_LEFT_PADDING;
    int rightPadding = DEFAULT_RIGHT_PADDING;
    
    /**
     * Amount of empty space horizontally around the text box
     */
    private int totalHorizontalEmptySpace = leftMargin + leftPadding 
                                       + rightPadding + rightMargin;
    
    /**
     * Amount of empty space vertically around the text box
     */
    private int totalVerticalEmptySpace = topMargin + topPadding 
                                  + bottomPadding + bottomMargin;
    
    /**
     * Minimum height of the text box required to display the text entered
     */
    private int minHeight = getFont().getHeight() + totalVerticalEmptySpace;
    
    /**
     * Width of the text box
     */
    int width = Display.getWidth();
    
    /**
     * Height of the text box
     */
    int height = minHeight;
    
    /**
     * Background image for the text box
     */
   
    /**
     * The core element of this text box
     */
    EditField editField;
    //private BasicEditField editField;
    
    //private String entireText;
    
    public CustomTextBoxML()
    {
        // Let the super class initialize the core
    	 super(NO_HORIZONTAL_SCROLL);
    	VerticalFieldManager vfmm = new VerticalFieldManager(VERTICAL_SCROLL | VERTICAL_SCROLLBAR | USE_ALL_WIDTH | USE_ALL_HEIGHT);
    	// An edit field is the sole field of this manager.
        editField = new EditField(  EditField.FOCUSABLE | EditField.EDITABLE);
        //editField = new CustomEditField();
        vfmm.add(editField);
        add(vfmm);
        
    }
    
 

    
    public void setSize(int width, int height)
    {
        boolean isChanged = false;
        
        if (width > 0 // Ignore invalid width
                && this.width != width)  
        {
            this.width = width;
            isChanged = true;
        }
        
        // Ignore the specified height if it is less 
        // than the minimum height required to display the text.
        if (height > minHeight && height != this.height)
        {
            this.height = height;
            isChanged = true;
        }
        
        // If width/height has been changed and background image 
        // is available, adapt it to the new dimension
      
    }
    
    public void setWidth(int width)
    {
        
        if (width > 0 && width != this.width)
        {
            this.width = width;
            
        
        }
    }
    
    public void setHeight(int height)
    {
        // Ignore the specified height if it is 
        // less than the minimum height required to display the text.
        if (height > minHeight)
        {
            this.height = height;
            
            // If background image is available, adapt it to the new width
           
        }
    }
    
  
    /**
     * Generate and return a Bitmap Image scaled according 
     * to the specified width and height.
     * 
     * @param image     EncodedImage object
     * @param width     Intended width of the returned Bitmap object
     * @param height    Intended height of the returned Bitmap object
     * @return Bitmap object
     */
   
    
    protected void sublayout(int width, int height)
    {
        // We have one and only child - the edit field. 
        // Place it at the appropriate place.
        Field field = getField(0);
        layoutChild(field, this.width - totalHorizontalEmptySpace, 
                    this.height - totalVerticalEmptySpace);
        setPositionChild(field, leftMargin+leftPadding, topMargin+topPadding);
        
        setExtent(this.width, this.height);
    }
    
    public void setTopMargin(int topMargin)
    {
        this.topMargin = topMargin;
    }
    
    public void setBottomMargin(int bottomMargin)
    {
        this.bottomMargin = bottomMargin;
    }
    
    
    protected void paint(Graphics graphics )
    {
        // Draw background image if available, otherwise draw a rectangle
       		
        	graphics.setColor(Color.WHITE); 
		 graphics.fillRoundRect(leftMargin, topMargin, 
                             width - (leftMargin+rightMargin), 
                             height - (topMargin+bottomMargin),15,15);
     	 graphics.fillRoundRect(leftMargin, topMargin, 
                                width - (leftMargin+rightMargin), 
                                height - (topMargin+bottomMargin), 15, 15);
     	
     	graphics.setColor(Color.BLACK); 
     	
     	 super.paint(graphics);
   
    }
    
    public int getPreferredWidth()
    {
        return width;
    }
    
    public int getPreferredHeight()
    {
        return height;
    }
    
   
    
    public String getText()
    {
        return editField.getText();
    }
    
    public void setText(final String text)
    {
        editField.setText(text);
    }
}