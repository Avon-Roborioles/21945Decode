package org.firstinspires.ftc.teamcode.Subsystems;

import com.bylazar.telemetry.TelemetryManager;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class CompLauncherSubsystem implements Subsystem {
    public static final CompLauncherSubsystem INSTANCE = new CompLauncherSubsystem();
    private CompLauncherSubsystem() {}

    // Hardware
    public MotorGroup launcherMotorGroup = new MotorGroup(
            new MotorEx("Launch Motor 1"),
            new MotorEx("Launch Motor 2")
    );

    public ServoEx hoodServo = new ServoEx("Hood");

    private ControlSystem launcherControlSystem = ControlSystem.builder()
            .velPid(0.01, 0,0.0001)
            .build();

    // Variables
    double speedTarget = 0;
    double hoodAngleTarget = 0;
    double maxHoodAngle = 45;
    double maxHoodPWM = 0.97;
    double maxSpeed = 1700;



    //Internal Commands
    public Command SpinUpToSpeed(double speed) {
        return new LambdaCommand().setStart(() -> {
                    // Runs on start
                    speedTarget=speed;
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
    }

    public Command HoodToAngle(double angle){
        return new LambdaCommand().setStart(() -> {
                    // Runs on start
                    hoodAngleTarget = angle;
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
    }

    //Basic Functions
    public double getTargetSpeed(){
        return speedTarget;
    }

    public double getServoAngle(){
        return (hoodServo.getPosition()/maxHoodPWM)*maxHoodAngle;
    }

    public double getMotorSpeed(){
        return launcherMotorGroup.getVelocity();
    }


    //Basic Commands
    public Command HoodDown(){
        return new SetPosition(hoodServo, 0.00).requires(hoodServo);
    }


    public Command SpeedUp = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                speedTarget = speedTarget + 50;
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

    public Command SpeedDown = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                speedTarget = speedTarget - 50;
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


    public Command StopLauncher = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                speedTarget = 0;
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

    public Command HoodPlus = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                hoodAngleTarget = hoodAngleTarget + 1;
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

    public Command HoodMinus = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                hoodAngleTarget = hoodAngleTarget - 1;
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


    //Math Functions
    private double angleToServo(double angle){
        if (angle> maxHoodAngle){
            angle = maxHoodAngle;
        }else if (angle< 0){
            angle = 0;
        }
        return (angle/ maxHoodAngle)*maxHoodPWM;
    }
    private double distanceToSpeed(double distance){
        return distance;
    }
    private double distanceToHoodAngle(double distance){
        return distance;
    }

    //Telemetry
    public void getLauncherTelemetry(TelemetryManager telemetry){
        telemetry.addData("Launcher Speed", getMotorSpeed());
        telemetry.addData("Launcher Speed Target", getTargetSpeed());
        telemetry.addData("Hood Angle", getServoAngle());
        telemetry.addData("Hood Angle Target", hoodAngleTarget);
        telemetry.addData("Hood PWM", hoodServo.getPosition());

    }


    @Override
    public void initialize() {
        // initialization logic (runs on init)
        launcherControlSystem.setGoal(new KineticState(0,0));
        speedTarget = 0;
        hoodAngleTarget = (hoodServo.getPosition()/maxHoodPWM)*maxHoodAngle;
    }

    @Override
    public void periodic() {
        // Periodic logic (runs every loop)
        if (speedTarget>maxSpeed){
            speedTarget = maxSpeed;
        }else if (speedTarget<0){
            speedTarget = 0;
        }
        if (hoodAngleTarget>maxHoodAngle){
            hoodAngleTarget = maxHoodAngle;
        }else if (hoodAngleTarget<0){
            hoodAngleTarget = 0;
        }
        if (!(speedTarget<250)){
            launcherControlSystem.setGoal(new KineticState(0, speedTarget));
            launcherMotorGroup.setPower(launcherControlSystem.calculate(launcherMotorGroup.getState()));
        }else {
            launcherMotorGroup.setPower(0);
        }

        hoodServo.setPosition(angleToServo(hoodAngleTarget));
        launcherControlSystem.isWithinTolerance(new KineticState(0,20));
    }


}
