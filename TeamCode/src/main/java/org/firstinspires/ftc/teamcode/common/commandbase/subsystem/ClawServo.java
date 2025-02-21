package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import static org.firstinspires.ftc.teamcode.common.commandbase.subsystem.ClawServo.ServoPositions.intaking;
import static org.firstinspires.ftc.teamcode.common.commandbase.subsystem.ClawServo.ServoPositions.placing;
import static org.firstinspires.ftc.teamcode.common.commandbase.subsystem.ClawServo.ServoPositions.safe;
import static org.firstinspires.ftc.teamcode.common.commandbase.subsystem.ClawServo.ServoPositions.safe2;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.Bot;


@com.acmerobotics.dashboard.config.Config
public class ClawServo extends SubsystemBase {
    private final Bot bot;
    public final Servo clawPivot;
    private double currentPosition = placing;
    @com.acmerobotics.dashboard.config.Config
    public static class ServoPositions {
        public static double placing = 0.4, safe = 0, intaking = 0.75, safe2= 0;
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
    public void placing() {
        safe = placing;
    }
    public void intaking() {
        safe = intaking;
    }
    public void safe() {
        safe = safe2;
    }


    //public double getPositionRAD() {
    //    return ((clawEncoder.getCurrentPosition() / 8192.0) * 2 * Math.PI);
    //}

}