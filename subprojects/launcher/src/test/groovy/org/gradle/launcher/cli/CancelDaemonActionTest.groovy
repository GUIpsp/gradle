/*
 * Copyright 2014 the original author or authors.
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
package org.gradle.launcher.cli

import org.gradle.launcher.daemon.client.DaemonClient
import spock.lang.Specification

class CancelDaemonActionTest extends Specification {
    final DaemonClient client = Mock()
    final Object toCancel = Mock()
    final CancelDaemonAction action = new CancelDaemonAction(client, toCancel)

    def executesCancelCommand() {
        when:
        action.run()

        then:
        1 * client.cancelBuild(toCancel)
        0 * _._
    }
}
