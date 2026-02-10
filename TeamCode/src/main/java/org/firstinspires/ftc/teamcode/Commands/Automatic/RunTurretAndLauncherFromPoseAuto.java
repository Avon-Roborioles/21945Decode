package org.firstinspires.ftc.teamcode.Commands.Automatic;

import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.Subsystems.CompLauncherSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompStatusSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompTurretSubsystem;
import org.firstinspires.ftc.teamcode.Utility.Timing;

import dev.nextftc.core.commands.Command;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;


public class RunTurretAndLauncherFromPoseAuto extends Command {

    Pose botPose;
    double distanceToGoal;
    boolean redAlliance;
    double turretFieldAngleRad;
    Timing.Timer startDelay = new Timing.Timer(500);

    static Pose redGoal = new Pose(144,144);
    static Pose RedGoalAngle = new Pose(133, 133);
    static Pose blueGoal = new Pose(0,144);
    static Pose BlueGoalAngle = new Pose(8, 134);
    Pose goal;
    Pose goalAngle;


    public RunTurretAndLauncherFromPoseAuto(boolean redAlliance, Pose EndPose) {
        this.redAlliance = redAlliance;
        this.botPose = EndPose;
        requires(CompTurretSubsystem.INSTANCE);
        setInterruptible(true); // this is the default, so you don't need to specify
    }

    @Override
    public boolean isDone() {
        return false;// whether or not the command is done
    }

    @Override
    public void start() {
        goal = redAlliance ? redGoal : blueGoal;
        goalAngle = redAlliance ? RedGoalAngle : BlueGoalAngle;
        startDelay.start();

        // executed when the command begins
    }

    @Override
    public void update() {
        if (!PedroComponent.follower().isBusy() && startDelay.done()){
            botPose = PedroComponent.follower().getPose();
        }
        distanceToGoal = Math.hypot((goal.getX()- botPose.getX()), (goal.getY()-botPose.getY()));
        turretFieldAngleRad = Math.atan2((goalAngle.getY()- botPose.getY()), (goalAngle.getX()- botPose.getX()) + PedroComponent.follower().getAngularVelocity()* CompStatusSubsystem.INSTANCE.getLoopTimeSeconds());

        CompTurretSubsystem.INSTANCE.turnTurretToFieldAngleAuto(turretFieldAngleRad, botPose.getHeading());
        CompLauncherSubsystem.INSTANCE.RunLauncherFromDistance(distanceToGoal);
        ActiveOpMode.telemetry().addLine("-------------- RunTurretAndLauncherFromHeading Telemetry: --------------");
        ActiveOpMode.telemetry().addData("redAlliance", redAlliance);
        ActiveOpMode.telemetry().addData("BotPose", botPose);
        ActiveOpMode.telemetry().addData("distanceToGoal", distanceToGoal);
        ActiveOpMode.telemetry().addData("turretFieldAngleDeg", Math.toDegrees(turretFieldAngleRad));

        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {

        CompLauncherSubsystem.INSTANCE.HoodDown().schedule();
        CompLauncherSubsystem.INSTANCE.StopLauncher.schedule();
        CompStatusSubsystem.INSTANCE.setPrismNorm();


        // executed when the command ends
    }
}
