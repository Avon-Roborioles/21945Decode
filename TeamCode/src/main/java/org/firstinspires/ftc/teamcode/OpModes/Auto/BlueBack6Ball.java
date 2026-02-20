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
import org.firstinspires.ftc.teamcode.Subsystems.SorterSubsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;

@Autonomous (group = "Blue Back", preselectTeleOp = "BlueTeleOp")
public class BlueBack6Ball extends AutoBase {
    Path DriveToScorePreload, DriveToPickUp1, DrivePickUp1, DriveToScore1, DriveToPickUp2, DrivePickUp2, DriveBack, DriveToPickUp2At2, DriveToScore2, EndDrive;

    Pose startingPos = new Pose(56.65, 10.25, Math.toRadians(270));
    Pose scorePreload = new Pose(58, 26, Math.toRadians(270));
    Pose toPickUp1 = new Pose(44, 35.5, Math.toRadians(180));
    Pose toPickUp1Cp = new Pose(64, 37);
    Pose pickUp1 = new Pose(18, 35.4, Math.toRadians(180));
    Pose toScore1 = new Pose(58, 26, Math.toRadians(270));
    Pose toScore1Cp = new Pose(59, 40);
    Pose toGrab2At1 = new Pose(14, 12, Math.toRadians(190));
    Pose toGrab2At1Cp = new Pose(9, 12);
    Pose grab2At1 = new Pose(13, 16, Math.toRadians(190));
    Pose backOut = new Pose(20, 10, Math.toRadians(180));
    Pose getToGrab2At2 = new Pose(12, 9, Math.toRadians(180));
    Pose toScore2 = new Pose(58, 26, Math.toRadians(270));



    Pose endPos = new Pose(60, 38, Math.toRadians(270));
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

        DriveToPickUp2 = new Path(new BezierLine(toScore1, toGrab2At1));
        DriveToPickUp2.setLinearHeadingInterpolation(toScore1.getHeading(), toGrab2At1.getHeading());

        DrivePickUp2 = new Path(new BezierCurve(toGrab2At1, toGrab2At1Cp, grab2At1));
        DrivePickUp2.setLinearHeadingInterpolation(toGrab2At1.getHeading(), grab2At1.getHeading());

        DriveBack = new Path(new BezierLine(grab2At1, backOut));
        DriveBack.setLinearHeadingInterpolation(grab2At1.getHeading(), backOut.getHeading());

        DriveToPickUp2At2 = new Path(new BezierLine(backOut, getToGrab2At2));
        DriveToPickUp2At2.setLinearHeadingInterpolation(backOut.getHeading(), getToGrab2At2.getHeading());

        DriveToScore2 = new Path(new BezierLine(getToGrab2At2, toScore2));
        DriveToScore2.setLinearHeadingInterpolation(getToGrab2At2.getHeading(), toScore2.getHeading());

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
        Command RunLaunchPre = new RunTurretAndLauncherFromPoseAuto(false, new Pose(scorePreload.getX()+8, scorePreload.getY()-8, scorePreload.getHeading()+Math.toRadians(3)));
        Command RunLaunch1 = new RunTurretAndLauncherFromPoseAuto(false,new Pose(scorePreload.getX()+6, scorePreload.getY()+6, scorePreload.getHeading()+Math.toRadians(3)));
        Command RunLaunch2 = new RunTurretAndLauncherFromPoseAuto(false,new Pose(scorePreload.getX()+6, scorePreload.getY()+6, scorePreload.getHeading()+Math.toRadians(3)));
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
                new LambdaCommand().setStart(() -> {
                    SorterSubsystem.INSTANCE.sortHug();
                }).setIsDone(() -> {return true;}),
                new LambdaCommand().setStart(()->{RunLaunchPre.schedule();}).setIsDone(()->{ return true;}),
                new Delay(0.25),
                new FollowPath(DriveToScorePreload),
                new Delay(3),
                LaunchWOSort,
                 new ParallelGroup(
                         new SequentialGroup(
                                 new FollowPath(DriveToPickUp1),
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(0.6);}),
                                 new FollowPath(DrivePickUp1)),
                         Intake1
                 ),
                 new ParallelGroup(
                         new SequentialGroup(
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(1);}),
                                 new FollowPath(DriveToScore1)

                         ),
                         new LambdaCommand().setStart(()->{RunLaunch1.schedule();}).setIsDone(()->{ return true;})//,
//                         IntakeCheck
                 ),
                new Delay(1.5),
                LaunchWOSort,
//                new SequentialGroup(
//                        new FollowPath(DriveToPickUp2),
//                        new InstantCommand(()->{ PedroComponent.follower().setMaxPower(0.5);})
//                ),
//                new LambdaCommand().setStart(()->{Intake2.start();}).setIsDone(()->{ return true;}),
//                new FollowPath(DrivePickUp2),
//                new Delay(0.25),
//                new FollowPath(DriveBack),
//                new Delay(0.25),
//                new FollowPath(DriveToPickUp2At2),
//                new Delay(0.25),
//                new InstantCommand(()->{ PedroComponent.follower().setMaxPower(1);}),
//                new LambdaCommand().setStart(()->{RunLaunch2.schedule();}).setIsDone(()->{ return true;}),
//                new FollowPath(DriveToScore2),
//                new Delay(0.25),
//                LaunchWOSort,
                new FollowPath(EndDrive)




        );
       runAuto.schedule();
    }

    @Override public void onStop () {}




}






