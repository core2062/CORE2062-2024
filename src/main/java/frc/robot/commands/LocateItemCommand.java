package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeVisionSubsystem;
import frc.robot.subsystems.LauncherVisionSubsystem;

public class LocateItemCommand extends Command{
    private LauncherVisionSubsystem v_LauncherVisionSubsystem;
    private IntakeVisionSubsystem i_IntakeVisionSubsystem;
    private double speed;
    private int id;
    private String aimAt;
    private boolean end;

    public LocateItemCommand(IntakeVisionSubsystem i_IntakeVisionSubsystem, LauncherVisionSubsystem v_LauncherVisionSubsystem, double speed, int id, String aimAt){
        this.v_LauncherVisionSubsystem = v_LauncherVisionSubsystem;
        this.i_IntakeVisionSubsystem = i_IntakeVisionSubsystem;
        addRequirements(v_LauncherVisionSubsystem);
        addRequirements(i_IntakeVisionSubsystem);

        this.speed = speed;
        this.id = id;
        this.aimAt = aimAt;
    }

    @Override
    public void execute() {
        if (aimAt == "speaker" || aimAt == "amp"){

        } else if (aimAt == "note"){

        }
    }

    @Override
    public boolean isFinished() {
        if (aimAt == "speaker"){
            if (v_LauncherVisionSubsystem.tid.getDouble(0.0) == id){
                end = true;
            } else {
                end = false;
            }
        } else if (aimAt == "amp"){
            if (v_LauncherVisionSubsystem.tid.getDouble(0.0) == id){
                end = true;
            } else {
                end = false;
            }
        } else if (aimAt == "note"){
            if (i_IntakeVisionSubsystem.tid.getDouble(0.0) == -1){
                end = true;
            } else {
                end = false;
            }
        }
        return end;
    }
}
