// package frc.robot.commands;

// import edu.wpi.first.wpilibj2.command.InstantCommand;
// import frc.robot.CANdleSystems;

// public class CANdleConfigCommands {
//     static public class ConfigBrightness extends InstantCommand {
//         public ConfigBrightness(CANdleSystems candleSystem, double brightnessPercent) {
//             super(() -> candleSystem.configBrightness(brightnessPercent), candleSystem);
//         }
//         @Override
//         public boolean runsWhenDisabled() {
//             return true;
//         }
//     }
//     static public class ConfigLosBehavior extends InstantCommand {
//         public ConfigLosBehavior(CANdleSystems candleSystem, boolean disableWhenLos) {
//             super(() -> candleSystem.configLos(disableWhenLos), candleSystem);
//         }
//         @Override
//         public boolean runsWhenDisabled() {
//             return true;
//         }
//     }
//     static public class ConfigStatusLedBehavior extends InstantCommand {
//         public ConfigStatusLedBehavior(CANdleSystems candleSystem, boolean disableWhile) {
//             super(() -> candleSystem.configStatusLedBehavior(disableWhile), candleSystem);
//         }
//         @Override
//         public boolean runsWhenDisabled() {
//             return true;
//         }
//     }
// }