package org.firstinspires.ftc.teamcode.OpModes.Auto;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Commands.Automatic.RunTurretAndLauncherFromPoseAuto;
import org.firstinspires.ftc.teamcode.Commands.Intake.AutoIntake;
import org.firstinspires.ftc.teamcode.Commands.Intake.AutoIntakeCheck;
import org.firstinspires.ftc.teamcode.Commands.Launch.ForceLaunchAuto;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;

@Autonomous (group = "Red Goal", preselectTeleOp = "RedTeleOp")
public class RedGoal12BallOld extends AutoBase {
    Path DriveToScorePreload, DriveToPickUp1, DrivePickUp1, DriveToScore1, DriveToPickUp2, DrivePickUp2, DriveToScore2, DriveToPickUp3, DrivePickUp3, DriveToScore3, DriveEndDrive;
    Pose startingPos = new Pose(26.75, 130, Math.toRadians(141)).mirror();
    Pose scorePreload = new Pose(54, 114, Math.toRadians(270)).mirror();
    Pose toPickUp1 = new Pose(46, 84, Math.toRadians(180)).mirror();
    Pose pickUp1 = new Pose(22, 78, Math.toRadians(170)).mirror();
    Pose toScore1 = new Pose(56, 79, Math.toRadians(270)).mirror();
    Pose toPickUp2 = new Pose( 44, 60, Math.toRadians(180)).mirror();
    Pose toPickUp2CP = new Pose(57, 58).mirror();
    Pose pickUp2 = new Pose(22, 60, Math.toRadians(180)).mirror();
    Pose toScore2 = new Pose(56, 78, Math.toRadians(270)).mirror();
    Pose toScore2CP = new Pose(51, 61).mirror();
    Pose toPickUp3 = new Pose(44,35.5 , Math.toRadians(180)).mirror();
    Pose toPickUp3CP = new Pose(50, 33).mirror();
    Pose pickUp3 = new Pose(22, 35.5, Math.toRadians(180)).mirror();
    Pose toScore3 = new Pose(56,110 , Math.toRadians(270)).mirror();
    Pose toScore3CP = new Pose(50, 38).mirror();
    double maxPower = 1;






    Command runAuto;



