package org.usfirst.frc.team6135.robot;
//May require slight modification of this or drive class to work correctly in current drive class
public class AutoPhase {
	public final double robotRadius=21.25/2;
	public static double fullBlastDis=212;
	public static double turningDis=0;
	public static double scoringDis=80;
	public static int counter=0;
	Drive robot;
	public AutoPhase(Drive r) {
		this.robot = r;
		robot.encReset();
	}
	public void blastThrough(double travelDis)
	{
		if(Math.min(robot.getleftDis(),robot.getRightDis())<travelDis)
		{
			robot.autoDrive(1.0);
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
		return 2*Math.PI*robotRadius*Math.abs(degree)/360;
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
				robot.autoDrive(0, 0.5);
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
			robot.autoDrive(-0.7);
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
		double[] vals=    {fullBlastDis,240,0};
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
		double[] vals=    {180,-90,100,90,84,240,0};
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
		double[] vals=    {180,90,100,-90,84,-240,0};
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
		double[] vals=     {fullBlastDis,-240,0};
		if(counter<3)
		{
			autoProcesses(processNums[counter],vals[counter]);	
		}
		else
		{
			idle();
		}
	}
	/*void driveStraightDistance(int tenthsOfIn, int masterPower)
	{
	  int tickGoal = (42 * tenthsOfIn) / 10;
	 
	  //This will count up the total encoder ticks despite the fact that the encoders are constantly reset.
	  int totalTicks = 0;
	 
	  //Initialise slavePower as masterPower - 5 so we don't get huge error for the first few iterations. The
	  //-5 value is based off a rough guess of how much the motors are different, which prevents the robot from
	  //veering off course at the start of the function.
	  int slavePower = masterPower - 5; 
	 
	  int error = 0;
	 
	  int kp = 5;
	 
	  SensorValue[leftEncoder] = 0;
	  SensorValue[rightEncoder] = 0;
	 
	  //Monitor 'totalTicks', instead of the values of the encoders which are constantly reset.
	  while(abs(totalTicks) < tickGoal)
	  {
	    //Proportional algorithm to keep the robot going straight.
	    motor[leftServo] = masterPower;
	    motor[rightServo] = slavePower;
	 
	    error = SensorValue[leftEncoder] - SensorValue[rightEncoder];
	 
	    slavePower += error / kp;
	 
	    SensorValue[leftEncoder] = 0;
	    SensorValue[rightEncoder] = 0;
	 
	    wait1Msec(100);
	 
	    //Add this iteration's encoder values to totalTicks.
	    totalTicks+= SensorValue[leftEncoder];
	  }
	  motor[leftServo] = 0; // Stop the loop once the encoders have counted up the correct number of encoder ticks.
	  motor[rightServo] = 0;  
	}*/
}

