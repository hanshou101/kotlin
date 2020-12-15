/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.descriptors.commonizer

import org.jetbrains.kotlin.commonizer.api.CommonizerTarget
import org.jetbrains.kotlin.commonizer.api.LeafCommonizerTarget
import org.jetbrains.kotlin.commonizer.api.SharedCommonizerTarget
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import java.io.File

sealed class Result {
    object NothingToCommonize : Result()

    class Commonized(
        val modulesByTargets: Map<CommonizerTarget, Collection<ModuleResult>>
    ) : Result() {
        val sharedTarget: SharedCommonizerTarget by lazy { modulesByTargets.keys.filterIsInstance<SharedCommonizerTarget>().single() }
        val leafTargets: Set<LeafCommonizerTarget> by lazy { modulesByTargets.keys.filterIsInstance<LeafCommonizerTarget>().toSet() }
    }
}

sealed class ModuleResult {
    class Absent(val originalLocation: File) : ModuleResult()
    class Commonized(val module: ModuleDescriptor) : ModuleResult()
}
