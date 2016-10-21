package org.usfirst.frc.team6135.robot;

public class Constants {
	//Ports
	//Motor controllers for driving
	public static final int lVicPort = 0;
	public static final int rVicPort = 1;
	
	//Victors for Robot Arm
	public static final int wVicPort = 9;

	//Victors for shooter
	public static final int shootLFTalon=0;
	public static final int shootLBVic = 2;
	public static final int shootRFTalon=1;
	public static final int shootRBVic = 3;
	
	//CANTalons for arm
	public static final int lArmTal = 9;
	public static final int rArmTal = 8;

	//Encoders for distance and balancing motors
	public static final int lEnc1 = 0;
	public static final int lEnc2 = 1;
	public static final int rEnc1 = 2;
	public static final int rEnc2 = 3;
	
	//Joystick
	public static final int dStick = 0;
	public static final int sStick = 1;
	
	//Buttons
	//Logitech Joystick
	//Buttons for Robot Driving
	public static final int exactDriveButton = 2;
	
	//Xbox 360 gamepad
	//Buttons for shooting
	public static final int outTakeButton = 1;
	public static final int inTakeButton = 7;

	//Buttons for Robot Arm Control
	public static final int extendArmButton = 3;
	public static final int contractArmButton = 4;
	public static final int rotateUpButton = 5;
	public static final int rotateDownButton = 6;
}
