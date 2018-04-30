import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowStateListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.MediaTracker;
import java.lang.Math;

class paint_2
{
	public static void main(String args[])
	{
		AllHandler painter = new AllHandler();
	}
}

class AllHandler extends Canvas implements ActionListener, MouseListener, MouseMotionListener, ComponentListener, WindowStateListener

{
	JMenuItem clr_pick,brush_pick,select_font,load_image,highlight,noaction;
	JMenuItem draw_line,draw_c_rect,draw_s_rect,draw_c_elipse,draw_s_elipse,draw_text,get_text,draw_free;
	JMenuItem change_back,clear;
	JMenuItem exit,save_as;
	JMenuBar menu_bar;
	JMenu file_menu,draw_menu,text_menu,canvas_menu;
	JFrame app_window,font_viewer;
	String text_to_draw = "ABCD abcd";
	Graphics g_current,g_temp,infant_g;
	Graphics2D g2_current,g2_temp;
	Color clr = Color.RED;
	Container conpane,subpane,space_holder;
	int xstart=0,ystart=0,xend=0,yend=0,movedx,movedy,clickedx,clickedy;
	boolean dragging,file_dialog_ok,flag=true;
	int brush_size=1,todo=0,under_mouse=-1,already_highlighted;
	double angle;
	String cur_event="Nothing",imgFile="G:\\_TERM_2\\sampleImage.jpeg";
	GridBagConstraints gbc; 
	FontChooser font_dialog;
	Font drawing_font;
	Vector<DrawnShapeSignature> collection;
	Image picture;
	JFileChooser file_browser;
	FileNameExtensionFilter file_filter;
	File imageFile;
	MediaTracker mt;
	JPopupMenu Pmenu;
	JMenuItem menuItem;
	AffineTransform at,tat,drg;
	Point2D start,end;
	
