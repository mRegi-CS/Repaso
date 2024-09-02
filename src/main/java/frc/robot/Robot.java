// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  //Sub-sistemans
  //Parentesis: Funcion/Accion No parentesis: Propiedad
   /*++++++++++++++++++++ CHASIS +++++++++++++++++ */
  //Decalrar motores
    CANSparkMax motorFrontLeft = new CANSparkMax(device:1, MotorType.kBrushless);
    CANSparkMax motorFrontRight = new CANSparkMax(device:3, MotorType.kBrushless);
    CANSparkMax motorBackLeft = new CANSparkMax(device:2, MotorType.kBrushless);
    CANSparkMax motorBackRight = new CANSparkMax(device:4, MotorType.kBrushless);

    //Declarar encoder
    //RelativeEncoder nombreDelEncoder = motor.getEncoder();
    RelativeEncoder encoderRight = motorFrontRight.getEncoder();
    RelativeEncoder encoderLeft = motorFrontLeft.getEncoder();

    DifferentialDrive chasis = new DifferentialDrive(motorFrontLeft, motorFrontRight);

    AHRS navX = AHRS(SPI.Port.kMXP);

    Solenoid solenoidChasis = new Solenoid(PneumaticsModuleType.REVPH, 4);

   /*++++++++++++++++++ CONVEYER +++++++++++++++++++++ */
    //Declarar motor
    CANSparkMax motorConveyer = new CANSparkMax(5, MotorType.kBrushless);


    /*+++++++++++++++++ INTAKE ++++++++++++++++++ */
    //Declarar solenoid
    // Solenoid NombreDelSolenoide = new Solenoid
    Solenoid solenoidIntake = new Solenoid(PneumaticsModuleType.REVPH, 1);

    //Declarar motores
    CANSparkMax motorIntake = new CANSparkMax(6, MotorType.kBrushless);

    /*+++++++++++++++++++++ SHOOTER +++++++++++++++++++++++++++++++++++ */
    CANSparkMax motorShooterLeft = new CANSparkMax(7, MotorType.kBrushless);
    CANSparkMax motorShooterRight = new CANSparkMax(8, MotorType.kBrushless);

    /* ++++++++++++++++ ESCALADOR ++++++++++++ */
    //Declarar Solenoide
    Solenoid solenoidClimber = new Solenoid(PneumaticsModuleType.REVPH, 2);

    /*+++++++++++++ CONTROLER +++++++++++ */
    Joystick controlDriver = new Joystick(1);
    Joystick controlPlacer = new Joystick(2);

    /*+++++++++++++++++++++++ VARIABLES ++++++++++++++++++++++++ */
    boolean isArcade = false;
    boolean McQueen = false;
    boolean Up = false;
    boolean conveyer = false;

  @Override
  public void robotInit() {
    // Establecer configuracion Inicial de Onix
    
    //Motores se siguen
    motorBackLeft.follow(motorFrontLeft);
    motorBackRight.follow(motorFrontRight);

    //Encoders inicial en 0
    encoderLeft.setPosition(0);
    encoderRight.setPosition(0);

    //NavX inicia en 0 (Sabe donde esta)
    navX.reset();

    //Solenoids en 0 (Contraidos)
    solenoidClimber.set(false);
    solenoidIntake.set(false);

    //Invierte un motor y el otro no para que vayan en los sentidos correctos
    motorShooterLeft.setInverted(true);
    motorShooterRight.setInverted(!motorShooterRight.getInverted());

    motorShooterLeft.follow(motorShooterRight);

  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    //Drivers controlan
    //Es arcade o es tanque??
    if(controlDriver.getRawButton(1)){
      isArcade = true;
    } else if (controlDriver.getRawButton(2)){
      isArcade = false;
    }

    //Que hace el robot con eso
    if (isArcade){
      chasis.arcadeDrive(controlDriver.getRawAxis(1)*0.7, controlDriver.getRawAxis(2)*0.7);
    } else{
      chasis.tankDrive(controlDriver.getRawAxis(1)*0.7, controlDriver.getRawAxis(2)*0.7);
    }

    //Cambio de velocidad (Alta o baja)
    if (controlDriver.getRawButton(3)){
      McQueen = true;
    } else if (controlDriver.getRawButton(4)){
      McQueen = false;
    }

    //Que hace con eso
    if (McQueen){
      solenoidChasis.set(true);
    } else {
      solenoidChasis.set(false);
    }

    //Intake
    //1. Un lado 2. El otro lado  sino pues no, nada
    if(controlPlacer.getRawButton(1)){
      motorIntake.set(0.5);
    } else if (controlPlacer.getRawButton(2)){
      motorIntake.set(-0.5);
    } else {
      motorIntake.set(0);
    }

    //Shooter
    //3. a 0.8 vel 4. a 0.6 (Más lento) sino pues no, nada    PD: Solo el derecho porque el Izq lo sigue 
    if (controlPlacer.getRawButton(3)) {
      motorShooterRight.set(0.8);
    } else if (controlPlacer.getRawButton(4)){
      motorShooterRight.set(0.6);
    } else {
      motorShooterRight.set(0);
    }

    //Climber
    //Como lo cambia
    if (controlPlacer.getRawButton(5)){
      Up = true;
    } else if (controlPlacer.getRawButton(6)){
      Up = false;
    } else {}
      
    //Que hace con eso
    if (Up) {
      solenoidClimber.set(true);
    } else {
      solenoidClimber.set(false);
    }

    //Conveyer
    /*if (controlPlacer.getTrigger()){
      conveyer = true;
      motorIntake.set(0.5);
      motorConveyer.set(0.5);
    } else if (controlPlacer.getTrigger()){
      conveyer = true;
      motorIntake.set(-0.5);
      motorConveyer.set(-0.5);
    }
    else {
      conveyer = false;
      motorIntake.set(0);
      motorConveyer.set(0);
    }*/

    if(controlPlacer.getRawAxis(2)> 0.1){
      motorConveyer.set(0.3);
    } else if (controlPlacer.getRawAxis(2)> 0.5){
      motorConveyer.set(0.5);
    } else if (controlPlacer.getRawAxis(2)> 0.9){
      motorConveyer.set(0.8);
    } else {
      motorConveyer.set(0);
    }
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
