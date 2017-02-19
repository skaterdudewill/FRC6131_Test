package org.usfirst.frc.team6131.robot;

	import edu.wpi.first.wpilibj.DigitalInput;
	import edu.wpi.first.wpilibj.IterativeRobot;
	import edu.wpi.first.wpilibj.Joystick;
	import edu.wpi.first.wpilibj.RobotDrive;
	import edu.wpi.first.wpilibj.Spark;
	import edu.wpi.first.wpilibj.SpeedController;
	import edu.wpi.first.wpilibj.Talon;
	import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.lang.*;
	import edu.wpi.first.wpilibj.CameraServer;
	import edu.wpi.first.wpilibj.SampleRobot;
	import edu.wpi.first.wpilibj.Servo;
	import edu.wpi.first.wpilibj.Timer;
	
	/**
	 * The VM is configured to automatically run this class, and to call the
	 * functions corresponding to each mode, as described in the IterativeRobot
	 * documentation. If you change the name of this class or the package after
	 * creating this project, you must also update the manifest file in the resource
	 * directory.
	 */
	public class Robot extends IterativeRobot {
		
		// setup the main drive variables
	
		// which PWMs are the drive motors connected to?
		int driveLeftPWM=1;
		int driveRightPWM=0;
		
		// which joystick will drive the robot?
		int driveJoystick=0;
	
		// which xbox axes will control the driving?
		int driveYAxis=5;
		int driveXAxis=4;
		
		// camera axes
		int cameraYAxis=1;
		int cameraXAxis=0;
		
		// Camera PWMS
		int cameraYPWM = 3;
		int cameraXPWM = 4;
		
		// Camera Servo
		Servo cameraYServo = new Servo(cameraYPWM);
		Servo cameraXServo = new Servo(cameraXPWM);
		
		// Keep up with the Y Angle
		int cameraYAngle;
		int cameraXAngle;
		
		// Min-Maxes for Camera Angle
		int cameraYMax = 170;
		int cameraYMin = 0;
		int cameraXMax = 170;
		int cameraXMin = 0;
		int cameraXCenter = 90;
		int cameraYCenter = 90;
		int cameraCenterController = 9;
		boolean cameraCenterButton = false;
		
		// 
		int cameraXMultiplier = 2;
		int cameraYMultiplier = 2;
		
		// which xbox buttons will control the drive multiplier up/down?
		int multUpButton=6;
		int multDnButton=5;
		
		// to reverse the values of an axis, set its swap value to -1 below. otherwise, leave it at 1
		int driveYAxisSwap=-1;
		int driveXAxisSwap=-1;
		
		// variables used for drive multiplier adjustments
		boolean multUpButtonState;
		boolean multDnButtonState;	
		double driveMultiplier;
	
		// how slow/fast should the robot be able to drive?
		double minDriveMultiplier=0.4;
		double maxDriveMultiplier=1.0;
		
		// PWM for the limit switch motor
		int trayMotorPWM;
		
		// Buttons for tray motors
    	boolean trayButtonUp = false;
        boolean trayButtonDown = false;
        int trayButtonUpController = 2;
        int trayButtonDownController = 3;
		
		// Robot drive for the limit switch motor;
		private SpeedController trayMotor;
		
		
		RobotDrive myRobot;
		Joystick stick;
		
		// DIO for tray limit switches
		DigitalInput trayLimitUp;
		DigitalInput trayLimitDown;
		boolean trayLimitUpPressed, trayLimitDownPressed;

		
		// automonous mode step variables, and time-for-next-step variables
		long autoMillisCounter;
		String nextAutoAction;
		long nextActionTime;
		
		// Gear step variables, and time-for-next-step variables
		long gearMillisCounter;
		String nextGearAction;
		long nextGearActionTime;
		int gearButtonController = 1;
		boolean gearButton = false;
		int gearPWM = 5;
		Servo gearServo = new Servo(gearPWM);
		int gearAngle;
		int gearAngleMin = 0;
		int gearAngleMax = 170;
		long gearStep2Time = 1000;
		
		// Setup camera variables
		CameraServer server;
		
		// Setting up tray button re-press tracker
		boolean trayButtonStillPressed;
		
		// Set up tray motor speed
		double trayMotorUpSpeed;
		double trayMotorDownSpeed;
	    /**
	     * This function is run when the robot is first started up and should be
	     * used for any initialization code.
	     */
	    public void robotInit() {
	    	// setup camera
	    	server = CameraServer.getInstance();
	    	server.setQuality(50);
	    	// Camera server name in web interface
	    	server.startAutomaticCapture("cam0");
	    	server.setSize(640);
	    	
	    	// setup tray limit switches
	    	trayLimitUp = new DigitalInput(0);
	    	trayLimitDown = new DigitalInput(1);

	    	
	    	// setup pwm for limit switch motor
	    	trayMotorPWM = 2;
	    	
	    	
	    	trayMotor = new Spark(trayMotorPWM);
	    	myRobot = new RobotDrive(driveLeftPWM, driveRightPWM);
	    	stick = new Joystick(driveJoystick);
	    	
	    	cameraYAngle = cameraYCenter;
	    	cameraYServo.setAngle(cameraYAngle);
	    	
	    	cameraXAngle = cameraXCenter;
	    	cameraXServo.setAngle(cameraXAngle);
	    	
	    	gearAngle = gearAngleMin;
	    	gearServo.setAngle(gearAngle);
	    }
	    
	    /**
	     * This function is run once each time the robot enters autonomous mode
	     */
	    public void autonomousInit() {
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
	        driveMultiplier=minDriveMultiplier;
	        multUpButtonState=false;
	        trayMotorUpSpeed = 1.0;
	        trayMotorDownSpeed = 0.7;
	        // Gear Servo Setup
	        gearMillisCounter = System.currentTimeMillis();
	    	nextGearAction = "Step 1";
	    	nextGearActionTime = System.currentTimeMillis() + 2000;
	        
	    }
	
	    /**
	     * This function is called periodically during operator control
	     */
	    public void teleopPeriodic() {
	    	// setup tray motor buttons
	    	trayButtonUp = stick.getRawButton(trayButtonDownController);
	    	trayButtonDown = stick.getRawButton(trayButtonUpController);
	    	
	    	// get values for the drive multiplier up/down buttons
	    	boolean multUpValue=stick.getRawButton(multUpButton);
	    	boolean multDnValue=stick.getRawButton(multDnButton);
	
	    	if (multUpButtonState==true){
	    		// button was pushed
	    		if (multUpValue==false){
	    			// button was pushed, now it's not
	        		multUpButtonState=false;
	    		} else {
	    			// button was pushed, it's still pushed
	    		}
	    	} else {
	    		// button was not pushed
	    		if (multUpValue==true){
	    			// button was not pushed, but it is now
					driveMultiplier+=0.1;
					multUpButtonState=true;
	    		} else {
	    			// button was not pushed, it's still not pushed
	    		}
			}    		  
	    	
	    	if (multDnButtonState==true){
	    		// button was pushed
	    		if (multDnValue==false){
	    			// button was pushed, now it's not
	        		multDnButtonState=false;
	    		} else {
	    			// button was pushed, it's still pushed
	    		}
	    	} else {
	    		// button was not pushed
	    		if (multDnValue==true){
	    			// button was not pushed, but it is now
					driveMultiplier-=0.1;
					multDnButtonState=true;
	    		} else {
	    			// button was not pushed, it's still not pushed
	    		}
			}  
	    	
	    	if (driveMultiplier > maxDriveMultiplier) driveMultiplier = maxDriveMultiplier;
	    	if (driveMultiplier < minDriveMultiplier) driveMultiplier = minDriveMultiplier;

	    	// see if the tray limit switches are closed
	    	if (trayLimitUp.get()){
	    		trayLimitUpPressed=false;
	    	} else {
	    		trayLimitUpPressed=true;
	    		trayButtonStillPressed = true;
	    	}
	    	
	    	if (trayLimitDown.get()){
	    		trayLimitDownPressed=false;
	    	} else {
	    		trayLimitDownPressed=true;
	    	}
	    	
	    	if (trayButtonUp == true || trayButtonDown == true) {
	    		// a button is pushed
	    		if (trayButtonUp == true && trayLimitUpPressed == false) {
	    			//Safe to go up
	    			if (trayButtonStillPressed == true ) {
	    				// but don't go up until the button is re-pressed
	    			} else {
	    				trayMotor.set(trayMotorUpSpeed);
	    			}
	    		} else if(trayButtonDown == true && trayLimitDownPressed==false) {
	    			trayMotor.set(-1 * trayMotorDownSpeed);
	    		} else {
	    			trayMotor.set(0.0);
	    		}
	    	} else {
	    		trayMotor.set(0.0);
	    		trayButtonStillPressed = false;
	    	}
	    	
	    	// read camera center button
	    	cameraCenterButton = stick.getRawButton(cameraCenterController);
	    	if(cameraCenterButton) {
	    		cameraYAngle = cameraYCenter;
	    		cameraXAngle = cameraXCenter;
	    		cameraYServo.setAngle(cameraYAngle);
	    		cameraXServo.setAngle(cameraXAngle);
	    	
	    	} else {
		    	// Grab Camera Joystick Values
		        double camerajsy= (stick.getRawAxis(cameraYAxis));
		        SmartDashboard.putNumber("Joystick Value", camerajsy);
		    	if (camerajsy > 0.5) { 
		    		// Move the camera up
		    		cameraYAngle += cameraYMultiplier;
		    		if (cameraYAngle > cameraYMax) {
		    			cameraYAngle = cameraYMax;
		    		}
		    		cameraYServo.setAngle(cameraYAngle);
		    	} else if (camerajsy < -0.5) {
		    		// Move the camera up
		    		cameraYAngle -= cameraYMultiplier;
		    		if (cameraYAngle < cameraYMin) {
		    			cameraYAngle = cameraYMin;
		    		}
		    		cameraYServo.setAngle(cameraYAngle);
		    	}
		    	
		        double camerajsx = (stick.getRawAxis(cameraXAxis));	    
		    	if (camerajsx > 0.5) {
		    		// Move the camera up
		    		cameraXAngle += cameraXMultiplier;
		    		if (cameraXAngle > cameraXMax) {
		    			cameraXAngle = cameraXMax;
		    		}
		    		cameraXServo.setAngle(cameraXAngle);
		    	} else if (camerajsx < -0.5) {
		    		// Move the camera up
		    		cameraXAngle -= cameraXMultiplier;
		    		if (cameraXAngle < cameraXMin) {
		    			cameraXAngle = cameraXMin;
		    		}
		    		cameraXServo.setAngle(cameraXAngle);
		    	}
	    	}
	       
	    	// Right joystick drive
	    	double drivey= (stick.getRawAxis(driveYAxis) * driveYAxisSwap) * driveMultiplier;
	        double drivex = (stick.getRawAxis(driveXAxis) * driveYAxisSwap) * driveMultiplier;
	    	myRobot.arcadeDrive(drivey, drivex, true);
	    	
	    	// perform gear actions
	    	gearButton = stick.getRawButton(gearButtonController);

	    	if (nextGearAction == "Step 1") {
	    		if (gearButton) {
	    			// Button was pushed  			
					nextAutoAction = "Step 2";
				}
			}
			if (nextGearAction == "Step 2") {
				gearServo.setAngle(gearAngleMax);
				nextGearActionTime = System.currentTimeMillis() + gearStep2Time;
				nextGearAction = "Step 3";
			}
			if (nextGearAction == "Step 3") {
				if (System.currentTimeMillis() >= nextGearActionTime) {
					gearServo.setAngle(gearAngleMin);
					nextGearActionTime = System.currentTimeMillis() + gearStep2Time;
					nextGearAction = "Step 4";
				}
			}
			if (nextGearAction == "Step 4") {
				if (!gearButton) {
					nextGearAction = "Step 1";
				}
			} 	
	    }
	    
	    /**
	     * This function is called periodically during test mode
	     */
	    public void testPeriodic() {
	    	LiveWindow.run();
	    }
	    
	}
