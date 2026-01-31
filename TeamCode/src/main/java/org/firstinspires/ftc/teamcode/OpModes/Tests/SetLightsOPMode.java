package org.firstinspires.ftc.teamcode.OpModes.Tests;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utility.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.Utility.Prism.PrismAnimations;
@TeleOp(name="Set Lights", group="Setup")
//@Disabled
@Configurable
public class SetLightsOPMode extends LinearOpMode {
    public static long pwmTarget = 1000;
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

    //0 - Purple Snakes
    //1 - PGP


    @Override
    public void runOpMode() throws InterruptedException {


        waitForStart();



    }
}
