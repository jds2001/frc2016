
package org.usfirst.frc.team4263.robot2016;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CameraServer;

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
	Talon Loader;
	Talon Shooter1;
	Talon Shooter2;
	Joystick joystick1;
	Joystick joystick2;
	VictorSP left1;
	VictorSP left2;
	VictorSP right1;
	VictorSP right2;
	Servo    triggerServo;
    Compressor comp;
    DoubleSolenoid solenoid1;
    // Solenoid solenoid2;
    CameraServer frontcam;
    enum tstate {
    	Open,
    	Holding,
    	Firing
    };
    tstate triggerState = tstate.Holding;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        
        CameraServer.getInstance().setQuality(50);
        CameraServer.getInstance().startAutomaticCapture();
;        
        /*
         * Drive motors
         */
        left1  = new VictorSP(0); // □  -- square
        left2  = new VictorSP(1); // ○  -- circle
        right1 = new VictorSP(2); // △   -- triangle
        right2 = new VictorSP(3); // ☆  -- star
        left1.setInverted(true);
        left2.setInverted(true);
        right1.setInverted(true);
        right2.setInverted(true);
        //RobotDrive takes:
        //RobotDrive(frontLeft, rearLeft, frontRight, rearRight)
        //For us, front and back have little meaning
        drive = new RobotDrive(left1, left2, right1, right2);
        
        /*
         * Ball loader
         */
        Loader  = new Talon(5); // half circle
        /*
         * Ball shooter
         */
        Shooter1 = new Talon(7); // Pentagon
        Shooter2 = new Talon(8); // Crescent
        triggerServo  = new Servo(9);
        
        
        comp = new Compressor(0);
        solenoid1 = new DoubleSolenoid(0, 1);
        // solenoid2 = new Solenoid(1);
        /*
         * The joysticks are indexed by the USB port they're plugged into
         * file:///C:/Users/robotics/wpilib/java/current/javadoc/edu/wpi/first/wpilibj/Joystick.html
         * Use the driver station to figure out which index to use below.
         */
        joystick1 = new Joystick(0);
        joystick2 = new Joystick(1);
        
        initalized = true;
    }
    public void teleopInit(){
    	if(!initalized){
    		this.robotInit();
    	}
    }
    public void disabledInit(){
    	if(initalized){
    		drive.stopMotor();
    		Loader.stopMotor();
    		Shooter1.stopMotor();
    		Shooter2.stopMotor();
    		triggerServo.set(0);
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
    private void handleTrigger(){
    	boolean updateTrigger = false;
	/*
	 * The trigger is a state machine with three states:
	 * 	Firing:
	 * 		The Servo is pushed fully inside the robot, if there is a ball in the robot,
	 * 		it pressing it into the shooter wheels.
         *	Holding:
	 *		The Servo is forming a gate, blockig the ball from rolling out of the front of
	 *		the robot, but not pressing it into the shooter wheels
	 *	Open:
	 *		The Servo is open as wide as possible, ready to take in a ball.
	 *
	 *	Firing can only be transitioned to from holding. All other transitions can happen from
	 *	any other state.
	 *
	 *	The trigger transitions to the firing state, button 6 transitons to open, and button 7 transitions to holding
	 */
    	if(joystick1.getTrigger()){
    		if(triggerState == tstate.Holding){
    			System.out.println("Firing");
    			triggerState = tstate.Firing;
    			updateTrigger = true;
    		}
    	}
    	if(joystick1.getRawButton(6)){
    		if(triggerState != tstate.Open){
    			System.out.println("Opening");
    			triggerState = tstate.Open;
    			updateTrigger = true;
    		}
    	}
    	if(joystick1.getRawButton(7)){
    		if(triggerState != tstate.Holding){
    			System.out.println("Holding");
    			triggerState = tstate.Holding;
    			updateTrigger = true;
    		}
    	}
    	if(updateTrigger){
		/*
		 * Servo objects can be set to a range between 0.0 and 1.0
		 * however, the range of our servo is between 0 and 0.065
		 */
    		switch(triggerState){
			case Firing:
				triggerServo.set(0);
				break;
			case Holding:
				triggerServo.set(.03);
				break;
			case Open:
				triggerServo.set(.065);
				break;
    		}
    	}
    }
    public void buttonList(){
	/*
	 * Helper function to figure out what the button mapping was on the joystick
	 */
    	for(int i = 1; i < joystick1.getButtonCount(); ++i){
    		System.out.println(i + " " + (joystick1.getRawButton(i)?"Pressed":"Not Pressed"));
    	}
    	Timer.delay(0.5);
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
    	comp.setClosedLoopControl(true);
    	handleTrigger();

    	if(joystick1.getRawButton(2)){
    		Shooter1.set(1);
    		Shooter2.set(-1);
    	}
    	else if(joystick1.getRawButton(3)){
		//Spin up the shooter
    		Shooter1.set(-1);
    		Shooter2.set(1);
    	}
    	else{
    		Shooter1.set(0);
    		Shooter2.set(0);
    	}
    	if(joystick1.getRawButton(2)){
    		Loader.set(-1);
    	}
    	else if(joystick1.getRawButton(4)){
    		Loader.set(1);
    	}
    	else if(joystick1.getRawButton(5)){
    		Loader.set(-1);
    	}
    	else{
    		Loader.set(0);
    	}
    	if(joystick1.getRawButton(6)) {
    		solenoid1.set(DoubleSolenoid.Value.kReverse);
    	}
    	else if (joystick1.getRawButton(7)) {
    		solenoid1.set(DoubleSolenoid.Value.kForward);
    	}
    	else {
    		solenoid1.set(DoubleSolenoid.Value.kOff);
    	}
    	drive.tankDrive(joystick1, joystick2);
    	Timer.delay(0.01);
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    
    }
    
}
