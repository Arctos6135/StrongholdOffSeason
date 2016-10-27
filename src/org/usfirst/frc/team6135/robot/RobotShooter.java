package org.usfirst.frc.team6135.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotShooter {
	
	
	//Speed Constants
	private static final double motorSpeedFront = 1.0;
	private static final double motorSpeedBack = 0.5;
	private static final double motorSpeedStop = 0.0;
	
	//Joystick constants
	private static final int counterBound = 25;
	//object declarations
	private Joystick joystick = null;
	private CANTalon shooterLeftFront = null;
	private Victor shooterLeftBack = null;
	private CANTalon shooterRightFront = null;
	private Victor shooterRightBack = null;
	
	//variables used
	private int counter = 0;
	
	public RobotShooter(Joystick j, int shootLF, int shootLB, int shootRF, int shootRB) {
		joystick = j;
		shooterLeftFront = new CANTalon(shootLF);
		shooterLeftBack = new Victor(shootLB);
		shooterRightFront = new CANTalon(shootRF);
		shooterRightBack = new Victor(shootRB);
		shooterLeftFront.setControlMode(0);
		shooterRightFront.setControlMode(0);
		counter = 0;
		SmartDashboard.putBoolean("Left Front Motor Reversed", shooterLeftFront.getInverted());
		SmartDashboard.putBoolean("Left Back Motor Reversed", shooterLeftBack.getInverted());
		SmartDashboard.putBoolean("Right Front Motor Reversed", shooterRightFront.getInverted());
		SmartDashboard.putBoolean("Right Back Motor Reversed", shooterRightBack.getInverted());
	}
	private void inTake() {
		shooterLeftFront.set(motorSpeedFront);
		shooterRightFront.set(-motorSpeedFront);
		shooterLeftBack.set(motorSpeedStop);
		shooterRightBack.set(motorSpeedStop);
		counter = 0;
	}
	private void shootOut() {
		if(counter < counterBound)
		{
			shooterLeftFront.set(-motorSpeedBack);
			shooterRightFront.set(motorSpeedBack);
			shooterLeftBack.set(motorSpeedStop);
			shooterRightBack.set(motorSpeedStop);
			counter++;
		}
		else
		{
			shooterLeftFront.set(-motorSpeedFront);
			shooterRightFront.set(motorSpeedFront);
			shooterLeftBack.set(motorSpeedBack);
			shooterRightBack.set(-motorSpeedBack);
		}

	}
	private void setZero()
	{
		shooterLeftFront.set(0);
		shooterRightFront.set(0);
		shooterLeftBack.set(0);
		shooterRightBack.set(0);
		counter = 0;
	}
	public void shooterTick() {
		if(joystick.getRawAxis(2) > 0.5) {
			inTake();
			SmartDashboard.putString("Shooter", "intaking");
		}
		else if(joystick.getRawAxis(3) > 0.5) {
			shootOut();
			SmartDashboard.putString("Shooter", "shooting");
		}
		else {
			setZero();
			SmartDashboard.putString("Shooter", "SETZERO");
		}
		SmartDashboard.putNumber("Shooter counter", counter);
	}
}
