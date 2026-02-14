package org.firstinspires.ftc.teamcode.Commands;

import org.firstinspires.ftc.teamcode.Subsystems.VisionSubsystem;

import dev.nextftc.core.commands.Command;

public class ReLocalizeWithLLCommand extends Command {
    public ReLocalizeWithLLCommand() {
        requires();
        setInterruptible(true);
    }
    @Override
    public boolean isDone() {
        return false; // whether or not the command is done
    }

    @Override
    public void start() {
        VisionSubsystem.INSTANCE.stopLL();
        VisionSubsystem.INSTANCE.setReLocalizingPipeline();


        // executed when the command begins
    }

    @Override
    public void update() {
        VisionSubsystem.INSTANCE.reLocalizeWithLimeLight();


        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {

        // executed when the command ends
    }

}
