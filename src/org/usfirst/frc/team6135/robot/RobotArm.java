package org.usfirst.frc.team6135.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

public class RobotArm {
	Joystick joystick;
	Victor winchVictor = new Victor(2);
	double winchSpeed = 1.0;
	int extendArmButton=7;
	int contractArmButton=8;
	public RobotArm(Joystick j)
	{
		this.joystick=j;
	}
	public void setwinchVictor(double speed)
	{
		winchVictor.set(speed);
	}
	public void extendContractArm()
	{
		if(joystick.getRawButton(extendArmButton)) {
			setwinchVictor(winchSpeed);
		}
		else if(joystick.getRawButton(contractArmButton)) {
			setwinchVictor(-winchSpeed);
		}
		else
		{
			setwinchVictor(0);
		}
	}
}
