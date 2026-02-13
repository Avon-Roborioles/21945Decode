package org.firstinspires.ftc.teamcode.OpModes.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Commands.Automatic.TeleOpDriveCommand;

import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.driving.DriverControlledCommand;

@TeleOp
public class RedTeleOp extends TeleOpBase {


    @Override
    public TeleOpDriveCommand driveCommand() {
        return new TeleOpDriveCommand(
                Gamepads.gamepad1().leftStickY().negate(),
                Gamepads.gamepad1().leftStickX().negate(),
                Gamepads.gamepad1().rightStickX().negate(),
                Gamepads.gamepad1().triangle()

        );
    }

    @Override
    public Boolean RedAlliance() {
        return true;
    }
}
