
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;

public class DrawnShapeSignature {

	int shapetype;			/* 1-Line,2-Free Hand,3-Solid Rectangle,
							*  4-Clear Rectangle,5-Solid Ellipse,
							*  6-Clear Ellipse,7-Text,
							*  51-Image,
	 						*/
	Graphics dg;
	Graphics2D dg2;
	Double rotation = 0.0;
	Double tx = 0.0;
	Double ty = 0.0;
	Color color;			// drawn in Color
	Font font;				// drawn in Font
	Stroke stroke;			// drawn in Stroke
	Line2D line;			//
	Rectangle rectangle;	//
	Rectangle2D rectangle2d;//	
	Image image;			//
	File file;				//
	int x1,y1;				// starting point of Line
	int x2,y2;				// finishing point of Line
	int x,y;				// Upper-Left corner of Rectangle
	int width,height;		// width and height of Rectangle
	String string;			// string drawn 
	// Constructor for Line with Graphics1D 
	DrawnShapeSignature(Graphics dg,Line2D line  )
	{
		this.shapetype = 1;
		this.dg = dg.create();
		this.dg2 = (Graphics2D)dg.create();
		this.line = line;
		this.x1 = (int)line.getX1();
		this.y1 = (int)line.getY1();
		this.x2 = (int)line.getX2();
		this.y2 = (int)line.getY2();
	}
	// Constructor for Line with Graphics2D
	DrawnShapeSignature(Graphics2D dg2,Line2D line  )
	{
		this.shapetype = 1;
		this.dg = dg2.create();
		this.dg2 = (Graphics2D)dg2.create();
		this.line = line;
		this.x1 = (int)line.getX1();
		this.y1 = (int)line.getY1();
		this.x2 = (int)line.getX2();
		this.y2 = (int)line.getY2();
	}
	// Constructor for Rectangle with Graphics1D
	DrawnShapeSignature(int shapetype,Graphics dg,Rectangle rectangle  )
	{
		this.shapetype = shapetype;
		this.dg = dg.create();
		this.dg2 = (Graphics2D)dg;
		this.rectangle = rectangle;
		this.rectangle2d = (Rectangle2D)rectangle;
		this.x = (int)rectangle.getX();
		this.y = (int)rectangle.getY();
		this.width = (int)rectangle.getWidth();
		this.height = (int)rectangle.getHeight();
	}
	// Constructor for 2D Rectangle with Graphics2D
	DrawnShapeSignature(int shapetype,Graphics2D dg2,Rectangle rectangle  )
	{
		this.shapetype = shapetype;
		this.dg = (Graphics)dg2;
		this.dg2 = dg2;
		this.rectangle = rectangle;
		this.rectangle2d = (Rectangle2D)rectangle;
		this.x = (int)rectangle.getX();
		this.y = (int)rectangle.getY();
		this.width = (int)rectangle.getWidth();
		this.height = (int)rectangle.getHeight();
	}	
	// Constructor for Rectangle2D with Graphics1D ****
	DrawnShapeSignature(int shapetype,Graphics dg,Rectangle2D rectangle2d)
	{
		this.shapetype = shapetype;
		this.dg = dg;
		this.dg2 = (Graphics2D)dg;
		this.rectangle = new Rectangle((int)rectangle2d.getX(),(int)rectangle2d.getY(),(int)rectangle2d.getWidth(),(int)rectangle2d.getHeight());
		this.rectangle2d = rectangle2d;
		this.x = (int)rectangle2d.getX();
		this.y = (int)rectangle2d.getY();
		this.width = (int)rectangle2d.getWidth();
		this.height = (int)rectangle2d.getHeight();
	}
	// Constructor for Rectangle2D with Graphics2D
	DrawnShapeSignature(int shapetype,Graphics2D dg2,Rectangle2D rectangle2d)
	{
		this.shapetype = shapetype;
		this.dg = (Graphics)dg2;
		this.dg2 = dg2;
		this.rectangle = new Rectangle((int)rectangle2d.getX(),(int)rectangle2d.getY(),(int)rectangle2d.getWidth(),(int)rectangle2d.getHeight());
		this.rectangle2d = rectangle2d;
		this.x = (int)rectangle2d.getX();
		this.y = (int)rectangle2d.getY();
		this.width = (int)rectangle2d.getWidth();
		this.height = (int)rectangle2d.getHeight();
	}
	// Constructor for Text with Graphics1D
	DrawnShapeSignature(int shapetype,Graphics dg,String string,int x,int y)
	{
		this.shapetype = shapetype;
		this.dg = dg.create();
		this.dg2 = (Graphics2D)dg;
		this.string = string;
		//this.font = font;
		this.x = x;
		this.y = y;
	}
	// Constructor for Text with Graphics2D
	DrawnShapeSignature(int shapetype,Graphics2D dg2,String string,int x,int y)
	{
		this.shapetype = shapetype;
		this.dg = (Graphics)dg2;
		this.dg2 = dg2;
		this.string = string;
		//this.font = font;
		this.x = x;
		this.y = y;
	}
	// Constructor for Image with Graphics1D
	DrawnShapeSignature(Graphics dg,String string,int x,int y,int width,int height)
	{
		this.shapetype = 51;
		this.dg = dg;
		this.dg2 = (Graphics2D)dg;
		//this.string = file.getPath() + file.getName();
		this.string = string;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	// Constructor for Image with Graphics2D
	DrawnShapeSignature(Graphics2D dg2,File file,int x,int y,int width,int height)
	{
		this.shapetype = 51;
		this.dg = (Graphics)dg2;
		this.dg2 = dg2;
		this.string = file.getPath() + file.getName();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	/* Returning copy of itself Function */
	public DrawnShapeSignature create()
	{
		return this;
	}
	/* Scaling for Rectangle */
	public void scaleTo(int width, int height)
	{
		this.width = width;
		this.height = height;
		if (shapetype == 3 || shapetype == 4 || shapetype == 5 || shapetype == 6 )
		{
			this.rectangle.setSize(width, height);
			((Rectangle) this.rectangle2d).setSize(width, height);
		}
	}
	/* Scaling for Text */
	public void scaleTo(double scale_factor)
	{
		
		Font fnt = dg.getFont();
		int pre_size = fnt.getSize();
		int new_size = (int) (pre_size*scale_factor);
		dg.setFont(new Font(fnt.getFontName(),fnt.getStyle(),new_size));
	}
	
	/* ****************************
	
	DrawnShapeSignature(Color color,Stroke stroke,Line2D line  )
	{
		this.shapetype = 1;
		this.color = color;
		this.stroke = stroke;
		this.line = line;
		this.x1 = (int)line.getX1();
		this.y1 = (int)line.getY1();
		this.x2 = (int)line.getX2();
		this.y2 = (int)line.getY2();
	}
	// Constructor for Rectangle
	DrawnShapeSignature(int shapetype,Color color,Stroke stroke,Rectangle rectangle  )
	{
		this.shapetype = shapetype;
		this.color = color;
		this.stroke = stroke;
		this.rectangle = rectangle;
		this.rectangle2d = (Rectangle2D)rectangle;
		this.x = (int)rectangle.getX();
		this.y = (int)rectangle.getY();
		this.width = (int)rectangle.getWidth();
		this.height = (int)rectangle.getHeight();
	}
	// Constructor for 2D Rectangle
	DrawnShapeSignature(int shapetype,Color color,Stroke stroke,Rectangle2D rectangle2d  )
	{
		this.shapetype = shapetype;
		this.color = color;
		this.stroke = stroke;
		this.rectangle = new Rectangle((int)rectangle2d.getX(),(int)rectangle2d.getY(),(int)rectangle2d.getWidth(),(int)rectangle2d.getHeight());
		this.rectangle2d = rectangle2d;
		this.x = (int)rectangle2d.getX();
		this.y = (int)rectangle2d.getY();
		this.width = (int)rectangle2d.getWidth();
		this.height = (int)rectangle2d.getHeight();
	}
	// Constructor for Text
	DrawnShapeSignature(int shapetype,Color color,Stroke stroke,String string,Font font,int x,int y)
	{
		this.shapetype = shapetype;
		this.color = color;
		this.stroke = stroke;
		this.string = string;
		this.font = font;
		this.x = x;
		this.y = y;
	}
	
	***************************************************/
}

