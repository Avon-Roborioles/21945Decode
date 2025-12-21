package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class TempIntake implements Subsystem {
    public static final TempIntake INSTANCE = new TempIntake();
    private TempIntake() {}
    double speed = 1;


    private MotorEx intakeMotor = new MotorEx("intake");

    @Override
    public void initialize() {
    }
    public Command intake(){
        return new LambdaCommand()
                .setUpdate(() -> {
                    intakeMotor.setPower(speed);
                    // Runs on update
                })
                .setIsDone(() -> false) // Returns if the command has finished
                .requires(TempIntake.INSTANCE/* subsystems the command implements */)
                .setInterruptible(true);

    }
    public Command outtake(){
        return new LambdaCommand()
                .setUpdate(() -> {
                    intakeMotor.setPower(-speed);
                    // Runs on update
                })
                .setIsDone(() -> false) // Returns if the command has finished
                .requires(TempIntake.INSTANCE/* subsystems the command implements */)
                .setInterruptible(true);
    }
    public Command stop() {
        return new LambdaCommand()
                .setUpdate(() -> {
                    intakeMotor.setPower(0);
                    // Runs on update
                })
                .setIsDone(() -> false) // Returns if the command has finished
                .requires(TempIntake.INSTANCE)
                .setInterruptible(true);
    }
}
