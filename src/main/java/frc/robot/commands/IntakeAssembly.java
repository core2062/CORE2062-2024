package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.ScoreAssembly;

public class IntakeAssembly extends Command{
    private ScoreAssembly score = new ScoreAssembly();
    private Intake i_Intake;
    private double speed;

    public IntakeAssembly(Intake i_Intake, double speed){
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
        if (score.getPhotoeye() == true){
            return true;
        } else {
            return false;
        }
    }
}
