package org.firstinspires.ftc.teamcode.OpModes;



import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.CompLauncherSubsystem;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

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
                new SubsystemComponent(CompLauncherSubsystem.INSTANCE),
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
    }

    @Override
    public void onWaitForStart() {

    }

    @Override
    public void onStartButtonPressed() {
        DriverControlledCommand driverControlled = new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY().negate(),
                Gamepads.gamepad1().leftStickX().negate(),
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
        Gamepads.gamepad1().touchpad().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.StopLauncher);


        Gamepads.gamepad2().circle();
        Gamepads.gamepad2().square().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.SpinUpToSpeed(400));
        Gamepads.gamepad2().triangle();
        Gamepads.gamepad2().cross().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.HoodDown());
        Gamepads.gamepad2().dpadUp().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.SpeedUp);
        Gamepads.gamepad2().dpadDown().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.SpeedDown);
        Gamepads.gamepad2().dpadLeft();
        Gamepads.gamepad2().dpadRight();
        Gamepads.gamepad2().leftStickX().atLeast(0.75);
        Gamepads.gamepad2().leftStickX().lessThan(-0.75);
        Gamepads.gamepad2().leftStickY().lessThan(-0.75).whenBecomesTrue(CompLauncherSubsystem.INSTANCE.HoodPlus);
        Gamepads.gamepad2().leftStickY().atLeast(0.75).whenBecomesTrue(CompLauncherSubsystem.INSTANCE.HoodMinus);
        Gamepads.gamepad2().leftStickButton().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.HoodToAngle(45/2));
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
        Gamepads.gamepad2().share();
        Gamepads.gamepad2().ps();
        Gamepads.gamepad2().touchpad().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.StopLauncher);
    }

    @Override
    public void onUpdate() {
        CompLauncherSubsystem.INSTANCE.getLauncherTelemetry(panelsTelemetry);
        telemetry.update();
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void onStop() {
    }
}
