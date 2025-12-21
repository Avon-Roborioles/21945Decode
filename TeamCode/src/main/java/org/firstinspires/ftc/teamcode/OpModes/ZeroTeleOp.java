package org.firstinspires.ftc.teamcode.OpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

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
    private Servo servo;
    public ZeroTeleOp() {

//        addComponents(
//                new SubsystemComponent(NewLauncher.INSTANCE),
//                BulkReadComponent.INSTANCE,
//                BindingsComponent.INSTANCE,
//                new SubsystemComponent(TempIntake.INSTANCE),
//                BulkReadComponent.INSTANCE,
//                BindingsComponent.INSTANCE,
//                new PedroComponent(Constants::createFollower)
//        );
    }
    @Override
    public void onInit(){
//        NewLauncher.INSTANCE.build(hardwareMap, true);
        servo = hardwareMap.get(Servo.class, "servo");
    }
    @Override
    public void onStartButtonPressed() {

//        Gamepads.gamepad2().b().toggleOnBecomesTrue()
//                .whenTrue(TempIntake.INSTANCE.outtake())
//                .whenFalse(TempIntake.INSTANCE.stop());
//






    }
    @Override
    public void onUpdate() {

        if(gamepad1.a){
            servo.setPosition(0);
        }else if(gamepad1.b){
            servo.setPosition(0.4);
        }
//        panelsTelemetry.update(telemetry);

    }

}
