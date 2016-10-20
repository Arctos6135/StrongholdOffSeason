package org.usfirst.frc.team6135.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

public class RobotShooter {
	Joystick joystick;
	Victor shooterLeft=new Victor(4);
	Victor shooterRight=new Victor(5);
	int inTakeSpeed;
	public void inTake()
	{
		shooterLeft.set(inTakeSpeed);
		shooterRight.set(inTakeSpeed);
	}
	public void shootOut()
	{
		
	}
}
