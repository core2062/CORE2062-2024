package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.AnalogEncoder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
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

    private DutyCycleEncoder launcherPitchEncoder = new DutyCycleEncoder(1);
    private double encoderValue = 0.0;

    public void setLauncherSpeed(double speed){
        upperLaunchMotor.set(ControlMode.PercentOutput, -speed);
        lowerLaunchMotor.set(ControlMode.PercentOutput, -speed);
    }    
    
    public void setFeedSpeed(double speed){
        leftFeedMotor.set(ControlMode.PercentOutput, speed);
        rightFeedMotor.set(ControlMode.PercentOutput, -speed);
    }
    
    public void LauncherRotationPercent(double speed){
        leftRotationMotor.set(ControlMode.PercentOutput, speed);
    }

    public void LauncherRotationAngle(double angle){
        // leftRotationMotor.set(ControlMode.PercentOutput, calculateSpeed(angle, encoderValue));
        System.out.println(calculateSpeed(angle, encoderValue));
    }

    public static int calculateSpeed(double desiredAngle, double currentAngle) {
        final int MAX_SPEED_RPM = 5330; // Maximum speed of the motor in RPM
        final double ANGLE_TOLERANCE = 1.0;
        // Calculate the angle difference
        double angleDifference = desiredAngle - currentAngle;

        // If the angle difference is within the tolerance, stop the motor
        if (Math.abs(angleDifference) <= ANGLE_TOLERANCE) {
            return 0; // Stop the motor
        }

        // Calculate the speed based on the angle difference
        double speedPercentage = angleDifference / 180.0; // Scaling the angle difference to [-1, 1]
        int speed = (int) (speedPercentage * MAX_SPEED_RPM);

        // Ensure the speed is within the motor's range
        speed = Math.min(MAX_SPEED_RPM, Math.max(-MAX_SPEED_RPM, speed));

        return speed;
    }

    // public void setAngle(double angle){
    //     Constants.LauncherConstants.MotorPos = angle;

    //     leftRotationMotor.set(ControlMode.MotionMagic, degreesToFalconSRX(angle));
    //     rightRotationMotor.set(ControlMode.MotionMagic, degreesToFalconSRX(angle));
    // }

    // public void changeAngle(double angle){
    //     Constants.LauncherConstants.MotorPos += angle;

    //     leftRotationMotor.set(ControlMode.MotionMagic, degreesToFalconSRX(Constants.LauncherConstants.MotorPos));
    //     rightRotationMotor.set(ControlMode.MotionMagic, degreesToFalconSRX(Constants.LauncherConstants.MotorPos));
    // }


    // double degreesToFalconSRX(double angle){
    //     return (angle / 360) * (4096 * 400);
    // }

    // double falconToDegrees(double ticks){ 
    //     return (ticks/(4096 * 400)) * 360;
    // }

    public void configMotors(){
        leftRotationMotor.configFactoryDefault();
        // leftRotationMotor.configAllSettings(Constants.LauncherConstants.motorConfigs.motorConfig);
        leftRotationMotor.setInverted(false);//TODO: determine whither or not to invert
        leftRotationMotor.setNeutralMode(NeutralMode.Brake);
        // leftRotationMotor.setSelectedSensorPosition(0);
        // leftRotationMotor.setSensorPhase(true);
        leftRotationMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        // leftRotationMotor.configLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        
        rightRotationMotor.configFactoryDefault();
        // rightRotationMotor.configAllSettings(Constants.LauncherConstants.motorConfigs.motorConfig);
        rightRotationMotor.setInverted(true);//TODO: determine whither or not to invert
        rightRotationMotor.setNeutralMode(NeutralMode.Brake);
        // rightRotationMotor.setSelectedSensorPosition(0);
        // rightRotationMotor.setSensorPhase(true);

        rightRotationMotor.follow(leftRotationMotor); //TODO: change depensing on which side has the limitswitch

        launcherPitchEncoder.setDistancePerRotation(360);
    }


    @Override
    public void periodic() {
        encoderValue = launcherPitchEncoder.getDistance();
        if (leftRotationMotor.isFwdLimitSwitchClosed() == 1){
            launcherPitchEncoder.reset();
        }
    }

    void resetEncoder(){
        launcherPitchEncoder.reset();
    }

    // public double getSensorPos(){
    //     return falconToDegrees(leftRotationMotor.getSelectedSensorPosition());
    // }
}