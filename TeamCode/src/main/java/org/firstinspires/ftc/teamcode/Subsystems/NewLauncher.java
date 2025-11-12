package org.firstinspires.ftc.teamcode.Subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

@Configurable
public class NewLauncher implements Subsystem {
    public static final NewLauncher INSTANCE = new NewLauncher();
    private NewLauncher (){}
    //Turret Yaw Proportional gain
    private double tKP = 2.5;

    private int maxTurret = 600;

    private int redPipeline = 0;
    private int bluePipeline = 1;
    private final int startingPipeline = 0;

    private Limelight3A limelight;
    public MotorEx launcherMotor = new MotorEx("LauncherMotor");
    public MotorEx turretMotor = new MotorEx("TurretMotor");
    public ServoEx hoodServo = new ServoEx("HoodServo");

    // Passes through Hardware map
    public void build(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "Limelight");
        limelight.pipelineSwitch(startingPipeline);
        limelight.start();
        turretMotor.getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void build(HardwareMap hardwareMap, boolean RedAlliance){
        limelight = hardwareMap.get(Limelight3A.class, "Limelight");
        if (RedAlliance){
            limelight.pipelineSwitch(redPipeline);
        }else{
            limelight.pipelineSwitch(bluePipeline);
        }
        limelight.start();
        turretMotor.getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }

    //Control Systems
    private ControlSystem launcherControlSystem = ControlSystem.builder()
            .velPid(0.02, 0,0.02)
            .basicFF(0.00025, 0.001, 0.0015)
            .build();
    private ControlSystem turretControlSystem = ControlSystem.builder()
            .posPid(0.03, 0,0.005)
            .build();

    // Internal running commands
    private Command SpinUpToSpeed(double speed) {
        return new RunToVelocity(launcherControlSystem, speed, 20).endAfter(1);
    }

    private Command rotateToPos(double pos) {
        return new RunToPosition(turretControlSystem, pos, 1);
    }

    //Hood Commands
    public Command HoodDown(){
        return new SetPosition(hoodServo, 0.00).requires(this);
    }
    public Command HoodUp(){
        return new SetPosition(hoodServo, angleToServo(50)).requires(this);
    }
    public Command HoodToAngle(double angle){
        hoodServo.setPosition(angleToServo(angle));
        return new NullCommand();
//        return new SetPosition(hoodServo, angleToServo(angle)).endAfter(1);
    }


    public Command stop(){
        return SpinUpToSpeed(0);
    }

    // Return functions
    public double getServoAngle(){
        return (hoodServo.getPosition()/0.187)*55;
    }
    public double getMotorSpeed(){
        return launcherMotor.getVelocity();
    }
    public double getRotatePositionRaw(){

        return turretMotor.getCurrentPosition();
    }
    //Math Functions
    private double angleToServo(double angle){  //from telemetry functions
        if(angle > 50){
            angle = 50;
        }
        if(angle < 0){
            angle = 0;
        }
        angle = angle/55;
        return angle*0.187;
    }

    private double yawToRotatePos(double yaw){
        double pos = turretMotor.getCurrentPosition()+ yaw*tKP;
        if (pos> maxTurret){
            pos = maxTurret;
        }
        if (pos< -maxTurret){
            pos = -maxTurret;
        }
        return pos;
    }

    private double distanceToSpeed(double distance){
        return  ((distance * 6) + 775);
    }

    private double distanceToHoodAngle(double distance){
        return (-0.0034 * distance * distance + 0.8903 * distance - 6.8119);
    }



    //LimeLight functions
    private LLResult readAprilTag(){
        return limelight.getLatestResult();
    }

    public void setPipeline(int pipeline){
        limelight.stop();
        limelight.pipelineSwitch(pipeline);
        limelight.start();
    }

    public double getDistance(LLResult result){

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



    //Telemetry
    public void getLauncherTelemetry(Telemetry telemetry){
        telemetry.addData("Launcher Speed:", launcherMotor.getVelocity());
        telemetry.addData("Hood Angle", (hoodServo.getPosition()/0.187)*55);
        telemetry.addData("Rotate Position Raw", turretMotor.getCurrentPosition());
        LLResult result = readAprilTag();
        if (result != null) {
            if (result.isValid()) {
                telemetry.addData("Yaw to Goal", result.getTx());
                telemetry.addData("Pitch to Goal", result.getTy());
                telemetry.addData("distance", getDistance(result));
            }
        }
    }
    public void getLauncherTelemetry(TelemetryManager telemetry){
        telemetry.addData("Launcher Speed:", launcherMotor.getVelocity());
        telemetry.addData("Hood Angle", (hoodServo.getPosition()/0.187)*55);
        telemetry.addData("Rotate Position Raw", turretMotor.getCurrentPosition());
        LLResult result = readAprilTag();
        if (result != null) {
            if (result.isValid()) {
                telemetry.addData("Yaw to Goal", result.getTx());
                telemetry.addData("Pitch to Goal", result.getTy());
                telemetry.addData("distance", getDistance(result));
            }
        }
    }
    public Command runLauncherFromAprilTag(){
        LLResult result = limelight.getLatestResult();
        if (result != null) {
            if (result.isValid()) {
                double distance = getDistance(result);
                double yaw = getYawToGoal(result);
                return new ParallelGroup(
                        SpinUpToSpeed(distanceToSpeed(distance)),
                        rotateToPos(yawToRotatePos(yaw)),
                        HoodToAngle(distanceToHoodAngle(distance))
                );
            }else{
                return new NullCommand();
            }
        }else{
            return new NullCommand();
        }

    }
    @Override
    public void initialize() {

    }

    @Override
    public void periodic() {
//        launcherMotor.setPower(launcherControlSystem.calculate(launcherMotor.getState()));
        turretMotor.setPower(turretControlSystem.calculate(turretMotor.getState()));

    }
}
