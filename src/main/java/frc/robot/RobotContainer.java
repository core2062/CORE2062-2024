// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.function.DoubleSupplier;

import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.*;
import frc.lib.util.COREConstants.constantType;
import frc.robot.commands.*;
import frc.robot.constants.Constants;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  /* Controllers */
  private final Joystick driver = new Joystick(0);
  private final Joystick operator = new Joystick(1);

  /* Drive Controls */
  private final int translationAxis = XboxController.Axis.kLeftY.value;
  private final int strafeAxis = XboxController.Axis.kLeftX.value;
  private final int rotationAxis = XboxController.Axis.kRightX.value;

  /* Drive Buttons */
  private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kY.value);
  private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);
  
  private final JoystickButton SpeakerTrack = new JoystickButton(driver, XboxController.Button.kB.value);
  private final JoystickButton AmpTrack = new JoystickButton(driver, XboxController.Button.kX.value);
  /* Operator Buttons */
  private final JoystickButton ScoreAssembly1 = new JoystickButton(operator, XboxController.Button.kLeftBumper.value);
  private final JoystickButton stopScoreAssenly = new JoystickButton(operator, XboxController.Button.kB.value);
  private final JoystickButton SpeakerScoreAssembly2 = new JoystickButton(operator, XboxController.Button.kRightBumper.value);
  private final JoystickButton AmpScoreAssembly2 = new JoystickButton(operator, XboxController.Button.kA.value);

  private final JoystickButton increaseLauncherHeading = new JoystickButton(operator, XboxController.Button.kY.value);
  private final JoystickButton decreaseLauncherHeading = new JoystickButton(operator, XboxController.Button.kX.value);

  private final JoystickButton goTo51Deg = new JoystickButton(operator, XboxController.Button.kStart.value);
  private final int leftTrigger = XboxController.Axis.kLeftTrigger.value;
  
  /* Subsystems */
  private final Swerve s_Swerve = new Swerve();
  private final IntakeSubsystem i_Intake = new IntakeSubsystem();
  private final LauncherSubsystem l_Launcher = new LauncherSubsystem();
  private final ScoreAssembly c_ScoreAssembly = new ScoreAssembly(l_Launcher, i_Intake);
  private final VisionSubsystem v_VisionSubsystem = new VisionSubsystem();

  public static DoubleSupplier speakerLauncherSpeed = () -> Constants.LauncherConstants.kSpeakerLaunchSpeed.get(0.0);
  public static DoubleSupplier intakeSpeed = () -> Constants.IntakeConstants.kIntakeSpeed.get(0.0);
  public static DoubleSupplier leftRotationSpeed = () -> Constants.LauncherConstants.kLeftRotationSpeed.get(0.0);
  public static DoubleSupplier rightRotationSpeed = () -> Constants.LauncherConstants.kRightRotationSpeed.get(0.0);
  public static DoubleSupplier feedSpeed = () -> Constants.LauncherConstants.kFeedSpeed.get(0.0);
  public static DoubleSupplier ampLauncherSpeed = () -> Constants.LauncherConstants.kAMPLaunchSpeed.get(0.0);
  public static DoubleSupplier launchDelay = () -> Constants.LauncherConstants.kDelay.get(0.0);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    s_Swerve.setDefaultCommand(
            new TeleopSwerve(
                s_Swerve, 
                true,
                () -> -driver.getRawAxis(translationAxis), 
                () -> -driver.getRawAxis(strafeAxis), 
                () -> -driver.getRawAxis(rotationAxis), 
                () -> robotCentric.getAsBoolean()
            )
        );
    s_Swerve.gyro.setYaw(0);
    // Configure the button bindings
    configureButtonBindings();
    l_Launcher.configMotors();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureButtonBindings() {
    /* Driver Buttons */
      zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroGyro()));
      SpeakerTrack.whileTrue(v_VisionSubsystem.AimAtSpeaker(l_Launcher, s_Swerve, 
                                                              () -> -driver.getRawAxis(translationAxis),
                                                              () -> -driver.getRawAxis(strafeAxis),
                                                              () -> robotCentric.getAsBoolean()
                                                              ));
    //   AmpTrack.whileTrue(v_VisionSubsystem.AimAtSpeaker(l_Launcher, s_Swerve, Constants.VisionConstants.SpeakerID,0,
    //                                                           () -> -driver.getRawAxis(translationAxis),
    //                                                           () -> -driver.getRawAxis(strafeAxis),
    //                                                           () -> robotCentric.getAsBoolean()
    //                                                           ));

    // /* Operator Buttons */
      // launcherMotors.onTrue(new InstantCommand(() -> l_Launcher.setLauncherSpeed(Constants.LauncherConstants.kLaunchSpeed.get(0.0))))
      //               .onFalse(new InstantCommand(() -> l_Launcher.setLauncherSpeed(0.0)));
      // launcherFeed.onTrue(new InstantCommand(() -> i_Intake.setFeedSpeed(Constants.LauncherConstants.kFeedSpeed.get(0.0))))
      //               .onFalse(new InstantCommand(() -> i_Intake.setFeedSpeed(0.0)));
      // intakeFeed.onTrue(new InstantCommand(() -> i_Intake.setIntakeSpeed(Constants.IntakeConstants.kIntakeSpeed.get(0.0))))
      //           .onFalse(new InstantCommand(() -> i_Intake.setIntakeSpeed(0.0)));

      increaseLauncherHeading.onTrue(new InstantCommand(() -> l_Launcher.LauncherRotationPercent(-leftRotationSpeed.getAsDouble(), -rightRotationSpeed.getAsDouble())))
                             .onTrue(new InstantCommand(() -> Constants.kOverride = true))
                             .onFalse(new InstantCommand(() -> l_Launcher.LauncherRotationPercent(0, 0)))
                             .onFalse(new InstantCommand(() -> Constants.kOverride = false));
      decreaseLauncherHeading.onTrue(new InstantCommand(() -> l_Launcher.LauncherRotationPercent(leftRotationSpeed.getAsDouble(), rightRotationSpeed.getAsDouble())))
                             .onTrue(new InstantCommand(() -> Constants.kOverride = true))
                             .onFalse(new InstantCommand(() -> l_Launcher.LauncherRotationPercent(0, 0)))
                             .onFalse(new InstantCommand(() -> Constants.kOverride = false));
      goTo51Deg.onTrue(new InstantCommand(() -> Constants.kTarget = true))
                   .onFalse(new InstantCommand(() -> Constants.kTarget = false));

      ScoreAssembly1.onTrue(c_ScoreAssembly.pickUpPiece(l_Launcher, i_Intake, intakeSpeed, feedSpeed));
      stopScoreAssenly.onTrue(c_ScoreAssembly.defaultCommand(l_Launcher, i_Intake));
      SpeakerScoreAssembly2.whileTrue(c_ScoreAssembly.launchPiece(l_Launcher, i_Intake, speakerLauncherSpeed, feedSpeed, launchDelay))
                    .onTrue(new InstantCommand(() -> Constants.assemblyDone = false))
                    .onFalse(new InstantCommand(() -> Constants.assemblyDone = true));
      AmpScoreAssembly2.whileTrue(c_ScoreAssembly.launchPiece(l_Launcher, i_Intake, ampLauncherSpeed, feedSpeed, launchDelay))
                    .onTrue(new InstantCommand(() -> Constants.assemblyDone = false))
                    .onFalse(new InstantCommand(() -> Constants.assemblyDone = true));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    //return new Autos(place auto stuff here)
    return new Autos(Constants.AutoSelected, s_Swerve);
  }
}
