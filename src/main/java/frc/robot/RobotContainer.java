// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.*;
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
//sets up controller joysticks
  /* Drive Buttons */
  private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kY.value);
  private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);
  
  private final JoystickButton SpeakerTrack = new JoystickButton(driver, XboxController.Button.kB.value);
  private final JoystickButton NoteTrack = new JoystickButton(driver, XboxController.Button.kX.value);

  private final POVButton ClimberUp = new POVButton(driver, 0);
  private final POVButton ClimberDown = new POVButton(driver, 180);
  /* Operator Buttons */
  private final JoystickButton IntakeAssembly = new JoystickButton(operator, 5);
  private final JoystickButton reverseIntakeFeed = new JoystickButton(operator, 7);

  private final JoystickButton backwardsLauncherAngle = new JoystickButton(operator, 9);

  private final JoystickButton stopAssemly = new JoystickButton(operator, 3);
  private final JoystickButton SpeakerLaunch = new JoystickButton(operator, 6);
  private final JoystickButton AmpLaunch = new JoystickButton(operator, 8);

  private final JoystickButton increaseLauncherHeading = new JoystickButton(operator, 4);
  private final JoystickButton decreaseLauncherHeading = new JoystickButton(operator, 1);

  private final JoystickButton LauncherFeed = new JoystickButton(operator, 2);

  private final POVButton CloseSpeakerAngle = new POVButton(operator, 90);
  private final POVButton ZeroAngle = new POVButton(operator, 180);
  private final POVButton AmpAngle = new POVButton(operator, 0);
  private final POVButton SafeZoneAngle = new POVButton(operator, 270);
  
  /* Subsystems */
  private final Swerve s_Swerve = new Swerve();
  private final IntakeSubsystem i_Intake = new IntakeSubsystem();
  private final LauncherSubsystem l_Launcher = new LauncherSubsystem();
  private final ScoreAssembly c_ScoreAssembly = new ScoreAssembly();
  private final LauncherTrackingSubsystem lt_LaunchTrackSubsystem = new LauncherTrackingSubsystem();
  private final SwerveTrackingSubsystem st_SwerveTrackSubsystem = new SwerveTrackingSubsystem();
  private final ClimberSubsystem c_ClimberSubsystem = new ClimberSubsystem();

  /* double Suppliers */
  public static DoubleSupplier intakeSpeed = () -> Constants.IntakeConstants.kIntakeSpeed.get(0.0);
  public static DoubleSupplier leftRotationSpeed = () -> Constants.LauncherConstants.kLeftRotationSpeed.get(0.0);
  public static DoubleSupplier rightRotationSpeed = () -> Constants.LauncherConstants.kRightRotationSpeed.get(0.0);
  public static DoubleSupplier feedSpeed = () -> Constants.LauncherConstants.kFeedSpeed.get(0.0);

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
      //is a command that makes the robot move.

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

      ClimberUp.onTrue(new InstantCommand(() -> c_ClimberSubsystem.setClimberSpeed(Constants.ClimberConstants.kUpClimberSpeed.get(0.0))))
                .onFalse(new InstantCommand(() -> c_ClimberSubsystem.setClimberSpeed(0.0)));
      ClimberDown.onTrue(new InstantCommand(() -> c_ClimberSubsystem.setClimberSpeed(Constants.ClimberConstants.kDownClimberSpeed.get(0.0))))
                .onFalse(new InstantCommand(() -> c_ClimberSubsystem.setClimberSpeed(0.0)));

      SpeakerTrack.whileTrue(st_SwerveTrackSubsystem.AimAtSpeaker(s_Swerve, 
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
      reverseIntakeFeed.onTrue(new InstantCommand(() -> i_Intake.setFeedAndIntakeSpeed(-Constants.IntakeConstants.kIntakeSpeed.get(0.0), -Constants.LauncherConstants.kFeedSpeed.get(0.0))))
                .onFalse(new InstantCommand(() -> i_Intake.setFeedAndIntakeSpeed(0.0, 0.0)));

      increaseLauncherHeading.onTrue(new InstantCommand(() -> l_Launcher.LauncherRotationPercent(-leftRotationSpeed.getAsDouble(), -rightRotationSpeed.getAsDouble())))
                             .onFalse(new InstantCommand(() -> l_Launcher.LauncherRotationPercent(0, 0)));
      decreaseLauncherHeading.onTrue(new InstantCommand(() -> l_Launcher.LauncherRotationPercent(leftRotationSpeed.getAsDouble(), rightRotationSpeed.getAsDouble())))
                             .onFalse(new InstantCommand(() -> l_Launcher.LauncherRotationPercent(0, 0)));
      IntakeAssembly.onTrue(c_ScoreAssembly.pickUpPiece(i_Intake, intakeSpeed, feedSpeed));

      backwardsLauncherAngle.onTrue(l_Launcher.launcherRotateCommand(() -> 116))
                        .onTrue(new InstantCommand(() -> l_Launcher.setLauncherSpeed(Constants.LauncherConstants.kSpeakerLaunchSpeed.get(0.0))))
                        .onFalse(new InstantCommand(() -> l_Launcher.LauncherRotationAngle(0.0)))
                        .onFalse(new InstantCommand(() -> l_Launcher.setLauncherSpeed(0.0)));

      stopAssemly.onTrue(new InstantCommand(() -> i_Intake.setFeedAndIntakeSpeed(0, 0)))
                 .onTrue(new InstantCommand(() -> Constants.endAssembly1 = true))
                 .onFalse(new InstantCommand(() -> Constants.endAssembly1 = false));

                 
      CloseSpeakerAngle.onTrue(l_Launcher.launcherRotateCommand(() -> 50))
                       .onTrue(new InstantCommand(() -> l_Launcher.setLauncherSpeed(Constants.LauncherConstants.kSpeakerLaunchSpeed.get(0.0))))
                       .onFalse(new InstantCommand(() -> l_Launcher.LauncherRotationAngle(0.0)))
                       .onFalse(new InstantCommand(() -> l_Launcher.setLauncherSpeed(0.0)));
                 
      // ZeroAngle.onTrue(l_Launcher.launcherRotateCommand(() -> 0))
      //          .onFalse(new InstantCommand(() -> l_Launcher.LauncherRotationAngle(0.0)));
                 
      AmpAngle.onTrue(l_Launcher.launcherRotateCommand(() -> 125))
              .onFalse(new InstantCommand(() -> l_Launcher.LauncherRotationAngle(0.0)));   
                 
      SafeZoneAngle.onTrue(l_Launcher.launcherRotateCommand(() -> 30.5))
                   .onFalse(new InstantCommand(() -> l_Launcher.LauncherRotationAngle(0.0)));
                 
      // SpeakerLaunch.onTrue(new InstantCommand(() -> l_Launcher.setLauncherSpeed(Constants.LauncherConstants.kSpeakerLaunchSpeed.get(0.0))))
      //              .onFalse(new InstantCommand(() -> l_Launcher.setLauncherSpeed(0.0)));
      
      SpeakerLaunch.whileTrue(lt_LaunchTrackSubsystem.TargetCommand(l_Launcher))
                   .onTrue(new InstantCommand(() -> Constants.AimDone = false))
                   .onFalse(new InstantCommand(() -> Constants.AimDone = true));

      AmpLaunch.onTrue(new InstantCommand(() -> l_Launcher.setLauncherSpeed(Constants.LauncherConstants.kAMPLaunchSpeed.get(0.0))))
               .onFalse(new InstantCommand(() -> l_Launcher.setLauncherSpeed(0.0)));

      LauncherFeed.onTrue(new InstantCommand(() -> i_Intake.setFeedSpeed(Constants.LauncherConstants.kFeedSpeed.get(0.0))))
                  .onFalse(new InstantCommand(() -> i_Intake.setFeedSpeed(0.0)));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    //return new Autos(place auto stuff here)
    return new Autos(Constants.side, Constants.AutoSelected, s_Swerve, i_Intake, l_Launcher);
  }
}
