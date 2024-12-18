# Puzzle-API
This tool allows you to interact with the OpenAI API to send queries based on the user's description of image wanting to be generated, along with the chosen dimensions of the the puzzle of choice

## Prerequisites
  
- **Java JDK**
- **OpenAI API key**
- **OpenAI API URL**
   
## Installation
      
1. **Install Dependencies**

    Make sure you have the latest JDK file installed and be able to run the program on your IDE before you compile it on Command Prompt

    ```https://www.oracle.com/java/technologies/downloads/```

2. **Proper imports stored in src folder** 

    Json https://search.maven.org/remotecontent?filepath=org/json/json/20240303/json-20240303.jar

    Json-lib https://mvnrepository.com/artifact/net.sf.json-lib/json-lib/2.2.3

    Okhttp https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp/4.11.0

    Okio https://mvnrepository.com/artifact/com.squareup.okio/okio/2.10.0
   
4. **API Key Setup**

   Ensure you have your OpenAI API and OpenAI Image URL stored as an environment variables:

   Check that you have the correct keys in your environment by running this:
   ```Command Prompt
   echo %YOUR_OPENAI_API_KEY%
   echo %YOUR_OPENAI_API_URL%
   ```

## Usage

Run the program by specifying the path to your src folder with the needed imports

```Command Prompt
   cd C:\path\to\src\folder
```

For example:

```Command Prompt
   cd C:\Users\poncema4\OneDrive - Seton Hall University\FALL 2024\CSAS2123AA\IntelliJWorkSpace\Puzzle Game API\src
```

Then...

To be able to just write "puzzle_game" to run your program open notepad or any text editor and write

```Notepad
@echo off
REM Set the classpath with your dependencies
set CLASSPATH=.;json-20240303.jar;json-lib-2.4-jdk15.jar;okhttp-4.11.0.jar;okio-2.10.0.jar

REM Compile your Java files
javac Your_java_file_1.java Your_java_file_2.java Your_java_file_3.java Your_java_file_etc.java

REM Run your game
java -cp %CLASSPATH% "Your_run_game_file.java
```

This should be converted to a batch (.bat) file now, name the notepad "puzzle_game.bat" and save it in the same src file

Now you must compile the java files in order to be able to run the program properly

```Command Prompt -> Step 1
 cd C:\path\to\src\folder
```

Then

```Command Prompt -> Step 2
puzzle_game.bat
```

Lastly, if you were successful, Command Prompt should say...

Note: Some input files use or override a deprecated API.
Note: Recompile with -Xlint:deprecation for details.

Check you src folder now and you should have your file names in .class format, this is how you also know that you successfully compiled your java files
(You only need to compile your java files once) -> If you make any changes, compile your files again to avoid any errors

## Run Program

```Command Prompt
puzzle_game
```

After you run the program, it will prompt the user to describe the image they wish to generate, along with the certain puzzle dimensions they wish to have
for their game and then it will return back in command prompt the URL of the generated image, also the "solved image" so the user knows what the image
should look like
________________________________________________________________________________

Instructions for user:

Left-click -> Select a puzzle piece

  "R" -> Rotate the puzzle piece 90 degrees to the right

  "L" -> Rotate the puzzle piece 90 degrees to the left

  "F" -> Flip the puzzle piece 180 degrees horizontally

Left-click-to-another-piece -> Both puzzle pieces swap places

"Puzzle was solved in ___ seconds"

## Notes

- **API Key**: The program checks for the `OPENAI_API_KEY` environment variable.
- **API_URL**: The program checks for the `OPENAI_API_URL` environment variable.
            If it’s not set, the program will exit with an error.
