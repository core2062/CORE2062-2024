package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ScoreAssembly;

public class IntakeAssemblyCommand extends Command{
    private IntakeSubsystem i_Intake;
    private double speed;

    public IntakeAssemblyCommand(IntakeSubsystem i_Intake, double speed){
        this.i_Intake = i_Intake;
        addRequirements(i_Intake);

        this.speed = speed;
    }

    @Override
    public void execute() {
        i_Intake.setIntakeSpeed(speed);
    }

    @Override
    public boolean isFinished() {
        if (ScoreAssembly.getPhotoeye() == true){
            return true;
        } else if (Constants.endAssembly1 == true){
            return true;
        }else {
            return false;
        }
    }
}
