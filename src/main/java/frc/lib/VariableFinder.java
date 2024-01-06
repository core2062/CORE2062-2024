package frc.lib;

import java.lang.reflect.Field;
import java.util.Scanner;

public class VariableFinder {
    public static void main(String name) {

        // Get user input for class name and variable name
        String className = "Constants";

        String variableName = name;

        try {
            // Load the class dynamically
            Class<?> clazz = Class.forName(className);

            // Get the declared fields of the class
            Field[] fields = clazz.getDeclaredFields();

            // Search for the variable in the fields
            boolean variableFound = false;
            for (Field field : fields) {
                if (field.getName().equals(variableName)) {
                    System.out.println("Variable found in class " + className + ": " + field);
                    variableFound = true;
                    break;  // No need to continue searching once found
                }
            }

            if (!variableFound) {
                System.out.println("Variable not found in the class " + className);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
        }
    }
}
