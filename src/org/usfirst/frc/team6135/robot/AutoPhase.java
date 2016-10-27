package org.usfirst.frc.team6135.robot;
//May require slight modification of this or drive class to work correctly in current drive class
public class AutoPhase {
	public static double fullBlastDis=212;
	public static double turningDis=0.5;
	public static double scoringDis=80;
	public static int counter=0;
	Drive robot;
	public AutoPhase(Drive r) {
		this.robot = r;
		robot.encReset();
	}
	public void blastThrough(double travelDis)
	{
		robot.autoDrive(0.5);
		if(Math.min(robot.getleftDis(),robot.getRightDis())<travelDis)
		{
			robot.autoDrive(0.5);
		}
		else
		{
			robot.autoDrive(0);
			robot.encReset();
			counter++;
		}
	}
	public double calcTurnDis(double degree)
	{
		return Math.abs(degree)*Math.PI/180.0*21.25/2;
	}
	public void turn(double degree)
	{
		turningDis=calcTurnDis(degree);
		if(degree>0)
		{
			if(robot.getleftDis()<turningDis)
			{
				robot.autoDrive(0.5, 0);
			}
			else
			{
				robot.autoDrive(0);
				robot.encReset();
				counter++;
			}
		}
		else
		{
			if(robot.getRightDis()<turningDis)
			{
				robot.autoDrive(0.5, 0);
			}
			else
			{
				robot.autoDrive(0);
				robot.encReset();
				counter++;
			}
		}
	}
	public void scoreTravel()
	{
		if(Math.min(robot.getleftDis(),robot.getRightDis())<scoringDis)
		{
			robot.autoDrive(0.7);
		}
		else if(Math.min(robot.getleftDis(),robot.getRightDis())<scoringDis){
			robot.autoDrive(turningDis, 0);
		}
		else{
			robot.autoDrive(0);
			robot.encReset();
			counter++;
		}
	}
	public void autoProcesses(int processNum,double val) //val = dis when processNum=1 else val=degree
	{
		if(processNum==1)
		{
			blastThrough(val);
		}
		if(processNum==2)
		{
			turn(val);
		}
		if(processNum==3)
		{
			scoreTravel();
		}
	}
	public void idle()
	{
		robot.autoDrive(0);
		robot.encReset();
	}
	public void autoProcess1()
	{
		int[] processNums={1,            2,3};
		double[] vals=    {fullBlastDis,60,0};
		if(counter<3)
		{
			autoProcesses(processNums[counter],vals[counter]);
		}
		else
		{
			idle();
		}
	}
	public void autoProcess3()
	{
		int[] processNums={1,    2,1,  2, 1 ,2, 3};
		double[] vals=    {180,-90,100,90,84,60,0};
		if(counter<7)
		{
			autoProcesses(processNums[counter],vals[counter]);
		}
		else
		{
			idle();
		}
	}
	public void autoProcess3Right()
	{
		int[] processNums={1,    2,1,   2, 1 ,2, 3};
		double[] vals=    {180,90,100,-90,84,-60,0};
		if(counter<7)
		{
			autoProcesses(processNums[counter],vals[counter]);
		}
		else
		{
			idle();
		}
	}
	public void autoProcess5()
	{
		int[] processNums= {1             ,2,3};
		double[] vals=     {fullBlastDis,-60,0};
		if(counter<3)
		{
			autoProcesses(processNums[counter],vals[counter]);	
		}
		else
		{
			idle();
		}
	}
}
