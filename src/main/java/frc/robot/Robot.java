/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot
{

    private Joystick mLeftFlightStick;
    private Joystick mRightFlightStick;
    private Joystick mXboxJoystick;

    private WPI_TalonSRX mLeftDriveMotor;
    private WPI_TalonSRX mRightDriveMotor;
    private DifferentialDrive mDiffDrive;

    private SendableChooser<DriveMode> mDriveModeSelector;

    private enum DriveMode
    {
        FLIGHT_STICK, XBOX_TANK, XBOX_HALO, XBOX_CHEEZY
    }

    public Robot()
    {
        mLeftFlightStick = new Joystick(0);
        mRightFlightStick = new Joystick(1);
        mXboxJoystick = new Joystick(2);

        mLeftDriveMotor = new WPI_TalonSRX(3);
        WPI_TalonSRX leftFollower = new WPI_TalonSRX(1);
        leftFollower.follow(mLeftDriveMotor);

        mRightDriveMotor = new WPI_TalonSRX(6);
        WPI_TalonSRX rightFollower = new WPI_TalonSRX(4);
        rightFollower.follow(mRightDriveMotor);

        mDiffDrive = new DifferentialDrive(mLeftDriveMotor, mRightDriveMotor);

        mDriveModeSelector = new SendableChooser<>();
        for (DriveMode dm : DriveMode.values())
        {
            mDriveModeSelector.addOption(dm.name(), dm);
        }

        SmartDashboard.putData("Drive Mode", mDriveModeSelector);
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic()
    {
        DriveMode driveMode = mDriveModeSelector.getSelected();
        if (driveMode == null)
        {
            System.out.println("OH UH, NOTHING SET IN SHUFFLEBOARD");
            driveMode = DriveMode.FLIGHT_STICK;
        }

        switch (driveMode)
        {
        case FLIGHT_STICK:
            mDiffDrive.tankDrive(mLeftFlightStick.getY(), mRightFlightStick.getY());
            break;
        case XBOX_HALO:
            mDiffDrive.arcadeDrive(mXboxJoystick.getRawAxis(XboxButtonMap.LEFT_Y_AXIS), mXboxJoystick.getRawAxis(XboxButtonMap.RIGHT_X_AXIS));
            break;
        case XBOX_CHEEZY:
            mDiffDrive.curvatureDrive(mXboxJoystick.getRawAxis(XboxButtonMap.LEFT_Y_AXIS), mXboxJoystick.getRawAxis(XboxButtonMap.RIGHT_X_AXIS),
                    mXboxJoystick.getRawButton(XboxButtonMap.RB_BUTTON));
            break;
        case XBOX_TANK:
            mDiffDrive.tankDrive(mXboxJoystick.getRawAxis(XboxButtonMap.LEFT_Y_AXIS), mXboxJoystick.getRawAxis(XboxButtonMap.RIGHT_Y_AXIS));
            break;
        default:
            break;
        }

        System.out.println("Drive Mode : " + driveMode);
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic()
    {
    }
}
