package frc.robot.commands;

import java.util.function.DoubleSupplier;

import frc.robot.constants.Constants;
import frc.robot.subsystems.LauncherSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class LauncherAimCommand extends Command{
    private LauncherSubsystem l_Launcher;
    private double DifferenceOfAngle;

    public LauncherAimCommand(LauncherSubsystem l_Launcher){
        this.l_Launcher = l_Launcher;
        addRequirements(l_Launcher);
    }

    @Override
    public void execute() {
        double desiredAngle = 51;
        double currentAngle = l_Launcher.getEncoderValue();
        final double MAX_SPEED_RPM = 2; // Maximum speed of the motor in RPM
        final double ANGLE_TOLERANCE = 1.0;
        // Calculate the angle difference
        double angleDifference = desiredAngle - currentAngle;
        DifferenceOfAngle = angleDifference;
        // Calculate the speed based on the angle difference
        double speedPercentage = angleDifference / 180.0; // Scaling the angle difference to [-1, 1]
        double speed = (speedPercentage * MAX_SPEED_RPM);
        
        // Ensure the speed is within the motor's range
        speed = Math.min(MAX_SPEED_RPM, Math.max(-MAX_SPEED_RPM, speed));
        
        
        // If the angle difference is within the tolerance, stop the motor
        if (Math.abs(angleDifference) <= ANGLE_TOLERANCE) {
            speed = 0; // Stop the motor
        } else if (Math.abs(angleDifference) > 10 && Math.abs(speed) < 0.4){
            if (speed < 0) {
                speed = -0.4;
            } else if (speed > 0){
                speed = 0.4;
            }
        } else if (Math.abs(angleDifference) > 1 && Math.abs(speed) < 0.2){
            if (speed < 0) {
                speed = -0.2;
            } else if (speed > 0){
                speed = 0.2;
            }
        }
        l_Launcher.LauncherRotationAngle(speed);
        SmartDashboard.putNumber("Desired Launcher Rotation Speed: ", speed);
    }

    @Override
    public boolean isFinished(){
        if (Math.abs(DifferenceOfAngle) <= 0.5){
            l_Launcher.LauncherRotationAngle(0.0);
            return true;
        } else {
            return false;
        }
    }
}
