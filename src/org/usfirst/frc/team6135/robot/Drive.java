package org.usfirst.frc.team6135.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {
	//Encoder constants
	private static final boolean lDirReverse = true;
	private static final boolean rDirReverse = false;
	private static final int minRate = 10;
	private static final int sampleAverage = 14;
	private static final double wheelRadius = 4; //Inches
	private static final double wheelCircum = 2 * Math.PI * wheelRadius;

	//Drive constants
	private static final double sensitivity = 0;
	private static final double inputPow = 3;
	private static final int yAxis = 1;
	private static final int yReverse = -1;
	private static final int xAxis = 0;
	private static final int xReverse = 1;
	private static final double accBound = 0.7; //The speed after which the drive starts to accelerate over time
	private static final int accLoop = 15; //The number of loops for the bot to accelerate to max speed
	private static final double compensateDeadzone = 2;
	private static final double maxCompensate = 0.2;
	private static final double compensateInc = 0.001;
	private static final int lReverse = 1;
	//private static final boolean lReverse; will replace int
	//private static final boolean rReverse; will replace int
	private static final int rReverse = -1;
	private static final double motorCurvePow = 1.0; //The type of curve relating pwm to velocity
	private static final boolean useSensitivityCalc = true;
	private static final boolean useAccelerateCalc = true;
	private static final boolean useCompensate = false;

	//Object Declarations
	private Joystick driveStick = null;
	private Victor leftDrive = null;
	private Victor rightDrive = null;
	private Encoder leftEnc = null;
	private Encoder rightEnc = null;

	//Variables Used
	private double xInput;
	private double yInput;
	private double xFinal;
	private double yFinal;
	private int accLoopCount = 0;
	private double lCompensate = 0.0;
	private double rCompensate = 0.0;
	private double compensate = 0.0;
	private double lSpeed = 0.0;
	private double rSpeed = 0.0;
	private double lPrev = 0.0;
	private double rPrev = 0.0;
	private int leadMotor = -1; //-1 means neither motor is being scaled    0 means left motor is being scaled down    1 means right motor is being scaled down
	
	//Constructors
	public Drive(Joystick j, int rDrive, int lDrive, int lEnc1, int lEnc2, int rEnc1, int rEnc2) {
		driveStick = j;
		leftDrive = new Victor(lDrive);
		rightDrive = new Victor(rDrive);
		leftEnc = new Encoder(lEnc1, lEnc2, lDirReverse, Encoder.EncodingType.k2X);
		rightEnc = new Encoder(rEnc1, rEnc2, rDirReverse, Encoder.EncodingType.k2X);
		leftEnc.setMinRate(minRate);
		rightEnc.setMinRate(minRate);
		leftEnc.setSamplesToAverage(sampleAverage);
		rightEnc.setSamplesToAverage(sampleAverage);
		leftEnc.setDistancePerPulse(wheelCircum);
		rightEnc.setDistancePerPulse(wheelCircum);
		leftDrive.set(0);
		rightDrive.set(0);
		SmartDashboard.putBoolean("Left Motor Reverse", leftDrive.getInverted());
		SmartDashboard.putBoolean("Right Motor Reversed", rightDrive.getInverted());
	}
	
	//Direct object access methods
	public double getleftDis() {
		return leftEnc.getDistance();
	}
	public double getRightDis() {
		return rightEnc.getDistance();
	}
	public void setMotors(double l, double r) {//sets motor speeds accounting for directions of motors
		leftDrive.set(lReverse * (lPrev = l));
		rightDrive.set(rReverse * (rPrev = r));
	}
	public void setLeft(double d) {
		leftDrive.set(d);
	}
	public void setRight(double d) {
		rightDrive.set(d);
	}
	public void encReset() {
		leftEnc.reset();
		rightEnc.reset();
		lCompensate = 1.0;
		rCompensate = 1.0;
	}
	
	private double angle(double x, double y) {
	    return Math.toDegrees(Math.atan2(x, y));
	}
	private double scaleCompensate(double d) { //This methods converts the compensate value found with the compensate method into what the speeds are actually scaled by
		return d / (2 * (1 + Math.abs(d))) + 1;
	}
	private void compensate() {//work in progress
		
	}
	public void getSpeeds() {//converts input values to motor speeds
		if (yFinal > 0.0) {
    		if (xFinal > 0.0) {
        	    rSpeed = yFinal - xFinal;
        	    lSpeed = Math.max(yFinal, xFinal);
        	}
			else {
        	    rSpeed = Math.max(yFinal, -xFinal);
        	    lSpeed = yFinal + xFinal;
        	}
    	}
		else {
        	if (xFinal > 0.0) {
        	    rSpeed = -Math.max(-yFinal, xFinal);
        	    lSpeed = yFinal + xFinal;
        	}
			else {
        	    rSpeed = yFinal - xFinal;
        	    lSpeed = -Math.max(-yFinal, -xFinal);
        	}
    	}
	}
	public void getValues(double x, double y, boolean sensitivity, boolean acc) {//takes input values and applies appropriate calculations to values to account for human error and/or joystick deficiencies
		xFinal = x;
		yFinal = y;
		if(sensitivity) {
			xFinal = sensitivity2Calc(xFinal);
			yFinal = sensitivityCalc(yFinal);
		}	

		if(acc) {
			xFinal = accCalc(xFinal);
			yFinal = accCalc(yFinal);
		}
	}
	
	//Teleop Driving methods
	private double accCalc(double input) {//applies a delay for motors to reach full speed for larger joystick inputs
		if(input > accBound && accLoopCount < accLoop) {//positive inputs
	    		return accBound + (input - accBound) * (accLoopCount++ / (double) accLoop);
	    }
		else if(input < -accBound && accLoopCount < accLoop) {//negative inputs
	    		return -accBound + (input + accBound) * (accLoopCount++ / (double) accLoop);
	    }
	    else if(Math.abs(input) <= accBound) {
	    	accLoopCount = 0;
	    }
		return input;
	}
	private double sensitivityCalc(double input) {//Squares magnitude of input to reduce magnitude of smaller joystick inputs
		if (input >= 0.0) {
	        return (input * input);
        }
		else {
    	    return -(input * input);
    	}
	}
	private double sensitivity2Calc(double input) {//Alternate sensitivity calculation currently not being used
		return sensitivity * input + (1 - sensitivity) * Math.pow(input, inputPow);
	}

	private void exactDrive() {//This method will essentially "round" the joystick inputs to purely in the x direction or purely in the y direction depending on which has greater magnitude 
		if(Math.abs(yInput) > Math.abs(xInput)) {
			xInput = 0;
		}
		else {
			yInput = 0;
		}
	}
	public void teleopDrive() {//Implements individual driving and input methods to allow driving in teleop
		xInput = xReverse * driveStick.getRawAxis(xAxis);
		yInput = yReverse * driveStick.getRawAxis(yAxis);
		if(Math.abs(xInput) < 0.2) {
			xInput = 0;
		}
		if(Math.abs(yInput) < 0.2) {
			yInput = 0;
		}
		if(Math.abs(xInput) < 0.1 && Math.abs(yInput) < 0.1) {
			encReset();
			setMotors(0, 0);
		}
		else {
			if(driveStick.getRawButton(Constants.exactDriveButton)) {
				exactDrive();
			}
			
			getValues(xInput, yInput, useSensitivityCalc, useAccelerateCalc);
			
			getSpeeds();
			
			
			if(useCompensate) {
				compensate();
			}
			setMotors(lSpeed, rSpeed);
		}

	}
	//Alternate drive method
	public void driveTrain(){
		if(driveStick.getRawButton(Constants.exactDriveButton)) {
			leftDrive.set(-0.5);
			rightDrive.set(0.5);		
		}
		else if(driveStick.getRawButton(1)) {
			leftDrive.set(0.5);
			rightDrive.set(-0.5);
		}
		else {
			//quadrant 2
			if (driveStick.getRawAxis(xAxis)< -0.2 && driveStick.getRawAxis(yAxis)<-0.2){
				rightDrive.set(-0.5); //cables resulted inverted motor
				leftDrive.set(Math.max(-1 * driveStick.getRawAxis(yAxis)/2, -0.5));				
			}
			//quadrant 1
			else if(driveStick.getRawAxis(xAxis)> 0.2 && driveStick.getRawAxis(yAxis)<-0.2){
				rightDrive.set(Math.min(driveStick.getRawAxis(yAxis)/2, -0.5));
				leftDrive.set(0.5);
			}
			//quadrant 3
			else if(driveStick.getRawAxis(xAxis)> 0.2 && driveStick.getRawAxis(yAxis)> 0.2){
				leftDrive.set(-0.5);
				rightDrive.set(Math.min(driveStick.getRawAxis(yAxis)/2,0.5));
			}
			//quadrant 4
			else if(driveStick.getRawAxis(xAxis) < -0.2 && driveStick.getRawAxis(yAxis)>0.2){
				rightDrive.set(0.5);
				leftDrive.set(Math.min(driveStick.getRawAxis(yAxis)/2, 0.5));
			}
			else if(driveStick.getRawAxis(xAxis)>=-0.2 && driveStick.getRawAxis(xAxis)<=0.2 &&driveStick.getRawAxis(yAxis)<=-0.2){
				rightDrive.set(driveStick.getRawAxis(yAxis));
				leftDrive.set(-1*driveStick.getRawAxis(yAxis));
			}
			else if(driveStick.getRawAxis(xAxis)>=-0.2 && driveStick.getRawAxis(xAxis)<=0.2 &&driveStick.getRawAxis(yAxis)>=0.2){
				rightDrive.set(driveStick.getRawAxis(yAxis));
				leftDrive.set(-1*driveStick.getRawAxis(yAxis));
			}
			else if(driveStick.getRawAxis(yAxis)>= -0.2 && driveStick.getRawAxis(yAxis)<=0.2 &&driveStick.getRawAxis(xAxis)<=-0.2){
				rightDrive.set(driveStick.getRawAxis(xAxis));
				leftDrive.set(driveStick.getRawAxis(xAxis));
			}
			else if(driveStick.getRawAxis(yAxis)>= -0.2 && driveStick.getRawAxis(yAxis)<=0.2 &&driveStick.getRawAxis(xAxis)>=0.2){
				rightDrive.set(driveStick.getRawAxis(xAxis));
				leftDrive.set(driveStick.getRawAxis(xAxis));
			}
			else {
				rightDrive.set(0);
				leftDrive.set(0);
			}
				
			//leftDrive.set((0.5 * j.getRawAxis(xAxis) + 0.5)*(-1 * j.getRawAxis(yAxis)));
			//leftDrive2.set((0.5 * j.getRawAxis(xAxis) + 0.5)*(-1 * j.getRawAxis(yAxis)));
			//rightDrive.set((-0.5 * j.getRawAxis(xAxis) + 0.5)*(-1 * j.getRawAxis(yAxis)));
			//leftDrive.set(-1 * xReverse * j.getRawAxis(yAxis) - j.getRawAxis(xAxis));
			//leftDrive2.set(-1 * xReverse * j.getRawAxis(yAxis) - j.getRawAxis(xAxis));
			//rightDrive.set(-1 * yReverse * j.getRawAxis(yAxis) + j.getRawAxis(xAxis));
		}
	}
	
	//Autonomous driving
	public void autoDrive(double speed)
	{
		compensate();
		setMotors(speed,speed);
	}
	public void autoDrive(double l,double r)
	{
		setMotors(l,r);
	}
	
	//Debugging and calibration methods
	public void motorDirectionTest() {//Allows for testing direction of individual motors; should be used with toSmartDashboard in a loop
		if(driveStick.getRawButton(7)) {
			setLeft(0.6);
		}
		else if(driveStick.getRawButton(8)) {
			setLeft(-0.6);
		}
		else {
			setLeft(0);
		}
		
		if(driveStick.getRawButton(9)) {
			setRight(0.6);
		}
		else if(driveStick.getRawButton(10)) {
			setRight(-0.6);
		}
		else {
			setLeft(0);
		}
	}
	public void toSmartDashboard()
	{
		SmartDashboard.putNumber("xInput: ", xInput);
		SmartDashboard.putNumber("yInput: ", yInput);
		SmartDashboard.putNumber("xFinal: ", xFinal);
		SmartDashboard.putNumber("yFinal: ", yFinal);
		SmartDashboard.putNumber("lComp: ", lCompensate);
		SmartDashboard.putNumber("rComp: ", rCompensate);
		SmartDashboard.putNumber("lSpeed: ", lSpeed);
		SmartDashboard.putNumber("rSpeed: ", rSpeed);
		SmartDashboard.putNumber("lRate: ", leftEnc.getRate());
		SmartDashboard.putNumber("rRate: ", rightEnc.getRate());
	}
}
