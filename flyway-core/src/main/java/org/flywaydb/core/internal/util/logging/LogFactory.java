/**
 * Copyright 2010-2014 Axel Fontaine
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flywaydb.core.internal.util.logging;

import org.flywaydb.core.internal.util.FeatureDetector;
import org.flywaydb.core.internal.util.logging.android.AndroidLogCreator;
import org.flywaydb.core.internal.util.logging.apachecommons.ApacheCommonsLogCreator;
import org.flywaydb.core.internal.util.logging.javautil.JavaUtilLogCreator;

/**
 * Factory for loggers.
 */
public class LogFactory {
    /**
     * Factory for implementation-specific loggers.
     */
    private static LogCreator logCreator;

    /**
     * Prevent instantiation.
     */
    private LogFactory() {
        // Do nothing
    }

    /**
     * @param logCreator The factory for implementation-specific loggers.
     */
    public static void setLogCreator(LogCreator logCreator) {
        LogFactory.logCreator = logCreator;
    }

    /**
     * Retrieves the matching logger for this class.
     *
     * @param clazz The class to get the logger for.
     * @return The logger.
     */
    public static Log getLog(Class<?> clazz) {
        if (logCreator == null) {
            FeatureDetector featureDetector = new FeatureDetector(Thread.currentThread().getContextClassLoader());
            if (featureDetector.isAndroidAvailable()) {
                logCreator = new AndroidLogCreator();
            } else if (featureDetector.isApacheCommonsLoggingAvailable()) {
                logCreator = new ApacheCommonsLogCreator();
            } else {
                logCreator = new JavaUtilLogCreator();
            }
        }

        return logCreator.createLogger(clazz);
    }
}
