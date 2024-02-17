package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.LauncherSubsystem;
import frc.robot.subsystems.ScoreAssembly;

public class LauncherAssemblyCommand extends Command{
    private LauncherSubsystem l_Launcher;
    private double speed;

    public LauncherAssemblyCommand(LauncherSubsystem l_Launcher, double speed){
        this.l_Launcher = l_Launcher;
        addRequirements(l_Launcher);

        this.speed = speed;
    }

    @Override
    public void execute() {
        System.out.println("Laucnher Speed: " + speed);
        l_Launcher.setLauncherSpeed(speed);
    }

    @Override
    public boolean isFinished() {
        return Constants.assemblyDone;
    }
}
