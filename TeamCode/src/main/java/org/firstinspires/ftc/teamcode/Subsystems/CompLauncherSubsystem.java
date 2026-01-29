package org.firstinspires.ftc.teamcode.Subsystems;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.impl.VoltageCompensatingMotor;
import dev.nextftc.hardware.positionable.SetPosition;

public class CompLauncherSubsystem implements Subsystem {
    public static final CompLauncherSubsystem INSTANCE = new CompLauncherSubsystem();
    private CompLauncherSubsystem() {}


    // Hardware
    public MotorGroup launcherMotorGroup = new MotorGroup(
            new VoltageCompensatingMotor(new MotorEx("Launch Motor 2"), 0.5, 13),
            new VoltageCompensatingMotor(new MotorEx("Launch Motor 1"), 0.5, 13)
    );

    public ServoEx hoodServo = new ServoEx("Hood");

    private ControlSystem launcherControlSystem = ControlSystem.builder()
            .basicFF(0.00047,0,0)
            .velPid(0.0025, 0.0000000000,0)
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
        hoodAngleTarget = 5;
        return new SetPosition(hoodServo, angleToServo(hoodAngleTarget));
    }


    public Command SpeedUp = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                speedTarget += 25;
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
                speedTarget -= 25;
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
                hoodAngleTarget += 2.5;
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
                hoodAngleTarget -= 2.5;
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
        return (distance * 4.8092) + 653.24;
    }
    private double distanceToHoodAngle(double distance){
        return   (Math.pow(distance, 2) * -0.0027) + (distance * 0.8495) - 21.433;
    }

    public void RunLauncherFromDistance(double distance){
//        ActiveOpMode.telemetry().addLine("-------------- For Nick: --------------");
//        ActiveOpMode.telemetry().addData("Recommended Speed", distanceToSpeed(distance));
//        ActiveOpMode.telemetry().addData("Recommended Angle", distanceToHoodAngle(distance));
//        ActiveOpMode.telemetry().addData("Distance", distance);
//        ActiveOpMode.telemetry().addData("Current Angle", hoodAngleTarget);
//        ActiveOpMode.telemetry().addData("Current Speed", getMotorSpeed());


        speedTarget = distanceToSpeed(distance);
        hoodAngleTarget = distanceToHoodAngle(distance);
    }



    @Override
    public void initialize() {
        // initialization logic (runs on init)
        launcherControlSystem.setGoal(new KineticState(0,0));
        speedTarget = 0;
        hoodAngleTarget = (hoodServo.getPosition()/maxHoodPWM)*maxHoodAngle;
        //launcherControlSystem.isWithinTolerance(new KineticState(0, 10));
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
        //Needs Some Work for Loop Times Replace With Command
        if (ActiveOpMode.isStarted()) {
            if (!(speedTarget < 250)) {
                launcherControlSystem.setGoal(new KineticState(0, speedTarget));
                launcherMotorGroup.setPower(launcherControlSystem.calculate(launcherMotorGroup.getState()));
            } else {
                launcherMotorGroup.setPower(0);
            }

            hoodServo.setPosition(angleToServo(hoodAngleTarget));

        }
        getLauncherTelemetryAdv();
    }
    //Telemetry
    public void getLauncherTelemetryAdv(){
        PanelsTelemetry.INSTANCE.getTelemetry().addData("Launcher Speed", getMotorSpeed());
        PanelsTelemetry.INSTANCE.getTelemetry().addData("Launcher Speed Target", getTargetSpeed());

         ActiveOpMode.telemetry().addLine("-------------- Launcher Telemetry Adv: --------------");
        ActiveOpMode.telemetry().addData("Launcher Speed", getMotorSpeed());
        ActiveOpMode.telemetry().addData("Launcher Speed Target", getTargetSpeed());
        ActiveOpMode.telemetry().addData("Hood Angle", getServoAngle());
        ActiveOpMode.telemetry().addData("Hood Angle Target", hoodAngleTarget);
        ActiveOpMode.telemetry().addData("Hood PWM", hoodServo.getPosition());

    }



}
