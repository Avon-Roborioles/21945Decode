package org.firstinspires.ftc.teamcode.Commands.Drive;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;

import dev.nextftc.core.commands.Command;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;
@Configurable
public class TeleOpRampDriveCommand extends Command {
    Pose RampPose;
    public static Pose RedRampPose = new Pose(14, 63, Math.toRadians(167.5)).mirror();
    public static Pose BlueRampPose = new Pose(14, 63, Math.toRadians(167.5));
    Pose PreRampPose;
    public static Pose BluePreRampPose = new Pose(30, 63, Math.toRadians(167.5));
    public static Pose RedPreRampPose = new Pose(30, 63, Math.toRadians(167.5)).mirror();

    Path RampPath;



    public TeleOpRampDriveCommand(Boolean redAlliance){
        RampPose = redAlliance ? RedRampPose : BlueRampPose;
        PreRampPose = redAlliance ? RedPreRampPose : BluePreRampPose;

        addRequirements(PedroComponent.follower());
        setInterruptible(true);
    }
    @Override
    public boolean isDone() {
        return false; // whether or not the command is done
    }

    @Override
    public void start() {
        PedroComponent.follower().breakFollowing();
        RampPath = new Path(new BezierCurve(PedroComponent.follower().getPose(), PreRampPose, RampPose));
        RampPath.setHeadingInterpolation(
                HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(
                                0,
                                .3,
                                HeadingInterpolator.linear(PedroComponent.follower().getHeading(), RampPose.getHeading())
                        ),
                        new HeadingInterpolator.PiecewiseNode(
                                .3,
                                1,
                                HeadingInterpolator.constant(RampPose.getHeading())
                        )
                ));
        PedroComponent.follower().followPath(RampPath);



        // executed when the command begins
    }

    @Override
    public void update() {
        ActiveOpMode.telemetry().addLine("rampDrive");

//        -

        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {
        PedroComponent.follower().breakFollowing();
        PedroComponent.follower().startTeleOpDrive();


        // executed when the command ends
    }
}
