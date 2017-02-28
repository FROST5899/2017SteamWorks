package org.usfirst.frc.team5899.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class EncoderTest {
	Encoder leftDriveEnc;
	Encoder rightDriveEnc;
	Encoder winchEnc;
	Encoder deliveryEnc;
	
	public EncoderTest(Encoder leftDrive, Encoder rightDrive, Encoder winch, Encoder delivery){
		this.leftDriveEnc = leftDrive;
		this.rightDriveEnc = rightDrive;
		this.winchEnc = winch;
		this.deliveryEnc = delivery;
	}
	
	public void displayEncoderValues(){
		double leftDriveEncVal = leftDriveEnc.getRaw();
		double rightDriveEncVal = rightDriveEnc.getRaw();
		double winchEncVal = winchEnc.getRaw();
		double deliveryEncVal = deliveryEnc.getRaw();
		
		SmartDashboard.putNumber("Left Drive", leftDriveEncVal);
		SmartDashboard.putNumber("Right Drive", rightDriveEncVal);
		SmartDashboard.putNumber("Winch", winchEncVal);
		SmartDashboard.putNumber("Delivery", deliveryEncVal);
		SmartDashboard.putNumber("test", 50);
		
		
		
	}
	
	
	
	
	
	
	
	
}

//Old code that could be run outside the class
		//teleop code:
		/*leftDriveEncoder.getRaw();
		rightDriveEncoder.getRaw();
		winchEncoder.getRaw();
		deliveryEncoder.getRaw();
		double leftDriveEncVal = leftDriveEncoder.getRaw();
		double rightDriveEncVal = rightDriveEncoder.getRaw();
		double winchEncVal = winchEncoder.getRaw();
		double deliveryEncVal = deliveryEncoder.getRaw();
		
		SmartDashboard.putNumber("Left Drive", leftDriveEncVal);
		SmartDashboard.putNumber("Right Drive", rightDriveEncVal);
		SmartDashboard.putNumber("Winch", winchEncVal);
		SmartDashboard.putNumber("Delivery", deliveryEncVal);
		SmartDashboard.putNumber("test", 45);
		SmartDashboard.putBoolean("Left Drive Stopped", leftDriveEncoder.getStopped());
		SmartDashboard.putBoolean("Right Drive Stopped", rightDriveEncoder.getStopped());
		SmartDashboard.putBoolean("Winch Stopped", winchEncoder.getStopped());
		SmartDashboard.putBoolean("Delivery Stopped", deliveryEncoder.getStopped());*/
		
		//robotInit code
		/*
		
		*/