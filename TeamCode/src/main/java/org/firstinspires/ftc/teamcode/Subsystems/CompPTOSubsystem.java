package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.Servo;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class CompPTOSubsystem implements Subsystem {
    public static final CompPTOSubsystem INSTANCE = new CompPTOSubsystem();
    private CompPTOSubsystem() {}
    double lUp = 0.85;
    double lDown = 0.05;
    double rUp = 0.85;
    double rDown = 0.05;
    // put hardware, commands, etc here
    public ServoEx ptoL = new ServoEx("PTO L");
    public ServoEx ptoR = new ServoEx("PTO R");

    public Command disengage = new ParallelGroup(new SetPosition(ptoL, lUp), new SetPosition(ptoR, rUp));
    public Command engage = new ParallelGroup(new SetPosition(ptoL, lDown), new SetPosition(ptoR, rDown));


    @Override
    public void initialize() {
        // initialization logic (runs on init)
    }

    @Override
    public void periodic() {
        // periodic logic (runs every loop)
    }
}
