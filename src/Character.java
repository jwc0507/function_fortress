import java.awt.Image;
import java.awt.Toolkit;

public class Character
{
	Image character, photo;
	
	Character()
	{
		 character =  Toolkit.getDefaultToolkit().createImage("image2.gif");
		 photo =  Toolkit.getDefaultToolkit().createImage("image2_photo.gif");
		
	}

}
