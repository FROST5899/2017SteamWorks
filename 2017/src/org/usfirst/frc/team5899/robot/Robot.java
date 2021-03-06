package org.usfirst.frc.team5899.robot;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import com.ctre.CANTalon;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;


//adam doesn't understand how github works
//and now he should
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
	//CANTalon Talon8 = new CANTalon(8);
	
	DigitalInput leftDriveEncoderA = new DigitalInput(0);
	DigitalInput leftDriveEncoderB = new DigitalInput(1);
	DigitalInput rightDriveEncoderA = new DigitalInput(2);
	DigitalInput rightDriveEncoderB = new DigitalInput(3);
	DigitalInput winchEncoderA = new DigitalInput(4);
	DigitalInput winchEncoderB = new DigitalInput(5);
	DigitalInput deliveryEncoderA = new DigitalInput(6);
	DigitalInput deliveryEncoderB = new DigitalInput(7);
	DigitalInput deliveryHighLimit = new DigitalInput(8);
	DigitalInput deliveryLowLimit = new DigitalInput(9);
	
	
	Encoder leftDriveEncoder = new Encoder(leftDriveEncoderA,leftDriveEncoderB);
	Encoder rightDriveEncoder = new Encoder(rightDriveEncoderA,rightDriveEncoderB);
	Encoder winchEncoder = new Encoder(winchEncoderA,winchEncoderB);
	Encoder deliveryEncoder = new Encoder(deliveryEncoderA,deliveryEncoderB);
	RobotDrive myRobot = new RobotDrive(Talon1, Talon6, Talon2, Talon3);
	
	
	/*myRobot.setInvertedMotor(MotorType.kRearLeft, false); 
	myRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
	myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
	myRobot.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
	myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);*/
	Joystick driveStickLeft = new Joystick(0);
	//Joystick driveStickRight = new Joystick(1);
	Joystick coPilotStick = new Joystick(1);
	Timer timer = new Timer();
	DeliverGear gearDelivery = new DeliverGear(coPilotStick, Talon7, myRobot, deliveryEncoder, deliveryLowLimit, deliveryHighLimit);
	EncoderTest testEncoders = new EncoderTest(leftDriveEncoder, rightDriveEncoder, winchEncoder, deliveryEncoder);
	private final Object imgLock = new Object();
	Vision vision = new Vision(myRobot,leftDriveEncoder, rightDriveEncoder, gearDelivery);//,imgLock);
	NetworkTable readingTable;
	
	//vision stuff:
	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 240;
	private VisionThread visionThread;
	private double rectX1 = 0.0;
	private double rectX2 = 0.0;
	private double rectX3 = 0.0;
	private double centerX = 0.0;
	private double centerX2 = 0.0;	
	double rectDistance = 0.0;
	double trueAcceleration = 0.0;
	double left, right;
	double firstTimeScan;
	double initTime;
	double rectY1;
	boolean autoVal;
	boolean driving;
	EncoderDrive autoDrive = new EncoderDrive(myRobot, leftDriveEncoder, rightDriveEncoder);
	
	
	
	
	

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		readingTable = NetworkTable.getTable("dataTable");
		readingTable.putNumber("test1", -1);
		//for auto//
		SmartDashboard.putBoolean("Turn Left?", false);
		
		/*
	    UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
	    camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
	    
	    visionThread = new VisionThread(camera, new PracticePipeline(), pipeline -> {
	        SmartDashboard.putBoolean("findIsEmpty", pipeline.findContoursOutput().isEmpty());
	        SmartDashboard.putBoolean("filterIsEmpty", pipeline.filterContoursOutput().isEmpty());
	        SmartDashboard.putNumber("element1", Imgproc.boundingRect(pipeline.filterContoursOutput().get(0)).x);
	    	if (!pipeline.filterContoursOutput().isEmpty()) {
	            Rect r1 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0)); 
	        
	          //  if (pipeline.filterContoursOutput().size() > 1){
	            	Rect r2 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(1));
	            	Rect r3 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(2));
	            	if (pipeline.filterContoursOutput().size() > 2) && ( ) {
	            		
	            	} else {
		            	synchronized (imgLock) {
		    	        	rectX1 = r1.x; // + (r1.width / 2);
		    	        	rectX2 = r2.x; //+ (r2.width / 2);
		    	        	rectX3 = r3.x;
		    	        	centerX1 = r1.x + (r1.width / 2);
		    	        	centerX2 = r2.x + (r2.width / 2);
		    	        	vision.setRect(r1.x, r2.x, r3.x);
		    	        	
		            	}       
	            	
	    	   
	            } else {
	            	synchronized (imgLock) {
	    	        	rectX1 = r1.x; // + (r1.width / 2);
	    	        	rectX2 = rectX1;
	    	        	centerX1 = r1.x + (r1.width / 2);
	    	        	centerX2 = centerX1;
	    	        	vision.setRect(r1.x, r1.x, rectX3);
	            	}
	            //}
	        } else {
	        	rectX1 = -1;
	        	rectX2 = -1;
	        	rectX3 = -1;
	        	vision.setRect(-1, -1, -1);
	        } 
	    });
	    visionThread.start();*/
		
		    UsbCamera frontCamera = CameraServer.getInstance().startAutomaticCapture(0);
		    UsbCamera backCamera = CameraServer.getInstance().startAutomaticCapture(1);
		    frontCamera.setResolution(IMG_WIDTH, IMG_HEIGHT);
		    backCamera.setResolution(IMG_WIDTH, IMG_HEIGHT);
		    
		    visionThread = new VisionThread(frontCamera, new PracticePipeline(), pipeline -> {
		        if (!pipeline.filterContoursOutput().isEmpty()) {
		            Rect r1 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
			        //Rect r2 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(1));
			         //Rect r3 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(2));
		            synchronized (imgLock) {
		                //vision.setRect(r1.x, r2.x, r3.x);
		            	rectX1 = r1.x;
		            	rectY1 = r1.y;
		            }
		            if (pipeline.filterContoursOutput().size() >= 2) {
		            	Rect r2 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(1));
		            	synchronized (imgLock) {
		            		rectX2 = r2.x;
		            	}
		            } else {
		            	synchronized (imgLock) {
		            		rectX2 = -1;
		            	}
		            }
		        } else {
			        synchronized (imgLock) {
			        	rectX1 = -1;
			        	rectX2 = -1;
			        	rectY1 = -1;
		        }
		        }
		    });
		    visionThread.start();
	    
		//NetworkTable.setServerMode();
		//NetworkTable.setIPAddress("10.58.99.59");
		//NetworkTable.setClientMode();
		/*NetworkTable.setTeam(5899);
		NetworkTable.setUpdateRate(0.01);
		NetworkTable.initialize();
		*/
		//CameraServer.getInstance().startAutomaticCapture();
		/*CameraServer.getInstance().addAxisCamera("raspi","10.58.99.59");
		CameraServer.getInstance().startAutomaticCapture("raspi");*/
		/*
		visionThread = new Thread(() -> {
			// Get the UsbCamera from CameraServer
			AxisCamera camera = CameraServer.getInstance().addAxisCamera("10.58.99.59");
			// Set the resolution
			camera.setResolution(640, 480);

			// Get a CvSink. This will capture Mats from the camera
			CvSink cvSink = CameraServer.getInstance().getVideo();
			// Setup a CvSource. This will send images back to the Dashboard
			CvSource outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);

			// Mats are very memory expensive. Lets reuse this Mat.
			Mat mat = new Mat();

			// This cannot be 'true'. The program will never exit if it is. This
			// lets the robot stop this thread when restarting robot code or
			// deploying.
			while (!Thread.interrupted()) {
				// Tell the CvSink to grab a frame from the camera and put it
				// in the source mat.  If there is an error notify the output.
				if (cvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					outputStream.notifyError(cvSink.getError());
					// skip the rest of the current iteration
					continue;
				}
				// Put a rectangle on the image
				Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
						new Scalar(255, 255, 255), 5);
				// Give the output stream a new image to display
				outputStream.putFrame(mat);
			}
		});
		visionThread.setDaemon(true);
		visionThread.start();
		*/
		
		
	}

	/**
	 * This function is run once each time the robot enters autonomous mode
	 */
	@Override
	public void autonomousInit() {
		timer.reset();
		timer.start();
		//vision.state = "scanning";
		gearDelivery.state = 1;
		firstTimeScan = Timer.getFPGATimestamp();
		vision.resetState();
		leftDriveEncoder.reset();
		rightDriveEncoder.reset();
		autoVal = SmartDashboard.getBoolean("Turn Left?", false);
		driving = true;
		vision.flickers = 0;
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		
		SmartDashboard.putBoolean("driving", driving);
		//driving = false;
		
		if (driving){
			
			//------Encoder Drive ------//
			
			driving = !autoDrive.driveStraight(60);
			testEncoders.displayEncoderValues();
			
			//------Time Drive ------//
			//driving = (Timer.getFPGATimestamp() - firstTimeScan < 1000);
			//myRobot.arcadeDrive(.4,0);
			
			SmartDashboard.putBoolean("driving", driving);
		} else {
		if (autoVal){
			synchronized (imgLock) {
				vision.setRect(rectX1, rectX2, rectY1);
				vision.visionCases("left");
			}
		}
		else{
			synchronized (imgLock) {
				vision.setRect(rectX1, rectX2, rectY1);
				vision.visionCases("right");
			}
		}
		}
		//testEncoders.displayEncoderValues();
		// Drive for 2 seconds
		
		//------ Non Vision --------//
		/*if ((timer.get() < 3)) {
			myRobot.drive(.4, 0.0); // drive forwards half speed
			//Talon5.set(0.4);
		} else {
			myRobot.drive(0.0, 0.0);
			gearDelivery.GearCases();
		}*/
		
		
		
		
		
		
		//-------- Vision Class -------//
		
		
		
		
		/*if ((timer.get() < 1)) {
			//myRobot.drive(.4, 0.0); // drive forwards half speed for 1 second
		}
		else {
			
		}*/
		
		
		
		//------- Other Worthless Crap ----------//
		// stop robot
			//gearDelivery.GearCases();
			//Talon5.set(0);
	//}	//Timer.getFPGATimestamp()
/*		double rectX1;
		double rectX2;
		double centerRect1;
		double centerRect2;*/ 
		/*synchronized (imgLock) {
			vision.setRect(rectX1, rectX2, rectX3);
		}*/
		
		//double turn = rectX1 - (IMG_WIDTH / 2);
		//double rectDistance =  Math.abs(centerRect1 - centerRect2);
		/*
		double centerX;
		synchronized (imgLock) {
			centerX = rectX2 != -1 ? rectX2 : rectX1;
			SmartDashboard.putNumber("rectX1Val", rectX1);
			SmartDashboard.putNumber("rectX2Val", rectX2);
		}
		double turn = centerX - (IMG_WIDTH / 2);
			*/
	
	
		/*SmartDashboard.putNumber("rectX1Val", rectX1);
		SmartDashboard.putNumber("rectX2Val", rectX2);*/
		//SmartDashboard.putNumber("rectDistance", rectDistance);
		/*
		if(Timer.getFPGATimestamp() - Math.floor(Timer.getFPGATimestamp()) > 0.5){
			myRobot.arcadeDrive(-0.1, turn * 0.003);
			//firstTimeScan = Timer.getFPGATimestamp();
		} else{
			myRobot.arcadeDrive(0,0);
		}
		*/
		//vision.scanX();
		//vision.visionCases("left");
		//myRobot.tankDrive(0.4, -0.4);
		/*SmartDashboard.putBoolean("turning left", true);
		
		if (this.rectX1 != -1){
			SmartDashboard.putBoolean("reached here", true);
		}*/
		//vision.visionCases("left");
		
		
		
	}

	
	/**
	 * This function is called once each time the robot enters tele-operated
	 * mode
	 */
	@Override
	public void teleopInit() {
		left = 0;
		right = 0;
		gearDelivery.state = 0;
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		
		double winchAxis = coPilotStick.getRawAxis(1);
		boolean enableTurn = coPilotStick.getRawButton(6);
		double coPilotTurn = coPilotStick.getRawAxis(4);
		double deliveryAxis = -coPilotStick.getRawAxis(5);
		double leftDriveAxis = driveStickLeft.getRawAxis(1);
		//double rightDriveAxis = driveStickRight.getRawAxis(1);
		double leftZAxis = driveStickLeft.getRawAxis(2);
		boolean fineControlButton = false; //driveStickLeft.getRawButton(2);
		//boolean driveStraightButton = driveStickRight.getRawButton(2);
		boolean invertButtonLeft = driveStickLeft.getRawButton(1);
		boolean deliveryLowLim = deliveryHighLimit.get(); 
		boolean deliveryHighLim = deliveryLowLimit.get();
		/*boolean invertButtonRight = driveStickRight.getRawButton(1);
		if (invertButtonRight){ leftDriveAxis = -leftDriveAxis; }*/
		//------Arcade Drive---------//
		
		
		if (invertButtonLeft){
			leftDriveAxis = -leftDriveAxis;
			//leftZAxis = -leftZAxis;
		}
		
		if (fineControlButton) {
			leftDriveAxis = leftDriveAxis/2;
			leftZAxis = leftZAxis/2;
		}
		if (!(gearDelivery.drivingChassis) && !vision.driving){
			double turnPower = Math.abs(driveStickLeft.getRawAxis(3)-1)/2*leftZAxis;
		myRobot.arcadeDrive(-leftDriveAxis, enableTurn ? coPilotTurn : turnPower);
		}
		
		SmartDashboard.putBoolean("vision driving", vision.driving);
		
		//------Tank Drive---------//
		/*
		rightDriveAxis = -rightDriveAxis;
		leftDriveAxis = -leftDriveAxis;
		double x;
		
		if (invertButtonLeft){
			x = rightDriveAxis;
			rightDriveAxis = -leftDriveAxis; 
			leftDriveAxis = -x; 
		}
		if (fineControlButton){
			leftDriveAxis = leftDriveAxis * 0.5;
			rightDriveAxis = rightDriveAxis * 0.5;
		}
		double z = 0.02;
		if (!(gearDelivery.drivingChassis)){
			if(driveStraightButton){
				
				myRobot.tankDrive(rightDriveAxis, rightDriveAxis);
				
				
				//how to ternary statement
				//finalvalue = ifstatement ? truevalue : falsevalue;
				
				left = rightDriveAxis < left ? left - z : left + z;
				right = rightDriveAxis < right ? right - z : right + z;
				myRobot.tankDrive(left, right);
			}
			else{
				myRobot.tankDrive(leftDriveAxis, rightDriveAxis);
				
				
				left = leftDriveAxis < left ? left - z : left + z;
				right = rightDriveAxis < right ? right - z : right + z;
				myRobot.tankDrive(left, right);
			}
		}
		*/
		
		//------ CoPilot -------//
		Talon5.set(winchAxis);
		if(!gearDelivery.drivingArm){
			if(deliveryAxis < 0){
				Talon7.set(0.9*deliveryAxis);
			}
			if(deliveryAxis > 0){
				Talon7.set(0.9*deliveryAxis);
			}
			
			if(Math.abs(deliveryAxis) < .1){
				Talon7.set(0);
			}	
		}
		
		if (coPilotStick.getRawButton(2)) {
			//right
			synchronized (imgLock) {
				vision.setRect(rectX1, rectX2, rectY1);
				vision.visionCases("right");
			}
		} else if (coPilotStick.getRawButton(3)) {
			//left
			synchronized (imgLock) {
				vision.setRect(rectX1, rectX2, rectY1);
				vision.visionCases("left");
			}
		} else {
			//reset
			
			vision.resetState();
			vision.driving = false;
			if (!coPilotStick.getRawButton(1)) {
				gearDelivery.state = 1;
				gearDelivery.drivingChassis = false;
				gearDelivery.drivingArm = false;
			}
		}
		
		if(coPilotStick.getRawButton(1)){
			gearDelivery.GearCases();
		} else {
			//if (!coPilotStick.getRawButton(2) || !coPilotStick.getRawButton(3)) {
				gearDelivery.state = 1;
				gearDelivery.drivingArm = false;
				gearDelivery.drivingChassis = false;
			//}
		}
		
		testEncoders.displayEncoderValues();
		//NetworkTable.initialize();
		SmartDashboard.putBoolean("deliveryLowLimit", deliveryLowLim);
		SmartDashboard.putBoolean("deliveryHighLimit", deliveryHighLim);
		double y = readingTable.getNumber("test1", -3);
		readingTable.putNumber("wtest1", 0);
		SmartDashboard.putNumber("NTtest1", y);
		boolean NTConn = readingTable.isConnected();
		SmartDashboard.putBoolean("NTtest2", NTConn);
		
	}

	/**
	 * This function is called periodically during test mode
	 */
	//@Override
	public void testInit() {
		/*timer.reset();
		timer.start();
		initTime = Timer.getFPGATimestamp();*/
	}
	
	public void testPeriodic() {
	/*	double Time = Timer.getFPGATimestamp();
		if (Math.abs(Time - initTime) < 2000) {
			Talon7.set(.4);
			Talon5.set(.4);
			myRobot.arcadeDrive(.5,0);
		}
		else if (Math.abs(Time - initTime) < 5000) {
			Talon7.set(-.4);
			Talon5.set(-.4);
			myRobot.arcadeDrive(0,0.5);
		} else {
			Talon7.set(0);
			Talon5.set(0);
			myRobot.arcadeDrive(0,0);
		}*/
		testEncoders.displayEncoderValues();
	}

		
	}

