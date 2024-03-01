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
import frc.robot.commands.ZeroLauncherCommand;
import frc.robot.constants.Constants;

public class LauncherSubsystem extends SubsystemBase{
    private TalonFX upperLaunchMotor = new TalonFX(Constants.LauncherConstants.kUpperMotorPort);
    private TalonFX lowerLaunchMotor = new TalonFX(Constants.LauncherConstants.kLowerMotorPort);
    
    private TalonSRX leftRotationMotor = new TalonSRX(Constants.LauncherConstants.kLeftRotationMotorPort);
    private TalonSRX rightRotationMotor = new TalonSRX(Constants.LauncherConstants.kRightRotationMotorPort);
    
    private static DutyCycleEncoder launcherRightPitchEncoder = new DutyCycleEncoder(1);
    private static DutyCycleEncoder launcherLeftPitchEncoder = new DutyCycleEncoder(2);
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

    public Command launcherRotateCommand(DoubleSupplier desiredangle){
        Command launch = new LauncherAimCommand(this, desiredangle);
        return launch;
    }

    public Command zeroLauncherCommand(){
        Command zero = new ZeroLauncherCommand(this);
        return zero;
    }

    public static double getRightEncoderValue(){
        return (launcherRightPitchEncoder.getDistance());
    }
    
    public static double getLeftEncoderValue(){
        return (-launcherLeftPitchEncoder.getDistance());
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

        launcherRightPitchEncoder.setDistancePerRotation(360);
        launcherLeftPitchEncoder.setDistancePerRotation(360);

    }

    @Override
    public void periodic() {
        if (leftRotationMotor.isFwdLimitSwitchClosed() == 1){
            resetEncoder();
        }
        SmartDashboard.putNumber("Right Encoder Value", getRightEncoderValue());
        SmartDashboard.putNumber("Left Encoder Value", getLeftEncoderValue());
        SmartDashboard.putNumber("Average Encoder Value: ", getAverageEncoderValue());
        SmartDashboard.putBoolean("Piece Ready", !ScoreAssembly.getPhotoeye());
    }

    public double getAverageEncoderValue(){
        double ave = getLeftEncoderValue() + getRightEncoderValue();
        return ave/2;
    }

    void resetEncoder(){
        launcherLeftPitchEncoder.reset();
        launcherRightPitchEncoder.reset();
    }

    public boolean getLimitSwitch(){
        return leftRotationMotor.isFwdLimitSwitchClosed() == 1;
    }
}