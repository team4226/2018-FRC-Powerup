/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
//Main program
package org.usfirst.frc.team4226.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private static final String kFwdNoScore = "Left and right forward no score";
	private static final String kPos1 = "Position 1 Autonomous";
	private static final String kPos2 = "Position 2 Autonomous";
	private static final String kPos3 = "Position 3 Autonomous";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	//ADXRS450_Gyro gyro;
	RobotDrive chassis;
	Spark spark7,spark8,spark9;
	Joystick main, secondary;
	DoubleSolenoid solenoid1, solenoid2;
	Relay compressor;
	AnalogGyro gyro;
	int strokect,count;
	boolean runAuto=true;
	double kp;
	String message;
	
	

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void robotInit() {
		//gyro=new ADXRS450_Gyro(edu.wpi.first.wpilibj.SPI.Port.kOnboardCS0);
		gyro=new AnalogGyro(0);
		chassis=new RobotDrive(0,1,2,3);
		spark7=new Spark(7);
		spark8=new Spark(8);
		spark9=new Spark(9);
		main=new Joystick(0);
		secondary=new Joystick(1);
		solenoid1=new DoubleSolenoid(0,1);
		//solenoid2=new DoubleSolenoid(2,3);
		kp=0.03;
		strokect=0;
		m_chooser.addDefault("Position 3 Autonomous",kPos3);
		m_chooser.addObject("left or right foward no score", kFwdNoScore);
		m_chooser.addObject("Position 1 Autonomous",kPos1);
		m_chooser.addObject("Position 2 Autonomous",kPos2);
		m_chooser.addObject("Position 3 Autonomous",kPos3);
		SmartDashboard.putData("Auto choices", m_chooser);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	@SuppressWarnings("depreciation")
	public void autonomousInit() {
		message = DriverStation.getInstance().getGameSpecificMessage();
		System.out.println(DriverStation.getInstance().getGameSpecificMessage());
			runAuto=true;
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void autonomousPeriodic() {
		gyro.reset();
		double angle = gyro.getAngle();
		chassis.setSafetyEnabled(false);
		if(m_chooser.getSelected()==kFwdNoScore&&runAuto) {
			chassis.drive(0.25, -angle*kp);
			Timer.delay(10/2.2);
			chassis.drive(0,0);
			runAuto=false;
		}
		if(m_chooser.getSelected()==kPos1&&runAuto&&message=="LLL"||message=="LLR"||message=="LRL"||message=="LRR") {
			chassis.drive(0.25, -angle*kp);
			Timer.delay(14.5/2.2);
			chassis.drive(0,0);
			Timer.delay(1);
			chassis.drive(0.45, 0.575); //right turn
			Timer.delay(1.25);
			chassis.drive(0,0);
			Timer.delay(1);
			chassis.drive(0.25, -0.0003125);
			Timer.delay(1/2.2);
			chassis.drive(0,0);
			runAuto=false;
		}
		if(m_chooser.getSelected()==kPos3&&runAuto) {
			chassis.drive(0.25, -angle*kp);
			Timer.delay(16/2.2);
			chassis.drive(0,0);
			Timer.delay(1);
			chassis.drive(0.45, -0.575); //left turn
			Timer.delay(1.11);
			chassis.drive(0,0);
			Timer.delay(1);
			chassis.drive(0.25, -0.00017578);
			Timer.delay(1/2.2);
			chassis.drive(0,0);
			runAuto=false;
		}
		
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		SmartDashboard.putString("Test", "Hello world");
		//drive chassis
		chassis.tankDrive(main.getRawAxis(1)*-1, main.getRawAxis(5)*-1,true);
		//shift up and down
		if(main.getRawButton(5)) {
			solenoid1.set(DoubleSolenoid.Value.kForward);
		}
		else if(main.getRawButton(6)) {
			solenoid1.set(DoubleSolenoid.Value.kReverse);
		}
		if(secondary.getRawButton(4)) {
			spark7.set(0.75);
		}
		else if(secondary.getRawButton(2)) {
			spark7.set(-0.75);
		}
		else {
			spark7.set(0);
		}
		spark8.set(secondary.getRawAxis(3));
		spark9.set(secondary.getRawAxis(1));
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		
	}
	
}
