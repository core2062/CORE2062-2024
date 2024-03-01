package frc.robot.commands;

import frc.robot.constants.Constants;
import frc.robot.subsystems.LauncherSubsystem;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.SwerveTrackingSubsystem;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class AutonAlignmentCommand extends TeleopSwerve{
    private SwerveTrackingSubsystem t_Tracking;
    private Swerve s_Swerve;

    public AutonAlignmentCommand(SwerveTrackingSubsystem t_Tracking, Swerve s_Swerve){
        super(s_Swerve, false, () -> 0, () -> 0, () -> t_Tracking.getRotation(0), () -> false);
        this.t_Tracking = t_Tracking;
        this.s_Swerve = s_Swerve;
        addRequirements(s_Swerve, t_Tracking);
    }

    @Override
    public void initialize() {
        t_Tracking.setPipelineSpeaker();
    }

    @Override
    public void end(boolean interrupted) {
        s_Swerve.drive(
            new Translation2d(0, 0).times(Constants.Swerve.maxSpeed), 
            0 * Constants.Swerve.maxAngularVelocity, 
            false, 
            true
        );
    }

    @Override
    public boolean isFinished(){
        if (Math.abs(t_Tracking.x) <= 1){
            return true;
        } else{
            return false;
        }
    }
}