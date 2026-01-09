/*
 * NextFTC: a user-friendly control library for FIRST Tech Challenge
 * Copyright (C) 2025 Rowan McAlpin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.firstinspires.ftc.teamcode.Utility;

import dev.nextftc.control.KineticState;
import dev.nextftc.control.interpolators.InterpolatorElement;
import java.util.Objects;

/**
 * Constraints for a trapezoidal motion profile.
 */
public class TrapezoidProfileConstraints {
    public final double maxVelocity;
    public final double maxAcceleration;

    public TrapezoidProfileConstraints(double maxVelocity, double maxAcceleration) {
        if (maxVelocity < 0) throw new IllegalArgumentException("Constraints must be non-negative");
        if (maxAcceleration < 0) throw new IllegalArgumentException("Constraints must be non-negative");
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
    }
}

