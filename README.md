# Util for renaming files from Altium Designer to Gerber format

### Build
- download (git required)
```bash
git clone https://github.com/sergeyezhov/altium-designer-gbr-renamer.git
```
- build the project
  - mac
    ```bash
    sh gradlew clean build
    ```
  - windows
    ```
    gradlew.bat clean build
    ```

### Usage
- Put files `./build/libs/altium-designer-gbr-renamer-1.0.0.jar` and `./execution/FileName_01` to your Altium project directory
- Rename `FileName_01` to `<your-file-name>_<your-file-version>`
- Launch script `<your-file-name>_<your-file-version>`
