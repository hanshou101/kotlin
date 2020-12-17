/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.commonizer.api

import org.jetbrains.kotlin.konan.target.KonanTarget
import org.jetbrains.kotlin.konan.target.KonanTarget.*
import kotlin.test.Test
import kotlin.test.assertEquals


class CommonizerTargetIdentityStringTest {

    @Test
    fun leafTargets() {
        KonanTarget.predefinedTargets.values.forEach { konanTarget ->
            assertEquals(konanTarget.name, CommonizerTarget(konanTarget).identityString)
            assertEquals(CommonizerTarget(konanTarget), parseCommonizerTarget(CommonizerTarget(konanTarget).identityString))
        }
    }

    @Test
    fun `simple shared targets are invariant under konanTarget order`() {
        val macosFirst = CommonizerTarget(MACOS_X64, LINUX_X64)
        val linuxFirst = CommonizerTarget(LINUX_X64, MACOS_X64)

        assertEquals(macosFirst, linuxFirst)
        assertEquals(macosFirst.identityString, linuxFirst.identityString)
        assertEquals(linuxFirst, parseCommonizerTarget(linuxFirst.identityString))
        assertEquals(macosFirst, parseCommonizerTarget(macosFirst.identityString))
        assertEquals(macosFirst, parseCommonizerTarget(linuxFirst.identityString))
        assertEquals(linuxFirst, parseCommonizerTarget(macosFirst.identityString))
    }

    @Test
    fun `hierarchical commonizer targets`() {
        val hierarchy = SharedCommonizerTarget(
            CommonizerTarget(LINUX_X64, MACOS_X64),
            CommonizerTarget(IOS_ARM64, IOS_X64)
        )
        assertEquals(setOf(LINUX_X64, MACOS_X64, IOS_ARM64, IOS_X64), hierarchy.konanTargets)
        assertEquals(hierarchy, parseCommonizerTarget(hierarchy.identityString))
    }

}
