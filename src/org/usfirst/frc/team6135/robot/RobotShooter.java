package org.usfirst.frc.team6135.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

public class RobotShooter {
	private Joystick joystick = null;
	private CANTalon shooterLeftFront = null;
	private Victor shooterLeftBack = null;
	private CANTalon shooterRightFront = null;
	private Victor shooterRightBack = null;
	private int counter=0;
	
	//Speed Constants
	private static final double motorSpeedFront = 1.0;
	private static final double motorSpeedBack = 0.5;
	private static final double motorSpeedStop = 0.0;
	public RobotShooter(Joystick j, int shootLF, int shootLB, int shootRF, int shootRB)
	{
		joystick = j;
		shooterLeftFront = new CANTalon(shootLF);
		shooterLeftBack = new Victor(shootLB);
		shooterRightFront = new CANTalon(shootRF);
		shooterRightBack = new Victor(shootRB);
		shooterLeftFront.setControlMode(0);
		shooterRightFront.setControlMode(0);
		counter=0;
	}
	private void inTake() {
		shooterLeftFront.set(motorSpeedFront);
		shooterRightFront.set(-motorSpeedFront);
		shooterLeftBack.set(-motorSpeedBack);
		shooterRightBack.set(motorSpeedBack);
	}
	private void shootOut() {
		if(counter<25)
		{
			shooterLeftFront.set(-motorSpeedFront);
			shooterRightFront.set(motorSpeedFront);
			shooterLeftBack.set(motorSpeedStop);
			shooterRightBack.set(motorSpeedStop);
			counter++;
		}
		else
		{
			shooterLeftFront.set(-motorSpeedFront);
			shooterRightFront.set(motorSpeedFront);
			shooterLeftBack.set(motorSpeedFront);
			shooterRightBack.set(-motorSpeedFront);
		}

	}
	private void setZero()
	{
		shooterLeftFront.set(0);
		shooterRightFront.set(0);
		shooterLeftBack.set(0);
		shooterRightBack.set(0);
	}
	public void shooterTick()
	{
		if(joystick.getRawButton(Constants.inTakeButton))
		{
			inTake();
			counter=0;
		}
		else
		{
			if(joystick.getRawButton(Constants.outTakeButton)) {
				shootOut();
			}
			else
			{
				setZero();
				System.out.println("SETZERO");
			}
		}	
	}
}
