package org.firstinspires.ftc.teamcode.OpModes.TeleOp;



import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Commands.Drive.TeleOpDriveCommand;
import org.firstinspires.ftc.teamcode.Commands.Drive.TeleOpRampDriveCommand;
import org.firstinspires.ftc.teamcode.Commands.Drive.TeleOpRampScoreDriveCommand;

import dev.nextftc.ftc.Gamepads;

@TeleOp
public class BlueTeleOp extends TeleOpBase {


    @Override
    public TeleOpDriveCommand driveCommand() {
        return new TeleOpDriveCommand(
                Gamepads.gamepad1().leftStickY(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX().negate(),
                Gamepads.gamepad1().triangle()
        );
    }

    @Override
    public TeleOpRampDriveCommand rampDriveCommand() {
        return new TeleOpRampDriveCommand(
                false
        );
    }

    @Override
    public TeleOpRampScoreDriveCommand rampScoreDriveCommand() {
        return new TeleOpRampScoreDriveCommand(
                false
        );
    }

    @Override
    public Boolean RedAlliance() {
        return false;
    }
}
