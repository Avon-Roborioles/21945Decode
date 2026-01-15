package org.firstinspires.ftc.teamcode.OpModes;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import dev.nextftc.core.commands.Command;

@Autonomous
public class AutoPathway {

        Command setStartingPoint, setDriveDown, setCollection, setDriveUp, setDriveDown2, setCollection2, setDriveUp2,setDriveDown3, setCollection3, setDriveUp4,
                setDriveDown4, setCollection4, setDriveUp5, setEndingPoint;

        Pose StartingPoint = new Pose (22.460, 120.248);
        Pose DriveDown = new Pose (34.770, 83.963);
        Pose Collection = new Pose (13.193, 83.404);
        Pose DriveUp = new Pose (39.801, 93.689);
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


    public AutoPathway (Follower follower) {
        PathChain Path1 = follower.pathBuilder().addPath(
                        new BezierLine(
                               new Pose(22.460, 120.248),

                                new Pose(34.770, 83.963)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

        PathChain Path2 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(34.770, 83.963),

                                new Pose(13.193, 83.404)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

        PathChain Path3 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(13.193, 83.404),

                                new Pose(39.801, 93.689)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

       PathChain Path4 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(39.801, 93.689),
                                new Pose(55.366, 56.891),
                                new Pose(33.540, 59.174)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

        PathChain Path5 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(33.540, 59.174),

                                new Pose(11.627, 58.807)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

        PathChain Path6 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(11.627, 58.807),
                                new Pose(24.373, 77.503),
                                new Pose(49.193, 84.522)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

        PathChain Path7 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(49.193, 84.522),
                                new Pose(49.065, 33.615),
                                new Pose(33.547, 35.528)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

        PathChain Path8 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(33.547, 35.528),

                                new Pose(11.851, 34.882)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

        PathChain Path9 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(11.851, 34.882),
                                new Pose(28.932, 60.286),
                                new Pose(54.335, 69.540)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

        PathChain Path10 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(54.335, 69.540),
                                new Pose(3.584, 47.528),
                                new Pose(2.683, 2.236)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(270))

                .build();

        PathChain Path11 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(2.683, 2.236),

                                new Pose(52.099, 81.168)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();
    }
}






