package org.firstinspires.ftc.teamcode.Subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

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
public class NewLauncher implements Subsystem {
    public static final Launcher INSTANCE = new Launcher();

    private NewLauncher (){}

    public double speed = 0;
    private Limelight3A limelight;
    private int redPipeline = 0;
    private int bluePipeline = 1;
    private int maxTurret = 1600;
    private int turretPos = 0;
private LLResult result;
private final int startingPipeline = 0;

public MotorEx launcherMotor = new MotorEx("NewLauncherMotor");
public MotorEx rotateMotor = new MotorEx("NewRotateMotor");
public ServoEx hoodServo = new ServoEx("NewHoodServo");

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
    private ControlSystem newLauncherControlSystem = ControlSystem.builder()
            .velPid(0.02, 0,0.02)
            .basicFF(0.00025, 0.001, 0.0015)
            .build();
    private ControlSystem newLauncherControlSystemRotate = ControlSystem.builder()
            .posPid(0.001, 0,0.001)
            .build();

    public Command Speed(double speed) {
        return new RunToVelocity(newLauncherControlSystem, speed, 20).endAfter(1).requires(this);
    }

    public Command rotate(double pos) {
        return new RunToPosition(newLauncherControlSystemRotate, pos, 20).endAfter(1).requires(this);

    }

    public Command HoodDown(){ return new SetPosition(hoodServo, 0.00).requires(this);}
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
    public double getServoAngle(){
        return (hoodServo.getPosition()/0.187)*55;
    }
    public double getMotorSpeed(){
        return launcherMotor.getVelocity();
    }
    public double getRotatePositionRaw(){
        return rotateMotor.getCurrentPosition();
    }
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
}
