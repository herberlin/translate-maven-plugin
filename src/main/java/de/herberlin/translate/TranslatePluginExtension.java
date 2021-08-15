package de.herberlin.translate;

/**
 * Configuration holder for gradle plugin.
 * e.g.
 * translate {
 *     mode="android"
 *     translator = "dummy"
 *     source = "data/android/values/strings.xml"
 *     certificate = "/home/aherbertz/Documents/my-google-translate-key.json"
 *     serviceUrl = "https://www.googleapis.com/auth/cloud-platform"
 *     languages = ["de", "ru"]
 * }
 */
public class TranslatePluginExtension {

    /**
     * Maven configuration parameter mode,
     * possible values android, json, default: andorid
     */
    private String mode = "android";
    /**
     * Maven configuration parameter translator,
     * possible values google, dummy.
     */
    private String translator = "google";

    /**
     * Maven configuration parameter source file.
     */
    private String source;

    /**
     * Maven configuration parameter certificate location.
     */
    private String certificate;

    /**
     * Maven configuration parameter serviceUrl, defaults to goocle cloud.
     */
    private String serviceUrl = "https://www.googleapis.com/auth/cloud-platform";


    private String[] languages ;


    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }



    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
