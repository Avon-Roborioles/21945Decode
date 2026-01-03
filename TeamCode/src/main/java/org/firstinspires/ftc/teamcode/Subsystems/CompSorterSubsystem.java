package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.rev.RevColorSensorV3;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Utility.Timing;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.ServoEx;

public class CompSorterSubsystem implements Subsystem {
    public static final CompSorterSubsystem INSTANCE = new CompSorterSubsystem();
    private CompSorterSubsystem() {}

    private double lUp = 0.5;
    private double lDown = 0;
    private double cUp = 0.5;
    private double cDown = 0.035;
    private double rUp = 0.535;
    private double rDown = 0.035;
    private boolean busy = false;

    Timing.Timer wait = new Timing.Timer(250, TimeUnit.MILLISECONDS);
    Timing.Timer reset = new Timing.Timer(200, TimeUnit.MILLISECONDS);

    public ServoEx sortL = new ServoEx("Sort L");
    public ServoEx sortC = new ServoEx("Sort C");
    public ServoEx sortR = new ServoEx("Sort R");
    public RevColorSensorV3 sortCSL;
    public RevColorSensorV3 sortCSR;
    public RevColorSensorV3 sortCSC;



    public Command ejectL = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                busy = true;
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
                        busy = true;
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
                busy = true;
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
                busy = false;
            })
            .setIsDone(() -> reset.done()) // Returns if the command has finished
            .requires()
            .setInterruptible(false);
    // put hardware, commands, etc here
    private boolean centerDetected(){
        return (sortCSC.getDistance(DistanceUnit.MM)<41.5);
    }
    private boolean leftDetected(){
        return (sortCSL.getDistance(DistanceUnit.MM)<43.75);
    }
    private boolean rightDetected(){
        return (sortCSR.getDistance(DistanceUnit.MM)<90);
    }


    @Override
    public void initialize() {
        // initialization logic (runs on init)
        sortCSL = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "Sort CS L");
        sortCSC = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "Sort CS C");
        sortCSR = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "Sort CS R");
        sortCSL.setGain(10);
        sortCSC.setGain(100);
        sortCSR.setGain(1);
        busy= false;
    }

    @Override
    public void periodic() {
        // periodic logic (runs every loop)
        getSorterTelemetryAdv();
    }
    public void getSorterTelemetryAdv(){
        ActiveOpMode.telemetry().addLine("-------------- Sorter Telemetry Adv: --------------");
        ActiveOpMode.telemetry().addData("Sorter Busy:", busy);
        ActiveOpMode.telemetry().addData("Sort CS L Distance MM:", sortCSL.getDistance(DistanceUnit.MM));
        ActiveOpMode.telemetry().addData("Sort CS C Distance MM:", sortCSC.getDistance(DistanceUnit.MM));
        ActiveOpMode.telemetry().addData("Sort CS R Distance MM:", sortCSR.getDistance(DistanceUnit.MM));
        ActiveOpMode.telemetry().addData("Sort L Detected", leftDetected());
        ActiveOpMode.telemetry().addData("Sort C Detected", centerDetected());
        ActiveOpMode.telemetry().addData("Sort R Detected", rightDetected());
        ActiveOpMode.telemetry().addData("Sort CS L [R,G,B]", "" + sortCSL.red() + ", " + sortCSL.green() + ", " + sortCSL.blue());
        ActiveOpMode.telemetry().addData("Sort CS C [R,G,B]", "" + sortCSC.red() + ", " + sortCSC.green() + ", " + sortCSC.blue());
        ActiveOpMode.telemetry().addData("Sort CS R [R,G,B]", "" + sortCSR.red() + ", " + sortCSR.green() + ", " + sortCSR.blue());



    }
}
