package org.firstinspires.ftc.teamcode.common;

@com.acmerobotics.dashboard.config.Config
public class Config {
    public static double pivot_kP = 0.03, pivot_kI = 0.0, pivot_kD = 0.001, pivot_min_kF = -0.002, pivot_max_kF = -0.004, pivot_tolerance = 5.0;
    public static double ascent_kP = 0.01, ascent_kI = 0.0, ascent_kD = 0.0, ascent_kF = 0.0, ascent_tolerance = 10.0;

    public static double ext_increment = 2.0, pivot_increment = 10.0;
    public static double extension_kP = 0.01, extension_kI = 0.0, extension_kD = 0.0, extension_kF = 0.0003, extension_tolerance = 5.0;

}