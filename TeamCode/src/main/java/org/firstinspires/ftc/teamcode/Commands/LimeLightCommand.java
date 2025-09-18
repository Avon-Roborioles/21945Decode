package org.firstinspires.ftc.teamcode.Commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.qualcomm.hardware.limelightvision.LLResult;

import org.firstinspires.ftc.teamcode.Subsystems.LimeLightSubsystem;

public class LimeLightCommand extends CommandBase {
    private LimeLightSubsystem limelightSubsystem;
    private LLResult lastResult;

    public LimeLightCommand(LimeLightSubsystem limelightSubsystem){
        this.limelightSubsystem = limelightSubsystem;
        addRequirements(limelightSubsystem);
    }
    @Override
    public void execute(){
        lastResult = limelightSubsystem.readAprilTag();
        limelightSubsystem.getLimelightTelemetry();
        limelightSubsystem.setPipeline(0);
    }
}
