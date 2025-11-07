package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(10.16)
            .forwardZeroPowerAcceleration(-34.57)
            .lateralZeroPowerAcceleration(-72.17)
            .useSecondaryTranslationalPIDF(true)
            .useSecondaryHeadingPIDF(true)
            .useSecondaryDrivePIDF(true)
            .centripetalScaling(0.0005)
            .automaticHoldEnd(true)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.5, 0, 0.04, 0.015))
            .headingPIDFCoefficients(new PIDFCoefficients(2.5, 0, 0.125, 0))
            .drivePIDFCoefficients(
                    new FilteredPIDFCoefficients(0.009, 0, 0.0005, 0.05, 0.01)
            )
            .secondaryTranslationalPIDFCoefficients(
                    new PIDFCoefficients(0.3, 0, 0.02, 0)
            )
            .secondaryHeadingPIDFCoefficients(new PIDFCoefficients(2, 0, 0.1, 0))
            .secondaryDrivePIDFCoefficients(
                    new FilteredPIDFCoefficients(0.01, 0, 0.0001, 0.01, 0)
            );

    public static MecanumConstants driveConstants = new MecanumConstants()
            .leftFrontMotorName("frontLeft")
            .leftRearMotorName("backLeft")
            .rightFrontMotorName("frontRight")
            .rightRearMotorName("backRight")
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .useBrakeModeInTeleOp(true)
            .xVelocity(66.12)
            .yVelocity(50.97)
            .motorCachingThreshold(0.01)
            .useVoltageCompensation(true)
            .nominalVoltage(12);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(5.09291339)
            .strafePodX(-3.48866142)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("odometry")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

    public static PathConstraints pathConstraints = new PathConstraints(
            0.995,
            500,
            1.25,
            1
    );

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .build();
    }
}
