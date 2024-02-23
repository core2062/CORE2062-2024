package frc.robot;

import frc.robot.commands.AutonShootCommand;
import frc.robot.commands.IntakeAssemblyCommand;
import frc.robot.commands.LauncherAimCommand;
import frc.robot.constants.Constants;
import frc.robot.subsystems.*;
import java.io.IOException;
import java.nio.file.Path;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;

public class Autos extends SequentialCommandGroup {    

    public Autos(int cases, Swerve s_Swerve, IntakeSubsystem i_Intake, LauncherSubsystem l_LauncherSubsystem){
        System.out.printf("autos selection: %d\n", cases);
        switch (cases) {
            case 0:
                movementAuto(s_Swerve, i_Intake, l_LauncherSubsystem);
                break;
            default:
                doNothingAuto(s_Swerve);
                break;
        }
    }




    public void doNothingAuto(Swerve s_Swerve) {}
                    
        



    public void movementAuto(Swerve s_Swerve, IntakeSubsystem i_Intake, LauncherSubsystem l_LauncherSubsystem) {
        System.out.println("move auto");  

        String trajectoryJSON = "paths/MoveBackPickup.wpilib.json"; //TODO: set path name to auton path
        Trajectory trajectory = new Trajectory(); 
        try {
            Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
            trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
            System.out.println("Path " + trajectoryPath);
        } catch (IOException ex) {
            DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
            };
        // An example trajectory to follow.  All units in meters.
        Trajectory Trajectory = trajectory;
        
        var thetaController =
        new ProfiledPIDController(Constants.AutoConstants.kPThetaController, 0, 0, Constants.AutoConstants.kThetaControllerConstraints);
        thetaController.enableContinuousInput(-Math.PI, Math.PI);
        
        SwerveControllerCommand swerveControllerCommand =
        new SwerveControllerCommand(
            Trajectory,
            s_Swerve::getPose,
            Constants.Swerve.swerveKinematics,
            new PIDController(Constants.AutoConstants.kPXController, 0, 0),
            new PIDController(Constants.AutoConstants.kPYController, 0, 0),
            thetaController,
            s_Swerve::setModuleStates,
            s_Swerve
        );
            
        addCommands(
            new InstantCommand(() -> s_Swerve.resetOdometry(Trajectory.getInitialPose())),
            new LauncherAimCommand(l_LauncherSubsystem, 51),
            new AutonShootCommand(i_Intake, l_LauncherSubsystem, 0.5, 0.6, 0.4),
            new LauncherAimCommand(l_LauncherSubsystem, 33.5),
            new InstantCommand(() -> i_Intake.setFeedAndIntakeSpeed(0.5, 0.5)),
            swerveControllerCommand,
            new IntakeAssemblyCommand(i_Intake, 0.5, 0.5),
            // new LauncherAimCommand(l_LauncherSubsystem, 33.5),
            new AutonShootCommand(i_Intake, l_LauncherSubsystem, 0.5, 0.6, 0.4),
            new LauncherAimCommand(l_LauncherSubsystem, 0)
        );
    }
}