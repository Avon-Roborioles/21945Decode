package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Subsystems.SorterSubsystem;

@TeleOp
public class SorterTeleOp extends LinearOpMode {
    @Override public void runOpMode() {
        SorterSubsystem sorter = new SorterSubsystem(hardwareMap);

        double open = 0.45;
        double close = 0;


        waitForStart();
        while (opModeIsActive()){

        if (gamepad1.a) {
                sorter.servoOne.setPosition(close);
            } else {
                sorter.servoOne.setPosition(open);
            }

            if (gamepad1.x) {
                sorter.servoTwo.setPosition(open);
            } else {
                sorter.servoTwo.setPosition(close);
            }

            if (gamepad1.left_bumper) {
                sorter.servoThree.setPosition(open);
            } else {
                sorter.servoThree.setPosition(close);
            }
        }
    }
}
