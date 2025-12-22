package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;

import dev.nextftc.core.subsystems.Subsystem;

public class StatusSubsystem implements Subsystem {
    public static final StatusSubsystem INSTANCE = new StatusSubsystem();
    private StatusSubsystem() {}

    private AnalogInput floodgate ;
    @Override
    public void initialize() {


    }
}
