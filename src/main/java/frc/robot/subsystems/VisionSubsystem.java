package frc.robot.subsystems;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.TeleopSwerve;

public class VisionSubsystem extends SubsystemBase {
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
    DataLog log = DataLogManager.getLog();
    DoubleLogEntry myDoubleLog = new DoubleLogEntry(log, "/my/double");
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
        SmartDashboard.putNumber("rotation", XboxController.Axis.kRightX.value);
        SmartDashboard.putNumber("strafe", XboxController.Axis.kLeftX.value);
        SmartDashboard.putNumber("translational", XboxController.Axis.kLeftY.value);
         myDoubleLog.append((tx.getDouble(0.0))*-0.04);
    }
    public Command AimAtApril(Swerve swerve, int id, int offset)
    {
        Command setPipelineCommand = this.runOnce(
            () -> pipeline.setDouble(id)
        );
        setPipelineCommand.addRequirements(this);
        Command rotateSwerveCommand = new TeleopSwerve(
            swerve, 
            false, 
            () -> 0, //translation
            () -> 0,//strafe
            () -> getRotation(offset),//rotate
            () -> false//robotCentric.getAsBoolean()
            
        );
        
        return setPipelineCommand.andThen(rotateSwerveCommand);
    }
    
    public double getDistance(){
    double area= ta.getDouble(0.0);
    double oneSide=Math.sqrt(area);
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
        if (id <= 0){
           
            return 0;
        }
        else{
        
        //return (tx.getDouble(0.0)-targetAngle)*-0.1;
        return (tx.getDouble(0.0)-targetAngle)*-0.04;
        }
    }
    public Command DriveToDistanceFromApril(Swerve swerve, int id, int distance)
    
     {
        Command setPipelineCommand = this.runOnce(
            () -> pipeline.setDouble(id)
        );
        setPipelineCommand.addRequirements(this);
        Command rotateSwerveCommand = new TeleopSwerve(
            swerve, 
            false, 
            () -> getTranslation(distance)*-1, //translation
            () -> 0,//strafe
            () -> 0,//rotate
            () -> true//robotCentric.getAsBoolean()
            //.8=6ft 1.73=4ft .45=8ft
            
        );
        
        return setPipelineCommand.andThen(rotateSwerveCommand);
    }
     public Command DriveAndAimAtApril(Swerve swerve, int id, int offset, DoubleSupplier translationSupplier, DoubleSupplier strafeSupplier, BooleanSupplier robotCentricSupplier)
    {
        Command setPipelineCommand = this.runOnce(
            () -> pipeline.setDouble(id)
        );
        setPipelineCommand.addRequirements(this);
        Command rotateSwerveCommand = new TeleopSwerve(
            swerve, 
            false, 
            translationSupplier, 
            strafeSupplier,
            () -> getRotation(offset),//rotate
            robotCentricSupplier
            
        );
        
        return setPipelineCommand.andThen(rotateSwerveCommand);
    }
    
}
