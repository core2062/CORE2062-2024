package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;

import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.FeedAssemblyCommand;
import frc.robot.commands.IntakeAssemblyCommand;
import frc.robot.commands.LauncherAssemblyCommand;
import frc.robot.constants.Constants;

public class ScoreAssembly extends SubsystemBase{
    // private DigitalInput photoeye = new DigitalInput(0);

    public Command pickUpPiece(LauncherSubsystem l_Launcher, IntakeSubsystem i_Intake, double launcherSpeed, double intakeSpeed){
        Command runIntake = new IntakeAssemblyCommand(i_Intake,  intakeSpeed);
        Command initiateLauncher = new LauncherAssemblyCommand(l_Launcher, launcherSpeed);
        Command runFeed = new FeedAssemblyCommand(l_Launcher, Constants.LauncherConstants.kFeedSpeed.get(0.0), 1);

        return runIntake.alongWith(runFeed).andThen(initiateLauncher);
    }

    public static boolean getPhotoeye(){
        // return photoeye.get();
        return false;
    }

    public Command launchPiece(LauncherSubsystem l_Launcher){
        Command runFeed = new LauncherAssemblyCommand(l_Launcher, Constants.LauncherConstants.kFeedSpeed.get(0.0));
        return runFeed;
    }
}
