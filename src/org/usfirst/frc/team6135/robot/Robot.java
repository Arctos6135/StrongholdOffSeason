package org.usfirst.frc.team6135.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
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
    final String lowBarAuto = "Low bar";
    final String autoMID = "AutoMID";
    final String autoMIDLeft = "AutoMIDLeft";
    final String autoLEFT= "AutoLEFT";
    String autoSelected;
    SendableChooser chooser;
    
	Drive robot;
	RobotArm arm;
	RobotShooter shooter;
	Joystick driveStick;
	Joystick operatorStick;
	AutoPhase autoPhase;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        chooser.addObject("Low Bar", lowBarAuto);
        chooser.addObject("AutoMID", autoMID);
        chooser.addObject("AutoMIDLeft", autoMIDLeft);
        chooser.addObject("AutoLEFT", autoLEFT);
        SmartDashboard.putData("Auto choices", chooser);

		driveStick = new Joystick(Constants.dStick);
		operatorStick = new Joystick(Constants.sStick);
		robot = new Drive(driveStick, Constants.rVicPort, Constants.lVicPort, Constants.lEnc1, Constants.lEnc2, Constants.rEnc1, Constants.rEnc2);
		//arm = new RobotArm(operatorStick, Constants.wVicPort, Constants.lArmTal, Constants.rArmTal);
		shooter = new RobotShooter(operatorStick, Constants.shootLFTalon, Constants.shootLBVic, Constants.shootRFTalon, Constants.shootRBVic);
		autoPhase = new AutoPhase(robot); 
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
		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	switch(autoSelected) {
    	case lowBarAuto:
         autoPhase.autoProcess3();  
            break;
    	case autoMID:
    		autoPhase.autoProcess3Right();
    		break;
    	case autoMIDLeft:
    		autoPhase.autoProcess3();
    		break;
    	case autoLEFT:
    		autoPhase.autoProcess5();
    		break;
    	default:
    	//Put default auto code here
            break;
    	}
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        robot.teleopDrive();
        shooter.shooterTick();
        //arm.rotateArm();
		robot.toSmartDashboard();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
