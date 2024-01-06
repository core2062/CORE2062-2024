package frc.lib;

import java.util.ArrayList;
import java.util.List;

public class COREConstantsManager {
    private static List<ICOREConstant> m_constants = new ArrayList<>();

    public static void UpdateConstants() {
        for (ICOREConstant constant : m_constants) {
            constant.UpdateConstant();
        }
    }

    public static void AddConstant(ICOREConstant instance) {
        m_constants.add(instance);
    }

    public static void CleanUp() {
        // for (ICOREConstant constant : m_constants) {
        //     constant.delete();
        // }
        m_constants.clear();
    }
}
