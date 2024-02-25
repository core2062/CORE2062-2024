package frc.robot.subsystems;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import org.opencv.core.Mat;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.None;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.LauncherAimCommand;
import frc.robot.commands.TeleopSwerve;
import frc.robot.commands.TrackingLauncherAimCommand;
import frc.robot.subsystems.LauncherSubsystem;
import frc.robot.constants.Constants;

public class AprilTagSubsystem extends SubsystemBase {
    // double angle;
    double x ;
    double id ;
    double y ;
    double area ;
  
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-front");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");
    NetworkTableEntry tid = table.getEntry("tid");
    NetworkTableEntry pipeline = table.getEntry("pipeline");
    public AprilTagSubsystem(){
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
        Constants.VisionConstants.DesiredAngle = findAngle();
        SmartDashboard.putNumber("Desired Angle of Launcher: ", Constants.VisionConstants.DesiredAngle);
    }
    
    public Command AimAtSpeaker(Swerve s_Swerve, DoubleSupplier translationSup, DoubleSupplier strafeSup, BooleanSupplier robotCentricSup) {
        Command setPipelineCommand = this.run(
            () -> pipeline.setDouble(Constants.VisionConstants.SpeakerID)
            );
            setPipelineCommand.addRequirements(this);
            Command rotateSwerveCommand = new TeleopSwerve(
                s_Swerve,
                false,
                translationSup,
                strafeSup,
                () -> getRotation(0),
                robotCentricSup 
            );
        return setPipelineCommand.alongWith(rotateSwerveCommand);
    }

    public Command TargetCommand(LauncherSubsystem l_Launcher){
        Command setPipelineCommand = this.run(
            () -> pipeline.setDouble(Constants.VisionConstants.SpeakerID)
            );
        setPipelineCommand.addRequirements(this);
        Command setAngle = new TrackingLauncherAimCommand(l_Launcher);
        return setPipelineCommand.alongWith(setAngle);
    }
    
    public double getDistance(){
        double area = ta.getDouble(0.0);
        double oneSide = Math.sqrt(area);
        double distance = 4.83/oneSide; //TODO: adjust value for apriltag tracking
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
            return (tx.getDouble(0.0)-targetAngle)*-0.04;
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

    // public double getDesiredAngle() {
    //     double angle = LauncherSubsystem.getEncoderValue();
    //     double LimelightHeight = getHeightOfLimelight();
    //     double AprilTagHeight = 57.125;
    //     double angleToGoalDeg = angle + ty.getDouble(0.0);
    //     double angleToGoalRad = angleToGoalDeg * (Math.PI / 180.0);
    //     double dist = (AprilTagHeight - LimelightHeight) / Math.tan(angleToGoalRad);

    //     double SpeakerHeight = 78;
    //     double noteThickness = 2;
    //     double floorToPivot = 15.44;

    //     double speakerOpening = SpeakerHeight + noteThickness;
    //     double heightForCalc = speakerOpening - floorToPivot;

    //     double hypotenuse = Math.sqrt((heightForCalc * heightForCalc) + (dist * dist) - (2 * heightForCalc * dist *Math.cos(90)));
    //     double desiredangle = Math.asin(heightForCalc/(hypotenuse/Math.sin(90)));
    //     return dist;
    // }


    // public double getHeightOfLimelight(){
        
    //     double LimelightAngle = LauncherSubsystem.getEncoderValue();

    //     double angle = 17*(1-Math.cos(LimelightAngle));
        
    //     // double angle2 = 90 - LimelightAngle;

    //     // return (17/Math.sin(angle2)) * Math.sin(90);
    //     return angle;
    // }

    // public double getDistanceToObject() {
    //     double Angle = LauncherSubsystem.getEncoderValue();
    //     double dist = 12 * getDistance();
    //     double angle2 = 90 - Angle;
    //     double dist2 = (dist / Math.sin(90)) * Math.sin(angle2);
    //     return dist2;
    // }

    // public double findAngle() {
    //     double dist = getDistanceToObject();
    //     double SpeakerHeight = 57.125;
    //     double hypotenuse = Math.sqrt((dist * dist) + (SpeakerHeight * SpeakerHeight));
    //     double desiredAngle = (SpeakerHeight/(hypotenuse/Math.sin(90)));
    //     return desiredAngle;
    // }
    public double findAngle(){
        //formula calulated from dcode.fr/function-equation-finder and data points collected
        double Dist = ((0.985078 * (getDistance() * 12)) + 11.3942) + 19.5;
        // double Dist = (getDistance() * 12) + 19.5;
        // System.out.println(Dist);

        //height Offset 
        double floorToPivot = 15.49;

        double targetHeight = 78 - floorToPivot;
        double apriltagHeight = 57.125 - floorToPivot;

        double xDist = Math.sqrt(Math.pow(Dist, 2) - Math.pow(apriltagHeight, 2));
        // System.out.println("xDist: " + xDist);
        double targetAngle = Math.toDegrees(Math.atan2(targetHeight, xDist));
        double desiredAngle = ((1.0899 * targetAngle) + 2.3724);
        if (desiredAngle < 21){
            desiredAngle += 3.5;
        } else if (desiredAngle < 24){
            desiredAngle += 3;
        } else if (desiredAngle > 25.5){
            desiredAngle += 2;
        }
        return desiredAngle;
    }
}