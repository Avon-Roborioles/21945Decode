package org.firstinspires.ftc.teamcode.Commands;

import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.Subsystems.CompLauncherSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompStatusSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompTurretSubsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;


public class RunTurretAndLauncherFromHeading extends Command {

    Pose botPose;
    double distanceToGoal;
    boolean redAlliance;
    double turretFieldAngleRad;

    static Pose redGoal = new Pose(142,142);
    static Pose blueGoal = new Pose(2,142);
    Pose goal;

    public RunTurretAndLauncherFromHeading(boolean redAlliance) {
        this.redAlliance = redAlliance;
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

        // executed when the command begins
    }

    @Override
    public void update() {
        botPose = PedroComponent.follower().getPose();
        distanceToGoal = Math.hypot((goal.getX()- botPose.getX()), (goal.getY()-botPose.getY()));
        turretFieldAngleRad = Math.atan2((goal.getY()- botPose.getY()), (goal.getX()- botPose.getX()) + PedroComponent.follower().getAngularVelocity()* CompStatusSubsystem.INSTANCE.getLoopTimeSeconds());

        CompTurretSubsystem.INSTANCE.turnTurretToFieldAngle(turretFieldAngleRad);
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


        // executed when the command ends
    }
}
