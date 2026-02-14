package org.firstinspires.ftc.teamcode.OpModes.Auto;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Commands.Automatic.RunTurretAndLauncherFromPoseAuto;
import org.firstinspires.ftc.teamcode.Commands.Intake.AutoIntake;
import org.firstinspires.ftc.teamcode.Commands.Intake.AutoIntakeCheck;
import org.firstinspires.ftc.teamcode.Commands.Launch.ForceLaunchAutoSlow;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;

@Autonomous (group = "Red Back", preselectTeleOp = "RedTeleOp")
public class RedBack6Ball extends AutoBase {
    Path DriveToScorePreload, DriveToPickUp1, DrivePickUp1, DriveToScore1, DriveToPickUp2, DrivePickUp2, DriveToScore2, EndDrive;

    Pose startingPos = new Pose(56.65, 10.25, Math.toRadians(270)).mirror();
    Pose scorePreload = new Pose(58, 26, Math.toRadians(270)).mirror();
    Pose toPickUp1 = new Pose(44, 35.5, Math.toRadians(180)).mirror();
    Pose toPickUp1Cp = new Pose(64, 37).mirror();
    Pose pickUp1 = new Pose(18, 35.4, Math.toRadians(180)).mirror();
    Pose toScore1 = new Pose(58, 26, Math.toRadians(270)).mirror();
    Pose toScore1Cp = new Pose(59, 40).mirror();
    Pose endPos = new Pose(60, 38, Math.toRadians(270)).mirror();
    double maxPower = 0.75;






    Command runAuto;



    public void buildPaths () {
        DriveToScorePreload = new Path(new BezierLine(startingPos, scorePreload));
        DriveToScorePreload.setLinearHeadingInterpolation(startingPos.getHeading(), scorePreload.getHeading());


        DriveToPickUp1 = new Path(new BezierCurve(scorePreload, toPickUp1Cp, toPickUp1));
        DriveToPickUp1.setLinearHeadingInterpolation(scorePreload.getHeading(), toPickUp1.getHeading());


        DrivePickUp1 = new Path(new BezierLine(toPickUp1, pickUp1));
        DrivePickUp1.setLinearHeadingInterpolation(toPickUp1.getHeading(), pickUp1.getHeading());
        DrivePickUp1.setTimeoutConstraint(2000);

        DriveToScore1 = new Path(new BezierCurve(pickUp1, toScore1Cp, toScore1));
        DriveToScore1.setLinearHeadingInterpolation(pickUp1.getHeading(), toScore1.getHeading());
        DriveToScore1.setTimeoutConstraint(1000);




        EndDrive = new Path(new BezierLine(toScore1, endPos));
        EndDrive.setLinearHeadingInterpolation(toScore1.getHeading(), endPos.getHeading());
        EndDrive.setTimeoutConstraint(2000);















    }
    @Override
    public void onInit() {

    }
    @Override public void onWaitForStart () {

    }
    @Override public void onStartButtonPressed (){

//
        Command RunLaunchPre = new RunTurretAndLauncherFromPoseAuto(true, new Pose(scorePreload.getX()-6, scorePreload.getY()-6, scorePreload.getHeading()-Math.toRadians(2)));
        Command RunLaunch1 = new RunTurretAndLauncherFromPoseAuto(true,new Pose(scorePreload.getX()-6, scorePreload.getY()-6, scorePreload.getHeading()-Math.toRadians(2)));
        Command Intake1 = new AutoIntake(4000);
        Command Intake2 = new AutoIntake(4000);


        Command IntakeCheck = new AutoIntakeCheck();
        Command StopLauncher = new LambdaCommand().setStart(()->{RunLaunchPre.cancel();RunLaunch1.cancel();
        }).setIsDone(()->{ return true;});
        Command LaunchWOSort = new SequentialGroup(new ForceLaunchAutoSlow(), StopLauncher);
        PedroComponent.follower().setPose(startingPos);
        PedroComponent.follower().setMaxPower(maxPower);
        PedroComponent.follower().update();
        buildPaths();
        runAuto = new SequentialGroup(
                new LambdaCommand().setStart(()->{RunLaunchPre.schedule();}).setIsDone(()->{ return true;}),
                new Delay(1.75),
                new FollowPath(DriveToScorePreload),
                new Delay(1.75),
                LaunchWOSort,
                 new ParallelGroup(
                         new SequentialGroup(
                                 new FollowPath(DriveToPickUp1),
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(0.5);}),
                                 new FollowPath(DrivePickUp1)),
                         Intake1
                 ),
                 new ParallelGroup(
                         new SequentialGroup(
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(1);}),
                                 new FollowPath(DriveToScore1),
                                 new Delay(1.5)

                         ),
                         new LambdaCommand().setStart(()->{RunLaunch1.schedule();}).setIsDone(()->{ return true;}),
                         IntakeCheck
                 ),
                LaunchWOSort,
                StopLauncher,
                new FollowPath(EndDrive)

        );
       runAuto.schedule();
    }

    @Override public void onStop () {}




}






