package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.SetClawCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.FollowPathCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeInCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeOutCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeStopCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.ClawServo;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Intake;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@Autonomous
public class AutoGPT extends LinearOpMode {

    //region Poses
    public static Pose startingPose = new Pose(10.2, 105, 0);
    public static Pose startingPoseIntermediate = new Pose(34, 105, 0);
    public static Pose score1 = new Pose(14.843, 133.809, Math.toRadians(315));
    public static Pose score2 = new Pose(20, 133.103, Math.toRadians(345.6));
    public static Pose score2intake = new Pose(27.471, 128.714, Math.toRadians(345.6));
    public static Pose score3 = new Pose(20, 133.103, Math.toRadians(0));
    public static Pose score3intake = new Pose(27.914, 133.103, Math.toRadians(0));
    public static Pose score4 = new Pose(20, 133.103, Math.toRadians(13));
    public static Pose score4intake = new Pose(20, 138.24, Math.toRadians(13));
    //endregion

    //region Subsystems
    private Bot bot;
    private Pivot pivot;
    private Extension extension;
    private Intake intake;
    private ClawServo claw;
    //endregion

    @Override
    public void runOpMode() {

        // Initialize telemetry
        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry telem = (dashboard != null) ? new MultipleTelemetry(telemetry, dashboard.getTelemetry()) : telemetry;

        GamepadEx driverGamepad = new GamepadEx(gamepad1);
        GamepadEx operatorGamepad = new GamepadEx(gamepad2);

        // Get voltage sensor safely
        VoltageSensor vs = hardwareMap.voltageSensor.iterator().hasNext() ? hardwareMap.voltageSensor.iterator().next() : null;
        if (vs == null) {
            telemetry.addLine("Warning: No voltage sensor found!");
            telemetry.update();
        }

        Constants.setConstants(FConstants.class, LConstants.class);
        Follower f = new Follower(hardwareMap);
        f.setPose(startingPose);
        f.setMaxPower(0.75);

        bot = new Bot(telem, hardwareMap, driverGamepad, operatorGamepad, false);
        pivot = bot.getPivot();
        extension = bot.getExtension();
        claw = bot.getClaw();
        intake = bot.getIntake();

        // Define auto sequence
        SequentialCommandGroup auto = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(new BezierLine(new Point(startingPose), new Point(startingPoseIntermediate)))
                                .setLinearHeadingInterpolation(startingPose.getHeading(), startingPoseIntermediate.getHeading())
                                .build())
                ),
                new WaitCommand(1000),
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(new BezierLine(new Point(startingPoseIntermediate), new Point(score1)))
                                .setLinearHeadingInterpolation(startingPoseIntermediate.getHeading(), score1.getHeading())
                                .build()),
                        new SequentialCommandGroup(
                                new SetPivotAngleCommand(pivot, claw, Pivot.setpoint_vertical),
                                new SetExtensionCommand(extension, claw, Extension.intakeMaxExtension),
                                new SetClawCommand(claw, ClawServo.ServoPositions.placing),
                                new IntakeOutCommand(intake)
                        )
                ),
                new WaitCommand(1000)
        );

        // Wait for start
        waitForStart();

        // Schedule autonomous commands
        if (auto != null) {
            CommandScheduler.getInstance().schedule(auto);
        } else {
            telemetry.addLine("Error: Auto sequence is null");
            telemetry.update();
        }

        // Opmode loop
        while (opModeIsActive()) {
            if (vs != null) {
                f.setMaxPower(10.0 / vs.getVoltage());
            }
            CommandScheduler.getInstance().run();
            f.update();
            f.telemetryDebug(telem);
        }
    }
}