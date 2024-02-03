package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Launcher extends SubsystemBase{
    private TalonFX upperLaunchMotor = new TalonFX(Constants.LauncherConstants.kUpperMotorPort);
    private TalonFX lowerLaunchMotor = new TalonFX(Constants.LauncherConstants.kLowerMotorPort);

    private TalonSRX leftLauncherMotor = new TalonSRX(Constants.LauncherConstants.kLeftSideMotorPort);
    private TalonSRX rightLauncherMotor = new TalonSRX(Constants.LauncherConstants.kRightSideMotorPort);

    private TalonSRX leftRotationMotor = new TalonSRX(Constants.LauncherConstants.kLeftRotationMotorPort);
    private TalonSRX rightRotationMotor = new TalonSRX(Constants.LauncherConstants.kRightRotationMotorPort);

    private DigitalInput photoeye = new DigitalInput(0);
    
    private boolean launcherOn = true;

    public void setLauncherSpeed(){
        if (launcherOn){
            upperLaunchMotor.set(Constants.LauncherConstants.kLaunchSpeed.get(0.0));
            lowerLaunchMotor.set(Constants.LauncherConstants.kLaunchSpeed.get(0.0));
            leftLauncherMotor.set(ControlMode.PercentOutput, Constants.LauncherConstants.kLaunchSpeed.get(0.0));
            rightLauncherMotor.set(ControlMode.PercentOutput, Constants.LauncherConstants.kLaunchSpeed.get(0.0));
            launcherOn = false;
        } else if (!launcherOn){
            upperLaunchMotor.set(0.0);
            lowerLaunchMotor.set(0.0);
            leftLauncherMotor.set(ControlMode.PercentOutput, 0.0);
            rightLauncherMotor.set(ControlMode.PercentOutput, 0.0);
            launcherOn = true;
        } else {
            System.out.println("Launcher Control Loop Broken");
        }
    }
    public void setLauncherSpeed(double speed){
        upperLaunchMotor.set(speed);
        lowerLaunchMotor.set(speed);
        leftLauncherMotor.set(ControlMode.PercentOutput, speed);
        rightLauncherMotor.set(ControlMode.PercentOutput, speed);
    }

    public void setAngle(){
        leftRotationMotor.set(ControlMode.Position, 0.0);
        rightRotationMotor.set(ControlMode.Position, 0.0);
    }

    public void configMotor(){
        leftRotationMotor.configFactoryDefault();
        leftRotationMotor.configAllSettings(Constants.LauncherConstants.motorConfigs.motorConfig);
        leftRotationMotor.setInverted(false);
        leftRotationMotor.setNeutralMode(NeutralMode.Brake);
        leftRotationMotor.setSelectedSensorPosition(0);
        leftRotationMotor.setSensorPhase(true);
    }

    public void checkForNote(){
        if (photoeye.get() == true) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }

    @Override
    public void periodic(){
        checkForNote();
    }
}