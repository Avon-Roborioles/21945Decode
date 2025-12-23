package org.firstinspires.ftc.teamcode.Subsystems;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Utility.Timing;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.core.subsystems.SubsystemGroup;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;
import kotlin.system.TimingKt;

public class SorterSubsystem implements Subsystem {
    public static  final SorterSubsystem INSTANCE = new SorterSubsystem();
    private SorterSubsystem (){    }
    Timing.Timer wait = new Timing.Timer(250, TimeUnit.MILLISECONDS);
    Timing.Timer reset = new Timing.Timer(150, TimeUnit.MILLISECONDS);
    public ServoEx servoOne = new ServoEx("Sort L");
    public ServoEx servoTwo = new ServoEx("Sort C");
    public ServoEx servoThree = new ServoEx("Sort R");


    public Command ejectOne = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                servoOne.setPosition(0.45);
                wait.start();
            })
            .setUpdate(() -> {
                // Runs on update
            })
            .setStop(interrupted -> {
                servoOne.setPosition(0);
                // Runs on stop
            })
            .setIsDone(() -> wait.done()) // Returns if the command has finished
            .requires()
            .setInterruptible(false);

    public Command ejectTwo =

            new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                servoTwo.setPosition(0);
                wait.start();
            })
            .setUpdate(() -> {
                // Runs on update
            })
            .setStop(interrupted -> {
                servoTwo.setPosition(0.45);
                // Runs on stop
            })
            .setIsDone(() -> wait.done()) // Returns if the command has finished
            .requires()
            .setInterruptible(false);

    public Command ejectThree = new LambdaCommand()
            .setStart(() -> {
                // Runs on start

                servoThree.setPosition(0.45);
                wait.start();
            })
            .setUpdate(() -> {
                // Runs on update
            })
            .setStop(interrupted -> {
                servoThree.setPosition(0);
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


}
