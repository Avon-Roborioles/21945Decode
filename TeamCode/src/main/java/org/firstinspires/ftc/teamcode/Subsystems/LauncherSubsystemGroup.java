package org.firstinspires.ftc.teamcode.Subsystems;

import dev.nextftc.core.subsystems.SubsystemGroup;

public class LauncherSubsystemGroup extends SubsystemGroup {
    public static final LauncherSubsystemGroup INSTANCE = new LauncherSubsystemGroup();

    private LauncherSubsystemGroup(){
        super(
             LauncherSubsystem.INSTANCE,
             TurretSubsystem.INSTANCE
        );
    }
}
