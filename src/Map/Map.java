package Map;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Map {
	private String name;
	private BufferedImage mapImage;
	private BufferedImage bombImage;
	
	private int w, h;
	private int bw, bh;
	private int[] pixels;
	private int[] pixel_bomb;
	
	public Map(String name)
	{
		this.name = name;
		try {
			this.mapImage = ImageIO.read(new File(".\\Map\\" + this.name + "\\" + this.name + "_bw.gif"));
			this.bombImage = ImageIO.read(new File(".\\Map\\bomb.gif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.w = mapImage.getWidth();
		this.h = mapImage.getHeight();
		this.bw = bombImage.getWidth();
		this.bh = bombImage.getHeight();
		
		this.pixels = new int[this.w*this.h];
		this.pixel_bomb = new int[this.bw*this.bh];
		
		this.mapImage.getRGB(0, 0, this.w, this.h, this.pixels, 0, this.w);
		this.bombImage.getRGB(0, 0, this.bw, this.bh, this.pixel_bomb, 0, this.bw);
		
		System.out.println(getPixel(30,30));
	}

	// 한 픽셀을 구해서 정보를 리턴
	public int getPixel(int x, int y)
	{
		return pixels[x*this.w + y];
	}
	
	// 범위안의 값을 모두 &해서 리턴
	public int getRangePiexels(int x, int y, int w, int d)
	{
		// 범위 안의 값
		if(x+w <= this.w && y+d <= this.h)
		{
			int result = 0xffffffff;
						
			for(int i=y; i<y+d; i++)
				for(int j=x; j<x+w; j++)
				{
	//				System.out.println(this.getPixel(i, j));
					result = result & this.getPixel(i, j);
				}
		//	System.out.println(result);
			return result;
		}
		
		else
			return 0;
		
	}

	public void bomb(Point mp)
	{		
		for(int i=0; i<bh; i++)
		{
			for(int j=0; j<bw; j++)
			{
				// 터지는 부분이면(흰 부분)
				if(pixel_bomb[i*bw + j] == 0xffffffff)
					// 그 부분의 맵을 투명으로 바꿔줌
					if(((mp.y+25-125)+i)*w + (mp.x+25-150) + j <= this.w*this.h)
						pixels[((mp.y+25-125)+i)*w + (mp.x+25-150) + j] = 0x00ffffff;
			}
		}
		
		this.mapImage.setRGB(0, 0, w, h, pixels, 0, w);
	}
	
	public int getW()
	{
		return this.w;
	}
	
	public int getH()
	{
		return this.h;
	}

	public String getName()
	{
		return this.name;
	}
	
	public BufferedImage getMapImage()
	{
		return this.mapImage;
	}
	
	abstract public Point getStartLandPoint();
	abstract public Point getStartSkyPoint();
}
