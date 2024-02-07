package frc.robot.commands;

import java.util.function.DoubleSupplier;

import frc.robot.Constants;
import frc.robot.subsystems.Launcher;
import edu.wpi.first.wpilibj2.command.Command;

public class AimCommand extends Command{
    private Launcher l_Launcher;
    private DoubleSupplier targetAngle;

    public AimCommand(Launcher l_Launcher, DoubleSupplier targetAngle){
        this.l_Launcher = l_Launcher;
        addRequirements(l_Launcher);
        this.targetAngle = targetAngle;
    }

    @Override
    public void execute() {
        l_Launcher.setAngle(targetAngle.getAsDouble());
        System.out.println("Target angle" + targetAngle);
    }

    @Override
    public boolean isFinished(){
        if (l_Launcher.getSensorPos() >= Constants.LauncherConstants.MotorPos + 2.5 || l_Launcher.getSensorPos() <= Constants.LauncherConstants.MotorPos - 2.5){
            return false;
        } else {
            return false;
        }
    }
}
