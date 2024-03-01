package frc.robot.commands;

import frc.robot.subsystems.LauncherSubsystem;

import edu.wpi.first.wpilibj2.command.Command;

public class ZeroLauncherCommand extends Command{
    private LauncherSubsystem l_Launcher;

    public ZeroLauncherCommand(LauncherSubsystem l_Launcher){
        this.l_Launcher = l_Launcher;
        addRequirements(l_Launcher);
    }

    @Override
    public void execute() {
        l_Launcher.LauncherRotationAngle(-0.75);
    }

    @Override
    public void end(boolean interrupted) {
        l_Launcher.LauncherRotationAngle(0);
    }

    @Override
    public boolean isFinished(){
        if (l_Launcher.getLimitSwitch() == true) {
            return true;
        } else{
            return false;
        }
    }
}
