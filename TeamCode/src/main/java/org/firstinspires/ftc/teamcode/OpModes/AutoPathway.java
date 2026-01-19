package org.firstinspires.ftc.teamcode.OpModes;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.OpModes.Auto.AutoBase;

import java.time.Duration;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;

@Autonomous
public class AutoPathway extends AutoBase {
        PathChain DriveToBalls, PickUpBalls, DriveToScore, DriveToBalls2, PickUpBalls2, DriveToScore2, DriveToBalls3,PickUpBalls3, DriveToScore3, DriveAndPickUp, DriveToScore4;
        Command runAuto;

        Command setStartingPoint, setDriveDown, setCollection, setDriveUp, setDriveDown2, setCollection2, setDriveUp2,setDriveDown3, setCollection3, setDriveUp4,
                setDriveDown4, setCollection4, setDriveUp5, setEndingPoint;

        Pose StartingPoint = new Pose (22.460, 120.248, Math.toRadians(180));
        Pose DriveDown = new Pose (34.770, 83.963);
        Pose Collection1 = new Pose (20, 83.404);
        Pose DriveUp = new Pose (39.801, 93.689)
        Pose DriveDown2 = new Pose (55.366, 56.891);
        Pose Collection2 = new Pose (33.540, 59.174);
        Pose DriveUp2 = new Pose (11.627, 58.807);
        Pose DriveDown3 = new Pose (24.373, 77.503);
        Pose Collection3 = new Pose (49.193, 84.522);
        Pose DriveUp3 = new Pose (49.065, 33.615);
        Pose DriveDown4 = new Pose (33.547, 35.528);
        Pose DriveCollection4 = new Pose (11.851, 34.882);
        Pose DriveUp4 = new Pose (28.932, 60.286);
        Pose DriveDown5 = new Pose(54.335, 69.540);
        Pose Collection5 = new Pose (3.584, 47.528);
        Pose DriveUp5 = new Pose (2.683, 2.236);
        Pose EndingPoint = new Pose (52.099, 81.168);


    public void buildPaths () {
        Follower follower = PedroComponent.follower();
        DriveToBalls = follower.pathBuilder().addPath(
                        new BezierLine(
                               StartingPoint,

                                DriveDown
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

         PickUpBalls = follower.pathBuilder().addPath(
                        new BezierLine(
                                DriveDown,

                                Collection1
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

        DriveToScore = follower.pathBuilder().addPath(
                        new BezierLine(
                                Collection1,

                                new Pose(39.801, 93.689)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

       DriveToBalls2 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(39.801, 93.689),
                                new Pose(55.366, 56.891),
                                new Pose(33.540, 59.174)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

       PickUpBalls2 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(33.540, 59.174),

                                new Pose(24, 58.807)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

       DriveToScore2 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(11.627, 58.807),
                                new Pose(24.373, 77.503),
                                new Pose(49.193, 84.522)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

       DriveToBalls3= follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(49.193, 84.522),
                                new Pose(49.065, 33.615),
                                new Pose(33.547, 35.528)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

       PickUpBalls3 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(33.547, 35.528),

                                new Pose(20, 34.882)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

       DriveToScore3 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(11.851, 34.882),
                                new Pose(28.932, 60.286),
                                new Pose(54.335, 69.540)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

       DriveAndPickUp = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(54.335, 69.540),
                                new Pose(3.584, 47.528),
                                new Pose(2.683, 2.236)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(270))

                .build();

       DriveToScore4 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(2.683, 2.236),

                                new Pose(52.099, 81.168)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();
    }
    @Override
    public void onInit() {
        PedroComponent.follower().setPose(StartingPoint);
        buildPaths();
         runAuto = new SequentialGroup(
                 new FollowPath(DriveToBalls),
                 new Delay(1),
                 new FollowPath(PickUpBalls),
                 new Delay(1),
                new FollowPath(DriveToScore),
              new Delay(1),
                 new FollowPath (DriveToBalls2),
                new Delay(1),
               new FollowPath (PickUpBalls2),
                 new Delay(1),
                new FollowPath (DriveToScore2),
                new Delay(1),
                new FollowPath (DriveToBalls3),
                new Delay(1),
                new FollowPath (PickUpBalls3),
                new Delay(1),
               new FollowPath (DriveToScore3),
               new Delay(1),
               new FollowPath (DriveAndPickUp),
               new Delay(1),
               new FollowPath (DriveToScore4)

        );
    }
    @Override public void onWaitForStart () {}
    @Override public void onStartButtonPressed (){
       runAuto.schedule();
    }
    @Override public void onUpdate () {}
    @Override public void onStop () {}




}






