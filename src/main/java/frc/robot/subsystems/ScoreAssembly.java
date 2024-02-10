package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;

import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.IntakeAssemblyCommand;
import frc.robot.commands.LauncherAssemblyCommand;

public class ScoreAssembly extends SubsystemBase{
    private DigitalInput photoeye = new DigitalInput(0);

    public Command pickUpPiece(LauncherSubsystem l_Launcher, IntakeSubsystem i_Intake, double launcherSpeed, double intakeSpeed){
        Command runIntake = new IntakeAssemblyCommand(i_Intake, intakeSpeed);
        Command initiateLauncher = new LauncherAssemblyCommand(l_Launcher, launcherSpeed, 1);

        return runIntake.andThen(initiateLauncher);
    }

    public boolean getPhotoeye(){
        return photoeye.get();
    }

    public Command launchPiece(LauncherSubsystem l_Launcher, double feedSpeed){
        Command runFeed = new LauncherAssemblyCommand(l_Launcher, feedSpeed, 2);
        return runFeed;
    }
}
