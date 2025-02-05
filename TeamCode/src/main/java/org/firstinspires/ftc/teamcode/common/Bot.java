package org.firstinspires.ftc.teamcode.common;

import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BotState;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Claw;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Intake;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;

public class Bot extends Robot {
    private final IMU imu;
    public final Telemetry telem;
    public final HardwareMap hMap;
    public final Gamepad gamepad;

    public BotState state = BotState.DEPOSIT;
    private final Pivot pivot;
    private final Extension extension;

    private final Intake intake;

    private final Claw claw;

    private MecanumDrivetrain drivetrain;

    private Modes mode = Modes.SAMPLES;

    public boolean climbing = false;

    public double speed = 0.7;

    private Levels level = Levels.Up;
    public enum Modes {
        SPECIMENS,
        SAMPLES,
    }

    public enum Levels {
        Up,
        Down
    }

    public Bot(Telemetry telem, HardwareMap hMap, Gamepad gamepad, boolean enableDrive) {
        this.telem = telem;
        this.hMap = hMap;
        this.gamepad = gamepad;

        // TODO: Adjust IMU parameters to match hub orientation
        imu = hMap.get(IMU.class, "imu");
        imu.initialize(
            new IMU.Parameters(
                new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.UP,
                    RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                )
            )
        );

        /* Subsystems */
        if (enableDrive) {
            drivetrain = new MecanumDrivetrain(this);
        }

        pivot = new Pivot(this);
        extension = new Extension(this);
        intake = new Intake(this);
        claw = new Claw(this);
    }

    /**
     * Get the IMU object for the robot
     * @return the IMU object
     */
    public IMU getImu() { return imu; }

    /**
     * Get the MecanumDrivetrain subsystem of the robot
     * @return the mecanum subsystem of the robot
     */
    public MecanumDrivetrain getDrivetrain() { return drivetrain; }

    /**
     * Set the pose estimate of the robot
     */
    public void setPoseEstimateDEG(Pose2d pose) {
        drivetrain.setOdoPositionDEG(pose);
    }

    public Pivot getPivot() { return pivot; }
    public Extension getExtension() { return extension; }
    public Intake getIntake() { return intake; }
    public Claw getClaw() { return claw; }
    public BotState getState() { return state; }

    public void setState(BotState state) { this.state = state; }

    public Modes getMode() { return mode; }

    public void toggleMode() {
        mode = mode == Modes.SPECIMENS ? Modes.SAMPLES : Modes.SPECIMENS;
    }

    public Levels getLevel() { return level; }

    public void setLevel(Bot.Levels level) {
        this.level = level;
    }

}
