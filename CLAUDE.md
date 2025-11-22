# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Kotlin learning playground repository organized as a multi-module Gradle project. It contains various Kotlin learning exercises, clone-coding projects, lecture implementations, and experimental code organized into separate submodules.

## Repository Structure

The repository is divided into several independent modules:

- **lecture/** - Educational implementations including:
  - `toby_xunit` - Custom xUnit testing framework implementation (TDD exercise)
  - `inflearn/coroutine` - Kotlin coroutines learning materials covering builders, cancellation, and state management

- **book/** - Code examples from "Kotlin in Action" book, organized by chapters covering:
  - Extension functions and collections (Chapter 3)
  - Classes, interfaces, and object declarations (Chapter 4)
  - Lambdas and higher-order functions (Chapter 5)
  - Nullability and primitive types (Chapter 6)
  - Inline functions (Chapter 8)
  - Generics and variance (Chapter 9)

- **project/** - Practice projects implementing common programming exercises:
  - `numberbaseball` - Number baseball game with clean architecture (domain, application, infra, presentation layers)
  - `racingcar` - Racing car game demonstrating domain modeling

- **clone-coding/** - Clone implementations of open-source Kotlin projects for learning
  - `mockk` - MockK library clone-coding

- **db-lab/** - Spring Boot + JPA experiments and practice:
  - JPA proxy pattern implementations
  - Entity relationship mappings
  - Uses H2 in-memory database

## Build System

- **Build Tool**: Gradle with Kotlin DSL
- **Kotlin Version**: 1.9.25
- **Java Version**: 21
- **Testing**: JUnit 5 with AssertJ
- **Coroutines**: kotlinx-coroutines-core 1.7.2

### Common Commands

Build the entire project:
```bash
./gradlew build
```

Run all tests:
```bash
./gradlew test
```

Run tests for a specific module:
```bash
./gradlew :lecture:test
./gradlew :project:test
./gradlew :db-lab:test
```

Run a specific test class:
```bash
./gradlew :project:test --tests "com.study.numberbaseball.domain.BallTest"
./gradlew :project:test --tests "com.study.racingcar.domain.CarTest"
```

Clean build artifacts:
```bash
./gradlew clean
```

Assemble JARs without running tests:
```bash
./gradlew assemble
```

Run the Spring Boot application (db-lab module):
```bash
./gradlew :db-lab:bootRun
```

Build Spring Boot executable JAR:
```bash
./gradlew :db-lab:bootJar
```

## Architecture Patterns

### Clean Architecture (project module)

The `numberbaseball` and `racingcar` projects follow clean architecture principles:

- **domain/** - Core business logic and domain models (e.g., `Ball`, `Balls`, `Car`, `Cars`)
- **application/** - Application services orchestrating domain logic
- **infra/** - Infrastructure implementations (console I/O, random number generation)
- **presentation/** - User input readers and view interfaces

Domain models are immutable and contain business rules. Infrastructure details are abstracted through interfaces defined in the domain layer.

### Spring Boot + JPA (db-lab module)

The `db-lab` module uses:
- Spring Boot 3.5.8 with Spring Data JPA
- Kotlin plugin for JPA (allOpen plugin configured for JPA entities)
- Entity classes with proper JPA annotations
- Lazy loading patterns with `FetchType.LAZY`

Note: Kotlin JPA entities require the `allOpen` plugin to make entity classes open (configured in db-lab/build.gradle.kts).

## Testing Conventions

- Test files use JUnit 5 (`@Test` annotations)
- Assertions use AssertJ (`assertThat()`)
- Test classes are located in standard `src/test/kotlin/` directories
- Custom xUnit framework exists in `lecture/src/main/kotlin/com/study/toby_xunit/xunit/` for educational purposes

## Coroutines Learning Path

The coroutine learning materials in the lecture module follow this progression:
1. Routine basics (`Routine.kt`)
2. Coroutine fundamentals (`Coroutine.kt`)
3. Coroutine builders (`Builder.kt`)
4. Cancellation mechanisms (`CoroutineCancel.kt`)
5. Job state management (`CoroutineState.kt`)

## Working with Modules

Each module can be built independently. When adding new code:
- Respect the existing package structure (`com.study.*`)
- Follow the architecture pattern of the specific module
- For domain-driven modules, keep domain logic pure and free of infrastructure concerns
- For Spring Boot code, leverage Kotlin's data classes and nullable types appropriately