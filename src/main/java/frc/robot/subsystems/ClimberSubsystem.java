package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

public class ClimberSubsystem extends SubsystemBase{
    private TalonSRX m_ClimberMotor = new TalonSRX(Constants.ClimberConstants.kClimberMotorPort);
    
    public void setClimberSpeed(double speed){
        m_ClimberMotor.set(ControlMode.PercentOutput, speed);
    }
}
