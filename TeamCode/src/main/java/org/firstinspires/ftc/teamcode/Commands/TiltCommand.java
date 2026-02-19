package org.firstinspires.ftc.teamcode.Commands;

import org.firstinspires.ftc.teamcode.Subsystems.PTOSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;

import dev.nextftc.bindings.Range;
import dev.nextftc.core.commands.Command;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;

public class TiltCommand extends Command {
    double leftPower;
    double rightPower;
    double power;
    Range input;
    double finalTarget = 100;
    double currTarget = 0;
    public static double KS = 0.05;



    public TiltCommand(Range input) {
        this.input = input;
    }
    @Override
    public boolean isDone() {
        return false; // whether or not the command is done
    }

    @Override
    public void start() {
        PTOSubsystem.INSTANCE.Engage();
        PedroComponent.follower().breakFollowing();
    }

    @Override
    public void update() {
        power = Math.abs(input.get());

        if(Math.abs(PTOSubsystem.INSTANCE.getPtoLPosition()- PTOSubsystem.INSTANCE.getPtoRPosition()) <5){
            currTarget +=5;
        }

        leftPower = (PTOSubsystem.INSTANCE.getPtoLPosition()-currTarget)*KS;
        rightPower = (PTOSubsystem.INSTANCE.getPtoRPosition()-currTarget)*KS;

        if(currTarget> finalTarget){
            currTarget = finalTarget;
        }





        PedroComponent.follower().getDrivetrain().runDrive(new double[]{leftPower*power, leftPower*power, rightPower*power, rightPower*power});

        ActiveOpMode.telemetry().addLine("-------------- PTO Joystick Command: --------------");
        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {
        PedroComponent.follower().getDrivetrain().runDrive(new double[]{0, 0,0,0});
        PedroComponent.follower().breakFollowing();
        PTOSubsystem.INSTANCE.Disengage();
        PedroComponent.follower().startTeleOpDrive();

        // executed when the command ends
    }
}

