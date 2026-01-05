package org.firstinspires.ftc.teamcode.Subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class CompIntakeSubsystem implements Subsystem {
    public static final CompIntakeSubsystem INSTANCE = new CompIntakeSubsystem();
    private CompIntakeSubsystem() {}

    //Variables
    private double intakeSpeed = -1;

    // put hardware, commands, etc here
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



    @Override
    public void initialize() {
        // initialization logic (runs on init)

        intakeMotor.setPower(0);
    }

    @Override
    public void periodic() {
        // periodic logic (runs every loop)
        getIntakeTelemetryAdv();
    }
    public void getIntakeTelemetryAdv(){
        ActiveOpMode.telemetry().addLine("-------------- Intake Telemetry Adv: --------------");

    }
}
