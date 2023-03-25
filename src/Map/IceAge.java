package Map;
import java.awt.*;
import java.util.*;

public class IceAge extends Map
{
	private Point[] startLandPoint = new Point[6];//맵의 스타팅 포인트 
	private Point[] startSkyPoint = new Point[6];
	private boolean[] landFlag = new boolean[6];//해당 스타팅랜드포인트가 사용되었는지를 표시하는 flag
	private boolean[] skyFlag = new boolean[6];//해당 스타팅스카이포인트가 사용되었는지를 표시하는 flag
	private int seed;
	public IceAge(int seed)
	{
		super("IceAge");
		
		this.seed = seed;
		//init landFlag
		for(int i=0; i<6; i++)
		{
			landFlag[i] = false;
			skyFlag[i] = false;
		}
		
		//init startLandPoint
		startLandPoint[0]  = new Point(93,250);
		startLandPoint[1] = new Point(346,255);
		startLandPoint[2] = new Point(579,210);
		startLandPoint[3] = new Point(755,250);
		startLandPoint[4] = new Point(1129,236);
		startLandPoint[5] = new Point(1380,285);
		//init startSkyPoint
		startSkyPoint[0] = new Point(100,96);
		startSkyPoint[1] = new Point(318,144);
		startSkyPoint[2] = new Point(667,56);
		startSkyPoint[3] = new Point(825,140);
		startSkyPoint[4] = new Point(1138,117);
		startSkyPoint[5] = new Point(1390,70);
	}
	
	public Point getStartLandPoint()
	{
		Random r = new Random();
		r.setSeed(this.seed);
		int ranNum = r.nextInt(6);//난수 생성
		
		while(this.landFlag[ranNum])//할당되지 않은 startLandPoint 찾기
		{
			ranNum = r.nextInt(6);
		}
		
		this.landFlag[ranNum] = true; 
		return this.startLandPoint[ranNum];
	}
	

	public Point getStartSkyPoint()
	{
		Random r = new Random();
		r.setSeed(this.seed);
		int ranNum = r.nextInt(6);//난수 생성
		
		while(this.skyFlag[ranNum])//할당되지 않은 startSkyPoint 찾기
		{
			ranNum = r.nextInt(6);
		}
		
		this.skyFlag[ranNum] = true; 
		return this.startSkyPoint[ranNum];
	}
}
