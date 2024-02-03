package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
// import com.ctre

import frc.robot.Constants.LauncherConstants;

public final class LauncherRotationMotorConfigs{

    public TalonSRXConfiguration motorConfig;
    
    public LauncherRotationMotorConfigs(){
      motorConfig = new TalonSRXConfiguration();
      motorConfig.slot0.kP = LauncherConstants.kP;
      motorConfig.slot0.kI = LauncherConstants.kI;
      motorConfig.slot0.kD = LauncherConstants.kD;
      motorConfig.slot0.kF = LauncherConstants.kF;
    
      motorConfig.continuousCurrentLimit = LauncherConstants.motorContinuousCurrentLimit;
      motorConfig.peakCurrentDuration = LauncherConstants.motorPeakCurrentDuration;
      motorConfig.peakCurrentLimit = LauncherConstants.motorPeakCurrentLimit;
      motorConfig.openloopRamp = LauncherConstants.motorOpenloopRamp;
      motorConfig.closedloopRamp = LauncherConstants.motorClosedloopRamp;
      motorConfig.motionAcceleration = 200.4;
      motorConfig.motionCruiseVelocity = 204.8;
      motorConfig.motionCurveStrength = 0;
    }
  }
