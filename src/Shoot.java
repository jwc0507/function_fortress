import java.awt.Point;
import java.util.*;

public class Shoot {
	private int ex3;
	private int ex2;
	private int ex1;
	private int ex0;
	private Point startPoint; // char
	private double zeroPoint; // x zeropoint
	private double x;
	private int direction;
	
	//��� 0���� �ʱ�ȭ
	public Shoot(Point start)
	{
		this.x = 0;
		this.zeroPoint = 0;
		this.ex3 = 0;
		this.ex2 = 0;
		this.ex1 = 0;
		this.ex0 = 0;
		this.startPoint = start;
		
	}
	
	public void setFunction(int exp3, int exp2, int exp1, int exp0)
	{
		
		this.ex3 = exp3;
		this.ex2 = exp2;
		this.ex1= exp1;
		this.ex0 = exp0;		
		System.out.println("[Shoot class] function set complete");
		System.out.println(this.ex3+","+this.ex2+","+this.ex1+","+this.ex0);
	}
	
	// x���� ���� �̻��� ��ġ ���. d : ��Ÿ��
	//d =1 -> ���������� �� d= -1 �������� ��
	
	//1.1v���� ���� ����
	public void setZeroPoint(int d,Point currentPoint) //currentPoint = ĳ������ ���� ��ǥ
	{
		direction = d;
		this.startPoint = currentPoint;
		//this.startPoint.x = startPoint.x + 25;
		x=0; 
		double D2 = ex1 * ex1 - 4*ex2 * ex0;
		//3�� ������ ���� �� �� ��. ���� ����
		double D3 = 0;
		double F = 0;
		double G = 0;
		double H = 0;
		double I = 0;
		double J = 0;
		double K = 0;
		double L = 0; 
		double M = 0;
		double N = 0; 
		double P = 0;
	
		// 3��������
		if(ex3!=0)
		{	
			D3 = Math.pow(ex1,2)*Math.pow(ex2,2)-4*ex0*Math.pow(ex2,3)-4*ex3*Math.pow(ex1,3)+18*ex3*ex2*ex1*ex0-27*Math.pow(ex0,2)*Math.pow(ex3,2);
			F = (((3*ex1)/ex3) - (Math.pow(ex2,2)/Math.pow(ex3,2)))/3;
			G = ((2*Math.pow(ex2,3)/Math.pow(ex3,3))-((9*ex2*ex1)/Math.pow(ex3,2))+(27*ex0/ex3)) / 27;
			H = (Math.pow(G,2)/4)+(Math.pow(F,3)/27);
			
			if(D3 ==0)
				zeroPoint = Math.cbrt(ex0/ex3)*(-1);
			else if(D3 > 0)
			{
				I = Math.sqrt((Math.pow(G,2)/4 - H)); 
				J = Math.cbrt(I);
				K = Math.acos(-(G/(2*I)));
				L = J * (-1);
				M = Math.cos(K/3);
				N = Math.sqrt(3) * Math.sin(K/3);
				P = (ex2/(3*ex3))*(-1);
				if(d==1)
					zeroPoint = min( (2*J)*Math.cos(K/3)-(ex2/(3*ex3)), L * (M + N) + P,L * (M - N) + P);
				else
					zeroPoint = max( (2*J)*Math.cos(K/3)-(ex2/(3*ex3)), L * (M + N) + P,L * (M - N) + P);
			}
		}
		//2��
		else if(ex2!=0)
		{
			if(D2<0)
				System.out.println("���");
			else if(D2 == 0)
			{
				zeroPoint = -ex1/(2*ex2);
			}
			else
			{
				if(d==1)
					zeroPoint = min((-ex1 + Math.sqrt(D2))/(2*ex2),(-ex1 -Math.sqrt(D2))/ (2*ex2));
					//zeroPoint = -ex1 -Math.sqrt(D2)/ (2*ex2);
				else
				//	zeroPoint = -ex1 + Math.sqrt(D2)/(2*ex2);
					zeroPoint = max((-ex1 + Math.sqrt(D2))/(2*ex2),(-ex1 -Math.sqrt(D2))/ (2*ex2));

			}
			
		}
		
		//1��
		else
		{
			zeroPoint = -ex0/(double)ex1;
		}
		
		//������ ����
		System.out.println("[Shoot class] origin zeoroPoint : "+zeroPoint);
		
	//	x + startPoint.getX, y + startPoint.getY 
	
	}
	
	//x���� 1�� ������Ű�鼭 �˵��� �׸���. 
	//1.1v���� ���� ����
	public Point getPoint()
	{
		Point missilePoint = new Point();
		missilePoint.setLocation(startPoint.getX()+50*x, startPoint.getY()-20*getMissileY() ); // -> ĳ������ �� ��ġ ����ؼ� �˵� ���
		
		if(direction==1)
		{	
			zeroPoint+=0.02;
			x+=0.02;
		}	
		else
		{	zeroPoint-=0.02;
			x-=0.02;	
		}
		
		return missilePoint;
	}
	public double getMissileY()
	{
		System.out.println("[ShootClass]getMissileY's exp :"+ex3+","+ex2+","+ex1+","+ex0);
		System.out.println("zeroPoint++ : "+zeroPoint);
		double y = ex3 * Math.pow(zeroPoint, 3) + ex2 * Math.pow(zeroPoint,2)+ ex1 * Math.pow(zeroPoint, 1) + ex0;
		System.out.println("getMissileY : "+y);
		return y;
	}
	//1.1v���� max,min �Լ� �߰�
	
	//2�������� �� �߿��� ū �� ���� �� ���
	public double max(double a, double b)
	{
		if(a>b)
			return a;
		else
			return b;
	}
	//3�������� �� �� ���� ū �� ���� �� ���
	public double max(double a, double b, double c)
	{
		double buffer[] ={a,b,c};
		double max= buffer[0];
		
		for(int i=0; i<3; i++)
			if(max<buffer[i])
				max = buffer[i];
		return max;
	}
	//2�������� �� �� ���� �� ���� �� ���
	public double min(double a, double b)
	{
		if(a>b)
			return b;
		else 
			return a;
	}
	//3�������� �� �߿��� ���� ���� �� ���� �� ���
	public double min(double a, double b, double c)
	{
		double buffer[] ={a,b,c};
		double min= buffer[0];
		
		for(int i=0; i<3; i++)
			if(min>buffer[i])
				min = buffer[i];
		return min;
	}
}
