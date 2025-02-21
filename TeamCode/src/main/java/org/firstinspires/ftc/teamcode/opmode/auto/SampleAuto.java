package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
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
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.FollowPathCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.ClawPID;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.ClawServo;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Intake;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.command.button.Button;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.BotState;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.SetClawCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.TeleOpDriveCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.ManualExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.ManualIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeInCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeOutCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeStopCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.ManualPivotCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.SetBotStateCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.ClawServo;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Intake;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;
import org.firstinspires.ftc.teamcode.Direction;


import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;



@Autonomous
public class SampleAuto extends LinearOpMode {

    private Bot bot;
    private Pivot pivot;
    private Extension extension;
    private Intake intake;
    private ClawServo claw;

    public static Pose startingPose = new Pose(10.2, 105, 0);

    public static Pose startingPoseIntermediate = new Pose(34, 105, 0);


    public static Pose score1 = new Pose( 12.551029235359252, 137.34585499200296, Math.toRadians(315));

    public static Pose score2 = new Pose(20, 133.1034679809761, Math.toRadians(345.599123));

    public static Pose score3 = new Pose(20, 133.1034679809761, Math.toRadians(0));

    public static Pose score4 = new Pose(20, 133.1034679809761, Math.toRadians(13));



    @Override
    public void runOpMode() {

        Telemetry telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        VoltageSensor vs = hardwareMap.voltageSensor.iterator().next();

        Constants.setConstants(FConstants.class, LConstants.class);
        Follower f = new Follower(hardwareMap);

        f.setPose(startingPose);
        f.setMaxPower(0.75);

        intake = bot.getIntake();
        claw = bot.getClaw();
        pivot = bot.getPivot();
        extension = bot.getExtension();



        SequentialCommandGroup auto = new SequentialCommandGroup(
                //Poner el precargado
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(startingPose),
                                                new Point(startingPoseIntermediate)
                                        )
                                )
                                .setLinearHeadingInterpolation(startingPose.getHeading(), startingPoseIntermediate.getHeading())
                                .build()
                        )

                ),

                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(startingPoseIntermediate),
                                                new Point(score1)
                                        )
                                )
                                .setLinearHeadingInterpolation(
                                        startingPoseIntermediate.getHeading(), score1.getHeading())
                                .build()
                        )

                ),
                new SequentialCommandGroup(
                        new WaitCommand (300),
                        new InstantCommand(pivot::setSetpointVertical),
                        new WaitCommand (300),
                        new InstantCommand(extension::getHighBasketTarget),
                        new WaitCommand(300),
                        new InstantCommand(claw::placing),
                        new WaitCommand(500),
                        new InstantCommand(intake::out)

                ),


                new WaitCommand(500),

                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(score1),
                                                new Point(score2)
                                        )
                                )
                                .setLinearHeadingInterpolation(score1.getHeading(), score2.getHeading())
                                .build()
                        )

                ),
                new SequentialCommandGroup(
                        new WaitCommand (300),
                        new InstantCommand(pivot::setSetpointIntaking),
                        new WaitCommand (300),
                        new InstantCommand(extension::maxExtension),
                        new WaitCommand(300),
                        new InstantCommand(claw::intaking),
                        new WaitCommand(500),
                        new InstantCommand(intake::in)

                ),
                new WaitCommand(1000),

                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(score2),
                                                new Point(score1)
                                        )
                                )
                                .setLinearHeadingInterpolation(
                                        score2.getHeading(), score1.getHeading())
                                .build()
                        )

                ),
                new SequentialCommandGroup(
                        new WaitCommand (300),
                        new InstantCommand(pivot::setSetpointVertical),
                        new WaitCommand (300),
                        new InstantCommand(extension::getHighBasketTarget),
                        new WaitCommand(300),
                        new InstantCommand(claw::placing),
                        new WaitCommand(500),
                        new InstantCommand(intake::out)

                ),
                new WaitCommand(1000),

                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(score1),
                                                new Point(score3)
                                        )
                                )
                                .setLinearHeadingInterpolation(
                                        score1.getHeading(), score3.getHeading())
                                .build()
                        )

                ),
                new SequentialCommandGroup(
                        new WaitCommand (300),
                        new InstantCommand(pivot::setSetpointIntaking),
                        new WaitCommand (300),
                        new InstantCommand(extension::maxExtension),
                        new WaitCommand(300),
                        new InstantCommand(claw::intaking),
                        new WaitCommand(500),
                        new InstantCommand(intake::in)

                ),
                new WaitCommand(1000),

                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(score3),
                                                new Point(score1)
                                        )
                                )
                                .setLinearHeadingInterpolation(
                                        score3.getHeading(), score1.getHeading())
                                .build()
                        )


                ),
                new SequentialCommandGroup(
                        new WaitCommand (300),
                        new InstantCommand(pivot::setSetpointVertical),
                        new WaitCommand (300),
                        new InstantCommand(extension::getHighBasketTarget),
                        new WaitCommand(300),
                        new InstantCommand(claw::placing),
                        new WaitCommand(500),
                        new InstantCommand(intake::out)

                ),
                new WaitCommand(1000),

                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(score1),
                                                new Point(score4)
                                        )
                                )
                                .setLinearHeadingInterpolation(
                                        score1.getHeading(), score4.getHeading())
                                .build()
                        )


                ),
                new SequentialCommandGroup(
                        new WaitCommand (300),
                        new InstantCommand(pivot::setSetpointIntaking),
                        new WaitCommand (300),
                        new InstantCommand(extension::maxExtension),
                        new WaitCommand(300),
                        new InstantCommand(claw::intaking),
                        new WaitCommand(500),
                        new InstantCommand(intake::in)

                ),
                new WaitCommand(1000),

                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(score4),
                                                new Point(score1)
                                        )
                                )
                                .setLinearHeadingInterpolation(
                                        score4.getHeading(), score1.getHeading())
                                .build()
                        )
                ),
                new SequentialCommandGroup(
                        new WaitCommand (300),
                        new InstantCommand(pivot::setSetpointVertical),
                        new WaitCommand (300),
                        new InstantCommand(extension::getHighBasketTarget),
                        new WaitCommand(300),
                        new InstantCommand(claw::placing),
                        new WaitCommand(500),
                        new InstantCommand(intake::out)

                ),
                new WaitCommand(1000),

                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(score1),
                                                new Point(score3)
                                        )
                                )
                                .setLinearHeadingInterpolation(
                                        score1.getHeading(), score3.getHeading())
                                .build()
                        )
                ),
                new SequentialCommandGroup(
                        new WaitCommand (300),
                        new InstantCommand(pivot::setSetpointHorizontal),
                        new WaitCommand (300),
                        new InstantCommand(extension::getMinExtension),
                        new WaitCommand(300),
                        new InstantCommand(claw::safe)
                )
        );


        waitForStart();
        CommandScheduler.getInstance().schedule(auto);

        // Opmode loop
        while (opModeIsActive()) {
            f.setMaxPower(10.0 / vs.getVoltage());
            CommandScheduler.getInstance().run();
            f.update();
            f.telemetryDebug(telem);
            intake.periodic();
            claw.periodic();
            pivot.periodic();
            extension.periodic();
        }
    }
}