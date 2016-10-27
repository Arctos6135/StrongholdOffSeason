package org.usfirst.frc.team6135.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotArm {
	//Winch Constants
	private static final double winchSpeed = 1.0;
	private static final double canSpeed = 10.0;
	
	//Object Declarations
	private Joystick joystick = null;
	private Victor winchVictor = null;
	private CANTalon leftTalon = null;
	private CANTalon rightTalon = null;
	
	public RobotArm(Joystick j, int wVic, int lTalon, int rTalon) {
		joystick = j;
		winchVictor = new Victor(wVic);
		leftTalon = new CANTalon(lTalon);
		rightTalon = new CANTalon(rTalon);
		leftTalon.setControlMode(0);
		rightTalon.setControlMode(0);
	}
	private void setwinchVictor(double speed)
	{
		winchVictor.set(speed);
	}
	private void setTalonVal(double t1,double t2)
	{
		leftTalon.set(t1);
		rightTalon.set(t2);
	}
	public void extendContractArm()
	{
		if(joystick.getRawButton(Constants.extendArmButton)) {
			setwinchVictor(winchSpeed);
			SmartDashboard.putString("Arm extension", "extending");
		}
		else if(joystick.getRawButton(Constants.contractArmButton)) {
			setwinchVictor(-winchSpeed);
			SmartDashboard.putString("Arm extension", "contracting");
		}
		else
		{
			setwinchVictor(0);
			SmartDashboard.putString("Arm extension", "neutral");
		}
	}
	public void rotateArm() {
		if(joystick.getRawButton(Constants.rotateUpButton)) {
			setTalonVal(canSpeed , canSpeed);
			SmartDashboard.putString("Arm rotation", "up");
		}
		else if(joystick.getRawButton(Constants.rotateDownButton)) {
			setTalonVal(-canSpeed, -canSpeed);
			SmartDashboard.putString("Arm rotation", "down");
		}
		else {
			//setTalonVal(0, 0);//Not sure if necessary or not
			SmartDashboard.putString("Arm rotation", "neutral");
		}
	}
}
