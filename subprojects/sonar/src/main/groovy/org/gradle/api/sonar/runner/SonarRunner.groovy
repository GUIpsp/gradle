/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.gradle.api.sonar.runner
import org.gradle.api.DefaultTask
import org.gradle.api.Incubating
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.process.JavaForkOptions
import org.gradle.process.internal.DefaultJavaForkOptions
import org.gradle.process.internal.JavaExecHandleBuilder

import javax.inject.Inject
/**
 * Analyses one or more projects with the <a href="http://docs.codehaus.org/display/SONAR/Analyzing+with+Sonar+Runner">
 * Sonar Runner</a>. Can be used with or without the {@code sonar-runner} plugin. If used together with the plugin,
 * {@code sonarProperties} will be populated with defaults based on Gradle's object model and user-defined
 * values configured via {@link SonarRunnerExtension} and {@link SonarRunnerRootExtension}. If used without the plugin,
 * all properties have to be configured manually.
 *
 * <p>For more information on how to configure the Sonar Runner, and on which properties are available, see the
 * <a href="http://docs.codehaus.org/display/SONAR/Analyzing+with+Sonar+Runner">Sonar Runner documentation</a>.
 */
@Incubating
class SonarRunner extends DefaultTask {
    private static final Logger LOGGER = Logging.getLogger(SonarRunner)

    /**
     * The String key/value pairs to be passed to the Sonar Runner. {@code null} values are not permitted.
     */
    @InputFiles
    Properties sonarProperties

    /**
     * Options for the analysis process. Configured via {@link SonarRunnerRootExtension#forkOptions}.
     */
    JavaForkOptions forkOptions

    SonarRunner() {
        forkOptions = new DefaultJavaForkOptions(getFileResolver())
    }

    @TaskAction
    void run() {
        prepareExec()
                .build()
                .start()
                .waitForFinish()
                .assertNormalExitValue();
    }

    JavaExecHandleBuilder prepareExec() {
        def properties = getSonarProperties()

        if (LOGGER.infoEnabled) {
            LOGGER.info("Executing Sonar Runner with properties:\n{}",
                    properties.sort().collect { key, value -> "$key: $value" }.join("\n"))
        }

        def javaExec = new JavaExecHandleBuilder(getFileResolver())
        forkOptions.copyTo(javaExec)

        javaExec.setClasspath(
                project.configurations[SonarRunnerPlugin.SONAR_RUNNER_CONFIGURATION_NAME]
        )

        javaExec.setMain('org.sonar.runner.Main')
        javaExec.systemProperties(properties)
    }

    @Inject
    protected FileResolver getFileResolver() {
        throw new UnsupportedOperationException();
    }
}
