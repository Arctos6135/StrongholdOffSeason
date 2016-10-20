
package org.usfirst.frc.team6135.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
    Compressor airCompressor = new Compressor();
    DoubleSolenoid doubleSolenoid1 = new DoubleSolenoid(0, 1);
    Joystick joystick = new Joystick(0);
    RobotArm arm=new RobotArm(joystick);
    int winchButton = 8;
    int leftEncoderSignalA = 1;
    int leftEncoderSignalB = 0;
    int rightEncoderSignalA = 3;
    int rightEncoderSignalB = 2;
	//Joystick yjoystick = new Joystick(1);
    Encoder leftEncoder = new Encoder(leftEncoderSignalA, leftEncoderSignalB, false, Encoder.EncodingType.k1X);
    Encoder rightEncoder = new Encoder(rightEncoderSignalA, rightEncoderSignalB, false, Encoder.EncodingType.k1X);
	Victor leftDrive = new Victor(0);
	Victor rightDrive = new Victor(1);
	double wheelRadius = 12.0;
	double wheelCircum = Math.PI * wheelRadius;
	RobotDrive robotDrive = new RobotDrive(leftDrive, rightDrive);
	double rDistance;
	double lDistance;
	double encDiffDeadzone = 0.05;
	double leftPWM=0.5;
	double rightPWM=0.5;
	Servo testServo=new Servo(9);
	CANTalon leftTalon=new CANTalon(0);
	CANTalon rightTalon=new CANTalon(1);
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        leftTalon.setControlMode(0);
        //leftTalon.set(outputValue);
        rightTalon.setControlMode(5);
        //rightTalon.set();
        //airCompressor.setClosedLoopControl(true);
        //airCompressor.start();
        //winchVictor.set(1.0);
        //winchVictor.set(-1.0);
        //leftEncoder.setDistancePerPulse(wheelCircum);
		//rightEncoder.setDistancePerPulse(wheelCircum);
        //robotDrive.setLeftRightMotorOutputs(0.1, 0.1);
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	switch(autoSelected) {
    	case customAuto:
        //Put custom auto code here   
            break;
    	case defaultAuto:
    	default:
    	//Put default auto code here
            break;
    	}
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	//robotDrive.arcadeDrive(joystick, true);
    	//robotDrive.drive(0.3, -0.1);
    	testServo.set(1);
    	arm.extendContractArm();
    	robotDrive.setLeftRightMotorOutputs(leftPWM, rightPWM);
    	System.out.println(leftPWM+" "+rightPWM);
    	System.out.println("R "+((-1)*rightEncoder.getRate()));
    	System.out.println("L "+(leftEncoder.getRate()));
    	//System.out.println(rightEncoder.getRate());
		if(joystick.getRawButton(3)){	
			robotDrive.drive(0.5, -1);
        }
		/*
		/*if(trajectory is not forwards or backwards) {
			leftEncoder.reset();
			rightEncoder.reset();
		*/
		if ((-1)*rightEncoder.getRate()!=leftEncoder.getRate()) {
			if((-1)*rightEncoder.getRate()<leftEncoder.getRate())
			{
				if(rightPWM<0.7)
				{
					rightPWM+=0.01;
				}
				else
				{
					leftPWM-=0.01;                                       
					rightPWM=0.7;
				}
			}
			else
			{
				if(leftPWM<0.7)
				{
					leftPWM+=0.01;
				}
				else
				{
					rightPWM-=0.01;
					leftPWM=0.7;
				}
			}
		}
		else if(rDistance > encDiffDeadzone + lDistance) {
			
			/*if(speed is less than or equal to 0.7) {
		 	 	increase speed of left victor 
			}
			else if(speed is greater than 0.7) {
				decrease speed of right victor
			}*/
		}

    }
    
    /*
     This function is called periodically during test mode
     */
    public void testPeriodic() {
    	airCompressor.start();
    }
    
}
