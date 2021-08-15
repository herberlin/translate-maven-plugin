package de.herberlin.translate;

/**
 * Logger facade.
 */
public interface Log {

    void debug(String msg );
    void info(String msg);
    void error(String msg, Exception e);

    public static class MavenLogger implements Log {

        private final org.apache.maven.plugin.logging.Log logger;

        public MavenLogger(org.apache.maven.plugin.logging.Log logger) {
            this.logger = logger;
        }

        @Override
        public void debug(String msg) {
            logger.debug(msg);
        }

        @Override
        public void info(String msg) {
            logger.info(msg);
        }

        @Override
        public void error(String msg, Exception e) {
            logger.error(msg,e);
        }
    }

    public static class GradleLogger implements Log {

        private final org.gradle.api.logging.Logger logger;

        public GradleLogger(org.gradle.api.logging.Logger logger) {
            this.logger = logger;
        }

        @Override
        public void debug(String msg) {
            logger.debug(msg);
        }

        @Override
        public void info(String msg) {
            logger.lifecycle(msg);
        }

        @Override
        public void error(String msg, Exception e) {
            logger.error(msg, e);
        }
    }

}
