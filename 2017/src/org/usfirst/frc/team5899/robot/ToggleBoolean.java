package org.usfirst.frc.team5899.robot;

public class ToggleBoolean {
	Boolean pressed;
	
	public ToggleBoolean() {
		this.pressed = false;
	}
	
	public Boolean toggle(Boolean value, Boolean toggleWhenTrue) {
		if (toggleWhenTrue && !this.pressed) {
			this.pressed = true;
			return !value;
		}
		this.pressed = false;
		return value;
	}
}
