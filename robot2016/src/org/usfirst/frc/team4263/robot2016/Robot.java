
package org.usfirst.frc.team4263.robot2016;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	boolean initalized = false;
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
	RobotDrive drive;
	Talon Loader1;
	Talon Loader2;
	Talon Shooter1;
	Talon Shooter2;
	Joystick inputJoystick;
	VictorSP left1;
	VictorSP left2;
	VictorSP right1;
	VictorSP right2;
    Compressor comp;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        
        /*
         * Drive motors
         */
        left1  = new VictorSP(0); // □  -- square
        left2  = new VictorSP(1); // ○  -- circle
        right1 = new VictorSP(2); // △   -- triangle
        right2 = new VictorSP(3); // ☆  -- star
        //RobotDrive takes:
        //RobotDrive(frontLeft, rearLeft, frontRight, rearRight)
        //For us, front and back have little meaning
        drive = new RobotDrive(left1, left2, right1, right2);
        
        /*
         * Ball loader
         */
        Loader1  = new Talon(5); // X -- cross
        Loader2  = new Talon(6); // Arrow
        /*
         * Ball shooter
         */
        Shooter1 = new Talon(7); // Pentagon
        Shooter2 = new Talon(8); // Crescent
        
        //comp = new Compressor(0);
        
        /*
         * The joysticks are indexed by the USB port they're plugged into
         * file:///C:/Users/robotics/wpilib/java/current/javadoc/edu/wpi/first/wpilibj/Joystick.html
         * Use the driver station to figure out which index to use below.
         */
        inputJoystick = new Joystick(1);
        
        initalized = true;
    }
    public void teleopInit(){
    	if(!initalized){
    		this.robotInit();
    	}
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
    	/*
    	 * See robotInit above for a to learn 
    	 * the pin mapping for each of these
    	 * objects
    	 */
    	if(inputJoystick.getRawButton(1)){
    		Shooter1.set(1);
    		Shooter2.set(-1);
    	}
    	else if(inputJoystick.getRawButton(3)){
    		Shooter1.set(-1);
    		Shooter2.set(1);
    	}
    	else{
    		Shooter1.set(0);
    		Shooter2.set(0);
    	}
    	if(inputJoystick.getRawButton(2)){
    		Loader1.set(-1);
    		Loader2.set(1);
    	}
    	else if(inputJoystick.getRawButton(4)){
    		Loader1.set(1);
    		Loader2.set(-1);
    	}
    	else{
    		Loader1.set(0);
    		Loader2.set(0);
    	}
    	drive.arcadeDrive(inputJoystick);
    	Timer.delay(0.01);
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    
    }
    
}
