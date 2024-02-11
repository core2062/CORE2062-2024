// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

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
  
  private final JoystickButton SpeakerTrack = new JoystickButton(driver, XboxController.Button.kY.value);
  private final JoystickButton AmpTrack = new JoystickButton(driver, XboxController.Button.kX.value);
  /* Operator Buttons */
  /* ----- Manuel overrides ----- */
  private final JoystickButton launcherMotors = new JoystickButton(operator, XboxController.Button.kRightBumper.value);
  private final JoystickButton intakeFeed = new JoystickButton(operator, XboxController.Button.kLeftBumper.value);

  private final POVButton increaseLauncherHeading = new POVButton(operator, 0);
  private final POVButton decreaseLauncherHeading = new POVButton(operator, 180);
  /* ----- sequences of actions ----- */
  private final JoystickButton ScoreAssembly1 = new JoystickButton(operator, XboxController.Button.kA.value);
  private final JoystickButton ScoreAssembly2 = new JoystickButton(operator, XboxController.Button.kB.value);

  
  /* Subsystems */
  private final Swerve s_Swerve = new Swerve();
  private final IntakeSubsystem i_Intake = new IntakeSubsystem();
  private final LauncherSubsystem l_Launcher = new LauncherSubsystem();
  private final ScoreAssembly c_ScoreAssembly = new ScoreAssembly();
  private final VisionSubsystem v_VisionSubsystem = new VisionSubsystem();

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
      zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroGyro())); //TODO: add buttons based upon funstions wanted
      SpeakerTrack.whileTrue(v_VisionSubsystem.AimAtSpeaker(l_Launcher, s_Swerve, Constants.VisionConstants.SpeakerID, 0,
                                                              () -> -driver.getRawAxis(translationAxis),
                                                              () -> -driver.getRawAxis(strafeAxis),
                                                              () -> robotCentric.getAsBoolean()
                                                              ));
      AmpTrack.whileTrue(v_VisionSubsystem.AimAtSpeaker(l_Launcher, s_Swerve, Constants.VisionConstants.SpeakerID,0,
                                                              () -> -driver.getRawAxis(translationAxis),
                                                              () -> -driver.getRawAxis(strafeAxis),
                                                              () -> robotCentric.getAsBoolean()
                                                              ));

    /* Operator Buttons */
      launcherMotors.onTrue(new InstantCommand(() -> l_Launcher.setLauncherSpeed(0.4)));
      intakeFeed.onTrue(new InstantCommand(() -> i_Intake.setIntakeSpeed()));

      increaseLauncherHeading.onTrue(new InstantCommand(() -> l_Launcher.changeAngle(10)));
      decreaseLauncherHeading.onTrue(new InstantCommand(() -> l_Launcher.changeAngle(-10)));

      ScoreAssembly1.whileTrue(c_ScoreAssembly.pickUpPiece(l_Launcher, i_Intake, 0.8, 0.4));
      ScoreAssembly2.whileTrue(c_ScoreAssembly.launchPiece(l_Launcher, 0.4)).onFalse(new InstantCommand(() ->Constants.assemblyDone = true));

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
