# STARLIGHT_COLLECTOR

*A Java Swing–based 2D arcade game demonstrating principles of object-oriented programming, interactive graphics, and event-driven design.*

## Overview

**Team Lead:** Pritam Thapa
**Co-Developers:** Ayush Shrestha, Roshika Rai
**Repository:** [Starlight_Collector](https://github.com/PritamTheCoder/Starlight_Collector.git)

Starlight Collector is an interactive desktop game developed in Java using the Swing GUI framework. The project was created as an academic exercise to explore structured game development, real-time rendering, input handling, and the application of MVC architecture in a manageable but meaningful environment.

The player navigates a basket to collect falling stars while avoiding meteors. As the score increases, the game dynamically adjusts difficulty through faster objects, new backgrounds, and additional visual feedback.

---

## Features

* **Core Gameplay:** Player moves the basket horizontally using arrow keys to collect stars and avoid meteors.
* **Scoring System:**

  * Normal Star → **+2 points**
  * Rare Star (Type I) → **+5 points**
  * Rare Star (Type II) → **+10 points**
* **Dynamic Level Progression:** Backgrounds and falling speeds change based on score thresholds.
* **Power-Ups:** Includes speed boosts and magnet-style attraction.
* **High-Score Storage:** The highest score is recorded and displayed for replay motivation.
* **Immersive Feedback:** Integrated images, sounds, and thematic visual transitions.

---

## Project Structure

```
/src      → Java source code organized using MVC principles  
/res      → Backgrounds, sprites, sound effects, and icons  
/classes  → Compiled class files  
```

### Architectural Notes

* **Model:** Game entities such as basket, stars, meteors; scoring and collision mechanics
* **View:** Swing-based rendering, UI painting, transitions, background management
* **Controller:** Keyboard input handling, game-loop timing, state changes

---

## Purpose & Design Rationale

This project was developed to:

* Apply **object-oriented principles** (encapsulation, inheritance, polymorphism) in a real project.
* Demonstrate **event-driven programming** using Java Swing and timers.
* Implement a clear **Model–View–Controller** separation for better clarity and maintainability.
* Provide a learning-friendly codebase for understanding **2D game logic**, resource loading, and animation loops.
* Create a playable, visually appealing game that motivates continuous improvement and experimentation.

The decisions behind the scoring system, level transitions, and power-ups were made to keep the gameplay simple yet progressively challenging, while also keeping the codebase approachable for academic demonstration and future expansion.

---

## Getting Started

### Prerequisites

* Java Development Kit (JDK) 8 or higher
* A Java-supported IDE (IntelliJ, Eclipse, NetBeans, VSC) or command-line compiler

### Installation

```bash
git clone https://github.com/PritamTheCoder/Starlight_Collector.git
```

### Running the Game

**Using an IDE:**

* Open the project
* Ensure the `res` folder is correctly referenced
* Run the `Main` class

**Using terminal:**
```bash
javac -d bin src/**/*.java
java -cp bin StarlightCollector
```

Make sure the `res` directory is accessible so images and sounds load correctly.

---

## How to Play

1. Launch the game from your IDE or terminal.
2. Use the **Left** and **Right arrow keys** to move the basket.
3. **Collect stars** to increase your score.
4. **Avoid meteors**—colliding with them ends the game.
5. Advance through levels as your score increases and aim to beat the saved high score.

---

## Future Enhancements

* Additional power-ups and star types
* Pause menu and in-game settings
* Export to runnable JAR for easier distribution
* Smoother animation effects and particle systems
* Multiple difficulty modes or challenge levels

---

## Contributing

We welcome contributions in the form of bug fixes, new features, or documentation improvements.
To contribute:

1. Fork the repository
2. Create a feature branch
3. Implement and test changes
4. Submit a detailed pull request

---

## Acknowledgements

This game was developed under the leadership of **Pritam Thapa**, with valuable contributions from **Ayush Shrestha**, **Roshika Rai** and **Omkar Ghimire**.
We extend appreciation to the Java open-source community for tools, references, and learning resources used throughout development.

