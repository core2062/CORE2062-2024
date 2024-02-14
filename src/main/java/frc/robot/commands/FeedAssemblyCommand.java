package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.LauncherSubsystem;
import frc.robot.subsystems.ScoreAssembly;

public class FeedAssemblyCommand extends Command{
    private LauncherSubsystem l_launcher;
    private double speed;
    private int stage;

    public FeedAssemblyCommand(LauncherSubsystem l_launcher, double speed, int stage){
        this.l_launcher = l_launcher;
        addRequirements(l_launcher);

        this.speed = speed;
        this.stage = stage;
    }

    @Override
    public void execute() {
        l_launcher.setFeedSpeed(speed);
    }

    @Override
    public boolean isFinished() {
        if (stage == 1 && Constants.endAssembly1 == true){
            return true;
        } else if (ScoreAssembly.getPhotoeye() == true && stage == 1) {
            return true;
        } else if (stage == 2 && Constants.assemblyDone == true) {
            return true;
        } else {
            return false;
        }
    }
}
