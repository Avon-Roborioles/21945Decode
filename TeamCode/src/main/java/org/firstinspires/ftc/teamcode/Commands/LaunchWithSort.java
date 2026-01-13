package org.firstinspires.ftc.teamcode.Commands;

import org.firstinspires.ftc.teamcode.Subsystems.CompStatusSubsystem;
import org.firstinspires.ftc.teamcode.Utility.Timing;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;
import dev.nextftc.ftc.ActiveOpMode;

public class LaunchWithSort extends Command {
    enum Step{
        LaunchCenter,
        WaitCenter,
        ResetCenter,
        LaunchLeft,
        WaitLeft,
        ResetLeft,
        LaunchRight,
        ResetRight,
        WaitRight,
        Pause,
        CheckForMiss,
        Done

    }

    enum order {
        Center,
        Left,
        Right
    }

    Step St = Step.LaunchCenter;
    Timing.Timer wait = new Timing.Timer(250, TimeUnit.MILLISECONDS);
    Timing.Timer reset = new Timing.Timer(200, TimeUnit.MILLISECONDS);
    Timing.Timer delay = new Timing.Timer(1000, TimeUnit.MILLISECONDS);
    order first, second, third;

    public LaunchWithSort() {
        requires(/* subsystems */);
        setInterruptible(true); // this is the default, so you don't need to specify
    }

    @Override
    public boolean isDone() {
        return St == Step.Done; // whether or not the command is done
    }

    @Override
    public void start() {
        St = Step.LaunchCenter;
        // executed when the command begins
        switch (CompStatusSubsystem.INSTANCE.getCurrentOBPattern()){
            case NULL:
                first = order.Center;
                second = order.Left;
                third = order.Right;
                break;
            case PPG:
                
                break;

        }
    }

    @Override
    public void update() {

//        switch (St) {
//            case LaunchCenter:
//
//                break;
//            case WaitCenter:
//
//                break;
//            case ResetCenter:
//
//                break;
//            case LaunchLeft:
//
//                break;
//            case WaitLeft:
//
//                break;
//            case ResetLeft:
//
//                break;
//            case LaunchRight:
//
//                break;
//            case WaitRight:
//
//                break;
//            case ResetRight:
//
//                break;
//            case Pause:
//
//                break;
//            case CheckForMiss:
//
//                break;
//        }
        // executed on every update of the command
        ActiveOpMode.telemetry().addData("Step", St);

    }

    @Override
    public void stop(boolean interrupted) {
        // executed when the command ends
    }
}
