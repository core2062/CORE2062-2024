// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.subsystems.*;
import frc.robot.commands.*;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  /* Controllers */
  private final Joystick driver = new Joystick(0);

  /* Drive Controls */
  private final int translationAxis = XboxController.Axis.kLeftY.value;
  private final int strafeAxis = XboxController.Axis.kLeftX.value;
  private final int rotationAxis = XboxController.Axis.kRightX.value;

  /* Drive Buttons */
  private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kRightBumper.value);
  private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);
  private final JoystickButton aimAtOne = new JoystickButton(driver, XboxController.Button.kA.value);
  private final JoystickButton aimAtTwo = new JoystickButton(driver, XboxController.Button.kB.value);
  private final JoystickButton aimAtThree = new JoystickButton(driver, XboxController.Button.kX.value);
  private final JoystickButton aimAtFour = new JoystickButton(driver, XboxController.Button.kY.value);
  
  /* Subsystems */
  private final Swerve s_Swerve = new Swerve();
  private final VisionSubsystem s_VisionSubsystem = new VisionSubsystem();

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
        aimAtOne.whileTrue(s_VisionSubsystem.DriveAndAimAtApril(s_Swerve,2,0,
          () -> -driver.getRawAxis(translationAxis),
          () -> -driver.getRawAxis(strafeAxis),
          () -> robotCentric.getAsBoolean()
        ));
        aimAtTwo.whileTrue(s_VisionSubsystem.DriveAndAimAtApril(s_Swerve,3,0,
          () -> -driver.getRawAxis(translationAxis),
          () -> -driver.getRawAxis(strafeAxis),
          () -> robotCentric.getAsBoolean()
        ));
        aimAtThree.whileTrue(s_VisionSubsystem.DriveToDistanceFromApril(s_Swerve,4,6));
        aimAtFour.whileTrue(s_VisionSubsystem.DriveAndAimAtApril(s_Swerve,4,0,
          () -> -driver.getRawAxis(translationAxis),
          () -> -driver.getRawAxis(strafeAxis),
          () -> robotCentric.getAsBoolean()
        ));
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
