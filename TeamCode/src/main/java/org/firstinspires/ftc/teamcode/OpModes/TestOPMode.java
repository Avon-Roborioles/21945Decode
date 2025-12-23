package org.firstinspires.ftc.teamcode.OpModes;



import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.CompLauncherSubsystem;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
@TeleOp
public class TestOPMode extends NextFTCOpMode {

    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

    {
        addComponents(
                new SubsystemComponent(CompLauncherSubsystem.INSTANCE),
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
        Gamepads.gamepad1().dpadUp().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.SpeedUp);
        Gamepads.gamepad1().dpadDown().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.SpeedDown);
        Gamepads.gamepad1().x().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.StopLauncher);
        Gamepads.gamepad1().y().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.HoodDown());
        Gamepads.gamepad1().leftStickY().atLeast(0.5).whenBecomesTrue(CompLauncherSubsystem.INSTANCE.HoodMinus);
        Gamepads.gamepad1().leftStickY().lessThan(-0.5).whenBecomesTrue(CompLauncherSubsystem.INSTANCE.HoodPlus);
        Gamepads.gamepad1().leftStickButton().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.HoodToAngle(45/2));
        Gamepads.gamepad1().b().whenBecomesTrue(CompLauncherSubsystem.INSTANCE.SpinUpToSpeed(400));
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
