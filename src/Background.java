import java.awt.Image;
import java.awt.Toolkit;

public class Background
{
	Image back;
	Image frame; 
	Image frame2;
	Image clock;
	

	Background()
	{
		back = Toolkit.getDefaultToolkit().createImage("back.jpg");
		frame2 = Toolkit.getDefaultToolkit().createImage("frame2.gif");
		frame = Toolkit.getDefaultToolkit().createImage("frame.gif");
		//clock = Toolkit.getDefaultToolkit().createImage("clock.gif");
				
		
	}

}
	