	/* Constructor || initialize all components  */
	AllHandler()
	{
		super();
		
		app_window = new JFrame("Paint Brush");
		//app_window.setVisible(true);
		app_window.setSize(800,600);
		app_window.setBackground(Color.BLACK);
		app_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/* Creating MenuBar and MenuHeadings */
		menu_bar = new JMenuBar();
				
		file_menu = new JMenu("File");
		menu_bar.add(file_menu);
		//menu_bar.add(arg0)
		draw_menu = new JMenu("Draw");
		menu_bar.add(draw_menu);
		text_menu = new JMenu("Labeling");
		menu_bar.add(text_menu);
		canvas_menu = new JMenu("Canvas");
		menu_bar.add(canvas_menu);
		/* Populating Menus */
		/*comp 1 */
		clr_pick = new JMenuItem("Color Picker");
		//clr_pick.setBackground(clr);
		clr_pick.addActionListener(this);		
		draw_menu.add(clr_pick);
		/*comp 2*/
		brush_pick = new JMenuItem("Brush Size");
		brush_pick.addActionListener(this);
		draw_menu.add(brush_pick);
		/*comp 3*/
		draw_line = new JMenuItem("Straight Line");
		draw_line.addActionListener(this);
		draw_menu.add(draw_line);
		/*comp 4*/
		draw_free = new JMenuItem("Free Hand");
		draw_free.addActionListener(this);
		draw_menu.add(draw_free);
		/*comp 5*/
		draw_c_rect = new JMenuItem("Clear Rectangle");
		draw_c_rect.addActionListener(this);
		draw_menu.add(draw_c_rect);
		/*comp 6*/
		draw_s_rect = new JMenuItem("Solid Rectangle");
		draw_s_rect.addActionListener(this);
		draw_menu.add(draw_s_rect);
		/*comp 7*/
		draw_c_elipse= new JMenuItem("Clear Elipse");
		draw_c_elipse.addActionListener(this);
		draw_menu.add(draw_c_elipse);
		/*comp 8*/
		draw_s_elipse= new JMenuItem("Solid Elipse");
		draw_s_elipse.addActionListener(this);
		draw_menu.add(draw_s_elipse);
		/*comp 9*/
		save_as = new JMenuItem("Save as jpeg");
		save_as.addActionListener(this);
		file_menu.add(save_as);
		
		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		file_menu.add(exit);
		
		/*comp 10*/
		get_text = new JMenuItem("Enter Text");
		text_menu.add(get_text);
		get_text.addActionListener(this);
		get_text.setVisible(true);
		/* comp 11*/
		select_font =new JMenuItem("Select Font");
		text_menu.add(select_font);
		select_font.addActionListener(this);
		select_font.setVisible(true);
		/*comp 12*/
		draw_text = new JMenuItem("Draw Text");
		text_menu.add(draw_text);
		draw_text.addActionListener(this);
		draw_text.setVisible(true);
		/*comp 13*/
		load_image = new JMenuItem("Load Picture");
		load_image.addActionListener(this);
		draw_menu.add(load_image);
		/*comp 14*/
		highlight = new JMenuItem("Highlight under curser");
		highlight.addActionListener(this);
		draw_menu.add(highlight);
		
		noaction = new JMenuItem("No Action");
		noaction.addActionListener(this);
		draw_menu.add(noaction);
		
		change_back = new JMenuItem("Change background");
		change_back.addActionListener(this);
		canvas_menu.add(change_back);
		
		clear = new JMenuItem("Clear Canvas");
		clear.addActionListener(this);
		canvas_menu.add(clear);
		
		/* Setting MenuBar on Application Window */
		app_window.setJMenuBar(menu_bar);
		menu_bar.setVisible(true);
		/* Adding the Canvas created by super(); */
		/*comp 12*/
		app_window.setLayout(new GridLayout());
		app_window.add(this);
		setVisible(true);
		//setSize(780,530);
		setBackground(Color.DARK_GRAY);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		/*  PopupMenu   beginning*/
		Pmenu = new JPopupMenu();
		
	    menuItem = new JMenuItem("Move");
	    menuItem.addActionListener(this);
	    Pmenu.add(menuItem);
	    menuItem = new JMenuItem("Scale");
	    Pmenu.add(menuItem);
	    menuItem.addActionListener(this);
	    menuItem = new JMenuItem("Rotate");
	    Pmenu.add(menuItem);
	    menuItem.addActionListener(this);
	    
	    menuItem = new JMenuItem("Change color");
	    Pmenu.add(menuItem);
	    menuItem.addActionListener(this);
	    
	    menuItem = new JMenuItem("Bring to front");
	    Pmenu.add(menuItem);
	    menuItem.addActionListener(this);
	    
	    menuItem = new JMenuItem("Send to back");
	    Pmenu.add(menuItem);
	    menuItem.addActionListener(this);
	    
	    menuItem = new JMenuItem("Delete");
	    Pmenu.add(menuItem);
	    menuItem.addActionListener(this);
	    menuItem = new JMenuItem("Cancel");
	    Pmenu.add(menuItem);
	    menuItem.addActionListener(this);
	    
	    /*  PopupMenu   finish*/
		
	    //drawing_font.size = 15;
		font_dialog = new FontChooser(app_window);
		collection = new Vector<DrawnShapeSignature>(10,10);
		file_browser = new JFileChooser();
		file_filter = new FileNameExtensionFilter("Java default image files (jpeg,gif,png)", "jpg","jpeg","gif","png");
		file_browser.setFileFilter(file_filter); 
		mt = new MediaTracker(this);
		start = new Point2D.Double(0.0,0.0);
		end  = new Point2D.Double(0.0,0.0);
		/* ** ** Test Code ** ** */
		picture = Toolkit.getDefaultToolkit().getImage(imgFile);
		app_window.setVisible(true);
		repaint();
	}
	/* OverRiding paint method */
	public void paint(Graphics g)
	{
		g.setColor(clr);
		g.setFont(drawing_font);
		BasicStroke stroke = new BasicStroke((float)brush_size);
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(stroke);
		g_current = g.create();
		/* For Tracing current event*/
		//g.drawString(cur_event,200,200);
		drawAllInAVector (collection, g2);
		g = g_current.create();
		g2 = (Graphics2D)g;
		switch (todo)
		{
		case 1 :
			if(dragging)
				g2.drawLine(xstart,ystart,xend,yend);				
			break;
		case 2 :
			if (dragging)
				g2.drawRect(movedx,movedy,brush_size,brush_size);
			break;
		case 3 :
			if (dragging)
				{
					Rectangle dragged_rect = formatToRectangle(xstart,ystart,xend,yend);
					g2.drawRect(dragged_rect.x,dragged_rect.y,dragged_rect.width,dragged_rect.height);
				}
			break;
		case 4 :
			if (dragging)
			{
				Rectangle dragged_rect = formatToRectangle(xstart,ystart,xend,yend);
				g2.fillRect(dragged_rect.x,dragged_rect.y,dragged_rect.width,dragged_rect.height);
			}
			break;
		case 5 :
			if (dragging)
			{
				Rectangle dragged_rect = formatToRectangle(xstart,ystart,xend,yend);
				g2.drawOval(dragged_rect.x,dragged_rect.y,dragged_rect.width,dragged_rect.height);
			}
			break;
		case 6:
			if (dragging)
			{
				Rectangle dragged_rect = formatToRectangle(xstart,ystart,xend,yend);
				g2.fillOval(dragged_rect.x,dragged_rect.y,dragged_rect.width,dragged_rect.height);
			}
			break;
		case 7 :
			g.drawString(text_to_draw, movedx, movedy);
			break;
		case 51 :
			/* Picture will be loaded first then resize*/
			break;
		/*
		case 100:
			if (collection != null && under_mouse!=-1)
			highlightUnderMouse(under_mouse,g);
			break;
		*/
		case 101 :
			if(under_mouse!=-1 && dragging)
			{	
				DrawnShapeSignature item_under_mouse = ((DrawnShapeSignature)collection.elementAt(under_mouse)).create();
				g2= (Graphics2D) item_under_mouse.dg.create();
				g2.setStroke(new BasicStroke(5f));
				g2.setColor(Color.GREEN);
				g2.translate((xend-xstart),(yend-ystart));
				if(item_under_mouse.rotation != 0)
					g2.rotate(item_under_mouse.rotation);
				
				switch (item_under_mouse.shapetype)
				{
					case 1:
						g2.draw(item_under_mouse.line);
						break;
					case 2 :
						break;
					case 3 :
						g2.draw(item_under_mouse.rectangle);
						break;
					case 4 :
						g2.draw(item_under_mouse.rectangle);
						break;
					case 5 :
						g2.drawOval(item_under_mouse.rectangle.x,item_under_mouse.rectangle.y,item_under_mouse.rectangle.width,item_under_mouse.rectangle.height);
						break;
					case 6 :
						g2.drawOval(item_under_mouse.rectangle.x,item_under_mouse.rectangle.y,item_under_mouse.rectangle.width,item_under_mouse.rectangle.height);
						break;
					case 7 :
						g2.drawString(item_under_mouse.string, 0, 0);
						break;
					case 51 :
						g2 = (Graphics2D) g_current.create();
						g2.setStroke(new BasicStroke(5f));
						g2.setColor(Color.GREEN);
						at = ((Graphics2D)item_under_mouse.dg).getTransform();
						g2.translate(at.getTranslateX(), at.getTranslateY());
						g2.translate((xend-xstart),(yend-ystart));
						if(item_under_mouse.rotation != 0)
							g2.rotate(item_under_mouse.rotation);
						g2.drawRect(item_under_mouse.x,item_under_mouse.y,item_under_mouse.width,item_under_mouse.height);
						break;
					default :
						break;
				}
				
			}
			break;
		default :
			break;
		}
		if (collection != null && under_mouse!=-1 && todo == 100)
			highlightUnderMouse(under_mouse,g);
	}
	/* Draw each shape and image drawn by user that are stored in a vector */
	private void drawAllInAVector (Vector collection, Graphics2D g2)
	{
		if (collection!=null)
		{	
		for(int i=0;i<collection.size();i++)
			{
			g2=(Graphics2D)(((DrawnShapeSignature)collection.elementAt(i)).dg.create());
			//g = (((DrawnShapeSignature)collection.elementAt(i)).dg.create());
			Rectangle temp_rect;
			DrawnShapeSignature temp_item = ((DrawnShapeSignature)collection.elementAt(i)).create();
			if(temp_item.rotation!=0)
				g2.rotate(temp_item.rotation);
			
			switch (((DrawnShapeSignature)collection.elementAt(i)).shapetype)
				{
				case 1 :
					g2.draw(temp_item.line);
					break;
				case 2 :
					temp_rect = new Rectangle(((DrawnShapeSignature)collection.elementAt(i)).rectangle);
					g2.drawRect(temp_rect.x,temp_rect.y,temp_rect.width,temp_rect.height);
					break;
				case 3 :
					temp_rect = new Rectangle(temp_item.rectangle);
					g2.drawRect(temp_rect.x,temp_rect.y,temp_rect.width,temp_rect.height);
					break;
				case 4 :
					temp_rect = new Rectangle(temp_item.rectangle);
					g2.fillRect(temp_rect.x,temp_rect.y,temp_rect.width,temp_rect.height);
					break;
				case 5 :
					temp_rect = new Rectangle(temp_item.rectangle);
					g2.drawOval(temp_rect.x,temp_rect.y,temp_rect.width,temp_rect.height);
					break;
				case 6 :
					temp_rect = new Rectangle(temp_item.rectangle);
					g2.fillOval(temp_rect.x,temp_rect.y,temp_rect.width,temp_rect.height);
					break;
				case 7 :
					g2.drawString(temp_item.string, 0, 0);
					break;
				case 51 :
					picture = Toolkit.getDefaultToolkit().getImage(temp_item.string);
					ImageWaiter waiter = new ImageWaiter();
					if (picture.getWidth(waiter) != temp_item.width || picture.getHeight(waiter) != temp_item.height )
						picture = picture.getScaledInstance(temp_item.width, temp_item.height, Image.SCALE_DEFAULT);
					try 
					{
						mt.addImage(picture, 0);
						mt.waitForAll();
					} catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					g2 = (Graphics2D) g_current.create();
					at = ((Graphics2D)temp_item.dg).getTransform();
					g2.translate(at.getTranslateX(), at.getTranslateY());
					if(temp_item.rotation != 0)
						g2.rotate(temp_item.rotation);
					g2.drawImage(picture, 0, 0, waiter);
					break;
				default :
					break;
				}
			}
			}
	}
	/* Method to get the index value of the vector element under mouse */
	public int indexOfElementUnderMouse(double mousex,double mousey)
	{
		int i, index = -1;
		DrawnShapeSignature ithitem;
		Graphics tg;
		Graphics2D tg2;
		
		if(collection!=null)
		{
		for(i=(collection.size()-1);i>=0;i--)
		{
			//JOptionPane.showMessageDialog(app_window, i);
			ithitem = ((DrawnShapeSignature)collection.elementAt(i)).create();
			AffineTransform at = ((Graphics2D)ithitem.dg).getTransform(), translator = new AffineTransform(), rotator = new AffineTransform();
			translator.translate(-at.getTranslateX(), -at.getTranslateY());
			if(ithitem.rotation != 0)
				rotator.rotate(-ithitem.rotation);
			Point2D mouse = new Point2D.Double((double)mousex,(double)mousey),back = new Point2D.Double(0.0,0.0);
			Rectangle temp_rect;
			Rectangle2D.Double rect2d;
			Ellipse2D.Double elp2d;
			//DrawnShapeSignature temp_item;
			translator.transform(mouse, back);
			if(ithitem.rotation != 0)
				rotator.transform(back, back);
		
		switch (ithitem.shapetype)
			{
			case 1 :
				Line2D.Float l2d = new Line2D.Float();
				
				l2d.setLine(ithitem.line);
				rect2d = new Rectangle2D.Double((double)(back.getX()-3),(double)(back.getY()-3),(double)6,(double)6);
				if(l2d.intersects(rect2d))
				{
					index = i;
					i = -1;
				}
				break;
			
			/*
			case 2 :
				temp_rect = new Rectangle(((DrawnShapeSignature)collection.elementAt(i)).rectangle);
				g.drawRect(temp_rect.x,temp_rect.y,temp_rect.width,temp_rect.height);
				break;
			*/
			case 3 :
				
				temp_rect = new Rectangle(ithitem.rectangle);
				rect2d = new Rectangle2D.Double((double)temp_rect.getX(),(double)temp_rect.getY(),(double)temp_rect.width,(double)temp_rect.height);
				if(rect2d.contains(back))
				{
					index = i;
					i = -1;
				}
				break;
				
			case 4 :
				temp_rect = new Rectangle(ithitem.rectangle);
				rect2d = new Rectangle2D.Double((double)temp_rect.getX(),(double)temp_rect.getY(),(double)temp_rect.width,(double)temp_rect.height);
				if(rect2d.contains(back))
				{
					index = i;
					i = -1;
				}
				break;

			case 5 :
				temp_rect = new Rectangle(ithitem.rectangle);
				//rect2d = new Rectangle2D.Double(at.getTranslateX(),at.getTranslateY(),(double)temp_rect.width,(double)temp_rect.height);
				elp2d = new Ellipse2D.Double((double)temp_rect.getX(),(double)temp_rect.getY(),(double)temp_rect.width,(double)temp_rect.height);
				if(elp2d.contains(back))
				{
					index = i;
					i = -1;
				}
				break;
			case 6 :
				temp_rect = new Rectangle(ithitem.rectangle);
				//rect2d = new Rectangle2D.Double(at.getTranslateX(),at.getTranslateY(),(double)temp_rect.width,(double)temp_rect.height);
				elp2d = new Ellipse2D.Double((double)temp_rect.getX(),(double)temp_rect.getY(),(double)temp_rect.width,(double)temp_rect.height);
				if(elp2d.contains(back))
				{
					index = i;
					i = -1;
				}
				break;
			
			case 7 :
				int font_size = (ithitem.dg.getFont()).getSize();
				rect2d = new Rectangle2D.Double(ithitem.x,ithitem.y-font_size*1.2,(double)ithitem.string.length()*font_size/1.4,(double)font_size*1.5);
				if(rect2d.contains(back))
				{
					index = i;
					i = -1;
				}
				break;
			
			case 51 :
				//temp_item = ((DrawnShapeSignature)collection.elementAt(i)).create();
				/*
				picture = Toolkit.getDefaultToolkit().getImage(ithitem.string);
				
				try 
				{
					mt.addImage(picture, 0);
					mt.waitForAll();
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				temp_rect = new Rectangle(ithitem.rectangle);
				*/
				rect2d = new Rectangle2D.Double((double)ithitem.x,(double)ithitem.y,(double)ithitem.width,(double)ithitem.height);
				//elp2d = new Ellipse2D.Double(at.getTranslateX(),at.getTranslateY(),(double)temp_rect.width,(double)temp_rect.height);
				if(rect2d.contains(back))
				{
					index = i;
					i = -1;
				}
				break;
			default :
				break;
			}
		}
		}
		return index;
	}
	/* Highlight the shape or image under mouse by cyan color */
	public void highlightUnderMouse(int i,Graphics g)
	{
		{
			g = g_current.create();
			Graphics2D g2;
			g2 =(Graphics2D)g;
			g.setColor(Color.CYAN);
			g2.setColor(Color.CYAN);
			g2.setStroke(new BasicStroke(5f));
			DrawnShapeSignature temp_item = ((DrawnShapeSignature)collection.elementAt(i)).create(); 
			AffineTransform at = ((Graphics2D)temp_item.dg).getTransform();
			Rectangle temp_rect;
			
			g2.setTransform(at);
			if(temp_item.rotation != 0 )
				g2.rotate(temp_item.rotation);
			
			switch (temp_item.shapetype)
				{
				case 1 :
					g2.draw(temp_item.line);
					break;
				case 2 :
					temp_rect = new Rectangle(((DrawnShapeSignature)collection.elementAt(i)).rectangle);
					g.drawRect(temp_rect.x,temp_rect.y,temp_rect.width,temp_rect.height);
					break;
				case 3 :
					temp_rect = new Rectangle(temp_item.rectangle);
					g2.drawRect(temp_rect.x,temp_rect.y,temp_rect.width,temp_rect.height);
					break;
				case 4 :
					
					temp_rect = new Rectangle(temp_item.rectangle);
					g2.drawRect(temp_rect.x,temp_rect.y,temp_rect.width,temp_rect.height);
					break;
				case 5 :
					temp_rect = new Rectangle(temp_item.rectangle);
					g2.drawOval(temp_rect.x,temp_rect.y,temp_rect.width,temp_rect.height);
					break;
				case 6 :
					temp_rect = new Rectangle(temp_item.rectangle);
					g2.drawOval(temp_rect.x,temp_rect.y,temp_rect.width,temp_rect.height);
					break;
				case 7 :
					int text_height = temp_item.dg.getFont().getSize();
					g2.drawLine( 0, 0,(int)(temp_item.string.length()*text_height/1.5),temp_item.y);
					g2.drawRect((int)(0-text_height*1.2),(int)(0-text_height),text_height,(int)(text_height*1.5));
					break;
				case 51 :
					g2.drawLine(0, 0, temp_item.width, temp_item.height);
					g2.drawLine(0, temp_item.height,temp_item.width ,0 );
					g2.drawRect(0, 0, temp_item.width, temp_item.height);
					break;
				default :
					break;
				}
			}
	}
   	/* Scale the element at index by changing the value of width and height (in case of line x1,y1,x2,y2 change) */
	public void scaleElement(int index)
	{
		DrawnShapeSignature item_to_scale = ((DrawnShapeSignature)collection.elementAt(index)).create();
		double scale_factor = 1.0;
		String scale_factor_string;
		scale_factor_string = JOptionPane.showInputDialog(null, "Enter scale factor in percentage ...");
		if (!isPositiveDouble(scale_factor_string))
			JOptionPane.showMessageDialog(null, "!! Wrong value entered !!");
		else
		{
			scale_factor = Double.parseDouble(scale_factor_string)/100;
			switch (item_to_scale.shapetype)
			{
			case 1 :
				double px1 = item_to_scale.line.getX1(),px2 = item_to_scale.line.getX2(),py1 = item_to_scale.line.getY1(),py2 = item_to_scale.line.getY2();
				double nx1 = 0, nx2 = 0, ny1 = 0,ny2 = 0;
				double m = java.lang.Math.atan((py2-py1)/(px2-px1));
				double length = java.lang.Math.sqrt(java.lang.Math.pow((px1-px2),2.0) + java.lang.Math.pow((py1-py2),2.0));
				
			
				double extend_more = scale_factor*length - length;
				double extend_morex = java.lang.Math.abs(java.lang.Math.cos(m)*extend_more);
				double extend_morey = java.lang.Math.abs(java.lang.Math.sin(m)*extend_more);
				if(extend_more<0)
				{
					extend_morex *= -1;
					extend_morey *= -1;
				}
				/*  */
				if (px1>0 && py1>0)
				{	
					nx1 = px1 + extend_morex/2;
					ny1 = py1 + extend_morey/2;
					nx2 = px2 - extend_morex/2;
					ny2 = py2 - extend_morey/2;
				
				}else if (px1<0 && py1<0)
				{	
					nx1 = px1 - extend_morex/2;
					ny1 = py1 - extend_morey/2;
					nx2 = px2 + extend_morex/2;
					ny2 = py2 + extend_morey/2;
				}else if (px1>0 && py1<0)
				{	
					nx1 = px1 + extend_morex/2;
					ny1 = py1 - extend_morey/2;
					nx2 = px2 - extend_morex/2;
					ny2 = py2 + extend_morey/2;
				}else if (px1<0 && py1>0)
				{	
					nx1 = px1 - extend_morex/2;
					ny1 = py1 + extend_morey/2;
					nx2 = px2 + extend_morex/2;
					ny2 = py2 - extend_morey/2;
				}
				
			
			item_to_scale.line.setLine(nx1, ny1, nx2, ny2);
			break; 
			/*
		case 2 :
			String scale_factor_string = JOptionPane.showInputDialog(null, "Enter scale factor in percentage ...");
			if (!isPositiveDouble(scale_factor_string))
				JOptionPane.showMessageDialog(null, "!! Wrong value entered !!");
			else
			{
				scale_factor = Double.parseDouble(scale_factor_string)/100;
				item_to_scale
			}
			break;
			*/
			case 3 :
				item_to_scale.scaleTo((int)(item_to_scale.width*scale_factor), (int)(item_to_scale.height*scale_factor));
				break;
			case 4 :
				item_to_scale.scaleTo((int)(item_to_scale.width*scale_factor), (int)(item_to_scale.height*scale_factor));
				break;
			case 5 :
				item_to_scale.scaleTo((int)(item_to_scale.width*scale_factor), (int)(item_to_scale.height*scale_factor));
				break;
			case 6 :
				item_to_scale.scaleTo((int)(item_to_scale.width*scale_factor), (int)(item_to_scale.height*scale_factor));
				break;
			
			case 7 :
				item_to_scale.scaleTo(scale_factor);
				break;
			
			case 51 :
				item_to_scale.scaleTo((int)(item_to_scale.width*scale_factor), (int)(item_to_scale.height*scale_factor));
				break;
			default :
				break;
			}
		}
	}
	/* Methods in ActionListener */ 
	public void actionPerformed(ActionEvent aEvent)
    {
		String s = aEvent.getActionCommand();
		DrawnShapeSignature drawn_item;

		if(s == "Color Picker")
		{
			clr = JColorChooser.showDialog(null,"Pick a Color",clr);
			//setBackground(clr);
			g_current.setColor(clr); 
		}
		
		if(s == "Brush Size")
		{
			String size = JOptionPane.showInputDialog(app_window,"Enter Brush size (at least 1)");
			if (isPositiveInteger(size) && Integer.valueOf(size)>=1)
			{
				brush_size = Integer.valueOf(size);
				JOptionPane.showMessageDialog(app_window,"Brush size has been changed to "+brush_size);
			} 
			else JOptionPane.showMessageDialog(app_window,"Invalid value entered");
		}
		if(s == "Clear Canvas")
		{
			//collection = null;
			collection.removeAllElements();
			todo = 0;
			repaint();
		}
		if(s == "Change background")
		{
			//clr = JColorChooser.showDialog(null,"Pick a Color",clr);
			setBackground(JColorChooser.showDialog(null,"Pick a Color",clr)); 
		}
		if (s == "No Action") todo = 0;
		if(s == "Select Font")
		{
			font_dialog.setVisible(true);
			drawing_font = font_dialog.getSelectedFont();
		}
		if (s == "Exit") System.exit(0);
		if (s == "Straight Line") todo=1;
		if (s == "Free Hand") todo=2;
		if (s == "Clear Rectangle") todo=3;
		if (s == "Solid Rectangle") todo=4;
		if (s == "Clear Elipse") todo=5;
		if (s == "Solid Elipse") todo=6;
		if (s == "Draw Text") todo=7;
		if (s == "Enter Text") 
			text_to_draw = JOptionPane.showInputDialog(app_window, "Enter the text for creating label");
		if (s == "Load Picture") 
		{
			todo = 0;
			importImageToVector(collection);
		}
		if (s == "Highlight under curser") 
		{
			todo = 100;
		}
		if (s == "Move")
		{
			todo = 101;
			repaint();
		}
		if (s == "Scale")
		{
			todo = 102;
			scaleElement(under_mouse);
			repaint();
		}
		if (s == "Rotate")
		{
			todo = 103;
			String angle_string = JOptionPane.showInputDialog(app_window, "Enter angle (in degree) to rotate");
			if(isDouble(angle_string) && Double.parseDouble(angle_string)%360!= 0 && under_mouse != -1 )
			{
				angle = Math.toRadians(  Double.parseDouble(angle_string)  );
				((DrawnShapeSignature)collection.elementAt(under_mouse)).rotation = angle;
				repaint();
			}
		}
		if (s == "Change color")
		{
			if(under_mouse != -1 )
				((DrawnShapeSignature)collection.elementAt(under_mouse)).dg.setColor(JColorChooser.showDialog(null,"Pick a Color",clr));
			repaint();
			//JOptionPane.showMessageDialog(app_window, s);
		}
		
		if (s == "Bring to front")
		{
			if(under_mouse != -1)
			{
				DrawnShapeSignature dss = ((DrawnShapeSignature)collection.elementAt(under_mouse)).create();
				collection.add(dss);
				collection.removeElementAt(under_mouse);
			}
			repaint();
		}
		if (s == "Send to back")
		{
			if(under_mouse != -1)
			{
				DrawnShapeSignature dss = ((DrawnShapeSignature)collection.elementAt(under_mouse)).create();
				collection.removeElementAt(under_mouse);
				collection.insertElementAt(dss,0);
				
			}
			repaint();
		}
		if (s == "Delete")
		{
			todo = 104;
			if(under_mouse != -1)
				collection.removeElementAt(under_mouse);
			repaint();
			//JOptionPane.showMessageDialog(app_window, s);
		}
		if (s == "Cancel")
		{
			todo = 105;
			repaint();
			//JOptionPane.showMessageDialog(app_window, s);
		}
		if (s ==  "Save as jpeg")
		{
			//save(makeImage());
			saveToFile();
		}
		
    }
	/* Methods in MouseListener */
    public void mouseClicked(MouseEvent mEvent)
    {
		dragging = false;
		clickedx = mEvent.getX();
		clickedy = mEvent.getY();
	}
	public void mousePressed(MouseEvent mEvent)
	{
		dragging = false;
		xstart = mEvent.getX();
		ystart = mEvent.getY();
		start = new Point2D.Double(xstart,ystart);

	}
	public void mouseReleased(MouseEvent mEvent)
	{
		dragging = false;
		cur_event = "Released";
		xend = mEvent.getX();
		yend = mEvent.getY();
		end = new Point2D.Double(xend,xend);
		
		if(mEvent.isPopupTrigger())
		{
			Pmenu.show(mEvent.getComponent(), mEvent.getX(), mEvent.getY());
			
		}else if(xstart!=xend || ystart!=yend)
		{
				
			DrawnShapeSignature drawn_item;
			Rectangle dragged_rect;
			switch (todo)
			{
			case 1 :
				double tx = (double)(xstart+xend)/2, ty = (double)(ystart+yend)/2;
				Line2D.Float line = new Line2D.Float((float)-(xstart-tx),(float)-(ystart-ty),(float)-(xend-tx),-(float)(yend-ty));				
				g_temp = g_current.create();
				g_temp.translate((int)tx, (int)ty);
				drawn_item = new DrawnShapeSignature(g_temp,line);
				collection.addElement(drawn_item);
				break;
			//case 2 :
				//break;
			case 3 :
				dragged_rect = formatToRectangle(xstart,ystart,xend,yend);
				//Rectangle2D.Float rectangle = new Rectangle2D.Float((float)dragged_rect.x,(float)dragged_rect.y,(float)dragged_rect.width,(float)dragged_rect.height);
				g_current.translate(dragged_rect.x, dragged_rect.y);
				dragged_rect.x = 0; dragged_rect.y = 0;
				drawn_item = new DrawnShapeSignature(todo,g_current,dragged_rect);
				collection.addElement(drawn_item);
				break;
			case 4 :
				dragged_rect = formatToRectangle(xstart,ystart,xend,yend);
				g_current.translate(dragged_rect.x, dragged_rect.y);
				dragged_rect.x = 0; dragged_rect.y = 0;
				drawn_item = new DrawnShapeSignature(todo,g_current,dragged_rect);
				collection.addElement(drawn_item);
				break;
			case 5 :
				dragged_rect = formatToRectangle(xstart,ystart,xend,yend);
				g_current.translate(dragged_rect.x, dragged_rect.y);
				dragged_rect.x = 0; dragged_rect.y = 0;
				drawn_item = new DrawnShapeSignature(todo,g_current,dragged_rect);
				collection.addElement(drawn_item);
				break;
			case 6 : 
				dragged_rect = formatToRectangle(xstart,ystart,xend,yend);
				g_current.translate(dragged_rect.x, dragged_rect.y);
				dragged_rect.x = 0; dragged_rect.y = 0;
				drawn_item = new DrawnShapeSignature(todo,g_current,dragged_rect);
				collection.addElement(drawn_item);
				break;
			case 7 :
				if(xstart==xend && ystart==yend)
				{
					Graphics temp_g = g_current.create();
					temp_g.translate(xend, yend);
					drawn_item = new DrawnShapeSignature(todo,temp_g,text_to_draw,0,0);
					collection.addElement(drawn_item);
				}
				break;
			case 101 :
				//JOptionPane.showMessageDialog(app_window, indexOfElementUnderMouse((double)mEvent.getX(), (double)mEvent.getY()));
				under_mouse = indexOfElementUnderMouse(movedx,movedy);
				if(under_mouse != -1)
				{
					//at.translate((double)(xend-xstart), (double)(yend-ystart));
					((DrawnShapeSignature)collection.elementAt(under_mouse)).dg.translate(xend-xstart, yend-ystart);
					repaint();
					todo = 0;
				}
					
				break;
			}
		}else if(xstart==xend && ystart==yend && todo==7)
		{
			DrawnShapeSignature drawn_item;
			Graphics temp_g = g_current.create();
			temp_g.translate(xend, yend);
			drawn_item = new DrawnShapeSignature(todo,temp_g,text_to_draw,0,0);
			collection.addElement(drawn_item);
		}
		
		if(todo!=0) repaint();
	}
    public void mouseEntered(MouseEvent mEvent)
    {
    	dragging = true;
	}
    public void mouseExited(MouseEvent mEvent)
    {
		dragging = false;
	}
	/* Methods in MouseMotionListener */
    public void mouseDragged(MouseEvent mEvent)
    {
		xend = mEvent.getX();
		yend = mEvent.getY();
		end = new Point2D.Double(xend,xend);
		cur_event = "dragged";
		dragging = true; 
		
		if (todo == 2)
		{
			Rectangle dragged_rect = new Rectangle(xend,yend,brush_size,brush_size);
			DrawnShapeSignature drawn_item = new DrawnShapeSignature(todo,g_current,dragged_rect);
			collection.addElement(drawn_item);
			repaint();
		}
			else if(todo!=0)
				repaint();
	}
    public void mouseMoved(MouseEvent mEvent)
    {
		dragging = false;
    	cur_event="Moved";
		movedx = mEvent.getX();
		movedy = mEvent.getY();
		if (todo == 7)
			repaint();
		if (collection!=null && todo!=0)
		{
			under_mouse = indexOfElementUnderMouse((double)mEvent.getX(), (double)mEvent.getY());
			if (already_highlighted != under_mouse)
			{
				if(under_mouse != -1 || flag)
				{
					repaint();
					already_highlighted = under_mouse;
					flag = true;
				}
				if(under_mouse == -1 && flag)
				{
					flag = false;
				}
			}
		}
		/*		*/
	}
	/* Window state Listener */
	public void windowStateChanged(WindowEvent e)
	{
		//Rectangle main_window_rect = conpane.getBounds;
		JOptionPane.showMessageDialog(app_window,"Window event happend");
	}
	/* Component Listener */
	public void componentResized(ComponentEvent e)
	{
		//JOptionPane.showMessageDialog(app_window,"Component Resized");
		Rectangle conpane_rect = app_window.getBounds();
		//subpane.setSize(conpane_rect.width-10,100);
		//subpane.setLocation(5,5);
		setSize(conpane_rect.width-5,conpane_rect.height-20);
		//setLocation(5,105);
		
	}
	public void componentHidden(ComponentEvent e)
	{

	}
	public void componentShown(ComponentEvent e)
	{

	}
	public void componentMoved(ComponentEvent e)
	{

	}	
	/* Let user select an Image and add it in the drawn item collection */
	public void importImageToVector(Vector<DrawnShapeSignature> collector)
	{
		{
			todo = 0;
			int dialog_return = file_browser.showOpenDialog(app_window); 
			
			if ( dialog_return == JFileChooser.APPROVE_OPTION)
				{
					imageFile = file_browser.getSelectedFile();
					imgFile = imageFile.getPath(); 
					picture = Toolkit.getDefaultToolkit().getImage(imgFile);
					try 
					{
						mt.addImage(picture, 0);
						mt.waitForAll();
						
					} catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					ImageWaiter waiter = new ImageWaiter();
					int img_width = 0,img_height = 0;
					img_width = picture.getWidth(waiter);
					img_height = picture.getHeight(waiter);
					
					DrawnShapeSignature drawn_item;
					drawn_item = new DrawnShapeSignature(g_current,imgFile,0,0,img_width,img_height);
					
					collector.addElement(drawn_item);
					mt.removeImage(picture, 0);
					repaint();
				}
			
			}
		
	}	
	/* Create a Rectangle from coordinate values of the two ends of a Line */
	Rectangle formatToRectangle(int x1,int y1,int x2,int y2) 
	{
	    int width = x2-x1;
	    int height = y2-y1;

	    /* Make width and height positive. */
	    if (width < 0) {
	        width = 0 - width;
	        x1 = x1 - width;
	    }
	    if (height < 0) {
	        height = 0 - height;
	        y1 = y1 - height;
	    }
	    return new Rectangle(x1, y1, width, height);
	}	
	/* Test the input string if it is an integer and positive*/
	boolean isPositiveInteger(String s)
	{
	    try {
	        Integer.parseInt(s);
	        if(Integer.parseInt(s)>=0)
	        	return true;
	        else
	        	return false;
	    	} catch(NumberFormatException e) {
	        return false;
	    }
	}
	/* Test the input string if it is an integer and positive*/
	boolean isPositiveDouble(String s)
	{
	    try {
	        Double.parseDouble(s);
	        if(Double.parseDouble(s)>0)
	        	return true;
	        else
	        	return false;
	    	} catch(NumberFormatException e) {
	        return false;
	    }
	}
	/* Test the input string if it is an integer or positive*/
	boolean isDouble(String s)
	{
	    try {
	        	Double.parseDouble(s);
	            return true;
	    	} catch(NumberFormatException e) 
	    	{
	        return false;
	    	}
	}
	/* Draw each shape and image drawn by user that are stored in a vector */
	private void drawAllInAVectorOnImage (Vector collection, Graphics2D g2, BufferedImage bimage)
	{
		if (collection!=null)
		{	
		for(int i=0;i<collection.size();i++)
			{
				g2 = bimage.createGraphics();
				Graphics2D g2_temp =(Graphics2D)(((DrawnShapeSignature)collection.elementAt(i)).dg.create());
				g2.setStroke(g2_temp.getStroke());
				AffineTransform at_temp = g2_temp.getTransform();
				g2.setColor(g2_temp.getColor());
				g2.translate(at_temp.getTranslateX(), at_temp.getTranslateY());
				Rectangle temp_rect;
				DrawnShapeSignature temp_item = ((DrawnShapeSignature)collection.elementAt(i)).create();
				if(temp_item.rotation!=0)
					g2.rotate(temp_item.rotation);
			
				switch (((DrawnShapeSignature)collection.elementAt(i)).shapetype)
				{
					case 1 :
						g2.draw(temp_item.line);
						break;
					case 2 :
						temp_rect = new Rectangle(((DrawnShapeSignature)collection.elementAt(i)).rectangle);
						g2.drawRect(temp_rect.x,temp_rect.y,temp_rect.width,temp_rect.height);
						break;
					case 3 :
						temp_rect = new Rectangle(temp_item.rectangle);
						g2.drawRect(temp_rect.x,temp_rect.y,temp_rect.width,temp_rect.height);
						break;
					case 4 :
						temp_rect = new Rectangle(temp_item.rectangle);
						g2.fillRect(temp_rect.x,temp_rect.y,temp_rect.width,temp_rect.height);
						break;
					case 5 :
						temp_rect = new Rectangle(temp_item.rectangle);
						g2.drawOval(temp_rect.x,temp_rect.y,temp_rect.width,temp_rect.height);
						break;
					case 6 :
						temp_rect = new Rectangle(temp_item.rectangle);
						g2.fillOval(temp_rect.x,temp_rect.y,temp_rect.width,temp_rect.height);
						break;
					case 7 :
						g2.drawString(temp_item.string, 0, 0);
						break;
					case 51 :
						picture = Toolkit.getDefaultToolkit().getImage(temp_item.string);
						ImageWaiter waiter = new ImageWaiter();
						if (picture.getWidth(waiter) != temp_item.width || picture.getHeight(waiter) != temp_item.height )
							picture = picture.getScaledInstance(temp_item.width, temp_item.height, Image.SCALE_DEFAULT);
						try 
						{
							mt.addImage(picture, 0);
							mt.waitForAll();
						} catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
						g2.drawImage(picture, 0, 0, waiter);
						break;
					default :
						break;
				}
			}
		}
	}
	private BufferedImage makeImage() 
	{
		int w = getWidth();
		int h = getHeight();
		int type = BufferedImage.TYPE_INT_RGB;
		BufferedImage image = new BufferedImage(w,h,type);
		Graphics2D g2 = image.createGraphics();
		//paint(g2);
		
		Rectangle2D rect;
		rect = new Rectangle2D.Float(0f,0f,(float)w,(float)h);
		g2.setColor(getBackground());
		g2.fill(rect);
		
		drawAllInAVectorOnImage(collection,g2,image);
		g2.dispose();
		return image;
	}
	private void save(BufferedImage image) 
	{
		int dialog_return = file_browser.showOpenDialog(app_window); 
		
		if ( dialog_return == JFileChooser.APPROVE_OPTION)
			{
				imageFile = file_browser.getSelectedFile();
				//imgFile = imageFile.getPath(); 
			}
		String ext = "jpg"; // png, bmp (j2se 1.5+), gif (j2se 1.6+)
		
		//File file = new File("G:\\componentToImage." + ext);
		File file = new File(imageFile.getPath() +"."+ ext);
		try 
		{
			ImageIO.write(image, ext, file);
		} catch(IOException e) 
		{
			System.out.println("write error: " + e.getMessage());
		}
	}
	private void saveToFile() 
	{
		new Thread
		(
			new Runnable() 
				{
					public void run() 
					{
						try 
						{
							Thread.sleep(1000);
						} catch(InterruptedException e) 
						{
							System.out.println("interrupted");
						}
						save(makeImage());
					}
				}
		).start();
	}

}
