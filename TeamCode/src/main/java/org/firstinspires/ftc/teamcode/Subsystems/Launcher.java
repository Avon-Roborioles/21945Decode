package org.firstinspires.ftc.teamcode.Subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;
@Configurable
public class Launcher implements Subsystem {

    public static final Launcher INSTANCE = new Launcher();
    private Launcher (){}

    public double speed = 0;
    private Limelight3A limelight;
    private int redPipeline = 0;
    private int bluePipeline = 1;
    private int angle = 0;
    private int maxTurret = 1600;
    private int turretPos = 0;

    private LLResult result;
    private final int startingPipeline = 0;


    public MotorEx launcherMotor = new MotorEx("LauncherMotor");
    public MotorEx rotateMotor = new MotorEx("RotateMotor");
    private ServoEx hoodServo = new ServoEx("HoodServo");

    public void build(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "Limelight");
        limelight.pipelineSwitch(startingPipeline);
        limelight.start();
    }

    public void build(HardwareMap hardwareMap, boolean RedAlliance){
        limelight = hardwareMap.get(Limelight3A.class, "Limelight");
        if (RedAlliance){
            limelight.pipelineSwitch(redPipeline);
        }else{
            limelight.pipelineSwitch(bluePipeline);
        }

        limelight.start();
    }

    private ControlSystem controlSystem = ControlSystem.builder()
            .velPid(0.02, 0,0.02)
            .basicFF(0.00025, 0.001, 0.0015)
            .build();

    private ControlSystem controlSystemRotate = ControlSystem.builder()
            .posPid(0.001, 0,0.001)
            .build();

    // Commands

    public Command Speed(double speed){
        return new RunToVelocity(controlSystem, speed, 20).endAfter(1).requires(this);
    }
    public Command rotate(double pos){
        return new RunToPosition(controlSystemRotate, pos, 20).endAfter(1).requires(this);
    }
    public Command setAngle(double angle){
        return new SetPosition(hoodServo, angleToServo(angle)).requires(this);
    }
    public Command HoodDown(){
        return new SetPosition(hoodServo, 0.00).requires(this);
    }
    public Command HoodUp(){
        return new SetPosition(hoodServo, angleToServo(50)).requires(this);
    }
    public Command stop(){
        return new LambdaCommand()
                .setUpdate(() -> {
                    speed =0;
                    // Runs on update
                })
                .setIsDone(() -> true) // Returns if the command has finished
                .requires(/* subsystems the command implements */)
                .setInterruptible(true);
    }
    public Command increaseSpeed(){

        return new LambdaCommand()
                .setUpdate(() -> {
                    speed +=50;
                    // Runs on update
                })
                .setIsDone(() -> true) // Returns if the command has finished
                .requires(/* subsystems the command implements */)
                .setInterruptible(true);
    }
    public Command decreaseSpeed(){
        return new LambdaCommand()
                .setUpdate(() -> {
                    speed -= 50;
                    // Runs on update
                })
                .setIsDone(() -> true) // Returns if the command has finished
                .requires(/* subsystems the command implements */)
                .setInterruptible(true);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void periodic() {
        readAprilTag();
        LLResult result1 = limelight.getLatestResult();
        if (result1 != null) {
            double distance = getDistance(result1);
            if (result1.isValid()) {

            speed = distance * 6 + 775;
            turretPos = (int) (turretPos+ (1* getYawAprilTag(result1)));
            if (turretPos>maxTurret){
                turretPos = maxTurret;
            }
            if (turretPos< -maxTurret){
                turretPos = -maxTurret;
            }
            hoodServo.setPosition(angleToServo(-0.0034 * distance * distance + 0.8903 * distance - 6.8119));
            launcherMotor.setPower(controlSystem.calculate(launcherMotor.getState()));
        }
        }

    }
    //Telemetry Functions

    public double getServoAngle(){
        return (hoodServo.getPosition()/0.187)*55;
    }
    public double getMotorSpeed(){
        return launcherMotor.getVelocity();
    }
    public double getRotatePositionRaw(){
        return rotateMotor.getCurrentPosition();
    }

    private double angleToServo(double angle){
        if(angle > 50){
            angle = 50;
        }
        if(angle < 0){
            angle = 0;
        }
        angle = angle/55;
        return angle*0.187;
    }

    //LimeLight stuff


    private LLResult readAprilTag(){
        getResult();
        result = limelight.getLatestResult();
        return result;

    }
    public void getLimelightTelemetry(Telemetry telemetry){
        readAprilTag();
        if (result != null) {
            if (result.isValid()) {
                Pose3D botpose = result.getBotpose();
                telemetry.addData("tx", result.getTx());
                telemetry.addData("ty", result.getTy());
                telemetry.addData("Botpose", botpose.toString());
                telemetry.addData("tags", result.getBotposeTagCount());
                telemetry.addData("angle to goal", result.getTx());
                telemetry.addData("distance", getDistance(result));
            }
        }
    }
    public void getLimelightTelemetry(TelemetryManager telemetry){
        readAprilTag();
        if (result != null) {
            if (result.isValid()) {
                Pose3D botpose = result.getBotpose();
                telemetry.addData("tx", result.getTx());
                telemetry.addData("ty", result.getTy());
                telemetry.addData("Botpose", botpose.toString());
                telemetry.addData("tags", result.getBotposeTagCount());
                telemetry.addData("angle to goal", result.getTx());
                telemetry.addData("distance", getDistance(result));
            }
        }
    }
    private void getResult(){
        result = limelight.getLatestResult();
    }
    public void setPipeline(int pipeline){
        limelight.stop();
        limelight.pipelineSwitch(pipeline);
        limelight.start();
    }

    public double getDistance(LLResult result){
        double targetOffsetAngle_Vertical = result.getTy();

// how many degrees back is your limelight rotated from perfectly vertical?
        double limelightMountAngleDegrees = 25.0;

        // distance from the center of the Limelight lens to the floor
        double limelightLensHeightInches = 14.370079;

        // distance from the target to the floor
        double goalHeightInches = 29.5;

        double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
        double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);

        //calculate distance
        return (goalHeightInches - limelightLensHeightInches) / Math.tan(angleToGoalRadians);
    }
    public double getYawAprilTag(LLResult result){
        return result.getTx();

    }
}
