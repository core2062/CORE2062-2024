package frc.lib;

import edu.wpi.first.wpilibj.Preferences;

interface ICOREConstant {
    String GetName();
    void UpdateConstant();
}

public class COREConstants<T> implements ICOREConstant {
    private String m_name;
    private T m_value;

    public COREConstants(String name) {
        m_name = name;
        if (!Preferences.containsKey(m_name)) {
            Preferences.initString(m_name, "INVALID TYPE SPECIFIED FOR: " + name);
        }
        UpdateConstant();
        COREConstantsManager.AddConstant(this);
    }

    public COREConstants(String name, T defaultValue) {
        m_name = name;
        Preferences.initString(m_name, "INVALID TYPE SPECIFIED FOR: " + name);
        m_value = defaultValue;
        COREConstantsManager.AddConstant(this);
    }

    public T Get() {
        return m_value;
    }

    public void Set(T value) {
        m_value = value;
        Preferences.initString(m_name, m_value.toString());
    }

    public String GetName() {
        return m_name;
    }

    public void UpdateConstant() {
        m_value = null;
    }
}

// class COREConstantDouble implements ICOREConstant {
//     private String m_name;
//     private double m_value;

//     public COREConstantDouble(String name) {
//         m_name = name;
//         if (!Preferences.containsKey(m_name)) {
//             Preferences.initDouble(m_name, 0);
//         }
//         UpdateConstant();
//         COREConstantsManager.AddConstant(this);
//     }

//     public COREConstantDouble(String name, double defaultValue) {
//         m_name = name;
//         Preferences.initDouble(m_name, defaultValue);
//         m_value = defaultValue;
//         COREConstantsManager.AddConstant(this);
//     }

//     public double Get() {
//         return m_value;
//     }

//     public void Set(double value) {
//         m_value = value;
//         Preferences.initDouble(m_name, m_value);
//     }

//     public String GetName() {
//         return m_name;
//     }

//     public void UpdateConstant() {
//         m_value = Preferences.getDouble(m_name, 0);
//     }
// }

// class COREConstantString implements ICOREConstant {
//     private String m_name;
//     private String m_value;

//     public COREConstantString(String name) {
//         m_name = name;
//         if (!Preferences.containsKey(m_name)) {
//             Preferences.initString(m_name, "null");
//         }
//         UpdateConstant();
//         COREConstantsManager.AddConstant(this);
//     }

//     public COREConstantString(String name, String defaultValue) {
//         m_name = name;
//         Preferences.initString(m_name, defaultValue);
//         m_value = defaultValue;
//         COREConstantsManager.AddConstant(this);
//     }

//     public String Get() {
//         return m_value;
//     }

//     public void Set(String value) {
//         m_value = value;
//         Preferences.initString(m_name, m_value);
//     }

//     public String GetName() {
//         return m_name;
//     }

//     public void UpdateConstant() {
//         m_value = Preferences.getString(m_name, "null");
//     }
// }

// class COREConstantBoolean implements ICOREConstant {
//     private String m_name;
//     private boolean m_value;

//     public COREConstantBoolean(String name) {
//         m_name = name;
//         if (!Preferences.containsKey(m_name)) {
//             Preferences.initBoolean(m_name, false);
//         }
//         UpdateConstant();
//         COREConstantsManager.AddConstant(this);
//     }

//     public COREConstantBoolean(String name, boolean defaultValue) {
//         m_name = name;
//         Preferences.initBoolean(m_name, defaultValue);
//         m_value = defaultValue;
//         COREConstantsManager.AddConstant(this);
//     }

//     public boolean Get() {
//         return m_value;
//     }

//     public void Set(boolean value) {
//         m_value = value;
//         Preferences.initBoolean(m_name, m_value);
//     }

//     public String GetName() {
//         return m_name;
//     }

//     public void UpdateConstant() {
//         m_value = Preferences.getBoolean(m_name, false);
//     }
// }

// class COREConstantInt implements ICOREConstant {
//     private String m_name;
//     private int m_value;

//     public COREConstantInt(String name) {
//         m_name = name;
//         if (!Preferences.containsKey(m_name)) {
//             Preferences.initInt(m_name, 0);
//         }
//         UpdateConstant();
//         COREConstantsManager.AddConstant(this);
//     }

//     public COREConstantInt(String name, int defaultValue) {
//         m_name = name;
//         Preferences.initInt(m_name, defaultValue);
//         m_value = defaultValue;
//         COREConstantsManager.AddConstant(this);
//     }

//     public int Get() {
//         return m_value;
//     }

//     public void Set(int value) {
//         m_value = value;
//         Preferences.initInt(m_name, m_value);
//     }

//     public String GetName() {
//         return m_name;
//     }

//     public void UpdateConstant() {
//         m_value = Preferences.getInt(m_name, 0);
//     }
// }