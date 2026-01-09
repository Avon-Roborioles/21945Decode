package org.firstinspires.ftc.teamcode.Utility;

import dev.nextftc.control.KineticState;
import dev.nextftc.control.interpolators.InterpolatorElement; /**
 * An InterpolatorElement that generates smooth trapezoidal motion profiles.
 */
public class TrapezoidProfileElement implements InterpolatorElement {
    private final TrapezoidProfile profile;
    private KineticState goal = new KineticState();
    private KineticState previousReference = new KineticState();
    private long lastTimestampNano = -1;

    public TrapezoidProfileElement(TrapezoidProfileConstraints constraints) {
        this.profile = new TrapezoidProfile(constraints);
    }

    @Override
    public void setGoal(KineticState goal) {
        this.goal = goal;
        this.lastTimestampNano = System.nanoTime();
    }

    @Override
    public KineticState getGoal() {
        return goal;
    }

    @Override
    public KineticState getCurrentReference() {
        long nextTimestampNano = System.nanoTime();

        if (lastTimestampNano == -1) {
            lastTimestampNano = nextTimestampNano;
            return new KineticState();
        }

        double dtSeconds = (nextTimestampNano - lastTimestampNano) / 1e9;
        previousReference = profile.calculate(dtSeconds, previousReference, goal);
        return previousReference;
    }

    @Override
    public void reset() {
        lastTimestampNano = System.nanoTime();
        previousReference = new KineticState();
    }
}
