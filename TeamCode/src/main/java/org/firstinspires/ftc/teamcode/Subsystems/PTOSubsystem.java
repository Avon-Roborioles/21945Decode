package org.firstinspires.ftc.teamcode.Subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class PTOSubsystem implements Subsystem {
    public static final PTOSubsystem INSTANCE = new PTOSubsystem();
    private PTOSubsystem() {}
    double lUp = 0.85;
    double lDown = 0.05;
    double rUp = 0.85;
    double rDown = 0.05;
    boolean engaged = false;
    // put hardware, commands, etc here
    public ServoEx ptoL = new ServoEx("PTO L");
    public ServoEx ptoR = new ServoEx("PTO R");

    public Command disengage = new ParallelGroup(new SetPosition(ptoL, lUp), new SetPosition(ptoR, rUp), new LambdaCommand().setStart(() -> engaged = false));
    public Command engage = new ParallelGroup(new SetPosition(ptoL, lDown), new SetPosition(ptoR, rDown), new LambdaCommand().setStart(() -> engaged = true));


    @Override
    public void initialize() {
        engaged = false;
        // initialization logic (runs on init)
    }

    @Override
    public void periodic() {
        // periodic logic (runs every loop)
//        getPTOTelemetryAdv();
    }
    public void getPTOTelemetryAdv(){
        ActiveOpMode.telemetry().addLine("-------------- PTO Telemetry Adv: --------------");
        ActiveOpMode.telemetry().addData("Engaged", engaged);
        ActiveOpMode.telemetry().addData("PTO L Position", ptoL.getPosition());
        ActiveOpMode.telemetry().addData("PTO R Position", ptoR.getPosition());


    }
}
