import java.util.Scanner;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import Character.*;
import Item.*;
import Map.*;

public class Client 
{
	private Process proc;
	private Display dis;
	//private Map map;
	private String mapName;
	private boolean flag;
	private int numOfUser;
	private int mynumber;	
	
	public Client()
	{
		this.mynumber = 2;
		
		mapName = JOptionPane.showInputDialog("map name : ");
		numOfUser = 4; //나중에 서버한테 받아옴
		dis = new Display(this,numOfUser, mynumber);
		
		//this.map = new IceAge();
		this.proc = new Process(4, mapName, 500 ,this);
		flag = true;
		
		dis.setSize(1200, 800);
		dis.setLocation(100, 100);
		dis.setVisible(true);
		
		dis.InitDisplay();
		
		while(flag)
		{
			String input = JOptionPane.showInputDialog("메세지 입력 : ");
			proc.processing(input);
		}
		
	}
	
	// process에서 실행. 서버로 데이터를 보냄
	public void sendData(String s)
	{
		// 미구현
	}
	public void sendItemName(String itemName,int index)
	{
		dis.showItemImage(itemName,index);
	}
	public void sendMapName(String mapName)
	{
		this.mapName =  mapName;
	}
	
	public Point getUserPoint(int i) 
	{
		return	this.proc.getUserPoint(i);
	}
	
	public STATE getUserState(int i)
	{
		return this.proc.getUserState(i);
	}
	
	public Point getItems(int i)
	{
		return this.proc.getItems(i);
	}
	
	public int getItemNum()
	{
		return this.proc.getItemNum();
	}
	
	public String getItemName(int i, int n)
	{
		return this.proc.getItemName(i, n);
	}
	
	public int getUserItemNum(int i)
	{
		return this.proc.getUserItemNum(i);
	}
	
	public int getUserDirection(int i)
	{
		return this.proc.getUserDirection(i);
	}
	
	//v1.1에서 추가. display에서 보낸 사용자 입력을 process로 보냄
	/*
	public void receiveCommand(String command)
	{
		System.out.println("1");
		proc.receiveCommand(command);
	}*/

	public void reMap(BufferedImage img)
	{
		this.dis.reMap(img);
	}
	
	public String getMapName()
	{
		return this.proc.getMapName();
	}
	
	public int getUserHp(int i)
	{
		return this.proc.getUserHp(i);
	}
	
	public void setChar(Point p,int index)
	{
		dis.setCharPoint(p,index);
	}
	
	public void setMissile(Point p, int index)
	{
		dis.setMissilePoint(p,index);
	}
	
	public void pushLeftButton() 
	{
		this.sendData("move:l");
	}

	public void pushRightButton()
	{
		this.sendData("movd:r");
	}

	public static void main(String[] args)
	{
		new Client();
	}
}