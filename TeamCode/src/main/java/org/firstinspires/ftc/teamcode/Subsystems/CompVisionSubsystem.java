package org.firstinspires.ftc.teamcode.Subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class CompVisionSubsystem implements Subsystem {
    public static final CompVisionSubsystem INSTANCE = new CompVisionSubsystem();
    private CompVisionSubsystem() {}
    public ServoEx LLTilt = new ServoEx("LL Tilt");
    // put hardware, commands, etc here
    public Command down = new SetPosition(LLTilt, 0);
    public Command up = new SetPosition(LLTilt, 1);

    @Override
    public void initialize() {
        // initialization logic (runs on init)
    }

    @Override
    public void periodic() {
        // periodic logic (runs every loop)
    }
}
