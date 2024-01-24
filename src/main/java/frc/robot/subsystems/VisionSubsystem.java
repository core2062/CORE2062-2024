package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
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
    public VisionSubsystem(){

    }
    @Override
    public void periodic() {

        x = tx.getDouble(0.0);
        id = tid.getDouble(0.0);
         y = ty.getDouble(0.0);
        area = ta.getDouble(0.0);
        
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
            () -> (tx.getDouble(0.0)-offset)/-10,//rotate
            () -> false//robotCentric.getAsBoolean()
            
        );
        
        return setPipelineCommand.andThen(rotateSwerveCommand);
    }
}