package org.usfirst.frc.team5899.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import static org.usfirst.frc.team5899.robot.DeliveryStateEnum;;
/*
state = 0   =>  ENCODERRESET1
state = 1   =>  RAISEDEPLOYER
state = 2   =>  DRIVEFWD
state = 3   =>  LOWERDEPLOYER
state = 4   =>  ENCODERRESET2


ENCODERRESET1, RAISEDEPLOYER, DRIVEFWD, LOWERDEPLOYER, ENCODERRESET2;

*/
public class DeliverGear{
	//DeliveryStateEnum state;
	CANTalon armTalon;
	Joystick coPilot;
	Encoder armEncoder;
	DigitalInput armLowLimit;
	DigitalInput armHighLimit;
	Timer driveTime;
	RobotDrive chassisDrive;
	
	boolean drivingArm = false;
	boolean drivingChassis = false;
	double state = 0;
	boolean pressed = false;
	double armDegrees = 0;
	double firstTime = 0;
	
	double ENCODERMULTIPLIER = 1;
	double ARMMAXANGLE = 95;
	//double winchAxis = winchStick.getRawAxis(1);
public DeliverGear(Joystick stick, CANTalon talon, RobotDrive drive, Encoder encoder, DigitalInput lowLimit, DigitalInput highLimit) {
	this.armTalon = talon;
	this.coPilot = stick; 
	this.armEncoder = encoder;
	this.armLowLimit = lowLimit;
	this.armHighLimit = highLimit;
	this.chassisDrive = drive;
	}	
	


public void GearCases(){
	/*if (coPilot.getRawButton(1) != pressed && coPilot.getRawButton(1)){
		if (state >= 4){
			state = 0; 
		}
		else{
			state += 1;
		}
	}*/
	//SmartDashboard.putDouble("Delivery State Double", state);
	pressed = coPilot.getRawButton(1);
	//SmartDashboard.putNumber("FPGATime", Timer.getFPGATimestamp());
	//SmartDashboard.putNumber("matchTime", Timer.getMatchTime());
	armDegrees = (armEncoder.get() * ENCODERMULTIPLIER);
		if (state == 1){
			SmartDashboard.putString("Delivery State", "EncoderReset1");
			if (!armLowLimit.get()){
				armTalon.set(-0.6); 
				drivingArm = true;
			}
			else{
				armEncoder.reset();
				armTalon.set(0);
				drivingArm = true;
				state = 2;
			}
		}	
		if (state == 2){
			SmartDashboard.putString("Delivery State", "RaiseDeployer");
			if (!armHighLimit.get()){
				armTalon.set(-.9);
				drivingArm = true;
			}
			else{
				
				firstTime = Timer.getFPGATimestamp();
				drivingArm = false;
				state = 3;
			}
		}
		if (state == 3){
			SmartDashboard.putString("Delivery State", "Delay");
			if (Timer.getFPGATimestamp() - firstTime < 2 && armHighLimit.get()){ 
				chassisDrive.tankDrive(0, 0);
				armTalon.set(-0.4);
			}
			if(Timer.getFPGATimestamp() - firstTime < 2 && !armHighLimit.get()){
				armTalon.set(0);
			}
			else{
				drivingChassis = false;
				state = 4;
				firstTime = Timer.getFPGATimestamp();
			}
		}
		
		if (state == 4){
			SmartDashboard.putString("Delivery State", "DriveBackward");
			SmartDashboard.putNumber("DriveBackTime",  (Timer.getFPGATimestamp() - firstTime));
			if (Timer.getFPGATimestamp() - firstTime < 0.5){
				drivingChassis = true; 
				chassisDrive.tankDrive(-0.7, -.7);
				armTalon.set(-0.3);
				
				
			}
			else{
				drivingChassis = false;
				state = 5;
				
			}
			
					
		}
		if (state == 5){
			
			SmartDashboard.putString("Delivery State", "LowerDeployer");
			if (!armLowLimit.get()){
				armTalon.set(-0.08); 
				drivingArm = true;
				
			}
			else{
				drivingArm = false;
				armEncoder.reset();
				state = 0;
				armTalon.set(0); 
				
			}
			
		}
		if (state == 0){
			
			SmartDashboard.putString("Delivery State", "Idle");
			
			
		}
	
	}
}
