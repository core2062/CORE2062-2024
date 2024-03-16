package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LauncherSubsystem;
import frc.robot.subsystems.ScoreAssembly;

public class IntakeAssemblyCommand extends Command{
    private IntakeSubsystem i_Intake;
    private LauncherSubsystem l_Launcher;
    private double intakeSpeed, feedSpeed;

    public IntakeAssemblyCommand(IntakeSubsystem i_Intake, double intakeSpeed, double feedSpeed, LauncherSubsystem l_Launcher){
        this.i_Intake = i_Intake;
        this.l_Launcher = l_Launcher;
        addRequirements(i_Intake, l_Launcher);

        this.intakeSpeed = intakeSpeed;
        this.feedSpeed = feedSpeed;
    }

    @Override
    public void initialize() {
        SmartDashboard.putString("Auton State", "Beginging Intake");
    }

    @Override
    public void execute() {
        i_Intake.setFeedAndIntakeSpeed(intakeSpeed, feedSpeed);
    }

    @Override
    public void end(boolean interrupted) {
        i_Intake.setFeedAndIntakeSpeed(0, 0);
        SmartDashboard.putString("Auton State", "Intake Complete");
    }

    @Override
    public boolean isFinished() {
        boolean finished = ScoreAssembly.getPhotoeye();
        finished = !finished;
        if (l_Launcher.getRightEncoderValue() > 30){
            return true;
        } else if (Constants.endAssembly1 == true){
            return true;
        } else {
            return finished;
        }
    }
}
