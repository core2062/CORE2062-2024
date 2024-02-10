package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.LauncherSubsystem;

public class LauncherAssemblyCommand extends Command{
    private LauncherSubsystem l_Launcher;
    private double speed;
    private int stage;

    public LauncherAssemblyCommand(LauncherSubsystem l_Launcher, double speed, int stage){
        this.l_Launcher = l_Launcher;
        addRequirements(l_Launcher);

        this.speed = speed;
        this.stage = stage;
    }

    @Override
    public void execute() {
        if (stage == 1){
            l_Launcher.setLauncherSpeed(speed);
        } else if (stage == 2){
            l_Launcher.setFeedSpeed(speed);
        }
    }

    @Override
    public boolean isFinished() {
        return Constants.assemblyDone;
    }
}
