package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

public class LauncherSubsystem extends SubsystemBase{
    private TalonFX upperLaunchMotor = new TalonFX(Constants.LauncherConstants.kUpperMotorPort);
    private TalonFX lowerLaunchMotor = new TalonFX(Constants.LauncherConstants.kLowerMotorPort);

    private TalonSRX leftFeedMotor = new TalonSRX(Constants.LauncherConstants.kLeftSideMotorPort);
    private TalonSRX rightFeedMotor = new TalonSRX(Constants.LauncherConstants.kRightSideMotorPort);

    private TalonSRX leftRotationMotor = new TalonSRX(Constants.LauncherConstants.kLeftRotationMotorPort);
    private TalonSRX rightRotationMotor = new TalonSRX(Constants.LauncherConstants.kRightRotationMotorPort);

    private double Pose = 0.0;

    public void setLauncherSpeed(double speed){
        upperLaunchMotor.set(ControlMode.PercentOutput, speed);
        lowerLaunchMotor.set(ControlMode.PercentOutput, speed);
    }    
    
    public void setFeedSpeed(double speed){
        leftFeedMotor.set(ControlMode.PercentOutput, speed);
        rightFeedMotor.set(ControlMode.PercentOutput, speed);
    }

    public void setAngle(double angle){
        Pose = Constants.LauncherConstants.MotorPos;
        Constants.LauncherConstants.MotorPos = Pose;

        leftRotationMotor.set(ControlMode.MotionMagic, 0.0);
        rightRotationMotor.set(ControlMode.MotionMagic, 0.0);
    }

    double degreesToFalconSRX(double angle){
        return (angle / 360) * 4096;
    }

    double falconToDegrees(double ticks){ 
        return (ticks/4096) * 360;
    }

    public void configMotor(){
        leftRotationMotor.configFactoryDefault();
        leftRotationMotor.configAllSettings(Constants.LauncherConstants.motorConfigs.motorConfig);
        leftRotationMotor.setInverted(false);
        leftRotationMotor.setNeutralMode(NeutralMode.Brake);
        leftRotationMotor.setSelectedSensorPosition(0);
        leftRotationMotor.setSensorPhase(true);
        
        rightRotationMotor.configFactoryDefault();
        rightRotationMotor.configAllSettings(Constants.LauncherConstants.motorConfigs.motorConfig);
        rightRotationMotor.setInverted(false);
        rightRotationMotor.setNeutralMode(NeutralMode.Brake);
        rightRotationMotor.setSelectedSensorPosition(0);
        rightRotationMotor.setSensorPhase(true);
    }

    void resetEncoder(){
        leftRotationMotor.setSelectedSensorPosition(0.0);
        rightRotationMotor.setSelectedSensorPosition(0.0);
    }

    public double getSensorPos(){
        return falconToDegrees(leftRotationMotor.getSelectedSensorPosition());
    }
}