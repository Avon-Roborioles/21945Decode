package org.firstinspires.ftc.teamcode.OpModes.TeleOp;


import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Commands.Launch.ForceLaunch;
import org.firstinspires.ftc.teamcode.Commands.Intake.IntakeToSorterCommand;
import org.firstinspires.ftc.teamcode.Commands.Launch.LaunchWithOutSort;
import org.firstinspires.ftc.teamcode.Commands.Launch.LaunchWithSort;
import org.firstinspires.ftc.teamcode.Commands.Automatic.RunTurretAndLauncherFromHeading;
import org.firstinspires.ftc.teamcode.Commands.Turret.TurretJoystickCommand;
import org.firstinspires.ftc.teamcode.Subsystems.CompIntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompLauncherSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompPTOSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompSorterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompStatusSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompTurretSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompVisionSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.LauncherSubsystemGroup;
import org.firstinspires.ftc.teamcode.Utility.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;

@TeleOp
public class BlueTeleOp extends NextFTCOpMode {
    Pose holdPose;

    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    {
        addComponents(
                new SubsystemComponent(CompLauncherSubsystem.INSTANCE, CompIntakeSubsystem.INSTANCE, CompSorterSubsystem.INSTANCE, CompPTOSubsystem.INSTANCE, CompTurretSubsystem.INSTANCE, CompVisionSubsystem.INSTANCE, CompStatusSubsystem.INSTANCE, LauncherSubsystemGroup.INSTANCE),
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
        PedroComponent.follower().setPose(new Pose(62,10.25,(3*Math.PI)/2));
//        PedroComponent.follower().setPose(new Pose(26.75, 130, Math.toRadians(141)));



    }

    @Override
    public void onWaitForStart() {
        panelsTelemetry.update(telemetry);


    }

    @Override
    public void onStartButtonPressed() {
        Command launchWithoutSort = new LaunchWithOutSort();
        Command launchWithSort = new LaunchWithSort();
        Command intakeToSorter = new IntakeToSorterCommand();
        Command runTurretAndLauncherFromHeading = new RunTurretAndLauncherFromHeading(false);
        Command runTurretFromJoystick = new TurretJoystickCommand(Gamepads.gamepad2().rightStickX());
        Command forceLaunch = new ForceLaunch();
        Command eStop = new SequentialGroup(CompLauncherSubsystem.INSTANCE.StopLauncher, CompIntakeSubsystem.INSTANCE.StopIntake, CompLauncherSubsystem.INSTANCE.HoodDown(), new LambdaCommand().setStart(intakeToSorter::cancel).setIsDone(() -> true),
                new LambdaCommand().setStart(forceLaunch::cancel).setIsDone(() -> true),
                new LambdaCommand().setStart(runTurretFromJoystick::cancel).setIsDone(() -> true),
                new LambdaCommand().setStart(runTurretAndLauncherFromHeading::cancel).setIsDone(() -> true),
                new LambdaCommand().setStart(launchWithSort::cancel).setIsDone(() -> true));
        runTurretFromJoystick.schedule();

        DriverControlledCommand driverControlled = new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX().negate(),
                false
        );
        driverControlled.schedule();

        Gamepads.gamepad1().circle().whenBecomesTrue(launchWithoutSort).whenBecomesFalse(new LambdaCommand().setStart(launchWithoutSort::cancel).setIsDone(() -> true));
        Gamepads.gamepad1().square();
        Gamepads.gamepad1().triangle().whenBecomesTrue(CompPTOSubsystem.INSTANCE.engage).whenBecomesFalse(CompPTOSubsystem.INSTANCE.disengage);
        Gamepads.gamepad1().cross().whenBecomesTrue(runTurretAndLauncherFromHeading).whenBecomesFalse(new LambdaCommand().setStart(() -> {runTurretAndLauncherFromHeading.cancel();runTurretFromJoystick.schedule();}).setIsDone(() -> true));;
        Gamepads.gamepad1().dpadUp();
        Gamepads.gamepad1().dpadDown();
        Gamepads.gamepad1().dpadLeft();
        Gamepads.gamepad1().dpadRight();
        Gamepads.gamepad1().leftStickButton();
        Gamepads.gamepad1().rightStickButton();
        Gamepads.gamepad1().leftTrigger().atLeast(0.75).whenBecomesTrue(new LambdaCommand().setStart(intakeToSorter::cancel).setIsDone(() -> true));;
        Gamepads.gamepad1().rightTrigger().atLeast(0.75).whenBecomesTrue(new LambdaCommand().setStart(()->{PedroComponent.follower().setMaxPower(0.5);}).setIsDone(() -> true)).whenBecomesFalse(new LambdaCommand().setStart(()->{PedroComponent.follower().setMaxPower(1);}));
        Gamepads.gamepad1().leftBumper().whenBecomesTrue(intakeToSorter);
        Gamepads.gamepad1().rightBumper().whenTrue(CompIntakeSubsystem.INSTANCE.Outtake).whenBecomesFalse(CompIntakeSubsystem.INSTANCE.StopIntake);
        Gamepads.gamepad1().options();
        Gamepads.gamepad1().share();
        Gamepads.gamepad1().ps().whenBecomesTrue(new LambdaCommand().setStart(() -> {PedroComponent.follower().setPose(new Pose(PedroComponent.follower().getPose().getX(),PedroComponent.follower().getPose().getY(),(3*Math.PI)/2));}).setIsDone(() -> true));;
        Gamepads.gamepad1().touchpad().whenBecomesTrue(eStop);


        Gamepads.gamepad2().circle().whenBecomesTrue(launchWithSort).whenBecomesFalse(new LambdaCommand().setStart(launchWithSort::cancel).setIsDone(() -> true));;
        Gamepads.gamepad2().square().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.SpinUpToSpeed(800));
        Gamepads.gamepad2().triangle().whenBecomesTrue(forceLaunch).whenBecomesFalse(new LambdaCommand().setStart(forceLaunch::cancel).setIsDone(() -> true));
        Gamepads.gamepad2().cross().toggleOnBecomesTrue().whenBecomesTrue(runTurretAndLauncherFromHeading).whenBecomesFalse(new LambdaCommand().setStart(() -> {runTurretAndLauncherFromHeading.cancel();runTurretFromJoystick.schedule();}).setIsDone(() -> true));;
        Gamepads.gamepad2().dpadUp().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.SpeedUp);
        Gamepads.gamepad2().dpadDown().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.SpeedDown);
        Gamepads.gamepad2().dpadLeft();
        Gamepads.gamepad2().dpadRight();
        Gamepads.gamepad2().leftStickX();
        Gamepads.gamepad2().leftStickX();
        Gamepads.gamepad2().leftStickY().lessThan(-0.75).whenBecomesTrue(CompLauncherSubsystem.INSTANCE.HoodPlus);
        Gamepads.gamepad2().leftStickY().atLeast(0.75).whenBecomesTrue(CompLauncherSubsystem.INSTANCE.HoodMinus);
        Gamepads.gamepad2().leftStickButton();
        Gamepads.gamepad2().rightStickY().lessThan(-0.75);
        Gamepads.gamepad2().rightStickY().atLeast(0.75);
        Gamepads.gamepad2().rightStickButton();
        Gamepads.gamepad2().leftTrigger().atLeast(0.75).whenBecomesTrue(new LambdaCommand().setStart(intakeToSorter::cancel).setIsDone(() -> true));
        Gamepads.gamepad2().rightTrigger().atLeast(0.75);
        Gamepads.gamepad2().leftBumper().whenBecomesTrue(intakeToSorter);
        Gamepads.gamepad2().rightBumper().whenTrue(CompIntakeSubsystem.INSTANCE.Outtake).whenBecomesFalse(CompIntakeSubsystem.INSTANCE.StopIntake);
        Gamepads.gamepad2().options().whenBecomesTrue(CompStatusSubsystem.INSTANCE.cycleOBPatternCommand);
        Gamepads.gamepad2().share();
        Gamepads.gamepad2().ps().toggleOnBecomesTrue().whenTrue(new LambdaCommand().setStart(() -> {CompVisionSubsystem.INSTANCE.setLLToOB();}).setUpdate(() -> {CompVisionSubsystem.INSTANCE.SearchForOb();}).setIsDone(() -> true).setStop((Interrupted)-> {CompVisionSubsystem.INSTANCE.stopLL();}));
        Gamepads.gamepad2().touchpad().whenBecomesTrue(eStop);
        Command start = new SequentialGroup(CompSorterSubsystem.INSTANCE.wake, CompSorterSubsystem.INSTANCE.resetSorter, CompPTOSubsystem.INSTANCE.disengage);
        start.schedule();
    }

    @Override
    public void onUpdate() {
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void onStop() {
        CompStatusSubsystem.INSTANCE.returnToDefault();
        CompStatusSubsystem.INSTANCE.prism.clearAllAnimations();
        CompStatusSubsystem.INSTANCE.prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);
        telemetry.addLine("Done");


    }

}
