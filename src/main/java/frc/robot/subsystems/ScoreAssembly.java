package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;

import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.FeedAssemblyCommand;
import frc.robot.commands.IntakeAssemblyCommand;
import frc.robot.commands.LauncherAssemblyCommand;
import frc.robot.constants.Constants;

public class ScoreAssembly extends SubsystemBase{
    public static DigitalInput photoeye = new DigitalInput(0);
    
    // public ScoreAssembly(IntakeSubsystem i_Intake){
    //     setDefaultCommand(defaultCommand(i_Intake));
    // }

    public Command defaultCommand(IntakeSubsystem i_Intake){
        Command stopIntake = new InstantCommand(() -> i_Intake.setIntakeSpeed(0));
        // Command stopLauncher = new InstantCommand(() -> l_Launcher.setLauncherSpeed(0));
        Command stopFeed = new InstantCommand(() -> i_Intake.setFeedSpeed(0));

        Command stopCommand = stopIntake.andThen(stopFeed);
        stopCommand.addRequirements(i_Intake, this);
        return stopCommand;
    }

    public Command pickUpPiece(LauncherSubsystem l_Launcher, IntakeSubsystem i_Intake, DoubleSupplier intakeSpeed, DoubleSupplier feedSpeed){        
        Command PickUpCommand = new IntakeAssemblyCommand(i_Intake, intakeSpeed, feedSpeed);
        PickUpCommand.addRequirements(l_Launcher, i_Intake, this);
        return PickUpCommand;
    }
    
    public static boolean getPhotoeye(){
        SmartDashboard.putBoolean("Piece Ready", photoeye.get());
        return photoeye.get();
    }
}
