/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.team7649;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 * The code is structured as a LinearOpMode
 *
 * This particular OpMode executes a POV Game style Teleop for a PushBot
 * In this mode the left stick moves the robot FWD and back, the Right stick turns left and right.
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Pushbot: Teleop POV", group="Pushbot")
//@Disabled
public class PushbotTeleopPOV_Linear extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareOmni hws           = new HardwareOmni();   // Use a Pushbot's hardware
                                                               // could also use HardwarePushbotMatrix class.

    @Override
    public void runOpMode() {
        double left;
        double right;
        double drive;
        double turn;
        double max;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        hws.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
            // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
            // This way it's also easy to just drive straight, or just turn.
            if (gamepad1.right_bumper)
            {
                hws.turbo = 1;
            }
            else
                hws.turbo = 1;
            // Sets the joystick values to variables for better math understanding
            // The Y axis goes
            hws.y1  = -gamepad1.left_stick_y;
            hws.x1  = gamepad1.left_stick_x;
            hws.x2  = gamepad1.right_stick_x;

            // sets the math necessary to control the motors to variables
            // The left stick controls the axial movement
            // The right sick controls the rotation
            hws.frontRightPower     = hws.y1 - hws.x2 - hws.x1;
            hws.backRightPower      = hws.y1 - hws.x2 + hws.x1;
            hws.frontLeftPower      = hws.y1 + hws.x2 + hws.x1;
            hws.backLeftPower       = hws.y1 + hws.x2 - hws.x1;

            // Normalize the values so neither exceed +/- 1.0
            hws.max =  Math.max(Math.abs(hws.frontRightPower), Math.max(Math.abs(hws.backRightPower),
                    Math.max(Math.abs(hws.frontLeftPower), Math.abs(hws.backLeftPower))));
            if (hws.max > 1.0)
            {
                hws.frontRightPower     /= hws.max;
                hws.backRightPower      /= hws.max;
                hws.frontLeftPower      /= hws.max;
                hws.backLeftPower       /= hws.max;
            }

            // sets the speed for the motros with the turbo multiplier
            hws.frontRightPower     *= hws.turbo;
            hws.backRightPower      *= hws.turbo;
            hws.frontLeftPower      *= hws.turbo;
            hws.backLeftPower       *= hws.turbo;

            hws.frontRightDrive.setPower(hws.frontRightPower);
            hws.backRightDrive.setPower(hws.backRightPower);
            hws.frontLeftDrive.setPower(hws.frontLeftPower);
            hws.backLeftDrive.setPower(hws.backLeftPower);

            left=hws.frontLeftPower;
            right=hws.backRightPower;

            // Send telemetry message to signify robot running;
            telemetry.addData("left",  "%.2f", left);
            telemetry.addData("right", "%.2f", right);
            telemetry.update();

            // Pace this loop so jaw action is reasonable speed.
            sleep(50);
        }
    }
}
