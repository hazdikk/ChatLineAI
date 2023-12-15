# ChatLineAI

ChatLineAI is a project that integrates OpenAI's GPT-3 model with the LINE messaging app to generate targeted advertisements using Natural Language Processing (NLP). This application offers an innovative approach to utilizing AI chatbots in advertising, analyzing conversation context to produce relevant ads.

## Prerequisites

- [Java 17](https://bell-sw.com/pages/downloads/#/java-17-lts)
- [Maven](https://maven.apache.org/)

## Installation

Follow these steps to set up your development environment:

### Cloning the Repository

1. Clone the repository using the following command:

    ```bash
    git clone https://github.com/hazdikk/ChatLineAI
    ```

### Building the Project

2. Navigate to the project directory:

    ```bash
    cd ChatLineAI
    ```

3. Build the project using Maven:

    ```bash
    mvn clean install
    ```

### Running the Application

4. Run the application with this command:

    ```bash
    mvn spring-boot:run
    ```

   The application will be accessible at `http://localhost:8080`.

## Troubleshooting

### Java Version Issue

If you encounter issues related to the Java version, follow these steps:

1. Ensure Java 17 is installed:

    ```bash
    java -version
    ```

   The output should indicate Java 17.

2. If Java 17 is not installed, download and install it from [BellSoft Liberica JDK 17 download page](https://bell-sw.com/pages/downloads/#/java-17-lts).

3. Set Java 17 as your `JAVA_HOME`:

    - For Windows:

        ```bash
        set JAVA_HOME="path_to_java17"
        ```

    - For macOS/Linux:

        ```bash
        export JAVA_HOME="path_to_java17"
        ```

   Replace `"path_to_java17"` with the installation path of Java 17.

4. Verify the `JAVA_HOME` setting:

    ```bash
    echo $JAVA_HOME
    ```

5. Rebuild and rerun the application.
