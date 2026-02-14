package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DigitalChannel;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class IntakeSubsystem implements Subsystem {
    public static final IntakeSubsystem INSTANCE = new IntakeSubsystem();
    private IntakeSubsystem() {}

    //Variables
    private double intakeSpeed = -1;

    // put hardware, commands, etc here
    private DigitalChannel intakeBeamBreak, intakeBB2;
    public MotorEx intakeMotor = new MotorEx("Intake Motor");

    public Command Intake = new SetPower(intakeMotor, intakeSpeed);
    public Command Outtake = new SetPower(intakeMotor, -intakeSpeed);
    public Command StopIntake = new SetPower(intakeMotor, 0);

    public void intake(){
        intakeMotor.setPower(intakeSpeed);
    }
    public void outtake(){
        intakeMotor.setPower(-intakeSpeed);
    }
    public void stopIntake(){
        intakeMotor.setPower(0);
    }
    public boolean intakeBBTripped(){
        return intakeBeamBreak.getState();
    }
    public boolean intakeBB2Tripped(){
        return intakeBB2.getState();
    }



    @Override
    public void initialize() {
        // initialization logic (runs on init)
        intakeBeamBreak = ActiveOpMode.hardwareMap().get(DigitalChannel.class, "IntakeBB");
        intakeBB2 = ActiveOpMode.hardwareMap().get(DigitalChannel.class, "IntakeBB2");
        intakeBeamBreak.setMode(DigitalChannel.Mode.INPUT);
        intakeBB2.setMode(DigitalChannel.Mode.INPUT);

        intakeMotor.setPower(0);
    }

    @Override
    public void periodic() {
        // periodic logic (runs every loop)
//        getIntakeTelemetryAdv();

    }
    public void getIntakeTelemetryAdv(){
        ActiveOpMode.telemetry().addLine("-------------- Intake Telemetry Adv: --------------");
        ActiveOpMode.telemetry().addData("Intake Beam Break", intakeBBTripped());


    }
}
