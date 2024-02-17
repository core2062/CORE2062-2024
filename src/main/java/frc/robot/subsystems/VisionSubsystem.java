package frc.robot.subsystems;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.LaunchdrAimCommand;
import frc.robot.commands.TeleopSwerve;
import frc.robot.constants.Constants;

public class VisionSubsystem extends SubsystemBase {
    // double angle;
    double x ;
    double id ;
    double y ;
    double area ;
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");
    NetworkTableEntry tid = table.getEntry("tid");
    NetworkTableEntry pipeline = table.getEntry("pipeline");
    public VisionSubsystem(){
    }
    @Override
    public void periodic() {
        x = tx.getDouble(0.0);
        id = tid.getDouble(0.0);
        y = ty.getDouble(0.0);
        area = ta.getDouble(0.0);
        SmartDashboard.putNumber("distance", getDistance());
        SmartDashboard.putNumber("limelightx", x);
        SmartDashboard.putNumber("limelighty", y);
        SmartDashboard.putNumber("limelighta", area);
        SmartDashboard.putNumber("limelightid", id);
        if (getDistance() > 7){
            if (Constants.VisionConstants.SpeakerID == 4){
                Constants.VisionConstants.SpeakerID = 1;
            } else if (Constants.VisionConstants.SpeakerID == 7) {
                Constants.VisionConstants.SpeakerID = 2;
            }
        } else if (getDistance() < 7){
            if (Constants.VisionConstants.SpeakerID == 1){
                Constants.VisionConstants.SpeakerID = 4;
            } else  if (Constants.VisionConstants.SpeakerID == 2) {
                Constants.VisionConstants.SpeakerID = 7;
            }

        }
    }
    
    public Command AimAtSpeaker(LauncherSubsystem l_Launcher, Swerve s_Swerve, int id, int offset, DoubleSupplier translationSup, DoubleSupplier strafeSup, BooleanSupplier robotCentricSup)    
    {
        
        Command setPipelineCommand = this.run(
            () -> pipeline.setDouble(Constants.VisionConstants.SpeakerID)
            );
            setPipelineCommand.addRequirements(this);
            // angle = getRotation(offset);
            // Command rotateMotorCommand = new LaunchdrAimCommand(l_Launcher, () -> getRotation(offset));
            Command rotateSwerveCommand = new TeleopSwerve(
                s_Swerve,
                false,
                translationSup,
                strafeSup,
                () -> getRotation(offset),
                robotCentricSup 
            );
        
        return setPipelineCommand.alongWith(rotateSwerveCommand);
    }

    public Command AimAtAmp(LauncherSubsystem l_Launcher, Swerve s_Swerve, int id, int offset, DoubleSupplier translationSup, DoubleSupplier strafeSup, BooleanSupplier robotCentricSup)
    {
        
        Command setPipelineCommand = this.run(
            () -> pipeline.setDouble(Constants.VisionConstants.SpeakerID)
            );
            setPipelineCommand.addRequirements(this);
            // angle = getRotation(offset);
        Command rotateMotorCommand = new LaunchdrAimCommand(l_Launcher, () -> getRotation(offset));
        Command rotateSwerveCommand = new TeleopSwerve(
            s_Swerve,
            false,
            translationSup,
            strafeSup,
            () -> getRotation(offset),
            robotCentricSup 
            );
        // System.out.println("Rotation: " + getRotation(offset));
        return setPipelineCommand.alongWith(rotateMotorCommand).alongWith(rotateSwerveCommand);
    }
    
    public double getDistance(){
        double area = ta.getDouble(0.0);
        double oneSide = Math.sqrt(area);
        double distance = 5.37/oneSide;
        return distance;
    }

    public double getTranslation(double targetDistance){
        double distance = getDistance();
        if (Double.isInfinite(distance)){
            return 0;
        }
        else{
            return targetDistance-distance;
        }
    }

    public double getRotation(double targetAngle){
        // System.out.println("id: " + id);
        if (id <= 0){
            System.out.println("id is 0");
            return 0;
        }
        else{
        //return (tx.getDouble(0.0)-targetAngle)*-0.1;
        System.out.println(rateOfChange((tx.getDouble(0.0)-targetAngle)*-0.04));

        // return rateOfChange((tx.getDouble(0.0) - targetAngle)*-0.04);
        return (tx.getDouble(0.0)-targetAngle)*-0.05;

        }
    }

    public static double rateOfChange(double x) {
        final double increaseRate = 1.1; // Adjust as needed

        // Calculate the distance from origin
        double distance = Math.abs(x);

        // Calculate rate of change
        double rate = distance * increaseRate;

        return rate;
    }

    public Command rotateSwerve(){
        
        return null;
    }
}