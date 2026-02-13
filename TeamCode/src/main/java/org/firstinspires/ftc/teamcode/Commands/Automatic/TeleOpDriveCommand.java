package org.firstinspires.ftc.teamcode.Commands.Automatic;

import com.pedropathing.geometry.Pose;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import dev.nextftc.bindings.Button;
import dev.nextftc.bindings.Range;
import dev.nextftc.core.commands.Command;
import dev.nextftc.extensions.pedro.PedroComponent;

public class TeleOpDriveCommand extends Command {
    Range drivePower, strafePower, turnPower;
    Button hold;
    boolean holdingPoint;
    Pose HoldPose;


    public TeleOpDriveCommand(Range drivePower, Range strafePower, Range turnPower, Button hold ){
        this.hold = hold;
        this.drivePower = drivePower;
        this.strafePower = strafePower;
        this.turnPower = turnPower;
        addRequirements();
        setInterruptible(true);
    }
    @Override
    public boolean isDone() {
        return false; // whether or not the command is done
    }

    @Override
    public void start() {
        PedroComponent.follower().startTeleOpDrive();


        // executed when the command begins
    }

    @Override
    public void update() {
//        -

        if(hold.get()){
            if(!holdingPoint){
                HoldPose = PedroComponent.follower().getPose();
                PedroComponent.follower().holdPoint(HoldPose);
                holdingPoint = true;
            }

        }else {
            if(holdingPoint){
                PedroComponent.follower().breakFollowing();
                PedroComponent.follower().startTeleOpDrive();
                holdingPoint = false;
            }

            PedroComponent.follower().setTeleOpDrive(drivePower.get(), strafePower.get(), turnPower.get(), false);
        }
        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {
        PedroComponent.follower().breakFollowing();


        // executed when the command ends
    }
}
