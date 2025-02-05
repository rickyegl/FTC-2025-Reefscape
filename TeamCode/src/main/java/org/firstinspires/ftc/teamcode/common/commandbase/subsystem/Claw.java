package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.Bot;

@com.acmerobotics.dashboard.config.Config
public class Claw extends SubsystemBase {

    private final Bot bot;

    public final Servo clawPivot;
    private double currentPosition = 0.4;

    public Claw(Bot bot) {
        this.bot = bot;
        clawPivot = bot.hMap.get(Servo.class, "claw");
    }

    @Override
    public void periodic() {
        clawPivot.setPosition(currentPosition);
    }

    public enum ClawState {
        PLACING(0.5);

        public final double pos;

        private ClawState (double pos) {
            this.pos = pos;
        }
    }

}