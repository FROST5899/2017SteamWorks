package org.usfirst.frc.team5899.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class EncoderDrive {
	Encoder encoderLeft, encoderRight;
	RobotDrive drive;
	double conversion = 8*(Math.PI);
	double leftConst = 1410;
	double rightConst = 1450;
	public EncoderDrive(RobotDrive drive, Encoder encoderLeft, Encoder encoderRight){
		this.drive = drive;
		this.encoderLeft = encoderLeft;
		this.encoderRight = encoderRight;
		
		
	}
	
	public boolean driveStraight(double distance){
		boolean done;
		double left , right;
		double leftLim = distance*leftConst/conversion;
		double rightLim = distance*rightConst/conversion;
		if ( Math.abs(Math.abs(encoderLeft.getRaw()) - leftLim) > 50 && Math.abs(Math.abs(encoderRight.getRaw()) - rightLim) > 50) {
			if (Math.abs(encoderLeft.getRaw())< leftLim){
				left = 0.5;
			} else {
				left = -.5;
			}
			if (Math.abs(encoderRight.getRaw()) < rightLim){
				right = 0.5;
			} else {
				right = -.5;
			}
			
			done = false;
		} else {
			right = 0;
			left = 0;
			done = true;
		}
		SmartDashboard.putNumber("leftLim", leftLim);
		SmartDashboard.putNumber("rightLim", rightLim);
		drive.tankDrive(left, right);
		return done;
		
		
	}

}
