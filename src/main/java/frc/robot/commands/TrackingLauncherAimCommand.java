package frc.robot.commands;

import frc.robot.constants.Constants;
import frc.robot.subsystems.LauncherSubsystem;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class TrackingLauncherAimCommand extends Command{
    private LauncherSubsystem l_Launcher;
    private double DifferenceOfAngle;
    private double lastAngle = 0;

    public TrackingLauncherAimCommand(LauncherSubsystem l_Launcher){
        this.l_Launcher = l_Launcher;
        addRequirements(l_Launcher);
    }

    @Override
    public void execute() {
        System.out.println(Constants.VisionConstants.DesiredAngle);
        double currentAngle = l_Launcher.getLeftEncoderValue();
        final double MAX_SPEED_RPM = 3; // Maximum speed of the motor in RPM
        final double ANGLE_TOLERANCE = 1.0;
        // Calculate the angle difference
        if (Constants.VisionConstants.DesiredAngle < 10.3724){
            Constants.VisionConstants.DesiredAngle = lastAngle;
        } else {
            lastAngle = Constants.VisionConstants.DesiredAngle;
        }
        System.out.println(Constants.VisionConstants.DesiredAngle);
        double angleDifference = Constants.VisionConstants.DesiredAngle - currentAngle;
        DifferenceOfAngle = angleDifference;
        // Calculate the speed based on the angle difference
        double speedPercentage = angleDifference / 180.0; // Scaling the angle difference to [-1, 1]
        double speed = (speedPercentage * MAX_SPEED_RPM);
        
        // Ensure the speed is within the motor's range
        speed = Math.min(MAX_SPEED_RPM, Math.max(-MAX_SPEED_RPM, speed));
        
        
        // If the angle difference is within the tolerance, stop the motor
        if (Math.abs(angleDifference) <= ANGLE_TOLERANCE) {
            speed = 0; // Stop the motor
        } else if (Math.abs(angleDifference) > 10 && Math.abs(speed) < 0.5){
            if (speed < 0) {
                speed = -0.5;
            } else if (speed > 0){
                speed = 0.5;
            }
        } else if (Math.abs(angleDifference) > 0.5 && Math.abs(speed) < 0.3){
            if (speed < 0) {
                speed = -0.3;
            } else if (speed > 0){
                speed = 0.3;
            }
        }

        double changeINSpeed = 1.21038 - (0.0191341 * Constants.VisionConstants.DesiredAngle);
        Constants.LauncherConstants.kSpeakerLaunchSpeed.set(changeINSpeed); 

        l_Launcher.LauncherRotationAngle(speed);
        l_Launcher.setLauncherSpeed(changeINSpeed);
        SmartDashboard.putNumber("Desired Launcher Rotation Speed: ", speed);
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("Command Done");
        l_Launcher.LauncherRotationAngle(0);
        l_Launcher.setLauncherSpeed(0.0);
        Constants.LauncherConstants.kSpeakerLaunchSpeed.set(0.6);
    }

    @Override
    public boolean isFinished(){
        if (Constants.AimDone == true){
            return true;
        } else {
            return false;
        }
    }
}

