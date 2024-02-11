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
        Constants.LauncherConstants.MotorPos = angle;

        leftRotationMotor.set(ControlMode.MotionMagic, degreesToFalconSRX(angle));
        rightRotationMotor.set(ControlMode.MotionMagic, degreesToFalconSRX(angle));
    }

    public void changeAngle(double angle){
        Constants.LauncherConstants.MotorPos += angle;

        leftRotationMotor.set(ControlMode.MotionMagic, degreesToFalconSRX(Constants.LauncherConstants.MotorPos));
        rightRotationMotor.set(ControlMode.MotionMagic, degreesToFalconSRX(Constants.LauncherConstants.MotorPos));
    }

    double degreesToFalconSRX(double angle){ //TODO: change depending on the gear ratio
        return (angle / 360) * 4096;
    }

    double falconToDegrees(double ticks){ 
        return (ticks/4096) * 360;
    }

    public void configMotors(){
        leftRotationMotor.configFactoryDefault();
        leftRotationMotor.configAllSettings(Constants.LauncherConstants.motorConfigs.motorConfig);
        leftRotationMotor.setInverted(false);//TODO: determine whither or not to invert
        leftRotationMotor.setNeutralMode(NeutralMode.Brake);
        leftRotationMotor.setSelectedSensorPosition(0);
        leftRotationMotor.setSensorPhase(true);
        
        rightRotationMotor.configFactoryDefault();
        rightRotationMotor.configAllSettings(Constants.LauncherConstants.motorConfigs.motorConfig);
        rightRotationMotor.setInverted(false);//TODO: determine whither or not to invert
        rightRotationMotor.setNeutralMode(NeutralMode.Brake);
        rightRotationMotor.setSelectedSensorPosition(0);
        rightRotationMotor.setSensorPhase(true);

        // rightRotationMotor.follow(leftRotationMotor); TODO: change depensing on which side has the encoder
    }

    void resetEncoder(){
        leftRotationMotor.setSelectedSensorPosition(0.0);
        rightRotationMotor.setSelectedSensorPosition(0.0);
    }

    public double getSensorPos(){
        return falconToDegrees(leftRotationMotor.getSelectedSensorPosition());
    }
}