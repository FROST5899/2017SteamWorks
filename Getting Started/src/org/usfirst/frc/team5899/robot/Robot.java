package org.usfirst.frc.team5899.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import com.ctre.CANTalon;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	CANTalon Talon1 = new CANTalon(1);
	CANTalon Talon2 = new CANTalon(2);
	CANTalon Talon3 = new CANTalon(3);
	CANTalon Talon4 = new CANTalon(4);
	CANTalon Talon5 = new CANTalon(5);
	CANTalon Talon6 = new CANTalon(6);
	CANTalon Talon7 = new CANTalon(7);
	CANTalon Talon8 = new CANTalon(8);
	RobotDrive myRobot = new RobotDrive(Talon1, Talon2, Talon3, Talon4);
	Joystick stick = new Joystick(2);
	Timer timer = new Timer();


	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
	}

	/**
	 * This function is run once each time the robot enters autonomous mode
	 */
	@Override
	public void autonomousInit() {
		//timer.reset();
		//timer.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		// Drive for 2 seconds
		/*if (timer.get() < 2.0) {
			//myRobot.drive(-0.5, 0.0); // drive forwards half speed
		} else {
			//myRobot.drive(0.0, 0.0); // stop robot
		}*/
	}

	/**
	 * This function is called once each time the robot enters tele-operated
	 * mode
	 */
	@Override
	public void teleopInit() {
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		double winchAxis = stick.getRawAxis(1);
		//myRobot.arcadeDrive(stick);
		Talon1.set(winchAxis);
		
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		//LiveWindow.run();
	}
}
