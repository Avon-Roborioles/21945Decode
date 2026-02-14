package org.firstinspires.ftc.teamcode.OpModes.Tests;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;

@TeleOp(name="PTO Test", group="Tests")
@Disabled
@Configurable
public class PTOTestOpMode extends LinearOpMode {
    MotorEx fl, fr, bl,br;
    ServoEx ptoL = new ServoEx("PTO L");
    ServoEx ptoR = new ServoEx("PTO R");

    @Override
    public void runOpMode() throws InterruptedException {
        fr = new MotorEx("FrontRight");
        fl = new MotorEx("FrontLeft").reversed();
        br = new MotorEx("BackRight");
        bl = new MotorEx("BackLeft").reversed();



        waitForStart();
        while(opModeIsActive()&&!isStopRequested()){
            if(gamepad1.a){
                ptoL.setPosition(0);
                ptoR.setPosition(0);
            }else if(gamepad1.b){
                ptoL.setPosition(1);
                ptoR.setPosition(1);
            }

            fr.setPower(gamepad1.left_stick_y);
            br.setPower(gamepad1.left_stick_y);
            fl.setPower(gamepad1.right_stick_y);
            bl.setPower(gamepad1.right_stick_y);

        }



    }
}