    public void buildPaths () {
        DriveToScorePreload = new Path(new BezierLine(startingPos, scorePreload));
        DriveToScorePreload.setLinearHeadingInterpolation(startingPos.getHeading(), scorePreload.getHeading());
        DriveToScorePreload.setTimeoutConstraint(1000);

        DriveToPickUp1 = new Path(new BezierLine(scorePreload, toPickUp1));
        DriveToPickUp1.setLinearHeadingInterpolation(scorePreload.getHeading(), toPickUp1.getHeading());


        DrivePickUp1 = new Path(new BezierLine(toPickUp1, pickUp1));
        DrivePickUp1.setLinearHeadingInterpolation(toPickUp1.getHeading(), pickUp1.getHeading());
        DrivePickUp1.setTimeoutConstraint(1000);

        DriveToScore1 = new Path(new BezierLine(pickUp1, toScore1));
        DriveToScore1.setLinearHeadingInterpolation(pickUp1.getHeading(), toScore1.getHeading());
        DriveToScore1.setTimeoutConstraint(750);


        DriveToPickUp2 = new Path(new BezierCurve(toScore1, toPickUp2CP, toPickUp2));
        DriveToPickUp2.setLinearHeadingInterpolation(toScore1.getHeading(), toPickUp2.getHeading());

        DrivePickUp2 = new Path(new BezierLine(toPickUp2, pickUp2));
        DrivePickUp2.setLinearHeadingInterpolation(toPickUp2.getHeading(), pickUp2.getHeading());

        DriveToScore2 = new Path(new BezierCurve(pickUp2, toScore2CP, toScore2));
        DriveToScore2.setLinearHeadingInterpolation(pickUp2.getHeading(), toScore2.getHeading());
        DriveToScore2.setTimeoutConstraint(1500);

        DriveToPickUp3 = new Path(new BezierCurve(toScore2, toPickUp3CP, toPickUp3));
        DriveToPickUp3.setLinearHeadingInterpolation(toScore2.getHeading(), toPickUp3.getHeading());

        DrivePickUp3 = new Path(new BezierLine(toPickUp3, pickUp3));
        DrivePickUp3.setLinearHeadingInterpolation(toPickUp3.getHeading(), pickUp3.getHeading());

        DriveToScore3 = new Path(new BezierCurve(pickUp3, toScore3CP, toScore3));
        DriveToScore3.setLinearHeadingInterpolation(pickUp3.getHeading(), toScore3.getHeading());
        DriveToScore3.setTimeoutConstraint(1000);

//        DriveEndDrive = new Path(new BezierLine(toScore3, endPos));
//        DriveEndDrive.setLinearHeadingInterpolation(toScore3.getHeading(), endPos.getHeading());


    }
    @Override
    public void onInit() {

    }
    @Override public void onWaitForStart () {

    }
    @Override public void onStartButtonPressed (){

//
        Command RunLaunchPre = new RunTurretAndLauncherFromPoseAuto(true, new Pose(scorePreload.getX()-4, scorePreload.getY()-4, Math.toRadians(250)));
        Command RunLaunch1 = new RunTurretAndLauncherFromPoseAuto(true, toScore1);
        Command RunLaunch2 = new RunTurretAndLauncherFromPoseAuto(true, toScore2);
        Command RunLaunch3 = new RunTurretAndLauncherFromPoseAuto(true, toScore3);

        Command Intake1 = new AutoIntake(5000);
        Command Intake2 = new AutoIntake(3500);
        Command Intake3 = new AutoIntake(3000);

        Command IntakeCheck = new AutoIntakeCheck();
        Command StopLauncher = new LambdaCommand().setStart(()->{RunLaunchPre.cancel();RunLaunch1.cancel();RunLaunch2.cancel();RunLaunch3.cancel();
        }).setIsDone(()->{ return true;});
        Command LaunchWOSort = new SequentialGroup(new ForceLaunchAuto(), StopLauncher);
        PedroComponent.follower().setPose(startingPos);
        PedroComponent.follower().setMaxPower(maxPower);
        PedroComponent.follower().update();
        buildPaths();
        runAuto = new SequentialGroup(
                new LambdaCommand().setStart(()->{RunLaunchPre.schedule();}).setIsDone(()->{ return true;}),
                new Delay(0.125),
                 new ParallelGroup(
                            new SequentialGroup(
                                    new FollowPath(DriveToScorePreload, false),
                                    new Delay(0.005)

                            )

                 ),
                LaunchWOSort,
                 new ParallelGroup(
                         new SequentialGroup(
                                 new FollowPath(DriveToPickUp1),
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(0.4);}),
                                 new FollowPath(DrivePickUp1)),
                         Intake1
                 ),
                 new ParallelGroup(
                         new SequentialGroup(
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(1);}),
                                 new FollowPath(DriveToScore1),
                                 new Delay(0.25)

                         ),
                         new LambdaCommand().setStart(()->{RunLaunch1.schedule();}).setIsDone(()->{ return true;}),
                         IntakeCheck
                 ),
                LaunchWOSort,
                 new ParallelGroup(
                         new SequentialGroup(
                                 new FollowPath(DriveToPickUp2),
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(0.35);}),
                                 new FollowPath(DrivePickUp2)),
                         Intake2
                 ),
                 new ParallelGroup(
                         new SequentialGroup(
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(maxPower);}),
                                 new FollowPath(DriveToScore2),
                                 new Delay(0.25)
                         ),
                         new LambdaCommand().setStart(()->{RunLaunch2.schedule();}).setIsDone(()->{ return true;}),
                         IntakeCheck
                 ),
                LaunchWOSort,
                 new ParallelGroup(
                         new SequentialGroup(
                                 new FollowPath(DriveToPickUp3),
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(0.35);}),
                                 new FollowPath(DrivePickUp3)),
                         Intake3
                 ),
                 new ParallelGroup(
                         new SequentialGroup(
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(maxPower);}),
                                 new FollowPath(DriveToScore3),
                                 new Delay(0.25)
                         ),
                         new LambdaCommand().setStart(()->{RunLaunch3.schedule();}).setIsDone(()->{ return true;}),
                         IntakeCheck
                 ),
                LaunchWOSort,
                StopLauncher

        );
       runAuto.schedule();
    }

    @Override public void onStop () {}




}






