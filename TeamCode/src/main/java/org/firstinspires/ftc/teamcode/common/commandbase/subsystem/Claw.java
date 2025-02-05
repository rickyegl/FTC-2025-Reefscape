package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.Bot;

@com.acmerobotics.dashboard.config.Config
public class Claw extends SubsystemBase {

    private final Bot bot;

    public final Servo clawPivot;
    private double currentPosition = placing;

    public static double placing = 0.4, safe = 0.2, intaking = 0;

    public Claw(Bot bot) {
        this.bot = bot;
        clawPivot = bot.hMap.get(Servo.class, "claw");
    }

    @Override
    public void periodic() {
        clawPivot.setPosition(currentPosition);
    }

    public void setPosition(double position){
        currentPosition = position;
    }

}