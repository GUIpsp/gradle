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

package org.gradle.api.internal.artifacts;

import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.artifacts.component.ModuleComponentSelector;
import org.gradle.api.internal.artifacts.component.DefaultModuleComponentSelector;
import org.gradle.api.internal.artifacts.metadata.DependencyMetaData;

public class DefaultComponentSelection implements ComponentSelectionInternal {
    private DependencyMetaData metadata;
    private ModuleComponentSelector requested;
    private ModuleComponentIdentifier candidate;
    private boolean rejected;
    private String rejectionReason;

    public DefaultComponentSelection(DependencyMetaData metadata, ModuleComponentIdentifier candidate) {
        this.metadata = metadata;
        this.candidate = candidate;
        this.requested = DefaultModuleComponentSelector.newSelector(metadata.getRequested());
    }

    public ModuleComponentSelector getRequested() {
        return requested;
    }

    public ModuleComponentIdentifier getCandidate() {
        return candidate;
    }

    public void reject(String reason) {
        rejected = true;
        rejectionReason = reason;
    }

    public boolean isRejected() {
        return rejected;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public DependencyMetaData getDependencyMetaData() {
        return metadata;
    }
}
