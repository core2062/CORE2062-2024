package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ScoreAssembly;

public class IntakeAssemblyCommand extends Command{
    private IntakeSubsystem i_Intake;
    private DoubleSupplier intakeSpeed, feedSpeed;

    public IntakeAssemblyCommand(IntakeSubsystem i_Intake, DoubleSupplier intakeSpeed, DoubleSupplier feedSpeed){
        this.i_Intake = i_Intake;
        addRequirements(i_Intake);

        this.intakeSpeed = intakeSpeed;
        this.feedSpeed = feedSpeed;
    }

    @Override
    public void execute() {
        i_Intake.setFeedAndIntakeSpeed(intakeSpeed.getAsDouble(), feedSpeed.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        i_Intake.setFeedAndIntakeSpeed(0, 0);
    }

    @Override
    public boolean isFinished() {
        boolean finished = ScoreAssembly.getPhotoeye();
        finished = !finished;
        return finished;
    }
}
