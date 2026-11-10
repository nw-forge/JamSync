
# JamSync 🎵

### Intelligent Musician Matching, Rehearsal Scheduling and Conflict Resolution System

JamSync is a Core Java console-based application designed to simplify rehearsal scheduling for musicians and bands.

It automatically matches musicians to required roles, identifies common availability, checks rehearsal room requirements, detects scheduling conflicts, and generates confirmed rehearsal schedules.

## Features

- Musician matching based on instruments and roles
- Recursive backtracking for lineup generation
- Common availability detection
- Rehearsal room and equipment validation
- Booking conflict detection
- Schedule ranking based on time preference and musician workload
- Custom exception handling
- CSV file generation for confirmed rehearsals

## Technologies Used

- Java 11+
- Object-Oriented Programming
- Java Collections Framework
- Java Streams
- File Handling
- Recursive Backtracking

## Project Structure

```text
JamSync/
├── src/
│   ├── Main.java
│   ├── Musician.java
│   ├── Song.java
│   ├── RehearsalRoom.java
│   ├── RehearsalSession.java
│   ├── ScheduleEngine.java
│   └── exceptions/
│       ├── BookingConflictException.java
│       └── NoFeasibleScheduleException.java
├── output/
│   └── jamsync_schedule.csv
└── README.md
```

## How to Run

Make sure Java 11 or later is installed.

From the project root directory, compile:

```bash
javac -d out src/exceptions/*.java src/*.java
```

Run:

```bash
java -cp out Main
```

## Sample Output

```text
===== JAMSYNC: CONFIRMED REHEARSAL =====
Song: Midnight Echo
Time: Mon 10:00
Room: Room A
Lineup:
 - Aarav (Vocals)
 - Mira (Guitar)
 - Kabir (Drums)
Status: CONFIRMED
```

The confirmed schedule is saved to:

`output/jamsync_schedule.csv`

## Current Scope

This is a console-based academic prototype using sample musicians, rooms, and discrete time slots. It currently supports one musician per required role.

## Author

Narayani Walwadkar  
Roll Number: SIT_13
