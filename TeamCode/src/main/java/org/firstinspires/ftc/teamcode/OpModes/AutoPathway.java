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
    Pose DriveDown = new Pose (53, 83.963);
    Pose Collection1 = new Pose (20, 83.404);
    Pose DriveUp = new Pose (39.801, 93.689);
    Pose DriveDown2 = new Pose (50,59);
    Pose DriveDown2CP= new Pose (55,56);
    Pose Collection2 = new Pose (20, 58);
    Pose DriveUp2 = new Pose (53, 84);
    Pose DriveUp2CP = new Pose (20,77);
    Pose DriveDown3 = new Pose (45, 35);
    Pose DriveDown3CP = new Pose (49, 33);
    Pose Collection3 = new Pose (20, 34);
    Pose DriveUp3 = new Pose (54, 84);
    Pose DriveUp3CP = new Pose (28, 60);
    //Pose DriveDown4 = new Pose (12, 2);
    //Pose DriveDown4CP= new Pose (5, 61);
   // Pose DriveUp4 = new Pose (52, 81);



       // Pose DriveDown2 = new Pose (33, 59);
       // Pose Collection2 = new Pose (37.540, 59.174);
       // Pose DriveUpControlPoint = new Pose ( 55, 56);
        //Pose DriveUp2 = new Pose (11.627, 58.807);
       // Pose DriveDown3 = new Pose (24.373, 77.503);
        //Pose Collection3ControlPoint = new Pose (24,77);
       // Pose Collection3 = new Pose (49.193, 84.522);
      //  Pose DriveUp3 = new Pose (49.065, 33.615);
      //  Pose DriveDown4 = new Pose (33.547, 35.528);
      //  Pose DriveCollection4 = new Pose (11.851, 34.882);
     //   Pose DriveUp4 = new Pose (28.932, 60.286);
      //  Pose DriveDown5 = new Pose(54.335, 69.540);
      //  Pose Collection5 = new Pose (3.584, 47.528);
       // Pose DriveUp5 = new Pose (2.683, 2.236);
      //  Pose EndingPoint = new Pose (52.099, 81.168);)


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

                                DriveUp
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

       DriveToBalls2 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                DriveUp,
                                DriveDown2CP,
                                DriveDown2
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

       PickUpBalls2 = follower.pathBuilder().addPath(
                        new BezierLine(
                               DriveDown2,

                                Collection2
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

       DriveToScore2 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                Collection2,
                                DriveUp2CP,
                               DriveUp2
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

       DriveToBalls3= follower.pathBuilder().addPath(
                        new BezierCurve(
                                DriveUp2,
                               DriveDown3CP,
                                DriveDown3
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

       PickUpBalls3 = follower.pathBuilder().addPath(
                        new BezierLine(
                                DriveDown3,

                                Collection3
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

       DriveToScore3 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                Collection3,
                                DriveUp3CP,
                                DriveUp3
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                .build();

//       DriveAndPickUp = follower.pathBuilder().addPath(
//                        new BezierCurve(
//                               DriveUp3,
//                                DriveDown4CP,
//                                DriveDown4
//                        )
//                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(270))
//
//                .build();
//
//       DriveToScore4 = follower.pathBuilder().addPath(
//                        new BezierLine(
//                                DriveDown4,
//
//                                DriveUp4
//                        )
//                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
//
//                .build();
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
               new FollowPath (DriveToScore3)

        );
    }
    @Override public void onWaitForStart () {}
    @Override public void onStartButtonPressed (){
       runAuto.schedule();
    }
    @Override public void onUpdate () {}
    @Override public void onStop () {}




}






