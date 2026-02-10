package org.firstinspires.ftc.teamcode.Commands;

import com.pedropathing.geometry.Pose;

import dev.nextftc.core.commands.Command;
import dev.nextftc.extensions.pedro.PedroComponent;

public class HumanPlayerReset extends Command {
    boolean redAlliance;
    public HumanPlayerReset(Boolean redAlliance) {
        this.redAlliance = redAlliance;
        requires();
        setInterruptible(true);
    }
    @Override
    public boolean isDone() {
        return true; // whether or not the command is done
    }

    @Override
    public void start() {
        if(redAlliance){
            PedroComponent.follower().setPose(new Pose(9.5,10.25,Math.toRadians(270)));
        }else{
            PedroComponent.follower().setPose(new Pose(133.5,10.25,Math.toRadians(270)));
        }


        // executed when the command begins
    }

    @Override
    public void update() {



        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {

        // executed when the command ends
    }

}
