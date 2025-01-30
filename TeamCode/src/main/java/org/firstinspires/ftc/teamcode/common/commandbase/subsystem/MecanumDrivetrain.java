package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.pedropathing.localization.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.common.Bot;

@Config
public class MecanumDrivetrain extends SubsystemBase {
    private final Bot bot;

    private final DcMotorEx frontLeft, frontRight, backLeft, backRight;
    private final PIDFController ascentController;
    private GoBildaPinpointDriver odo;
    public static boolean fieldCentric = false, headingLock = false;

    public static Pose2D pose;

    private boolean isEncoderMode = false;

    private static final double TICKS_PER_CM = 384.5 / 11.2;
    private double setPointCM = 0.0;
    private static final double hangCM = 45.0;

    public MecanumDrivetrain(Bot bot) {
        this.bot = bot;

        odo = bot.hMap.get(GoBildaPinpointDriver.class,"odo");
        odo.setOffsets(-82.66924000028, 110.830759999962);
        odo.setEncoderResolution(8192 / (Math.PI * 35));
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);

        if (pose == null) {
            pose = new Pose2D(DistanceUnit.MM, 0, 0, AngleUnit.RADIANS, 0);
        }

        odo.setPosition(pose);

        frontLeft = bot.hMap.get(DcMotorEx.class, "frontLeft");
        frontRight = bot.hMap.get(DcMotorEx.class, "frontRight");
        backLeft = bot.hMap.get(DcMotorEx.class, "backLeft");
        backRight = bot.hMap.get(DcMotorEx.class, "backRight");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        ascentController = new PIDFController(
                org.firstinspires.ftc.teamcode.common.Config.ascent_kP,
                org.firstinspires.ftc.teamcode.common.Config.ascent_kI,
                org.firstinspires.ftc.teamcode.common.Config.ascent_kD,
                org.firstinspires.ftc.teamcode.common.Config.ascent_kF
        );
    }

    @Override
    public void periodic() {
        if (isEncoderMode) {
            double targetTicks = setPointCM * TICKS_PER_CM;

            double leftPower = ascentController.calculate(backLeft.getCurrentPosition(), targetTicks);
            double rightPower = ascentController.calculate(backRight.getCurrentPosition(), targetTicks);

            backLeft.setPower(leftPower);
            backRight.setPower(rightPower);
            frontLeft.setPower(leftPower);
            frontRight.setPower(rightPower);

            // Debugging telemetry
            //bot.telem.addData("BAD BAD BAD BAD BAD FUNNY", targetTicks);
        }

        odo.update();
        pose = odo.getPosition();
        //bot.telem.addData("Pose",
        //        "X: " + pose.getX(DistanceUnit.MM) +
        //                ", Y: " + pose.getY(DistanceUnit.MM) +
        //                ", Heading: " + pose.getHeading(AngleUnit.DEGREES));
    }


    public void teleopDrive(Vector2d leftStick, double rx, double multiplier) {
        if (!isEncoderMode) {
            double x = leftStick.getX() * multiplier;
            double y = -leftStick.getY() * multiplier;

            //double extensionPosition = bot.getExtension().getPositionCM();

            double extensionThreshold = 20;
            double rotationSpeedMultiplier = 1.0;

            //if (extensionPosition > extensionThreshold) {
            //    rotationSpeedMultiplier = 0.5;
            //}

            rx *= rotationSpeedMultiplier;

            if (!fieldCentric) {
                y *= 1.1; // counteract imperfect strafe
                double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                double frontLeftPower = (y + x + rx) / denominator;
                double frontRightPower = (y - x - rx) / denominator;
                double backLeftPower = (y - x + rx) / denominator;
                double backRightPower = (y + x - rx) / denominator;

                double[] powers = {frontLeftPower, frontRightPower, backLeftPower, backRightPower};
                double[] normalizedPowers = normalizeWheelSpeeds(powers);

                frontLeft.setPower(normalizedPowers[0]);
                frontRight.setPower(normalizedPowers[1]);
                backLeft.setPower(normalizedPowers[2]);
                backRight.setPower(normalizedPowers[3]);

                return;
            }

            double botHeading = pose.getHeading(AngleUnit.RADIANS);

            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX *= 1.1; // counteract imperfect strafe

            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            double[] powers = {frontLeftPower, frontRightPower, backLeftPower, backRightPower};
            double[] normalizedPowers = normalizeWheelSpeeds(powers);

            frontLeft.setPower(normalizedPowers[0]);
            frontRight.setPower(normalizedPowers[1]);
            backLeft.setPower(normalizedPowers[2]);
            backRight.setPower(normalizedPowers[3]);
        }
    }

    public void resetEncoders() {
        backLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        backLeft.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }


    public void winchArms() {
        resetEncoders();
        setPointCM = hangCM;
    }

    public void toggleDriveMode(boolean mode) {
        resetEncoders();
        isEncoderMode = mode;
    }

    // TODO: Try to implement heading lock
    public void toggleHeadingLock() {
        headingLock = !headingLock;
    }

    public void toggleFieldCentric() {
        fieldCentric = !fieldCentric;
    }

    private double[] normalizeWheelSpeeds(double[] speeds) {
        if (largestAbsolute(speeds) > 1) {
            double max = largestAbsolute(speeds);
            for (int i = 0; i < speeds.length; i++){
                speeds[i] /= max;
            }
        }
        return speeds;
    }

    private double largestAbsolute(double[] arr) {
        double largestAbsolute = 0;
        for (double d : arr) {
            double absoluteValue = Math.abs(d);
            if (absoluteValue > largestAbsolute) {
                largestAbsolute = absoluteValue;
            }
        }
        return largestAbsolute;
    }

    public double getHeadingDEG() {
        return pose.getHeading(AngleUnit.DEGREES);
    }

    public Pose2d getOdoPositionDEG() {
        return new Pose2d(
                pose.getX(DistanceUnit.CM),
                pose.getY(DistanceUnit.CM),
                new Rotation2d(pose.getHeading(AngleUnit.DEGREES))
        );
    }

    public Pose2d getOdoPositionRAD() {
        return new Pose2d(
                pose.getX(DistanceUnit.CM),
                pose.getY(DistanceUnit.CM),
                new Rotation2d(pose.getHeading(AngleUnit.RADIANS))
        );
    }

    public void setOdoPositionDEG(Pose2d pose) {
        Pose2D pose2D = new Pose2D(
                DistanceUnit.CM,
                pose.getX(),
                pose.getY(),
                AngleUnit.DEGREES,
                pose.getHeading()
        );

        odo.setPosition(pose2D);
    }
}
