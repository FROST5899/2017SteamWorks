package org.usfirst.frc.team5899.robot;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import com.ctre.CANTalon;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;



public class Vision {

	//vision stuff:
	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 240;
	private VisionThread visionThread;
	private double Rect2 = 0.0;
	private double Rect1 = 0.0;
	private double Rect3 = 0.0;
	private double centerRect1;
	private double centerRect2;	
	double rectDistance = 0.0;
	int flickers = 0;
	String state = "scanning";
	String DIR = "";
	RobotDrive drive;
	Encoder encoderLeft;
	Encoder encoderRight;
	double[] oldXValues;
	double[] newXValues;
	double Offset;
	double firstTime = 0;
	String newDirection = null;
	double scanMultiplier = 0.5;
	double Rect1y;
	DeliverGear gearDeliv;
	
	boolean driving = false;
	
	
	public Vision(RobotDrive drive, Encoder encoderLeft, Encoder encoderRight, DeliverGear gearDeliv ){
		this.drive = drive;
		this.encoderLeft = encoderLeft;
		this.encoderRight = encoderRight;
		this.gearDeliv = gearDeliv;
		
		
	}
	public void setRect(double R1x, double R2x, double R1y) { //double R3) {
		this.Rect1 = R1x;
		this.Rect2 = R2x;
		this.Rect1y = R1y;
		//this.Rect3 = R3;
	}
	public void resetState() {
		state = "scanning";
		newDirection = null;
		
	}
	
	
	public  void scanX(double multiplyer){
		if(Timer.getFPGATimestamp() - Math.floor(Timer.getFPGATimestamp()) > 0.7){
			drive.arcadeDrive(0, multiplyer);
			//firstTimeScan = Timer.getFPGATimestamp();
		} else{
			drive.arcadeDrive(0,0);
		}
	}
	
	public void centerX(double forwardSpeed, double turn) {
		// ---- important value -----//
		
		
		double driveAddVal = .36;//.35
		double minDriveValue = .45;//47 on other chassis
		
		
		//---------------------------///
		double negatedVal = (turn < 0) ? -driveAddVal : driveAddVal;
		double speed =  negatedVal + turn * .0000000125;
		if (Math.abs(speed) < minDriveValue){
			if (speed < 0){
				speed = -minDriveValue;
			}
			else{
				speed = minDriveValue;
			}
		}
		//double actualTurn = speed;0
		SmartDashboard.putNumber("turnSpeed", speed);
		drive.arcadeDrive(forwardSpeed, speed > .37 ? .37 : speed);
	}
	
	
	public void visionCases(String direction) {
		SmartDashboard.putString("state", state);		

		if (Rect1 != -1) {
			if (Rect2 != -1) { 
				if (Rect1 < Rect2) {
					Offset = Rect1; 
				}
				else { 
					Offset = Rect2; 
				}
			} else {
			Offset = Rect1;
			}
		}
		
		double OffCube = (Offset - (320/2) - 53)*(Offset - (320/2) - 53)*(Offset - (320/2) - 53);
		
		if (state == "scanning") {
			this.flickers = 0;
			driving = true;
			if (Rect1 == -1) {
				SmartDashboard.putBoolean("lined up", false);
				direction = newDirection != null ? newDirection : direction;
				scanX(direction == "left" ? -scanMultiplier : scanMultiplier);
			} else {
				drive.arcadeDrive(0, 0);
				state = "centering";
				//double firstTime = Timer.getFPGATimestamp();
			}
		} else if (state == "centering") {
			driving = true;
			if (Offset > 0) {
				newDirection = "right";
			} else {
				newDirection = "left";
			}
//			if (Timer.getFPGATimestamp() - firstTime < 2){
				if (Math.abs(Offset - (320/2) - 53) > 50) {
					SmartDashboard.putNumber("CenterXval", Offset - (320/2) - 53);
					SmartDashboard.putBoolean("lined up", false);
					centerX(0, OffCube);
					if (Rect1 == -1){
						state = "scanning";		
					}
				} else {
					drive.arcadeDrive(0, 0);
					state = "driving";
				}
/*			} else {
				state = "scanning";
			}*/
		} else if (state == "driving") {
			driving = true;
			//drive.arcadeDrive(.5, 0);
			//*.25); 
			SmartDashboard.putNumber("flickering vqalue", flickers);
			if(Timer.getFPGATimestamp() - Math.floor(Timer.getFPGATimestamp()) > 0.5){	
				drive.arcadeDrive(.5, 0);
				if( Rect1y < 5 && Rect1 > 200){
					state = "delivering";
					gearDeliv.state = 1;
					firstTime = Timer.getFPGATimestamp();
				}
			} else {
				if (flickers < 50000000) {
					this.flickers++;
					state = "centering";
				} else {
					state = "delivering";
				}
			}
		}
		if (state == "delivering"){
			driving = true;
			if (Timer.getFPGATimestamp() - firstTime < 1 ){
				drive.arcadeDrive(0.5,0);
			}
			else{
				driving = true;
				gearDeliv.GearCases();
			}
			
		}
		
		//my stuff
		
		
		SmartDashboard.putNumber("RectOffset", 320/2 - Offset - 53);
		SmartDashboard.putNumber("Rect1", Rect1);
		SmartDashboard.putNumber("Rect2", Rect2);
		SmartDashboard.putNumber("RectY", Rect1y);
		
			//centerX(Offset+106-320/2);
			//double val = (1/((Offset - 320/2)*(Offset - 320/2)*(Offset - 320/2));
		
		
		
		/*
		this.DIR = direction;
		SmartDashboard.putString("visionState", state);
		SmartDashboard.putString("scanningDir", DIR);
		if (state == "scanning"){
			//firstTime = Timer.getFPGATimestamp();
			if (DIR == "left") {
				drive.tankDrive(0.3, -0.3);
				SmartDashboard.putBoolean("turning left", true);
				SmartDashboard.putBoolean("reached here", false);
				if (this.Rect1 != -1){
					state = "scanningForSecondContour";
				}
			}
			if (DIR == "right") {
				drive.tankDrive(-0.3, 0.3);
				SmartDashboard.putBoolean("turning right", true);
				SmartDashboard.putBoolean("reached here", false);
				if (newXValues != oldXValues){
					state = "scanningForSecondContour";
				}
			}

				SmartDashboard.putNumber("scanningValue1", Rect1);
				SmartDashboard.putNumber("scanningValue2", Rect2);
		
		}
		
		if(state == "scanningForSecondContour"){
			SmartDashboard.putBoolean("reached here", true);
		}*/
	}
	
}
