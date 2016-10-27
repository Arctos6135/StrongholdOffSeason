package org.usfirst.frc.team6135.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;
//import edu.wpi.first.wpilibj.buttons.Button;

public class TestDrive {
	Joystick joystick = new Joystick(0);
	//Joystick yjoystick = new Joystick(1);
	Victor leftDrive = new Victor(0);
	Victor rightDrive = new Victor(1);
	RobotDrive robotDrive = new RobotDrive(leftDrive, rightDrive);
	//Button button = new Button(0);
	
	
	public void teleopPeriodic(){
		//robotDrive.arcadeDrive(xjoystick, 0, yjoystick, 1, true);
		robotDrive.arcadeDrive(joystick, true);
		if(joystick.getRawButton(3)){
			robotDrive.drive(0.5, 0.5);
        }
	}
	
	
}

