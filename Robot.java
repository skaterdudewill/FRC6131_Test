package org.usfirst.frc.team6131.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import java.lang.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	RobotDrive myRobot;
	Joystick stick;
	int autoLoopCounter;
	int autoLoopCounter2;
	long autoMillisCounter;
	String nextAutoAction;
	long nextActionTime;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	myRobot = new RobotDrive(0,1);
    	stick = new Joystick(0);
    	
    	
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
    	autoLoopCounter = 0;
    	autoLoopCounter2 = 0;
    	autoMillisCounter = System.currentTimeMillis();
    	nextAutoAction = "Step 1";
    	nextActionTime = System.currentTimeMillis() + 2000;
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
			if (nextAutoAction == "Step 1") {
				if (System.currentTimeMillis() < nextActionTime) {
					myRobot.drive(-1.0, 0.0);
				} else {
					nextAutoAction = "Step 2";
					nextActionTime = System.currentTimeMillis() + 2000;
				}
			}
			
			if (nextAutoAction == "Step 2") {
				if (System.currentTimeMillis() < nextActionTime) {
					myRobot.drive(0.0, 0.0);
				} else {
					nextAutoAction = "Step 3";
					nextActionTime = System.currentTimeMillis() + 2000;
				}
			}
			if (nextAutoAction == "Step 3") {
				if (System.currentTimeMillis() < nextActionTime) {
					myRobot.drive(1.0, 0.0);
				} else {
					nextAutoAction = "Step 4";
					nextActionTime = System.currentTimeMillis() + 2000;
				}
			}
			if (nextAutoAction == "Step 4") {
				if (System.currentTimeMillis() < nextActionTime) {
					myRobot.drive(0.0, 0.0);
				} else {
					nextAutoAction = "Step 5";
					nextActionTime = System.currentTimeMillis() + 2000;
				}
			}
	    }
    
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit(){
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	boolean button = stick.getRawButton(1);
        myRobot.arcadeDrive(stick, 5, stick, 4, true);
        if (button == true) {
        	myRobot.drive(1, 0.0);
        }
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
}
