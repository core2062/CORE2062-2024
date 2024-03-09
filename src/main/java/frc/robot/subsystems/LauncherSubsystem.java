package frc.robot.subsystems;

// import java.lang.invoke.ClassSpecializer.SpeciesData;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.Angle;
import edu.wpi.first.units.Distance;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.MutableMeasure;
import edu.wpi.first.units.Time;
import edu.wpi.first.units.Voltage;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.units.Units.*;
import edu.wpi.first.units.Velocity;
import edu.wpi.first.wpilibj.Timer;

import static edu.wpi.first.units.MutableMeasure.mutable;
import static edu.wpi.first.units.Units.Degree;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Volts;

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

    private double prevTime = 0;
    private double prevPos = 0;

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



      // Mutable holder for unit-safe voltage values, persisted to avoid reallocation.
  private final MutableMeasure<Voltage> m_appliedVoltage = mutable(Volts.of(0));
  // Mutable holder for unit-safe linear distance values, persisted to avoid reallocation.
  private final MutableMeasure<Angle> m_distance = mutable(Degree.of(0));
  // Mutable holder for unit-safe linear velocity values, persisted to avoid reallocation.
  private final MutableMeasure<Velocity<Angle>> m_velocity = mutable(DegreesPerSecond.of(0));

  private final Measure<Velocity<Voltage>> rampRate = Volts.per(Second).of(.2);
  private final Measure<Voltage> stepVoltage = Volts.of(4);
  private final Measure<Time> timeout = Second.of(10);

    private final SysIdRoutine m_sysIdRoutine =
    new SysIdRoutine(
        // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
        new SysIdRoutine.Config(rampRate, stepVoltage, timeout),
        new SysIdRoutine.Mechanism(
            // Tell SysId how to plumb the driving voltage to the motors.
            (Measure<Voltage> volts) -> {
              leftRotationMotor.set(ControlMode.PercentOutput, -volts.in(Volts));
              rightRotationMotor.set(ControlMode.PercentOutput, -volts.in(Volts));
            },
            // Tell SysId how to record a frame of data for each motor on the mechanism being
            // characterized.
            log -> {
                if (prevTime == 0 && prevPos == 0){
                    prevPos = getLeftEncoderValue();
                    prevTime = Timer.getFPGATimestamp();
                    return;
                }
                double time = Timer.getFPGATimestamp();
                double pos = getLeftEncoderValue();

                double vel = (pos - prevPos)/(time - prevTime);

                prevPos = pos;
                prevTime = time;
              // Record a frame for the left motors.  Since these share an encoder, we consider
              // the entire group to be one motor.
              log.motor("rotate-left")
                  .voltage(
                      m_appliedVoltage.mut_replace(
                          leftRotationMotor.getMotorOutputPercent() * RobotController.getBatteryVoltage(), Volts))
                  .angularPosition(m_distance.mut_replace(getLeftEncoderValue(), Degree))
                  .angularVelocity(m_velocity.mut_replace(vel, DegreesPerSecond));
              // Record a frame for the right motors.  Since these share an encoder, we consider
              // the entire group to be one motor.
            //   log.motor("rotate-right")
            //       .voltage(
            //           m_appliedVoltage.mut_replace(
            //               rightRotationMotor.getMotorOutputPercent() * RobotController.getBatteryVoltage(), Volts))
            //       .angularPosition(m_distance.mut_replace(launcherRightPitchEncoder.getDistance(), Degree))
            //       .angularVelocity(m_velocity.mut_replace(vel, DegreesPerSecond));
            },
            // Tell SysId to make generated commands require this subsystem, suffix test state in
            // WPILog with this subsystem's name ("drive")
            this));

    /**
     * Returns a command that will execute a quasistatic test in the given direction.
     *
     * @param direction The direction (forward or reverse) to run the test in
     */
    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        System.out.println("SysIdQuas");
        return m_sysIdRoutine.quasistatic(direction);
    }
    
    /**
     * Returns a command that will execute a dynamic test in the given direction.
     *
     * @param direction The direction (forward or reverse) to run the test in
     */
    public Command sysIdDynamic(SysIdRoutine.Direction direction) {
        System.out.println("SysIdDynam");
        return m_sysIdRoutine.dynamic(direction);
    }
}