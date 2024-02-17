package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LauncherSubsystem;
import frc.robot.subsystems.ScoreAssembly;

public class FeedAssemblyCommand extends Command{
    private IntakeSubsystem i_Intake;
    private DoubleSupplier speed, delay;
    public static Timer feedDelaytime = new Timer();


    public FeedAssemblyCommand(IntakeSubsystem i_Intake, DoubleSupplier speed, DoubleSupplier delay){
        this.i_Intake = i_Intake;
        addRequirements(i_Intake);

        this.speed = speed;
        this.delay = delay;
    }

    @Override
    public void initialize() {
        feedDelaytime.start();
    }

    @Override
    public void execute() {
        if (feedDelaytime.get() > delay.getAsDouble()){
            i_Intake.setFeedSpeed(speed.getAsDouble());
        }
    }

    @Override
    public void end(boolean interrupted) {
        feedDelaytime.stop();
        feedDelaytime.reset();
    }

    @Override
    public boolean isFinished() {
        return Constants.assemblyDone;
    }
}
