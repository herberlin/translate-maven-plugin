translate-maven-plugin
----------------------

A maven plugin to translate language resource files into different languages 
using the Google Translate Cloud service. A Google api key is required.

Google Cloud Translate is a payed service. See https://cloud.google.com/translate/docs/setup
how to obtain an api-key and enable billing. 

The plugin also works with gradle, see below. 

Choose your source language file and configure the target languages you 
need. The plugin creates the necessary files and inserts the translated
phrases. Already existing translations are not touched, missing translations
are added. 

## Supported file formats
Have a look at the data directory for examples.
### Android xml resources
The resources are located as required in the android build:

    src/main/res
        values/
            strings.xml
        values-de/
            strings.xml
        values-es/
            strings.xml

and so on. Choose the `values/strings.xml` as base, the other files and
directories are generated.

Inside the file we support the following: 

    <resources>
        <string name="start" translatable="false">start</string>
        <string name="anchor_alert_running">Anchor alert at<xliff:g> %1$s </xliff:g>of<xliff:g> %2$s </xliff:g>m.</string>
        <string updated="true" name="warning">Urgent Warning</string>
    </resources>

- The `translatable="false"` attribute is supportes (android-standard). These entries are not translated.
- The `<xliff:g>`-Tag is supported (android-standard). Content between these tags is not translated. 
- It's also possible to force re-translation when you change an
  expression and don't want to change the key. Use: `updated="true"` and remove the attribute after translation. 

### Json - language resources

Json resouces must be located in the same directory having the to letter 
language code as filename and `.json` as file extension:

    i18n/
        de.json
        en.json
        fr.json

Choose you base language file (e.g. en.json) as source, files for the 
configured languages are generated in the directory. 

Inside the json - file the hierarchy is respected. 

    { 
        "common": {
            "base": "_UPDATED_baseentry",
            "unchanged" : "unchanged",
            "@ignored" : "ignored"
        }
    }

The keyword `_UPDATED_` at the start of the value (not the key) indicates
that a value was changed and should be re-translated. Remove it manually afterwords. 

Otherwise existing translations are not touched.

Keys starting with an ampersat '@' are ignored and not translated. 

### Java property - files
Implementation missing.

## Configuration

### Maven

    <project>
    <build>
        <plugins>
            <plugin>
                <groupId>de.herberlin.translate</groupId>
                <artifactId>translate-maven-plugin</artifactId>
                <!-- plugin version here -->
                <version>1.0.0</version>
                <configuration>

                    <!-- android or json -->
                    <mode>android</mode>

                    <!-- 
                        Location of your source file relative to the pom.
                        For json use e.g. i18n/en.json
                    -->
                    <source>app/src/main/res/values/strings.xml</source>

                    <!-- 
                        Location of your certificte.
                        Use of the GOOGLE_APPLICATION_CREDENTIALS environment 
                        variable is currently not supported. 
                    -->
                    <certificate>
                        /home/aherbertz/Dokumente/goolge/my-google-translate-key.json
                    </certificate>

                    <!-- Settings for google translate, no need to change -->
                    <serviceUrl>https://www.googleapis.com/auth/cloud-platform</serviceUrl>
                    <translator>google</translator>

                    <languages>
                        <!-- Confiture target languages here -->
                        <language>ar</language> <!-- Arabic -->
                        <language>cs</language> <!-- Czech -->
                        <language>da</language> <!-- Danish -->
                        <language>de</language> <!-- German -->
                    </languages>
                </configuration>
            </plugin>
        </plugins>
    </build>
    </project>

## Gradle

        buildscript {
            repositories {
                mavenCentral()
                maven {
                    // If you want to use snapshots this is the the sonatype snapshot repo
                    url 'https://s01.oss.sonatype.org/content/repositories/snapshots'
                }                
            }
            dependencies {
                // add the dependency
                classpath 'de.herberlin.translate:translate-maven-plugin:1.1.0'
            }
        }
        
        // run:  ./gradlew translate
        apply plugin: 'de.herberlin.translate'

        // configuration
        translate {
            
            // modes: android, json, properties
            mode = "android"

            // google or dummy for testing
            translator = "google"

            // google service url
            serviceUrl = "https://www.googleapis.com/auth/cloud-platform"

            // path to your google cloud certificate, see above
            certificate = "/home/aherbertz/Dokumente/goolge/my-google-translate-key.json"

            // location of your soure files
            source = "data/android/values/strings.xml"

            // an array of target languages
            languages = ["de", "ru"]
        }

## License 
Apache 2 
http://www.apache.org/licenses/LICENSE-2.0





    





