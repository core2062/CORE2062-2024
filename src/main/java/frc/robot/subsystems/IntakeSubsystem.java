package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

public class IntakeSubsystem extends SubsystemBase{
    private double iterations = 0;
    private TalonFX intakeMotor = new TalonFX(Constants.IntakeConstants.kIntakeMotorPort);

    private TalonSRX leftFeedMotor = new TalonSRX(Constants.LauncherConstants.kLeftSideMotorPort);
    private TalonSRX rightFeedMotor = new TalonSRX(Constants.LauncherConstants.kRightSideMotorPort);
    
    public static DoubleSupplier intakeSpeed = () -> Constants.IntakeConstants.kIntakeSpeed.get(0.0);
    public double Intakespeed;
    public void setIntakeSpeed(double speed){
        intakeMotor.set(ControlMode.PercentOutput, speed);
    }
    
    public void setFeedSpeed(double speed){
        leftFeedMotor.set(ControlMode.PercentOutput, speed);
        rightFeedMotor.set(ControlMode.PercentOutput, -speed);
        if (speed == 0){
            System.out.println("Setting zero in feed function: " + iterations);
            iterations += 1;
        }
    }
    
    public void setFeedAndIntakeSpeed(double intakeSpeed, double feedSpeed){
        leftFeedMotor.set(ControlMode.PercentOutput, feedSpeed);
        rightFeedMotor.set(ControlMode.PercentOutput, -feedSpeed);
        
        intakeMotor.set(ControlMode.PercentOutput, intakeSpeed);
        if (feedSpeed == 0){
            System.out.println("Setting zero in feed and intake function: " + iterations);
            iterations += 1;
        }
    }

    @Override
    public void periodic() {
        Intakespeed = intakeSpeed.getAsDouble();
    }
}
