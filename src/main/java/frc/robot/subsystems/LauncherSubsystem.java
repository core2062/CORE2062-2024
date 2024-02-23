package frc.robot.subsystems;

// import java.lang.invoke.ClassSpecializer.SpeciesData;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.LauncherAimCommand;
import frc.robot.constants.Constants;

public class LauncherSubsystem extends SubsystemBase{
    private TalonFX upperLaunchMotor = new TalonFX(Constants.LauncherConstants.kUpperMotorPort);
    private TalonFX lowerLaunchMotor = new TalonFX(Constants.LauncherConstants.kLowerMotorPort);
    
    private TalonSRX leftRotationMotor = new TalonSRX(Constants.LauncherConstants.kLeftRotationMotorPort);
    private TalonSRX rightRotationMotor = new TalonSRX(Constants.LauncherConstants.kRightRotationMotorPort);
    
    private DutyCycleEncoder launcherPitchEncoder = new DutyCycleEncoder(1);
    public double encoderValue = 0.0;

    public double launchSpeed;
    public static double leftRotateSpeed;
    public static double rightRotateSpeed;
    public static DoubleSupplier rotateSpeed;
    public static double desiredLauncherAngle;

    public void setLauncherSpeed(double speed){
        upperLaunchMotor.set(ControlMode.PercentOutput, -speed);
        lowerLaunchMotor.set(ControlMode.PercentOutput, -speed);
    }
    
    public void LauncherRotationPercent(double leftSpeed, double rightSpeed){
        leftRotationMotor.set(ControlMode.PercentOutput, leftSpeed);
        rightRotationMotor.set(ControlMode.PercentOutput, rightSpeed);
    }

    public void LauncherRotationAngle(double speed){
        leftRotationMotor.set(ControlMode.PercentOutput, -speed);
        rightRotationMotor.set(ControlMode.PercentOutput, -speed);
    }

    public Command launcherRotateCommand(double desiredangle){
        Command launch = new LauncherAimCommand(this, desiredangle);
        return launch;
    }

    public double getEncoderValue(){
        return launcherPitchEncoder.getDistance();
    }

    public void configMotors(){
        leftRotationMotor.configFactoryDefault();
        leftRotationMotor.setInverted(false);
        leftRotationMotor.setNeutralMode(NeutralMode.Brake);
        leftRotationMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        
        rightRotationMotor.configFactoryDefault();
        rightRotationMotor.setInverted(true);
        rightRotationMotor.setNeutralMode(NeutralMode.Brake);
        rightRotationMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);

        launcherPitchEncoder.setDistancePerRotation(360);
    }

    @Override
    public void periodic() {
        if (leftRotationMotor.isFwdLimitSwitchClosed() == 1){
            resetEncoder();
        }
        SmartDashboard.putNumber("Encoder Value", getEncoderValue());
    }

    void resetEncoder(){
        launcherPitchEncoder.reset();
    }
}