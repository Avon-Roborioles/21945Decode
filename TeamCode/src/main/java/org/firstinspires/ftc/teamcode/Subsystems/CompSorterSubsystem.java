package org.firstinspires.ftc.teamcode.Subsystems;

import org.firstinspires.ftc.teamcode.Utility.Timing;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class CompSorterSubsystem implements Subsystem {
    public static final CompSorterSubsystem INSTANCE = new CompSorterSubsystem();
    private CompSorterSubsystem() {}

    private double lUp = 0.5;
    private double lDown = 0;
    private double cUp = 0.5;
    private double cDown = 0;
    private double rUp = 0.535;
    private double rDown = 0.035;

    Timing.Timer wait = new Timing.Timer(250, TimeUnit.MILLISECONDS);
    Timing.Timer reset = new Timing.Timer(200, TimeUnit.MILLISECONDS);

    public ServoEx sortL = new ServoEx("Sort L");
    public ServoEx sortC = new ServoEx("Sort C");
    public ServoEx sortR = new ServoEx("Sort R");


    public Command ejectL = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                sortL.setPosition(lUp);
                wait.start();
            })
            .setUpdate(() -> {
                // Runs on update
            })
            .setStop(interrupted -> {
                sortL.setPosition(lDown);
                // Runs on stop
            })
            .setIsDone(() -> wait.done()) // Returns if the command has finished
            .requires()
            .setInterruptible(false);

    public Command ejectC =

            new LambdaCommand()
                    .setStart(() -> {
                        // Runs on start
                        sortC.setPosition(cUp);
                        wait.start();
                    })
                    .setUpdate(() -> {
                        // Runs on update
                    })
                    .setStop(interrupted -> {
                        sortC.setPosition(cDown);
                        // Runs on stop
                    })
                    .setIsDone(() -> wait.done()) // Returns if the command has finished
                    .requires()
                    .setInterruptible(false);

    public Command ejectR = new LambdaCommand()
            .setStart(() -> {
                // Runs on start

                sortR.setPosition(rUp);
                wait.start();
            })
            .setUpdate(() -> {
                // Runs on update
            })
            .setStop(interrupted -> {
                sortR.setPosition(rDown);
                // Runs on stop
            })
            .setIsDone(() -> wait.done()) // Returns if the command has finished
            .requires()
            .setInterruptible(false);

    public Command resetSorter = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                reset.start();
            })
            .setUpdate(() -> {
                // Runs on update
            })
            .setStop(interrupted -> {
                // Runs on stop
            })
            .setIsDone(() -> reset.done()) // Returns if the command has finished
            .requires()
            .setInterruptible(false);
    // put hardware, commands, etc here

    @Override
    public void initialize() {
        // initialization logic (runs on init)
    }

    @Override
    public void periodic() {
        // periodic logic (runs every loop)
    }
}
