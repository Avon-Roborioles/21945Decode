package org.firstinspires.ftc.teamcode.Utility;

import static java.lang.Math.PI;
import com.pedropathing.geometry.Pose;
import dev.nextftc.bindings.Button;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;

public abstract class Storage  extends NextFTCOpMode {
    Button  a;
    double currentMenuY = 0;
    double currentMenuX = 1;
    double menuMaxY = 2;
    double menuMaxX = 3;
    double menuMinY = 0;
    double menuMinX = 1;
    String currentSet = null;
    Pose testPose;


    public void initPoseSelect(){
        testPose = PedroComponent.follower().poseTracker.getPose();

        a = Gamepads.gamepad1().a();
    }

    public void runPoseSelect(){
        telemetry.clear();
        if(Gamepads.gamepad1().dpadUp().get()){
            currentMenuY++;
        }
        if(Gamepads.gamepad1().dpadDown().get()){
            currentMenuY--;
        }
        if(Gamepads.gamepad1().dpadLeft().get()){
            currentMenuX++;
        }
        if (Gamepads.gamepad1().dpadRight().get()){
            currentMenuX--;
        }


        if(currentMenuY > menuMaxY){
            currentMenuY = menuMaxY;
        }else if(currentMenuY < menuMinY){
            currentMenuY = menuMinY;
        }
        if(currentMenuX > menuMaxX){
            currentMenuX = menuMaxX;
        }else if(currentMenuX < menuMinX){
            currentMenuX = menuMinX;
        }


        switch ((int) currentMenuY){
            case 0:
                telemetry.addLine("Option: Use Current Pose");
                break;
            case 1:
                if(currentMenuX == 1){
                    if (a.get()){
                        PosStorage.memory.lastPose = new Pose(72,72, Math.toRadians(270));
                       currentSet = "Center Field With Intake Toward Audience";
                    }
                    telemetry.addLine("Center Field With Intake Toward Audience");
                } else if(currentMenuX == 2) {
                    if (a.get()){
                        PosStorage.memory.lastPose = new Pose(62,10.25,(3*Math.PI)/2);
                        currentSet = "Blue Goal Side Small Triangle";
                    }
                    telemetry.addLine("Blue Goal Side Small Triangle");
                }else {
                    if (a.get()){
                        PosStorage.memory.lastPose = new Pose(133.5,10.25,Math.toRadians(270));
                        currentSet = "Blue Human Player Zone Intake Towards Audience";
                    }
                    telemetry.addLine("Blue Human Player Zone Intake Towards Audience");

                }

                break;
            case 2:
                if(currentMenuX == 1){
                    if (a.get()){
                        PosStorage.memory.lastPose = new Pose(72,72, Math.toRadians(270));
                        currentSet = "Center Field With Intake Toward Audience";
                    }
                    telemetry.addLine("Center Field With Intake Toward Audience");
                } else if(currentMenuX == 2) {
                    if (a.get()){
                        PosStorage.memory.lastPose = new Pose(62,10.25,(3*Math.PI)/2).mirror();
                        currentSet = "Red Goal Side Small Triangle";
                    }
                    telemetry.addLine("Red Goal Side Small Triangle");
                } else {
                    if (a.get()){
                        PosStorage.memory.lastPose = new Pose(9.5,10.25,Math.toRadians(270));
                        currentSet = "Red Human Player Zone Intake Towards Audience";
                    }
                telemetry.addLine("Red Human Player Zone Intake Towards Audience");

            }
                break;

        }
        telemetry.addData("Current Set", currentSet);
        telemetry.addData("Y", currentMenuY);
        telemetry.addData("X", currentMenuX);
        telemetry.addData("lastPose",  PosStorage.memory.lastPose);
        telemetry.addData("test Pose", testPose);
        PedroComponent.follower().setPose( PosStorage.memory.lastPose);
        PedroComponent.follower().setHeading( PosStorage.memory.lastPose.getHeading());
        telemetry.addData("BotPose", PedroComponent.follower().getPose());
        telemetry.update();



    }




}