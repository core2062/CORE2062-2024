package frc.lib;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Preferences;
import frc.lib.VariableFinder;

public class COREConstant {

    private static String[] PreferencesBooleanConstantName = {};
    private static String[] PreferencesDoubleConstantName = {};
    private static String[] PreferencesIntConstantName = {};
    private static String[] PreferencesFloatConstantName = {};

    private static String[] PreferencesBooleanKeyName = {};
    private static String[] PreferencesDoubleKeyName = {};
    private static String[] PreferencesIntKeyName = {};
    private static String[] PreferencesFloatKeyName = {};

    //smart dashboard
    //adds a boolean to smart dashboard
    public static void addData(String name, boolean state){
        SmartDashboard.putBoolean(name, state);
    }
    
    //adds a double to smart dashboard
    public static void addData(String name, double value){
        SmartDashboard.putNumber(name, value);
    }

    //adds an array of doubles to smart dashboard
    public static void addData(String name, double...args){
        SmartDashboard.putNumberArray(name, args);
    }

    //adds an intiger to smart dashboard
    public static void addData(String name, int value){
        SmartDashboard.putNumber(name, value);
    }

    //updates values
    public static void updateSmartdashboard(){
        SmartDashboard.updateValues();
    }



    //preferences
    //adds a boolean to the preferences data table
    public static void addDataToTable(String name, boolean state, String variable){
        Preferences.initBoolean(name, state);
        PreferencesBooleanConstantName[PreferencesBooleanConstantName.length] = variable;
        PreferencesBooleanKeyName[PreferencesBooleanKeyName.length] = name;
    }

    //adds a double to the preferences data table
    public static void addDataToTable(String name, double value, String variable){
        Preferences.initDouble(name, value);
        PreferencesDoubleConstantName[PreferencesDoubleConstantName.length] = variable;
        PreferencesDoubleKeyName[PreferencesDoubleKeyName.length] = name;
    }

    //adds an intiger to the preferences data table
    public static void addDataToTable(String name, int value, String variable){
        Preferences.initInt(name, value);
        PreferencesIntConstantName[PreferencesIntConstantName.length] = variable;
        PreferencesIntKeyName[PreferencesIntKeyName.length] = name;
    }

    //adds an intiger to the preferences data table
    public static void addDataToTable(String name, float value, String variable){
        Preferences.initFloat(name, value);
        PreferencesFloatConstantName[PreferencesFloatConstantName.length] = variable;
        PreferencesFloatKeyName[PreferencesFloatKeyName.length] = name;
    }
    
    private static int I = 0;
    private static boolean bool;
    private static double doubl;
    private static int in;
    private static float floa;

    //updates variables in data table
    public static void updateData(){
        bool = false;
        if (PreferencesBooleanConstantName.length != 0) {
            while (I < PreferencesBooleanConstantName.length){
                bool = Preferences.getBoolean(PreferencesBooleanKeyName[I], false);
                VariableFinder.main(PreferencesBooleanConstantName[I]);
                I += 1;
            }
        }
        I = 0;

        doubl = 0.0;
        if (PreferencesDoubleConstantName.length != 0) {
            while (I < PreferencesDoubleConstantName.length){
                doubl = Preferences.getDouble(PreferencesDoubleKeyName[I], 0);
                VariableFinder.main(PreferencesDoubleConstantName[I]);
                I += 1;
            }
        }
        I = 0;

        in = 0;
        if (PreferencesIntConstantName.length != 0) {
            while (I < PreferencesIntConstantName.length){
                in = Preferences.getInt(PreferencesIntKeyName[I], 0);
                I += 1;
            }
        }
        I = 0;

        floa = 0.0f;
        if (PreferencesFloatConstantName.length != 0) {
            while (I < PreferencesFloatConstantName.length){
                floa = Preferences.getFloat(PreferencesFloatKeyName[I], 0);
                I += 1;
            }
        }
    }
}



