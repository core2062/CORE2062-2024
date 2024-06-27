package frc.robot.commands;

import frc.robot.constants.Constants;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.SwerveTrackingSubsystem;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonMoveForwardCommand extends TeleopSwerve{
    private Swerve s_Swerve;
    private Timer time = new Timer();
    private double duration;

    public AutonMoveForwardCommand(Swerve s_Swerve, double duration){
        super(s_Swerve, false, () -> 0.4, () -> 0, () -> 0, () -> false);
        this.s_Swerve = s_Swerve;
        addRequirements(s_Swerve);
        this.duration = duration;
    }

    @Override
    public void initialize() {
        SmartDashboard.putString("Auton State", "Beginging Movement");  
        time.start();
    }


    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public void end(boolean interrupted) {
        s_Swerve.drive(
            new Translation2d(0, 0).times(Constants.Swerve.maxSpeed), 
            0 * Constants.Swerve.maxAngularVelocity, 
            false, 
            true
        );
        SmartDashboard.putString("Auton State", "Movement Done");
        time.stop();
        time.reset();
    }

    @Override
    public boolean isFinished(){
        if (time.get() > duration){
            return true;
        } else{
            return false;
        }
    }
}