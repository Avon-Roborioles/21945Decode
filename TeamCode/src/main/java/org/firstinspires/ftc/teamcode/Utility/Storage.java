package org.firstinspires.ftc.teamcode.Utility;

import static java.lang.Math.PI;
import com.pedropathing.geometry.Pose;
import dev.nextftc.bindings.Button;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;

public abstract class Storage  extends NextFTCOpMode {
    Button d_up, d_down, d_left, d_right, a;
    double currentMenuY = 0;
    double currentMenuX = 1;
    double menuMaxY = 2;
    double menuMaxX = 3;
    double menuMinY = 0;
    double menuMinX = 1;
    String currentSet = null;
    public static class memory {
        public static Pose lastPose = new Pose(72, 72, Math.toDegrees(270));
    }

    public void initPoseSelect(){
        d_up = Gamepads.gamepad1().dpadUp();
        d_down = Gamepads.gamepad1().dpadDown();
        d_left = Gamepads.gamepad1().dpadLeft();
        d_right = Gamepads.gamepad1().dpadRight();
        a = Gamepads.gamepad1().a();
    }

    public void runPoseSelect(){
        telemetry.clear();
        d_up.whenBecomesTrue(()->{currentMenuY++;});
        d_down.whenBecomesTrue(()->{currentMenuY--;});
        d_right.whenBecomesTrue(()->{currentMenuX++;});
        d_left.whenBecomesTrue(()->{currentMenuX--;});

//        if(d_up.whenBecomesTrue()){
//            currentMenuY++;
//        }else if(d_down.get()){
//            currentMenuY--;
//        }
//        if(d_left.get()){
//            currentMenuX--;
//        }else if(d_right.get()){
//            currentMenuX++;
//        }
        if(currentMenuY > menuMaxY){
            currentMenuY = menuMinY;
        }else if(currentMenuY < menuMinY){
            currentMenuY = menuMaxY;
        }
        if(currentMenuX > menuMaxX){
            currentMenuX = menuMinX;
        }else if(currentMenuX < menuMinX){
            currentMenuX = menuMaxX;
        }


        switch ((int) currentMenuY){
            case 0:
                telemetry.addLine("Option: Use Current Pose");
                telemetry.addData("Current Set", currentSet);
                telemetry.addData("Y", currentMenuY);
                telemetry.addData("X", currentMenuX);
                break;
            case 1:
                if(currentMenuX == 1){
                    if (a.get()){
                       memory.lastPose = new Pose(72,72, Math.toDegrees(270));
                       currentSet = "Center Field With Intake Toward Audience";
                    }
                    telemetry.addLine("Center Field With Intake Toward Audience");
                } else if(currentMenuX == 2) {
                    if (a.get()){
                        memory.lastPose = new Pose(62,10.25,(3*Math.PI)/2);
                        currentSet = "Blue Goal Side Small Triangle";
                    }
                    telemetry.addLine("Blue Goal Side Small Triangle");
                }else {
                    if (a.get()){
                        memory.lastPose = new Pose(133,9,Math.toDegrees(270));
                        currentSet = "Blue Human Player Zone Intake Towards Audience";
                    }
                    telemetry.addLine("Blue Human Player Zone Intake Towards Audience");

                }
                telemetry.addData("Current Set", currentSet);
                telemetry.addData("Y", currentMenuY);
                telemetry.addData("X", currentMenuX);
                break;
            case 2:
                if(currentMenuX == 1){
                    if (a.get()){
                        memory.lastPose = new Pose(72,72, Math.toDegrees(270));
                        currentSet = "Center Field With Intake Toward Audience";
                    }
                    telemetry.addLine("Center Field With Intake Toward Audience");
                } else if(currentMenuX == 2) {
                    if (a.get()){
                        memory.lastPose = new Pose(62,10.25,(3*Math.PI)/2);
                        currentSet = "Red Goal Side Small Triangle";
                    }
                    telemetry.addLine("Red Goal Side Small Triangle");
                } else {
                    if (a.get()){
                        memory.lastPose = new Pose(9,9,Math.toDegrees(270));
                        currentSet = "Red Human Player Zone Intake Towards Audience";
                    }
                telemetry.addLine("Red Human Player Zone Intake Towards Audience");

            }
                telemetry.addData("Current Set", currentSet);
                telemetry.addData("Y", currentMenuY);
                telemetry.addData("X", currentMenuX);
                break;

        }



    }




}