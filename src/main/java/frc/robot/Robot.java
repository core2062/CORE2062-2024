// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Optional;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DataLogEntry;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.constants.CTREConfigs;
import frc.robot.constants.Constants;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private static final String kDefaultAuto = "Do Nothing";
  private static final String kMidAuto = "Mid Auto";
  private static final String kBlueLeftPickAuto = "Blue Left Pickup Auto";
  private static final String kBlueLeftMoveAuto = "Blue Left Move Auto";
  private static final String kBlueRightAuto = "Blue Right Auto";
  private static final String kRedRightPickAuto = "Red Right Pickup Auto";
  private static final String kRedRightMoveAuto = "Red Right Move Auto";
  private static final String kRedLeftAuto = "Red Left Auto";

  private String m_autoSelected;
  private String m_speedSelected;

  private static final String compSpeed = "Comp Motor Speed";
  private static final String demoSpeed = "Demo Motor Speed";

  private final SendableChooser<String> m_autoChooser = new SendableChooser<>();
  private final SendableChooser<String> m_driveSpeedchooser = new SendableChooser<>();

  // Compressor phCompressor = new Compressor(3, PneumaticsModuleType.REVPH); //TODO: tune to robot
  public static CTREConfigs ctreConfigs;

  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    Optional<Alliance> ally = DriverStation.getAlliance();
    if (ally.isPresent()) {
      if (ally.get() == Alliance.Red) {
        Constants.VisionConstants.SpeakerID = 4;
        Constants.VisionConstants.farSpeakerID = 1;
        Constants.VisionConstants.AmpID = 5;
        Constants.side = "red";
      }
      if (ally.get() == Alliance.Blue) {
        Constants.VisionConstants.SpeakerID = 7;
        Constants.VisionConstants.farSpeakerID = 2;
        Constants.VisionConstants.AmpID = 6;
        Constants.side = "blue";
      }
      System.out.println("Selected Side is: " + Constants.side);
    }
    else {

    }
    
    //setup for auton selection on driver station
    m_autoChooser.setDefaultOption("Do Nothing", kDefaultAuto);
    if (ally.get() == Alliance.Red){
      m_autoChooser.addOption("Mid Auto", kMidAuto);
      m_autoChooser.addOption("Red Left Auto", kRedLeftAuto);
      m_autoChooser.addOption("Red Right Move Auto", kRedRightMoveAuto);
      m_autoChooser.addOption("Red Right Pickup Auto", kRedRightPickAuto);
    } else if (ally.get() == Alliance.Blue){
      m_autoChooser.addOption("Mid Auto", kMidAuto);
      m_autoChooser.addOption("Blue Right Auto", kBlueRightAuto);
      m_autoChooser.addOption("Blue Left Move Auto", kBlueLeftMoveAuto);
      m_autoChooser.addOption("Blue Left Pickup Auto", kBlueLeftPickAuto);
    }
    SmartDashboard.putData("Auto choices", m_autoChooser);

    //setup for drive speed on driver station
    m_driveSpeedchooser.setDefaultOption("Set Comp Speed", compSpeed);
    m_driveSpeedchooser.addOption("Set Demo Speed", demoSpeed); //TODO: change default based upon speed mod you want
    SmartDashboard.putData("Speed chooser", m_driveSpeedchooser);
  
    ctreConfigs = new CTREConfigs();
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();

    StringLogEntry stringLog;
    DataLogManager.start();
    DriverStation.startDataLog(DataLogManager.getLog());
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    System.out.println("Autos intilized");

    m_autoSelected = m_autoChooser.getSelected();
    switch (m_autoSelected) {
      case kDefaultAuto:
        Constants.AutoSelected = 100;
        System.out.println("Autos Default");
        break;
      case kMidAuto:
        Constants.AutoSelected = 0;
        System.out.println("Movement");
        break;
      case kBlueRightAuto:
        Constants.AutoSelected = 1;
        System.out.println("Movement");
        break;
      case kBlueLeftMoveAuto:
        Constants.AutoSelected = 3;
        System.out.println("Movement");
        break;
      case kBlueLeftPickAuto:
        Constants.AutoSelected = 2;
        System.out.println("Movement");
        break;
      case kRedLeftAuto:
        Constants.AutoSelected = 1;
        System.out.println("Movement");
        break;
      case kRedRightMoveAuto:
        Constants.AutoSelected = 3;
        System.out.println("Movement");
        break;
      case kRedRightPickAuto:
        Constants.AutoSelected = 2;
        System.out.println("Movement");
        break;
      default:
        System.out.println("default auto Movement");
        Constants.AutoSelected = 10;
        break;
    }
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    m_speedSelected = m_driveSpeedchooser.getSelected();
    switch (m_speedSelected) {
      case compSpeed:
        Constants.Swerve.SpeedMod.set(0.8); //TODO: tune to robot
        System.out.println("compspeed");
        break;
      case demoSpeed:
        Constants.Swerve.SpeedMod.set(0.3);
        System.out.println("demospeed");
        break;
      default:
      System.out.println("default");
        break;
    }
    Optional<Alliance> ally = DriverStation.getAlliance();
    if (ally.isPresent()) {
      if (ally.get() == Alliance.Red) {
        Constants.VisionConstants.SpeakerID = 4;
        Constants.VisionConstants.farSpeakerID = 1;
        Constants.VisionConstants.AmpID = 5;
      }
      if (ally.get() == Alliance.Blue) {
        Constants.VisionConstants.SpeakerID = 7;
        Constants.VisionConstants.farSpeakerID = 2;
        Constants.VisionConstants.AmpID = 6;
      }
    }
    else {

    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
    Optional<Alliance> ally = DriverStation.getAlliance();
    if (ally.isPresent()) {
      if (ally.get() == Alliance.Red) {
        Constants.VisionConstants.SpeakerID = 4;
        Constants.VisionConstants.farSpeakerID = 1;
        Constants.VisionConstants.AmpID = 5;
      }
      if (ally.get() == Alliance.Blue) {
        Constants.VisionConstants.SpeakerID = 7;
        Constants.VisionConstants.farSpeakerID = 2;
        Constants.VisionConstants.AmpID = 6;
      }
    }
    else {

    }
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
