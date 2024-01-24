package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase{
    private TalonSRX intakeMotor = new TalonSRX(Constants.IntakeConstants.kIntakeMotorPort);
    
    private boolean intakeOn = true;

    public void setIntakeSpeed(){
        if (intakeOn){
            intakeMotor.set(ControlMode.PercentOutput, Constants.IntakeConstants.kIntakeSpeed.get(0.0));
        } else if (!intakeOn) {
            intakeMotor.set(ControlMode.PercentOutput, 0.0);
        } else {
            System.out.println("Intake Control Loop Broken");
        }
    }
    
    public void setIntakeSpeed(double speed){
        intakeMotor.set(ControlMode.PercentOutput, speed);
    }
    
}
