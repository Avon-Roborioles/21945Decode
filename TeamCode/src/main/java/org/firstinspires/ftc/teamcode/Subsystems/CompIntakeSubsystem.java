package org.firstinspires.ftc.teamcode.Subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class CompIntakeSubsystem implements Subsystem {
    public static final CompIntakeSubsystem INSTANCE = new CompIntakeSubsystem();
    private CompIntakeSubsystem() {}

    //Variables
    private double intakeSpeed = 1;

    // put hardware, commands, etc here
    public MotorEx intakeMotor = new MotorEx("Intake Motor");

    public Command intake = new SetPower(intakeMotor, intakeSpeed);
    public Command outtake = new SetPower(intakeMotor, -intakeSpeed);
    public Command stop = new SetPower(intakeMotor, 0);


    @Override
    public void initialize() {
        // initialization logic (runs on init)

        intakeMotor.setPower(0);
    }

    @Override
    public void periodic() {
        // periodic logic (runs every loop)
    }
}
