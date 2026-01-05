package org.firstinspires.ftc.teamcode.OpModes;



import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.Commands.IntakeToSorterCommand;
import org.firstinspires.ftc.teamcode.Commands.LaunchWithOutSort;
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
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;

@TeleOp
public class TestOPMode extends NextFTCOpMode {

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

        Gamepads.gamepad1().circle().whenBecomesTrue(new SequentialGroup( CompSorterSubsystem.INSTANCE.ejectL.setRequirements(CompSorterSubsystem.INSTANCE), CompSorterSubsystem.INSTANCE.resetSorter.setRequirements(CompSorterSubsystem.INSTANCE)));
        Gamepads.gamepad1().square().whenBecomesTrue(new SequentialGroup( CompSorterSubsystem.INSTANCE.ejectC.setRequirements(CompSorterSubsystem.INSTANCE), CompSorterSubsystem.INSTANCE.resetSorter.setRequirements(CompSorterSubsystem.INSTANCE)));;
        Gamepads.gamepad1().triangle().whenBecomesTrue(new SequentialGroup( CompSorterSubsystem.INSTANCE.ejectR.setRequirements(CompSorterSubsystem.INSTANCE), CompSorterSubsystem.INSTANCE.resetSorter.setRequirements(CompSorterSubsystem.INSTANCE)));;
        Gamepads.gamepad1().cross();
        Gamepads.gamepad1().dpadUp().whenBecomesTrue(CompPTOSubsystem.INSTANCE.disengage);
        Gamepads.gamepad1().dpadDown().whenBecomesTrue(CompPTOSubsystem.INSTANCE.engage);
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
        Gamepads.gamepad1().touchpad().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.StopLauncher);


        Gamepads.gamepad2().circle().whenBecomesTrue(new SequentialGroup( CompSorterSubsystem.INSTANCE.ejectL.setRequirements(CompSorterSubsystem.INSTANCE), CompSorterSubsystem.INSTANCE.resetSorter.setRequirements(CompSorterSubsystem.INSTANCE), CompSorterSubsystem.INSTANCE.ejectR.setRequirements(CompSorterSubsystem.INSTANCE), CompSorterSubsystem.INSTANCE.resetSorter.setRequirements(CompSorterSubsystem.INSTANCE), CompSorterSubsystem.INSTANCE.ejectC.setRequirements(CompSorterSubsystem.INSTANCE), CompSorterSubsystem.INSTANCE.resetSorter.setRequirements(CompSorterSubsystem.INSTANCE)));
        Gamepads.gamepad2().square().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.SpinUpToSpeed(400));
        Gamepads.gamepad2().triangle().toggleOnBecomesTrue().whenTrue(CompIntakeSubsystem.INSTANCE.Intake).whenBecomesFalse(CompIntakeSubsystem.INSTANCE.StopIntake);
        Gamepads.gamepad2().cross().whenBecomesTrue(new IntakeToSorterCommand());
        Gamepads.gamepad2().dpadUp().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.SpeedUp);
        Gamepads.gamepad2().dpadDown().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.SpeedDown);
        Gamepads.gamepad2().dpadLeft().whenBecomesTrue(CompVisionSubsystem.INSTANCE.up);
        Gamepads.gamepad2().dpadRight().whenBecomesTrue(CompVisionSubsystem.INSTANCE.down);
        Gamepads.gamepad2().leftStickX().atLeast(0.75).whenTrue(CompTurretSubsystem.INSTANCE.turretLeft);
        Gamepads.gamepad2().leftStickX().lessThan(-0.75).whenTrue(CompTurretSubsystem.INSTANCE.turretRight);;
        Gamepads.gamepad2().leftStickY().lessThan(-0.75).whenBecomesTrue(CompLauncherSubsystem.INSTANCE.HoodPlus);
        Gamepads.gamepad2().leftStickY().atLeast(0.75).whenBecomesTrue(CompLauncherSubsystem.INSTANCE.HoodMinus);
        Gamepads.gamepad2().leftStickButton().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.HoodDown());
        Gamepads.gamepad2().rightStickX().atLeast(0.75);
        Gamepads.gamepad2().rightStickX().lessThan(-0.75);
        Gamepads.gamepad2().rightStickY().lessThan(-0.75);
        Gamepads.gamepad2().rightStickY().atLeast(0.75);
        Gamepads.gamepad2().rightStickButton();
        Gamepads.gamepad2().leftTrigger().atLeast(0.75);
        Gamepads.gamepad2().rightTrigger().atLeast(0.75);
        Gamepads.gamepad2().leftBumper().whenBecomesTrue(CompVisionSubsystem.INSTANCE.tiltPlus);
        Gamepads.gamepad2().rightBumper().whenBecomesTrue(CompVisionSubsystem.INSTANCE.tiltMinus);
        Gamepads.gamepad2().options();
        Gamepads.gamepad2().share().toggleOnBecomesTrue().whenTrue(CompIntakeSubsystem.INSTANCE.Outtake).whenBecomesFalse(CompIntakeSubsystem.INSTANCE.StopIntake);
        Gamepads.gamepad2().ps().whenBecomesTrue(new LaunchWithOutSort());
        Gamepads.gamepad2().touchpad().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.StopLauncher);
        Command start = new SequentialGroup(CompSorterSubsystem.INSTANCE.wake, CompSorterSubsystem.INSTANCE.resetSorter);
        start.schedule();


    }

    @Override
    public void onUpdate() {



        telemetry.addData("x", PedroComponent.follower().getPose().getX());
        telemetry.addData("y", PedroComponent.follower().getPose().getY());
        telemetry.addData("heading", PedroComponent.follower().getPose().getHeading());
        telemetry.addData("Distance to Goal", Math.hypot((0-PedroComponent.follower().getPose().getX()), (144-PedroComponent.follower().getPose().getY())));

        panelsTelemetry.update(telemetry);
        CompStatusSubsystem.INSTANCE.sorterLight.schedule();

    }

    @Override
    public void onStop() {
        CompStatusSubsystem.INSTANCE.returnToDefault();
        CompStatusSubsystem.INSTANCE.prism.clearAllAnimations();
        CompStatusSubsystem.INSTANCE.prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);
        telemetry.addLine("Done");


    }

}
