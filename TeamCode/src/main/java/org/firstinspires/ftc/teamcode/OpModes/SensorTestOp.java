package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Utility.OctoQuadFWv3;
@TeleOp
public class SensorTestOp extends LinearOpMode {

    private double turretPos = 0;
    private static final double DEGREES_PER_US = (410.67 / 1024.0);
    private double angleOffset = 206;
    private final OctoQuadFWv3.EncoderDataBlock data = new OctoQuadFWv3.EncoderDataBlock();
    private AnalogInput floodgate;
    @Override
    public void runOpMode() throws InterruptedException {
        OctoQuadFWv3 Octo = hardwareMap.get(OctoQuadFWv3.class, "OctoQuad");
        Octo.resetEverything();
        Octo.setChannelBankConfig(OctoQuadFWv3.ChannelBankConfig.ALL_PULSE_WIDTH);
        Octo.setSingleEncoderDirection(0, OctoQuadFWv3.EncoderDirection.REVERSE);
        Octo.setSingleChannelPulseWidthParams(0, new OctoQuadFWv3.ChannelPulseWidthParams(0,1024));
        Octo.setSingleChannelPulseWidthTracksWrap(0, true);
        Octo.saveParametersToFlash();
        floodgate = hardwareMap.get(AnalogInput.class, "FloodGate");


        waitForStart();
        while (opModeIsActive()&& !isStopRequested()) {
            Octo.readAllEncoderData(data);
            turretPos = ((data.positions[0] * DEGREES_PER_US) - angleOffset);
            telemetry.addData("Floodgate Output Raw", floodgate.getVoltage());
            telemetry.addData("Current", (floodgate.getVoltage()/3.3)*80);
            telemetry.addData("turret Pos", turretPos);
            telemetry.addData("pwm", data.positions[0]);
            telemetry.update();
        }
    }


}
