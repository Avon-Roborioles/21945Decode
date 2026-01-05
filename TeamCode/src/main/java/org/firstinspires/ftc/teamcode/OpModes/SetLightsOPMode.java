package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utility.Prism.Color;
import org.firstinspires.ftc.teamcode.Utility.Prism.Direction;
import org.firstinspires.ftc.teamcode.Utility.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.Utility.Prism.PrismAnimations;
@TeleOp(name="Set Lights", group="Setup")
@Disabled
public class SetLightsOPMode extends LinearOpMode {
    GoBildaPrismDriver prism;

    PrismAnimations.Snakes purpleSnakes = new PrismAnimations.Snakes();
    PrismAnimations.Solid purpleLeft = new PrismAnimations.Solid();
    PrismAnimations.Solid purpleMiddleBack = new PrismAnimations.Solid();
    PrismAnimations.Solid purpleMiddleFront = new PrismAnimations.Solid();
    PrismAnimations.Solid purpleRight = new PrismAnimations.Solid();
    PrismAnimations.Solid greenLeft = new PrismAnimations.Solid();
    PrismAnimations.Solid greenMiddleBack = new PrismAnimations.Solid();
    PrismAnimations.Solid greenMiddleFront = new PrismAnimations.Solid();
    PrismAnimations.Solid greenRight = new PrismAnimations.Solid();
    PrismAnimations.Solid offLeft = new PrismAnimations.Solid();
    PrismAnimations.Solid offMiddleBack = new PrismAnimations.Solid();
    PrismAnimations.Solid offMiddleFront = new PrismAnimations.Solid();
    PrismAnimations.Solid offRight = new PrismAnimations.Solid();




    @Override
    public void runOpMode() throws InterruptedException {
        prism = hardwareMap.get(GoBildaPrismDriver.class,"Prism");
        prism.setStripLength(36);
        prism.enableDefaultBootArtboard(true);
        prism.setDefaultBootArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);










        waitForStart();
        prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);



    }
}
