package Item;

import java.awt.*;

public abstract class Item{
	private String name;
	
	public  Item(String name)
	{		
		this.name = name;
	}
	
	public String getItemName()
	{
		return this.name;
	}	
}