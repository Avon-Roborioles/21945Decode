//experimentation


package org.firstinspires.ftc.teamcode.Subsystems;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.OpModes.SorterTeleOp;

import java.util.concurrent.TimeUnit;

public abstract class Prism extends SorterTeleOp {
    public class Color {
        public int red;
        public int green;
        public int blue;
//variables
        public Color(int red, int green, int blue) {
            this.red = Math.min(red, 255);
            this.green = Math.min(green, 255);
            this.blue = Math.min(blue, 255);
        }

        @Override
        public String toString() {
            return String.format("%d, %d, %d", red, green, blue);
            new HardwareMap("Prism");
        }


            public class GoBildaPrismExample extends LinearOpMode {

                GoBildaPrismDriver prism;

                PrismAnimations.Solid solid;
                PrismAnimations.RainbowSnakes rainbowSnakes = new RainbowSnakes();

                public GoBildaPrismExample() {
                    solid = new PrismAnimations.Solid(Color.blue);
                }

                @Override
                public void runOpMode() {

                    prism = hardwareMap.get(GoBildaPrismDriver.class,"prism");


                    prism.setStripLength(32);
                    solid.setBrightness(50);
                    solid.setStartIndex(0);
                    solid.setStopIndex(12);

                    rainbowSnakes.setNumberOfSnakes(2);
                    rainbowSnakes.setSnakeLength(3);
                    rainbowSnakes.setSpacingBetween(6);
                    rainbowSnakes.setSpeed(0.5f);

                    telemetry.addData("Device ID: ", prism.getDeviceID());
                    telemetry.addData("Firmware Version: ", prism.getFirmwareVersionString());
                    telemetry.addData("Hardware Version: ", prism.getHardwareVersionString());
                    telemetry.addData("Power Cycle Count: ", prism.getPowerCycleCount());
                    telemetry.update();

                    waitForStart();
                    resetRuntime();

                    while (opModeIsActive()) {


                        if(gamepad1.aWasPressed()){

                            prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, solid);
                            prism.insertAndUpdateAnimation(LayerHeight.LAYER_1,rainbowSnakes);
                        }

                        if(gamepad1.xWasPressed()){

                            prism.clearAllAnimations();
                        }

                        if(gamepad1.dpadDownWasPressed()){

                            prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);
                        }

                        telemetry.addLine("Press A to insert and update the created animations.");
                        telemetry.addLine("Press X to clear current animations.");
                        telemetry.addLine("Press D-Pad Down to save current animations to Artboard #0");
                        telemetry.addLine();
                        telemetry.addData("Run Time (Hours): ",prism.getRunTime(TimeUnit.HOURS));
                        telemetry.addData("Run Time (Minutes): ",prism.getRunTime(TimeUnit.MINUTES));
                        telemetry.addData("Number of LEDS: ", prism.getNumberOfLEDs());
                        telemetry.addData("Current FPS: ", prism.getCurrentFPS());
                        telemetry.update();
                        sleep(50);
                    }
                }


            }
            }
}



