import java.awt.*;
import java.awt.event.*;

class Control extends Frame
{
	private int x;
	private int y;
			
	Client client = new Client();
	
	public Control() 
	{
		this.registerEventHandler(); // control
	}

	private void registerEventHandler() 
	{
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e) 
			{
				System.exit(0);
			}
		});

		addKeyListener(new KeyAdapter() 
		{
			
			public void keyPressed(KeyEvent ke)
			{
				
				int key = ke.getKeyCode();
				
				if(key==KeyEvent.VK_UP)
				{
					/*
					if(y >= 0)
					{
						//client.pushUpButton(null);
										
						//y -= 3;
									
					}*/
				}
				else if(key==KeyEvent.VK_DOWN)
				{
					/*
					if(y + 100 <= 600)
					{
						
						//y += 3;
					}
					*/
				}
				else if(key==KeyEvent.VK_LEFT)
				{
					client.pushLeftButton();
					/*
					if(x >= 0)
					{
						
						
						//x -= 3;
					}*/
				}
				else if(key==KeyEvent.VK_RIGHT)
				{
					client.pushRightButton();
				
					/*
					if(x + 100 <= 1200)
					{
						
						
						//x += 3;
					}*/
				}
			}
		}
		);
	
	}
}