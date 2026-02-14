package org.firstinspires.ftc.teamcode.OpModes.Tests;


import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.MovingStatistics;

import org.firstinspires.ftc.teamcode.Commands.Intake.IntakeToSorterCommand;
import org.firstinspires.ftc.teamcode.Commands.Launch.LaunchWithOutSort;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.LauncherSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.PTOSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.SorterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.StatusSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.VisionSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.LauncherSubsystemGroup;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;

@TeleOp
@Disabled
public class LoopTimeTestOPMode extends NextFTCOpMode {

    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    long prevTime = 0;
    MovingStatistics statistics = new MovingStatistics(200);
    //With nothing loop times around 150 Hz

    {
        addComponents(
                new SubsystemComponent(LauncherSubsystem.INSTANCE, IntakeSubsystem.INSTANCE, SorterSubsystem.INSTANCE, PTOSubsystem.INSTANCE, TurretSubsystem.INSTANCE, VisionSubsystem.INSTANCE, StatusSubsystem.INSTANCE, LauncherSubsystemGroup.INSTANCE),
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
        PedroComponent.follower().setPose(new Pose(72,72,270));



    }

    @Override
    public void onWaitForStart() {
        panelsTelemetry.update(telemetry);


    }

    @Override
    public void onStartButtonPressed() {


        DriverControlledCommand driverControlled = new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX().negate(),
                false
        );
        driverControlled.schedule();

        Gamepads.gamepad1().circle();
        Gamepads.gamepad1().square();
        Gamepads.gamepad1().triangle();
        Gamepads.gamepad1().cross();
        Gamepads.gamepad1().dpadUp();
        Gamepads.gamepad1().dpadDown();
        Gamepads.gamepad1().dpadLeft();
        Gamepads.gamepad1().dpadRight();
        Gamepads.gamepad1().leftStickButton();
        Gamepads.gamepad1().rightStickButton();
        Gamepads.gamepad1().leftTrigger().atLeast(0.75);
        Gamepads.gamepad1().rightTrigger().atLeast(0.75);
        Gamepads.gamepad1().leftBumper();
        Gamepads.gamepad1().rightBumper();
        Gamepads.gamepad1().options();
        Gamepads.gamepad1().share();
        Gamepads.gamepad1().ps();
        Gamepads.gamepad1().touchpad().whenBecomesTrue(LauncherSubsystem.INSTANCE.StopLauncher);


        Gamepads.gamepad2().circle().whenBecomesTrue(new LaunchWithOutSort());;
        Gamepads.gamepad2().square().whenBecomesTrue(LauncherSubsystem.INSTANCE.SpinUpToSpeed(800));
        Gamepads.gamepad2().triangle();
        Gamepads.gamepad2().cross().whenBecomesTrue(new IntakeToSorterCommand());
        Gamepads.gamepad2().dpadUp().whenBecomesTrue(LauncherSubsystem.INSTANCE.SpeedUp);
        Gamepads.gamepad2().dpadDown().whenBecomesTrue(LauncherSubsystem.INSTANCE.SpeedDown);
        Gamepads.gamepad2().dpadLeft();
        Gamepads.gamepad2().dpadRight();
        Gamepads.gamepad2().leftStickX().atLeast(0.75).whenTrue(TurretSubsystem.INSTANCE.turretLeft);
        Gamepads.gamepad2().leftStickX().lessThan(-0.75).whenTrue(TurretSubsystem.INSTANCE.turretRight);;
        Gamepads.gamepad2().leftStickY().lessThan(-0.75).whenBecomesTrue(LauncherSubsystem.INSTANCE.HoodPlus);
        Gamepads.gamepad2().leftStickY().atLeast(0.75).whenBecomesTrue(LauncherSubsystem.INSTANCE.HoodMinus);
        Gamepads.gamepad2().leftStickButton();
        Gamepads.gamepad2().rightStickX().atLeast(0.75);
        Gamepads.gamepad2().rightStickX().lessThan(-0.75);
        Gamepads.gamepad2().rightStickY().lessThan(-0.75);
        Gamepads.gamepad2().rightStickY().atLeast(0.75);
        Gamepads.gamepad2().rightStickButton();
        Gamepads.gamepad2().leftTrigger().atLeast(0.75);
        Gamepads.gamepad2().rightTrigger().atLeast(0.75);
        Gamepads.gamepad2().leftBumper();
        Gamepads.gamepad2().rightBumper();
        Gamepads.gamepad2().options();
        Gamepads.gamepad2().share().toggleOnBecomesTrue().whenTrue(IntakeSubsystem.INSTANCE.Outtake).whenBecomesFalse(IntakeSubsystem.INSTANCE.StopIntake);
        Gamepads.gamepad2().ps();
        Gamepads.gamepad2().touchpad().whenBecomesTrue(LauncherSubsystem.INSTANCE.StopLauncher);
        Command start = new SequentialGroup(SorterSubsystem.INSTANCE.wake, SorterSubsystem.INSTANCE.resetSorter, PTOSubsystem.INSTANCE.disengage);
        start.schedule();


    }

    @Override
    public void onUpdate() {





//        telemetry.addData("x", PedroComponent.follower().getPose().getX());
//        telemetry.addData("y", PedroComponent.follower().getPose().getY());
//        telemetry.addData("heading", PedroComponent.follower().getPose().getHeading());
//        telemetry.addData("Distance to Goal", Math.hypot((0-PedroComponent.follower().getPose().getX()), (144-PedroComponent.follower().getPose().getY())));
        panelsTelemetry.update(telemetry);
        long curTime = System.currentTimeMillis();
        if (prevTime != 0)
        {
            long deltaTime = curTime - prevTime;
            statistics.add(deltaTime);
            telemetry.addData("Loop Hz", 1e3/statistics.getMean());
        }
        prevTime = curTime;

//        CompStatusSubsystem.INSTANCE.sorterLight.schedule();
        /*------------The Problem^
        *
         */
    }

    @Override
    public void onStop() {



    }

}
