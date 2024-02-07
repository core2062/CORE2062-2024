package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;

import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.IntakeAssembly;
import frc.robot.commands.LauncherAssembly;

public class ScoreAssembly extends SubsystemBase{
    private DigitalInput photoeye = new DigitalInput(0);

    public Command pickUpPiece(Launcher l_Launcher, Intake i_Intake, double launcherSpeed, double intakeSpeed){
        Command runIntake = new IntakeAssembly(i_Intake, intakeSpeed);
        Command initiateLauncher = new LauncherAssembly(l_Launcher, launcherSpeed, 1);

        return runIntake.andThen(initiateLauncher);
    }

    public boolean getPhotoeye(){
        return photoeye.get();
    }

    public Command launchPiece(Launcher l_Launcher, double feedSpeed){
        Command runFeed = new LauncherAssembly(l_Launcher, feedSpeed, 2);
        return runFeed;
    }
}
