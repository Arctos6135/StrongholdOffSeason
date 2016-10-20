package org.usfirst.frc.team6135.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

public class Drive {
	//Encoder constants
	private static final boolean lDirReverse = false;
	private static final boolean rDirReverse = false;
	private static final int minRate = 10;
	private static final int sampleAverage = 14;
	private static final double wheelRadius = 12; //Inches
	private static final double wheelCircum = Math.PI * wheelRadius;

	//Drive constants
	private static final double sensitivity = 0.3;
	private static final double inputPow = 3;
	private static final int yAxis = 0;
	private static final int yReverse = -1;
	private static final int xAxis = 1;
	private static final int xReverse = 1;
	private static final double accBound = 0.7; //The speed after which the drive starts to accelerate over time
	private static final int accLoop = 15; //The number of loops for the bot to accelerate to max speed
	private static final int angleRange = 25;
	private static final double maxCompensate = 0.2;
	private static final double compensateInc = 0.01;
	private static final int lReverse = 1;
	private static final int rReverse = 1;

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
	private double lSpeed = 0.0;
	private double rSpeed = 0.0;

	public Drive(Joystick j, int rDrive, int lDrive, int lEnc1, int lEnc2, int rEnc1, int rEnc2) {
		driveStick = j;
		leftDrive = new Victor(lDrive);
		rightDrive = new Victor(rDrive);
		leftEnc = new Encoder(lEnc1, lEnc2, lDirReverse, Encoder.EncodingType.k2X);
		rightEnc = new Encoder(rEnc1, rEnc2, rDirReverse, Encoder.EncodingType.k2X);
		leftEnc.setMinRate(minRate);
		rightEnc.setMinRate(minRate);
		leftEnc.setDistancePerPulse(wheelCircum);
		rightEnc.setDistancePerPulse(wheelCircum);
		leftEnc.setSamplesToAverage(sampleAverage);
		rightEnc.setSamplesToAverage(sampleAverage);
	}
	private double sensitivityCalc(double input) {
		return sensitivity * input + (1 - sensitivity) * Math.pow(input, inputPow);
	}
	private double sensitivity2Calc(double input) {
		if (input >= 0.0) {
	        return (input * input);
        }
		else {
    	    return -(input * input);
    	}
	}
	private double angle() {
		return Math.toDegrees(Math.atan2(yInput, xInput));
	}
	private double accCalc(double input) {
		if(input > accBound && accLoopCount < accLoop) {
	    		return accBound + (input - accBound) * (accLoopCount++ / (double) accLoop);
	    }
		else if(input < -accBound && accLoopCount < accLoop) {
	    		return -accBound + (input + accBound) * (accLoopCount++ / (double) accLoop);
	    }
	    else if(Math.abs(input) <= 0.7) {
	    	accLoopCount = 0;
	    }
		return input;
	}
	private void encReset() {
		leftEnc.reset();
		rightEnc.reset();
		lCompensate = 0.0;
		rCompensate = 0.0;
	}
	private void compensate() {//Will modify this method
		if(leftEnc.getDistance() > rightEnc.getDistance() + 3 * wheelCircum / 2) {
			lCompensate = Math.min(lCompensate, lCompensate + maxCompensate - (rCompensate + compensateInc));
			rCompensate = Math.min(maxCompensate, rCompensate + compensateInc);
			return;
		}
		if(rightEnc.getDistance() > leftEnc.getDistance() + 3 * wheelCircum / 2) {
			rCompensate = Math.min(rCompensate, rCompensate + maxCompensate - (lCompensate + compensateInc));
			lCompensate = Math.min(maxCompensate, lCompensate + compensateInc);
			return;
		}
	}
	public void getSpeeds() {
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
	public void getValues() {
		xFinal = (xInput = xReverse * driveStick.getRawAxis(xAxis));
		yFinal = (yInput = yReverse * driveStick.getRawAxis(yAxis));
		
		xFinal = sensitivity2Calc(xFinal);
		yFinal = sensitivity2Calc(yFinal);

		xFinal = accCalc(xFinal);
		yFinal = accCalc(yFinal);
		
		getSpeeds();

		if(Math.abs(Math.abs(angle()) - 90) < angleRange) {//Will modify
			compensate();
		}
		else {
			encReset();
		}
	}
	public void getValues(double x, double y, boolean sensitivity, boolean acc) {
		xFinal = (xInput = xReverse * x);
		yFinal = (yInput = yReverse * y);
		
		if(sensitivity) {
			xFinal = sensitivity2Calc(xFinal);
			yFinal = sensitivity2Calc(yFinal);
		}	

		if(acc) {
			xFinal = accCalc(xFinal);
			yFinal = accCalc(yFinal);
		}
			
		getSpeeds();

		if(Math.abs(angle()) == 90) {
			compensate();
		}
		else {
			encReset();
		}
	}
	public void setMotors() {
		leftDrive.set(lReverse * (lSpeed + lCompensate));
		rightDrive.set(rReverse * (rSpeed + rCompensate));
	}
	public void setMotors(double l, double r) {
		leftDrive.set(lReverse * (l + lCompensate));
		rightDrive.set(rReverse * (r + rCompensate));
	}
}
