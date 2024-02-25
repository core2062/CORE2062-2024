package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

public class ClimberSubsystem extends SubsystemBase{
    private TalonSRX LeftClimberMotor = new TalonSRX(Constants.ClimberConstants.kLeftClimberMotorPort);
    private TalonSRX RightClimberMotor = new TalonSRX(Constants.ClimberConstants.kRightClimberMotorPort);
    
    public void setClimberSpeed(double speed){
        LeftClimberMotor.set(ControlMode.PercentOutput, speed);
        RightClimberMotor.set(ControlMode.PercentOutput, speed);
    }
}
