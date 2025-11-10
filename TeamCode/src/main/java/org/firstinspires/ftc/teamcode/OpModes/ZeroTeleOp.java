package org.firstinspires.ftc.teamcode.OpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.NewLauncher;
import org.firstinspires.ftc.teamcode.Subsystems.TempIntake;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Configurable

@TeleOp
public class ZeroTeleOp extends NextFTCOpMode {

    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

    public ZeroTeleOp() {

        addComponents(
                new SubsystemComponent(NewLauncher.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new SubsystemComponent(TempIntake.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }
    @Override
    public void onInit(){
        NewLauncher.INSTANCE.build(hardwareMap, true);
    }
    @Override
    public void onStartButtonPressed() {


        Gamepads.gamepad1().a()
                .whenTrue(TempIntake.INSTANCE.intake());
        Gamepads.gamepad1().b()
                .whenTrue(TempIntake.INSTANCE.outtake());
        Gamepads.gamepad1().x()
                .whenTrue(TempIntake.INSTANCE.stop());





    }
    @Override
    public void onUpdate() {


        panelsTelemetry.update(telemetry);

    }
}
