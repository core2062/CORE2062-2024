package frc.robot.commands;

import frc.robot.constants.Constants;
import frc.robot.subsystems.Swerve;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;


public class TeleopSwerve extends Command {    
    private Swerve s_Swerve;    
    private DoubleSupplier translationSup;
    private DoubleSupplier strafeSup;
    private DoubleSupplier rotationSup;
    private BooleanSupplier robotCentricSup;
    private boolean applyDeadband;

    public TeleopSwerve(Swerve s_Swerve, boolean applyDeadband, DoubleSupplier translationSup, DoubleSupplier strafeSup, DoubleSupplier rotationSup, BooleanSupplier robotCentricSup) {
        this.s_Swerve = s_Swerve;
        addRequirements(s_Swerve);

        this.applyDeadband = applyDeadband;
        this.translationSup = translationSup;
        this.strafeSup = strafeSup;
        this.rotationSup = rotationSup;
        this.robotCentricSup = robotCentricSup;
    }

    public double applyDeadband(double input){
        if (applyDeadband){
            return MathUtil.applyDeadband(input, Constants.stickDeadband);
        } else {
            return input;
        }
    }

    @Override
    public void execute() {
        /* Get Values, Deadband*/
        double translationVal = applyDeadband(translationSup.getAsDouble());
        double strafeVal = applyDeadband(strafeSup.getAsDouble());
        double rotationVal = applyDeadband(rotationSup.getAsDouble());

        /* Drive */
        s_Swerve.drive(
            new Translation2d(translationVal, strafeVal).times(Constants.Swerve.maxSpeed), 
            rotationVal * Constants.Swerve.maxAngularVelocity, 
            !robotCentricSup.getAsBoolean(), 
            true
            
        );
    }
}