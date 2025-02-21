package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.Bot;


@com.acmerobotics.dashboard.config.Config
public class ClawServo extends SubsystemBase {
    private final Bot bot;
    public final Servo clawPivot;
    private double currentPosition = ServoPositions.placing;
    @com.acmerobotics.dashboard.config.Config
    public static class ServoPositions {
        public static double placing = 0.45, safe = 0, intaking = 0.75, pickSpecimen = 0.2, specimen = 0.7, safe2 = 0.25;
    }
    public ClawServo(Bot bot) {
        this.bot = bot;
        clawPivot = bot.hMap.get(Servo.class, "clawServo");
    }
    @Override
    public void periodic() {
    }

    public void setPosition(double position){
        clawPivot.setPosition(position);
    }

    //public double getPositionRAD() {
    //    return ((clawEncoder.getCurrentPosition() / 8192.0) * 2 * Math.PI);
    //}

}