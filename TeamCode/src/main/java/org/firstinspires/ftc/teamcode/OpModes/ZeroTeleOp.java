package org.firstinspires.ftc.teamcode.OpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Launcher;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;

@Configurable

@TeleOp
public class ZeroTeleOp extends NextFTCOpMode {

    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

    public ZeroTeleOp() {

        addComponents(
                new SubsystemComponent(Launcher.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }
    @Override
    public void onInit(){
        Launcher.INSTANCE.build(hardwareMap, true);
    }
    @Override
    public void onStartButtonPressed() {

        Gamepads.gamepad2().b().toggleOnBecomesTrue()
                .whenTrue(Launcher.INSTANCE.stop());
        Gamepads.gamepad2().dpadUp()
                .whenTrue(Launcher.INSTANCE.HoodUp());
        Gamepads.gamepad2().dpadDown()
                .whenTrue(Launcher.INSTANCE.HoodDown());




    }
    @Override
    public void onUpdate() {
        telemetry.addData(String.valueOf(Launcher.INSTANCE.getMotorSpeed()), "speed");
        telemetry.addData(String.valueOf(Launcher.INSTANCE.speed), "target");
        telemetry.addData(String.valueOf(Launcher.INSTANCE.getServoAngle()), "angle");
        telemetry.addData("Rotate Position Raw", Launcher.INSTANCE.getRotatePositionRaw());


        panelsTelemetry.addData("Angle", Launcher.INSTANCE.getServoAngle());
        panelsTelemetry.addData("Motor Speed", Launcher.INSTANCE.getMotorSpeed());
        panelsTelemetry.addData("Target Speed", Launcher.INSTANCE.speed);
        panelsTelemetry.addData("Rotate Position Raw", Launcher.INSTANCE.getRotatePositionRaw());


        Launcher.INSTANCE.getLimelightTelemetry(telemetry);
        Launcher.INSTANCE.getLimelightTelemetry(panelsTelemetry);

        panelsTelemetry.update(telemetry);

    }
}
