package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class CompVisionSubsystem implements Subsystem {
    public static final CompVisionSubsystem INSTANCE = new CompVisionSubsystem();
    private CompVisionSubsystem() {}

    public ServoEx LLTilt = new ServoEx("LL Tilt");
    private Limelight3A limelight;

    private int redPipeline = 0;
    private int bluePipeline = 1;
    private final int startingPipeline = 0;
    private int reLocalizingPipeline = 3;
    private int restPipeline = 4;

    private int OBPipeline = 2;
    private double maxLLAngle = 49;
    private double minLLAngle = -40;
    private double maxLLPWM = 0.76;
    private double minLLPWM = 0;
    private LLResult latestResult;
    private double lLTiltAngle = 0;

    // put hardware, commands, etc here
    public Command down = new SetPosition(LLTilt, 0);
    public Command up = new SetPosition(LLTilt, 1);
    public Command tiltPlus = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                lLTiltAngle += 5;
            })
            .setUpdate(() -> {
                // Runs on update
            })
            .setStop(interrupted -> {
                // Runs on stop
            })
            .setIsDone(() -> true) // Returns if the command has finished
            .requires(this)
            .setInterruptible(true);
    public Command tiltMinus = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                lLTiltAngle -= 5;
            })
            .setUpdate(() -> {
                // Runs on update
            })
            .setStop(interrupted -> {
                // Runs on stop
            })
            .setIsDone(() -> true) // Returns if the command has finished
            .requires(this)
            .setInterruptible(true);
    public void llTiltToAngle (double angle){
        //clamp it
        if (angle > maxLLAngle) {
            angle = maxLLAngle;
        }else if (angle < minLLAngle ){
            angle = minLLAngle;
        }
        //find fraction of total Range
        angle = angle - minLLAngle;// make it positive
        angle = angle/(maxLLAngle-minLLAngle);

        lLTiltAngle = angle;
        LLTilt.setPosition((angle*(maxLLPWM-minLLPWM))+minLLPWM);

    }

    public void setLLToRedGoal(){
        limelight.stop();
        limelight.pipelineSwitch(redPipeline);
        limelight.start();
    }
    public void setLLToBlue(){
        limelight.stop();
        limelight.pipelineSwitch(bluePipeline);
        limelight.start();
        lLTiltAngle = 0;
    }
    public void SearchForOb(){
        llTiltToAngle(10);
        update();
        if (latestResult != null) {
            //may need to sort in case multiple tags can be seen
            if (latestResult.isValid()) {
                switch (latestResult.getFiducialResults().get(0).getFiducialId()) {
                    case 21:
                        CompStatusSubsystem.INSTANCE.setCurrentOBPattern(CompStatusSubsystem.OBPattern.GPP);
                        break;
                    case 22:
                        CompStatusSubsystem.INSTANCE.setCurrentOBPattern(CompStatusSubsystem.OBPattern.PGP);
                        break;
                    case 23:
                        CompStatusSubsystem.INSTANCE.setCurrentOBPattern(CompStatusSubsystem.OBPattern.PPG);
                        break;

                }

            }
        }

    }
    public void stopLL(){
        limelight.stop();
        limelight.close();
    }
    public void setReLocalizingPipeline(){
        llTiltToAngle(30);
        lLTiltAngle = 30;
        limelight.stop();
        limelight.pipelineSwitch(reLocalizingPipeline);
        limelight.start();
        limelight.updateRobotOrientation(PedroComponent.follower().getHeading());

    }
    public void reLocalizeWithLimeLight(){
        update();
        getLLTelemetryAdv();
        if(latestResult != null){
            if(latestResult.isValid()) {
                PedroComponent.follower().setX(latestResult.getBotpose().getPosition().toUnit(DistanceUnit.INCH).x+72);
                PedroComponent.follower().setY(latestResult.getBotpose().getPosition().toUnit(DistanceUnit.INCH).y+72);
                PedroComponent.follower().setHeading(latestResult.getBotpose().getOrientation().getYaw(AngleUnit.RADIANS) + Math.PI/2);
            }
        }
    }
    private void startLL(){
        limelight.start();
    }
    public void captureSnap(){
        limelight.captureSnapshot("snap "+ ActiveOpMode.getRuntime());
    }
    private LLResult readAprilTag(){
        return limelight.getLatestResult();
    }
    private LLResult update(){
        latestResult = limelight.getLatestResult();
        return latestResult;
    }
    public double getDistanceToGoal(LLResult result){

        double limelightMountAngleDegrees = 25.0;// how many degrees back is your limelight rotated from perfectly vertical?
        double limelightLensHeightInches = 14.370079;// distance from the center of the Limelight lens to the floor

        double angleToGoalDegrees = limelightMountAngleDegrees + result.getTy();
        double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);

        //calculate distance
        return (29.5 - limelightLensHeightInches) / Math.tan(angleToGoalRadians);
    }

    public double getYawToGoal(LLResult result){
        return result.getTx();
    }

    public void setLLToOB(){
        limelight.stop();
        limelight.pipelineSwitch(OBPipeline);
        limelight.start();

    }

    @Override
    public void initialize() {
        // initialization logic (runs on init)
        limelight = ActiveOpMode.hardwareMap().get(Limelight3A.class, "LimeLight");
        lLTiltAngle = 0;

    }



    @Override
    public void periodic() {
        // periodic logic (runs every loop)
//        getLLTelemetryAdv();
//        getLLTiltTelemetryAdv();
//        if(ActiveOpMode.isStarted()){
//            llTiltToAngle(lLTiltAngle);
//        }
    }
    public void getLLTelemetryAdv(){
        update();
        ActiveOpMode.telemetry().addLine("-------------- LimeLight Telemetry Adv: --------------");
        if (latestResult != null) {
            ActiveOpMode.telemetry().addData("Target Visible", latestResult.isValid());
            if (latestResult.isValid()) {
                ActiveOpMode.telemetry().addData("BotPose", latestResult.getBotpose());


                ActiveOpMode.telemetry().addData("Yaw to Goal", latestResult.getTx());
                ActiveOpMode.telemetry().addData("Pitch to Goal", latestResult.getTy());
            }
        }
    }
    public void getLLTiltTelemetryAdv(){
        ActiveOpMode.telemetry().addLine("-------------- LimeLight Tilt Telemetry Adv: --------------");
        ActiveOpMode.telemetry().addData("LL Tilt Target Angle", lLTiltAngle );
        ActiveOpMode.telemetry().addData("LL Tilt Current PWM", LLTilt.getPosition());

    }
}
