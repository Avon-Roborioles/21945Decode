package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
@TeleOp
public class LimeLightOpMode extends LinearOpMode {
    private Limelight3A limelight;
    private Servo servo;
    private double servoPos = 0.5;
    @Override
    public void runOpMode() throws InterruptedException {
        servo = hardwareMap.get(Servo.class, "swingArm");
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        telemetry.setMsTransmissionInterval(11);
        servo.setPosition(0.5);
        limelight.pipelineSwitch(0);


        /*
         * Starts polling for data.
         */
        limelight.start();
        waitForStart();
        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();
            if (result != null) {
                if (result.isValid()) {
                    Pose3D botpose = result.getBotpose();
                    telemetry.addData("tx", result.getTx());
                    telemetry.addData("ty", result.getTy());
                    telemetry.addData("Botpose", botpose.toString());
                    telemetry.addData("tags", result.getBotposeTagCount());

                    telemetry.addData("distance", getDistance());
                    telemetry.update();
                    if (result.getTx()!= 0){
                        servoPos -= 0.00003 * result.getTx();
                    }
//                    if(result.getTx()>1){
//                        servoPos -=0.0002;
//                    }else if(result.getTx()<10){
//                        servoPos += 0.0002;
//                    }

                }
            }
            if (servoPos> 0.65){
                servoPos = 0.65;
            }else if (servoPos< 0.35){
                servoPos = 0.35;
            }
            servo.setPosition(servoPos);
        }
    }
    double getDistance(){
        LLResult result = limelight.getLatestResult();
        double targetOffsetAngle_Vertical = result.getTy();
        
// how many degrees back is your limelight rotated from perfectly vertical?
        double limelightMountAngleDegrees = 24.0;

        // distance from the center of the Limelight lens to the floor
        double limelightLensHeightInches = 11.5;

        // distance from the target to the floor
        double goalHeightInches = 28.5;

        double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
        double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);

        //calculate distance
        return (goalHeightInches - limelightLensHeightInches) / Math.tan(angleToGoalRadians);
    }
    double getYawAprilTag(){
        LLResult result = limelight.getLatestResult();
        return result.getBotpose().getOrientation().getYaw(AngleUnit.DEGREES);

    }

}
